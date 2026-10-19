////药品出库管理API

import request from "@/utils/drugAdministration/request";

/** 加载主表数据 */
export function getOutStoreBillMasterList<T>(
  dateType: string, //日期类型
  startDate: string, //开始时间
  endDate: string, //结束时间
  orgaid: string, //机构id
  deptId: string, //部门id
  executor: string, //审核人id
  status: string //是/否 （未审核的按钮 是否勾选）
): Promise<T> {
  return request.post<T>("/his/medicine/getOutStoreBillMasterList", {
    dateType,
    startDate,
    endDate,
    orgaid,
    deptId,
    executor,
    status
  });
}

/** 加载明细数据 */
export function getOutStoreBillDetailListWithDrawal<T>(
  outStoreBillId: string, //主表返回的id
  orgaid: string //机构id
  // isGetInStorePrice: boolean, //固定参数
  // cmbpxfs: string //排序方式 0（默认排序） 1（药品代码）2（药品名称）
): Promise<T> {
  return request.post<T>("/his/medicine/getOutStoreBillDetailListWithDrawal", {
    outStoreBillId,
    orgaid
    // isGetInStorePrice,
    // cmbpxfs
  });
}

/** 保存出库单   主表&明细表 */
export function saveOutStoreBill<T>(
  isDrugStore: string, //是否为药库
  MediOutStoreBillMaster: object, //主表数据
  DefineOutStoreBillDetailInfoWithDrawal: object //详细表数据
): Promise<T> {
  return request.post<T>("/his/medicine/saveOutStoreBill", {
    isDrugStore,
    MediOutStoreBillMaster,
    DefineOutStoreBillDetailInfoWithDrawal
  });
}

/** 审核出库单 */
export function checkOutStoreBill<T>(
  userId: string, //操作员id
  stockFlag: string, //系统设置 药品一栏的 药库标志
  isContinue: string, //是/否 （药品批次不同，是否继续执行）
  orgaid: string, //机构id
  deptId: string, //部门id
  outStoreBillId: string //出库单主表id
): Promise<T> {
  return request.post<T>("/his/medicine/checkOutStoreBill", {
    userId,
    stockFlag,
    isContinue,
    orgaid,
    deptId,
    outStoreBillId
  });
}

/** 作废出库单 */
export function wasterOutStoreBill<T>(
  orgaid: string, //机构id
  deptId: string, //部门id
  outStoreBillId: string,
  operatorId: string
): Promise<T> {
  return request.post<T>("/his/medicine/wasterOutStoreBill", {
    orgaid,
    deptId,
    outStoreBillId,
    operatorId
  });
}

/** 删除明细表 */
export function deleteOutStoreBillDetail<T>(
  orgaid: string, //机构id
  deptId: string,
  outStoreBillId: string,
  detailId: string,
  operatorId: string
): Promise<T> {
  return request.post<T>("/his/medicine/deleteOutStoreBillDetail", {
    orgaid,
    deptId,
    outStoreBillId,
    detailId,
    operatorId
  });
}
