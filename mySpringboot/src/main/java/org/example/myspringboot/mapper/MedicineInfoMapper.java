package org.example.myspringboot.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myspringboot.entity.MediCatalogInfo;
import org.example.myspringboot.entity.User;

import java.util.List;

@Mapper
/*
     药品目录维护服务
 */
public interface MedicineInfoMapper extends BaseMapper<MediCatalogInfo> {
//    List<MediCatalogInfo> getMediCatalogInfoList(String textString);

//    int getOrgaDrugInfoCount(String catalogId, String orgaId, String drugInputCode);
//
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String catalogId, String orgaId, String drugInputCode, Boolean isDJ);
//
//    List<EmployeeUser> getapp_employeeUserList(String orgId);
//
//    List<Recipepresc> getPrescList(String orgaId, String depId);
//
//    int drugStoreDJRecord(String orgaId, String drugId, String id);
//
//    int updateOrgaDrugInfo(String comment, String id);
//
//    int updateDrugStoreInfo(String status, String createUser, String orgaId, String drugId, String orgaDrugId);
//
//    int isPrescriptionMedica(String registId);
//
//    List<PrivateCheckMatInventory> deleteOrgaDrugCheckStock(String orgaId, String drugId);
//
//    void updateInsureRelationBySaveDrugInfo(HashMap map);
//
//    String insertNanDongSonDrug(Map<String, String> map);
//
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoListAll(String catalogId, String orgaId, String drugInputCode);
//
//    List<QueryOrgaDrugInfo> getOrgaDrugInfoList(String orgaId, String drugInputCode);
//
//    List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrugByItemClass(String classId, String drugId, String drugCode,String orgaId);
//
//    String getTBOrgaDrugInfoAll(Map map);
//
//    List<SYSKeyValue> getKeyValueOfListDictKey(String kvGroups);
//
//    List<PrivateCheckIsActive> isInpChargeActive(String orgaId, String id);
//
//    int updateChargeItemPriceIsActive(String orgaId, String id);
//
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoStockQueryWithRealTime(String id, String orgaId, String drugId, String deptid, String orgaDrugID);
//
//    void getStockInfoStockQuery(Map map);
//
//    List<DefineStockBillStockQueryInfo> getStockInfoStockQueryList(Map map);
//
//    void getDrugStoreInfoStockQuery(Map map);
//
//    List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryList(Map map);
//
//    List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryWithFlag(String orgaID, String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum);
//
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListQuery(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag);
//
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoListQueryByIsOutp(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag);
//
//    List<MediStockBatchInfo> getStockBatchInfoListQuery(String orgaID, String deptID, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag);
//
//    List<MediSuppyInfo> getSuppyInfoList(String orgaId, String deptInputCode);
//
//    List<MediDrugStoreInfo> getDrugStoreInfoList(String orgaId, String deptId,List<String>deptIdList, String drugStoreInputCode);
//
//    List<DefineMediDrugStoreFreeze> getFreezeBatchInfo(String id);
//
//    Integer updateFreezeBatchInfo(String id, String orgid, String deptid, String countNumber);
//
//    Integer updateFreezeInfo(String orgadrugid, String deptid, String countNumber);
//
//    Integer insertMedi_DrugStoreFreeze(Map map);
//
//    void bd_GetCleanStockBatchInfo(Map map);
//
//    void bd_CleanDrugStoreBatchInfo(Map map);
//
//    Integer updateFreezeCancelInfo(Map map);
//
//
//    MediDrugStoreBatchInfo selectBatchInfoNumberIsZero2(String id, String orgaid, String deptid);
//
//    Integer updateInfoForNumberIsZero(String orgadrugid, String id, String orgaid, String deptid);
//
//    Integer updateBatchInfoForNumberIsZero(String id, String orgaid, String deptid);
//
//    Integer deleteMedi_DrugStoreFreeze(String id, String orgaid, String deptid, String userId, String userName);
//
//    Integer updateFreezeCancelBatchInfo(String id, String orgaid, String deptid);
//
//    List<DefineMediDrugStoreFreeze> select_DrugStoreFreezestatus(String id, String orgaid, String deptid);
//
//    String getMediStockBatchTotal(Map map);
//
//    List<MediStockBatchInfo> getStockBatchInfo(Map map);
//
//    List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoByIsOutp(Map map);
//
//
//    List<SysOrgaDept> getOrgaDeptAddVillageDept(String orgaId);
//
//    List<StoreBillDetails> getOutStoreBillDetailsCombox(String orgaId, String drugCode);
//
//    List<StoreBillDetails> getInStoreBillDetailsCombox(String orgaId, String drugCode);
//
//    void callInOrOutStoreBillInfoByOutNew(Map map);
//
//    List<DefineGetInOrOutStoreBillInfoByOut> getInOrOutStoreBillInfoByOutNew();
//
//    void callInOrOutStoreBillInfoByOutNewPaging(Map map);
//
//
//    void callInOrOutStoreBillInfoByIn(Map map);
//
//    List<DefineGetInOrOutStoreBillInfoByIn> getInOrOutStoreBillInfoByIn();
//
//    void callGetInOrOutStoreBillInfoByInPaging(Map map);
//
//    List<DefineOutStoreBillMasterInfo> getOutStoreBillMasterList(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status);
//
//    List<DefineStoreBillMasterMoneyCount> getOutStoreBillMasterMoneyCount(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status);
//
//    List<DefineMediStockBatchInfo> getStockBatchInfoListForDrugCode(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//    List<DefineOutStoreBillDetailInfoWithDrawal> getOutStoreBillDetailListWithDrawal(String outStoreBillId, String orgaid, String isGetInStorePrice, String cmbpxfs);
//
//     List<AppRelativeOrganization> getRelativeOrganizationListForAll(String orgaid);
//
//     List<DefineRefundingBillMasterInfo> getRefundingBillMasterList(String orgaID, String deptId, Boolean isInputOrCheckDate, String startDate, String endDate, String checkID, String status, String tbSuppliers);
//    List<DefineRefundingBillDetailInfoWithDrawal> getRefundingBillDetailListWithDrawal(String refundingBillID, String id, String cmbpxfs,String orgaID);
//
//
//    String getPriceUnitIdByDrugId(String drugId);
//
//    String wasterRefundingBillMaster( Map  map);
//    String wasterRefundingBillDetail( Map  map);
//
//    void checkRefundingBill(Map  map);
//
//    void getNanDongDrugIsHave(Map<String, String> map);
//
//    void checkOutStoreBillNew(Map<String, String> map);
//    void newReverseRefundingBill( Map  map);
//
//    String getOutStoreBillMasterStatus(String orgaid, String id);
//
//    void deleteOutStoreBillDetail(Map<String, String> map);
//
//    DefineOutStoreBillMasterInfo getOutStoreBillMasterById(String orgaid, String deptId, String outStoreBillId);
//
//    void wasterOutStoreBill(Map<String, String> map);
//
//    List<DefineStockBatchInfoOutStoreFastAdd> GetStockBatchInfoListForDrugCodeForOneStepOutStore(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower);
//
//    void saveOutStoreBillToAutomatic(Map<String, String> map);
//
//    List<MediStockBatchInfo> getCleanStockBatchInfo(String id, String orgadurgid, String orgaid, String deptid);
//
//    Integer insertCleanStockBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate);
//
//    Integer deleteMedi_StockBatchInfoForClean(String id, String orgadurgid,String orgaid,String deptid);
//
//    List<MediDrugStoreBatchInfo> getCleanDrugStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid);
//
//    Integer insertCleanDrugStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate);
//
//    Integer deleteMedi_DrugStoreBatchInfoForClean(String id, String orgadurgid,String orgaid,String deptid);
//
//    String getCountryCodeByInsureCode(Map<String, String> map);
//    List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrug2(String orgaId, String drugId, String drugCode);
//
//    List<DefineDrugBreakBillMasterInfo> getDrugBreakBillMasterList(String orgaId, String deptId, boolean isInputOrCheckDate, String startDate, String endDate, String checkId);
//    void getStockBatchInfoListForDrugCodeWithBreakProc(Map<String, String> map);
//    List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithBreak();
//    void getDrugStoreBatchInfoListForDrugCodeWithBreakProc(Map<String, String> map);
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListForDrugCodeWithBreak();
//    List<DefineDrugBreakBillDetailInfoWithDrawal> getDrugBreakBillDetailListWithDrawal(String drugBreakBillId, String id);
//    String wasterDrugBreakBillDetail(Map<String, String> map);
//    BigDecimal getBreakAmountByPara(String orgaId, String drugBreakBillId);
//    Integer updateBreakAmount(String breakAmount,String breakType,String orgaId, String drugBreakBillId);
//    String checkDrugBreakBill(Map map);
//    int isExistDeptByDeptId(String deptId);
//    // MediDrugStoreBatchInfo getStoreBatchByBreakWithDrawal(String orgaId,String deptId,String drugId,String orgaDrugId,String batch);
//    MediStockInfo getMediStockInfoByParameters(String deptId,String drugId,String orgaDrugId,String orgaId);
//    boolean updateMediStockInfo(MediStockInfo stockInfo);
//    void getDrugBreakBillPrintMasterByProc(Map<String, String> map);
//    DefineDrugBreakBillPrintMasterInfo getDrugBreakBillPrintMaster();
//    void getDrugBreakBillPrintDetailByProc(Map<String, String> map);
//    List<DefineDrugBreakBillPrintDetailInfo> getDrugBreakBillPrintDetail();
//    void getDrugStoreQuery(Map map);
//    List<MediStockBatchInfo> getStockBatchInfoByDrugBreak(String orgaId,String deptId,String drugId,String orgaDrugId,String batch,String bBatchPara);
//    int updateMediStockBatchInfo(MediStockBatchInfo stockBatchInfo);
//    String wasterDrugBreakBill(Map<String, String> map);
//
//    List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMaster(Map<String, Object> queryMap);
//
//    List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCount(Map<String, Object> queryMap);
//
//    String checkInteragencyOutStoreBill(Map<String, String> map);
//
//    SysOrgaDept getRelativeOrganizationByDept(String deptId);
//
//    List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMasterCS(Map<String, Object> queryMap);
//    List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCountCS(Map<String, Object> queryMap);
//
//    List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithPageFilter(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD,Integer limitNum,Integer offsetNum);
//
//    Integer getStockBatchInfoListForDrugCodeCount(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//    String checkOutStoreBillToCS(Map<String, String> map);
//
//    String checkInteragencyOutStoreBillCS(Map<String, String> map);
//
//    List<SysOrgaDept> getRelativeOrgaDeptIsDrugStore(String orgaId, String relativeDeptId);
//
//    List<DefineMediStockBatchInfoCS> getStockBatchInfoListForDrugCodeCS(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD);
//
//
//    // region【进销存上传服务】
//    void getJXYJDrugReturnInvoice(String orgaId, String startDate, String endDate, String deptId, String billNo, String flag);
//
//    List<DefineJXYJDrugReturnInvoiceInfo> getJXYJDrugReturnInvoiceList();
//
//    void getJXYJDrugInvoiceDetail(String orgaId, String invoiceNumber);
//
//    List<DefineJXYJDrugInvoiceDetailInfo> getJXYJDrugInvoiceDetailList();
//
//    Integer getMediDrugInvoiceJXYJCount(String orgaId, String invoiceNumber);
//
//    Integer updateMediDrugInvoiceJXYJ(String orgaId, String invoiceNumber, String operatorId);
//
//    Integer insertMediDrugInvoiceJXYJ(Map map);
//
//    Integer getMediDrugInvoiceDeatailJXYJCount(String orgaId, String masterId);
//
//    Integer insertMediDrugInvoiceDetailJXYJ(Map map);
//
//    Integer updateMediDrugInvoiceDetailJXYJ(String orgaId, String masterId, String operatorId);
//
//    Integer getMediDrugInvoiceBillJXYJCount(String orgaId, String masterId);
//
//    Integer updateMediDrugInvoiceBillJXYJ(String orgaId, String masterId, String operatorId);
//
//    Integer insertMediDrugInvoiceBillJXYJ(Map map);
//
//    Integer getMediDrugRetuInvoiceJXYJCount(String orgaId, String masterId);
//
//    Integer updateMediDrugRetuInvoiceJXYJ(String orgaId, String masterId, String operatorId);
//
//    Integer insertMediDrugRetuInvoiceJXYJ(Map map);
//
//    void savePurchaseNationDrugInfoList(Map map);
//
//    void getJXYJDrugInvoice(String orgaId, String startDate, String endDate, String invoiceNumber, String deptId, String flag);
//
//    List<DefineJXYJDrugInvoiceInfo> getJXYJDrugInvoiceList();
//
//    void getJXYJDrugInfo(String orgaId, String searchText);
//
//    List<DefineJXYJDrugInfo> getJXYJDrugInfoList();
//
//
//    // endregion
//    List<DefineDrugStoreBatchInfoS> getDrugStoreBatchInfoListForDrugCodeWithStockBatch(String orgaId, String relativeOrgaId, String deptId, String relativeDeptId, String drugCode, String deptFlag);
//
//    Integer checkVillageDrugRefundingBill(String masterId);
//
//    Integer checkVillageDrugRefundingBillDetail(String masterId);
//
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfo(Map<String, String> map);
//
//    void getJXYJDrugReturnInvoiceDetailInfo(String orgaId, String masterId);
//
//    List<DefineJXYJDrugReturnInvoiceDetailInfo> getJXYJDrugReturnInvoiceDetailInfoList();
//
//    List<DefineDrugRefundingBillMasterInfo> getDrugRefundingBillMasterListForVillageCheck(Map<String, Object> queryMap);
//
//    void checkDrugRefundingBillForV(Map<String, String> map);
//    void checkDrugRefundingBillForVNew(Map<String, String> map);
//
//    List<DefineDrugRefundingBillMasterInfoForV> getDrugRefundingBillMasterListForVillage(String orgaId, String deptId, String startDate, String endDate, String checkId, String dateType, String status);
//
//    List<DefineDrugRefundingBillDetailInfoWithDrawalForVillage> getDrugRefundingBillDetailListWithDrawalForVillage(String drugRefundingBillID);
//
//    /**
//     * 获取打印数据主表 存储写入临时表
//     * @param orgaId
//     * @param masterId
//     */
//    void getRefundingBillPrintMasterProc(String orgaId, String masterId);
//
//    /**
//     * 临时表 打印主表数据
//     * @return
//     */
//    List<MediRefundingBillMaster> getRefundingBillMasterPrint();
//    /**
//     * 获取打印数据明细 存储写入临时表
//     * @param orgaId
//     * @param masterId
//     */
//    void getRefundingBillPrintDetailProc(String orgaId, String masterId);
//
//    /**
//     * 临时表 打印明细表数据
//     * @return
//     */
//    List<MediRefundingBillDetail> getRefundingBillDetailPrint();
//
//    void printDrugRefundingBillMaster(String orgaId, String masterId);
//
//    MediDrugRefundingBillMaster getDrugRefundingBillPrintMaster();
//
//    void checkVillageDrugRefundingBillNew(Map<String, String> map);
//
//    SysOrgaDept getOrgaDept(String id);
//    int getDrugStoreBatchExist(Map map);
//
//    List<String> relativeOrgaDrug(String orgaId, String drugId);
//
//    Integer getStockBatchInfoCount(String orgaId, String deptId, String startDate, String endDate, String batchNumber, String drugNameOrCode);
//
//    List<MediStockBatchInfo> getStockBatchInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String orgaId, String deptId, String startDate, String endDate, String batchNumber, String drugNameOrCode);
//
//    Integer getDrugStoreBatchInfoCount(String orgaId, String deptId, String startDate, String endDate, String batchNumber, String drugNameOrCode);
//
//    List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String orgaId, String deptId, String startDate, String endDate, String batchNumber, String drugNameOrCode);
//
//    Integer updateStockBatchInfo(Map map);
//
//    Integer UpdateDrugStoreBatchInfo(Map map);
//
//    List<MediStockInfo> getStockInfoListDiffQuery(String orgaId, String deptId,List<String>deptIdList, String drugStoreInputCode);
//
//    List<DrugStoreFreezeList> getDrugStoreBatchDiffQuery(String orgaId, String deptId, List<String> deptIdList, String includeZeroDrug, String drugStoreInputCode);
//
//    List<DrugStoreFreezeList> getStoreBatchDiffQuery(String orgaId, String deptId, List<String> deptIdList, String includeZeroDrug, String drugStoreInputCode);
//
//    String isMatchInterDrugInfo(String countryCode);
//
//    Page<MediOrgaDrugCataLogInfo> getCataLogDrugInfoList(Page page,String orgaId,String searchText);
//
//    Integer updateOrAddMediInternetDrugLog(String id,String type);
//
//    List<MediInternetDrugLog> selectMediInternetDrugLog(String orgaId,String id, String type);


}


