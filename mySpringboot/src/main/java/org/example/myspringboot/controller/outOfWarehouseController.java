package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.*;
import org.example.myspringboot.service.impl.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static org.example.myspringboot.entity.generateBillno.generateOutStoreCode;

/**
 * 功能：药品出库管理 Controller
 * 作者：yangjia
 * 日期：2025/4/25 上午12:29
 */
@Data
@Slf4j  // 自动生成 log 对象
@RestController

@RequestMapping("/his/medicine")
@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
public class outOfWarehouseController {
    @Resource
    MediOutStoreBillMasterImpl mediOutStoreBillMaster;

    @Resource
    MediOutstoreBillDetailImpl mediOutstoreBillDetail;

    @Resource
    MediStockBatchInfoImpl mediStockBatchInfo;   //药库批次表

    @Resource
    MediStockInfoImpl mediStockInfo;    //药库库存表

    @Resource
    MediDrugStoreInfoImpl mediDrugStoreInfo;   //药房库存表

    @Resource
    MediDrugStoreBatchInfoImpl mediDrugStoreBatchInfo;   //药房批次库存表

    /** 查询主表 */
    @PostMapping("/getOutStoreBillMasterList")
    public Result getOutStoreBillMasterList(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            String checkId = paramsJson.getString("executor");     //审核人ID
            String dateType = paramsJson.getString("dateType");     //  1（按录单时间） 2（按审核时间）
            String status = paramsJson.getString("status");     //  是/否 （未审核的按钮 是否勾选）

            QueryWrapper<MediOutStoreBillMaster> queryWrapper = new QueryWrapper<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //1、审核状态
            queryWrapper.eq("ISACTIVE", "在用");
            if( status.equals("是")){
                queryWrapper.eq("STATUS", "未审核");
            }
            //2、根据录单时间查
            if(dateType.equals("1")){
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CREATEDATE",
                            LocalDateTime.parse(startDate, formatter),
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            //2、根据审核时间查询
            if(dateType.equals("2")){
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CHECKDATE",
                            LocalDateTime.parse(startDate, formatter),
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            //3、审核人ID全字段匹配
            queryWrapper.eq(StrUtil.isNotBlank(checkId), "CHECKID",checkId);
            queryWrapper.orderByDesc("CREATEDATE"); //按创建时间倒序排
            //4、查询+构造返回结果
            List<MediOutStoreBillMaster> list = mediOutStoreBillMaster.list(queryWrapper);
            JSONObject result = new JSONObject();
            result.put("DefineOutStoreBillMasterInfo", list);
            return Result.success(result);

        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 查询明细表 */
    @PostMapping("/getOutStoreBillDetailListWithDrawal")
    public Result getOutStoreBillDetailListWithDrawal(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String outStoreBillId = paramsJson.getString("outStoreBillId");   //药品批号
            if(outStoreBillId.isEmpty()){
                return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
            }
            //不为空时：
            QueryWrapper<MediOutstoreBillDetail> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("OUTSTOREBILLID", outStoreBillId).eq("ISACTIVE","在用");
            List<MediOutstoreBillDetail> infos = mediOutstoreBillDetail.list(queryWrapper);
            JSONObject result = new JSONObject();
            result.put("DefineOutStoreBillDetailInfoWithDrawal", infos);
            return Result.success(result);

        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 删除明细表  */
    @PostMapping("/deleteOutStoreBillDetail")
    public Result deleteOutStoreBillDetail(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String detailId = paramsJson.getString("detailId");   //明细表ID
            String operatorId = paramsJson.getString("operatorId");   //操作人ID
            UpdateWrapper<MediOutstoreBillDetail> updateWrapper  = new UpdateWrapper<>();
            updateWrapper.eq("ID", detailId).eq("ISACTIVE","在用").set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now()).set("OPERATORID", operatorId).set("INPUTDATE",LocalDateTime.now());
            if(mediOutstoreBillDetail.update(updateWrapper)){
                return Result.success();
            }else {
                return rollbackError("删除失败");
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 作废入库单，将“未审核”入库单记录设置为“作废”状态。 和停用状态 */
    @PostMapping("/wasterOutStoreBill")
    public Result wasterOutStoreBill(@RequestBody JSONObject paramsJson){
        if (paramsJson.isEmpty()) {
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String outStoreBillId = paramsJson.getString("outStoreBillId");   //出库主表id
            String operatorId = paramsJson.getString("operatorId");   //出库主表id

            UpdateWrapper<MediOutStoreBillMaster> updateWrapper  = new UpdateWrapper<>();
            updateWrapper.eq("ID", outStoreBillId).eq("ISACTIVE","在用");
            MediOutStoreBillMaster info = mediOutStoreBillMaster.getOne(updateWrapper);
            //判断是否是 未审核 的入库单
            if(info != null && Objects.equals(info.getStatus(), "未审核")){
                //设置为 停用、作废状态
                updateWrapper.set("STATUS","作废").set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now()).set("OPERATORID",operatorId).set("INPUTDATE",LocalDateTime.now());
                boolean result =  mediOutStoreBillMaster.update(updateWrapper);
                if (result) {
                    return Result.success();
                } else {
                    return rollbackError("作废出库单失败！");
                }
            }else{
                return rollbackError(CustomizeCode.FAIL, "未查询到该入库单 或 不是未审核的入库单");
            }
        }catch (Exception e) {
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 审核出库单， 药库两表减少，药房两表增加 */
    @PostMapping("/checkOutStoreBill")
    public Result checkOutStoreBill(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String outStoreBillId = paramsJson.getString("outStoreBillId");  //主表ID
            String checkId = paramsJson.getString("userId");   //审核人id
            String orgaId = paramsJson.getString("orgaid");   //机构ID
            // 1.查询是否有该出库单
            UpdateWrapper<MediOutStoreBillMaster> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("ID", outStoreBillId).eq("ISACTIVE","在用");
            MediOutStoreBillMaster masterInfo = mediOutStoreBillMaster.getOne(updateWrapper1.last("FOR UPDATE"));
            updateWrapper1.last("");
            if (masterInfo != null && !"未审核".equals(masterInfo.getStatus())) return rollbackError("该单据已审核或已作废");
            if(masterInfo == null){
                return rollbackError("未查询到该出库单！");
            }else{
                //2.查询出库明细表
                UpdateWrapper<MediOutstoreBillDetail> updateWrapper2 = new UpdateWrapper<>();
                updateWrapper2.eq("OUTSTOREBILLID", outStoreBillId).eq("ISACTIVE","在用");
                List<MediOutstoreBillDetail> outstoreBillDetails = mediOutstoreBillDetail.list(updateWrapper2);
            for (MediOutstoreBillDetail row : outstoreBillDetails) { if (row.getNumber() == null || row.getNumber() <= 0) return rollbackError("数量必须为正数"); }

                for(MediOutstoreBillDetail detail : outstoreBillDetails){
                    //3. StockBatchInfo 表扣除相关库存(药库)
                    UpdateWrapper<MediStockBatchInfo> updateWrapper3 = new UpdateWrapper<>();
                    // 查询drugid 、batch、batchnumber 相同的记录
                    updateWrapper3.eq("DRUGID", detail.getDrugId()).eq("BATCH",detail.getBatch()).eq("BATCHNUMBER",detail.getBatchNumber()).eq("STATUS","在用").last("LIMIT 1");
                    MediStockBatchInfo batchInfo = mediStockBatchInfo.getOne(updateWrapper3.last("FOR UPDATE"));
                    updateWrapper3.last("");
                    if(batchInfo == null){
                        return rollbackError("药库中未查询到该批次的药品记录");
                    }
                    if(batchInfo.getPriceNumber() < detail.getNumber() ){
                        return rollbackError("该批次药品的库存余量小于出库数量");
                    }
                    // 减去 出库的库存量
                    updateWrapper3.set("PRICENUMBER", batchInfo.getPriceNumber()-detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);
                    boolean result3 =  mediStockBatchInfo.update(updateWrapper3);
                    if (!result3) { return rollbackError("更新药库库存批次表失败！");}

                    //4. StockInfo 表扣除相关库存(药库)   //没有的记录没有设置增加功能
                    UpdateWrapper<MediStockInfo> updateWrapper4 = new UpdateWrapper<>();
                    // 根据StockBatchInfo 表记录“batchInfo”药品ID 查询StockInfo表
                    updateWrapper4.eq("DRUGID",batchInfo.getDrugId()).eq("STATUS","在用")
                            .setSql("INVENTORY = INVENTORY - " + detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);

                    log.info("更新条件: DRUGID={}, STATUS=在用", batchInfo.getDrugId());
                    log.info("减少库存数量: {}", detail.getNumber());
                    log.info("操作员ID: {}", checkId);

                    boolean result4 = mediStockInfo.update(updateWrapper4);
                    if (!result4) { return rollbackError("更新药库库存总表失败！"); }

                    //5. 药房库存批次表相同记录库存增加，   没有相同记录则新增记录
                    UpdateWrapper<MediDrugStoreBatchInfo> updateWrapper5 = new UpdateWrapper<>();
                    updateWrapper5.eq("DRUGID",detail.getDrugId()).eq("DEPTID",masterInfo.getRelativeDeptId())
                            .eq("BATCH",detail.getBatch()).eq("BATCHNUMBER",detail.getBatchNumber())
                            .eq("STATUS","在用");
                    if(mediDrugStoreBatchInfo.list(updateWrapper5).toArray().length != 0){
                        //有该记录，增加库存
                        updateWrapper5.setSql("NUMBER_ = NUMBER_ + " + detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);
                        if (!mediDrugStoreBatchInfo.update(updateWrapper5)) { return rollbackError("更新药房库存批次表失败！"); }
                    }else{
                        // 没有记录，新增记录
                        MediDrugStoreBatchInfo newInfo = convertToBatchInfo(detail, masterInfo, checkId);
                        if(!mediDrugStoreBatchInfo.save(newInfo)) return rollbackError(CustomizeCode.FAIL, "新增药房库存批次表记录失败！");
                    }
                   //6. 药房库存总表库存增加（没有该药品记录需增加）
                    UpdateWrapper<MediDrugStoreInfo> updateWrapper6 = new UpdateWrapper<>();
                    updateWrapper6.eq("DRUGID",detail.getDrugId()).eq("DEPTID",masterInfo.getRelativeDeptId())
                            .eq("STATUS","在用");
                    if(mediDrugStoreInfo.list(updateWrapper6).toArray().length != 0){
                        // 有该药房的该药品记录，库存增加
                        updateWrapper6.setSql("INVENTORY = INVENTORY + " + detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);
                        if (!mediDrugStoreInfo.update(updateWrapper6)) { return rollbackError("更新药房库存总表失败！"); }
                    }else {
                        // 没有该药品记录，新增记录
                        MediDrugStoreInfo newInfo = convertToStoreInfo(detail, masterInfo, checkId);
                        if(!mediDrugStoreInfo.save(newInfo)) return rollbackError(CustomizeCode.FAIL, "新增药房库存总表记录失败！");
                    }
                }

                //7. 更新出库主表mediOutStoreBillMaster字段
                updateWrapper1.set("STATUS","已审核").set("CHECKID",checkId).set("INPUTDATE",LocalDateTime.now()).set("CHECKDATE",LocalDateTime.now());
                boolean result1 =  mediOutStoreBillMaster.update(updateWrapper1);
                //8. 更新出库明细表mediOutStoreBillDetail字段
                updateWrapper2.set("STATUS", "已审核").set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);
                boolean result2 =  mediOutstoreBillDetail.update(updateWrapper2);     //设置 明细表 为已审核

                if(result2 && result1 ){
                    return Result.success();
                }else{
                    return rollbackError("审核失败！");
                }
            }
        }catch(Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 保存主表和明细表 */
    @PostMapping("/saveOutStoreBill")
    public Result saveOutStoreBill(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try {
            MediOutStoreBillMaster masterInfo = paramsJson.getObject("MediOutStoreBillMaster", MediOutStoreBillMaster.class);
            List<MediOutstoreBillDetail> detailList = paramsJson.getList("DefineOutStoreBillDetailInfoWithDrawal", MediOutstoreBillDetail.class);
            if (StrUtil.isNotBlank(masterInfo.getId())) {
                MediOutStoreBillMaster existing = mediOutStoreBillMaster.getOne(new QueryWrapper<MediOutStoreBillMaster>().eq("ID", masterInfo.getId()).last("FOR UPDATE"));
                if (existing == null || !"未审核".equals(existing.getStatus())) return rollbackError("仅可编辑未审核单据");
            }
            masterInfo.setStatus("未审核"); masterInfo.setIsActive("在用");
            // 主表、明细表都为新增
            if(masterInfo.getId().isEmpty()){
                // 1、新增出库单
                masterInfo.setCreateDate(LocalDateTime.now());   //创建时间
                masterInfo.setBillNo(generateOutStoreCode());  //生成出库单号
                masterInfo.setIsActive("在用");
                masterInfo.setStatus("未审核");
                if (!mediOutStoreBillMaster.save(masterInfo)) {return rollbackError(CustomizeCode.FAIL, "新增主表失败！");}
                // 2、新增明细表
                if(detailList != null && !detailList.isEmpty()){
                    // 2.1 为所有明细对象设置相同的字段
                    detailList.forEach(detail -> detail.setId(""));    //防止id误传值
                    detailList.forEach(detail -> detail.setOutStoreBillId(masterInfo.getId()));
                    detailList.forEach(detail -> detail.setBillNo(masterInfo.getBillNo()));   //出库单号
                    detailList.forEach(detail -> detail.setCreateDate(LocalDateTime.now()));  //创建时间
                    detailList.forEach(detail -> detail.setStatus("未审核"));
                    detailList.forEach(detail -> detail.setIsActive("在用"));
                    // 2.2 插入明细表
                    boolean result2 = mediOutstoreBillDetail.saveBatch(detailList);
                    if (!result2) {return rollbackError(CustomizeCode.FAIL, "新增明细表失败！");}
                }
            }else{
                //主表为修改，明细表为增加/修改
                // 1、修改主表
                masterInfo.setInputDate(LocalDateTime.now());
                if (!mediOutStoreBillMaster.updateById(masterInfo)) {return rollbackError(CustomizeCode.FAIL, "修改主表失败！");}
                // 2、修改 / 添加 明细表
                if(detailList != null && !detailList.isEmpty()){
                    detailList.forEach(detail -> detail.setInputDate(LocalDateTime.now()));  //修改时间
                    detailList.forEach(detail -> detail.setOutStoreBillId(masterInfo.getId()));
                    detailList.forEach(detail -> detail.setBillNo(masterInfo.getBillNo()));   //出库单号
                    detailList.forEach(detail -> detail.setStatus("未审核"));
                    detailList.forEach(detail -> detail.setIsActive("在用"));
                    if(!mediOutstoreBillDetail.saveOrUpdateBatch(detailList)){
                        return rollbackError(CustomizeCode.FAIL, "修改明细表失败！");
                    }
                }
            }
            return Result.success();
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 根据 drugCode 查询药品表一条信息 */
    @PostMapping("/getDrugInfoInStoreBatchByCode")
    public Result getDrugInfoInStoreBatchByCode(@RequestBody JSONObject paramsJson){
        String drugCode = paramsJson.getString("drugCode");
        QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("DRUGCODE", drugCode).last("LIMIT 1");
        return Result.success(mediStockBatchInfo.getOne(queryWrapper));
    }


    /** 根据 药品出库明细表数据 生成 对应的药房库存明细表数据 */
    public  MediDrugStoreBatchInfo convertToBatchInfo(MediOutstoreBillDetail detail, MediOutStoreBillMaster masterInfo , String checkId){
        MediDrugStoreBatchInfo batchInfo = new MediDrugStoreBatchInfo();
        // --------------------- 直接映射字段 ---------------------
        // 机构、药品信息
        batchInfo.setOrgaID(detail.getOrgaId());
        batchInfo.setDrugID(detail.getDrugId());
        batchInfo.setOrgaDrugID(detail.getOrgaDrugId());
        batchInfo.setDrugCode(detail.getDrugCode());
        batchInfo.setDrugName(detail.getDrugName());
        batchInfo.setDrugSpecification(detail.getDrugSpecification());
        batchInfo.setOrigin(detail.getOrigin());

        // 供应商信息
        batchInfo.setSupplierID(detail.getSupplierId());
        batchInfo.setSupplyCode(detail.getSupplyCode());
        batchInfo.setSupplyName(detail.getSupplyName());
        batchInfo.setSupplyInputCode(detail.getSupplyInputCode());

        // 批次信息
        batchInfo.setBatch(detail.getBatch());
        batchInfo.setBatchNumber(detail.getBatchNumber());
        batchInfo.setValidityPeriod(detail.getValidityPeriod());

        // 价格 \ 数量 \ 单位 \ 药品输入码 \ 批发价 \ 零售价
        batchInfo.setPurchasePrice(detail.getPurchasePrice());
        batchInfo.setRetailPrice(detail.getRetailPrice());
        batchInfo.setNumber_((int) (detail.getNumber() != null ? detail.getNumber() : 0L)); // 空值默认0
        batchInfo.setPackagQuantity(detail.getPackagQuantity());
        batchInfo.setUnitID(detail.getUnitId());
        batchInfo.setUnitName(detail.getUnitName());
        batchInfo.setDrugInputCode(detail.getDrugInputCode());
        batchInfo.setDrugInputCode2(detail.getDrugInputCode2());
        batchInfo.setDrugInputCode3(detail.getDrugInputCode3());
        batchInfo.setPurchaseMoney(detail.getPurchaseMoney());   //批发价，
        batchInfo.setRetailMoney(detail.getRetailMoney());          //零售价

        // --------------------- 需要特殊处理的字段 ---------------------
        // 部门信息 即为  出库表 的相对部门信息
        batchInfo.setDeptID(masterInfo.getRelativeDeptId()); // 部门ID 即为 出库表 的相对部门ID
        batchInfo.setDeptInputCode(masterInfo.getReceivingDeptInputCode());
        batchInfo.setDeptCode(masterInfo.getReceivingDeptCode());
        batchInfo.setDeptName(masterInfo.getReceivingDeptName());

        // 代发药数量（无来源，初始化默认值）
        batchInfo.setVirtualInventory(0);

        // 位置与操作信息（无来源，设置空值）
        batchInfo.setPosition("");
        batchInfo.setOperation("药库出库");

        // --------------------- 审计字段处理 ---------------------
        batchInfo.setCreateUser(checkId);   //审核人即为创建人
        batchInfo.setStatus("在用");
        batchInfo.setCreateDate(LocalDateTime.now());

        return batchInfo;
    }

    /** 根据 药品出库明细表数据  生成 药房库存总表记录 */
    public  MediDrugStoreInfo convertToStoreInfo(MediOutstoreBillDetail detail, MediOutStoreBillMaster masterInfo , String checkId){
        MediDrugStoreInfo storeInfo = new MediDrugStoreInfo();
        // --------------------- 直接映射字段 ---------------------
        // 机构、药品信息
        storeInfo.setOrgaID(detail.getOrgaId());
        storeInfo.setDrugID(detail.getDrugId());
        storeInfo.setOrgaDrugID(detail.getOrgaDrugId());
        storeInfo.setDrugCode(detail.getDrugCode());
        storeInfo.setDrugName(detail.getDrugName());
        storeInfo.setDrugSpecification(detail.getDrugSpecification());
        storeInfo.setOrigin(detail.getOrigin());

        // 价格 \ 数量 \ 单位 \ 药品输入码 \ 批发价 \ 零售价
        storeInfo.setPurchasePrice(detail.getPurchasePrice());
        storeInfo.setRetailPrice(detail.getRetailPrice());
        storeInfo.setInventory((detail.getNumber() != null ? detail.getNumber() : 0L)); // 空值默认0
        storeInfo.setPackagQuantity((detail.getPackagQuantity()));
        storeInfo.setUnitID(detail.getUnitId());
        storeInfo.setUnitName(detail.getUnitName());
        storeInfo.setDrugInputCode(detail.getDrugInputCode());
        storeInfo.setDrugInputCode2(detail.getDrugInputCode2());
        storeInfo.setDrugInputCode3(detail.getDrugInputCode3());
        storeInfo.setPurchaseMoney(detail.getPurchaseMoney());   //批发价，
        storeInfo.setRetailMoney(detail.getRetailMoney());          //零售价

        // --------------------- 需要特殊处理的字段 ---------------------
        // 部门信息 即为  出库表 的相对部门信息
        storeInfo.setDeptID(masterInfo.getRelativeDeptId()); // 部门ID 即为 出库表 的相对部门ID
        storeInfo.setDeptInputCode(masterInfo.getReceivingDeptInputCode());
        storeInfo.setDeptCode(masterInfo.getReceivingDeptCode());
        storeInfo.setDeptName(masterInfo.getReceivingDeptName());

        // 代发药数量（无来源，初始化默认值）
        storeInfo.setVirtualInventory(0L);
        // 操作信息
        storeInfo.setOperation("药库出库");

        // --------------------- 审计字段处理 ---------------------
        storeInfo.setCreateUser(checkId);   //审核人即为创建人
        storeInfo.setStatus("在用");
        storeInfo.setCreateDate(LocalDateTime.now());

        return storeInfo;
    }
    private Result rollbackError(String message) { return rollbackError("500", message); }
    private Result rollbackError(String code, String message) {
        org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return Result.error(code, message);
    }
}


