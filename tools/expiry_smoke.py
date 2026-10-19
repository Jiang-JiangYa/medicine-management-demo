import json,urllib.request,pathlib,datetime
root=pathlib.Path(__file__).resolve().parents[1];cfg=json.loads((root/"private/local-config.json").read_text());base="http://127.0.0.1:4173/api/"
if cfg.get("DATA_MODE") == "existing_business_copy": raise SystemExit("当前使用原毕设业务数据副本，已禁用会修改虚拟单据/批次的测试脚本")

def call(path,data,token=""):
 return json.load(urllib.request.urlopen(urllib.request.Request(base+path,json.dumps(data).encode(),{"Content-Type":"application/json","Token":token})))
t=call("user/login",{"username":"demo_user","password":cfg["DEMO_USER_PASSWORD"]})["data"]["token"]
def api(path,data):
 result=call("his/medicine/"+path,data,t);assert result["code"]=="200",result;return result["data"]
def query(start="",end=""):
 return api("getDrugAndStoreBatchInfoListWithPageFilter",{"pageSize":100,"pageIndex":1,"batchNumber":"","drugNameOrCode":"DEMO001","isStock":"是","startDate":start,"endDate":end})["MediStockBatchInfo"]
rows=query();assert len(rows)>=3
invalid=call("his/medicine/updateDrugAndStoreBatchInfo",{"isStock":"是","MediStockBatchInfo":{"validityPeriod":"2099-01-01"}},t)
assert invalid["code"]=="400";assert query()==rows
now=datetime.datetime.now().replace(microsecond=0);dates=[now-datetime.timedelta(days=10),now+datetime.timedelta(days=15),now+datetime.timedelta(days=365)]
for row,date in zip(rows[:3],dates):
 api("updateDrugAndStoreBatchInfo",{"isStock":"是","MediStockBatchInfo":{"id":row["id"],"batchNumber":row["batchNumber"],"produceDate":(now-datetime.timedelta(days=60)).strftime("%Y-%m-%d"),"validityPeriod":date.strftime("%Y-%m-%d")}})
ranges=[(now-datetime.timedelta(days=365),now),(now,now+datetime.timedelta(days=30)),(now+datetime.timedelta(days=31),now+datetime.timedelta(days=730))]
for row,(a,b) in zip(rows[:3],ranges):
 assert any(x["id"]==row["id"] for x in query(a.isoformat(),b.isoformat()))
(root/"private/expiry-check.json").write_text(json.dumps({"passed":True,"scenarios":["expired","near_expiry","future"],"blank_id_rejected_without_changes":True}),encoding="utf-8")
print("PASS: expired, near-expiry, future date ranges; blank ID cannot mass-update batches")
