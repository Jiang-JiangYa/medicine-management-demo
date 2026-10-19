package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.MediStockBatchInfo;
import org.example.myspringboot.service.impl.MediStockBatchInfoImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * 功能：药品效期管理页面 - Controller
 * 作者：yangjia
 * 日期：2025/3/18 下午8:31
 */
@Data
@RestController

@RequestMapping("/his/medicine")
public class drugExpirationDateController {
    @Resource
    private MediStockBatchInfoImpl mediStockBatchInfo;   //药库分批库存表

    /** 药品有效期数据查询分页 */
    @PostMapping("/getDrugAndStoreBatchInfoListWithPageFilter")
    public Result getDrugAndStoreBatchInfoListWithPageFilter(@RequestBody JSONObject paramsJson) {
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            // 1. 提取参数
            //String orgaId = paramsJson.getString("orgaId");
            Integer pageSize = paramsJson.getInteger("pageSize");
            Integer pageIndex = paramsJson.getInteger("pageIndex");
            String batchNumber = paramsJson.getString("batchNumber");   //药品批号
            String drugNameOrCode = paramsJson.getString("drugNameOrCode").toUpperCase();  //药品输入码等搜索条件，转化为大写形式
            String isStock = paramsJson.getString("isStock");     //是否是药库
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            // 2. 构建查询条件
            QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StrUtil.isNotBlank(batchNumber),"BATCHNUMBER", batchNumber);    // 等于CATALOGID， like DRUGINPUTCODE 或 drugNameOrCode
            // 动态条件组合（药品输入码/药品代码模糊查询 + 时间范围查询）
            if(StrUtil.isNotBlank(drugNameOrCode)){
                queryWrapper.and(wrapper -> {
                    // 模糊查询：DRUGINPUTCODE LIKE drugNameOrCode 或 DRUGCODE LIKE drugNameOrCode
                    if(StrUtil.isNotBlank(drugNameOrCode)){
                        wrapper.and(q -> q
                                .like("DRUGINPUTCODE", drugNameOrCode)
                                .or()
                                .like("DRUGCODE", drugNameOrCode)
                        );
                    }
                });
            }
            // 添加 ValidityPeriod 时间范围查询
            if (StrUtil.isAllNotBlank(startDate, endDate)) {
                queryWrapper.between("VALIDITYPERIOD",
                        LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME),  //前端需传递 ISO 8601 格式时间（如 2023-10-10T23:59:59）。
                        LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME)
                );
            }
            // 3. 分页查询
            IPage<MediStockBatchInfo> queryInfos = mediStockBatchInfo.page(new Page<>(pageIndex, pageSize), queryWrapper);
            // 4. 构造返回结果
            JSONObject result = new JSONObject();
            result.put("listCount", queryInfos.getTotal());   //总条数
            result.put("MediStockBatchInfo", queryInfos.getRecords());
            return Result.success(result);

        }catch (Exception e){
            return Result.error(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /**  修改药品效期、生产日期 及批号， */
    @PostMapping("/updateDrugAndStoreBatchInfo")
    public Result updateDrugAndStoreBatchInfo(@RequestBody JSONObject paramsJson) {
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            // 1. 提取参数
            String isStock = paramsJson.getString("isStock");     //是否是药库
            JSONObject MediStockBatchInfo = paramsJson.getJSONObject("MediStockBatchInfo");
            String id = MediStockBatchInfo.getString("id");
            if (StrUtil.isBlank(id)) return Result.error("400", "必须指定批次ID");
            String batchNumber = MediStockBatchInfo.getString("batchNumber");   //药品批号
            String validityPeriod = MediStockBatchInfo.getString("validityPeriod"); // 药品效期
            String produceDate = MediStockBatchInfo.getString("produceDate"); // 结束时间
            // 2. 操作数据库赋值
            // 使用 UpdateWrapper 构建更新条件
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            UpdateWrapper<MediStockBatchInfo> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                    .eq(StrUtil.isNotBlank(id),"ID", id) // WHERE id = #{id}
                    .set(StrUtil.isNotBlank(batchNumber),"BATCHNUMBER", batchNumber)
                    .set(StrUtil.isNotBlank(validityPeriod),"VALIDITYPERIOD",  parseDateString(validityPeriod))
                    .set(StrUtil.isNotBlank(produceDate), "PRODUCEDATE", parseDateString(produceDate));
            boolean isUpdated = mediStockBatchInfo.update(updateWrapper);
            if (!isUpdated) {
                return Result.error(CustomizeCode.FAIL, "更新失败");
            }
            return Result.success();
        }catch (Exception e){
            return Result.error(CustomizeCode.FAIL, e.getMessage());
        }
    }

    /** 导出接口--查询所有数据 */
    @PostMapping("/getDrugAndStoreBatchInfoList")
    public Result getDrugAndStoreBatchInfoList(@RequestBody JSONObject paramsJson) {
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            // 1. 提取参数
            String batchNumber = paramsJson.getString("batchNumber");   //药品批号
            String drugNameOrCode = paramsJson.getString("drugNameOrCode").toUpperCase();  //药品输入码等搜索条件，转化为大写形式
            String isStock = paramsJson.getString("isStock");     //是否是药库
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            // 2. 构建查询条件
            QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StrUtil.isNotBlank(batchNumber),"BATCHNUMBER", batchNumber);    // 等于CATALOGID， like DRUGINPUTCODE 或 drugNameOrCode
            // 动态条件组合（药品输入码/药品代码模糊查询 + 时间范围查询）
            if(StrUtil.isNotBlank(drugNameOrCode)){
                queryWrapper.and(wrapper -> {
                    // 模糊查询：DRUGINPUTCODE LIKE drugNameOrCode 或 DRUGCODE LIKE drugNameOrCode
                    if(StrUtil.isNotBlank(drugNameOrCode)){
                        wrapper.and(q -> q
                                .like("DRUGINPUTCODE", drugNameOrCode)
                                .or()
                                .like("DRUGCODE", drugNameOrCode)
                        );
                    }
                });
            }
            // 添加 ValidityPeriod 时间范围查询
            if (StrUtil.isAllNotBlank(startDate, endDate)) {
                queryWrapper.between("VALIDITYPERIOD",
                        LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE_TIME),  //前端需传递 ISO 8601 格式时间（如 2023-10-10T23:59:59）。
                        LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE_TIME)
                );
            }
            // 3. 查询
            List<MediStockBatchInfo> queryInfos = mediStockBatchInfo.list(queryWrapper);
            // 4. 构造返回结果
            JSONObject result = new JSONObject();
            result.put("listCount", queryInfos.size());   //总条数
            result.put("MediStockBatchInfo", queryInfos);
            return Result.success(result);

        }catch (Exception e){
            return Result.error(CustomizeCode.FAIL, e.getMessage());
        }
    }


    // 自定义日期解析方法
    private LocalDateTime parseDateString(String dateStr) {
        if (StrUtil.isBlank(dateStr)) return null;
        try {
            // 直接解析完整日期时间（如 "2025-06-11 00:00:00"）
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e1) {
            try {
                // 解析纯日期并补充时间（如 "2025-06-11" → "2025-06-11T00:00:00"）
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                return date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("日期格式不合法: " + dateStr);
            }
        }
    }
}
