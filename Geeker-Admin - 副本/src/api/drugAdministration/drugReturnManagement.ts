import request from "@/utils/drugAdministration/request";

/**获取药品退货主单信息 */
export function getRefundingBillMasterList<T>(
  orgaID: string, //机构id
  deptId: string, //部门id
  isInputOrCheckDate: string, //根据时间
  startDate: string, //开始
  endDate: string, //结束
  checkID: string, //审核人
  status: string, //状态
  tbSuppliers: string //供货商
): Promise<T> {
  return request.post<T>("/his/medicine/getRefundingBillMasterList", {
    orgaID, //机构id
    deptId, //部门id
    isInputOrCheckDate, //根据时间
    startDate, //开始
    endDate, //结束
    checkID, //审核人
    status, //状态
    tbSuppliers
  });
}

/**加载明细表中的数据 以及下方红字数据 */
export function getRefundingBillDetailListWithDrawal<T>(
  orgaID: string, //机构id
  id: string,
  refundingBillID: string, //主表返回id
  cmbpxfs: string //排序方式
): Promise<T> {
  return request.post<T>("/his/medicine/getRefundingBillDetailListWithDrawal", {
    orgaID, //机构id
    id,
    refundingBillID, //主表返回id
    cmbpxfs //排序方式
  });
}

/**退货单保存或者修改(主表与明细表) */
export function getsaveRefundingBillMasterNew<T>(
  defineRefundingBillMasterInfo: object, //主表
  defineRefundingBillDetailInfoWithDrawal: object, //明细表
  addeditMode: string //新增/修改
): Promise<T> {
  return request.post<T>("/his/medicine/saveRefundingBillMasterNew", {
    defineRefundingBillMasterInfo, //主表
    defineRefundingBillDetailInfoWithDrawal, //明细表
    addeditMode //新增/修改
  });
}

/**审核退货单 */
export function getcheckRefundingBill<T>(
  orgaId: string, //机构id
  checkID: string, //审核人id
  RefundingBillID: string //退货主表id
): Promise<T> {
  return request.post<T>("/his/medicine/checkRefundingBill", {
    orgaId, //机构id
    checkID, //审核人id
    RefundingBillID //退货主表id
  });
}

/** 删除明细表 */
export function getwasterRefundingBillDetail<T>(
  orgaID: string, //机构id
  refundingBillID: string, //退货主表id
  RefundingBillDetailID: string, //退货明细id
  operatorID: string //操作员id
): Promise<T> {
  return request.post<T>("/his/medicine/wasterRefundingBillDetail", {
    orgaID, //机构id
    refundingBillID, //退货主表id
    RefundingBillDetailID, //退货明细id
    operatorID //操作员id
  });
}

/** 作废退货单（删除主表） */
export function getwasterRefundingBillMaster<T>(
  orgaID: string, //机构id
  deptID: string, //部门id
  refundingBillID: string, //退货主表id
  operatorID: string //操作员id
): Promise<T> {
  return request.post<T>("/his/medicine/wasterRefundingBillMaster", {
    orgaID, //机构id
    deptID, //部门id
    refundingBillID, //退货主表id
    operatorID //操作员id
  });
}

/** 反冲退货单 */
export function getnewReverseRefundingBill<T>(
  orgaID: string, //机构id
  deptID: string, //部门id
  refundingMasterID: string, //退货主表id
  userID: string //操作员id
): Promise<T> {
  return request.post<T>("/his/medicine/newReverseRefundingBill", {
    orgaID, //机构id
    deptID, //部门id
    refundingMasterID, //退货主表id
    userID //操作员id
  });
}

/** 根据药品代码模糊查询药库库存分支表信息 */
export function getDrugInfoInStoreBatchByCode<T>(drugCode: string): Promise<T> {
  return request.post<T>("/his/medicine/getDrugInfoInStoreBatchByCode", {
    drugCode
  });
}
