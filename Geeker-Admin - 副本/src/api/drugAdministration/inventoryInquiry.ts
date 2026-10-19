import request from "@/utils/drugAdministration/request";

/** 查询明细表数据 */
export function btnQueryDetail<T>(
  orgaid: string, //机构id
  deptId: string, //部门ID
  drugId: string, //药品ID
  isZerroDrug: boolean //是否包含零库存
): Promise<T> {
  return request.post<T>("/his/medicine/btnQueryDetail", {
    orgaid,
    deptId,
    drugId,
    isZerroDrug
  });
}
/** 查询主表数据 */
export function btnQueryMaster<T>(
  orgaId: string, //机构id
  sort: string, //排序（正序：true  降序 false）
  drugStoreInputCode: string, //药品输入码
  deptId: string, //部门ID
  catalogID: string, //药品分类ID
  flag: boolean, //是否包含零库存
  lowFlag: boolean, //是否到达下线库存（true/false）
  pageSize: number, //分页条数
  pageIndex: number //页码
): Promise<T> {
  return request.post<T>("/his/medicine/btnQueryMaster", {
    orgaId,
    sort,
    drugStoreInputCode,
    deptId,
    catalogID,
    flag,
    lowFlag,
    pageSize,
    pageIndex
  });
}
// /** 删除明细表记录 */
// export function cleanStoreBatchInfo<T>(
//   bathId: string, //明细数据ID
//   orgadrugId: string, //机构药品ID
//   deptId: string, //部门ID
//   orgaId: string, //机构ID
//   drugId: string, //药品ID
//   batch: string, // 批次
//   isStock: string, //是否药库（是/否）
//   isDrug: string, // 是否药库（是/否）
//   userId: string, // 操作员ID
//   batchCreateDate: string, //批次创建日期
//   bNumber: string, // 药库可用库存
//   priceNumber: string, // 散装数
//   realNumber: string, // 药房可用库存
//   virtualInventory: string // 待发数
// ): Promise<T> {
//   return request.post<T>("/his/medicine/cleanStoreBatchInfo", {
//     bathId,
//     orgadrugId,
//     deptId,
//     orgaId,
//     drugId,
//     batch,
//     isStock,
//     isDrug,
//     userId,
//     batchCreateDate,
//     bNumber,
//     priceNumber,
//     realNumber,
//     virtualInventory
//   });
// }

// /** 导出数据 */
// export function searchExcelData<T>(
//   orgaid: string, //机构ID
//   deptId: string, //部门ID
//   sCatalogID: string, //药品分类ID
//   sDrugcode: string, //药品输入码
//   flag: boolean, //是否包含零库存（true/false）
//   addRateNum: string, // 零差率 全部-1
//   lowflag: boolean, //是否到达下线库存（true/false）
//   excelresult: string, // 导出类型（1：库存总量/2.库存批次）
//   isStock: string, // 是否药库
//   isDrug: string //是否药房
// ): Promise<T> {
//   return request.post<T>("/his/medicine/searchExcelData", {
//     orgaid,
//     deptId,
//     sCatalogID,
//     sDrugcode,
//     flag,
//     addRateNum,
//     lowflag,
//     excelresult,
//     isStock,
//     isDrug
//   });
// }
