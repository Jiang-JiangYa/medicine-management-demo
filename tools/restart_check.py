import json,pathlib,urllib.request,hashlib
root=pathlib.Path(__file__).resolve().parents[1];cfg=json.loads((root/"private/local-config.json").read_text());saved=json.loads((root/"private/business-smoke.json").read_text());upload=json.loads((root/"private/upload-check.json").read_text());base="http://127.0.0.1:4173"
if cfg.get("DATA_MODE") == "existing_business_copy":
 import runpy
 runpy.run_path(str(root/"tools/check_existing_data.py"),run_name="__main__")
 raise SystemExit(0)

def api(path,data,token=""):
 return json.load(urllib.request.urlopen(urllib.request.Request(base+"/api/"+path,json.dumps(data).encode(),{"Content-Type":"application/json","Token":token})))
token=api("user/login",{"username":"demo_user","password":cfg["DEMO_USER_PASSWORD"]})["data"]["token"]
rows=api("his/medicine/btnQueryMaster",{"pageSize":100,"pageIndex":1,"catalogID":"","drugStoreInputCode":"","flag":True,"lowFlag":False,"sort":False},token)["data"]["DefineStockBillStockQueryInfo"]
actual=next(int(x["inventory"]) for x in rows if x["drugId"].strip()=="DEMO-DRUG-01")
assert actual==saved["inventory_after"],(actual,saved["inventory_after"])
content=urllib.request.urlopen(urllib.request.Request(base+upload["url"],headers={"Token":token})).read()
assert hashlib.sha256(content).hexdigest()==upload["sha256"]
(root/"private/restart-check.json").write_text(json.dumps({"passed":True,"inventory":actual,"file_sha256_unchanged":True}),encoding="utf-8")
print("PASS: application restart preserves inventory and uploaded file; login works")
