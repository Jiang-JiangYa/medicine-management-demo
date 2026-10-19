package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.*;
import org.example.myspringboot.service.impl.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.myspringboot.entity.generateBillno.generateInStoreCode;

/**
 * 功能：药品入库管理业务
 * 作者：yangjia
 * 日期：2025/3/21 下午3:49
 */
@Data
@RestController

@RequestMapping("/his/medicine")
@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
public class drugStorageManagementController {
    @Resource
    MediInstoreBillMasterImpl mediInstoreBillMaster;   //药品入库主表

    @Resource
    MediInstoreBillDetailImpl mediInstoreBillDetail;   //药品入库明细表

    @Resource
    MediStockBatchInfoImpl mediStockBatchInfo;   //药库批次表

    @Resource
    MediStockInfoImpl mediStockInfo;    //药库库存表

    @Resource
    MedicineDrugInfoImpl mediDrugInfo;     //药品信息表

    /** 多条件查询主表数据， */
    @PostMapping("/getInStoreBillMasterList")
    public Result getInStoreBillMasterList(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            Boolean isInputOrCheckDate = paramsJson.getBoolean("isInputOrCheckDate");  //false:录单  true：审核
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            String checkId = paramsJson.getString("checkId");     //审核人ID
            Boolean inStoreType = paramsJson.getBoolean("inStoreTypeId");   //是否票货不符，票货不符id为2
            Boolean isChecked = paramsJson.getBoolean("isChecked");     //是否审核
            String supplierinfo = paramsJson.getString("supplierInfo").toUpperCase();     //供应商拼音或id
            //多条件
            QueryWrapper<MediInstoreBillMaster> queryWrapper = new QueryWrapper<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //前端格式
            // 独立处理状态条件
            if (isChecked) {
                queryWrapper.ne("STATUS", "作废");
            } else {
                queryWrapper.eq("STATUS", "未审核");
            }
            //处理日期
            if(isInputOrCheckDate){
                //根据审核时间查询
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CHECKDATE",
                            LocalDateTime.parse(startDate, formatter),
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            else{
                //根据创建时间查询
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CREATEDATE",
                            LocalDateTime.parse(startDate, formatter),  //前端需传递 ISO 8601 格式时间（如 2023-10-10T23:59:59）。
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            if(StrUtil.isNotBlank(checkId) || StrUtil.isNotBlank(supplierinfo)  || inStoreType){
                queryWrapper.and(wrapper -> {
                    // 模糊查询：SUPPLYCODE LIKE supplierinfo 或 SUPPLYINPUTCODE LIKE supplierinfo
                    if(StrUtil.isNotBlank(supplierinfo)){
                        wrapper.and(q -> q
                                .like("SUPPLYCODE", supplierinfo)
                                .or()
                                .like("SUPPLYINPUTCODE", supplierinfo)
                        );
                    }
                    wrapper.eq(inStoreType,"INSTORETYPEID","7445b79c-67bd-4d2f-a582-e697d4b6b9b0")  //票货不符id为7445b79c-67bd-4d2f-a582-e697d4b6b9b0
                            .eq(StrUtil.isNotBlank(checkId),"CHECKID", checkId);  //审核人id
                });
            }
            queryWrapper.orderByDesc("CREATEDATE"); //按创建时间倒序排
            //查询+构造返回结果
            List<MediInstoreBillMaster> queryInfos = mediInstoreBillMaster.list(queryWrapper);
            JSONObject result = new JSONObject();
            result.put("DefineInStoreBillMasterInfo", queryInfos);
            return Result.success(result);
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, "错误："+ e.getMessage());
        }
    }

