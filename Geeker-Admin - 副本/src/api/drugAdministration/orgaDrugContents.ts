//机构药品信息目录接口
/* eslint-disable */
import request from "@/utils/drugAdministration/request";
import { PORT3 } from "../config/servicePort";

/**@description 加载医疗机构 */
export function getOranizationByUser<T>(data: any): Promise<T> {
  return request.post<T>("/his/systemManage/getOranizationByUser", data);
}

//加载药品目录树
export function getMediCatalogInfoList<T>(txtString: string): Promise<T> {
  return request.post<T>("/his/medicine/getMediCatalogInfoList", {
    txtString
  });
}

//查询药品信息加载分页信息
export function getOrgaDrugInfoListWithPageFilter<T>(
  pageSize: string, //数据条数
  pageIndex: string, //页码
  catalogId: string, //药品分类id
  orgaId: string, //机构id
  drugInputCode: string, //药品代码/药品名称/药品医保代码/药品输入码
  isDJ: string //是否冻结药品
): Promise<T> {
  return request.post<T>("/his/medicine/getOrgaDrugInfoListWithPageFilter", {
    pageSize,
    pageIndex,
    catalogId,
    orgaId,
    drugInputCode,
    isDJ
  });
}

//机构药品查询
export function GetOrgaDrugInfoList<T>(
  orgaId: string, //机构id
  drugInputCode: string //药品代码
): Promise<T> {
  return request.post<T>("/his/medicine/GetOrgaDrugInfoList", {
    orgaId, //机构id
    drugInputCode //药品代码
  });
}

//新增机构药品信息
export function addOrgaDrugInfoAndChargeItemPrice<T>(MediOrgaDruginfo: object): Promise<T> {
  return request.post<T>("/his/medicine/addOrgaDrugInfoAndChargeItemPrice", {
    MediOrgaDruginfo
  });
}

//修改机构药品信息
export function UpdataOrgaDrugInfoAndChargeItemPrice<T>(MediOrgaDruginfo: object): Promise<T> {
  return request.post<T>("/his/medicine/updataOrgaDrugInfoAndChargeItemPrice", {
    MediOrgaDruginfo
  });
}

// 通用获取字典数据 获取下拉框值
export function getKeyValueOfListDictKey<T>(data: object): Promise<T> {
  return request.post<T>("/his/systemManage/getKeyValueOfListDictKey", data);
}

// 获取门诊/住院科室
export function getOrgaDept<T>(
  orgaId: string, //机构ID
  isOutpati: boolean, //是否门诊
  isDrugStore: boolean, //是否药房
  isStock: boolean, //是否药库
  isInp: boolean, // 是否住院
  isInpArea: boolean, //是否病区
  isMedicalTech: boolean, //
  isSuppliesStock: boolean, //是否材料库
  isSupplyRoom: boolean //是否供应室
): Promise<T> {
  return request.post<T>("/his/systemManage/getOrgaDept", {
    orgaId,
    isOutpati,
    isDrugStore,
    isStock,
    isInp,
    isInpArea,
    isMedicalTech,
    isSuppliesStock,
    isSupplyRoom
  });
}

//根据药品分类，输入码,代码获取药品信息
export function getDrugInfoListForOrgaDrugByItemClass<T>(
  orgaId: string, //机构id
  classId: string, //药品分类id
  drugId: string, //药品id
  drugCode: string //输入码
): Promise<T> {
  return request.post<T>("/his/medicine/getDrugInfoListForOrgaDrugByItemClass", {
    orgaId,
    classId, //药品分类id
    drugId, //药品id
    drugCode //输入码
  });
}
//获取收费类别
export function getChargeItemClassId<T>(
  orgaId: string //机构id
): Promise<T> {
  return request.post<T>("/his/medicine/getChargeItemClassId", {
    orgaId
  });
}

//查询机构药品--冻结
export function getinsertDrugStoreDJ<T>(
  id: string, //id
  orgaId: string, //机构id
  drugId: string, //药品id
  orgaDrugMemo: string, //药品状态
  drugName: string, //药品名称
  drugCode: string, //药品代码
  deptId: string, //部门id
  deptCode: string, //部门code
  DeptName: string, //部门名称
  createUser: string //创建人
): Promise<T> {
  return request.post<T>("/his/medicine/insertDrugStoreDJ", {
    QueryOrgaDrugInfo: {
      id, //id
      orgaId,
      drugId,
      orgaDrugMemo,
      drugName,
      drugCode,
      deptId,
      deptCode,
      DeptName
    },

    createUser
  });
}

//获取用户配置
export function getUserConfigByGroupFlag<T>(userId: string, orgaId: string, groupFlag: string): Promise<T> {
  return request.post<T>("/his/systemManage/getUserConfigByGroupFlag", {
    userId,
    orgaId,
    groupFlag
  });
}

//1  删除前， 检查是否有库存
export function deleteOrgaDrugCheckStock<T>(orgaId: string, drugId: string): Promise<T> {
  return request.post<T>("/his/medicine/deleteOrgaDrugCheckStock", {
    orgaId,
    drugId
  });
}
//2 删除/启用机构药品
export function updateOrgaDrugInfoIsActive<T>(
  // id: string,
  // orgaId: string,
  // chargeItemID: string,
  // isActive: string,
  // inputDate: string,
  // operatorId: string
  mediOrgaDruginfo: object
): Promise<T> {
  return request.post<T>("/his/medicine/updateOrgaDrugInfoIsActive", {
    // mediOrgaDruginfo: {
    //   id,
    //   orgaId,
    //   chargeItemID,
    //   isActive,
    //   inputDate,
    //   operatorId,
    // },
    mediOrgaDruginfo
  });
}

//查询机构药品目录库存
export function getDrugStoreQuery<T>(QueryOrgaDrugInfo: object): Promise<T> {
  return request.post<T>("/his/medicine/getDrugStoreQuery", {
    QueryOrgaDrugInfo
  });
}

//修改接口 之前获取药品信息匹配
export function GetDrugInfoListForOrgaDrug2<T>(orgaId: string, drugId: string, drugCode: string): Promise<T> {
  return request.post<T>("/his/medicine/getDrugInfoListForOrgaDrug2", {
    orgaId,
    drugId,
    drugCode
  });
}

/**@description 根据医保码获取国家码 */
export function getCountryCodeByInsureCode<T>(data: any): Promise<T> {
  return request.post<T>("/his/medicine/getCountryCodeByInsureCode", data);
}

//处方权限
export function getKeyValue<T>(kvgroups: string): Promise<T> {
  return request.post<T>("/auth/getKeyValue", {
    kvgroups
  });
}

//判断是否匹配云药房目录
export function isMatchInterDrugInfo<T>(countryCode: string): Promise<T> {
  return request.post<T>("/his/medicine/isMatchInterDrugInfo", {
    countryCode
  });
}

export function hz2WBString<T>(
  strText: string //药品名称
): Promise<T> {
  return request.post<T>("/his/systemManage/hz2WBString", {
    strText
  });
}

/**@description 皮试方式 */
export function getClinicItemListSkinType<T>(data: any): Promise<T> {
  return request.post<T>("/auth/getKeyValue", data);
}

//测试接口
export function LoginApi(data: {
  username: string;
  password: string;
}): Promise<{ code: string; msg: string; data: { token: string; username: string; id: string; name: string } }> {
  return request.post(PORT3 + "/login", data);
}
