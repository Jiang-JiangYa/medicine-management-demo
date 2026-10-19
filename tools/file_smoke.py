import pathlib,json,urllib.request,urllib.error,struct,zlib,hashlib
root=pathlib.Path(__file__).resolve().parents[1];cfg=json.loads((root/"private/local-config.json").read_text());base="http://127.0.0.1:4173"
login=urllib.request.urlopen(urllib.request.Request(base+"/api/user/login",json.dumps({"username":"demo_user","password":cfg["DEMO_USER_PASSWORD"]}).encode(),{"Content-Type":"application/json"}))
body=json.load(login);token=body["data"]["token"];cookie=login.headers["Set-Cookie"].split(";")[0]
def chunk(t,b):return struct.pack("!I",len(b))+t+b+struct.pack("!I",zlib.crc32(t+b)&0xffffffff)
png=b"\x89PNG\r\n\x1a\n"+chunk(b"IHDR",struct.pack("!2I5B",2,2,8,2,0,0,0))+chunk(b"IDAT",zlib.compress((b"\0"+b"\xff\0\0"*2)*2))+chunk(b"IEND",b"")
def upload(name,content):
 boundary="demo-test-boundary";data=("--"+boundary+'\r\nContent-Disposition: form-data; name="file"; filename="'+name+'"\r\nContent-Type: image/png\r\n\r\n').encode()+content+("\r\n--"+boundary+"--\r\n").encode()
 req=urllib.request.Request(base+"/api/file/upload",data,{"Content-Type":"multipart/form-data; boundary="+boundary,"Token":token})
 try:return json.load(urllib.request.urlopen(req))
 except urllib.error.HTTPError as e:return json.load(e)
res=upload("demo.png",png);assert res["code"]=="200",res
url=res["data"];assert url.startswith("/api/file/download/")
for header in ({"Token":token},{"Cookie":cookie}):
 downloaded=urllib.request.urlopen(urllib.request.Request(base+url,headers=header)).read();assert downloaded==png
for name,content in [("evil.html",b"<script/>"),("fake.png",b"not-an-image"),("big.png",png+b"x"*(5*1024*1024))]:
 assert upload(name,content)["code"]!="200"
try:urllib.request.urlopen(base+url);raise AssertionError("Anonymous download must fail")
except urllib.error.HTTPError as e:assert e.code==401
(root/"private/upload-check.json").write_text(json.dumps({"url":url,"sha256":hashlib.sha256(png).hexdigest(),"passed":True}),encoding="utf-8")
print("PASS: authorized upload/download, cookie image access, anonymous denial, extension/content/size validation")
