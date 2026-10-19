import request from "@/utils/drugAdministration/request";

/** 入库单主表查询 */
export function getInStoreBillMasterList<T>(
  orgaId: string, //机构id
  deptId: string, //部门id
  isInputOrCheckDate: string, //录单日期？审核日期？
  startDate: string, //开始时间
  endDate: string, //结束时间
  checkId: string, //审核人
  inStoreTypeId: string, //入库类型
  isChecked: string, //是否审核
  supplierInfo: string
): Promise<T> {
  return request.post<T>("/his/medicine/getInStoreBillMasterList", {
    orgaId,
    deptId,
    isInputOrCheckDate,
    startDate,
    endDate,
    checkId,
    inStoreTypeId,
    isChecked,
    supplierInfo
  });
}
/** 保存入库单, 包括添加和修改 --1 */
export function getaddDrugWithdrawalInfo<T>(
  mediInStoreBillMaster: object,
  defineInStoreBillDetailInfoWithDrawal: object,
  isdefineInStoreBillDetailInfoWithDrawal: object
): Promise<T> {
  return request.post<T>("/his/medicine/saveInStoreBillWithDrawal", {
    mediInStoreBillMaster,
    defineInStoreBillDetailInfoWithDrawal,
    isdefineInStoreBillDetailInfoWithDrawal
  });
}
/** 入库单审核  ---1*/
export function checkInStoreBill<T>(
  orgaId: string, //机构id
  deptId: string, //部门id
  inStoreBillId: string, //入库单主表ID
  checkId: string //审核人id
): Promise<T> {
  return request.post<T>("/his/medicine/checkInStoreBill", {
    orgaId, //机构id
    deptId, //部门id
    inStoreBillId, //入库单主表ID
    checkId //审核人id
  });
}
/** 入库单明细删除 */
export function getwasterInStoreBillDetail<T>(
  orgaId: string,
  deptId: string,
  inStoreBillId: string,
  inStoreBillDetailId: string, //必需
  operatorId: string
): Promise<T> {
  return request.post<T>("/his/medicine/wasterInStoreBillDetail", {
    orgaId,
    deptId,
    inStoreBillId,
    inStoreBillDetailId,
    operatorId
  });
}
/** 获取入库单明细表 */
export function getInStoreBillDetailListWithDrawal<T>(
  orgaId: string, //机构id
  inStoreBillId: string, //主表id
  deptId: string, //部门id
  cmbpxfs: string //排序规则
): Promise<T> {
  return request.post<T>("/his/medicine/getInStoreBillDetailListWithDrawal", {
    orgaId, //机构id
    inStoreBillId, //主表id
    deptId, //部门id
    cmbpxfs //排序规则
  });
}
/** 作废入库单 */
export function getwasterInStoreBill<T>(orgaId: string, deptId: string, inStoreBillId: string, operatorId: string): Promise<T> {
  return request.post<T>("/his/medicine/wasterInStoreBill", {
    orgaId,
    deptId,
    inStoreBillId,
    operatorId
  });
}
/** 根据药品代码查询药品信息 */
export function getDrugInfoByCode<T>(drugCode: string): Promise<T> {
  return request.post<T>("/his/medicine/getDrugInfoByCode", {
    drugCode
  });
}
