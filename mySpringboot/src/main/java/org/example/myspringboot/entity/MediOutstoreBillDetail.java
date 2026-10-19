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
 * 功能：出库明细表 - 实体类
 * 作者：yangjia
 * 日期：2025/4/25 上午12:06
 */
@Data
@TableName("MEDI_OUTSTOREBILLDETAIL")
public class MediOutstoreBillDetail {
    // --------------------- 主键及基础字段 ---------------------
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // UUID 主键策略
    private String id;

    @TableField("ORGAID")
    private String orgaId;

    @TableField("OUTSTOREBILLID")
    private String outStoreBillId;

    @TableField("BILLNO")
    private String billNo;

    // --------------------- 药品信息字段 ---------------------
    @TableField("DRUGID")
    private String drugId;

    @TableField("ORGADRUGID")
    private String orgaDrugId;

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

    // --------------------- 供应商及批次信息 ---------------------
    @TableField("SUPPLIERID")
    private String supplierId;

    @TableField("SUPPLYINPUTCODE")
    private String supplyInputCode;

    @TableField("SUPPLYCODE")
    private String supplyCode;

    @TableField("SUPPLYNAME")
    private String supplyName;

    @TableField("BATCHNUMBER")
    private String batchNumber;

    @TableField("BATCH")
    private String batch;

    // --------------------- 价格与数量字段 ---------------------
    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") // 日期格式化
    @TableField("VALIDITYPERIOD")
    private LocalDateTime validityPeriod;

    @Nullable
    @TableField("PURCHASEPRICE")
    private BigDecimal purchasePrice; // NUMBER(15,5)

    @Nullable
    @TableField("RETAILPRICE")
    private BigDecimal retailPrice;


    @TableField("NUMBER_") // 处理特殊字段名
    private Long number = 0L; // NUMBER(38,0) → Long    //Integer?

    @TableField("UNITID")
    private String unitId;

    @TableField("UNITNAME")
    private String unitName;

    @Nullable
    @TableField("PACKAGQUANTITY")
    private Long packagQuantity;         //Integer?

    @Nullable
    @TableField("PURCHASEMONEY")
    private BigDecimal purchaseMoney; // NUMBER(15,4)

    @Nullable
    @TableField("RETAILMONEY")
    private BigDecimal retailMoney;

    // --------------------- 状态与操作信息 ---------------------
    @TableField("ISACTIVE")
    private String isActive;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("STOPDATE")
    private LocalDateTime stopDate;

    @TableField("STATUS")
    private String status;

    @TableField("OPERATORID")
    private String operatorId;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    // --------------------- 特殊字段 ---------------------
    @TableField("COMMENT_") // 映射带下划线的字段
    private String comment;

    @TableField("RECEIVESTATUS")
    private String receiveStatus;

    @TableField("RECEIVEUSER")
    private String receiveUser;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // DATE 类型格式化为日期（不含时间）
    @TableField("RECEIVEDATE")
    private LocalDateTime receiveDate;
}
