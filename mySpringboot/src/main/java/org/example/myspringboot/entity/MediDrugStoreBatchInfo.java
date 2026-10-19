package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能：药房库存批次表
 * 作者：yangjia
 * 日期：2025/4/25 下午7:12
 */
@Data
@TableName("MEDI_DRUGSTOREBATCHINFO")
public class MediDrugStoreBatchInfo {
    // --------------------- 主键及基础字段 ---------------------
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // UUID 主键策略
    private String id;

    @TableField("ORGAID")
    private String orgaID;

    @TableField("DEPTID")
    private String deptID;

    @TableField("DEPTINPUTCODE")
    private String deptInputCode;

    @TableField("DEPTCODE")
    private String deptCode;

    @TableField("DEPTNAME")
    private String deptName;

    // --------------------- 药品信息字段 ---------------------
    @TableField("DRUGID")
    private String drugID;

    @TableField("ORGADRUGID")
    private String orgaDrugID;

    @TableField("DRUGINPUTCODE")
    private String drugInputCode;

    @TableField("DRUGINPUTCODE2")
    private String drugInputCode2;

    @TableField("DRUGINPUTCODE3")
    private String drugInputCode3;

    @TableField("DRUGCODE")
    private String drugCode;

    @TableField("DRUGNAME")
    private String drugName;

    @TableField("DRUGSPECIFICATION")
    private String drugSpecification;

    @TableField("ORIGIN")
    private String origin;

    // --------------------- 供应商及单位信息 ---------------------
    @TableField("SUPPLIERID")
    private String supplierID;

    @TableField("SUPPLYINPUTCODE")
    private String supplyInputCode;

    @TableField("SUPPLYCODE")
    private String supplyCode;

    @TableField("SUPPLYNAME")
    private String supplyName;

    @TableField("UNITID")
    private String unitID;

    @TableField("UNITNAME")
    private String unitName;

    // --------------------- 库存数量与价格字段 ---------------------
    @Nullable
    @TableField("PACKAGQUANTITY")
    private Long packagQuantity; // NUMBER → Long

    @Nullable
    @TableField("NUMBER_")
    private Integer number_; // NUMBER → Long

    @Nullable
    @TableField("VIRTUALINVENTORY")
    private Integer virtualInventory; // NUMBER → Long

    @Nullable
    @TableField("PURCHASEPRICE")
    private BigDecimal purchasePrice; // NUMBER(15,5) → BigDecimal

    @Nullable
    @TableField("RETAILPRICE")
    private BigDecimal retailPrice;

    @Nullable
    @TableField("PURCHASEMONEY")
    private BigDecimal purchaseMoney; // NUMBER(15,4) → BigDecimal

    @Nullable
    @TableField("RETAILMONEY")
    private BigDecimal retailMoney;

    // --------------------- 批次与位置信息 ---------------------
    @TableField("POSITION")
    private String position;

    @TableField("BATCHNUMBER")
    private String batchNumber;

    @TableField("BATCH")
    private String batch;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("VALIDITYPERIOD")
    private LocalDateTime validityPeriod; // TIMESTAMP(0) → LocalDateTime

    // --------------------- 状态与操作信息 ---------------------
    @TableField("STATUS")
    private String status;

    @TableField("OPERATORID")
    private String operatorID;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    // --------------------- 特殊字段 ---------------------
    @TableField("COMMENT_")
    private String comment_;

    @TableField("OPERATION")
    private String operation;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // DATETIME(6) → LocalDateTime
    @TableField("PRODUCEDATE")
    private LocalDateTime produceDate; // 生产日期（数据库注释）
}
