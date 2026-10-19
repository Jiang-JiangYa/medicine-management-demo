"""Local static preview plus same-origin API proxy; production uses Nginx."""
import http.server, http.client, pathlib, argparse
ROOT = pathlib.Path(__file__).resolve().parents[1]
class Gateway(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(ROOT / "Geeker-Admin - 副本" / "dist"), **kwargs)
    def proxy(self):
        conn = http.client.HTTPConnection("127.0.0.1", 8081, timeout=30)
        headers = {k:v for k,v in self.headers.items() if k.lower() not in ("host", "connection", "x-forwarded-prefix", "x-forwarded-proto", "x-forwarded-host")}
        headers.update({"X-Forwarded-Proto":"http", "X-Forwarded-Host":self.headers.get("Host","127.0.0.1:4173"), "X-Forwarded-Prefix":"/api"})
        body = self.rfile.read(int(self.headers.get("Content-Length", "0")))
        try:
            conn.request(self.command, self.path[4:] or "/", body, headers)
            response=conn.getresponse(); data=response.read(); self.send_response(response.status)
            for k,v in response.getheaders():
                if k.lower() not in ("connection", "transfer-encoding", "content-length"): self.send_header(k,v)
            self.send_header("Content-Length",str(len(data))); self.end_headers()
            if self.command != "HEAD": self.wfile.write(data)
        except (OSError, http.client.HTTPException): self.send_error(502,"Backend unavailable")
        finally: conn.close()
    def do_GET(self):
        if self.path.startswith("/api/"): return self.proxy()
        super().do_GET()
    def do_HEAD(self):
        if self.path.startswith("/api/"): return self.proxy()
        super().do_HEAD()
    def do_POST(self): self.proxy() if self.path.startswith("/api/") else self.send_error(404)
    do_PUT=do_POST
    do_DELETE=do_POST
if __name__ == "__main__":
    parser=argparse.ArgumentParser(); parser.add_argument("--host",default="127.0.0.1"); args=parser.parse_args()
    http.server.ThreadingHTTPServer((args.host,4173),Gateway).serve_forever()
