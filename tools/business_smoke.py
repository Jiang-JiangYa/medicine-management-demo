"""Creates disposable virtual bills only in the configured local demo application."""
import json, pathlib, urllib.request, urllib.error, datetime, sys
root=pathlib.Path(__file__).resolve().parents[1]; cfg=json.loads((root/"private/local-config.json").read_text()); results=[]
if cfg.get("DATA_MODE") == "existing_business_copy": raise SystemExit("当前使用原毕设业务数据副本，已禁用会修改虚拟单据/批次的测试脚本")

def call(name,data,token=None):
 req=urllib.request.Request("http://127.0.0.1:4173/api/"+name,json.dumps(data).encode(),{"Content-Type":"application/json",**({"Token":token} if token else {})})
 try: raw=urllib.request.urlopen(req,timeout=20).read()
 except urllib.error.HTTPError as e: raw=e.read()
 body=json.loads(raw)
 if body.get("code")!="200": raise AssertionError(name+": "+str(body))
 return body.get("data")
user=call("user/login",{"username":"demo_user","password":cfg["DEMO_USER_PASSWORD"]}); token=user["token"]
def api(name,data): return call("his/medicine/"+name,data,token)
def record(name): results.append(name); print("PASS:",name)
def stocks(): return api("btnQueryMaster",{"pageSize":100,"pageIndex":1,"catalogID":"","drugStoreInputCode":"","flag":True,"lowFlag":False,"sort":False})["DefineStockBillStockQueryInfo"]
def stock(): return next((int(x["inventory"]) for x in stocks() if x["drugId"].strip()=="DEMO-DRUG-01"),0)
now=datetime.datetime.now(); marker="demo-"+now.strftime("%Y%m%d%H%M%S")
master={"id":"","orgaId":"0521","deptId":"101","deptCode":"101","deptName":"演示药库","status":"未审核","isActive":"在用","supplyName":"虚拟供应商","supplyCode":"DEMO-SUP","supplierId":"DEMO-SUP","comment":marker}
detail={"id":"","orgaId":"0521","drugId":"DEMO-DRUG-01","catalogId":"DEMO-CAT-01","drugCode":"DEMO001","drugName":"虚拟演示药品1","drugInputCode":"YSYP1","number":100,"packagQuantity":1,"unitId":"BOX","unitName":"盒","isActive":"在用","status":"未审核","batchNumber":marker,"purchasePrice":1,"retailPrice":2,"purchaseMoney":100,"retailMoney":200,"validityPeriod":(now+datetime.timedelta(days=365)).strftime("%Y-%m-%d %H:%M:%S"),"produceDate":now.strftime("%Y-%m-%d %H:%M:%S")}
try:
 before=stock()
 api("saveInStoreBillWithDrawal",{"mediInStoreBillMaster":master,"defineInStoreBillDetailInfoWithDrawal":[detail],"isdefineInStoreBillDetailInfoWithDrawal":[]});record("save virtual inbound bill")
 bills=api("getInStoreBillMasterList",{"isInputOrCheckDate":False,"startDate":"","endDate":"","checkId":"","inStoreTypeId":False,"isChecked":False,"supplierInfo":""})["DefineInStoreBillMasterInfo"]
 bill=next(x for x in bills if x.get("comment")==marker)
 api("checkInStoreBill",{"inStoreBillId":bill["id"],"checkId":str(user["id"]),"orgaId":"0521","deptId":"101"})
 assert stock()==before+100;record("inbound audit adds 100 inventory")
 ds=api("getInStoreBillDetailListWithDrawal",{"inStoreBillId":bill["id"]})["DefineInStoreBillDetailInfoWithDrawal"];batch=ds[0]["batch"]
 od={**detail,"id":"","number":20,"batch":batch,"purchaseMoney":20,"retailMoney":40}
 om={**master,"id":"","relativeDeptId":"102","receivingDeptName":"演示药房","comment_":marker,"outStoreMemo":marker}
 api("saveOutStoreBill",{"MediOutStoreBillMaster":om,"DefineOutStoreBillDetailInfoWithDrawal":[od]});record("save virtual outbound bill")
 obs=api("getOutStoreBillMasterList",{"dateType":"1","startDate":"","endDate":"","executor":"","status":"是"})["DefineOutStoreBillMasterInfo"]
 ob=next(x for x in obs if x.get("outStoreMemo")==marker)
 api("checkOutStoreBill",{"outStoreBillId":ob["id"],"userId":str(user["id"]),"orgaid":"0521"});assert stock()==before+80;record("outbound audit subtracts 20 inventory")
 rd={**detail,"id":"","drugID":"DEMO-DRUG-01","number":10,"batch":batch}
 api("saveRefundingBillMasterNew",{"addeditMode":"Add","defineRefundingBillMasterInfo":{**master,"id":"","comment":marker},"defineRefundingBillDetailInfoWithDrawal":[rd]});record("save virtual return bill")
 rbs=api("getRefundingBillMasterList",{"isInputOrCheckDate":True,"startDate":"","endDate":"","checkID":"","status":"未审核","tbSuppliers":""})["DefineRefundingBillMasterInfo"]
 rb=next(x for x in rbs if x.get("comment")==marker)
 api("checkRefundingBill",{"RefundingBillID":rb["id"],"checkID":str(user["id"]),"orgaId":"0521"});assert stock()==before+70;record("supplier return subtracts 10 inventory")
 def rejected(name,data):
  try: api(name,data)
  except AssertionError: return
  raise AssertionError("Expected rejection: "+name)
 stable=stock()
 rejected("checkInStoreBill",{"inStoreBillId":bill["id"],"checkId":str(user["id"]),"orgaId":"0521","deptId":"101"})
 rejected("checkOutStoreBill",{"outStoreBillId":ob["id"],"userId":str(user["id"]),"orgaid":"0521"})
 rejected("checkRefundingBill",{"RefundingBillID":rb["id"],"checkID":str(user["id"]),"orgaId":"0521"})
 assert stock()==stable;record("duplicate inbound/outbound/return audit rejected without inventory change")
 badmarker=marker+"-bad"
 badmaster={**om,"id":"","outStoreMemo":badmarker}
 gooddetail={**od,"id":"","number":1}
 baddetail={**od,"id":"","number":1,"batchNumber":"nonexistent-batch"}
 api("saveOutStoreBill",{"MediOutStoreBillMaster":badmaster,"DefineOutStoreBillDetailInfoWithDrawal":[gooddetail,baddetail]})
 invalids=api("getOutStoreBillMasterList",{"dateType":"1","startDate":"","endDate":"","executor":"","status":"是"})["DefineOutStoreBillMasterInfo"]
 invalid=next(x for x in invalids if x.get("outStoreMemo")==badmarker)
 rejected("checkOutStoreBill",{"outStoreBillId":invalid["id"],"userId":str(user["id"]),"orgaid":"0521"})
 assert stock()==stable;record("later detail failure rolls back earlier inventory writes")
 expiry=api("getDrugAndStoreBatchInfoListWithPageFilter",{"pageSize":100,"pageIndex":1,"batchNumber":marker,"drugNameOrCode":"","isStock":"是","startDate":"","endDate":""});assert expiry["listCount"]>=1;record("expiry query returns virtual batch")
except Exception as e:
 (root/"private/business-smoke.json").write_text(json.dumps({"passed":results,"error":str(e)},ensure_ascii=False,indent=2),encoding="utf-8");print("FAILED:",e);sys.exit(1)
(root/"private/business-smoke.json").write_text(json.dumps({"passed":results,"inventory_before":before,"inventory_after":stock()},ensure_ascii=False,indent=2),encoding="utf-8")
