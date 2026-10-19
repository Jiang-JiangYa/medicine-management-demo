package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能： 退库单明细表 实体类
 * 作者：yangjia
 * 日期：2025/4/23 下午8:27
 */
@Data
@TableName("MEDI_REFUNDINGBILLDETAIL")
public class MediRefundingBillDetail {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // 非自增主键
    private String id;

    @TableField("ORGAID")
    private String orgaID;

    @TableField("REFUNDINGBILLID")
    private String refundingBillID;

    @TableField("BILLNO")
    private String billNo;

    @TableField("DRUGID")
    private String drugID;

    @TableField("ORGADRUGID")
    private String orgaDrugIDQ;

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

    @TableField("PACKAGQUANTITY")
    private Long packagQuantity; // NUMBER(38,0) → Long

    @TableField("NUMBER_")
    private Long number; // NUMBER(38,0) → Long

    @TableField("PURCHASEPRICE")
    private BigDecimal purchasePrice; // NUMBER(15,5) → BigDecimal

    @TableField("RETAILPRICE")
    private BigDecimal retailPrice; // NUMBER(15,5) → BigDecimal

    @TableField("PURCHASEMONEY")
    private BigDecimal purchaseMoney; // NUMBER(15,4) → BigDecimal

    @TableField("RETAILMONEY")
    private BigDecimal retailMoney; // NUMBER(15,4) → BigDecimal

    @TableField("EXPENDITUREAMOUNT")
    private BigDecimal expenditureAmount; // NUMBER(15,4) → BigDecimal

    @TableField("BATCH")
    private String batch;

    @TableField("BATCHNUMBER")
    private String batchNumber;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALIDITYPERIOD")
    private LocalDateTime validityPeriod; // TIMESTAMP → LocalDateTime

    @TableField("ISACTIVE")
    private String isActive;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("STOPDATE")
    private LocalDateTime stopDate; // TIMESTAMP → LocalDateTime

    @TableField("STATUS")
    private String status;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate; // TIMESTAMP → LocalDateTime

    @TableField("OPERATORID")
    private String operatorID;

    @TableField("CREATEUSER")
    private String createUser;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATEDATE")
    private LocalDateTime createDate; // TIMESTAMP → LocalDateTime

    @TableField("COMMENT_") // 特殊字段名映射
    private String comment;

    @TableField("IDENTIFYCODE")
    private String identifyCode; // 溯源码 (长度4000)
}
