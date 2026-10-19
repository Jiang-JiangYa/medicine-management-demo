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
import org.example.myspringboot.service.impl.MediRefundingBillDetailImpl;
import org.example.myspringboot.service.impl.MediRefundingBillMasterImpl;
import org.example.myspringboot.service.impl.MediStockBatchInfoImpl;
import org.example.myspringboot.service.impl.MediStockInfoImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static org.example.myspringboot.entity.generateBillno.generateInStoreCode;
import static org.example.myspringboot.entity.generateBillno.generateRefundingCode;

/**
 * 功能：药品退货管理模块  -- Controller层
 * 作者：yangjia
 * 日期：2025/4/23 下午8:45
 */
@Data
@RestController

@RequestMapping("/his/medicine")
@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
public class drugReturnManagementController {

    @Resource
    MediRefundingBillMasterImpl mediRefundingBillMaster;

    @Resource
    MediRefundingBillDetailImpl mediRefundingBillDetail;

    @Resource
    MediStockBatchInfoImpl mediStockBatchInfo;   //药库批次表

    @Resource
    MediStockInfoImpl mediStockInfo;    //药库库存表

    /** 查询退库单主表 */
    @PostMapping("/getRefundingBillMasterList")
    public Result getRefundingBillMasterList(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            Boolean isInputOrCheckDate = paramsJson.getBoolean("isInputOrCheckDate");  //false:审核时间  true：录单时间
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            String checkId = paramsJson.getString("checkID");     //审核人ID
            String status = paramsJson.getString("status");     //“未审核” / “”
            String supplierinfo = paramsJson.getString("tbSuppliers").toUpperCase(); //供应商名称

            //1、构造条件条件
            QueryWrapper<MediRefundingBillMaster> queryWrapper = new QueryWrapper<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            queryWrapper.eq("ISACTIVE", "在用");
            if(status.equals("未审核")){
                queryWrapper.eq("STATUS", "未审核");
            }
            // 处理查询日期
            if(isInputOrCheckDate){
                //根据录单时间查询
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CREATEDATE",
                            LocalDateTime.parse(startDate, formatter),  //前端需传递 ISO 8601 格式时间（如 2023-10-10T23:59:59）。
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }else {
                //根据审核日期查询
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    queryWrapper.between("CHECKDATE",
                            LocalDateTime.parse(startDate, formatter),  //前端需传递 ISO 8601 格式时间（如 2023-10-10T23:59:59）。
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            //根据审核人ID全字段匹配，根据供应商信息模糊匹配
            if(StrUtil.isNotBlank(checkId) || StrUtil.isNotBlank(supplierinfo)){
                queryWrapper.and(wrapper -> {
                    // 模糊查询：SUPPLYCODE LIKE supplierinfo 或 SUPPLYINPUTCODE LIKE supplierinfo
                    if(StrUtil.isNotBlank(supplierinfo)){
                        wrapper.and(q -> q
                                .like("SUPPLYCODE", supplierinfo)
                                .or()
                                .like("SUPPLYINPUTCODE", supplierinfo)
                        );
                    }
                    wrapper.eq(StrUtil.isNotBlank(checkId),"CHECKID", checkId);  //审核人id
                });
            }
            queryWrapper.orderByDesc("CREATEDATE"); //按创建时间倒序排
            //2、查询+构造返回结果
            List<MediRefundingBillMaster> queryInfos = mediRefundingBillMaster.list(queryWrapper);
            JSONObject result = new JSONObject();
            result.put("DefineRefundingBillMasterInfo", queryInfos);
            return Result.success(result);

        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, "错误："+ e.getMessage());
        }
    }

