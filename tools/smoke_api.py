import json, urllib.request, urllib.error, pathlib, sys, http.cookiejar
root=pathlib.Path(__file__).resolve().parents[1]
cfg=json.loads((root/"private/local-config.json").read_text())
base="http://127.0.0.1:4173/api"
results=[]
if cfg.get("DATA_MODE") == "existing_business_copy":
 import runpy
 runpy.run_path(str(root/"tools/check_existing_data.py"),run_name="__main__")
 raise SystemExit(0)

def call(path,data=None,token=None,method=None):
    headers={"Content-Type":"application/json"}
    if token: headers["Token"]=token
    req=urllib.request.Request(base+path,data=None if data is None else json.dumps(data).encode(),headers=headers,method=method)
    try: response=urllib.request.urlopen(req,timeout=20)
    except urllib.error.HTTPError as e: response=e
    raw=response.read()
    try: body=json.loads(raw)
    except ValueError: body={"raw_bytes":len(raw)}
    return response.status,body,response.headers
def check(name,condition):
    results.append({"check":name,"passed":bool(condition)})
    if not condition: print("FAILED:",name)
for role in ("admin","user"):
    status,body,headers=call("/user/login",{"username":"demo_"+role,"password":cfg["DEMO_"+role.upper()+"_PASSWORD"]})
    check(role+" login",status==200 and body.get("code")=="200")
    check(role+" password omitted","password" not in json.dumps(body))
    check(role+" cookie", "HttpOnly" in headers.get("Set-Cookie","") and "SameSite=Strict" in headers.get("Set-Cookie",""))
    if body.get("code")!="200": print(body); continue
    token=body["data"]["token"]
    status,body,_=call("/user/selectAll",token=token)
    check(role+" user administration",status==(200 if role=="admin" else 403))
    if role=="user": user_token=token
status,body,_=call("/user/selectAll");check("anonymous denied",status==401)
status,body,_=call("/user/selectAll",token="forged");check("forged token denied",status==401)
for path,method in (("/user/register","POST"),("/user/password","PUT")):
    status,body,_=call(path,{},method=method);check(path+" disabled",status==403)
status,body,_=call("/user/login",{"username":"demo_user","password":"wrong-password"});check("wrong password denied",body.get("code")!="200")
status,body,_=call("/his/medicine/getMediCatalogInfoList",{"txtString":""},user_token);check("catalog query",status==200 and body.get("code")=="200" and len(body.get("data",[]))==1)
status,body,_=call("/his/medicine/getOrgaDrugInfoListWithPageFilter",{"pageSize":10,"pageIndex":1,"catalogId":"DEMO-CAT-01","orgaId":"0521","drugInputCode":"","isDJ":False},user_token);check("three virtual drugs",body.get("code")=="200" and len(body.get("data",{}).get("queryOrgaDrugInfo",[]))==3)
(root/"private/api-smoke.json").write_text(json.dumps(results,ensure_ascii=False,indent=2),encoding="utf-8")
print("API checks:",sum(x["passed"] for x in results),"/",len(results))
sys.exit(0 if all(x["passed"] for x in results) else 1)
