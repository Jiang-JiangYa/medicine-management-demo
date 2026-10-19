package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.MediStockBatchInfo;
import org.example.myspringboot.entity.MediStockInfo;
import org.example.myspringboot.service.impl.MediStockBatchInfoImpl;
import org.example.myspringboot.service.impl.MediStockInfoImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;



/**
 * 功能：库存查询页面 Controller
 * 作者：yangjia
 * 日期：2025/3/13 下午1:52
 */
@Data
@RestController

@RequestMapping("/his/medicine")
public class InventInquiryController {
    @Resource
    private MediStockInfoImpl mediStockInfo;    //操作药库库存表
    @Resource
    private MediStockBatchInfoImpl mediStockBatchInfo;   //药库分批库存表
    /**
     * 查询 药库库存表 信息。根据输入码，分类id、正序倒序、分页、0库存、达到库存下线
     */
    @PostMapping("/btnQueryMaster")
    public Result btnQueryMaster(@RequestBody JSONObject paramsJson) {
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        else{
            Integer pageSize = paramsJson.getInteger("pageSize");
            Integer pageIndex = paramsJson.getInteger("pageIndex");
            String catalogId = paramsJson.getString("catalogID");
            //String orgaId = paramsJson.getString("orgaId");
            String drugInputCode = paramsJson.getString("drugStoreInputCode").toUpperCase();  //药品输入码等搜索条件，转化为大写形式


            Boolean flag = paramsJson.getBoolean("flag");     //是否包含0库存
            Boolean lowFlag = paramsJson.getBoolean("lowFlag"); // 是否 只显示达到库存下线的药品
            Boolean sort = paramsJson.getBoolean("sort"); // true-正序  false-倒序

            QueryWrapper<MediStockInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StrUtil.isNotBlank(catalogId),"CATALOGID", catalogId);    // 等于CATALOGID， like DRUGINPUTCODE 或 drugInputCode
            // 动态条件组合  -- 有一个条件成立才使用 and()
            if(lowFlag || !flag || StrUtil.isNotBlank(drugInputCode)){
                queryWrapper.and(wrapper -> {
                    // 当 lowFlag 为 true 时，添加 INVENTORY <= ALTERLOWER
                    if (lowFlag) {
                        //apply 方法允许直接插入原生 SQL 片段，避免框架对值的自动处理（如添加引号）
                        wrapper.apply("INVENTORY <= ALTERLOWER");  // 直接拼接列名
                    }
                    // flag 为 false时，只显示INVENTORY > 0 的记录
                    if(!flag){
                        wrapper.gt("INVENTORY", 0);
                    }
                    // 模糊查询：DRUGINPUTCODE LIKE drugInputCode 或 DRUGCODE LIKE drugInputCode
                    if(StrUtil.isNotBlank(drugInputCode)){
                        wrapper.and(q -> q
                                .like("DRUGINPUTCODE", drugInputCode)
                                .or()
                                .like("DRUGCODE", drugInputCode)
                        );
                    }
                });
            }
            // 动态排序
            if (sort) {
                queryWrapper.orderByAsc("INVENTORY"); // 正序
            } else {
                queryWrapper.orderByDesc("INVENTORY"); // 倒序
            }
            JSONObject result = new JSONObject();
            IPage<MediStockInfo> queryInfos = mediStockInfo.page(new Page<>(pageIndex, pageSize), queryWrapper);
            result.put("resultCount", queryInfos.getTotal());
            result.put("DefineStockBillStockQueryInfo", queryInfos.getRecords());
            return Result.success(result);
        }
    }

    /** 查询药库分批库存表 Medi_StockBatchInfo   查询药品入库的批次信息，根据药品id查询 */
    @PostMapping("/btnQueryDetail")
    public Result btnQueryDetail(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }else{
            String drugId = paramsJson.getString("drugId");    //药品id
            Boolean isZerroDrug = paramsJson.getBoolean("isZerroDrug");    //是否包含0库存

            QueryWrapper<MediStockBatchInfo> queryWrapper = new QueryWrapper<>();
            if(StrUtil.isBlank(drugId)){
                return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
            }else{
                queryWrapper.eq(StrUtil.isNotBlank(drugId),"DRUGID", drugId);    // 等于CATALOGID， like DRUGINPUTCODE 或 drugInputCode
                if(!isZerroDrug){
                    queryWrapper.gt("PRICENUMBER", 0);
                }
                List<MediStockBatchInfo> infos = mediStockBatchInfo.list(queryWrapper);
                if(infos.isEmpty()){
                    return Result.error(CustomizeCode.DATA_NOT_FOUND,CustomizeMessage.DATA_NOT_FOUND);  //未查询到相关数据
                }
                JSONObject result = new JSONObject();   //DefineStockBatchInfo
                result.put("DefineStockBatchInfo", infos);
                return Result.success(result);
            }

        }
    }
}
