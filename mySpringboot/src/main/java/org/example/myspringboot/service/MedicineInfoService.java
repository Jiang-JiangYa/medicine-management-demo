package org.example.myspringboot.service;

//import com.alibaba.fastjson2.JSONObject;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.wondersgroup.common.his.entity.*;
//import com.wondersgroup.opchismedicine.entity.*;
//import com.wondersgroup.opchismedicine.entity.AppChargeItemClass;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myspringboot.entity.MediCatalogInfo;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.mapper.MedicineInfoMapper;
import org.example.myspringboot.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
/*
     药品目录维护服务
 */
public interface MedicineInfoService {
    //List<MediCatalogInfo> getMediCatalogInfoList(String textString);



    //    int getOrgaDrugInfoCount(String catalogId, String orgaId, String drugInputCode);
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String catalogId, String orgaId, String  drugInputCode , Boolean isDJ);
//    List<AppClinicItem> getClinicItemListSkinType(String orgaId, String inputCode);
//    List<AppChargeItemClass> getChargeItemClassId(String orgId);
//    List<EmployeeUser> getapp_employeeUserList(String orgId);
//    List<Recipepresc> getPrescList(String orgaId,String depId);
//    boolean insertDrugStoreDJ(QueryOrgaDrugInfo oDrugInfo, String createUser);
//    /*
//    boolean updateOrgaDrugInfo(String comment,String id);
//    */
//
//    int isPrescriptionMedica(String registId);
//    String deleteOrgaDrugCheckStock(String orgaId,String materialId);
//    boolean updateInsureRelationBySaveDrugInfo(String orgaId, String orgadrugId, String operatorId, String insureItemCode);
//    boolean checkItemCodeExits(String itemCode,String orgaId);
//    boolean checkYBItemCodeExits(String itemcode, String itemybcode, String orgaID);
//    String addOrgaDrugInfoAndChargeItemPriceNanDong(MediOrgaDrugInfo odi, AppChargeItemPrice aci, String IsYbItemCodeCheck);
//    int addOrgaDrugInfoAndChargeItemPrice(MediOrgaDrugInfo odi, AppChargeItemPrice aci, String IsYbItemCodeCheck);
//    int updataOrgaDrugInfoAndChargeItemPrice(String olditemybcode, MediOrgaDrugInfo odi, AppChargeItemPrice aci, String isYbItemCodeCheck);
//    boolean checkYBItemCodeUpdateExits(String olditemybcode, String itemcode, String itemybcode, String orgaId);
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoListAll(String catalogId, String orgaId, String drugInputCode);
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoList(String orgaId, String drugInputCode);
//
//    List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrugByItemClass(String classId, String drugId, String drugCode,String orgaId);
//
//    String getTBOrgaDrugInfoAll(String orgaId);
//
//    String updateOrgaDrugInfoIsActive(MediOrgaDrugInfo odi);
//
//
//           // #region 库存查询
//
//    //获取库存查询(药房)
//
//    List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQuery(String orgaID, String drugStoreInputCode,
//                                                                       String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum);
//
//    //Integer getDrugStoreInfoStockQueryCount(Map map);
//
//    List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryWithPageFilter(Integer pageSize, Integer pageIndex,
//                                                                                     String orgaID, String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum, Boolean isKSS , String specialDrugType);
//
//    List<DefineDrugStoreBillStockQueryInfo> newGetDrugStoreInfoStockQueryWithPageFilter(Map map);
//
//    List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryWithFlag(String orgaID,
//                                                                               String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum);
//
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListQuery(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag);
//
//
//
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoListQueryByIsOutp(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag );
//
//
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoStockQueryWithRealTime(String id, String orgaId, String drugId, String deptid, String orgaDrugID);
//
//    Integer getPrescMediCount(String orgaID, String deptID);
//    //获取库存查询(药库)
//    List<DefineStockBillStockQueryInfo> getStockInfoStockQuery(Integer pageSize, Integer pageIndex,
//                                                               String orgaID, String stockInputCode, String deptID, String catalogID, String sCatalogID,
//                                                               Boolean flag, Boolean lowFlag, Boolean isCount, Integer addRateNum, Boolean isKSS, String specialDrugType);
//
//    // Integer getGetStockInfoStockQueryCount(Map map);
//    List<DefineStockBillStockQueryInfo> newGetStockInfoStockQuery(Map map);
//    List<DefineStockBillStockQueryInfo> getStockInfoStockQueryWithFlag(String orgaID,
//                                                                       String stockInputCode, String deptID, String catalogID, String sCatalogID, Boolean flag, Boolean lowFlag, Integer addRateNum);
//
//    //药库库存明细数据查询
//
//    List<MediStockBatchInfo> getStockBatchInfoListQuery(String orgadID, String deptID, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag);
//
//     //   #endregion
//
//    // #region 入出库查询
//    List<MediSuppyInfo> getSuppyInfoList(String orgaId, String deptInputCode);
//
//    List<SysOrgaDept> getOrgaDeptAddVillageDept(String orgaId);
//
//    List<StoreBillDetails> getOutStoreBillDetailsCombox(String orgaId, String drugCode);
//
//    List<StoreBillDetails> getInStoreBillDetailsCombox(String orgaId, String drugCode);
//
//    List<DefineGetInOrOutStoreBillInfoByOut> getInOrOutStoreBillInfoByOutCount(String deptid, String orgaId, String startDate, String endDate, String supplierId, String stockId, String durgNameOrCode, String selectOrgaId, String sCatalogId, String orgaDrugId);
//
//    List<DefineGetInOrOutStoreBillInfoByOut> getInOrOutStoreBillInfoByOutWithPageFilter(String deptid, String orgaId, String startDate, String endDate, String supplierId, String stockId, String durgNameOrCode, String selectOrgaId, String sCatalogId, String orgaDrugId,Integer limit,Integer offset,String cktk);
//    //   #endregion
//    List<SYSKeyValue> getKeyValueOfListDictKey(String kvGroups);
//    List<MediDrugStoreInfo> getDrugStoreInfoList(String orgaId, String deptId, String drugStoreInputCode);
//    Boolean updateFreezeInfo(Map map);
//
//    String bd_GetCleanStockBatchInfo(Map map);
//    String bd_CleanDrugStoreBatchInfo(Map map);
//    Boolean cleanStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate);
//    Boolean cleanDrugStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate);
//
//    Boolean updateFreezeCancelInfo(Map map);
//    List<DefineMediDrugStoreFreeze> select_DrugStoreFreezestatus(String Id, String orgaid, String deptid);
//    String getMediStockBatchTotal(String orgaID, String deptId, String isYF, String sCatalogID, Integer addRateNum);
//
//    List<MediStockBatchInfo> getStockBatchInfo(Map map);
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoByIsOutp(Map map);
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfo(Map map);
//
//    List<DefineGetInOrOutStoreBillInfoByIn> getInOrOutStoreBillInfoByIn(String orgaId, String startDate, String endDate, String dateType, String sinvoicenumber, String supplierId, String stockId, String durgNameOrCode, String sCatalogId, String orgaDrugId);
//
//    List<DefineGetInOrOutStoreBillInfoByIn> getInOrOutStoreBillInfoByInWithPageFilter(String orgaId, String startDate, String endDate, String dateType, String sinvoicenumber, String supplierId, String stockId, String durgNameOrCode, String sCatalogId, String orgaDrugId, Integer limit, Integer offset, String rkth);
//
//
//    List<DefineOutStoreBillMasterInfo> getOutStoreBillMasterList(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status);
//
//    List<DefineStoreBillMasterMoneyCount> getOutStoreBillMasterMoneyCount(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status);
//
//    List<DefineMediStockBatchInfo> getStockBatchInfoListForDrugCode(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//    List<DefineOutStoreBillDetailInfoWithDrawal> getOutStoreBillDetailListWithDrawal(String outStoreBillId, String orgaid, String isGetInStorePrice, String cmbpxfs);
//    List<AppRelativeOrganization> getRelativeOrganizationListForAll(String orgaid);
//
//    List<DefineRefundingBillMasterInfo> getRefundingBillMasterList(String orgaID,String deptId,Boolean isInputOrCheckDate,String startDate, String endDate, String checkID,String status,String tbSuppliers);
//    List<DefineRefundingBillDetailInfoWithDrawal> getRefundingBillDetailListWithDrawal(String refundingBillID, String id , String cmbpxfs,String orgaID);
//    String saveRefundingBillMasterNew(MediRefundingBillMaster rbm, List<MediRefundingBillDetail> isbdList);
//
//
//    Integer saveOutStoreBill(MediOutStoreBillMaster newMaster, List<MediOutStoreBillDetail> mediOutStoreBillDetails, String isOutpati, String isInp);
//
//
//    String wasterRefundingBillMaster(String orgaID,String deptID,String refundingBillID,String operatorID);
//
//    String wasterRefundingBillDetail(String orgaID,String refundingBillID,String RefundingBillDetailID,String operatorID);
//    String checkRefundingBill(String orgaID,String refundingBillID,String checkID);
//
//    String getPriceUnitIdByDrugId(String drugId);
//
//    String getNanDongDrugIsHave(String orgaid, String outStoreBillId);
//
//    String checkOutStoreBillNew(DefineOutStoreBillMasterInfo outStoreBillMaster);
//
//
//    String saveRefundingBillMaster(MediRefundingBillMaster rbm, List<MediRefundingBillDetail> detailInfoWithDrawals);
//
//    String newReverseRefundingBill(String orgaID, String deptID, String refundingMasterID, String operatorID);
//
//    String getOutStoreBillMasterStatus(String orgaid, String id);
//
//    String deleteOutStoreBillDetail(String orgaid, String deptId, String outStoreBillId, String detailId, String operatorId);
//
//    DefineOutStoreBillMasterInfo getOutStoreBillMasterById(String orgaid, String deptId, String outStoreBillId);
//
//    String wasterOutStoreBill(String orgaid, String deptId, String outStoreBillId, String operatorId);
//
//    List<DefineStockBatchInfoOutStoreFastAdd> GetStockBatchInfoListForDrugCodeForOneStepOutStore(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower);
//
//    String saveOutStoreBillToAutomatic(String orgaid, String deptId, String userId);
//
//    String getCountryCodeByInsureCode(String orgaID, String itemYbCode, String type);
//
//    List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrug2(String orgaId, String drugId, String drugCode);
//
//    //药品破损管理
//    List<DefineDrugBreakBillMasterInfo> getDrugBreakBillMasterList(String orgaId, String deptId, boolean isInputOrCheckDate, String startDate, String endDate, String checkId);
//    void getStockBatchInfoListForDrugCodeWithBreakProc(String orgaId, String deptId,String drugCode);
//    List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithBreak(String orgaId, String deptId,String drugCode);
//    void getDrugStoreBatchInfoListForDrugCodeWithBreakByProc(String orgaId, String deptId,String drugCode);
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListForDrugCodeWithBreak(String orgaId, String deptId,String drugCode);
//    List<DefineDrugBreakBillDetailInfoWithDrawal> getDrugBreakBillDetailListWithDrawal(String drugBreakBillId, String id);
//    boolean  wasterDrugBreakBillDetail(DefineStopDrugBreakBillDetailParamInfo dsisbdpi);
//    boolean updateBreakMoney(String orgaId, String drugBreakBillId,String breakType);
//    int saveDrugBreakBill(boolean IsStock,MediDrugBreakBillMaster dbbm, List<MediDrugBreakBillDetail> dbbdList);
//    Boolean drugAddAndUpdateBatch(List<MediDrugBreakBillDetail> abdAddList, List<MediDrugBreakBillDetail> abdEditList,String breakType);
//    String checkDrugBreakBill(DefineCheckDrugBreakBillParamInfo dcdbbpi);
//    String reverseDrugBreakBill(List<DefineDrugBreakBillDetailInfoWithDrawal> isbdReverseList, String deptId);
//    DefineDrugBreakBillPrint getDrugBreakBillPrintMaster(String orgaId, String drugBreakBillId,String orgaName,String userName);
//    List<DefineDrugBreakDetailPrint> getDrugBreakBillPrintDetail(String orgaId, String drugBreakBillId,String comment,String printOperator);
//    //  #endregion
//    List<DefineDrugStoreQueryInfo> getDrugStoreQuery(QueryOrgaDrugInfo info);
//    boolean wasterDrugBreakBill(String orgaId,String deptId,String  drugBreakBillId,String operatorId);
//
//    List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMaster(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status);
//
//    List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCount(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status);
//
//    String checkInteragencyOutStoreBill(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId,String userId);
//
//    SysOrgaDept getRelativeOrganizationByDept(String deptId);
//
//    List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMasterCS(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status);
//
//    List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCountCS(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status);
//
//    List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithPageFilter(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD,Integer limitNum,Integer offsetNum);
//
//    Integer getStockBatchInfoListForDrugCodeCount(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//    String checkOutStoreBillToCS(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId);
//
//    String checkInteragencyOutStoreBillCS(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId);
//
//    List<SysOrgaDept> getRelativeOrgaDeptIsDrugStore(String orgaId, String relativeDeptId);
//
//    List<DefineMediStockBatchInfoCS> getStockBatchInfoListForDrugCodeCS(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//    // region【进销存上传服务】
//    List<DefineJXYJDrugReturnInvoiceInfo> getJXYJDrugReturnInvoiceList(String orgaId, String startDate, String endDate, String deptId, String billNo, String flag);
//
//    List<DefineJXYJDrugInvoiceDetailInfo> getJXYJDrugInvoiceDetailList(String orgaId, String invoiceNumber);
//
//    boolean updateDrugInvoiceJXYJ(String orgaId, String masterId, String invoiceNumber, String flag, String status, String operatorId);
//
//    ResultObject savePurchaseNationDrugInfoList(List<PurchaseNationDrugInfo> drugResultList);
//
//    List<DefineJXYJDrugInvoiceInfo> getJXYJDrugInvoiceList(String orgaId, String startDate, String endDate, String invoiceNumber, String deptId, String flag);
//
//    List<DefineJXYJDrugInfo> getJXYJDrugInfoList(String orgaId, String searchText);
//
//    // endregion
//
//    List<DefineDrugStoreBatchInfoS> getDrugStoreBatchInfoListForDrugCodeWithStockBatch(String orgaId, String relativeOrgaId, String deptId, String relativeDeptId, String drugCode, String deptFlag);
//
//
//    boolean checkVillageDrugRefundingBill(String orgaId,String deptId,String relativeOrgaId,String relativeDeptId,String masterId,String isDrugStore,String isStock,String operatorId);
//    boolean checkVillageDrugRefundingBillNew(String orgaId,String deptId,String relativeOrgaId,String relativeDeptId,String masterId,String isDrugStore,String isStock,String operatorId);
//
//    boolean saveVDrugRefundingBillMasterNew(String orgaId, DefineDrugRefundingBillMasterInfo masterInfo, List<DefineDrugStoreBatchInfoS> addInfos, List<DefineDrugRefundingBillDetailInfoWithDrawalForVillage> batchInfos, String operatorId);
//
//    List<DefineJXYJDrugReturnInvoiceDetailInfo> getJXYJDrugReturnInvoiceDetailInfo(String orgaId, String masterId);
//
//    List<DefineDrugRefundingBillMasterInfo> getDrugRefundingBillMasterListForVillageCheck(String orgaId, String deptId, String startDate, String endDate, String checkId, String dateType,String requestOrg);
//
//    boolean checkDrugRefundingBillForV(String orgaId, String deptId, String relativeOrgaId, String masterId, String operatorId);
//    boolean checkDrugRefundingBillForVNew(String orgaId, String deptId, String relativeOrgaId, String masterId, String operatorId);
//
//    List<DefineDrugRefundingBillMasterInfoForV> getDrugRefundingBillMasterListForVillage(String orgaId, String deptId, String startDate, String endDate, String checkId, String dateType, String status);
//
//    List<DefineDrugRefundingBillDetailInfoWithDrawalForVillage> getDrugRefundingBillDetailListWithDrawalForVillage(String drugRefundingBillID);
//
//    /**
//     * 退货打印信息
//     * @param orgaId
//     * @param masterId
//     * @param orderStatus
//     * @return
//     */
//    JSONObject getRefundingBillPrintData(String orgaId, String masterId, String orderStatus);
//
//    JSONObject getDrugRefundingBillPrintDataForVillage(String orgaId, String masterId);
//
//    String relativeOrgaDrug(String orgaId, String drugId);
//
//
//    Integer getStockBatchInfoCount(String orgaId , String deptId , String startDate , String endDate , String batchNumber , String drugNameOrCode );
//    List<MediStockBatchInfo> getStockBatchInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String orgaId , String deptId , String startDate , String endDate , String batchNumber , String drugNameOrCode );
//    boolean updateStockBatchInfo(MediStockBatchInfo info);
//
//    Integer getDrugStoreBatchInfoCount(String orgaId , String deptId , String startDate , String endDate , String batchNumber , String drugNameOrCode );
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String orgaId , String deptId , String startDate , String endDate , String batchNumber , String drugNameOrCode );
//    boolean UpdateDrugStoreBatchInfo(MediDrugStoreBatchInfo info);
//
//    List<MediStockInfo> getStockInfoListDiffQuery(String orgaId, String deptId, String drugStoreInputCode);
//
//    List<DrugStoreFreezeList> getDrugStoreBatchDiffQuery(String orgaId, String deptId, String includeZeroDrug, String drugStoreInputCode);
//
//    List<DrugStoreFreezeList> getStoreBatchDiffQuery(String orgaId, String deptId, String includeZeroDrug, String drugStoreInputCode);
//
//    String isMatchInterDrugInfo(String countryCode);
//    List<MediOrgaCataLogInfo> getMediOrgaCataLogInfoList(String orgaId);
//    List<MediOrgaDrugCataLogInfo> getMediOrgaDrugCataLogInfo(String orgaId, String orgaCataLogId,String searchText);
//    Page<MediOrgaDrugCataLogInfo> getCataLogDrugInfoList(String orgaId, String searchText, Integer pageNum, Integer pageSize);
//    Boolean addMediOrgaDrugCataLogInfo(List<MediOrgaDrugCataLogInfo> infos);
//    Boolean delMediOrgaDrugCataLogInfo(List<MediOrgaDrugCataLogInfo> infos);
//    Boolean updateMediOrgaCataLogInfo(MediOrgaCataLogInfo info,Integer status);
//
}