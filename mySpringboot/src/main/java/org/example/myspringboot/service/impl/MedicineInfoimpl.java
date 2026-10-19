package org.example.myspringboot.service.impl;

//import cn.hutool.core.date.DatePattern;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.extra.pinyin.PinyinUtil;
//import com.alibaba.fastjson2.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.wondersgroup.common.feign.AuthFeign;
//import com.wondersgroup.common.his.entity.*;
//import com.wondersgroup.common.his.utils.PublicFunc;
//import com.wondersgroup.common.utils.DateTimeUtils;
//import com.wondersgroup.common.utils.KeyGeneratorUtils;
//import com.wondersgroup.opchismedicine.entity.*;
//import com.wondersgroup.opchismedicine.entity.AppChargeItemClass;
//import com.wondersgroup.opchismedicine.mapper.*;
//import com.wondersgroup.opchismedicine.service.MedicineInfoService;
//import jodd.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myspringboot.entity.MediCatalogInfo;
import org.example.myspringboot.mapper.MedicineInfoMapper;
import org.example.myspringboot.service.MedicineInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
/*
     药品目录维护服务
 */
public class MedicineInfoimpl extends ServiceImpl<MedicineInfoMapper, MediCatalogInfo> {
    private static final Logger log = LoggerFactory.getLogger(MedicineInfoimpl.class);
    @Resource
    private MedicineInfoMapper medicineInfoMapper;
//    @Resource
//    private AppClinicItemMapper clinicItemMapper;
//    @Resource
//    private ChargeItemClassMapper chargeItemClassMapper;
//    @Resource
//    private DrugStoreFreezeOneMapper drugStoreFreezeOneMapper;
//    @Resource
//    private RedisTemplate redisTemplate;
//    @Resource
//    private AppChargeItemPriceMapper chargeItemPriceMapper;
//    @Resource
//    private MediOrgaDruginfoMapper mediOrgaDrugInfoMapper;
//    @Resource
//    private MediRefundingBillDetailMapper mediRefundingBillDetailMapper;
//    @Resource
//    private MediRefundingBillMasterMapper mediRefundingBillMasterMapper;
//    @Resource
//    private PublicFunc publicFunc;
//    @Resource
//    private MediOutStoreBillMasterMapper mediOutStoreBillMasterMapper;
//    @Resource
//    private MediOutStoreBillDetailMapper mediOutStoreBillDetailMapper;
//    @Resource
//    AuthFeign authFeign;
//    @Resource
//    MediDrugBreakBillDetailMapper mediDrugBreakBillDetailMapper;
//    @Resource
//    MediDrugBreakBillMasterMapper mediDrugBreakBillMasterMapper;
//    @Resource
//    MediDrugStoreBatchInfoMapper mediDrugStoreBatchInfoMapper;
//    @Resource
//    MediStockBatchInfoMapper mediStockBatchInfoMapper;
//    @Resource
//    MediDrugStoreInfoMapper mediDrugStoreInfoMapper;
//
//    @Resource
//    MediDrugRefundingBillMasterMapper mediDrugRefundingBillMasterMapper;
//    @Resource
//    MediDrugRefundingBillDetailMapper mediDrugRefundingBillDetailMapper;
//
//    @Resource
//    ReturnStockMapper returnStockMapper;
//
//    @Resource
//    MediInternetDrugLogMapper mediInternetDrugLogMapper;
//
//    @Resource
//    private MediOrgaCataLogInfoMapper mediOrgaCataLogInfoMapper;
//    @Resource
//    private MediOrgaDrugCataLogInfoMapper mediOrgaDrugCataLogInfoMapper;



//    @Override
//    public int getOrgaDrugInfoCount(String catalogId, String orgaId, String drugInputCode) {
//        return medicineInfoMapper.getOrgaDrugInfoCount(catalogId, orgaId, drugInputCode);
//    }
//
//    @Override
//    public List<QueryOrgaDrugInfo> getOrgaDrugInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String catalogId, String orgaId, String drugInputCode, Boolean isDJ) {
//        return medicineInfoMapper.getOrgaDrugInfoListWithPageFilter(limitNum, offsetNum, catalogId, orgaId, drugInputCode, isDJ);
//    }
//
//    @Override
//    public List<AppClinicItem> getClinicItemListSkinType(String orgaId, String inputCode) {
//        QueryWrapper<AppClinicItem> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("orgaId",orgaId);
//        List<AppClinicItem> clinicItemList = clinicItemMapper.selectList(queryWrapper);
//        if (clinicItemList != null && clinicItemList.size() > 0) {
//            if (PublicFunc.StringIsNullOrEmpty(inputCode))
//                clinicItemList = clinicItemList.stream().filter(a ->PublicFunc.IsNullString(a.getItemName()).contains("皮试")).collect(Collectors.toList());
//            else {
//                clinicItemList = clinicItemList.stream().filter(a ->
//                        PublicFunc.IsNullString(a.getItemName()).contains("皮试")
//                        && (PublicFunc.IsNullString(a.getItemCode()).toLowerCase().contains(inputCode.toLowerCase())
//                        || PublicFunc.IsNullString(a.getItemName()).toLowerCase().contains(inputCode.toLowerCase())
//                        || PublicFunc.IsNullString(a.getInputCode()).toLowerCase().contains(inputCode.toLowerCase())
//                        || PublicFunc.IsNullString(a.getInputCode3()).toLowerCase().contains(inputCode.toLowerCase())
//                )).collect(Collectors.toList());
//
//            }
//        }
//        return clinicItemList;
//    }
//
//    @Override
//    public List<AppChargeItemClass> getChargeItemClassId(String orgId) {
//
//        List<AppChargeItemClass> clinicItemList = chargeItemClassMapper.selectList(null);
//
//        if (clinicItemList != null) {
//            return clinicItemList.stream().filter(a -> (
//                            (a.getOrgaId().trim()).equals(orgId)
//                                    || ("0").equals((a.getOrgaId()).trim())
//                    )
//                            && ("在用").equals(a.getIsActive().trim())
//            ).collect(Collectors.toList());
//
//
//        } else
//            return null;
//
//    }
//
//    @Override
//    public List<EmployeeUser> getapp_employeeUserList(String orgId) {
//        return medicineInfoMapper.getapp_employeeUserList(orgId);
//    }
//
//    @Override
//    public List<Recipepresc> getPrescList(String orgaId, String depId) {
//        return medicineInfoMapper.getPrescList(orgaId, depId);
//    }
//
//    @Transactional
//    @Override
//    public boolean insertDrugStoreDJ(QueryOrgaDrugInfo oDrugInfo, String createUser) {
//        int returnNum = 0;
//        if (PublicFunc.IsNullString(oDrugInfo.getOrgaDrugMemo()).equals("冻结")) {
//            returnNum = medicineInfoMapper.drugStoreDJRecord(oDrugInfo.orgaId, oDrugInfo.drugId, oDrugInfo.id);
//
//            if (returnNum > 0) {
//                return false; // vs中 return false;应该return true??? qianlishuang 2023.05.05
//            } else {
//
//                DrugStoreFreezeOne drugInfo = new DrugStoreFreezeOne();
//                drugInfo.setId(UUID.randomUUID().toString());
//                drugInfo.setOrgaId(oDrugInfo.getOrgaId());
//                drugInfo.setStatus(oDrugInfo.getOrgaDrugMemo());
//                drugInfo.setDrugId(oDrugInfo.getDrugId());
//                drugInfo.setOrgaDrugId(oDrugInfo.getId());
//                drugInfo.setDrugCode(oDrugInfo.getDrugCode());
//                drugInfo.setDrugName(oDrugInfo.getDrugName());
//                drugInfo.setComment_("");
//                drugInfo.setCreateUser(createUser);
//                drugInfo.setCreateDate(DateTimeUtils.getCurrentLocalDateTime());
//                drugInfo.setDeptId(oDrugInfo.getDeptID());
//                drugInfo.setDeptCode(oDrugInfo.getDeptCode());
//                drugInfo.setDeptName(oDrugInfo.getDeptName());
//
//                if (drugStoreFreezeOneMapper.insert(drugInfo) > 0) {
//
//                    if (medicineInfoMapper.updateOrgaDrugInfo(oDrugInfo.getOrgaDrugMemo(), oDrugInfo.getId()) > 0)
//                        return true;
//                    else
//                        return false;
//                } else
//                    return false;
//            }
//        } else // getOrgaDrugMemo != ("冻结")
//        {
//            if (medicineInfoMapper.updateDrugStoreInfo(PublicFunc.IsNullString(oDrugInfo.getOrgaDrugMemo()),
//                    createUser, oDrugInfo.getOrgaId(), oDrugInfo.getDrugId(), oDrugInfo.getId()) > 0) {
//
//                if (medicineInfoMapper.updateOrgaDrugInfo("", oDrugInfo.getId()) > 0)
//                    return true;
//                else
//                    return false;
//            } else
//                return false;
//        }
//    }
//
//    /*
//        @Override
//        public Integer updateOrgaDrugInfo(String comment, String id) {
//            return medicineInfoMapper.updateOrgaDrugInfo(PublicFunc.IsNullString(comment), PublicFunc.IsNullString(id));
//        }
//    */
//    @Override
//    public int isPrescriptionMedica(String registId) {
//        return medicineInfoMapper.isPrescriptionMedica(registId);
//    }
//
//    @Override
//    public String deleteOrgaDrugCheckStock(String orgaId, String drugId) {
//        String msgInfo = "";
//
//        List<PrivateCheckMatInventory> orgaDrugCheckList = medicineInfoMapper.deleteOrgaDrugCheckStock(orgaId, drugId);
//
//        if (orgaDrugCheckList != null && orgaDrugCheckList.size() > 0) {
//            for (PrivateCheckMatInventory Drug :
//                    orgaDrugCheckList) {
//                if (Drug.getAmount() > 0 || Drug.getPriceAmount() > 0) {
//                    msgInfo += PublicFunc.IsNullString(Drug.getDept()) + ":" + Drug.getAmount() + PublicFunc.IsNullString(Drug.getUnit());
//                    if (Drug.getPriceAmount() > 0)
//                        msgInfo += Drug.getPriceAmount() + PublicFunc.IsNullString(Drug.getPriceUnit());
//                    msgInfo += "\r\n";
//                }
//
//            }
//
//        }
//        return msgInfo;
//    }
//
//    @Transactional
//    @Override
//    public boolean updateInsureRelationBySaveDrugInfo(String orgaId, String orgadrugId, String operatorId, String insureItemCode) {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("orgaId", orgaId);
//        map.put("orgaDrugId", orgadrugId);
//        map.put("operator", operatorId);
//        map.put("insureItemCode", insureItemCode);
//
//        try {
//            medicineInfoMapper.updateInsureRelationBySaveDrugInfo(map);
//        } catch (Exception ex) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean checkItemCodeExits(String itemCode, String orgaId) {
//        QueryWrapper<AppChargeItemPrice> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("orgaId", orgaId);
//        queryWrapper.eq("itemCode", itemCode);
//        queryWrapper.eq("isActive", "在用");
//
//        List<AppChargeItemPrice> chargeItemList = chargeItemPriceMapper.selectList(queryWrapper);
///*
//        if (chargeItemList != null) {
//            List<AppChargeItemPrice> filterItem = chargeItemList.stream().filter(p -> PublicFunc.IsNullString(p.getOrgaId()).equals(orgaId) && PublicFunc.IsNullString(p.getItemCode()).equals(itemCode.trim()) && PublicFunc.IsNullString(p.getIsActive()).equals("在用")).collect(Collectors.toList());
//
// */
//        if (chargeItemList != null && chargeItemList.size() > 0)
//            return true;
//
//        return false;
//    }
//
//    @Override
//    public boolean checkYBItemCodeExits(String itemcode, String itemybcode, String orgaId) {
//
//        if (itemcode.substring(0, 4).equals("0197") || itemcode.substring(0, 4).equals("0297")) {
//            return false;
//        } else {
//            QueryWrapper<AppChargeItemPrice> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("orgaId", orgaId);
//            queryWrapper.eq("itemCode", PublicFunc.IsNullString(itemybcode));
//            queryWrapper.eq("isActive", "在用");
//
//            List<AppChargeItemPrice> chargeItemList = chargeItemPriceMapper.selectList(queryWrapper);
//
//            if (chargeItemList != null && chargeItemList.size() > 0) {
//                List<AppChargeItemPrice> filterItem = chargeItemList.stream().filter(p -> !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0197")
//                        && !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0297")).collect(Collectors.toList());
//
//
//                if (filterItem != null && filterItem.size() > 0)
//                    return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    @Transactional
//    public String addOrgaDrugInfoAndChargeItemPriceNanDong(MediOrgaDrugInfo odi, AppChargeItemPrice aci, String isybItemCodeCheck) {
//
//        String sReturn = "";
//        try {
//
//            if (checkItemCodeExits(PublicFunc.IsNullString(aci.getItemCode()), PublicFunc.IsNullString(aci.getOrgaId())))
//                return "已存在";
//
//
//            //sys_config参数开关IsYbItemCodeCheck是或者空就进行医保校验，否则跳过
//            if (PublicFunc.StringIsNullOrEmpty(isybItemCodeCheck) || isybItemCodeCheck.equals("是")) {
//                //增加验证判断  机构药品目录 新增或者修改。判断延伸以外药品医保代码是否在本机构存在 ，延伸药品不判断 0197 0297
//                if (checkYBItemCodeExits(aci.getItemCode(), aci.getItemYBCode(), aci.getOrgaId()))
//                    return "医保代码已存在";
//            }
//
//            aci.setId(UUID.randomUUID().toString());
//            aci.setCreateDate(DateTimeUtils.getCurrentLocalDateTime());
//
//            odi.setChargeItemId(aci.getId());
//            odi.setId(UUID.randomUUID().toString());
//            odi.setCreateDate(DateTimeUtils.getCurrentLocalDateTime());
//
//            mediOrgaDrugInfoMapper.insert(odi);//机构药品目录
//            chargeItemPriceMapper.insert(aci);//收费目录
//
//            Map<String, String> map = new HashMap<>();
//            map.put("id", odi.getId());
//            medicineInfoMapper.insertNanDongSonDrug(map);
//            sReturn = (String) map.get("CV_1");
//
//
//        } catch (Exception ex) {
//            sReturn = "保存失败";
//            throw new RuntimeException("数据异常！" + ex.toString());
//        }
//        return sReturn;
//    }
//
//    @Override
//    @Transactional
//    public int addOrgaDrugInfoAndChargeItemPrice(MediOrgaDrugInfo odi, AppChargeItemPrice aci, String isybItemCodeCheck) {
//
//        int iReturn = 0;
//
//        try {
//
//            if (checkItemCodeExits(aci.getItemCode(), aci.getOrgaId()))
//                return -255;
//
//            //sys_config参数开关IsYbItemCodeCheck是或者空就进行医保校验，否则跳过
//            if (PublicFunc.StringIsNullOrEmpty(isybItemCodeCheck) || isybItemCodeCheck.equals("是")) {
//                //增加验证判断  机构药品目录 新增或者修改。判断延伸以外药品医保代码是否在本机构存在 ，延伸药品不判断 0197 0297
//                if (checkYBItemCodeExits(aci.getItemCode(), aci.getItemYBCode(), aci.getOrgaId()))
//                    return -254;
//            }
//
//            aci.setId(UUID.randomUUID().toString());
//            aci.setCreateDate(DateTimeUtils.getCurrentLocalDateTime());
//
//            odi.setChargeItemId(aci.getId());
//            odi.setId(UUID.randomUUID().toString());
//            odi.setCreateDate(DateTimeUtils.getCurrentLocalDateTime());
//
//            //更新云药房记录表 MEDI_INTERNETDRUGLOG
//            if(odi.netDrugstore!=null &&odi.netDrugstore!="")
//            {
//                updateMediInternetDrugLog(odi);
//            }
//
//            mediOrgaDrugInfoMapper.insert(odi);//机构药品目录
//            chargeItemPriceMapper.insert(aci);//收费目录
//
//            iReturn = 0;
//
//        } catch (Exception ex) {
//            iReturn = 2;
//            //iReturn ="数据异常！" + ex.toString();
//        }
//        return iReturn;
//    }
//
//    @Override
//    @Transactional
//    public int updataOrgaDrugInfoAndChargeItemPrice(String olditemybcode, MediOrgaDrugInfo odi, AppChargeItemPrice aci, String isYbItemCodeCheck) {
//        int iResult = 1;
//        if (isYbItemCodeCheck.equals("是") || PublicFunc.StringIsNullOrEmpty(isYbItemCodeCheck)) {
//            //增加验证判断  机构药品目录 新增或者修改。判断延伸以外药品医保代码是否在本机构存在 ，延伸药品不判断 0197 0297
//            if (checkYBItemCodeUpdateExits(olditemybcode, aci.getItemCode(), aci.getItemYBCode(), aci.getOrgaId()))
//                throw new RuntimeException("不能重复添加相同机构药品信息!");
//        }
//        //收费目录
//        String chargeItemId = SaveAppChargeItemPrice(aci);
//        if (PublicFunc.StringIsNullOrEmpty(odi.chargeItemId))
//            odi.chargeItemId = chargeItemId;
//
//        //更新云药房记录表 MEDI_INTERNETDRUGLOG
//        if(odi.netDrugstore!=null &&odi.netDrugstore!="")
//        {
//            updateMediInternetDrugLog(odi);
//        }
//        //更新机构药品信息
//        updateMediOrgaDruginfo(odi);
//        return iResult;
//    }
//
//    private boolean updateMediInternetDrugLog(MediOrgaDrugInfo odi) {
//        //查询
//        QueryWrapper<MediOrgaDrugInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", odi.getId());
//        queryWrapper.eq("isActive", "在用");
//        MediOrgaDrugInfo orgaDrugInfo = mediOrgaDrugInfoMapper.selectOne(queryWrapper);
//        if (orgaDrugInfo!=null && orgaDrugInfo.netDrugstore!=null && !orgaDrugInfo.netDrugstore.isEmpty() && !orgaDrugInfo.netDrugstore.equals(odi.netDrugstore))
//        {
//            if (odi.netDrugstore.equals("1"))
//            {
//                List<MediInternetDrugLog> addDrugList=medicineInfoMapper.selectMediInternetDrugLog(odi.orgaId,odi.id,"1");
//                if(addDrugList!=null && addDrugList.size()>0)
//                {
//                    MediInternetDrugLog addDrug=addDrugList.get(0);
//                    addDrug.setCreateDate(LocalDateTime.now());
//                    if(mediInternetDrugLogMapper.updateById(addDrug)==0)
//                    {
//                        throw new RuntimeException("更新云药房记录表新增失败！");
//                    }
//                }
//                else
//                {
//                    //新增
//                    Integer result=medicineInfoMapper.updateOrAddMediInternetDrugLog(odi.id,"1");
//                    return result>0;
//                }
//
//            }
//            else if (odi.netDrugstore.equals("0"))
//            {
//                //删除
//                List<MediInternetDrugLog> deleteDrugList=medicineInfoMapper.selectMediInternetDrugLog(odi.orgaId,odi.id,"3");
//                if(deleteDrugList!=null && deleteDrugList.size()>0)
//                {
//                    MediInternetDrugLog deleteDrug=deleteDrugList.get(0);
//                    deleteDrug.setCreateDate(LocalDateTime.now());
//                    if(mediInternetDrugLogMapper.updateById(deleteDrug)==0)
//                    {
//                        throw new RuntimeException("更新云药房记录表删除失败！");
//                    }
//                }
//                else
//                {
//                    //新增
//                    Integer result=medicineInfoMapper.updateOrAddMediInternetDrugLog(odi.id,"3");
//                    return result>0;
//                }
//            }
//        }
//        else if ((orgaDrugInfo== null || (orgaDrugInfo!=null && orgaDrugInfo.netDrugstore==null))  & odi.netDrugstore.equals("1"))
//        {
//            //新增
//            Integer result=medicineInfoMapper.updateOrAddMediInternetDrugLog(odi.id,"1");
//            return result>0;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean checkYBItemCodeUpdateExits(String olditemybcode, String itemcode, String itemybcode, String orgaId) {
//        int resultCount = 0;
//        if (PublicFunc.IsNullString(itemcode).substring(0, 4).equals("0197") || PublicFunc.IsNullString(itemcode).substring(0, 4).equals("0297")) {
//            return false;
//        } else {
//
//            QueryWrapper<AppChargeItemPrice> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("orgaId", orgaId);
//            queryWrapper.eq("itemYBCode", itemybcode);
//            queryWrapper.eq("isActive", "在用");
//
//            List<AppChargeItemPrice> chargeItemList = chargeItemPriceMapper.selectList(queryWrapper);
//
//            if (olditemybcode == itemybcode) {
//                List<AppChargeItemPrice> filterItem = chargeItemList.stream().filter(p -> !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0197")
//                        && !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0297")).collect(Collectors.toList());
//
//                if (filterItem.size() == 0 || filterItem.size() == 1) {
//                    return false;
//                } else {
//                    return true;
//                }
//            } else {
//                if (chargeItemList.stream().filter(p -> !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0197")
//                        && !PublicFunc.IsNullString(p.getItemCode()).substring(0, 4).equals("0297")).collect(Collectors.toList()).size() > 0)
//                    return true;
//                else
//                    return false;
//            }
//        }
//
//    }
//
//    // 更新 药品信息
//    public boolean updateMediOrgaDruginfo(MediOrgaDrugInfo odi) {
//        QueryWrapper<MediOrgaDrugInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", odi.getId());
//        MediOrgaDrugInfo drug = mediOrgaDrugInfoMapper.selectOne(queryWrapper);
//        if (drug == null) {
//            return false;
//        } else {
//            //包装校验
//            if (odi.outpPackagQuantity > drug.outpPackagQuantity) {
//                SysOrgaDept orgaDept = medicineInfoMapper.getOrgaDept(odi.outpPerformedDeptId);
//                if (orgaDept == null) {
//                    throw new RuntimeException("未匹配到门诊执行科室！");
//                }
//                if ("是".equals(orgaDept.isOutpati)) { //判小改大判断是否门诊药房
//                    Map map = new HashMap();
//                    map.put("orgaId", odi.orgaId);
//                    map.put("deptId", odi.outpPerformedDeptId);
//                    map.put("orgaDrugId", odi.id);
//                    int retNum = medicineInfoMapper.getDrugStoreBatchExist(map);
//                    if (retNum > 0)
//                        throw new RuntimeException("门诊药房存在库存数据，不允许变更包装量！");
//                }
//            }
//            if ((odi.outpPackagQuantity == drug.stockPackagQuantity &&
//                    !odi.outpPackagUnitName.equals(drug.stockPackagUnitName)))
//                throw new RuntimeException("门诊包装单位维护错误！");
//            if ((odi.stockPackagQuantity % odi.outpPackagQuantity) != 0)
//                throw new RuntimeException("门诊包装量换算错误，不允许有余数！");
//            if (odi.outpPackagQuantity < drug.outpPackagQuantity) { //大改小
//                QueryWrapper batchQw = new QueryWrapper();
//                batchQw.eq("orgaid", odi.orgaId);
//                batchQw.eq("deptid", odi.outpPerformedDeptId);
//                batchQw.eq("orgadrugid", odi.id);
//                List<MediDrugStoreBatchInfo> batchlist = mediDrugStoreBatchInfoMapper.selectList(batchQw);
//                for (MediDrugStoreBatchInfo batchinfo : batchlist) {
//
//                    batchinfo.unitID = odi.outpPackagUnitId;
//                    batchinfo.unitName = odi.outpPackagUnitName;
//                    batchinfo.drugSpecification = odi.outpPackageSpec;
//                    batchinfo.number_ = batchinfo.number_ * (batchinfo.packagQuantity / odi.outpPackagQuantity);
//                    batchinfo.virtualInventory = batchinfo.virtualInventory * (batchinfo.packagQuantity / odi.outpPackagQuantity);
//                    batchinfo.purchasePrice = batchinfo.purchasePrice.divide(new BigDecimal(batchinfo.packagQuantity / odi.outpPackagQuantity));
//                    batchinfo.retailPrice = batchinfo.retailPrice.divide(new BigDecimal(batchinfo.packagQuantity / odi.outpPackagQuantity));
//                    batchinfo.packagQuantity = odi.outpPackagQuantity;
//                    if (mediDrugStoreBatchInfoMapper.updateById(batchinfo) == 0)
//                        throw new RuntimeException("更新库存批次表数据失败！");
//                    QueryWrapper storeQW = new QueryWrapper();
//                    storeQW.eq("orgaid", odi.orgaId);
//                    storeQW.eq("deptid", odi.outpPerformedDeptId);
//                    storeQW.eq("orgadrugid", odi.id);
//                    List<MediDrugStoreInfo> infolist = mediDrugStoreInfoMapper.selectList(batchQw);
//                    for (MediDrugStoreInfo info : infolist) {
//
//                        info.unitId = odi.outpPackagUnitId;
//                        info.unitName = odi.outpPackagUnitName;
//                        info.drugSpecification = odi.outpPackageSpec;
//                        info.inventory = info.inventory * (info.packagQuantity / odi.outpPackagQuantity);
//                        info.virtualInventory = info.virtualInventory * (info.packagQuantity / odi.outpPackagQuantity);
//                        info.purchasePrice = info.purchasePrice.divide(new BigDecimal(info.packagQuantity / odi.outpPackagQuantity));
//                        info.retailPrice = info.retailPrice.divide(new BigDecimal(info.packagQuantity / odi.outpPackagQuantity));
//                        info.packagQuantity = odi.outpPackagQuantity;
//                        if (mediDrugStoreInfoMapper.updateById(info) == 0)
//                            throw new RuntimeException("更新库存表数据失败！");
//                    }
//                }
//            }
//
//            drug.setId(odi.getId());
//            drug.setOrgaId(odi.getOrgaId());
//            drug.setDrugId(odi.getDrugId());
//            drug.setOutpPackagQuantity(odi.getOutpPackagQuantity());
//            drug.setOutpPackagUnitId(odi.getOutpPackagUnitId());
//            drug.setOutpPackagUnitName(odi.getOutpPackagUnitName());
//            drug.setBigPackagUnitName(odi.getBigPackagUnitName());
//            drug.setStockPackagQuantity(odi.getStockPackagQuantity());
//            drug.setStockPackagUnitId(odi.getStockPackagUnitId());
//            drug.setStockPackagUnitName(odi.getStockPackagUnitName());
//            drug.setPackageSpec(odi.getPackageSpec());
//            drug.setOutpPackageSpec(odi.getOutpPackageSpec());
//            drug.setBigPackageSpec(odi.getBigPackageSpec());
//            drug.setStockPackageSpec(odi.getStockPackageSpec());
//            drug.setAddRate(odi.getAddRate());
//            drug.setOutpPerformedDeptId(odi.getOutpPerformedDeptId());
//            drug.setInpPerformedDeptId(odi.getInpPerformedDeptId());
//            drug.setOrderClassId(odi.getOrderClassId());
//            drug.setFeeTypeMaskFlag(odi.getFeeTypeMaskFlag());
//            drug.setServiceType(odi.getServiceType());
//            drug.setDrugMemo(odi.getDrugMemo());
//            //drug.setPurchasePrice(odi.getPurchasePrice ());
//            //drug.setRetailPrice(odi.getRetailPrice());
//            drug.setIsActive(odi.getIsActive());
//            drug.setStopDate(odi.getStopDate());
//
//            if (odi.getInputDate() == null)
//                odi.setInputDate(DateTimeUtils.getCurrentLocalDateTime());
//            else
//                drug.setInputDate(odi.getInputDate());
//
//            drug.setOperatorId(odi.getOperatorId());
//            //   drug.setCreateUser(odi.getCreateUser());
//            //  drug.setCreateDate(odi.getCreateDate());
//            drug.setComment_(odi.getComment_());
//            drug.setChargeItemId(odi.getChargeItemId());
//            drug.setChargeItemClassId(odi.getChargeItemClassId());
//            drug.setSkinTestMark(odi.getSkinTestMark());
//            drug.setSkinTestTypeId(odi.getSkinTestTypeId());
//            drug.setSkinTestTypeName(odi.getSkinTestTypeName());
//            drug.setDrugFrequency(odi.getDrugFrequency());
//            drug.setDrugDosage(odi.getDrugDosage());
//            drug.setUseUnitName(odi.getUseUnitName());
//            drug.setIsDl(odi.getIsDl());
//            drug.setIsGpo(odi.getIsGpo());
//            drug.setCountryCode(odi.getCountryCode());
//            drug.setDecoctionMethod(odi.getDecoctionMethod());
//            drug.setNegotiatedPrice(odi.getNegotiatedPrice());
//            drug.setYpid(odi.getYpid());
//            drug.setPrescauthId(odi.prescauthId);
//            drug.setPrescauthName(odi.prescauthName);
//            drug.setSexLimit(odi.sexLimit);
//            drug.setNetDrugstore(odi.netDrugstore);
//            drug.setDrugPropertyId(odi.drugPropertyId);
//            drug.setDrugPropertyName(odi.drugPropertyName);
//            drug.setIdentifyCode(odi.identifyCode);
//            mediOrgaDrugInfoMapper.updateById(drug);
//            return true;
//        }
//    }
//
//    public String SaveAppChargeItemPrice(AppChargeItemPrice cip) {
//
//        if (PublicFunc.StringIsNullOrEmpty(cip.getId())) {
//            cip.setId(UUID.randomUUID().toString());
//            chargeItemPriceMapper.insert(cip);
//
//        } else {
//
//            QueryWrapper<AppChargeItemPrice> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("orgaId", PublicFunc.IsNullString(cip.getOrgaId()));
//            queryWrapper.eq("id", PublicFunc.IsNullString(cip.getId()));
//
//            List<AppChargeItemPrice> cipList = chargeItemPriceMapper.selectList(queryWrapper);
//
//            AppChargeItemPrice ciprice = cipList.get(0);
//            ciprice.setOrgaId(cip.getOrgaId());
//            ciprice.setItemClassId(cip.getItemClassId());
//            ciprice.setItemCode(cip.getItemCode());
//            ciprice.setItemYBCode(cip.getItemYBCode());
//            ciprice.setItemOtherCode(cip.getItemOtherCode());
//            ciprice.setInputCode(cip.getInputCode());
//            ciprice.setInputCode2(cip.getInputCode2());
//            ciprice.setInputCode3(cip.getInputCode3());
//            ciprice.setItemName(cip.getItemName());
//            ciprice.setItemBasicSpec(cip.getItemBasicSpec());
//            ciprice.setItemOutpSpec(cip.getItemOutpSpec());
//            ciprice.setItemInpSpec(cip.getItemInpSpec());
//            ciprice.setItemBasicUnit(cip.getItemBasicUnit());
//            ciprice.setItemOutpUnit(cip.getItemOutpUnit());
//            ciprice.setItemInpUnit(cip.getItemInpUnit());
//            ciprice.setOutpTransFactor(cip.getOutpTransFactor());
//            ciprice.setInpTransFactor(cip.getInpTransFactor());
//            ciprice.setItemPrice(cip.getItemPrice());
//            ciprice.setItemMaxPrice(cip.getItemMaxPrice());
//            ciprice.setItemNoPrice(cip.getItemNoPrice());
//            ciprice.setItemPreferPrice(cip.getItemPreferPrice());
//            ciprice.setOutpPerformedDeptId(cip.getOutpPerformedDeptId());
//            ciprice.setInpPerformedDeptId(cip.getInpPerformedDeptId());
//            ciprice.setInsurePropertId(cip.getInsurePropertId());
//            ciprice.setDrugOrItemFlag(cip.getDrugOrItemFlag());
//            ciprice.setOrderClassId(cip.getOrderClassId());
//            ciprice.setOrderName(cip.getOrderName());
//            ciprice.setItemBusiClassId(cip.getItemBusiClassId());
//            ciprice.setFeeTypeMaskFlag(cip.getFeeTypeMaskFlag());
//            ciprice.setYBCatalogClassId(cip.getYBCatalogClassId());
//            ciprice.setYBBrandName(cip.getYBBrandName());
//            ciprice.setYBCertifyNo(cip.getYBCertifyNo());
//            ciprice.setItemMemo(cip.getItemMemo());
//            ciprice.setInputLimit(cip.getInputLimit());
//            ciprice.setServiceType(cip.getServiceType());
//            ciprice.setIsActive(cip.getIsActive());
//            ciprice.setStopDate(cip.getStopDate());
//            ciprice.setCreateUser(cip.getCreateUser());
//            ciprice.setCreateDate(cip.getCreateDate());
//            ciprice.setRegionChargeItemId(cip.getRegionChargeItemId());
//            ciprice.setIsVariable(cip.getIsVariable());
//            ciprice.setInsureRate(cip.getInsureRate());//医保转换单位系数
//            ciprice.setCountryCode(cip.getCountryCode());
//            ciprice.setPurchaseRate(cip.getPurchaseRate()); //采购转换系数
//            ciprice.setSexLimit(cip.getSexLimit());
//            chargeItemPriceMapper.updateById(ciprice);
//
//        }
//        return cip.getId();
//    }
//
//    @Override
//    public List<QueryOrgaDrugInfo> getOrgaDrugInfoListAll(String catalogId, String orgaId, String drugInputCode) {
//        return medicineInfoMapper.getOrgaDrugInfoListAll(catalogId, orgaId, drugInputCode);
//    }
//
//    @Override
//    public List<QueryOrgaDrugInfo> getOrgaDrugInfoList(String orgaId, String drugInputCode) {
//        return medicineInfoMapper.getOrgaDrugInfoList(orgaId, drugInputCode);
//    }
//
//    /**
//     * 根据药品分类，输入码,代码获取药品信息
//     *
//     * @param classId
//     * @param drugId
//     * @param drugCode
//     * @return
//     */
//    @Override
//    public List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrugByItemClass(String classId, String drugId, String drugCode, String orgaId) {
//        return medicineInfoMapper.getDrugInfoListForOrgaDrugByItemClass(classId, drugId, drugCode, orgaId);
//    }
//
//    /**
//     * 同步所有药品
//     *
//     * @param orgaId
//     * @return
//     */
//    @Override
//    public String getTBOrgaDrugInfoAll(String orgaId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        medicineInfoMapper.getTBOrgaDrugInfoAll(map);
//        String result = map.get("cv_1");
//        return result;
//    }
//
//    /**
//     * 删除/启用机构药品
//     *
//     * @param odi
//     * @return
//     */
//    @Override
//    @Transactional
//    public String updateOrgaDrugInfoIsActive(MediOrgaDrugInfo odi) {
//        String result = "1";
//
//        //判断是否有在使用该用品  使用中不可停用
//        if (odi.isActive.equals("停用")) {
//            List<PrivateCheckIsActive> lstCg = medicineInfoMapper.isInpChargeActive(odi.getOrgaId(), odi.getId());
//            if (lstCg != null && lstCg.size() > 0) {
//                result = "Msg";
//                for (PrivateCheckIsActive cg : lstCg) {
//                    if (StrUtil.isNotBlank(cg.getPatientName()))
//                        result += cg.getPatientName().trim() + " 用量：" + cg.getAmount().toString() + cg.getUnit() + "\r\n";
//                }
//                return result;
//            }
//        }
//
//        boolean flag = true;
//
//        //更新机构药品信息是否在用状态
//        QueryWrapper<MediOrgaDrugInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", odi.getId()).eq("orgaId", odi.getOrgaId());
//        MediOrgaDrugInfo mediOrgaDruginfo = mediOrgaDrugInfoMapper.selectOne(queryWrapper);
//        if (mediOrgaDruginfo == null)
//            flag = false;
//        else {
//            mediOrgaDruginfo.setId(odi.getId());
//            mediOrgaDruginfo.setInputDate(odi.getInputDate());
//            mediOrgaDruginfo.setOperatorId(odi.getOperatorId());
//            mediOrgaDruginfo.setIsActive(odi.getIsActive());
//            mediOrgaDrugInfoMapper.updateById(mediOrgaDruginfo);
//            flag = true;
//        }
//
//        if (!flag) {
//            result = "2";
//            return result;
//        }
//
//        //更新机构收费项目数据状态
//        int num = medicineInfoMapper.updateChargeItemPriceIsActive(odi.getOrgaId(), odi.getChargeItemId());
//        if (num == 0) {
//            result = "3";
//            return result;
//        }
//
//        result = "0";
//        return result;
//    }
//
//    @Override
//    public List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQuery(String orgaID, String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum) {
//        Map<String, Object> map = new HashMap<>();
//        medicineInfoMapper.getDrugStoreInfoStockQuery(map);
//        map.put("pagefiter", true);
//        List<DefineDrugStoreBillStockQueryInfo> infoList = medicineInfoMapper.getDrugStoreInfoStockQueryList(map);
//        return infoList;
//    }
//
///*  @Override
//    @Transactional
//    public Integer getDrugStoreInfoStockQueryCount(Map map) {
//        medicineInfoMapper.getDrugStoreInfoStockQuery(map);
//        map.put("pagefiter", false);
//        List<DefineDrugStoreBillStockQueryInfo> infoList = medicineInfoMapper.getDrugStoreInfoStockQueryList(map);
//        return Math.toIntExact(infoList.stream().count());
//    }*/
//
//    @Override
//    public List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryWithPageFilter(Integer pageSize, Integer pageIndex, String orgaID, String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum, Boolean isKSS, String specialDrugType) {
//        Map<String, String> map = new HashMap<>();
//        medicineInfoMapper.getDrugStoreInfoStockQuery(map);
//        String limitNum = map.get("limitNum").toString();
//        String offsetNum = map.get("offsetNum").toString();
//        List<DefineDrugStoreBillStockQueryInfo> infoList = medicineInfoMapper.getDrugStoreInfoStockQueryList(map);
//        return infoList;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineDrugStoreBillStockQueryInfo> newGetDrugStoreInfoStockQueryWithPageFilter(Map map) {
//        medicineInfoMapper.getDrugStoreInfoStockQuery(map);
//        List<DefineDrugStoreBillStockQueryInfo> infoList = medicineInfoMapper.getDrugStoreInfoStockQueryList(map);
//        return infoList;
//    }
//
//    @Override
//    public List<DefineDrugStoreBillStockQueryInfo> getDrugStoreInfoStockQueryWithFlag(String orgaID, String drugStoreInputCode, String deptID, String catalogID, String flag, String lowFlag, String isOutp, Integer addRateNum) {
//        List<DefineDrugStoreBillStockQueryInfo> infoList = medicineInfoMapper.getDrugStoreInfoStockQueryWithFlag(orgaID, drugStoreInputCode, deptID, catalogID, flag, lowFlag, isOutp, addRateNum);
//        return infoList;
//    }
//
//
//    @Override
//    public List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListQuery(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag) {
//        List<MediDrugStoreBatchInfo> infoList = medicineInfoMapper.getDrugStoreBatchInfoListQuery(orgaID, deptId, catalogID, flag, addRateNum, lowflag);
//        return infoList;
//    }
//
//    @Override
//    public List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoListQueryByIsOutp(String orgaID, String deptId, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag) {
//        List<MediDrugStoreBatchInfoByIsOutp> infoList = medicineInfoMapper.getDrugStoreBatchInfoListQueryByIsOutp(orgaID, deptId, catalogID, flag, addRateNum, lowflag);
//        return infoList;
//    }
//
//    @Override
//    public List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoStockQueryWithRealTime(String id, String orgaId, String drugId, String deptid, String orgaDrugID) {
//        return medicineInfoMapper.getDrugStoreBatchInfoStockQueryWithRealTime(id, orgaId, drugId, deptid, orgaDrugID);
//    }
//
//    @Override
//    public Integer getPrescMediCount(String orgaID, String deptID) {
//        return null;
//    }
//
//    @Override
//    public List<DefineStockBillStockQueryInfo> getStockInfoStockQuery(Integer pageSize, Integer pageIndex, String orgaID, String stockInputCode, String deptID, String catalogID, String sCatalogID, Boolean flag, Boolean lowFlag, Boolean isCount, Integer addRateNum, Boolean isKSS, String specialDrugType) {
//        Map<String, String> map = new HashMap<>();
//        medicineInfoMapper.getStockInfoStockQuery(map);
//        List<DefineStockBillStockQueryInfo> infoList = medicineInfoMapper.getStockInfoStockQueryList(map);
//        return infoList;
//    }
//
///*  @Override
//    @Transactional
//    public Integer getGetStockInfoStockQueryCount(Map map) {
//        medicineInfoMapper.getStockInfoStockQuery(map);
//        map.put("pagefiter", false);
//        List<DefineStockBillStockQueryInfo> infoList = medicineInfoMapper.getStockInfoStockQueryList(map);
//        return Math.toIntExact(infoList.stream().count());
//    }*/
//
//    @Override
//    @Transactional
//    public List<DefineStockBillStockQueryInfo> newGetStockInfoStockQuery(Map map) {
//        medicineInfoMapper.getStockInfoStockQuery(map);
//        List<DefineStockBillStockQueryInfo> infoList = medicineInfoMapper.getStockInfoStockQueryList(map);
//        return infoList;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineStockBillStockQueryInfo> getStockInfoStockQueryWithFlag(String orgaID, String deptID, String stockInputCode, String catalogID, String sCatalogID, Boolean flag, Boolean lowFlag, Integer addRateNum) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("orgaId", orgaID);
//        map.put("drugStoreInputCode", stockInputCode);
//        map.put("deptID", deptID);
//        map.put("catalogID", catalogID);
//        map.put("sCatalogID", sCatalogID);
//        map.put("includeZoreFlag", flag);
//        map.put("lowFlag", lowFlag);
//        map.put("isCount", false);
//        map.put("addRateNum", addRateNum);
//        map.put("isKSS", false);
//        map.put("specialDrugType", "");
//        // map.put("pagefiter", false);
//        medicineInfoMapper.getStockInfoStockQuery(map);
//        List<DefineStockBillStockQueryInfo> infoList = medicineInfoMapper.getStockInfoStockQueryList(map);
//        return infoList;
//    }
//
//
//    @Override
//    public List<MediStockBatchInfo> getStockBatchInfoListQuery(String orgadID, String deptID, String catalogID, Boolean flag, Integer addRateNum, Boolean lowflag) {
//        List<MediStockBatchInfo> infoList = medicineInfoMapper.getStockBatchInfoListQuery(orgadID, deptID, catalogID, flag, addRateNum, lowflag);
//        return infoList;
//    }
//
//    @Override
//    public List<MediSuppyInfo> getSuppyInfoList(String orgaId, String deptInputCode) {
//        return medicineInfoMapper.getSuppyInfoList(orgaId, deptInputCode);
//    }
//
//    @Override
//    public List<SYSKeyValue> getKeyValueOfListDictKey(String kvGroups) {
//        return medicineInfoMapper.getKeyValueOfListDictKey(kvGroups);
//    }
//
//    @Override
//    public List<MediDrugStoreInfo> getDrugStoreInfoList(String orgaId, String deptId, String drugStoreInputCode) {
//        List<String> deptIdList=new ArrayList<>();
//        if (deptId!=null &&!deptId.isEmpty()&&deptId.contains(","))
//            deptIdList=Arrays.asList(deptId.split(","));
//        return medicineInfoMapper.getDrugStoreInfoList(orgaId, deptId,deptIdList, drugStoreInputCode);
//    }
//
//    @Override
//    @Transactional
//    public Boolean updateFreezeInfo(Map map) {
//        String id = map.get("id").toString();
//        String orgid = map.get("orgaId").toString();
//        String deptid = map.get("deptId").toString();
//        String countNumber = map.get("countNumber").toString();
//        String orgadrugid = map.get("orgadrugId").toString();
//        Boolean boolResule = false;
//        List<DefineMediDrugStoreFreeze> defineMediDrugStoreFreezes = medicineInfoMapper.getFreezeBatchInfo(id);
//        if (defineMediDrugStoreFreezes == null || defineMediDrugStoreFreezes.stream().count() == 0) {
//            boolResule = medicineInfoMapper.updateFreezeBatchInfo(id, orgid, deptid, countNumber) > 0 ? true : false;
//            if (boolResule) {
//                boolResule = medicineInfoMapper.updateFreezeInfo(orgadrugid, deptid, countNumber) > 0 ? true : false;
//            }
//            if (boolResule) {
//                boolResule = medicineInfoMapper.insertMedi_DrugStoreFreeze(map) > 0 ? true : false;
//            }
//        }
//        return boolResule;
//    }
//
//    @Override
//    public String bd_GetCleanStockBatchInfo(Map map) {
//        medicineInfoMapper.bd_GetCleanStockBatchInfo(map);
//        String GetMessage = (String) map.get("cv_1");
//        return GetMessage;
//
//    }
//
//    @Override
//    public String bd_CleanDrugStoreBatchInfo(Map map) {
//        medicineInfoMapper.bd_CleanDrugStoreBatchInfo(map);
//        String GetMessage = (String) map.get("cv_1");
//        return GetMessage;
//    }
//
//    @Override
//    public Boolean cleanStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate) {
//        Boolean result = false;
//        List<MediStockBatchInfo> clean = medicineInfoMapper.getCleanStockBatchInfo(id, orgadurgid, orgaid, deptid);
//        if (clean.size() == 1) {
//            result = medicineInfoMapper.insertCleanStockBatchInfo(id, orgadurgid, orgaid, deptid, cleanUser, cleanDate) > 0 ? true : false;
//            if (result) {
//                result = medicineInfoMapper.deleteMedi_StockBatchInfoForClean(id, orgadurgid, orgaid, deptid) > 0 ? true : false;
//            }
//
//        }
//        return result;
//    }
//
//    @Override
//    public Boolean cleanDrugStoreBatchInfo(String id, String orgadurgid, String orgaid, String deptid, String cleanUser, String cleanDate) {
//        Boolean result = false;
//        List<MediDrugStoreBatchInfo> clean = medicineInfoMapper.getCleanDrugStoreBatchInfo(id, orgadurgid, orgaid, deptid);
//        if (clean.size() == 1) {
//            result = medicineInfoMapper.insertCleanDrugStoreBatchInfo(id, orgadurgid, orgaid, deptid, cleanUser, cleanDate) > 0 ? true : false;
//            if (result) {
//                result = medicineInfoMapper.deleteMedi_DrugStoreBatchInfoForClean(id, orgadurgid, orgaid, deptid) > 0 ? true : false;
//            }
//
//        }
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public Boolean updateFreezeCancelInfo(Map map) {
//        String id = map.get("id").toString();
//        String orgaid = map.get("orgaid").toString();
//        String deptid = map.get("deptid").toString();
//        String orgaDrugId = map.get("orgaDrugId").toString();
//        String userId = map.get("userId").toString();
//        String userName = map.get("userName").toString();
//        Boolean boolResule = false;
//        List<DefineMediDrugStoreFreeze> defineMediDrugStoreFreezes = medicineInfoMapper.getFreezeBatchInfo(id);
//        if (defineMediDrugStoreFreezes != null && defineMediDrugStoreFreezes.stream().count() == 1) {
//            Boolean isZero = false;
//            MediDrugStoreBatchInfo dsb = medicineInfoMapper.selectBatchInfoNumberIsZero2(id, orgaid, deptid);
//            if (dsb == null) {
//                boolResule = false;
//            } else {
//                isZero = (dsb.number_ == 0) ? true : false;
//
//            }
//            if (isZero) {
//                boolResule = medicineInfoMapper.updateInfoForNumberIsZero(orgaDrugId, id, orgaid, deptid) > 0 ? true : false;
//                if (boolResule) {
//                    boolResule = medicineInfoMapper.updateBatchInfoForNumberIsZero(id, orgaid, deptid) > 0 ? true : false;
//                }
//                if (boolResule) {
//
//
//                    boolResule = medicineInfoMapper.deleteMedi_DrugStoreFreeze(id, orgaid, deptid, userId, userName) > 0 ? true : false;
//                }
//            } else {
//                boolResule = medicineInfoMapper.updateFreezeCancelBatchInfo(id, orgaid, deptid) > 0 ? true : false;
//                if (boolResule) {
//                    boolResule = medicineInfoMapper.updateFreezeCancelInfo(map) > 0 ? true : false;
//                }
//                if (boolResule) {
//                    boolResule = medicineInfoMapper.deleteMedi_DrugStoreFreeze(id, orgaid, deptid, userId, userName) > 0 ? true : false;
//                }
//            }
//        }
//        return boolResule;
//    }
//
//    @Override
//    public List<DefineMediDrugStoreFreeze> select_DrugStoreFreezestatus(String Id, String orgaid, String deptid) {
//        List<DefineMediDrugStoreFreeze> resultList = medicineInfoMapper.select_DrugStoreFreezestatus(Id, orgaid, deptid);
//        return resultList;
//    }
//
//    @Override
//    public String getMediStockBatchTotal(String orgaID, String deptId, String isYF, String sCatalogID, Integer addRateNum) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("orgaID", orgaID);
//        map.put("deptId", deptId);
//        map.put("isYF", isYF);
//        map.put("sCatalogID", sCatalogID);
//        map.put("addRateNum", addRateNum);
//        String result = medicineInfoMapper.getMediStockBatchTotal(map);
//        return result;
//    }
//
//    @Override
//    public List<MediStockBatchInfo> getStockBatchInfo(Map map) {
//        List<MediStockBatchInfo> mediStockBatchInfos = medicineInfoMapper.getStockBatchInfo(map);
//        return mediStockBatchInfos;
//    }
//
//    @Override
//    public List<MediDrugStoreBatchInfoByIsOutp> getDrugStoreBatchInfoByIsOutp(Map map) {
//        List<MediDrugStoreBatchInfoByIsOutp> mediDrugStoreBatchInfos = medicineInfoMapper.getDrugStoreBatchInfoByIsOutp(map);
//        return mediDrugStoreBatchInfos;
//    }
//
//    @Override
//    public List<MediDrugStoreBatchInfo> getDrugStoreBatchInfo(Map map) {
//        List<MediDrugStoreBatchInfo> mediDrugStoreBatchInfos = medicineInfoMapper.getDrugStoreBatchInfo(map);
//        return mediDrugStoreBatchInfos;
//    }
//
//    @Override
//    public List<SysOrgaDept> getOrgaDeptAddVillageDept(String orgaId) {
//        return medicineInfoMapper.getOrgaDeptAddVillageDept(orgaId);
//    }
//
//    @Override
//    public List<StoreBillDetails> getOutStoreBillDetailsCombox(String orgaId, String drugCode) {
//        return medicineInfoMapper.getOutStoreBillDetailsCombox(orgaId, drugCode);
//    }
//
//    @Override
//    public List<StoreBillDetails> getInStoreBillDetailsCombox(String orgaId, String drugCode) {
//        return medicineInfoMapper.getInStoreBillDetailsCombox(orgaId, drugCode);
//    }
//
//    @Override
//    @Transactional
//    public List<DefineGetInOrOutStoreBillInfoByOut> getInOrOutStoreBillInfoByOutCount(String deptid, String orgaId, String startDate, String endDate, String supplierId, String stockId, String durgNameOrCode, String selectOrgaId, String sCatalogId, String orgaDrugId) {
//        List<DefineGetInOrOutStoreBillInfoByOut> result = null;
//        Map resultMap = new HashMap();
//        resultMap.put("v_deptid", deptid);
//        resultMap.put("v_orgaid", orgaId);
//        resultMap.put("v_StartDate", startDate);
//        resultMap.put("v_EndDate", endDate);
//        resultMap.put("v_SupplierID", supplierId != null && supplierId.equals("") ? null : supplierId);
//        resultMap.put("v_StockID", stockId);
//        resultMap.put("v_RELATIVEORGAID", selectOrgaId);
//        resultMap.put("v_sCatalogID", sCatalogId != null && sCatalogId.equals("") ? null : sCatalogId);
//        resultMap.put("v_orgdruid", orgaDrugId != null && orgaDrugId.equals("") ? null : orgaDrugId);
//        medicineInfoMapper.callInOrOutStoreBillInfoByOutNew(resultMap);
//        result = medicineInfoMapper.getInOrOutStoreBillInfoByOutNew();
//        if (durgNameOrCode != null && !durgNameOrCode.isEmpty()) {
//            durgNameOrCode = durgNameOrCode.toUpperCase();
//            String finalDurgNameOrCode = durgNameOrCode;
//            if (result != null && result.size() > 0)
//                result = result.stream().filter(p -> p.drugCode.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.drugName.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.drugInputCode.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.billNo.contains(finalDurgNameOrCode)).collect(Collectors.toList());
//        }
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineGetInOrOutStoreBillInfoByOut> getInOrOutStoreBillInfoByOutWithPageFilter(String deptid, String orgaId, String startDate, String endDate, String supplierId, String stockId, String durgNameOrCode, String selectOrgaId, String sCatalogId, String orgaDrugId, Integer limit, Integer offset, String cktk) {
//        List<DefineGetInOrOutStoreBillInfoByOut> result = null;
//        if (durgNameOrCode != null && !durgNameOrCode.isEmpty())
//            durgNameOrCode = durgNameOrCode.toUpperCase();
//        Map resultMap = new HashMap();
//        resultMap.put("v_deptid", deptid);
//        resultMap.put("v_orgaid", orgaId);
//        resultMap.put("v_StartDate", startDate);
//        resultMap.put("v_EndDate", endDate);
//        resultMap.put("v_SupplierID", supplierId != null && supplierId.equals("") ? null : supplierId);
//        resultMap.put("v_StockID", stockId);
//        resultMap.put("v_RELATIVEORGAID", selectOrgaId);
//        resultMap.put("v_sCatalogID", sCatalogId != null && sCatalogId.equals("") ? null : sCatalogId);
//        resultMap.put("v_orgdruid", orgaDrugId != null && orgaDrugId.equals("") ? null : orgaDrugId);
//        resultMap.put("v_durgNameOrCode", durgNameOrCode != null && durgNameOrCode.equals("") ? null : durgNameOrCode);
//        resultMap.put("v_limit", limit);
//        resultMap.put("v_offset", offset);
//        resultMap.put("v_cktk", cktk);
//        medicineInfoMapper.callInOrOutStoreBillInfoByOutNewPaging(resultMap);
//        result = medicineInfoMapper.getInOrOutStoreBillInfoByOutNew();
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineGetInOrOutStoreBillInfoByIn> getInOrOutStoreBillInfoByIn(String orgaId, String startDate, String endDate, String dateType, String sinvoicenumber, String supplierId, String stockId, String durgNameOrCode, String sCatalogId, String orgaDrugId) {
//        List<DefineGetInOrOutStoreBillInfoByIn> result = null;
//        Map resultMap = new HashMap();
//        resultMap.put("v_orgaid", orgaId);
//        resultMap.put("v_StartDate", startDate);
//        resultMap.put("v_EndDate", endDate);
//        resultMap.put("v_DateType", dateType);
//        resultMap.put("v_Invoicenumber", sinvoicenumber != null && sinvoicenumber.equals("") ? null : sinvoicenumber);
//        resultMap.put("v_SupplierID", supplierId != null && supplierId.equals("") ? null : supplierId);
//        resultMap.put("v_StockID", stockId);
//        resultMap.put("v_sCatalogID", sCatalogId != null && sCatalogId.equals("") ? null : sCatalogId);
//        resultMap.put("v_orgdruid", orgaDrugId != null && orgaDrugId.equals("") ? null : sCatalogId);
//        medicineInfoMapper.callInOrOutStoreBillInfoByIn(resultMap);
//        result = medicineInfoMapper.getInOrOutStoreBillInfoByIn();
//        if (durgNameOrCode != null && !durgNameOrCode.isEmpty()) {
//            durgNameOrCode = durgNameOrCode.toUpperCase();
//            String finalDurgNameOrCode = durgNameOrCode;
//            if (result != null && result.size() > 0)
//                result = result.stream().filter(p -> p.drugCode.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.drugName.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.drugInputCode.toUpperCase().contains(finalDurgNameOrCode)
//                        || p.billNo.contains(finalDurgNameOrCode)).collect(Collectors.toList());
//        }
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineGetInOrOutStoreBillInfoByIn> getInOrOutStoreBillInfoByInWithPageFilter(String orgaId, String startDate, String endDate, String dateType, String sinvoicenumber, String supplierId, String stockId, String durgNameOrCode, String sCatalogId, String orgaDrugId, Integer limit, Integer offset, String rkth) {
//        List<DefineGetInOrOutStoreBillInfoByIn> result = null;
//        if (durgNameOrCode != null && !durgNameOrCode.isEmpty())
//            durgNameOrCode = durgNameOrCode.toUpperCase();
//        Map resultMap = new HashMap();
//        resultMap.put("v_orgaid", orgaId);
//        resultMap.put("v_StartDate", startDate);
//        resultMap.put("v_EndDate", endDate);
//        resultMap.put("v_DateType", dateType);
//        resultMap.put("v_Invoicenumber", sinvoicenumber != null && sinvoicenumber.equals("") ? null : sinvoicenumber);
//        resultMap.put("v_SupplierID", supplierId != null && supplierId.equals("") ? null : supplierId);
//        resultMap.put("v_StockID", stockId);
//        resultMap.put("v_sCatalogID", sCatalogId != null && sCatalogId.equals("") ? null : sCatalogId);
//        resultMap.put("v_orgdruid", orgaDrugId != null && orgaDrugId.equals("") ? null : orgaDrugId);
//        resultMap.put("v_durgNameOrCode", durgNameOrCode != null && durgNameOrCode.equals("") ? null : durgNameOrCode);
//        resultMap.put("v_limit", limit);
//        resultMap.put("v_offset", offset);
//        resultMap.put("v_rkth", rkth);
//        medicineInfoMapper.callGetInOrOutStoreBillInfoByInPaging(resultMap);
//        result = medicineInfoMapper.getInOrOutStoreBillInfoByIn();
//        return result;
//    }
//
//    @Override
//    public List<DefineOutStoreBillMasterInfo> getOutStoreBillMasterList(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status) {
//        return medicineInfoMapper.getOutStoreBillMasterList(dateType, startDate, endDate, orgaid, deptId, executor, status);
//    }
//
//    @Override
//    public List<DefineStoreBillMasterMoneyCount> getOutStoreBillMasterMoneyCount(String dateType, String startDate, String endDate, String orgaid, String deptId, String executor, String status) {
//        return medicineInfoMapper.getOutStoreBillMasterMoneyCount(dateType, startDate, endDate, orgaid, deptId, executor, status);
//    }
//
//    @Override
//    @Transactional
//    public List<DefineMediStockBatchInfo> getStockBatchInfoListForDrugCode(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD) {
//        if (drugCode != null && !drugCode.equals(""))
//            drugCode = drugCode.toUpperCase();
//        if (isOpenRMFFreezingOrgaD != null && isOpenRMFFreezingOrgaD != "" && isOpenRMFFreezingOrgaD.equals("是"))
//            isOpenRMFFreezingOrgaD = "true";
//        return medicineInfoMapper.getStockBatchInfoListForDrugCode(orgaid, deptId, drugCode, relativeDeptId, isAlterLower, relativeOrgaId, isZero, isOpenRMFFreezingOrgaD);
//    }
//
//    @Override
//    public List<DefineOutStoreBillDetailInfoWithDrawal> getOutStoreBillDetailListWithDrawal(String outStoreBillId, String orgaid, String isGetInStorePrice, String cmbpxfs) {
//        return medicineInfoMapper.getOutStoreBillDetailListWithDrawal(outStoreBillId, orgaid, isGetInStorePrice, cmbpxfs);
//    }
//
//    @Override
//    public List<AppRelativeOrganization> getRelativeOrganizationListForAll(String orgaid) {
//        return medicineInfoMapper.getRelativeOrganizationListForAll(orgaid);
//    }
//
//    @Override
//    public List<DefineRefundingBillMasterInfo> getRefundingBillMasterList(String orgaID, String deptId, Boolean isInputOrCheckDate, String startDate, String endDate, String checkID, String status, String tbSuppliers) {
//        return medicineInfoMapper.getRefundingBillMasterList(orgaID, deptId, isInputOrCheckDate, startDate, endDate, checkID, status, tbSuppliers);
//    }
//
//    @Override
//    public List<DefineRefundingBillDetailInfoWithDrawal> getRefundingBillDetailListWithDrawal(String refundingBillID, String id, String cmbpxfs, String orgaID) {
//        return medicineInfoMapper.getRefundingBillDetailListWithDrawal(refundingBillID, id, cmbpxfs, orgaID);
//    }
//
//    @Override
//    @Transactional
//    public String saveRefundingBillMasterNew(MediRefundingBillMaster rbm, List<MediRefundingBillDetail> isbdList) {
//        String result = "";
//        rbm.billNo = publicFunc.getCurrSerialNoExt(rbm.orgaID, "05", rbm.operatorID);
//        //Medi_RefundingBillMaster新增一条记录
//        if(mediRefundingBillMasterMapper.insert(rbm)==0)
//            throw new RuntimeException("新增退货主单失败");
//        //Medi_InStoreBillDetail新增N条记录(N>=1)
//        for (MediRefundingBillDetail isbdItem : isbdList) {
//            isbdItem.id = UUID.randomUUID().toString();
//            isbdItem.billNo = rbm.billNo;
//            isbdItem.refundingBillID = rbm.id;
//            if (mediRefundingBillDetailMapper.insert(isbdItem)==0)
//                throw new RuntimeException("新增退货数据失败");
//        }
//        result="退货单保存成功";
//        return result;
//    }
//
//
//    @Override
//    @Transactional
//    public Integer saveOutStoreBill(MediOutStoreBillMaster newMaster, List<MediOutStoreBillDetail> mediOutStoreBillDetails, String isOutpati, String isInp) {
//        Integer result = 1;
//        //修改
//        if (newMaster.billNo != null && !newMaster.billNo.isEmpty()) {
//            MediOutStoreBillMaster master = mediOutStoreBillMasterMapper.selectById(newMaster.getId());
//            if (master != null) {
//                int reNum = mediOutStoreBillMasterMapper.updateById(newMaster);
//                if (reNum == 0)
//                    throw new RuntimeException("更新出库主表失败！");
//            }
//            for (MediOutStoreBillDetail detail : mediOutStoreBillDetails) {
//                QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("outStoreBillId", detail.getOutStoreBillId());
//                queryWrapper.eq("orgadrugid", detail.getOrgaDrugId());
//                queryWrapper.eq("batch", detail.getBatch());
//                queryWrapper.eq("drugid", detail.getDrugId());
//                queryWrapper.eq("isActive", "在用");
//                Map<String, Object> map = new HashMap<>();
//                map.put("orgaId", detail.getOrgaId());
//                map.put("deptId", newMaster.getDeptId());
//                map.put("drugId", detail.getDrugId());
//                map.put("orgaDrugId", detail.getOrgaDrugId());
//                map.put("batch", detail.getBatch());
//                List<MediStockBatchInfo> mediStockBatchInfos = medicineInfoMapper.getStockBatchInfo(map);
//                if (mediStockBatchInfos == null || (mediStockBatchInfos != null && mediStockBatchInfos.size() == 0))
//                    throw new RuntimeException("未找到库存信息！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                MediStockBatchInfo mediStockBatchInfo = mediStockBatchInfos.get(0);
///*                if (isInp.equals("是") && isOutpati.equals("否")) {
//                    //住院药房
//                    if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(detail.getNumber_()))
//                        throw new RuntimeException("库存不足！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                } else {
//                    //门诊药房
//                    if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(detail.getNumber_()) * publicFunc.IsNullInt(mediStockBatchInfo.outpPackagQuantity))
//                        throw new RuntimeException("库存不足！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                }*/
//                if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(detail.getNumber_()) * publicFunc.IsNullInt(detail.packagQuantity)  )
//                    throw new RuntimeException("库存不足！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                MediOutStoreBillDetail detail1 = mediOutStoreBillDetailMapper.selectOne(queryWrapper);
//                if (detail1 == null) {
//                    //修改的时候，新增;
//                    detail.billNo = newMaster.billNo;
//                    detail.id = UUID.randomUUID().toString();
//                    if (detail.packagQuantity == 1)
//                        detail.unitId = getPriceUnitIdByDrugId(detail.drugId);
//                    detail.setPurchaseMoney(publicFunc.IsNullDecimal(detail.getPurchasePrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(detail.getNumber_()))));
//                    detail.setRetailMoney(publicFunc.IsNullDecimal(detail.getRetailPrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(detail.number_))));
//                    detail.createDate = LocalDateTime.now();
//                    detail.inputDate = LocalDateTime.now();
//
//                    int reNum = mediOutStoreBillDetailMapper.insert(detail);
//                    if (reNum == 0)
//                        throw new RuntimeException("新增出库明细失败！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                } else {
//                    if (!detail.getOutStoreBillId().equals(newMaster.id))
//                        throw new RuntimeException("主单id与明细中主单id不一致,药品！" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                    detail1.setNumber_(detail.getNumber_());
//                    detail1.setUnitName(detail.unitName);
//                    detail1.setPackagQuantity(detail.packagQuantity);
//                    if (detail.packagQuantity == 1)
//                        detail1.unitId = getPriceUnitIdByDrugId(detail.drugId);
//                    detail1.setPurchasePrice(detail.getPurchasePrice());//.divide(BigDecimal.valueOf(mediStockBatchInfo.packagQuantity / mediStockBatchInfo.outpPackagQuantity)));
//                    detail1.setRetailPrice(detail.getRetailPrice());//.divide(BigDecimal.valueOf(mediStockBatchInfo.packagQuantity / mediStockBatchInfo.outpPackagQuantity)));
//                    detail1.setPurchaseMoney(publicFunc.IsNullDecimal(detail.getPurchasePrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(detail1.number_))));
//                    detail1.setRetailMoney(publicFunc.IsNullDecimal(detail.getRetailPrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(detail1.number_))));
//                    int reNum = mediOutStoreBillDetailMapper.updateById(detail1);
//                    if (reNum == 0)
//                        throw new RuntimeException("更新出库明细失败！药品:" + detail.getDrugCode() + detail.getDrugName() + detail.getBatch());
//                }
//            }
//            result = 0;
//        }
//        //新增
//        else {
//            MediOutStoreBillMaster master = mediOutStoreBillMasterMapper.selectById(newMaster.getId());
//            if (master != null) {
//                throw new RuntimeException("请勿新增相同出库单！");
//            }
//            String billNo = publicFunc.getCurrSerialNoExt(newMaster.orgaId, "06", newMaster.operatorId);
//            if (billNo == null || billNo.equals(""))
//                return result;
//            //调用获取当前序列号方法
//            newMaster.billNo = billNo;
//            newMaster.id = UUID.randomUUID().toString();
//            newMaster.createDate = LocalDateTime.now();
//            newMaster.inputDate = LocalDateTime.now();
//            //Medi_OutStoreBillMaster新增一条记录
//            //Medi_OutStoreBillDetail新增N条记录(N>=1)
//            for (MediOutStoreBillDetail isbdItem : mediOutStoreBillDetails) {
//                Map<String, Object> map = new HashMap<>();
//                map.put("orgaId", isbdItem.getOrgaId());
//                map.put("deptId", newMaster.getDeptId());
//                map.put("drugId", isbdItem.getDrugId());
//                map.put("orgaDrugId", isbdItem.getOrgaDrugId());
//                map.put("batch", isbdItem.getBatch());
//                List<MediStockBatchInfo> mediStockBatchInfos = medicineInfoMapper.getStockBatchInfo(map);
//                if (mediStockBatchInfos == null || (mediStockBatchInfos != null && mediStockBatchInfos.size() == 0))
//                    throw new RuntimeException("未找到库存信息！药品:" + isbdItem.getDrugCode() + isbdItem.getDrugName() + isbdItem.getBatch());
//                MediStockBatchInfo mediStockBatchInfo = mediStockBatchInfos.get(0);
//                /*if (isInp.equals("是") && isOutpati.equals("否")) {
//                    //住院药房
//                    if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(isbdItem.getNumber_()))
//                        throw new RuntimeException("库存不足！药品:" + isbdItem.getDrugCode() + isbdItem.getDrugName() + isbdItem.getBatch());
//                } else {
//                    //门诊药房
//                    if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(isbdItem.getNumber_()) * publicFunc.IsNullInt(mediStockBatchInfo.outpPackagQuantity))
//                        throw new RuntimeException("库存不足！药品:" + isbdItem.getDrugCode() + isbdItem.getDrugName() + isbdItem.getBatch());
//                }*/
//                if (publicFunc.IsNullInt(mediStockBatchInfo.getNumber()) * publicFunc.IsNullInt(mediStockBatchInfo.packagQuantity) + publicFunc.IsNullInt(mediStockBatchInfo.priceNumber) < publicFunc.IsNullInt(isbdItem.getNumber_()) * publicFunc.IsNullInt(isbdItem.packagQuantity)  )
//                    throw new RuntimeException("库存不足！药品:" + isbdItem.getDrugCode() + isbdItem.getDrugName() + isbdItem.getBatch());
//                isbdItem.billNo = billNo;
//                isbdItem.id = UUID.randomUUID().toString();
//                isbdItem.outStoreBillId = newMaster.id;
//                isbdItem.setUnitName(isbdItem.unitName);
//                isbdItem.setPackagQuantity(isbdItem.packagQuantity);
//                //isbdItem.setUnitId(mediStockBatchInfo.outpPackagUnitId);
//                if (isbdItem.packagQuantity == 1)
//                    isbdItem.unitId = getPriceUnitIdByDrugId(isbdItem.drugId);
//                isbdItem.setPurchasePrice(isbdItem.getPurchasePrice());//divide(BigDecimal.valueOf(mediStockBatchInfo.packagQuantity / mediStockBatchInfo.outpPackagQuantity)));
//                isbdItem.setRetailPrice(isbdItem.getRetailPrice());//.divide(BigDecimal.valueOf(mediStockBatchInfo.packagQuantity / mediStockBatchInfo.outpPackagQuantity)));
//                isbdItem.setPurchaseMoney(publicFunc.IsNullDecimal(isbdItem.getPurchasePrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(isbdItem.number_))));
//                isbdItem.setRetailMoney(publicFunc.IsNullDecimal(isbdItem.getRetailPrice()).multiply(BigDecimal.valueOf(publicFunc.IsNullInt(isbdItem.number_))));
//                isbdItem.setCreateDate(newMaster.createDate);
//                isbdItem.setInputDate(newMaster.inputDate);
//
//                mediOutStoreBillDetailMapper.insert(isbdItem);
//            }
//            mediOutStoreBillMasterMapper.insert(newMaster);
//            result = 0;
//        }
//        return result;
//    }
//
//    @Override
//    public String wasterRefundingBillMaster(String orgaID, String deptID, String refundingBillID, String operatorID) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaID", orgaID);
//        map.put("deptID", deptID);
//        map.put("refundingBillID", refundingBillID);
//        map.put("operatorID", operatorID);
//        medicineInfoMapper.wasterRefundingBillMaster(map);
//        String getMessage = (String) map.get("v_return");
//        return getMessage;
//    }
//
//    @Override
//    public String wasterRefundingBillDetail(String orgaID, String refundingBillID, String RefundingBillDetailID, String operatorID) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaID", orgaID);
//        map.put("refundingBillID", refundingBillID);
//        map.put("RefundingBillDetailID", RefundingBillDetailID);
//        map.put("operatorID", operatorID);
//        medicineInfoMapper.wasterRefundingBillDetail(map);
//        String getMessage = (String) map.get("v_return");
//        return getMessage;
//    }
//
//    @Override
//    @Transactional
//    public String checkRefundingBill(String orgaID, String refundingBillID, String checkID) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaID", orgaID);
//        map.put("refundingBillID", refundingBillID);
//        map.put("checkID", checkID);
//        medicineInfoMapper.checkRefundingBill(map);
//        String getMessage = (String) map.get("cv_1");
//        if (!getMessage.equals("ok"))
//            throw new RuntimeException(getMessage);
//        return getMessage;
//    }
//
//
//    @Override
//    public String getPriceUnitIdByDrugId(String drugId) {
//        return medicineInfoMapper.getPriceUnitIdByDrugId(drugId);
//    }
//
//    @Override
//    @Transactional
//    public String getNanDongDrugIsHave(String orgaid, String outStoreBillId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", orgaid);
//        map.put("v_OutStoreBillID", outStoreBillId);
//        medicineInfoMapper.getNanDongDrugIsHave(map);
//        result = (String) map.get("cv_1");
//        if (result == null)
//            result = "";
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public String checkOutStoreBillNew(DefineOutStoreBillMasterInfo outStoreBillMaster) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", outStoreBillMaster.orgaId);
//        map.put("v_DeptID", outStoreBillMaster.deptId);
//        map.put("v_RelativeDeptID", outStoreBillMaster.relativeDeptId);
//        map.put("v_ReceivingDeptInputCode", outStoreBillMaster.receivingDeptInputCode);
//        map.put("v_ReceivingDeptCode", outStoreBillMaster.receivingDeptCode);
//        map.put("v_ReceivingDeptName", outStoreBillMaster.receivingDeptName);
//        map.put("v_OutStoreBillID", outStoreBillMaster.id);
//        map.put("v_OperatorID", outStoreBillMaster.operatorId);
//        medicineInfoMapper.checkOutStoreBillNew(map);
//        result = (String) map.get("cv_1");
//        if (result == null || !result.equals("执行成功"))
//            throw new RuntimeException(result);
//        return result;
//    }
//
//    @Override
//    public String saveRefundingBillMaster(MediRefundingBillMaster rbm, List<MediRefundingBillDetail> mediRefundingBillDetails) {
//        String resultStr = "";
//        MediRefundingBillMaster isbm1 = mediRefundingBillMasterMapper.selectById(rbm.id);
//        if (isbm1 == null) {
//            throw new RuntimeException("修改退货主单失败");
//        } else {
//            //isbm1.ID = rbm.ID;//ID	ID
//            //isbm1.OrgaID = rbm.OrgaID;//机构ID	OrgaID
//            //isbm1.DeptID = rbm.DeptID;//部门ID	DeptID
//            //isbm1.DeptInputCode = rbm.DeptInputCode;//部门输入码	DeptInputCode
//            //isbm1.DeptCode = rbm.DeptCode;//部门代码	DeptCode
//            //isbm1.DeptName = rbm.DeptName;//部门名称	DeptName
//            //isbm1.CheckID = rbm.CheckID;//审核员ID	CheckID
//            //isbm1.CheckDate = rbm.CheckDate;//审核日期	CheckDate
//            //isbm1.BillNo = rbm.BillNo;//退货单号	BillNo
//            //isbm1.PublicSignID = rbm.PublicSignID;//招标类型ID	PublicSignID
//            //isbm1.PublicSignName = rbm.PublicSignName;//招标类型名称	PublicSignName
//            //isbm1.InStoreTypeID = rbm.InStoreTypeID;//入库类型ID	RefundingTypeID
//            //isbm1.InStoreTypeName = rbm.InStoreTypeName;//入库类型名称	RefundingTypeName
//            isbm1.expenditureAmount = rbm.expenditureAmount;//实退金额	ExpenditureAmount
//            isbm1.supplierID = rbm.supplierID;//供货商ID	SupplierID
//            isbm1.supplyInputCode = rbm.supplyInputCode;//供应商输入码	SupplyInputCode
//            isbm1.supplyCode = rbm.supplyCode;//供应商代码	SupplyCode
//            isbm1.supplyName = rbm.supplyName;//供应商名称	SupplyName
//            //isbm1.RefundingMemo = rbm.RefundingMemo;//退货备注	RefundingMemo
//            isbm1.isActive = rbm.isActive;//是否在用	IsActive
//            isbm1.stopDate = rbm.stopDate;//停用日期	StopDate
//            //isbm1.Status = rbm.Status;//状态	Status
//            isbm1.operatorID = rbm.operatorID;//修改人ID	OperatorID
//            isbm1.inputDate = rbm.inputDate;//修改时间	InputDate
//            isbm1.invoiceAmount = rbm.invoiceAmount;
//            isbm1.invoiceDate = rbm.invoiceDate;
//            isbm1.invoiceNumber = rbm.invoiceNumber;
//            //isbm1.CreateUser = rbm.CreateUser;//创建人ID	CreateUser
//            //isbm1.CreateDate = rbm.CreateDate;//创建时间	CreateDate
//            //isbm1.Comment = rbm.Comment;//备注	Comment
//            if (mediRefundingBillMasterMapper.updateById(isbm1)==0)
//                throw new RuntimeException("修改退货主单失败");
//            for (MediRefundingBillDetail inItem : mediRefundingBillDetails) {
//                MediRefundingBillDetail detail = mediRefundingBillDetailMapper.selectById(inItem.id);
//                if (detail == null) {
//                    inItem.id = UUID.randomUUID().toString();
//                    inItem.billNo = rbm.billNo;
//                    inItem.refundingBillID = rbm.id;
//                    if (mediRefundingBillDetailMapper.insert(inItem)==0)
//                        throw new RuntimeException("修改退货数据失败");
//                } else {
//                    if (mediRefundingBillDetailMapper.updateById(inItem)==0)
//                        throw new RuntimeException("修改退货数据失败");
//                }
//            }
//        }
//        resultStr="退货单修改成功";
//        return resultStr;
//    }
//
//    @Override
//    public String newReverseRefundingBill(String orgaID, String deptID, String refundingMasterID, String operatorID) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaID", orgaID);
//        map.put("deptID", deptID);
//        map.put("refundingMasterID", refundingMasterID);
//        map.put("operatorID", operatorID);
//        medicineInfoMapper.newReverseRefundingBill(map);
//        result = (String) map.get("cv_1");
//        if (result == null)
//            result = "";
//        return result;
//    }
//
//    @Override
//    public String getOutStoreBillMasterStatus(String orgaid, String id) {
//        String result = null;
//        result = medicineInfoMapper.getOutStoreBillMasterStatus(orgaid, id);
//        if (result == null)
//            result = "";
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public String deleteOutStoreBillDetail(String orgaid, String deptId, String outStoreBillId, String detailId, String operatorId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", orgaid);
//        map.put("v_DeptID", deptId);
//        map.put("v_OutStoreBillID", outStoreBillId);
//        map.put("v_DetailID", detailId);
//        map.put("v_OperatorID", operatorId);
//        medicineInfoMapper.deleteOutStoreBillDetail(map);
//        result = (String) map.get("cv_1");
//        if (result == null)
//            result = "删除失败！";
//        else if (result.equals("ok"))
//            result = "删除成功！";
//        return result;
//
//    }
//
//    @Override
//    public DefineOutStoreBillMasterInfo getOutStoreBillMasterById(String orgaid, String deptId, String outStoreBillId) {
//        DefineOutStoreBillMasterInfo result = null;
//        result = medicineInfoMapper.getOutStoreBillMasterById(orgaid, deptId, outStoreBillId);
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public String wasterOutStoreBill(String orgaid, String deptId, String outStoreBillId, String operatorId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", orgaid);
//        map.put("v_DeptID", deptId);
//        map.put("v_OutStoreBillID", outStoreBillId);
//        map.put("v_OperatorID", operatorId);
//        medicineInfoMapper.wasterOutStoreBill(map);
//        result = (String) map.get("V_RERURN");
//        if (result == null)
//            result = "作废失败！";
//        else if (result.equals("ok"))
//            result = "作废成功！";
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public List<DefineStockBatchInfoOutStoreFastAdd> GetStockBatchInfoListForDrugCodeForOneStepOutStore(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower) {
//        List<DefineStockBatchInfoOutStoreFastAdd> result = null;
//        result = medicineInfoMapper.GetStockBatchInfoListForDrugCodeForOneStepOutStore(orgaid, deptId, drugCode, relativeDeptId, isAlterLower);
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public String saveOutStoreBillToAutomatic(String orgaid, String deptId, String userId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", orgaid);
//        map.put("v_DeptID", deptId);
//        map.put("v_UserID", userId);
//        medicineInfoMapper.saveOutStoreBillToAutomatic(map);
//        result = (String) map.get("V_RERURN");
//        if (result == null)
//            result = "保存失败！";
//        else if (result.equals("ok"))
//            result = "保存成功！";
//        else
//            result = "保存失败!" + result;
//        return result;
//    }
//
//    @Override
//    public String getCountryCodeByInsureCode(String orgaId, String itemYbCode, String type) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("V_OrgaId", orgaId);
//        map.put("V_ItemYbCode", itemYbCode);
//        map.put("V_Type", type);
//        medicineInfoMapper.getCountryCodeByInsureCode(map);
//        return (String) map.get("V_RERURN");
//
//    }
//
//    @Override
//    public List<DefineDrugBreakBillMasterInfo> getDrugBreakBillMasterList(String orgaId, String deptId, boolean isInputOrCheckDate, String startDate, String endDate, String checkId) {
//        String _endDate = "";
//
//        if (!PublicFunc.StringIsNullOrEmpty(endDate) && PublicFunc.IsNullString(endDate).contains("."))
//            _endDate = endDate.split(".")[0];
//        else
//            _endDate = endDate;
//
//        return medicineInfoMapper.getDrugBreakBillMasterList(orgaId, deptId, isInputOrCheckDate, startDate, _endDate, checkId);
//    }
//
//    @Override
//    public void getStockBatchInfoListForDrugCodeWithBreakProc(String orgaId, String deptId, String drugCode) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("drugCode", drugCode);
//        medicineInfoMapper.getStockBatchInfoListForDrugCodeWithBreakProc(map);
//    }
//
//    @Override
//    @Transactional
//    public List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithBreak(String orgaId, String deptId, String drugCode) {
//        getStockBatchInfoListForDrugCodeWithBreakProc(orgaId, deptId, drugCode);
//
//        return medicineInfoMapper.getStockBatchInfoListForDrugCodeWithBreak();
//    }
//
//    @Override
//    public void getDrugStoreBatchInfoListForDrugCodeWithBreakByProc(String orgaId, String deptId, String drugCode) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("drugCode", drugCode);
//        medicineInfoMapper.getDrugStoreBatchInfoListForDrugCodeWithBreakProc(map);
//    }
//
//    @Override
//    @Transactional
//    public List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListForDrugCodeWithBreak(String orgaId, String deptId, String drugCode) {
//
//        getDrugStoreBatchInfoListForDrugCodeWithBreakByProc(orgaId, deptId, drugCode);
//        return medicineInfoMapper.getDrugStoreBatchInfoListForDrugCodeWithBreak();
//    }
//
//    @Override
//    public List<DefineDrugBreakBillDetailInfoWithDrawal> getDrugBreakBillDetailListWithDrawal(String drugBreakBillId, String id) {
//        return medicineInfoMapper.getDrugBreakBillDetailListWithDrawal(drugBreakBillId, id);
//    }
//
//    @Override
//    public List<DefineDrugInfoForOrgaDrug> getDrugInfoListForOrgaDrug2(String orgaId, String drugId, String drugCode) {
//        return medicineInfoMapper.getDrugInfoListForOrgaDrug2(orgaId, drugId, drugCode);
//    }
//
//    @Override
//    public boolean wasterDrugBreakBillDetail(DefineStopDrugBreakBillDetailParamInfo dsisbdpi) {
//
//        String sResult = "";
//
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", PublicFunc.IsNullString(dsisbdpi.getOrgaId()));
//        map.put("deptId", PublicFunc.IsNullString(dsisbdpi.getDeptId()));
//        map.put("drugBreakBillId", PublicFunc.IsNullString(dsisbdpi.getDrugBreakBillId()));
//        map.put("drugBreakBillDetailId", PublicFunc.IsNullString(dsisbdpi.getDrugBreakBillDetailId()));
//        map.put("operatorId", PublicFunc.IsNullString(dsisbdpi.getOperatorId()));
//        medicineInfoMapper.wasterDrugBreakBillDetail(map);
//
//        sResult = (String) map.get("V_RETURN");
//
//        if (sResult.contains("出错"))
//            return false;
//        else
//            return true;
//    }
//
//    @Override
//    @Transactional
//    public boolean updateBreakMoney(String orgaId, String drugBreakBillId, String breakType) {
//        boolean bResult = false;
//
//        try {
//            BigDecimal breakAmout = medicineInfoMapper.getBreakAmountByPara(orgaId, drugBreakBillId);
//            medicineInfoMapper.updateBreakAmount(PublicFunc.IsNullString(breakAmout), breakType, orgaId, drugBreakBillId);
//            bResult = true;
//        } catch (Exception ex) {
//            bResult = false;
//        }
//        return bResult;
//
//    }
//
//    @Override
//    @Transactional
//    public int saveDrugBreakBill(boolean IsStock, MediDrugBreakBillMaster dbbm, List<MediDrugBreakBillDetail> dbbdList) {
//        int result = 1;
//        String serialNo = "14";
//        try {
//
//            //调用获取当前序列号方法
//            if (IsStock) //药库
//                serialNo = "14";
//            else
//                serialNo = "15";
//
//            dbbm.setBillNo(publicFunc.getCurrSerialNoExt(PublicFunc.IsNullString(dbbm.getOrgaId()), serialNo, PublicFunc.IsNullString(dbbm.getOperatorId())));
//            // dbbm.setBillNo("323432432");
//
//
//            dbbm.setId(UUID.randomUUID().toString());
//
//            BigDecimal breakAmount = new BigDecimal(0); //报损总金额
//            //Medi_DrugBreakBillDetail新增N条记录(N>=1)
//            for (MediDrugBreakBillDetail dbbdItem : dbbdList) {
//                dbbdItem.setBillNo(dbbm.getBillNo());
//                dbbdItem.setBreakBillId(dbbm.getId());
//                breakAmount = breakAmount.add(dbbdItem.getBreakAmount());//报损金额
//
//                dbbdItem.setId(KeyGeneratorUtils.getUUIDKey());
//                dbbdItem.setCreateDate(LocalDateTime.now());
//                dbbdItem.setInputDate(LocalDateTime.now());
//
//                mediDrugBreakBillDetailMapper.insert(dbbdItem);
//            }
//            //Medi_DrugBreakBillMaster新增一条记录
//            dbbm.setBreakAmount(breakAmount);
//            dbbm.setCreateDate(LocalDateTime.now());
//            dbbm.setInputDate(LocalDateTime.now());
//            mediDrugBreakBillMasterMapper.insert(dbbm);
//
//            result = 0;
//        } catch (RuntimeException exception) {
//            throw new RuntimeException("数据异常！" + exception.getMessage().toString());
//        }
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public Boolean drugAddAndUpdateBatch(List<MediDrugBreakBillDetail> dbbdAddList, List<MediDrugBreakBillDetail> dbbdEditList, String breakType) {
//        int iResult = 0;
//
//        try {
//
//            String drugBreakBillId = "", orgaId = "";
//            if (dbbdAddList != null) {
//                for (MediDrugBreakBillDetail dbbdItem : dbbdAddList) {
//
//                    dbbdItem.setId(KeyGeneratorUtils.getUUIDKey());
//                    dbbdItem.setCreateDate(LocalDateTime.now());
//                    dbbdItem.setInputDate(LocalDateTime.now());
//
//                    orgaId = dbbdItem.getOrgaId();
//                    drugBreakBillId = dbbdItem.getBreakBillId();
//
//                    mediDrugBreakBillDetailMapper.insert(dbbdItem);
//                }
//            }
//            //Medi_DrugBreakBillDetail修改N条记录(N>=1)
//            if (dbbdEditList != null) {
//                for (MediDrugBreakBillDetail dbbdItem : dbbdEditList) {
//                    orgaId = dbbdItem.getOrgaId();
//                    drugBreakBillId = dbbdItem.getBreakBillId();
//                    dbbdItem.setInputDate(LocalDateTime.now());
//
//                    QueryWrapper<MediDrugBreakBillDetail> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("OrgaId", dbbdItem.getOrgaId()).eq("ID", dbbdItem.getId());
//
//                    MediDrugBreakBillDetail detail = mediDrugBreakBillDetailMapper.selectOne(queryWrapper);
//
//                    if (detail != null) {
//                        mediDrugBreakBillDetailMapper.updateById(dbbdItem);
//                    }
//
//                }
//            }
//            //更新主单中报损总金额
//            updateBreakMoney(orgaId, drugBreakBillId, breakType);
//            iResult = 0;
//        } catch (RuntimeException exception) {
//            iResult = 2;
//        }
//
//        if (iResult == 0)
//            return true;
//        else
//            return false;
//    }
//
//    @Override
//    public String checkDrugBreakBill(DefineCheckDrugBreakBillParamInfo dcdbbpi) {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("orgaId", PublicFunc.IsNullString(dcdbbpi.getOrgaId()));
//        map.put("deptId", PublicFunc.IsNullString(dcdbbpi.getDeptId()));
//        map.put("drugBreakBillId", PublicFunc.IsNullString(dcdbbpi.getDrugBreakBillId()));
//        map.put("checkId", PublicFunc.IsNullString(dcdbbpi.getCheckId()));
//        map.put("isStork", PublicFunc.IsNullInt(dcdbbpi.getIsStork()));
//        medicineInfoMapper.checkDrugBreakBill(map);
//
//        return (String) map.get("CV_1");
//
//    }
//
//    @Override
//    @Transactional
//    public String reverseDrugBreakBill(List<DefineDrugBreakBillDetailInfoWithDrawal> isbdReverseList, String deptId) {
//        String result = "1";
//        //特殊处理
//        String drugBreakMasterId = "";
//        String operatorId = "";
//        BigDecimal amount = new BigDecimal(0);
//
//        try {
//
//            //根据冲销数逐一算出冲销金额
//            for (DefineDrugBreakBillDetailInfoWithDrawal item : isbdReverseList) {
//                drugBreakMasterId = item.getBreakBillId();
//                operatorId = item.getOperatorId();
//
//                // item.setCreateDate(LocalDateTime.now());
//                item.setInputDate(LocalDateTime.now());
//
//                //先改变原纪录的状态
//                if (PublicFunc.IsNullInt(item.getNumber()) == PublicFunc.IsNullInt(item.getDwAmount())) {
//                    item.setIsActive("停用");
//                }
//
//                updateOriginStatus(item);
//
//                //再进行根据冲销数逐一算出冲销金额
//                MediDrugBreakBillDetail isbd = new MediDrugBreakBillDetail();
//                //更新库存
//                if (medicineInfoMapper.isExistDeptByDeptId(deptId) > 0) {
//                    item.setNumber(0 - PublicFunc.IsNullInt(item.getDwAmount()));
//                    item.setPurchasePrice((item.getPurchasePrice().divide(PublicFunc.IsNullDecimal(item.getPackagQuantity()), 2, BigDecimal.ROUND_HALF_UP)).multiply(PublicFunc.IsNullDecimal(item.getNumber())).multiply(PublicFunc.IsNullDecimal(item.getPackagQuantity())));
//                    item.setRetailMoney((item.getRetailPrice().divide(PublicFunc.IsNullDecimal(item.getPackagQuantity()), 2, BigDecimal.ROUND_HALF_UP)).multiply(PublicFunc.IsNullDecimal(item.getNumber())).multiply(PublicFunc.IsNullDecimal(item.getPackagQuantity())));
//                    amount = amount.add(item.getPurchasePrice().divide(PublicFunc.IsNullDecimal(item.getPackagQuantity()), 2, BigDecimal.ROUND_HALF_UP)).multiply(PublicFunc.IsNullDecimal(item.getNumber()).multiply(PublicFunc.IsNullDecimal(item.getPackagQuantity())));
//                    isbd = setMediDrugBreakBillDetailByDefineItem(item);
//                    isbd.setId(UUID.randomUUID().toString());
//                    isbd.setBreakAmount((item.getPurchasePrice().divide(PublicFunc.IsNullDecimal(item.getPackagQuantity()), 2, BigDecimal.ROUND_HALF_UP))
//                            .multiply(PublicFunc.IsNullDecimal(item.getNumber()).multiply(PublicFunc.IsNullDecimal(item.getPackagQuantity()))));
//
//                    updateStockBatchByBreakWithDrawal(isbd, PublicFunc.IsNullInt(item.getDwAmount()), deptId);
//                    updateStockByBreakWithDrawal(isbd, PublicFunc.IsNullInt(item.getDwAmount()), deptId);
//                } else {
//                    item.setNumber(0 - PublicFunc.IsNullInt(item.getDwAmount()));
//                    item.setPurchaseMoney(item.getPurchasePrice().multiply(PublicFunc.IsNullDecimal(item.getNumber())));
//                    item.setRetailMoney(item.getRetailPrice().multiply(PublicFunc.IsNullDecimal(item.getNumber())));
//                    amount = amount.add(item.getPurchasePrice().multiply(PublicFunc.IsNullDecimal(item.getNumber())));
//                    isbd = setMediDrugBreakBillDetailByDefineItem(item);
//                    isbd.setId(UUID.randomUUID().toString());
//                    isbd.setBreakAmount(item.getPurchasePrice().multiply(PublicFunc.IsNullDecimal(item.getNumber())));
//                    //不是药库，那么就更新药房库存
//                    updateStoreBatchByBreakWithDrawal(isbd, PublicFunc.IsNullInt(item.getDwAmount()), deptId);
//                    updateStoreByBreakWithDrawal(isbd, PublicFunc.IsNullInt(item.getDwAmount()), deptId);
//                }
//
//                mediDrugBreakBillDetailMapper.insert(isbd);
//
//            }
//
//            //查询改单明细列表
//            List<DefineDrugBreakBillDetailInfoWithDrawal> isbdList = medicineInfoMapper.getDrugBreakBillDetailListWithDrawal(drugBreakMasterId, "");
//
//            //判断是否全部为0
//            int cnt = isbdList.stream().filter(p -> (p.getNumber() == 0)).collect(Collectors.toList()).size();
//            if (cnt == isbdList.size()) {
//                //将入库单作废
//                updateMasterByWithDrawal(amount, "已作废", "停用", drugBreakMasterId, operatorId);
//            } else {
//                updateMasterByWithDrawal(amount, "已审核", "在用", drugBreakMasterId, operatorId);
//            }
//
//
//        } catch (RuntimeException exception) {
//            result = exception.getMessage().toString();
//        }
//        return result;
//    }
//
//    public Boolean updateOriginStatus(DefineDrugBreakBillDetailInfoWithDrawal isbd) {
//        QueryWrapper<MediDrugBreakBillDetail> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", isbd.getId());
//        MediDrugBreakBillDetail isbd1 = mediDrugBreakBillDetailMapper.selectOne(queryWrapper);
//        try {
//
//            if (isbd1 == null) {
//                return false;
//            } else {
//                isbd1.setStatus("被冲销");
//                isbd1.setOperatorId(isbd.getOperatorId());
//                isbd1.setInputDate(isbd.getInputDate());
//                isbd1.setCreateUser(isbd.getCreateUser());
//                isbd1.setCreateDate(isbd.getCreateDate());
//                isbd1.setIsActive(isbd.getIsActive());
//            }
//
//            mediDrugBreakBillDetailMapper.updateById(isbd1);
//
//        } catch (RuntimeException exception) {
//            throw new RuntimeException("数据异常！" + exception.getMessage().toString());
//
//        }
//        return true;
//
//    }
//
//    public MediDrugBreakBillDetail setMediDrugBreakBillDetailByDefineItem(DefineDrugBreakBillDetailInfoWithDrawal item) {
//        MediDrugBreakBillDetail tmp = new MediDrugBreakBillDetail();
//        tmp.setBatch(item.getBatch());
//        tmp.setBatchNumber(item.getBatchNumber());
//        tmp.setBillNo(item.getBillNo());
//        tmp.setBreakAmount(item.getBreakAmount());
//        tmp.setBreakBillId(item.getBreakBillId());
//        tmp.setBreakReasonId(item.getBreakReasonId());
//        tmp.setBreakReasonName(item.getBreakReasonName());
//        tmp.setComment_(item.getComment());
//        tmp.setCreateDate(item.getCreateDate());
//        tmp.setCreateUser(item.getCreateUser());
//        tmp.setDrugCode(item.getDrugCode());
//        tmp.setDrugId(item.getDrugId());
//        tmp.setDrugInputCode(item.getDrugInputCode());
//        tmp.setDrugInputCode2(item.getDrugInputCode2());
//        tmp.setDrugInputCode3(item.getDrugInputCode3());
//        tmp.setDrugName(item.getDrugName());
//        tmp.setDrugSpecification(item.getDrugSpecification());
//        tmp.setId(item.getId());
//        tmp.setInputDate(item.getInputDate());
//        tmp.setIsActive(item.getIsActive());
//        tmp.setNumber(item.getNumber());
//        tmp.setOperatorId(item.getOperatorId());
//        tmp.setOrgaDrugId(item.getOrgaDrugId());
//        tmp.setOrgaId(item.getOrgaId());
//        tmp.setOrigin(item.getOrigin());
//        tmp.setPackagQuantity(item.getPackagQuantity());
//        tmp.setProcessTypeId(item.getProcessTypeId());
//        tmp.setProcessTypeName(item.getProcessTypeName());
//        tmp.setPurchaseMoney(item.getPurchaseMoney());
//        tmp.setPurchasePrice(item.getPurchasePrice());
//        tmp.setRetailMoney(item.getRetailMoney());
//        tmp.setRetailPrice(item.getRetailPrice());
//        tmp.setStatus(item.getStatus());
//        tmp.setStopDate(item.getStopDate());
//        tmp.setSupplierId(item.getSupplierId());
//        tmp.setSupplyCode(item.getSupplyCode());
//        tmp.setSupplyInputCode(item.getSupplyInputCode());
//        tmp.setSupplyName(item.getSupplyName());
//        tmp.setUnitId(item.getUnitId());
//        tmp.setUnitName(item.getUnitName());
//        tmp.setValIdityPeriod(item.getValidityPeriod());
//        return tmp;
//    }
//
//    public boolean updateStoreBatchByBreakWithDrawal(MediDrugBreakBillDetail isbd, int dwAmount, String deptId) {
//        try {
//
//            QueryWrapper<MediDrugStoreBatchInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("OrgaID", PublicFunc.IsNullString(isbd.getOrgaId()));
//            queryWrapper.eq("DeptID", PublicFunc.IsNullString(deptId));
//            queryWrapper.eq("DrugID", PublicFunc.IsNullString(isbd.getDrugId()));
//            queryWrapper.eq("OrgaDrugID", PublicFunc.IsNullString(isbd.getOrgaDrugId()));
//            queryWrapper.eq("batch", PublicFunc.IsNullString(isbd.getBatch()));
//            MediDrugStoreBatchInfo batchInfo = mediDrugStoreBatchInfoMapper.selectOne(queryWrapper);
//
//            if (batchInfo == null) {
//                return false;
//            } else {
//                batchInfo.setNumber_(batchInfo.getNumber_() + dwAmount);
//                batchInfo.setPurchaseMoney(batchInfo.getPurchaseMoney().subtract(isbd.getPurchaseMoney()));
//                batchInfo.setRetailMoney(batchInfo.getRetailMoney().subtract(isbd.getRetailMoney()));
//            }
//
//            mediDrugStoreBatchInfoMapper.update(batchInfo, queryWrapper);
//
//            return true;
//
//        } catch (RuntimeException exception) {
//            return false;
//        }
//    }
//
//    public boolean updateStockByBreakWithDrawal(MediDrugBreakBillDetail isbd, int dwAmount, String deptId) {
//        MediStockInfo si1 =
//                medicineInfoMapper.getMediStockInfoByParameters(deptId, PublicFunc.IsNullString(isbd.getDrugId()),
//                        PublicFunc.IsNullString(isbd.getOrgaDrugId()), PublicFunc.IsNullString(isbd.getOrgaId()));
//
//        /*
//        QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("OrgaId", PublicFunc.IsNullString(isbd.getOrgaId()));
//        queryWrapper.eq("DeptId", PublicFunc.IsNullString(deptId));
//        queryWrapper.eq("DrugId", PublicFunc.IsNullString(isbd.getDrugId()));
//        queryWrapper.eq("OrgaDrugId", PublicFunc.IsNullString(isbd.getOrgaDrugId()));
//
//         */
//
//        List<MediStockBatchInfo> batchInfo = medicineInfoMapper.getStockBatchInfoByDrugBreak(PublicFunc.IsNullString(isbd.getOrgaId()),
//                PublicFunc.IsNullString(deptId), PublicFunc.IsNullString(isbd.getDrugId()), PublicFunc.IsNullString(isbd.getOrgaDrugId()), "", null);
//
//        if (si1 == null && batchInfo == null) {
//            return false;
//        } else {
//            si1.setInventory(batchInfo.stream().mapToInt(MediStockBatchInfo::getNumber).sum());
//            si1.setPriceInventory(batchInfo.stream().mapToInt(MediStockBatchInfo::getPriceNumber).sum());
//        }
//
//        medicineInfoMapper.updateMediStockInfo(si1);
//        return true;
//    }
//
//    public boolean updateStockBatchByBreakWithDrawal(MediDrugBreakBillDetail isbd, int dwAmount, String deptId) {
//        /*
//        QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("OrgaId", PublicFunc.IsNullString(isbd.getOrgaId()));
//        queryWrapper.eq("DeptId", PublicFunc.IsNullString(deptId));
//        queryWrapper.eq("DrugId", PublicFunc.IsNullString(isbd.getDrugId()));
//        queryWrapper.eq("OrgaDrugId", PublicFunc.IsNullString(isbd.getOrgaDrugId()));
//        queryWrapper.eq("Batch", PublicFunc.IsNullString(isbd.getBatch()));
//
//        MediStockBatchInfo batchInfo = mediStockBatchInfoMapper.selectOne(queryWrapper);
//
//         */
//        List<MediStockBatchInfo> batchInfoList = medicineInfoMapper.getStockBatchInfoByDrugBreak(PublicFunc.IsNullString(isbd.getOrgaId()),
//                PublicFunc.IsNullString(deptId), PublicFunc.IsNullString(isbd.getDrugId()), PublicFunc.IsNullString(isbd.getOrgaDrugId()),
//                PublicFunc.IsNullString(isbd.getBatch()), "true");
//
//        if (batchInfoList == null || batchInfoList.size() <= 0)
//            return false;
//
//        MediStockBatchInfo batchInfo = batchInfoList.get(0);
//
//        if (batchInfo == null) {
//            return false;
//        } else {
//            batchInfo.setNumber((batchInfo.getNumber() * batchInfo.getPackagQuantity() + dwAmount * isbd.getPackagQuantity() + batchInfo.getPriceNumber()) / batchInfo.getPackagQuantity());
//            batchInfo.setPriceNumber(((batchInfo.getNumber() * batchInfo.getPackagQuantity() + dwAmount * isbd.getPackagQuantity() + batchInfo.getPriceNumber()) % batchInfo.getPackagQuantity()));
//            batchInfo.setPurchaseMoney(batchInfo.getPurchaseMoney().subtract(isbd.getPurchaseMoney()));
//            batchInfo.setRetailMoney(batchInfo.getRetailMoney().subtract(isbd.getRetailMoney()));
//        }
//
//        //mediStockBatchInfoMapper.update(batchInfo,queryWrapper);
//        medicineInfoMapper.updateMediStockBatchInfo(batchInfo);
//        return true;
//    }
//
//
//    public boolean updateStoreByBreakWithDrawal(MediDrugBreakBillDetail isbd, int dwAmount, String deptId) {
//        QueryWrapper<MediDrugStoreInfo> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("OrgaId", PublicFunc.IsNullString(isbd.getOrgaId()));
//        queryWrapper.eq("DeptId", PublicFunc.IsNullString(deptId));
//        queryWrapper.eq("DrugId", PublicFunc.IsNullString(isbd.getDrugId()));
//        queryWrapper.eq("OrgaDrugId", PublicFunc.IsNullString(isbd.getOrgaDrugId()));
//
//        MediDrugStoreInfo si1 = mediDrugStoreInfoMapper.selectOne(queryWrapper);
//
//        if (si1 == null) {
//            return false;
//        } else {
//            si1.setInventory(si1.getInventory() + dwAmount);
//            si1.setPurchaseMoney(si1.getPurchaseMoney().subtract(isbd.getPurchaseMoney()));
//            si1.setRetailMoney(si1.getRetailMoney().subtract(isbd.getRetailMoney()));
//        }
//
//        mediDrugStoreInfoMapper.update(si1, queryWrapper);
//
//        return true;
//    }
//
//    //Update主表价格和状态
//    public boolean updateMasterByWithDrawal(BigDecimal amount, String status, String isActive, String drugBreakMasterId, String operatorId) {
//
//        QueryWrapper<MediDrugBreakBillMaster> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", drugBreakMasterId);
//
//        MediDrugBreakBillMaster isbm1 = mediDrugBreakBillMasterMapper.selectOne(queryWrapper);
//
//        if (isbm1 == null) {
//            return false;
//        } else {
//            isbm1.setBreakAmount(isbm1.getBreakAmount().add(amount));
//            isbm1.setStatus(status);
//            isbm1.setIsActive(isActive);
//            //isbm1.InputDate = DateTime.Now;
//            isbm1.setOperatorId(operatorId);
//        }
//
//        mediDrugBreakBillMasterMapper.update(isbm1, queryWrapper);
//
//        return true;
//    }
//
//    @Override
//    @Transactional
//    public DefineDrugBreakBillPrint getDrugBreakBillPrintMaster(String orgaId, String drugBreakBillId, String orgaName, String userName) {
//        try {
//
//            Map<String, String> map = new HashMap<>();
//            map.put("orgaId", PublicFunc.IsNullString(orgaId));
//            map.put("drugBreakBillId", PublicFunc.IsNullString(drugBreakBillId));
//            medicineInfoMapper.getDrugBreakBillPrintMasterByProc(map);
//            DefineDrugBreakBillPrintMasterInfo master = medicineInfoMapper.getDrugBreakBillPrintMaster();
//            if (master == null)
//                return null;
//
//            DefineDrugBreakBillPrint printMaster = new DefineDrugBreakBillPrint();
//            printMaster.setOrgaName(orgaName);
//            printMaster.setId(master.getId());
//            printMaster.setOrgaId(master.getOrgaId());
//            printMaster.setDeptId(master.getDeptId());
//            printMaster.setDeptInputCode(master.getDeptInputCode());
//            printMaster.setDeptCode(master.getDeptCode());
//            printMaster.setDeptName("库房：" + master.getDeptName());
//            printMaster.setCheckDate("审核日期" + master.getCheckDate());
//            printMaster.setCheckId(master.getCheckId());
//            printMaster.setBillNo("编号：" + master.getBillNo());
//            printMaster.setBillMemo(master.getBillMemo());
//            printMaster.setIsActive(master.getIsActive());
//            printMaster.setStopDate(master.getStopDate());
//            printMaster.setStatus(master.getStatus());
//            printMaster.setOperatorId(master.getOperatorId());
//            printMaster.setInputDate(master.getInputDate());
//            printMaster.setCreateUser(master.getCreateUser());
//            printMaster.setCreateDate("填制日期" + master.getCreateDate());
//            printMaster.setComment("摘要：" + master.getComment());
//            printMaster.setTitleReport(orgaName + "药品报损单");
//            printMaster.setCheckName(master.checkName);
//            printMaster.setPrintDate("打印日期：" + LocalDateTime.now());
//            printMaster.setPrintOperator("打印人：" + userName);
//
//            return printMaster;
//        } catch (RuntimeException exception) {
//            return null;
//        }
//    }
//
//    @Override
//    @Transactional
//    public List<DefineDrugBreakDetailPrint> getDrugBreakBillPrintDetail(String orgaId, String drugBreakBillId, String comment, String printOperator) {
//        int pageMax = 10;//每页最大10个
//        int onePageCnt = 0;//每页明细计数
//        int cnt = 0;//总计数
//        int page = 0;//页数
//        BigDecimal sumPurchaseMoney = BigDecimal.ZERO;
//        BigDecimal sumRetailMoney = BigDecimal.ZERO;
//        BigDecimal sumBreakAmount = BigDecimal.ZERO;
//        BigDecimal curPurchaseMoney = BigDecimal.ZERO;
//        BigDecimal curSumRetailMoney = BigDecimal.ZERO;
//        BigDecimal curSumBreakAmount = BigDecimal.ZERO;
//
//        try {
//            Map<String, String> map = new HashMap<>();
//            map.put("orgaId", PublicFunc.IsNullString(orgaId));
//            map.put("drugBreakBillId", PublicFunc.IsNullString(drugBreakBillId));
//            medicineInfoMapper.getDrugBreakBillPrintDetailByProc(map);
//            List<DefineDrugBreakBillPrintDetailInfo> detailInfoList = medicineInfoMapper.getDrugBreakBillPrintDetail();
//
//            if (detailInfoList == null)
//                return null;
//
//            List<DefineDrugBreakDetailPrint> dataList = new ArrayList<>();
//            while (onePageCnt < detailInfoList.size()) {
//
//                onePageCnt = 0;
//
//                List<DefineDrugBreakBillPrintDetailInfo> DrugBreakBillPrintDetailInfoListPage = new ArrayList<>();
//                DefineDrugBreakDetailPrint print = new DefineDrugBreakDetailPrint();
//
//                while (onePageCnt < pageMax) {
//
//                    if (cnt < detailInfoList.size()) {
//                        DrugBreakBillPrintDetailInfoListPage.add(detailInfoList.get(cnt));
//                    } else {
//                        DrugBreakBillPrintDetailInfoListPage.add(new DefineDrugBreakBillPrintDetailInfo());
//                    }
//                    onePageCnt = onePageCnt + 1;
//                    cnt = cnt + 1;
//                }
//
//                page = page + 1;
//                print.detail = DrugBreakBillPrintDetailInfoListPage;
//                print.setComment(comment);
//                print.setPrinterName(printOperator);
//
//
//                sumPurchaseMoney = detailInfoList.stream().map(p -> p.getPurchaseMoney() == null ? BigDecimal.ZERO : p.getPurchaseMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
//                sumRetailMoney = detailInfoList.stream().map(p -> p.getRetailMoney() == null ? BigDecimal.ZERO : p.getRetailMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
//                sumBreakAmount = detailInfoList.stream().map(p -> p.getBreakAmount() == null ? BigDecimal.ZERO : p.getBreakAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
//                curPurchaseMoney = DrugBreakBillPrintDetailInfoListPage.stream().map(p -> p.getPurchaseMoney() == null ? BigDecimal.ZERO : p.getPurchaseMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
//                curSumRetailMoney = DrugBreakBillPrintDetailInfoListPage.stream().map(p -> p.getRetailMoney() == null ? BigDecimal.ZERO : p.getRetailMoney()).reduce(BigDecimal.ZERO, BigDecimal::add);
//                curSumBreakAmount = DrugBreakBillPrintDetailInfoListPage.stream().map(p -> p.getBreakAmount() == null ? BigDecimal.ZERO : p.getBreakAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
//
//                print.setMoneyInfo("总进价金额：" + sumPurchaseMoney + " 总零售金额：" + sumRetailMoney
//                        + " 总报损金额：" + sumBreakAmount + " 本页进价金额：" + curPurchaseMoney
//                        + " 本页零售金额：" + curSumRetailMoney + " 本页报损金额：" + curSumBreakAmount);
//
//                print.setPageInfo("第" + page + "页/共" + ((detailInfoList.size() % pageMax) == 0 ? (detailInfoList.size() / pageMax) : (detailInfoList.size() / pageMax) + 1) + "页");
//                print.setInfoCnt("本页" + DrugBreakBillPrintDetailInfoListPage.stream().filter(p -> p.getId() != null).collect(Collectors.toList()).size()
//                        + "种/共" + detailInfoList.size() + "种药品");
//
//                dataList.add(print);
//            }
//
//            return dataList;
//        } catch (RuntimeException exception) {
//            throw new RuntimeException("数据异常！" + exception.getMessage().toString());
//        }
//    }
//
//    @Override
//    public List<DefineDrugStoreQueryInfo> getDrugStoreQuery(QueryOrgaDrugInfo info) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("orgaId", PublicFunc.IsNullString(info.getOrgaId()));
//        map.put("orgaDrugId", PublicFunc.IsNullString(info.getId()));
//        map.put("drugId", PublicFunc.IsNullString(info.getDrugId()));
//        medicineInfoMapper.getDrugStoreQuery(map);
//        if (map.get("CUR_OUT") == null) {
//            return null;
//        }
//        List<DefineDrugStoreQueryInfo> result = (List<DefineDrugStoreQueryInfo>) map.get("CUR_OUT");
//        return result;
//
//    }
//
//    @Override
//    public boolean wasterDrugBreakBill(String orgaId, String deptId, String drugBreakBillId, String operatorId) {
//
//        String sResult = "";
//
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", PublicFunc.IsNullString(orgaId));
//        map.put("deptId", PublicFunc.IsNullString(deptId));
//        map.put("drugBreakBillId", PublicFunc.IsNullString(drugBreakBillId));
//        map.put("operatorId", PublicFunc.IsNullString(operatorId));
//        medicineInfoMapper.wasterDrugBreakBill(map);
//
//        sResult = (String) map.get("V_RETURN");
//
//        if (sResult.contains("出错"))
//            return false;
//        else
//            return true;
//    }
//
//    @Override
//    public List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMaster(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status) {
//        Map<String, Object> map = new HashMap<>();
//        if (orgaidlist != null && orgaidlist.size() > 0)
//            map.put("s", orgaidlist);
//        map.put("dateType", dateType);
//        map.put("startDate", startDate);
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("endDate", endDate);
//        map.put("executor", executor);
//        map.put("status", status);
//        return medicineInfoMapper.getInteragencyOutStoreBillMaster(map);
//    }
//
//    @Override
//    public List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCount(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status) {
//        Map<String, Object> map = new HashMap<>();
//        if (orgaidlist != null && orgaidlist.size() > 0)
//            map.put("s", orgaidlist);
//        map.put("dateType", dateType);
//        map.put("startDate", startDate);
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("endDate", endDate);
//        map.put("executor", executor);
//        map.put("status", status);
//        return medicineInfoMapper.getInteragencyOutStoreBillMasterMoneyCount(map);
//    }
//
//    @Override
//    @Transactional
//    public String checkInteragencyOutStoreBill(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId, String userId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", PublicFunc.IsNullString(outStoreBillMaster.orgaId));
//        map.put("v_DeptID", PublicFunc.IsNullString(outStoreBillMaster.deptId));
//        map.put("f_v_RelativeOrgaID", PublicFunc.IsNullString(relativeOrgaId));
//        map.put("f_v_RelativeDeptID", PublicFunc.IsNullString(outStoreBillMaster.relativeDeptId));
//        map.put("f_v_ReceivingDeptInputCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptInputCode));
//        map.put("f_v_ReceivingDeptCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptCode));
//        map.put("f_v_ReceivingDeptName", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptName));
//        map.put("v_OutStoreBillID", PublicFunc.IsNullString(outStoreBillMaster.id));
//        map.put("v_OperatorID", PublicFunc.IsNullString(userId));
//        medicineInfoMapper.checkInteragencyOutStoreBill(map);
//        result = (String) map.get("cv_1");
//        return result;
//    }
//
//    @Override
//    public SysOrgaDept getRelativeOrganizationByDept(String deptId) {
//        return medicineInfoMapper.getRelativeOrganizationByDept(deptId);
//    }
//
//    @Override
//    public List<DefineOutStoreBillMasterInfo> getInteragencyOutStoreBillMasterCS(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status) {
//        Map<String, Object> map = new HashMap<>();
//        if (orgaidlist != null && orgaidlist.size() > 0)
//            map.put("s", orgaidlist);
//        map.put("dateType", dateType);
//        map.put("startDate", startDate);
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("endDate", endDate);
//        map.put("executor", executor);
//        map.put("status", status);
//        return medicineInfoMapper.getInteragencyOutStoreBillMasterCS(map);
//    }
//
//    @Override
//    public List<DefineStoreBillMasterMoneyCount> getInteragencyOutStoreBillMasterMoneyCountCS(List<String> orgaidlist, String dateType, String startDate, String orgaId, String deptId, String endDate, String executor, String status) {
//        Map<String, Object> map = new HashMap<>();
//        if (orgaidlist != null && orgaidlist.size() > 0)
//            map.put("s", orgaidlist);
//        map.put("dateType", dateType);
//        map.put("startDate", startDate);
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("endDate", endDate);
//        map.put("executor", executor);
//        map.put("status", status);
//        return medicineInfoMapper.getInteragencyOutStoreBillMasterMoneyCountCS(map);
//    }
//
//    @Override
//    public List<MediStockBatchInfo> getStockBatchInfoListForDrugCodeWithPageFilter(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD, Integer limitNum, Integer offsetNum) {
//        if (drugCode != null && !drugCode.equals(""))
//            drugCode = drugCode.toUpperCase();
//        if (isOpenRMFFreezingOrgaD != null && isOpenRMFFreezingOrgaD != "" && isOpenRMFFreezingOrgaD.equals("是"))
//            isOpenRMFFreezingOrgaD = "true";
//        return medicineInfoMapper.getStockBatchInfoListForDrugCodeWithPageFilter(orgaid, deptId, drugCode, relativeDeptId, isAlterLower, relativeOrgaId, isZero, isOpenRMFFreezingOrgaD, limitNum, offsetNum);
//    }
//
//    @Override
//    public Integer getStockBatchInfoListForDrugCodeCount(String orgaid, String deptId, String drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String isOpenRMFFreezingOrgaD) {
//        if (drugCode != null && !drugCode.equals(""))
//            drugCode = drugCode.toUpperCase();
//        if (isOpenRMFFreezingOrgaD != null && isOpenRMFFreezingOrgaD != "" && isOpenRMFFreezingOrgaD.equals("是"))
//            isOpenRMFFreezingOrgaD = "true";
//        return medicineInfoMapper.getStockBatchInfoListForDrugCodeCount(orgaid, deptId, drugCode, relativeDeptId, isAlterLower, relativeOrgaId, isZero, isOpenRMFFreezingOrgaD);
//    }
//
//    @Override
//    @Transactional
//    public String checkOutStoreBillToCS(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", PublicFunc.IsNullString(outStoreBillMaster.orgaId));
//        map.put("v_DeptID", PublicFunc.IsNullString(outStoreBillMaster.deptId));
//        map.put("f_v_RelativeOrgaID", PublicFunc.IsNullString(relativeOrgaId));
//        map.put("f_v_RelativeDeptID", PublicFunc.IsNullString(outStoreBillMaster.relativeDeptId));
//        map.put("f_v_ReceivingDeptInputCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptInputCode));
//        map.put("f_v_ReceivingDeptCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptCode));
//        map.put("f_v_ReceivingDeptName", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptName));
//        map.put("v_OutStoreBillID", PublicFunc.IsNullString(outStoreBillMaster.id));
//        map.put("v_OperatorID", PublicFunc.IsNullString(outStoreBillMaster.operatorId));
//        medicineInfoMapper.checkOutStoreBillToCS(map);
//        result = (String) map.get("cv_1");
//        if (result == null || !result.equals("执行成功"))
//            throw new RuntimeException(result);
//        return result;
//    }
//
//    @Override
//    @Transactional
//    public String checkInteragencyOutStoreBillCS(DefineOutStoreBillMasterInfo outStoreBillMaster, String relativeOrgaId) {
//        String result = "";
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", PublicFunc.IsNullString(outStoreBillMaster.orgaId));
//        map.put("v_DeptID", PublicFunc.IsNullString(outStoreBillMaster.deptId));
//        map.put("f_v_RelativeOrgaID", PublicFunc.IsNullString(relativeOrgaId));
//        map.put("f_v_RelativeDeptID", PublicFunc.IsNullString(outStoreBillMaster.relativeDeptId));
//        map.put("f_v_ReceivingDeptInputCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptInputCode));
//        map.put("f_v_ReceivingDeptCode", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptCode));
//        map.put("f_v_ReceivingDeptName", PublicFunc.IsNullString(outStoreBillMaster.receivingDeptName));
//        map.put("v_OutStoreBillID", PublicFunc.IsNullString(outStoreBillMaster.id));
//        map.put("v_OperatorID", PublicFunc.IsNullString(outStoreBillMaster.operatorId));
//        medicineInfoMapper.checkInteragencyOutStoreBillCS(map);
//        result = (String) map.get("cv_1");
//        if (result == null || !result.equals("执行成功"))
//            throw new RuntimeException(result);
//        return result;
//    }
//
//    @Override
//    public List<SysOrgaDept> getRelativeOrgaDeptIsDrugStore(String orgaId, String relativeDeptId) {
//        return medicineInfoMapper.getRelativeOrgaDeptIsDrugStore(orgaId, relativeDeptId);
//
//    }
//
//    @Override
//    public List<DefineMediStockBatchInfoCS> getStockBatchInfoListForDrugCodeCS(String orgaid, String deptId, String
//            drugCode, String relativeDeptId, String isAlterLower, String relativeOrgaId, String isZero, String
//                                                                                       isOpenRMFFreezingOrgaD) {
//        if (drugCode != null && !drugCode.equals(""))
//            drugCode = drugCode.toUpperCase();
//        if (isOpenRMFFreezingOrgaD != null && isOpenRMFFreezingOrgaD != "" && isOpenRMFFreezingOrgaD.equals("是"))
//            isOpenRMFFreezingOrgaD = "true";
//        return medicineInfoMapper.getStockBatchInfoListForDrugCodeCS(orgaid, deptId, drugCode, relativeDeptId, isAlterLower, relativeOrgaId, isZero, isOpenRMFFreezingOrgaD);
//    }
//
//    // region【进销存上传服务】
//    @Transactional
//    @Override
//    public List<DefineJXYJDrugReturnInvoiceInfo> getJXYJDrugReturnInvoiceList(String orgaId, String
//            startDate, String endDate, String deptId, String billNo, String flag) {
//        medicineInfoMapper.getJXYJDrugReturnInvoice(orgaId, startDate, endDate, deptId, billNo, flag);
//        return medicineInfoMapper.getJXYJDrugReturnInvoiceList();
//
//    }
//
//    @Transactional
//    @Override
//    public List<DefineJXYJDrugInvoiceDetailInfo> getJXYJDrugInvoiceDetailList(String orgaId, String invoiceNumber) {
//        medicineInfoMapper.getJXYJDrugInvoiceDetail(orgaId, invoiceNumber);
//        return medicineInfoMapper.getJXYJDrugInvoiceDetailList();
//    }
//
//    @Transactional
//    @Override
//    public boolean updateDrugInvoiceJXYJ(String orgaId, String masterId, String invoiceNumber, String flag, String
//            status, String operatorId) {
//        boolean resultBool = false;
//        if (flag.equals("1"))//发票上传状态
//        {
//            Integer selectCount = medicineInfoMapper.getMediDrugInvoiceJXYJCount(orgaId, invoiceNumber);
//            if (selectCount > 0) {
//                resultBool = medicineInfoMapper.updateMediDrugInvoiceJXYJ(orgaId, invoiceNumber, operatorId) > 0 ? true : false;
//            } else {
//                Map map = new HashMap<>();
//                map.put("id", UUID.randomUUID().toString());
//                map.put("orgaId", orgaId);
//                map.put("invoiceNumber", invoiceNumber);
//                map.put("status", status);
//                map.put("operatorId", operatorId);
//                resultBool = medicineInfoMapper.insertMediDrugInvoiceJXYJ(map) > 0 ? true : false;
//            }
//        }
//        if (flag.equals("2"))//随同单上传状态
//        {
//            Integer selectCount = medicineInfoMapper.getMediDrugInvoiceDeatailJXYJCount(orgaId, masterId);
//            if (selectCount > 0) {
//                resultBool = medicineInfoMapper.updateMediDrugInvoiceDetailJXYJ(orgaId, masterId, operatorId) > 0 ? true : false;
//            } else {
//                Map map = new HashMap<>();
//                map.put("id", UUID.randomUUID().toString());
//                map.put("orgaId", orgaId);
//                map.put("masterId", masterId);
//                map.put("status", status);
//                map.put("operatorId", operatorId);
//                resultBool = medicineInfoMapper.insertMediDrugInvoiceDetailJXYJ(map) > 0 ? true : false;
//            }
//        }
//        if (flag.equals("3"))//发票和随同单上传状态
//        {
//            Integer selectCount = medicineInfoMapper.getMediDrugInvoiceBillJXYJCount(orgaId, masterId);
//            if (selectCount > 0) {
//                resultBool = medicineInfoMapper.updateMediDrugInvoiceBillJXYJ(orgaId, masterId, operatorId) > 0 ? true : false;
//            } else {
//                Map map = new HashMap<>();
//                map.put("id", UUID.randomUUID().toString());
//                map.put("orgaId", orgaId);
//                map.put("masterId", masterId);
//                map.put("status", status);
//                map.put("operatorId", operatorId);
//                resultBool = medicineInfoMapper.insertMediDrugInvoiceBillJXYJ(map) > 0 ? true : false;
//            }
//        }
//        if (flag.equals("4"))//药品出库上传状态
//        {
//            Integer selectCount = medicineInfoMapper.getMediDrugRetuInvoiceJXYJCount(orgaId, masterId);
//            if (selectCount > 0) {
//                resultBool = medicineInfoMapper.updateMediDrugRetuInvoiceJXYJ(orgaId, masterId, operatorId) > 0 ? true : false;
//            } else {
//                Map map = new HashMap<>();
//                map.put("id", UUID.randomUUID().toString());
//                map.put("orgaId", orgaId);
//                map.put("masterId", masterId);
//                map.put("status", status);
//                map.put("operatorId", operatorId);
//                resultBool = medicineInfoMapper.insertMediDrugRetuInvoiceJXYJ(map) > 0 ? true : false;
//            }
//        }
//        return resultBool;
//    }
//
//    @Override
//    public ResultObject savePurchaseNationDrugInfoList(List<PurchaseNationDrugInfo> drugResultList) {
//        ResultObject object = new ResultObject();
//        object.success = true;
//        if (drugResultList != null && drugResultList.size() > 0) {
//            for (PurchaseNationDrugInfo p : drugResultList) {
//                Map map = new HashMap();
//                map.put("orgaId", p.orgaId);
//                map.put("platFormCode", p.platFormCode);
//                map.put("tenderYear", p.tenderYear);
//                map.put("productId", p.productId);
//                map.put("drugName", p.drugName);
//                map.put("drugMode", p.drugMode);
//                map.put("drugSpec", p.drugSpec);
//                map.put("miniPack", p.miniPack);
//                map.put("price", p.price);
//                map.put("productSpec", p.productSpec);
//                map.put("packMaterials", p.packMaterials);
//                map.put("commType", p.commType);
//                map.put("factoryName", p.factoryName);
//                map.put("tradeName", p.tradeName);
//                map.put("status", p.status);
//                map.put("modifDate", p.modifDate);
//                map.put("authorizeNumber", p.authorizeNumber);
//                map.put("drugNationcode", p.drugNationcode);
//                map.put("isactive", p.isactive);
//                map.put("stopdate", p.stopdate);
//                map.put("operatorId", p.operatorid);
//                map.put("createuser", p.createuser);
//                map.put("comment", p.comment);
//                map.put("nameChn", p.nameChn);
//                map.put("standRate", p.standRate);
//                map.put("specUnit", p.specUnit);
//                map.put("basicFlag", p.basicFlag);
//                medicineInfoMapper.savePurchaseNationDrugInfoList(map);
//                String rst = map.get("V_RETURN").toString();
//                if (!rst.equals("更新成功")) {
//                    throw new RuntimeException("保存失败" + rst);
//                } else {
//                    object.message = rst;
//                    object.success = true;
//                }
//            }
//        }
//        return object;
//    }
//
//    @Transactional
//    @Override
//    public List<DefineJXYJDrugInvoiceInfo> getJXYJDrugInvoiceList(String orgaId, String startDate, String
//            endDate, String invoiceNumber, String deptId, String flag) {
//        List<DefineJXYJDrugInvoiceInfo> result = new ArrayList<>();
//        medicineInfoMapper.getJXYJDrugInvoice(orgaId, startDate, endDate, invoiceNumber, deptId, flag);
//        result = medicineInfoMapper.getJXYJDrugInvoiceList();
//        return result;
//    }
//
//    @Transactional
//    @Override
//    public List<DefineJXYJDrugInfo> getJXYJDrugInfoList(String orgaId, String searchText) {
//        List<DefineJXYJDrugInfo> result = new ArrayList<>();
//        medicineInfoMapper.getJXYJDrugInfo(orgaId, searchText);
//        result = medicineInfoMapper.getJXYJDrugInfoList();
//        return result;
//    }
//
//    // endregion
//    @Override
//    public List<DefineDrugStoreBatchInfoS> getDrugStoreBatchInfoListForDrugCodeWithStockBatch(String orgaId, String
//            relativeOrgaId, String deptId, String relativeDeptId, String drugCode, String deptFlag) {
//        return medicineInfoMapper.getDrugStoreBatchInfoListForDrugCodeWithStockBatch(orgaId, relativeOrgaId, deptId, relativeDeptId, drugCode, deptFlag);
//    }
//
//    @Override
//    public boolean checkVillageDrugRefundingBill(String orgaId, String deptId, String relativeOrgaId, String
//            relativeDeptId, String masterId, String isDrugStore, String isStock, String operatorId) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("orgaid", orgaId);
//        queryWrapper.eq("drugRefundingBillId", masterId);
//        List<MediDrugRefundingBillDetail> details = mediDrugRefundingBillDetailMapper.selectList(queryWrapper);
//        if (details == null || (details != null && details.size() == 0))
//            throw new RuntimeException("未查询到退库单信息!");
//        for (MediDrugRefundingBillDetail detail : details) {
//            MediStockBatchInfo batchNow = new MediStockBatchInfo();
//            MediStockBatchInfo batchStock = new MediStockBatchInfo();
//            MediDrugStoreBatchInfo batchDrugStore = new MediDrugStoreBatchInfo();
//            Map<String, String> map = new HashMap<>();
//            map.put("orgaId", relativeOrgaId);
//            map.put("deptId", relativeDeptId);
//            map.put("drugid", detail.getDrugId());
//            map.put("batch", detail.getBatch());
//            List<MediStockBatchInfo> mediStockBatchInfos = medicineInfoMapper.getStockBatchInfo(map);
//            if (mediStockBatchInfos == null || mediStockBatchInfos.size() == 0)
//                throw new RuntimeException("未查询到相对部门库存信息!");
//            batchNow = mediStockBatchInfos.get(0);
//            if (isStock.equals("是")) {
//                Map<String, String> map2 = new HashMap<>();
//                map2.put("orgaId", detail.getOrgaId());
//                map2.put("deptId", deptId);
//                map2.put("drugid", detail.getDrugId());
//                map2.put("orgaDrugId", detail.getOrgaDrugId());
//                map2.put("batch", detail.getBatch());
//                List<MediStockBatchInfo> mediStockBatchInfos2 = medicineInfoMapper.getStockBatchInfo(map2);
//                if (mediStockBatchInfos == null || mediStockBatchInfos.size() == 0)
//                    throw new RuntimeException("未查询到当前药库库存信息!");
//                batchStock = mediStockBatchInfos2.get(0);
//                if (detail.getNumber_() > batchStock.number)
//                    throw new RuntimeException(batchStock.drugName + "的退库数量大于库存量，请修改或删除该明细！");
//            }
//            if (isDrugStore.equals("是")) {
//                Map<String, String> map3 = new HashMap<>();
//                map3.put("orgaId", detail.getOrgaId());
//                map3.put("deptId", deptId);
//                map3.put("drugid", detail.getDrugId());
//                map3.put("orgaDrugId", detail.getOrgaDrugId());
//                map3.put("batch", detail.getBatch());
//                List<MediDrugStoreBatchInfo> mediDrugStockBatchInfos = medicineInfoMapper.getDrugStoreBatchInfo(map3);
//                if (mediDrugStockBatchInfos == null || mediDrugStockBatchInfos.size() == 0)
//                    throw new RuntimeException("未查询到当前药房库存信息!");
//                batchDrugStore = mediDrugStockBatchInfos.get(0);
//                if (detail.getNumber_() > batchDrugStore.number_)
//                    throw new RuntimeException(batchDrugStore.drugName + "的退库数量大于库存量，请修改或删除该明细！");
//            }
//        }
//        Integer master = medicineInfoMapper.checkVillageDrugRefundingBill(masterId);
//        Integer detail = medicineInfoMapper.checkVillageDrugRefundingBillDetail(masterId);
//        return master > 0 && detail > 0 ? true : false;
//
//
//    }
//
//    @Override
//    @Transactional
//    public boolean checkVillageDrugRefundingBillNew(String orgaId, String deptId, String relativeOrgaId, String
//            relativeDeptId, String masterId, String isDrugStore, String isStock, String operatorId) {
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("orgaid", orgaId);
//        queryWrapper.eq("drugRefundingBillId", masterId);
//        List<MediDrugRefundingBillDetail> details = mediDrugRefundingBillDetailMapper.selectList(queryWrapper);
//        if (details == null || (details != null && details.size() == 0))
//            throw new RuntimeException("未查询到退库单信息!");
//        for (MediDrugRefundingBillDetail detail : details) {
//            MediStockBatchInfo batchNow = new MediStockBatchInfo();
//            MediStockBatchInfo batchStock = new MediStockBatchInfo();
//            MediDrugStoreBatchInfo batchDrugStore = new MediDrugStoreBatchInfo();
//            if (isStock.equals("是")) {
//                Map<String, String> map2 = new HashMap<>();
//                map2.put("orgaId", detail.getOrgaId());
//                map2.put("deptId", deptId);
//                map2.put("drugid", detail.getDrugId());
//                map2.put("orgaDrugId", detail.getOrgaDrugId());
//                map2.put("batch", detail.getBatch());
//                List<MediStockBatchInfo> mediStockBatchInfos2 = medicineInfoMapper.getStockBatchInfo(map2);
//                batchStock = mediStockBatchInfos2.get(0);
//                if (detail.getNumber_() > batchStock.number)
//                    throw new RuntimeException(batchStock.drugName + "的退库数量大于库存量，请修改或删除该明细！");
//            }
//            if (isDrugStore.equals("是")) {
//                Map<String, String> map3 = new HashMap<>();
//                map3.put("orgaId", detail.getOrgaId());
//                map3.put("deptId", deptId);
//                map3.put("drugid", detail.getDrugId());
//                map3.put("orgaDrugId", detail.getOrgaDrugId());
//                map3.put("batch", detail.getBatch());
//                List<MediDrugStoreBatchInfo> mediDrugStockBatchInfos = medicineInfoMapper.getDrugStoreBatchInfo(map3);
//                if (mediDrugStockBatchInfos == null || mediDrugStockBatchInfos.size() == 0)
//                    throw new RuntimeException("未查询到当前药房库存信息!");
//                batchDrugStore = mediDrugStockBatchInfos.get(0);
//                if (detail.getNumber_() > batchDrugStore.number_)
//                    throw new RuntimeException(batchDrugStore.drugName + "的退库数量大于库存量，请修改或删除该明细！");
//            }
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("v_OrgaID", orgaId);
//        map.put("v_DeptID", deptId);
//        map.put("v_RelativeOrgaID", relativeOrgaId);
//        map.put("v_DrugRefundingBillID", masterId);
//        map.put("v_CheckID", operatorId);
//        medicineInfoMapper.checkVillageDrugRefundingBillNew(map);
//        if (!PublicFunc.IsNullString(map.get("cv_1")).equals("ok"))
//            throw new RuntimeException(map.get("cv_1"));
//        return true;
//    }
//
//    @Override
//    public boolean saveVDrugRefundingBillMasterNew(String orgaId, DefineDrugRefundingBillMasterInfo
//            masterInfo, List<DefineDrugStoreBatchInfoS> addInfos, List<DefineDrugRefundingBillDetailInfoWithDrawalForVillage> batchInfos, String
//                                                           operatorId) {
//        boolean addMode = false;
//        String billNo = "";
//        String DrugRefundingBillId = "";
//        MediDrugRefundingBillMaster master = new MediDrugRefundingBillMaster();
//        //新增
//        if (masterInfo != null && PublicFunc.StringIsNullOrEmpty(masterInfo.getId())) {
//            addMode = true;
//            billNo = publicFunc.getCurrSerialNoExt(orgaId, "07", operatorId);
//            DrugRefundingBillId = UUID.randomUUID().toString();
//            master.setCreateDate(LocalDateTime.now());
//            master.setCreateUser(operatorId);
//        } else //修改
//        {
//            billNo = masterInfo.getBillNo();
//            DrugRefundingBillId = masterInfo.getId();
//        }
//        master.setId(DrugRefundingBillId);
//        master.setBillNo(billNo);
//        master.setDeptId(masterInfo.getDeptId());
//        master.setOrgaId(masterInfo.getOrgaId());
//        master.setDeptCode(masterInfo.getDeptCode());
//        master.setDeptName(masterInfo.getDeptName());
//        master.setDeptInputCode(masterInfo.getDeptInputCode());
//        master.setBillMemo(masterInfo.getBillMemo());
//        master.setInputDate(LocalDateTime.now());
//        master.setIsActive("在用");
//        master.setStatus("未审核");
//        master.setOperatorId(operatorId);
//        master.setRelateDeptCode(masterInfo.getRelateDeptCode());
//        master.setRelateDeptId(masterInfo.getRelateDeptId());
//        master.setRelateDeptInputCode(masterInfo.getRelateDeptInputCode());
//        master.setRelateDeptName(masterInfo.getRelateDeptName());
//
//        BigDecimal addMoney = BigDecimal.ZERO;
//        if ((addMode && (addInfos == null || addInfos.size() == 0)) || (!addMode && (batchInfos == null || batchInfos.size() == 0)))
//            throw new RuntimeException("明细不能为空！");
//        if (addInfos != null) {
//            for (DefineDrugStoreBatchInfoS info : addInfos) {
//                if (info.getReturnAmount() == null || info.getReturnAmount() == 0)
//                    throw new RuntimeException(info.getDrugCode() + info.getDrugName() + ",批次:" + info.getBatch() + "未填写数量!");
//                QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("orgaid", info.getOrgaId());
//                queryWrapper.eq("orgadrugid", info.getOrgaDrugId());
//                queryWrapper.eq("drugid", info.getDrugId());
//                queryWrapper.eq("batch", info.getBatch());
//                queryWrapper.eq("deptid", info.getDeptId());
//                MediDrugStoreBatchInfo mediDrugStoreBatchInfo = mediDrugStoreBatchInfoMapper.selectOne(queryWrapper);
//                if (mediDrugStoreBatchInfo == null)
//                    throw new RuntimeException(info.getDrugCode() + info.getDrugName() + ",批次:" + info.getBatch() + ",未查询到库存信息!");
//                if ((mediDrugStoreBatchInfo.getNumber_() - mediDrugStoreBatchInfo.getVirtualInventory()) < info.getReturnAmount())
//                    throw new RuntimeException(info.getDrugCode() + info.getDrugName() + ",批次:" + info.getBatch() + ",库存不足!");
//                QueryWrapper queryWrapper2 = new QueryWrapper();
//                queryWrapper2.eq("orgaid", info.getOrgaId());
//                queryWrapper2.eq("drugRefundingBillId", master.getId());
//                queryWrapper2.eq("orgadrugid", info.getOrgaDrugId());
//                queryWrapper2.eq("drugid", info.getDrugId());
//                queryWrapper2.eq("batch", info.getBatch());
//                queryWrapper2.eq("isactive", "在用");
//                MediDrugRefundingBillDetail detail1 = mediDrugRefundingBillDetailMapper.selectOne(queryWrapper2);
//                if (detail1 != null)
//                    throw new RuntimeException(detail1.getDrugCode() + detail1.getDrugName() + ",批次:" + detail1.getBatch() + "已存在!");
//                MediDrugRefundingBillDetail detail = new MediDrugRefundingBillDetail();
//                detail.setBatch(info.getBatch());
//                detail.setBatchNumber(info.getBatchNumber());
//                detail.setBillNo(billNo);
//                detail.setComment_(info.getComment_());
//                detail.setCreateDate(LocalDateTime.now());
//                detail.setCreateUser(operatorId);
//                detail.setDrugCode(info.getDrugCode());
//                detail.setDrugId(info.getDrugId());
//                detail.setDrugInputCode(info.getDrugInputCode());
//                detail.setDrugInputCode2(info.getDrugInputCode2());
//                detail.setDrugInputCode3(info.getDrugInputCode3());
//                detail.setDrugName(info.getDrugName());
//                detail.setDrugSpecification(info.getDrugSpecification());
//                detail.setId(UUID.randomUUID().toString());
//                detail.setInputDate(LocalDateTime.now());
//                detail.setIsActive("在用");
//                detail.setNumber_(info.getReturnAmount());
//                detail.setOperatorId(operatorId);
//                detail.setOrgaDrugId(info.getOrgaDrugId());
//                detail.setOrgaId(info.getOrgaId());
//                detail.setOrigin(info.getOrigin());
//                detail.setDrugRefundingBillId(DrugRefundingBillId);
//                detail.setPackagQuantity(info.getPackagQuantity());
//                detail.setPurchasePrice(info.getPurchasePrice());
//                detail.setRetailMoney(new BigDecimal(info.getReturnAmount()).multiply(info.getRetailPrice()));
//                detail.setPurchaseMoney(new BigDecimal(info.getReturnAmount()).multiply(info.getPurchasePrice()));
//                detail.setExpenditureAmount(new BigDecimal(info.getReturnAmount()).multiply(info.getPurchasePrice()));
//                detail.setRetailPrice(info.getRetailPrice());
//                detail.setStatus("未审核");
//                detail.setSupplierId(info.getSupplierId());
//                detail.setSupplyCode(info.getSupplyCode());
//                detail.setSupplyInputCode(info.getSupplyInputCode());
//                detail.setSupplyName(info.getSupplyName());
//                detail.setUnitId(info.getUnitId());
//                detail.setUnitName(info.getUnitName());
//                detail.setValidityPeriod(info.getValidityPeriod());
//                if (mediDrugRefundingBillDetailMapper.insert(detail) == 0)
//                    throw new RuntimeException("插入明细失败！");
//            }
//            addMoney = addInfos.stream().map(p -> new BigDecimal(p.getReturnAmount()).multiply(p.getPurchasePrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
//        }
//        BigDecimal updateMoney = BigDecimal.ZERO;
//        if (batchInfos != null) {
//            for (DefineDrugRefundingBillDetailInfoWithDrawalForVillage detail : batchInfos) {
//                if (detail.getReturnAmount() == null || detail.getReturnAmount() == 0)
//                    throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + ",批次:" + detail.getBatch() + "未填写数量!");
//                QueryWrapper queryWrapper = new QueryWrapper();
//                queryWrapper.eq("orgaid", detail.getOrgaId());
//                queryWrapper.eq("orgadrugid", detail.getOrgaDrugId());
//                queryWrapper.eq("drugid", detail.getDrugId());
//                queryWrapper.eq("batch", detail.getBatch());
//                queryWrapper.eq("deptid", master.getDeptId());
//                MediDrugStoreBatchInfo mediDrugStoreBatchInfo = mediDrugStoreBatchInfoMapper.selectOne(queryWrapper);
//                if (mediDrugStoreBatchInfo == null)
//                    throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + ",批次:" + detail.getBatch() + ",未查询到库存信息!");
//                if ((mediDrugStoreBatchInfo.getNumber_() - mediDrugStoreBatchInfo.getVirtualInventory()) < detail.getReturnAmount())
//                    throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + ",批次:" + detail.getBatch() + ",库存不足!");
//                QueryWrapper queryWrapper2 = new QueryWrapper();
//                queryWrapper2.eq("orgaid", detail.getOrgaId());
//                queryWrapper2.eq("drugRefundingBillId", master.getId());
//                queryWrapper2.eq("orgadrugid", detail.getOrgaDrugId());
//                queryWrapper2.eq("drugid", detail.getDrugId());
//                queryWrapper2.eq("batch", detail.getBatch());
//                queryWrapper2.eq("isactive", "在用");
//                MediDrugRefundingBillDetail detail1 = mediDrugRefundingBillDetailMapper.selectOne(queryWrapper2);
//                if (detail1 != null) {
//                    detail1.setNumber_(detail.getReturnAmount());
//                    detail1.setOperatorId(detail.getOperatorId());
//                    detail1.setInputDate(LocalDateTime.now());
//                    detail1.setRetailMoney(new BigDecimal(detail.getReturnAmount()).multiply(detail.getRetailPrice()));
//                    detail1.setPurchaseMoney(new BigDecimal(detail.getReturnAmount()).multiply(detail.getPurchasePrice()));
//                    detail1.setExpenditureAmount(new BigDecimal(detail.getReturnAmount()).multiply(detail.getPurchasePrice()));
//                    if (mediDrugRefundingBillDetailMapper.updateById(detail1) == 0)
//                        throw new RuntimeException("更新明细失败！");
//                }
//            }
//            updateMoney = batchInfos.stream().map(p -> new BigDecimal(p.getReturnAmount()).multiply(p.getPurchasePrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
//        }
//        master.setExpenditureAmount(PublicFunc.IsNullDecimal(updateMoney).add(PublicFunc.IsNullDecimal(addMoney)));
//        if (addMode) {
//            if (mediDrugRefundingBillMasterMapper.insert(master) == 0)
//                throw new RuntimeException("新增主表失败！");
//        } else {
//            if (mediDrugRefundingBillMasterMapper.updateById(master) == 0)
//                throw new RuntimeException("更新主表失败！");
//        }
//        return true;
//    }
//
//    @Override
//    public List<DefineDrugRefundingBillMasterInfo> getDrugRefundingBillMasterListForVillageCheck(String
//                                                                                                         orgaId, String deptId, String startDate, String endDate, String checkId, String dateType, String requestOrg) {
//        String[] orgaList = null;
//        if (requestOrg != null && !requestOrg.equals("")) {
//            orgaList = requestOrg.split(",");
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("startDate", startDate);
//        map.put("endDate", endDate);
//        map.put("checkId", checkId);
//        map.put("orgaList", orgaList);
//        return medicineInfoMapper.getDrugRefundingBillMasterListForVillageCheck(map);
//    }
//
//    @Override
//    @Transactional
//    public boolean checkDrugRefundingBillForV(String orgaId, String deptId, String relativeOrgaId, String
//            masterId, String operatorId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("relativeOrgaId", relativeOrgaId);
//        map.put("masterId", masterId);
//        map.put("operatorId", operatorId);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("orgaid", orgaId);
//        queryWrapper.eq("drugRefundingBillId", masterId);
//        List<MediDrugRefundingBillDetail> details = mediDrugRefundingBillDetailMapper.selectList(queryWrapper);
//        if (details == null || (details != null && details.size() == 0))
//            throw new RuntimeException("未查询到退库单信息!");
//        for (MediDrugRefundingBillDetail detail : details) {
//            QueryWrapper queryWrapper2 = new QueryWrapper();
//            queryWrapper2.eq("orgaid", orgaId);
//            queryWrapper2.eq("orgadrugid", detail.getOrgaDrugId());
//            queryWrapper2.eq("drugid", detail.getDrugId());
//            queryWrapper2.eq("batch", detail.getBatch());
//            queryWrapper2.eq("deptid", deptId);
//            MediDrugStoreBatchInfo mediDrugStoreBatchInfo = mediDrugStoreBatchInfoMapper.selectOne(queryWrapper2);
//            if (mediDrugStoreBatchInfo == null)
//                throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + "批次:" + detail.getBatch() + ",未查询到库存信息!");
//            if ((mediDrugStoreBatchInfo.getNumber_() - mediDrugStoreBatchInfo.getVirtualInventory()) < detail.getNumber_())
//                throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + "批次:" + detail.getBatch() + ",库存不足!");
//            if (detail != null && detail.getStatus().equals("已审核"))
//                throw new RuntimeException("该笔单据已审核!");
//        }
//        medicineInfoMapper.checkDrugRefundingBillForV(map);
//        if (!PublicFunc.IsNullString(map.get("vReturn")).equals("ok"))
//            throw new RuntimeException(map.get("vReturn"));
//        return true;
//    }
//
//    @Override
//    @Transactional
//    public boolean checkDrugRefundingBillForVNew(String orgaId, String deptId, String relativeOrgaId, String
//            masterId, String operatorId) {
//        Map<String, String> map = new HashMap<>();
//        map.put("orgaId", orgaId);
//        map.put("deptId", deptId);
//        map.put("relativeOrgaId", relativeOrgaId);
//        map.put("masterId", masterId);
//        map.put("operatorId", operatorId);
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("orgaid", orgaId);
//        queryWrapper.eq("drugRefundingBillId", masterId);
//        List<MediDrugRefundingBillDetail> details = mediDrugRefundingBillDetailMapper.selectList(queryWrapper);
//        if (details == null || (details != null && details.size() == 0))
//            throw new RuntimeException("未查询到退库单信息!");
//        for (MediDrugRefundingBillDetail detail : details) {
//            QueryWrapper queryWrapper2 = new QueryWrapper();
//            queryWrapper2.eq("orgaid", orgaId);
//            queryWrapper2.eq("orgadrugid", detail.getOrgaDrugId());
//            queryWrapper2.eq("drugid", detail.getDrugId());
//            queryWrapper2.eq("batch", detail.getBatch());
//            queryWrapper2.eq("deptid", deptId);
//            MediDrugStoreBatchInfo mediDrugStoreBatchInfo = mediDrugStoreBatchInfoMapper.selectOne(queryWrapper2);
//            if (mediDrugStoreBatchInfo == null)
//                throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + "批次:" + detail.getBatch() + ",未查询到库存信息!");
//            if ((mediDrugStoreBatchInfo.getNumber_() - mediDrugStoreBatchInfo.getVirtualInventory()) < detail.getNumber_())
//                throw new RuntimeException(detail.getDrugCode() + detail.getDrugName() + "批次:" + detail.getBatch() + ",库存不足!");
//            if (detail != null && detail.getStatus().equals("已审核"))
//                throw new RuntimeException("该笔单据已审核!");
//        }
//        medicineInfoMapper.checkDrugRefundingBillForVNew(map);
//        if (!PublicFunc.IsNullString(map.get("vReturn")).equals("ok"))
//            throw new RuntimeException(map.get("vReturn"));
//        return true;
//    }
//
//    @Override
//    public List<DefineDrugRefundingBillMasterInfoForV> getDrugRefundingBillMasterListForVillage(String
//                                                                                                        orgaId, String deptId, String startDate, String endDate, String checkId, String dateType, String status) {
//        return medicineInfoMapper.getDrugRefundingBillMasterListForVillage(orgaId, deptId, startDate, endDate, checkId, dateType, status);
//    }
//
//    @Override
//    public List<DefineDrugRefundingBillDetailInfoWithDrawalForVillage> getDrugRefundingBillDetailListWithDrawalForVillage
//            (String drugRefundingBillID) {
//        return medicineInfoMapper.getDrugRefundingBillDetailListWithDrawalForVillage(drugRefundingBillID);
//    }
//
//    @Override
//    @Transactional
//    public JSONObject getDrugRefundingBillPrintDataForVillage(String orgaId, String masterId) {
//        //获取主表信息
//        medicineInfoMapper.printDrugRefundingBillMaster(orgaId, masterId);
//        MediDrugRefundingBillMaster masterInfo = medicineInfoMapper.getDrugRefundingBillPrintMaster();
//        if (masterInfo == null) {
//            throw new RuntimeException("未获取到主表数据");
//        }
//        //明细数据
//        returnStockMapper.printDrugRefundingBillDetail(orgaId, masterId);
//        List<MediDrugRefundingBillDetail> drugRefundingBillDetail = returnStockMapper.getDrugRefundingBillPrintDetail();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("MediDrugRefundingBillMaster", masterInfo);
//        jsonObject.put("MediDrugRefundingBillDetail", drugRefundingBillDetail);
//        return jsonObject;
//    }
//
//
//    @Transactional
//    @Override
//    public List<DefineJXYJDrugReturnInvoiceDetailInfo> getJXYJDrugReturnInvoiceDetailInfo(String orgaId, String
//            masterId) {
//        List<DefineJXYJDrugReturnInvoiceDetailInfo> infos = new ArrayList<>();
//        medicineInfoMapper.getJXYJDrugReturnInvoiceDetailInfo(orgaId, masterId);
//        infos = medicineInfoMapper.getJXYJDrugReturnInvoiceDetailInfoList();
//        return infos;
//
//    }
//
//    /**
//     * 退货打印信息获取
//     *
//     * @param orgaId
//     * @param masterId
//     * @param orderStatus
//     * @return
//     */
//    @Override
//    @Transactional
//    public JSONObject getRefundingBillPrintData(String orgaId, String masterId, String orderStatus) {
//        //获取主表信息
//        medicineInfoMapper.getRefundingBillPrintMasterProc(orgaId, masterId);
//        List<MediRefundingBillMaster> master = medicineInfoMapper.getRefundingBillMasterPrint();
//        if (master == null || master.size() == 0) {
//            return null;
//        }
//        //获取明细信息
//        medicineInfoMapper.getRefundingBillPrintDetailProc(orgaId, masterId);
//        List<MediRefundingBillDetail> details = medicineInfoMapper.getRefundingBillDetailPrint();
//        if (orderStatus.equals("1")) {
//            details = details.stream().sorted(Comparator.comparing(MediRefundingBillDetail::getDrugCode)).collect(Collectors.toList());
//        } else if (orderStatus.equals("2")) {
//            details = details.stream().sorted(Comparator.comparing(MediRefundingBillDetail::getDrugName)).collect(Collectors.toList());
//        }
//        JSONObject resultJson = new JSONObject();
//        resultJson.put("MediRefundingBillMaster", master.get(0));
//        resultJson.put("MediRefundingBillDetail", details);
//        return resultJson;
//    }
//
//    @Override
//    public String relativeOrgaDrug(String orgaId, String drugId) {
//        String strResult = "";
//        List<String> result = medicineInfoMapper.relativeOrgaDrug(orgaId, drugId);
//        if (result != null && result.size() > 0) {
//            for (String item : result) {
//                strResult += "相对机构" + item + "该药品正在使用 ";
//            }
//        }
//        return strResult;
//    }
//
//
//    @Override
//    public Integer getStockBatchInfoCount(String orgaId, String deptId, String startDate, String endDate, String
//            batchNumber, String drugNameOrCode) {
//        Integer stockCount = medicineInfoMapper.getStockBatchInfoCount(orgaId, deptId, startDate, endDate, batchNumber, drugNameOrCode.toUpperCase());
//        return stockCount;
//    }
//
//    @Override
//    public List<MediStockBatchInfo> getStockBatchInfoListWithPageFilter(Integer limitNum, Integer offsetNum, String
//            orgaId, String deptId, String startDate, String endDate, String batchNumber, String drugNameOrCode) {
//        List<MediStockBatchInfo> result = medicineInfoMapper.getStockBatchInfoListWithPageFilter(limitNum, offsetNum, orgaId, deptId, startDate, endDate, batchNumber, drugNameOrCode.toUpperCase());
//        if (result != null && result.size() > 0) {
//            for (MediStockBatchInfo stock : result) {
//                stock.comment="";
//                if (stock.priceNumber != 0) {
//                    String a = stock.drugSpecification.split("/")[0];
//                    stock.comment = String.format("%s%s%s%s", stock.number, stock.unitName, stock.priceNumber, a.substring(a.length() - 1));
//                } else
//                    stock.comment = String.format("%s%s", stock.number, stock.unitName);
//
//            }
//        }
//        return result;
//    }
//
//    @Override
//    public boolean updateStockBatchInfo(MediStockBatchInfo info) {
//        Integer resultCount=0;
//        Map map=new HashMap();
//        map.put("validityPeriod", info.validityPeriod.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//        map.put("batchNumber",info.batchNumber);
//        map.put("operatorID",info.operatorID);
//        map.put("inputDate",info.inputDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//        map.put("id",info.id);
//        map.put("orgaId",info.orgaID);
//        map.put("deptId",info.deptID);
//        map.put("producedate",info.produceDate);
//        resultCount =medicineInfoMapper.updateStockBatchInfo(map);
//      if(resultCount>0)
//          return true;
//      else
//          return false;
//    }
//
//    @Override
//    public Integer getDrugStoreBatchInfoCount(String orgaId, String deptId, String startDate, String
//            endDate, String batchNumber, String drugNameOrCode) {
//        Integer drugStockCount = medicineInfoMapper.getDrugStoreBatchInfoCount(orgaId, deptId, startDate, endDate, batchNumber, drugNameOrCode.toUpperCase());
//        return drugStockCount;
//    }
//
//    @Override
//    public List<MediDrugStoreBatchInfo> getDrugStoreBatchInfoListWithPageFilter(Integer limitNum, Integer
//            offsetNum, String orgaId, String deptId, String startDate, String endDate, String batchNumber, String
//                                                                                        drugNameOrCode) {
//        List<MediDrugStoreBatchInfo> result = medicineInfoMapper.getDrugStoreBatchInfoListWithPageFilter(limitNum, offsetNum, orgaId, deptId, startDate, endDate, batchNumber, drugNameOrCode.toUpperCase());
//        return result;
//    }
//
//    @Override
//    public boolean UpdateDrugStoreBatchInfo(MediDrugStoreBatchInfo info) {
//        Integer resultCount=0;
//        Map map=new HashMap();
//        map.put("validityPeriod", info.validityPeriod.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//        map.put("batchNumber",info.batchNumber);
//        map.put("operatorID",info.operatorID);
//        map.put("inputDate",info.inputDate.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));
//        map.put("id",info.id);
//        map.put("orgaId",info.orgaID);
//        map.put("deptId",info.deptID);
//        map.put("produceDate",info.produceDate);
//        resultCount =medicineInfoMapper.UpdateDrugStoreBatchInfo(map);
//        if(resultCount>0)
//            return true;
//        else
//            return false;
//    }
//
//    @Override
//    public List<MediStockInfo> getStockInfoListDiffQuery(String orgaId, String deptId, String drugStoreInputCode) {
//        List<String> deptIdList=new ArrayList<>();
//        if (deptId!=null &&!deptId.isEmpty()&&deptId.contains(","))
//            deptIdList=Arrays.asList(deptId.split(","));
//        return medicineInfoMapper.getStockInfoListDiffQuery(orgaId, deptId, deptIdList,drugStoreInputCode);
//    }
//
//    @Override
//    public List<DrugStoreFreezeList> getDrugStoreBatchDiffQuery(String orgaId, String deptId, String includeZeroDrug, String drugStoreInputCode) {
//        List<String> deptIdList=new ArrayList<>();
//        if (deptId!=null &&!deptId.isEmpty()&&deptId.contains(","))
//            deptIdList=Arrays.asList(deptId.split(","));
//        return medicineInfoMapper.getDrugStoreBatchDiffQuery(orgaId, deptId, deptIdList,includeZeroDrug,drugStoreInputCode);
//    }
//
//    @Override
//    public List<DrugStoreFreezeList> getStoreBatchDiffQuery(String orgaId, String deptId, String includeZeroDrug, String drugStoreInputCode) {
//        List<String> deptIdList=new ArrayList<>();
//        if (deptId!=null &&!deptId.isEmpty()&&deptId.contains(","))
//            deptIdList=Arrays.asList(deptId.split(","));
//        return medicineInfoMapper.getStoreBatchDiffQuery(orgaId, deptId, deptIdList,includeZeroDrug,drugStoreInputCode);
//    }
//
//    @Override
//    public String isMatchInterDrugInfo(String countryCode) {
//        return medicineInfoMapper.isMatchInterDrugInfo(countryCode.trim());
//    }
//
//    @Override
//    public List<MediOrgaCataLogInfo> getMediOrgaCataLogInfoList(String orgaId) {
//        QueryWrapper<MediOrgaCataLogInfo> qw =new QueryWrapper<>();
//        qw.eq("orgaId",orgaId).eq("isActive","在用");
//        qw.orderByDesc("parentid is null")
//                .orderByAsc("catalogcode");
//        List<MediOrgaCataLogInfo> infoList = mediOrgaCataLogInfoMapper.selectList(qw);
//        return infoList;
//    }
//
//    @Override
//    public List<MediOrgaDrugCataLogInfo> getMediOrgaDrugCataLogInfo(String orgaId, String orgaCataLogId,String searchText) {
//        QueryWrapper<MediOrgaDrugCataLogInfo> qw = new QueryWrapper<MediOrgaDrugCataLogInfo>();
//        qw.eq("orgaId", orgaId)
//                .eq("orgaCataLogId", orgaCataLogId)
//                .orderByAsc("createdate")
//                .orderByAsc("drugName");
//        if (StringUtil.isNotBlank(searchText)) {
//            String search = searchText.toUpperCase();
//            qw.and(x->x.like("drugCode",search)
//                    .or().like("drugName",search)
//                    .or().like("drugInputCode",search)
//                    .or().like("drugInputCode2",search)
//                    .or().like("drugInputCode3",search));
//        }
//        List<MediOrgaDrugCataLogInfo> drugCataLogInfos = mediOrgaDrugCataLogInfoMapper.selectList(qw);
//        return drugCataLogInfos;
//    }
//
//    @Override
//    public Page<MediOrgaDrugCataLogInfo> getCataLogDrugInfoList(String orgaId, String searchText, Integer pageNum, Integer pageSize) {
//        Page<MATSuppyInfo> page = new Page<>(pageNum, pageSize);
//        Page<MediOrgaDrugCataLogInfo> cataLogDrugInfoList = medicineInfoMapper.getCataLogDrugInfoList(page, orgaId, searchText);
//        return cataLogDrugInfoList;
//    }
//
//    @Override
//    @Transactional
//    public Boolean addMediOrgaDrugCataLogInfo(List<MediOrgaDrugCataLogInfo> infos) {
//        for (MediOrgaDrugCataLogInfo info : infos) {
//            info.setId(UUID.randomUUID().toString());
//            info.setCreateDate(LocalDateTime.now());
//            if (mediOrgaDrugCataLogInfoMapper.insert(info)<=0) {
//                throw new RuntimeException("分类明细插入失败!");
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean delMediOrgaDrugCataLogInfo(List<MediOrgaDrugCataLogInfo> infos) {
//        for (MediOrgaDrugCataLogInfo info : infos) {
//            if (StringUtil.isBlank(info.getId())) {
//                throw new RuntimeException("入参缺失:明细ID为空!");
//            }
//            if (mediOrgaDrugCataLogInfoMapper.deleteById(info.getId())<=0) {
//                throw new RuntimeException("分类明细删除失败!");
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public Boolean updateMediOrgaCataLogInfo(MediOrgaCataLogInfo info,Integer status) {
//        //0添加,1修改,2删除
//        if (status == 0) {
//            info.setId(UUID.randomUUID().toString());
//            info.setCatalogInputCode(PinyinUtil.getFirstLetter(info.getCatalogName(),"").toUpperCase());//输入码拼音
//            info.setInputDate(LocalDateTime.now());
//            info.setCreateDate(LocalDateTime.now());
//            info.setIsActive("在用");
//            if (mediOrgaCataLogInfoMapper.insert(info)<=0) {
//                throw new RuntimeException("新增分类目录失败!");
//            }
//        } else  if (status == 1) {
//            if (StringUtil.isBlank(info.getId())) {
//                throw new RuntimeException("缺少关键参数ID!");
//            }
//            MediOrgaCataLogInfo logInfo = mediOrgaCataLogInfoMapper.selectOne(new QueryWrapper<MediOrgaCataLogInfo>()
//                    .eq("id", info.getId()).eq("isActive","在用"));
//            if (logInfo == null) {
//                throw new RuntimeException("未寻到需要修改的目录!");
//            }
//            logInfo.setCatalogCode(info.getCatalogCode());
//            logInfo.setCatalogName(info.getCatalogName());
//            logInfo.setCatalogInputCode((PinyinUtil.getFirstLetter(info.getCatalogName(),"")).toUpperCase());//输入码拼音
//            logInfo.setInputDate(LocalDateTime.now());
//            logInfo.setOperatorId(info.getOperatorId());
//            if (mediOrgaCataLogInfoMapper.updateById(logInfo)<=0) {
//                throw new RuntimeException("修改分类目录失败!");
//            }
//        } else  if (status == 2) {
//            if (StringUtil.isBlank(info.getId())) {
//                throw new RuntimeException("缺少关键参数ID!");
//            }
//            //判断是否存在关系
//            List<MediOrgaCataLogInfo> logInfos = mediOrgaCataLogInfoMapper.selectList(
//                    new QueryWrapper<MediOrgaCataLogInfo>().eq("orgaId", info.getOrgaId()));
//            List<String> ids =new ArrayList<>();
//            ids.add(info.getId());
//            ids = getIds(logInfos,ids,info.getId(),info.getOrgaId());
//            QueryWrapper<MediOrgaDrugCataLogInfo> isqw = new QueryWrapper<MediOrgaDrugCataLogInfo>();
//            isqw.or().in("orgaCatalogId",ids);
//            isqw.and(x->x.eq("orgaId",info.getOrgaId()));
//            if (mediOrgaDrugCataLogInfoMapper.selectCount(isqw)>0) {
//                throw new RuntimeException("目录中存在设置的分类关系,请先解除分类关系!");
//            }
//            MediOrgaCataLogInfo logInfo = mediOrgaCataLogInfoMapper.selectOne(new QueryWrapper<MediOrgaCataLogInfo>()
//                    .eq("id", info.getId()).eq("isActive","在用"));
//            if (logInfo == null) {
//                throw new RuntimeException("未寻到需要删除的目录!");
//            }
//            logInfo.setInputDate(LocalDateTime.now());
//            logInfo.setOperatorId(info.getOperatorId());
//            logInfo.setIsActive("停用");
//            if (mediOrgaCataLogInfoMapper.updateById(logInfo)<=0) {
//                throw new RuntimeException("删除分类目录失败!");
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 判断目录下是否存在关系
//     * @return
//     */
//    private List<String> getIds(List<MediOrgaCataLogInfo> logInfos,List<String> ids,String id,String orgaId){
//        List<String> listCata = logInfos.stream().filter(p -> id.equals(PublicFunc.IsNullString(p.getParentId()))).map(MediOrgaCataLogInfo::getId).collect(Collectors.toList());
//        if (listCata == null || listCata.size() == 0) {
//            return ids;
//        }
//        ids.addAll(listCata);
//        for (String s : listCata) {
//            getIds(logInfos, ids, s, orgaId);
//        }
//        return ids;
//    }
}
