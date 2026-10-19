import request from "@/utils/drugAdministration/request";

/** 药品有效期数据查询分页 */
export function storeBatchInfoListWithPageFilter<T>(data: object): Promise<T> {
  return request.post<T>("/his/medicine/getDrugAndStoreBatchInfoListWithPageFilter", data);
}

/** 药品有效期的导出数据查询 --查询所有数据*/
export function storeBatchInfoList<T>(data: object): Promise<T> {
  return request.post<T>("/his/medicine/getDrugAndStoreBatchInfoList", data);
}

/** 药品有效期修改  */
export function updateStoreBatchInfo<T>(data: object): Promise<T> {
  return request.post<T>("/his/medicine/updateDrugAndStoreBatchInfo", data);
}
