package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.MediInstoreBillDetail;
import org.example.myspringboot.entity.MediInstoreBillMaster;
import org.example.myspringboot.entity.MediOutStoreBillMaster;
import org.example.myspringboot.entity.MediOutstoreBillDetail;
import org.example.myspringboot.service.impl.MediInstoreBillDetailImpl;
import org.example.myspringboot.service.impl.MediInstoreBillMasterImpl;
import org.example.myspringboot.service.impl.MediOutStoreBillMasterImpl;
import org.example.myspringboot.service.impl.MediOutstoreBillDetailImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 功能：出入库查询 Controller
 * 作者：yangjia
 * 日期：2025/5/1 下午4:33
 */
@Data
@Slf4j  // 自动生成 log 对象
@RestController

@RequestMapping("/his/medicine")
public class inOutQueryController {

    @Resource
    MediOutStoreBillMasterImpl mediOutStoreBillMaster;

    @Resource
    MediOutstoreBillDetailImpl mediOutstoreBillDetail;

    @Resource
    MediInstoreBillMasterImpl mediInstoreBillMaster;   //药品入库主表

    @Resource
    MediInstoreBillDetailImpl mediInstoreBillDetail;   //药品入库明细表

    /** 查询入库记录 */
    @PostMapping("/getInStoreBillInfoWithPageFilter")
    public Result getInStoreBillInfoWithPageFilter(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            String dateType = paramsJson.getString("dateType");     // 0（审核时间）1（发票时间）
            String invoiceNumber = paramsJson.getString("sinvoicenumber");      //发票号  //模糊查询
            String drugNameOrCode = paramsJson.getString("durgNameOrCode").toUpperCase();     //药品信息（拼音/代码）
            String supplierInfo = paramsJson.getString("supplierId").toUpperCase();     //供应商拼音或代码
            String limit = paramsJson.getString("limit");     // 页面显示数量
            String offset = paramsJson.getString("offset");     //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  //前端传递格式
            // 1. 查询相应的出库单主表
            QueryWrapper<MediInstoreBillMaster> masterQueryWrapper = new QueryWrapper<>();
            if(dateType.equals("0")){
                // 查询 审核时间
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    masterQueryWrapper.between("CHECKDATE",
                            LocalDateTime.parse(startDate, formatter),
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            if(dateType.equals("1")){
                // 查询 发票时间
                if (StrUtil.isAllNotBlank(startDate, endDate)) {
                    masterQueryWrapper.between("INVOICEDATE",
                            LocalDateTime.parse(startDate, formatter),
                            LocalDateTime.parse(endDate, formatter)
                    );
                }
            }
            // 模糊查询发票号
            masterQueryWrapper.like(StrUtil.isNotBlank(invoiceNumber),"INVOICENUMBER", invoiceNumber)
                    .eq("STATUS","已审核").eq("ISACTIVE","在用");
            List<MediInstoreBillMaster> masterInfos = mediInstoreBillMaster.list(masterQueryWrapper);
            log.info("查询到的主表记录总数："+String.valueOf(masterInfos.size()));   //打印查询的主表数量
            // 2. 根据主表ID查询 明细表记录,所有记录放进results
            List<MediInstoreBillDetail> results = new ArrayList<MediInstoreBillDetail>() {};
            for (MediInstoreBillMaster masterInfo : masterInfos) {
                QueryWrapper<MediInstoreBillDetail> detailQueryWrapper = new QueryWrapper<>();
                detailQueryWrapper.eq("INSTOREBILLID", masterInfo.getId())
                        .eq("STATUS","已审核").eq("ISACTIVE","在用");
                if( StrUtil.isNotBlank(supplierInfo) || StrUtil.isNotBlank(drugNameOrCode)){
                    detailQueryWrapper.and(wrapper -> {
                        // 模糊查询：SUPPLYCODE LIKE supplierinfo 或 SUPPLYINPUTCODE LIKE supplierinfo
                        if(StrUtil.isNotBlank(supplierInfo)){
                            wrapper.and(q -> q
                                    .like("SUPPLYCODE", supplierInfo)
                                    .or()
                                    .like("SUPPLYINPUTCODE", supplierInfo)
                            );
                        }
                        // 模糊查询药品信息
                        if(StrUtil.isNotBlank(drugNameOrCode)){
                            wrapper.and(q -> q
                                    .like("DRUGCODE", drugNameOrCode)
                                    .or()
                                    .like("DRUGINPUTCODE", drugNameOrCode)
                            );
                        }
                    });
                }
                detailQueryWrapper.orderByDesc("CREATEDATE"); //按创建时间倒序排
                List<MediInstoreBillDetail> details = mediInstoreBillDetail.list(detailQueryWrapper);
                results.addAll(details); // 插入到 results 列表
            }

            // 转换分页参数（ offset 是页码，从 1 开始）
            int pageSize = Integer.parseInt(limit);
            int pageNumber = Integer.parseInt(offset);
            int start = (pageNumber - 1) * pageSize;
            int end = Math.min(start + pageSize, results.size());

            List<MediInstoreBillDetail> pagedList = results.subList(start, end);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", results.size());   //总条数
            jsonObject.put("DefineGetInOrOutStoreBillInfoByIn", pagedList);
            return Result.success(jsonObject);
        }catch (Exception e){
            return Result.error(CustomizeCode.FAIL, "错误："+ e.getMessage());
        }
    }

    /** 查询出库记录 。只能根据审核时间查询 */
    @PostMapping("/getOutStoreBillInfoWithPageFilter")
    public Result getOutStoreBillInfoWithPageFilter(@RequestBody JSONObject paramsJson){
        if(paramsJson.isEmpty()){
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        try{
            String startDate = paramsJson.getString("startDate"); // 开始时间
            String endDate = paramsJson.getString("endDate"); // 结束时间
            String RelativeDeptId = paramsJson.getString("deptid");     // 出库相对部门id
            String drugNameOrCode = paramsJson.getString("durgNameOrCode").toUpperCase(); //药品信息（拼音/代码）
            String supplierInfo = paramsJson.getString("supplierId").toUpperCase();     //供应商拼音/代码
            String limit = paramsJson.getString("limit");     // 页面显示数量
            String offset = paramsJson.getString("offset");     //
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");  //前端传递格式
            // 1. 根据审核时间、出库相对部门查询相应的出库单主表
            QueryWrapper<MediOutStoreBillMaster> masterQueryWrapper = new QueryWrapper<>();
            // 查询 审核时间
            if (StrUtil.isAllNotBlank(startDate, endDate)) {
                masterQueryWrapper.between("CHECKDATE",
                        LocalDateTime.parse(startDate, formatter),
                        LocalDateTime.parse(endDate, formatter)
                );
            }
            // 查询相对部门
            masterQueryWrapper.eq(StrUtil.isNotBlank(RelativeDeptId),"RELATIVEDEPTID", RelativeDeptId)
                    .eq("STATUS","已审核").eq("ISACTIVE","在用");
            List<MediOutStoreBillMaster> masterInfos = mediOutStoreBillMaster.list(masterQueryWrapper);
            log.info("查询到的主表记录总数：" + String.valueOf(masterInfos.size()));  //打印查询的主表数量

            // 2. 根据主表ID查询 明细表记录,所有记录放进results
            List<MediOutstoreBillDetail> results = new ArrayList<MediOutstoreBillDetail>() {};
            for (MediOutStoreBillMaster masterInfo : masterInfos) {
                QueryWrapper<MediOutstoreBillDetail> detailQueryWrapper = new QueryWrapper<>();
                detailQueryWrapper.eq("OUTSTOREBILLID", masterInfo.getId())
                        .eq("STATUS","已审核").eq("ISACTIVE","在用");
                if( StrUtil.isNotBlank(supplierInfo) || StrUtil.isNotBlank(drugNameOrCode)){
                    detailQueryWrapper.and(wrapper -> {
                        // 模糊查询：SUPPLYCODE LIKE supplierinfo 或 SUPPLYINPUTCODE LIKE supplierinfo
                        if(StrUtil.isNotBlank(supplierInfo)){
                            wrapper.and(q -> q
                                    .like("SUPPLYCODE", supplierInfo)
                                    .or()
                                    .like("SUPPLYINPUTCODE", supplierInfo)
                            );
                        }
                        // 模糊查询药品信息
                        if(StrUtil.isNotBlank(drugNameOrCode)){
                            wrapper.and(q -> q
                                    .like("DRUGCODE", drugNameOrCode)
                                    .or()
                                    .like("DRUGINPUTCODE", drugNameOrCode)
                            );
                        }
                    });
                }
                detailQueryWrapper.orderByDesc("CREATEDATE"); //按创建时间倒序排
                List<MediOutstoreBillDetail> details = mediOutstoreBillDetail.list(detailQueryWrapper);
                results.addAll(details); // 插入到 results 列表
            }

            // 转换分页参数（ offset 是页码，从 1 开始）
            int pageSize = Integer.parseInt(limit);
            int pageNumber = Integer.parseInt(offset);
            int start = (pageNumber - 1) * pageSize;
            int end = Math.min(start + pageSize, results.size());

            List<MediOutstoreBillDetail> pagedList = results.subList(start, end);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", results.size());   //总条数
            jsonObject.put("DefineGetInOrOutStoreBillInfoByOut", pagedList);
            return Result.success(jsonObject);
        }catch (Exception e){
            return Result.error(CustomizeCode.FAIL, "错误："+ e.getMessage());
        }
    }
}
