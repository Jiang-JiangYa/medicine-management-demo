import json,pathlib,urllib.request,urllib.error
root=pathlib.Path(__file__).resolve().parents[1];cfg=json.loads((root/"private/local-config.json").read_text());base="http://127.0.0.1:4173/api/"
def request(path,data=None,token=""):
 req=urllib.request.Request(base+path,None if data is None else json.dumps(data).encode(),{"Content-Type":"application/json","Token":token})
 try:response=urllib.request.urlopen(req,timeout=20)
 except urllib.error.HTTPError as e:response=e
 return response.status,json.load(response)
status,login=request("user/login",{"username":"demo_user","password":cfg["DEMO_USER_PASSWORD"]});assert status==200 and login["code"]=="200";assert "password" not in login["data"];token=login["data"]["token"]
def api(name,data):
 status,body=request("his/medicine/"+name,data,token);assert status==200 and body["code"]=="200",(name,body);return body["data"]
categories=api("getMediCatalogInfoList",{"txtString":""});assert len(categories)==19
count=0
for category in categories:
 status,body=request("his/medicine/getOrgaDrugInfoListWithPageFilter",{"pageSize":100,"pageIndex":1,"catalogId":category["id"],"orgaId":"0521","drugInputCode":"","isDJ":False},token)
 assert status==200
 if body["code"]=="200":
  count+=int(body["data"]["pages"])
  assert not any(x["drugCode"].startswith("DEMO") for x in body["data"]["queryOrgaDrugInfo"])
filtered_count=count
all_drugs=api("getOrgaDrugInfoListWithPageFilter",{"pageSize":100,"pageIndex":1,"catalogId":"","orgaId":"0521","drugInputCode":"","isDJ":False})
count=int(all_drugs["pages"]);assert count==37
assert not any(x["drugCode"].startswith("DEMO") for x in all_drugs["queryOrgaDrugInfo"])
stocks=api("btnQueryMaster",{"pageSize":100,"pageIndex":1,"catalogID":"","drugStoreInputCode":"","flag":True,"lowFlag":False,"sort":False});assert stocks["resultCount"]==30
batches=api("getDrugAndStoreBatchInfoListWithPageFilter",{"pageSize":100,"pageIndex":1,"batchNumber":"","drugNameOrCode":"","isStock":"是","startDate":"","endDate":""});assert batches["listCount"]==33
status,body=request("user/selectAll",token=token);assert status==403
result={"passed":True,"categories":19,"drugs":count,"warehouse_stock_rows":30,"warehouse_batch_rows":33,"original_users_not_copied":True,"mode":"existing_business_copy"}
(root/"private/existing-data-check.json").write_text(json.dumps(result,ensure_ascii=False,indent=2),encoding="utf-8");print(json.dumps(result,ensure_ascii=True))
