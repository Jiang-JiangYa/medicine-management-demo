import request from "@/utils/drugAdministration/request";

/** 分页查询出库表 */
export function getOutStoreBillInfoWithPageFilter<T>(
  deptid: string, //出库相对部门id
  orgaId: string, //机构id
  startDate: string, //开始时间
  endDate: string, //结束时间
  supplierId: string, //供货商id
  // stockId: string, //系统配置里，药品一栏的id
  durgNameOrCode: string, //药品输入框输入的信息
  // selectOrgaId: string, //出库相对机构id
  // sCatalogId: string, //药品分类id
  // orgaDrugId: string, //查出药品信息后，取的机构id
  // cbCk: string, //是否出库
  // cbTk: string, //是否退库
  limit: string, //页面显示条数
  offset: string //页数（第几页）
): Promise<T> {
  return request.post<T>("/his/medicine/getOutStoreBillInfoWithPageFilter", {
    deptid, //出库相对部门id
    orgaId, //机构id
    startDate, //开始时间
    endDate, //结束时间
    supplierId, //供货商id
    // stockId, //系统配置里，药品一栏的id
    durgNameOrCode, //药品输入框输入的信息
    // selectOrgaId, //出库相对机构id
    // sCatalogId, //药品分类id
    // orgaDrugId, //查出药品信息后，取的机构id
    // cbCk, //是否出库
    // cbTk, //是否退库
    limit, //页面显示条数
    offset //页数（第几页）
  });
}

/** 分页查询入库表 */
export function getInStoreBillInfoWithPageFilter<T>(
  orgaId: string, //机构id
  startDate: string, //开始时间
  endDate: string, //结束时间
  dateType: string, //0（查询时间）1（发票时间）
  sinvoicenumber: string, //发票号
  supplierId: string, //供货商id
  // stockId: string, //系统配置里，药品一栏的id
  durgNameOrCode: string, //药品输入框输入的信息
  // sCatalogId: string, //药品分类id
  // orgaDrugId: string, //查出药品信息后，取的机构id
  // cbRk: string, //是否入库
  // cbTh: string, //是否退货
  limit: string, //页面显示条数
  offset: string //页数（第几页）
): Promise<T> {
  return request.post<T>("/his/medicine/getInStoreBillInfoWithPageFilter", {
    orgaId, //机构id
    startDate, //开始时间
    endDate, //结束时间
    dateType, //0（查询时间）1（发票时间）
    sinvoicenumber, //发票号
    supplierId, //供货商id
    // stockId, //系统配置里，药品一栏的id
    durgNameOrCode, //药品输入框输入的信息
    // sCatalogId, //药品分类id
    // orgaDrugId, //查出药品信息后，取的机构id
    // cbRk, //是否入库
    // cbTh, //是否退货
    limit, //页面显示条数
    offset //页数（第几页）
  });
}
