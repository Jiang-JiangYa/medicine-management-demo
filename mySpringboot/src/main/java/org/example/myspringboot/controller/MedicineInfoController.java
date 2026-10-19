package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.myspringboot.common.CustomizeCode;
import org.example.myspringboot.common.CustomizeMessage;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.MediCatalogInfo;
import org.example.myspringboot.entity.MediDrugInfo;
import org.example.myspringboot.entity.MediDrugStoreInfo;
import org.example.myspringboot.service.impl.MediDrugStoreInfoImpl;
import org.example.myspringboot.service.impl.MedicineDrugInfoImpl;
import org.example.myspringboot.service.impl.MedicineInfoimpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能：机构药品目录信息- controller
 * 作者：yangjia
 * 日期：2025/2/27 下午10:47
 */
@Data
@RestController

@RequestMapping("/his/medicine")
public class MedicineInfoController {
    @Resource
    private MedicineInfoimpl medicineInfoService;   //药品分类目录表

    @Resource
    private MedicineDrugInfoImpl medicineDrugInfo;    //药品信息表

    @Resource
    private MediDrugStoreInfoImpl mediDrugStoreInfo;     //药品库存信息表
    /**
     * 获取机构药品目录左边树结构
     */
    @PostMapping("/getMediCatalogInfoList")
    public Result getMediCatalogInfoList(@RequestBody JSONObject paramsJson) {
        //String txtString = paramsJson.getString("txtString");
        List<MediCatalogInfo> mediCatalogInfos = medicineInfoService.list(new QueryWrapper<MediCatalogInfo>());   //查询所有
        return Result.success(mediCatalogInfos);
    }

    /**
     * 分页查询药品信息
     */
    @PostMapping("/getOrgaDrugInfoListWithPageFilter")
    public Result getOrgaDrugInfoListWithPageFilter(@RequestBody JSONObject paramsJson) {
        if (paramsJson != null) {
            Integer pageSize = paramsJson.getInteger("pageSize");
            Integer pageIndex = paramsJson.getInteger("pageIndex");
            String catalogId = paramsJson.getString("catalogId");
            String orgaId = paramsJson.getString("orgaId");
            String drugInputCode = paramsJson.getString("drugInputCode");
            if (drugInputCode != null) {
                drugInputCode = drugInputCode.toUpperCase();     //输入码转化为大写
            }
            Boolean isDJ = paramsJson.getBoolean("isDJ");
            if (StrUtil.isBlank(orgaId)) {
                return Result.error(CustomizeCode.DATA_NOT_FOUND, CustomizeMessage.DATA_NOT_FOUND);
            }
            QueryWrapper<MediDrugInfo> queryWrapper = new QueryWrapper<MediDrugInfo>();
            queryWrapper.eq(StrUtil.isNotBlank(catalogId), "CATALOGID", catalogId);    // 等于CATALOGID， like DRUGINPUTCODE 或 drugInputCode
            queryWrapper.eq(isDJ, "STATUS", "冻结");
//            queryWrapper.eq(!isDJ,"STATUS","解冻");
            final String searchCode = drugInputCode;
            if (StrUtil.isNotBlank(searchCode)) {
                queryWrapper.and(q -> q.like("DRUGINPUTCODE", searchCode).or().like("DRUGCODE", searchCode));
            }


            long totalNum = medicineDrugInfo.count(queryWrapper);
            JSONObject jsonObject = new JSONObject(); //创建1个 json 对象,2个属性
            jsonObject.put("pages", totalNum);   //总条数
            IPage<MediDrugInfo> queryOrgaDrugInfos = medicineDrugInfo.page(new Page<>(pageIndex, pageSize), queryWrapper);
            jsonObject.put("queryOrgaDrugInfo", queryOrgaDrugInfos.getRecords());
            return Result.success(jsonObject);

        } else {
            return Result.error(CustomizeCode.DATA_NOT_FOUND, CustomizeMessage.DATA_NOT_FOUND);
        }
    }

    /**
     * 删除药品信息前查询是否有库存 接口
     */
    @PostMapping("/deleteOrgaDrugCheckStock")
    public Result deleteOrgaDrugCheckStock(@RequestBody JSONObject paramsJson) {
        String orgaId = paramsJson.getString("orgaId");
        String drugId = paramsJson.getString("drugId");
//        //验证是否有库存
//        String msgInfo = medicineInfoService.deleteOrgaDrugCheckStock(orgaId, drugId);
//        if (msgInfo.isEmpty() || msgInfo.equals("")) {
//            //验证相对机构是否存在此药品
//            msgInfo = medicineInfoService.relativeOrgaDrug(orgaId, drugId);
//        }
//        QueryWrapper<MediDrugInfo> queryWrapper = new QueryWrapper<MediDrugInfo>();
//        queryWrapper.eq("ID",drugId);
//        medicineDrugInfo.removeById(drugId);
        return Result.success();
        // 没有库存时 返回的data 为 “”
    }