    /** 根据主表ID 查询退库单明细表  */
    @PostMapping("/getRefundingBillDetailListWithDrawal")
    public Result getRefundingBillDetailListWithDrawal(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String refundingBillID = paramsJson.getString("refundingBillID");   //退货单主表ID
            if(StrUtil.isNotBlank(refundingBillID)){
                QueryWrapper<MediRefundingBillDetail> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("REFUNDINGBILLID", refundingBillID).eq("ISACTIVE","在用");    //在用
                List<MediRefundingBillDetail> infos = mediRefundingBillDetail.list(queryWrapper);
                JSONObject result = new JSONObject();
                result.put("DefineRefundingBillDetailInfoWithDrawal", infos);
                return Result.success(result);
            }else{
                return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 删除明细表 */
    @PostMapping("/wasterRefundingBillDetail")
    public Result wasterRefundingBillDetail(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
//            String refundingBillID = paramsJson.getString("refundingBillID");   //主表ID
            String RefundingBillDetailID = paramsJson.getString("RefundingBillDetailID");   //明细表ID
            UpdateWrapper<MediRefundingBillDetail> updateWrapper  = new UpdateWrapper<>();
            //状态设置为 停用
            updateWrapper.eq("ID", RefundingBillDetailID).set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now());
            boolean result = mediRefundingBillDetail.update(updateWrapper);
            if (result) {
                return Result.success();
            } else {
                return rollbackError("删除失败");
            }
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /**  作废退货单接口，将“未审核”退货单记录设置为“作废”状态。 和停用状态*/
    @PostMapping("/wasterRefundingBillMaster")
    public Result wasterRefundingBillMaster(@RequestBody JSONObject paramsJson){
        if (paramsJson.isEmpty()) {
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try {
            String refundingBillID = paramsJson.getString("refundingBillID");   //退货主表id
            String operatorID = paramsJson.getString("operatorID");   //操作人

            UpdateWrapper<MediRefundingBillMaster> updateWrapper  = new UpdateWrapper<>();
            updateWrapper.eq("ID", refundingBillID).last("LIMIT 1");
            MediRefundingBillMaster info = mediRefundingBillMaster.getOne(updateWrapper);
            //判断是否是 未审核 的入库单
            if(info != null && Objects.equals(info.getStatus(), "未审核")){
                //设置为 停用、作废状态
                updateWrapper.set("STATUS","作废").set("ISACTIVE","停用").set("STOPDATE", LocalDateTime.now()).set("OPERATORID",operatorID).set("INPUTDATE",LocalDateTime.now());
                boolean result =  mediRefundingBillMaster.update(updateWrapper);
                if (result) {
                    return Result.success();
                } else {
                    return rollbackError("删除失败");
                }
            }else{
                return rollbackError(CustomizeCode.FAIL, "未查询到该退货单 或 不是未审核的退货单");
            }
        } catch (Exception e) {
            return rollbackError(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 审核退货单 */
    @PostMapping("/checkRefundingBill")
    public Result checkRefundingBill(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String refundingBillID = paramsJson.getString("RefundingBillID");  //主表ID
            String checkId = paramsJson.getString("checkID");   //审核人id
            String orgaId = paramsJson.getString("orgaId");
            // 1.查询是否有该退货单
            UpdateWrapper<MediRefundingBillMaster> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("ID", refundingBillID).last("LIMIT 1").eq("ISACTIVE","在用");
            MediRefundingBillMaster lockedMaster = mediRefundingBillMaster.getOne(updateWrapper1.last("FOR UPDATE"));
            updateWrapper1.last("");
            if (lockedMaster != null && !"未审核".equals(lockedMaster.getStatus())) return rollbackError("该单据已审核或已作废");
            if(lockedMaster == null){
                return rollbackError("未查询到该退货单！");
            }else{
               //2. 查询退货明细表
                UpdateWrapper<MediRefundingBillDetail> updateWrapper2 = new UpdateWrapper<>();
                updateWrapper2.eq("REFUNDINGBILLID", refundingBillID).eq("ISACTIVE","在用");
                List<MediRefundingBillDetail> refundingBillDetails = mediRefundingBillDetail.list(updateWrapper2);
            for (MediRefundingBillDetail row : refundingBillDetails) { if (row.getNumber() == null || row.getNumber() <= 0) return rollbackError("数量必须为正数"); }

                for(MediRefundingBillDetail detail : refundingBillDetails){
                    //3. StockBatchInfo 表扣除相关库存
                    UpdateWrapper<MediStockBatchInfo> updateWrapper3 = new UpdateWrapper<>();
                    // 查询drugid 、batch、batchnumber 相同的记录
                    updateWrapper3.eq("DRUGID", detail.getDrugID()).eq("BATCH",detail.getBatch()).eq("BATCHNUMBER",detail.getBatchNumber()).last("LIMIT 1");
                    MediStockBatchInfo batchInfo = mediStockBatchInfo.getOne(updateWrapper3.last("FOR UPDATE"));
                    updateWrapper3.last("");
                    if(batchInfo == null){
                        return rollbackError("药库中未查询到该批次的药品记录");
                    }
                    if(batchInfo.getPriceNumber() < detail.getNumber() ){
                        return rollbackError("该批次药品的库存余量小于退货数量");
                    }
                    // 减去 退货的库存量
                    updateWrapper3.set("PRICENUMBER", batchInfo.getPriceNumber()-detail.getNumber()).set("INPUTDATE",LocalDateTime.now());
                    boolean result3 =  mediStockBatchInfo.update(updateWrapper3);
                    if (!result3) { return rollbackError("更新药库库存批次表失败！");}

                    //4. StockInfo 表扣除相关库存
                    UpdateWrapper<MediStockInfo> updateWrapper4 = new UpdateWrapper<>();
                    // 根据StockBatchInfo 表记录batchInfo的部门ID、药品ID 查询StockInfo表
                    updateWrapper4.eq("DRUGID",detail.getDrugID())
                            .setSql("INVENTORY = INVENTORY - " + detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",checkId);
                    boolean result4 = mediStockInfo.update(updateWrapper4);
                    if (!result4) { return rollbackError("更新药库库存总表失败！"); }
                }

                // 5.主表mediRefundingBillMasterr设置为 审核
                updateWrapper1.set("STATUS","已审核").set("CHECKID",checkId).set("INPUTDATE",LocalDateTime.now()).set("CHECKDATE",LocalDateTime.now());
                boolean result1 =  mediRefundingBillMaster.update(updateWrapper1);

                // 6. 明细表mediRefundingBillDetail 设置为 审核
                updateWrapper2.set("STATUS", "已审核").set("INPUTDATE",LocalDateTime.now());
                boolean result2 =  mediRefundingBillDetail.update(updateWrapper2);     //设置 明细表 为已审核

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

    /** 冲销退货单。主表设置为“作废""停用"。执行审核反向操作，记录 操作人ID 。明细表设置为”被冲销“”停用“*/
    @PostMapping("/newReverseRefundingBill")
    public Result newReverseRefundingBill(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String refundingBillID = paramsJson.getString("refundingMasterID");  //主表ID
            String userID = paramsJson.getString("userID");   //操作人id
            // 1.查询是否有该退货单
            UpdateWrapper<MediRefundingBillMaster> updateWrapper1 = new UpdateWrapper<>();
            updateWrapper1.eq("ID", refundingBillID).last("LIMIT 1").eq("ISACTIVE","在用");
            if(mediRefundingBillMaster.getOne(updateWrapper1) == null){
                return rollbackError("未查询到该退货单！");
            }
            if(mediRefundingBillMaster.getOne(updateWrapper1).getStatus() == "未审核"){
                return rollbackError("该退货单未审核，请直接删除，无需冲销操作！");
            }
            //2. 查询退货明细表
            UpdateWrapper<MediRefundingBillDetail> updateWrapper2 = new UpdateWrapper<>();
            updateWrapper2.eq("REFUNDINGBILLID", refundingBillID).eq("ISACTIVE","在用");
            List<MediRefundingBillDetail> refundingBillDetails = mediRefundingBillDetail.list(updateWrapper2);

            for(MediRefundingBillDetail detail : refundingBillDetails){
                //3. StockBatchInfo 表添加相关库存
                UpdateWrapper<MediStockBatchInfo> updateWrapper3 = new UpdateWrapper<>();
                // 查询drugid 、batch、batchnumber 相同的记录
                updateWrapper3.eq("DRUGID", detail.getDrugID()).eq("BATCH",detail.getBatch()).eq("BATCHNUMBER",detail.getBatchNumber()).last("LIMIT 1");
                MediStockBatchInfo batchInfo = mediStockBatchInfo.getOne(updateWrapper3.last("FOR UPDATE"));
                    updateWrapper3.last("");
                if(batchInfo == null){
                    return rollbackError("药库中未查询到该批次的药品记录");
                }
                // 添加 退货的库存量
                updateWrapper3.set("PRICENUMBER", batchInfo.getPriceNumber()+detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",userID);
                boolean result3 =  mediStockBatchInfo.update(updateWrapper3);
                if (!result3) { return rollbackError("更新药库库存批次表失败！");}

                //4. StockInfo 表添加相关库存
                UpdateWrapper<MediStockInfo> updateWrapper4 = new UpdateWrapper<>();
                // 根据StockBatchInfo 表记录batchInfo的部门ID、药品ID 查询StockInfo表
                updateWrapper4.eq("DRUGID",batchInfo.getDrugId())
                        .setSql("INVENTORY = INVENTORY + " + detail.getNumber()).set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",userID);
                boolean result4 = mediStockInfo.update(updateWrapper4);
                if (!result4) { return rollbackError("更新药库库存总表失败！"); }
            }

            // 5.主表mediRefundingBillMasterr设置为 “作废""停用"
            updateWrapper1.set("STATUS","作废").set("ISACTIVE","停用").set("INPUTDATE",LocalDateTime.now()).set("STOPDATE",LocalDateTime.now()).set("OPERATORID",userID);
            boolean result1 =  mediRefundingBillMaster.update(updateWrapper1);

            // 6. 明细表mediRefundingBillDetail 设置为 ”被冲销“”停用“
            updateWrapper2.set("STATUS", "被冲销").set("ISACTIVE","停用").set("INPUTDATE",LocalDateTime.now()).set("OPERATORID",userID).set("STOPDATE",LocalDateTime.now());
            boolean result2 =  mediRefundingBillDetail.update(updateWrapper2);

            if(result2 && result1 ){
                return Result.success();
            }else{
                return rollbackError("冲销失败！");
            }

        }catch(Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }

    /** 保存退货单主表/明细表  --Add/Update */
    @PostMapping("/saveRefundingBillMasterNew")
    public Result saveRefundingBillMasterNew(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return rollbackError(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try {
            MediRefundingBillMaster masterInfo = paramsJson.getObject("defineRefundingBillMasterInfo", MediRefundingBillMaster.class);
            List<MediRefundingBillDetail> detailList = paramsJson.getList("defineRefundingBillDetailInfoWithDrawal", MediRefundingBillDetail.class);
            if (StrUtil.isNotBlank(masterInfo.getId())) {
                MediRefundingBillMaster existing = mediRefundingBillMaster.getOne(new QueryWrapper<MediRefundingBillMaster>().eq("ID", masterInfo.getId()).last("FOR UPDATE"));
                if (existing == null || !"未审核".equals(existing.getStatus())) return rollbackError("仅可编辑未审核单据");
            }
            masterInfo.setStatus("未审核"); masterInfo.setIsActive("在用");
            String addeditMode = paramsJson.getString("addeditMode");   //  Add / Update
            //新增操作  退货单主表 / 明细表
            if(addeditMode.trim().equals("Add")){
                if(masterInfo.getId().isEmpty()){
                    //1. 新增退货单
                    masterInfo.setCreateDate(LocalDateTime.now());   //创建时间
                    masterInfo.setBillNo(generateRefundingCode());  //生成退货单号
                    masterInfo.setCreateUser("yang");
                    masterInfo.setIsActive("在用");
                    masterInfo.setStatus("未审核");
                    boolean result1 = mediRefundingBillMaster.save(masterInfo);
                    if (!result1) {return rollbackError(CustomizeCode.FAIL, "保存主表失败！");}
                    // 获取主表生成的 ID（插入时自动生成的 ID）
                    String masterId = masterInfo.getId();

                    //2. 新增明细表
                    if(detailList != null && !detailList.isEmpty()){
                        // 2.1 为所有明细对象设置相同的字段
                        detailList.forEach(detail -> detail.setRefundingBillID(masterId));
                        detailList.forEach(detail -> detail.setBillNo(masterInfo.getBillNo()));   //退库单号
                        detailList.forEach(detail -> detail.setCreateDate(LocalDateTime.now()));  //创建时间
                        detailList.forEach(detail -> detail.setStatus("未审核"));
                        detailList.forEach(detail -> detail.setIsActive("在用"));
                        // 2.2 插入明细表
                        boolean result2 = mediRefundingBillDetail.saveBatch(detailList);
                        if (!result2) {return rollbackError(CustomizeCode.FAIL, "插入明细表失败！");}
                    }
                }
            }
            //修改操作
            if(addeditMode.trim().equals("Update")){
                if(!masterInfo.getId().isEmpty()){   //有ID，修改主表,可以添加/修改明细表
                    // 1、更新主表
                    masterInfo.setInputDate(LocalDateTime.now());
                    boolean result3 = mediRefundingBillMaster.updateById(masterInfo);
                    if (!result3) {return rollbackError(CustomizeCode.FAIL, "更新主表失败！");}

                    // 2、更新/添加  明细表
                    detailList.forEach(detail -> detail.setInputDate(LocalDateTime.now()));  //修改时间
                    detailList.forEach(detail -> detail.setRefundingBillID(masterInfo.getId()));
                    detailList.forEach(detail -> detail.setBillNo(masterInfo.getBillNo()));   //退库单号
                    detailList.forEach(detail -> detail.setOperatorID("yang"));
                    detailList.forEach(detail -> detail.setStatus("未审核"));
                    boolean result4 = mediRefundingBillDetail.saveOrUpdateBatch(detailList);
                    if (!result4) {return rollbackError(CustomizeCode.FAIL, "更新明细表失败！");}

                }
            }
            return Result.success();
        }catch (Exception e){
            return rollbackError(CustomizeCode.FAIL, e.getClass() + e.getMessage());
        }
    }


    private Result rollbackError(String message) { return rollbackError("500", message); }
    private Result rollbackError(String code, String message) {
        org.springframework.transaction.interceptor.TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return Result.error(code, message);
    }
}