    /** 根据主表ID查询明细表数据 */
    @PostMapping("/getInStoreBillDetailListWithDrawal")
    public Result getInStoreBillDetailListWithDrawal(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String inStoreBillId = paramsJson.getString("inStoreBillId");   //药品批号
            if(StrUtil.isNotBlank(inStoreBillId)){
                QueryWrapper<MediInstoreBillDetail> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("INSTOREBILLID", inStoreBillId).eq("ISACTIVE","在用");
                List<MediInstoreBillDetail> infos = mediInstoreBillDetail.list(queryWrapper);
                JSONObject result = new JSONObject();
                result.put("DefineInStoreBillDetailInfoWithDrawal", infos);
                return Result.success(result);
            }else{
                return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 明细表删除数据 */
    @PostMapping("/wasterInStoreBillDetail")
    public Result wasterInStoreBillDetail(@RequestBody JSONObject paramsJson){
         if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
//            String inStoreBillId = paramsJson.getString("inStoreBillId");   //主表ID
            String inStoreBillDetailId = paramsJson.getString("inStoreBillDetailId");   //明细表ID
            UpdateWrapper<MediInstoreBillDetail> updateWrapper  = new UpdateWrapper<>();
            //状态设置为 停用
            updateWrapper.eq("ID", inStoreBillDetailId).set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now());
            boolean result = mediInstoreBillDetail.update(updateWrapper);
            if (result) {
                return Result.success();
            } else {
                return rollbackError("删除失败");
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 作废入库单接口，将“未审核”入库单记录设置为“作废”状态。 和停用状态*/
    @PostMapping("/wasterInStoreBill")
    public Result wasterInStoreBill(@RequestBody JSONObject paramsJson) {
        if (paramsJson.isEmpty()) {
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try {
            String inStoreBillId = paramsJson.getString("inStoreBillId");   //入库主表id
            String operatorId = paramsJson.getString("operatorId");   //入库主表id

            UpdateWrapper<MediInstoreBillMaster> updateWrapper  = new UpdateWrapper<>();
            updateWrapper.eq("ID", inStoreBillId);
            MediInstoreBillMaster info = mediInstoreBillMaster.getOne(updateWrapper);
            //判断是否是 未审核 的入库单
            if(info != null && Objects.equals(info.getStatus(), "未审核")){
                //设置为 停用、作废状态
                updateWrapper.set("STATUS","作废").set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now()).set("OPERATORID",operatorId).set("INPUTDATE",LocalDateTime.now());
                boolean result =  mediInstoreBillMaster.update(updateWrapper);
                if (result) {
                    return Result.success();
                } else {
                    return rollbackError("删除失败");
                }
            }else{
                return rollbackError(CustomizeCode.FAIL, "未查询到该入库单 或 不是未审核的入库单");
            }
        } catch (Exception e) {
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 保存（添加/修改）主表和明细表 */
    @PostMapping("/saveInStoreBillWithDrawal")
    public Result saveInStoreBillWithDrawal(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try {
            MediInstoreBillMaster masterInfo = paramsJson.getObject("mediInStoreBillMaster", MediInstoreBillMaster.class);
            List<MediInstoreBillDetail> detailInsertList = paramsJson.getList("defineInStoreBillDetailInfoWithDrawal", MediInstoreBillDetail.class);
            List<MediStockInfo> stockInsertList = paramsJson.getList("defineInStoreBillDetailInfoWithDrawal", MediStockInfo.class);
            List<MediInstoreBillDetail> detailUpdateList = paramsJson.getList("isdefineInStoreBillDetailInfoWithDrawal", MediInstoreBillDetail.class);

            if (StrUtil.isNotBlank(masterInfo.getId())) {
                MediInstoreBillMaster existing = mediInstoreBillMaster.getOne(new QueryWrapper<MediInstoreBillMaster>().eq("ID", masterInfo.getId()).last("FOR UPDATE"));
                if (existing == null || !"未审核".equals(existing.getStatus())) return rollbackError("仅可编辑未审核单据");
            }
            masterInfo.setStatus("未审核"); masterInfo.setIsActive("在用");
            if(masterInfo.getId().isEmpty()){
                //1.1 新增入库单
                masterInfo.setCreateDate(LocalDateTime.now());   //创建时间
                masterInfo.setBillNo(generateInStoreCode());  //生成入库单号
            }else{
                //1.1 修改数据
                masterInfo.setInputDate(LocalDateTime.now());  //修改时间
            }
            boolean result1 = mediInstoreBillMaster.saveOrUpdate(masterInfo);
            if (!result1) {return rollbackError(CustomizeCode.FAIL, "保存主表失败！");}
            // 获取主表生成的 ID（插入或更新后的 ID）
            String masterId = masterInfo.getId();

            ////2.新增明细表
            if(detailInsertList != null && !detailInsertList.isEmpty()){
                // 2.1 生成批次号（示例：20231023 + 123456）
                String batch = generateRandomBatchNumber();
                // 2.2 为所有明细对象设置相同的batch等字段
                detailInsertList.forEach(detail -> detail.setBatch(batch));
                detailInsertList.forEach(detail -> detail.setInstoreBillId(masterId));
                detailInsertList.forEach(detail -> detail.setCreateDate(LocalDateTime.now()));  //创建时间
                // 2.3 插入明细表
                boolean result2 = mediInstoreBillDetail.saveBatch(detailInsertList);
                if (!result2) {return rollbackError(CustomizeCode.FAIL, "插入明细表失败！");}


                // 2.4 插入stockinfo 表
                // 处理stockInsertList (待插入 stockinfo 表的记录)
                stockInsertList.forEach(info -> {info.setId(""); info.setStatus("在用"); info.setDeptId("101"); info.setDeptCode("101");info.setDeptName("西药库");info.setDeptInputCode("XYK");});   //映射的id 为明细表id,需删除
                for(MediStockInfo stockInfo : stockInsertList){
                    // 每次循环创建新的 Wrapper，避免条件叠加
                    QueryWrapper<MediStockInfo> queryWrapper1 = new QueryWrapper<>();
                    QueryWrapper<MediStockBatchInfo> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper1.eq("DRUGID", stockInfo.getDrugId());
                    queryWrapper2.eq("DRUGID", stockInfo.getDrugId());

                    if(mediStockInfo.exists(queryWrapper1)){
                        //stockInfo表 中若有该药品信息, 将 stockBatchInfo表 所有相关药品的 priceNumber 库存余量相加
                        // 计算批次表中该药品的 priceNumber 总和
                        queryWrapper2.select("SUM(PRICENUMBER) AS total");
                        List<Object> sumResult = mediStockBatchInfo.listObjs(queryWrapper2);  //[{total=150}]

                        // 处理总和（可能为 null 或空）
                        Long totalInventory = sumResult.stream()
                                .filter(Objects::nonNull)
                                .mapToLong(obj -> ((Number) obj).longValue())
                                .sum();
                        // 设置库存总量（若无记录则默认为0）
                        stockInfo.setInventory( totalInventory );
                    }else{
                        //若没有该药品信息,则新增, 先处理数据, 设置分类id等
                        stockInfo.setInventory(0L);
                        stockInfo.setPriceInventory(0L);

                        QueryWrapper<MediDrugInfo> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("ID",stockInfo.getDrugId());
                        List<MediDrugInfo> results = mediDrugInfo.list(queryWrapper3);  //根据drugid 查询 catalogid

                        if(!results.isEmpty()){
                            //查询到 药品分类id
                            stockInfo.setCatalogId(results.get(0).getCatalogID());
                            mediStockInfo.save(stockInfo);   //StockInfo表 新增数据
                        }else{
                            //未查询到 药品分类id
                            return rollbackError(CustomizeCode.FAIL, "该药品不在机构药品信息目录中,无法进行入库!!");
                        }
                    }
                }

            }
            //// 3.修改明细表
            if(detailUpdateList != null && !detailUpdateList.isEmpty()){
                boolean result3 = mediInstoreBillDetail.updateBatchById(detailUpdateList);
                detailUpdateList.forEach(detail -> detail.setInputDate(LocalDateTime.now()));  //修改时间
                if (!result3) {return rollbackError(CustomizeCode.FAIL, "更新明细表失败！");}
            }
            return Result.success();
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 审核订单 ，入库表增加响应库存*/
    @PostMapping("/checkInStoreBill")
    public Result checkInStoreBill(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String inStoreBillId = paramsJson.getString("inStoreBillId");   //主表id
            String checkId = paramsJson.getString("checkId");   //审核人id
            String orgaId = paramsJson.getString("orgaId");
            String deptId = paramsJson.getString("deptId");
            UpdateWrapper<MediInstoreBillMaster> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("ID", inStoreBillId);
            MediInstoreBillMaster lockedMaster = mediInstoreBillMaster.getOne(updateWrapper1.last("FOR UPDATE"));
            updateWrapper1.last("");
            if (lockedMaster != null && !"未审核".equals(lockedMaster.getStatus())) return rollbackError("该单据已审核或已作废");
            if(lockedMaster == null){
                return rollbackError("未查询到该入库单！");
            }else{
                // 1.主表InstoreBillMaster设置为 审核
                updateWrapper1.set("STATUS","已审核").set("CHECKID",checkId).set("INPUTDATE",LocalDateTime.now()).set("CHECKDATE",LocalDateTime.now());
                boolean result1 =  mediInstoreBillMaster.update(updateWrapper1);
                // 2. 明细表InstoreBillDetail 设置为审核
                UpdateWrapper<MediInstoreBillDetail> updateWrapper2 = new UpdateWrapper<>();
                updateWrapper2.eq("INSTOREBILLID", inStoreBillId).set("STATUS", "已审核").set("INPUTDATE",LocalDateTime.now()).eq("ISACTIVE","在用");
                boolean result2 =  mediInstoreBillDetail.update(updateWrapper2);     //设置 明细表 为已审核

                // 3.将审核的药品信息 插入StockBatchInfo 表
                QueryWrapper<MediInstoreBillDetail> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("INSTOREBILLID", inStoreBillId).eq("ISACTIVE","在用");
                List<MediInstoreBillDetail> infos = mediInstoreBillDetail.list(queryWrapper);
            for (MediInstoreBillDetail row : infos) { if (row.getNumber() == null || row.getNumber() <= 0) return rollbackError("数量必须为正数"); }   // 审核后的药品列表
                List<MediStockBatchInfo> insertInfos = new ArrayList<>();  //待插入 药库批次表的数据
                // 采用循环,将infos 相同部分复制到 insertInfos
                for (MediInstoreBillDetail detail : infos) {
                    MediStockBatchInfo batchInfo = getMediStockBatchInfo(detail);
                    insertInfos.add(batchInfo);
                }
                boolean insertResult = mediStockBatchInfo.saveBatch(insertInfos);
                if (!insertResult) {
                    return rollbackError(CustomizeCode.FAIL, "插入药库库存批次表失败！");
                }

                // 4.更新 StockInfo 表的库存信息
                // 4.1取出所有insertInfos中所有不同的 drugid  /drugCode
                Set<String> drugCodeSet = new HashSet<>();
                for (MediStockBatchInfo info : insertInfos) {
                    drugCodeSet.add(info.getDrugCode()); // Set 自动去重
                }

                List<String> distinctDrugCodes = new ArrayList<>(drugCodeSet);
                for( String drugCode : distinctDrugCodes){
                    //4.2 根据不同的drugid 查询 StockBatchInfo 的所有记录,获取总库存量
                    QueryWrapper<MediStockBatchInfo> queryStockBatch = new QueryWrapper<>();
                    queryStockBatch.eq("DRUGCODE", drugCode);
                    queryStockBatch.select("SUM(PRICENUMBER) AS total");
                    List<Object> sumResult = mediStockBatchInfo.listObjs(queryStockBatch);  //[{total=150}]
                    // 处理总和（可能为 null 或空）
                    Long totalInventory = sumResult.stream()
                            .filter(Objects::nonNull)
                            .mapToLong(obj -> ((Number) obj).longValue())
                            .sum();

                    //4.3 更新StockInfo的 Inventory 字段
                    UpdateWrapper<MediStockInfo> updateStockInfo = new UpdateWrapper<>();
                    updateStockInfo.eq("DRUGCODE", drugCode).set("INVENTORY", totalInventory).set("INPUTDATE",LocalDateTime.now());
                    boolean updateResult = mediStockInfo.update(updateStockInfo);
                    // 更新失败说明该 drugCode 在药库库存总表中不存在，需要执行插入操作。
                    if (!updateResult) {
                        MediStockInfo newStock = buildNewMediStockInfo(drugCode, totalInventory,deptId,orgaId,checkId);
                        boolean insertSuccess = mediStockInfo.save(newStock);
                        if (!insertSuccess) {
                            return rollbackError(CustomizeCode.FAIL, "新增药库库存记录失败！");
                        }
                    }
                }
                if(result2 && result1 ){
                    return Result.success();
                }else{
                    return rollbackError("审核失败！");
                }
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 根据 drugCode 查询药品表一条信息 */
    @PostMapping("/getDrugInfoByCode")
    public Result getDrugInfoByCode(@RequestBody JSONObject paramsJson){
        String drugCode = paramsJson.getString("drugCode");   //drugCode
        return Result.success(selectByDrugCode(drugCode));
    }

    private MediDrugInfo selectByDrugCode(String drugCode) {
        QueryWrapper<MediDrugInfo> query = new QueryWrapper<>();
        query.like("DRUGCODE", drugCode).last("LIMIT 1");
        return mediDrugInfo.getOne(query);
    }


    /** 将MediInstoreBillDetail表 相同部分复制到 MediStockBatchInfo*/
    private static @NotNull MediStockBatchInfo getMediStockBatchInfo(MediInstoreBillDetail detail) {
        MediStockBatchInfo batchInfo = new MediStockBatchInfo();

        // 1. 复制相同名称的属性
        batchInfo.setOrgaId(detail.getOrgaId());
        batchInfo.setDrugId(detail.getDrugId());
        batchInfo.setDrugInputCode(detail.getDrugInputCode());
        batchInfo.setDrugInputCode2(detail.getDrugInputCode2());
        batchInfo.setDrugInputCode3(detail.getDrugInputCode3());
        batchInfo.setDrugCode(detail.getDrugCode());
        batchInfo.setDrugName(detail.getDrugName());
        batchInfo.setDrugSpecification(detail.getDrugSpecification());
        batchInfo.setOrigin(detail.getOrigin());
        batchInfo.setSupplierId(detail.getSupplierId());
        batchInfo.setSupplyInputCode(detail.getSupplyInputCode());
        batchInfo.setSupplyCode(detail.getSupplyCode());
        batchInfo.setSupplyName(detail.getSupplyName());
        batchInfo.setUnitId(detail.getUnitId());
        batchInfo.setUnitName(detail.getUnitName());
        batchInfo.setPackagQuantity(detail.getPackagQuantity() != null ? detail.getPackagQuantity().intValue() : 0); // Long → Integer
        batchInfo.setNumber(detail.getNumber() != null ? detail.getNumber().intValue() : 0); // Long → Integer
        batchInfo.setPurchasePrice(detail.getPurchasePrice());
        batchInfo.setRetailPrice(detail.getRetailPrice());
        batchInfo.setPurchaseMoney(detail.getPurchaseMoney());
        batchInfo.setRetailMoney(detail.getRetailMoney());
        batchInfo.setPosition(detail.getPosition());
        batchInfo.setBatchNumber(detail.getBatchNumber());
        batchInfo.setBatch(detail.getBatch());
        batchInfo.setValidityPeriod(detail.getValidityPeriod());
        batchInfo.setOperatorId(detail.getOperatorId());
        batchInfo.setInputDate(detail.getInputDate());
        batchInfo.setCreateUser(detail.getCreateUser());
        batchInfo.setCreateDate(detail.getCreateDate());
        batchInfo.setComment(detail.getComment());
        batchInfo.setProduceDate(detail.getProduceDate());

        // 2. 处理差异属性（设置为空或默认值）
        batchInfo.setDeptId("101"); // MediStockBatchInfo 特有字段
        batchInfo.setDeptInputCode("");
        batchInfo.setDeptCode("");
        batchInfo.setDeptName("");
        batchInfo.setStatus(detail.getIsActive());  // 在用/禁用
        batchInfo.setPriceNumber(batchInfo.getNumber()); // 新增时 库存余量 与 入库量相同

        batchInfo.setCatalogId(detail.getCatalogId());   // 可以不使用

        return batchInfo;
    }

    /**
     * 生成批次号：8位日期 + 6位随机数（同一批次相同）
     */
    private String generateRandomBatchNumber() {
        // 1. 获取当前日期（格式：yyyyMMdd）
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 2. 生成6位随机数（范围：000000 ~ 999999）
        int randomNumber = new Random().nextInt(999999);
        String randomPart = String.format("%06d", randomNumber);   //数字转化为字符串

        // 3. 拼接日期和随机数
        return datePart + randomPart;
    }

    /** 构造 MediStockInfo类 用于新增 MediStockInfo 表 */
    // 构建新库存记录的工具方法
    private MediStockInfo buildNewMediStockInfo(String drugCode, Long totalInventory,String deptId, String orgaId,String checkId) {
        MediStockInfo newStock = new MediStockInfo();

        // 生成UUID主键（达梦数据库CHAR(36)格式要求）
        newStock.setId(UUID.randomUUID().toString().replace("-", ""));

        // 核心字段赋值
        newStock.setDrugCode(drugCode);
        newStock.setInventory(totalInventory);
        newStock.setInputDate(LocalDateTime.now());

        newStock.setOrgaId(orgaId);     // 当前用户所属机构ID
        newStock.setDeptId(deptId);     // 当前用户所属部门ID
        newStock.setStatus("在用");                   // 默认状态

        // 药品基础信息（需从药品主表关联获取）

        MediDrugInfo drug = selectByDrugCode(drugCode);
        if (drug != null) {
            newStock.setDrugId(drug.getId());
            newStock.setDrugName(drug.getDrugName());
            newStock.setDrugSpecification(drug.getDrugSpecification());
            newStock.setUnitId(drug.getPriceUnitID());
            newStock.setUnitName(drug.getPriceUnitName());
        }

        // 系统审计字段
        newStock.setCreateUser(checkId);     // 当前操作人ID
        newStock.setCreateDate(LocalDateTime.now());    // 创建时间

        return newStock;
    }

    private Result rollbackError(String message) { return rollbackError("500", message); }
    private Result rollbackError(String code, String message) {
        org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return Result.error(code, message);
    }
}