    /**
     * 删除药品信息表
     */
    @PostMapping("/updateOrgaDrugInfoIsActive")
    public Result updateOrgaDrugInfoIsActive(@RequestBody JSONObject paramsJson) {
        JSONObject drugInfoJson = paramsJson.getJSONObject("mediOrgaDruginfo");
        String drugId = drugInfoJson.getString("id");
        medicineDrugInfo.removeById(drugId);
        return Result.success();
    }

    /**
     * 冻结/解冻 药品
     */
    @PostMapping("/insertDrugStoreDJ")
    public Result insertDrugStoreDJ(@RequestBody JSONObject paramsJson) {

        JSONObject drugInfoJson = paramsJson.getJSONObject("QueryOrgaDrugInfo");
//        QueryOrgaDrugInfo drugInfo = drugInfoJson.toJavaObject(QueryOrgaDrugInfo.class);
//        String createUser = paramsJson.getString("createUser");
//        log.info("insertDrugStoreDJ 药品数据：{}", drugInfo);
//        boolean bResult = medicineInfoService.insertDrugStoreDJ(drugInfo, createUser);
        UpdateWrapper<MediDrugInfo> updateWrapper = new UpdateWrapper<MediDrugInfo>();
        updateWrapper.eq("ID", drugInfoJson.getString("id")).set("STATUS", drugInfoJson.getString("orgaDrugMemo"));
        boolean result = medicineDrugInfo.update(updateWrapper); // 调用 update 方法
        if (result) {
            return Result.success();
        } else
            return Result.error("失败");
    }

    /**
     * 新增药品信息表
     */
    @PostMapping("/addOrgaDrugInfoAndChargeItemPrice")
    public Result addOrgaDrugInfoAndChargeItemPrice(@RequestBody JSONObject paramsJson) {
        MediDrugInfo druginfo = paramsJson.getObject("MediOrgaDruginfo", MediDrugInfo.class);  //转化为 MediDrugInfo类型
        if (druginfo == null) {
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        }
        boolean result = medicineDrugInfo.save(druginfo);
        if (result) {
            return Result.success();
        } else
            return Result.error("新增失败！");
    }

    /**
     * 修改药品信息
     */
    @PostMapping("/updataOrgaDrugInfoAndChargeItemPrice")
    public Result updataOrgaDrugInfoAndChargeItemPrice(@RequestBody JSONObject paramsJson) {

        MediDrugInfo drugInfo = paramsJson.getObject("MediOrgaDruginfo", MediDrugInfo.class);

        if (drugInfo == null || StrUtil.isBlank(drugInfo.getId()))
            return Result.error(CustomizeCode.MISSING_PARAMETER, CustomizeMessage.MISSING_PARAMETER);
        if (medicineDrugInfo.getById(drugInfo.getId()) == null) {
            return Result.error(CustomizeCode.SELF_ERROR, "未查询到该数据！");
        } else {
            boolean result = medicineDrugInfo.saveOrUpdate(drugInfo);
            if (result) {
                return Result.success(medicineDrugInfo.getById(drugInfo.getId()));
            } else {
                return Result.error(CustomizeCode.SELF_ERROR, "更新数据失败");
            }
        }
    }
    /** 查询药品库存信息 */
    @PostMapping("/getDrugStoreQuery")
    public Result getDrugStoreQuery(@RequestBody JSONObject json) {
        if (!json.isEmpty()) {
            JSONObject drugInfoJson = json.getJSONObject("QueryOrgaDrugInfo");
            String drugId = drugInfoJson.getString("drugId");
            QueryWrapper<MediDrugStoreInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq(StrUtil.isNotBlank(drugId),"DRUGID",drugId);
            List<MediDrugStoreInfo> detailInfo = mediDrugStoreInfo.list(queryWrapper);
            // 只传递3个字段
            List<Map<String, Object>> result = detailInfo.stream().map(info -> {
                Map<String, Object> map = new HashMap<>();
                map.put("deptCode", info.getDeptCode());
                map.put("deptName", info.getDeptName());
                map.put("inventory", info.getInventory());
                return map;
            }).collect(Collectors.toList());
            return Result.success(result);
        } else
            return Result.error(CustomizeCode.DATA_NOT_FOUND, CustomizeMessage.DATA_NOT_FOUND);
    }


}    //