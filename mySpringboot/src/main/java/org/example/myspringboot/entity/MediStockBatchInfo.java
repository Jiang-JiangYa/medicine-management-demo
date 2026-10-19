package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能：MediStockBatchInfo - 药库分批库存信息表
 * 作者：yangjia
 * 日期：2025/3/17 下午4:12
 */
@Data
@TableName("MEDI_STOCKBATCHINFO")
public class MediStockBatchInfo {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("ORGAID")
    private String orgaId;

    @TableField("DEPTID")
    private String deptId;

    @TableField("DEPTINPUTCODE")
    private String deptInputCode;

    @TableField("DEPTCODE")
    private String deptCode;

    @TableField("DEPTNAME")
    private String deptName;

    @TableField("DRUGID")
    private String drugId;

    @TableField("CATALOGID")
    private String catalogId;

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
    private String supplierId;

    @TableField("SUPPLYINPUTCODE")
    private String supplyInputCode;

    @TableField("SUPPLYCODE")
    private String supplyCode;

    @TableField("SUPPLYNAME")
    private String supplyName;

    @TableField("UNITID")
    private String unitId;

    @TableField("UNITNAME")
    private String unitName;

    @TableField("PACKAGQUANTITY")
    private Integer packagQuantity;

    @TableField("NUMBER_")
    private Integer number;

    @TableField("PURCHASEPRICE")
    private BigDecimal purchasePrice;

    @TableField("RETAILPRICE")
    private BigDecimal retailPrice;

    @TableField("PURCHASEMONEY")
    private BigDecimal purchaseMoney;

    @TableField("RETAILMONEY")
    private BigDecimal retailMoney;

    @TableField("POSITION")
    private String position;

    @TableField("BATCHNUMBER")
    private String batchNumber;

    @TableField("BATCH")
    private String batch;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("VALIDITYPERIOD")
    private LocalDateTime validityPeriod;

    @TableField("STATUS")
    private String status;    //是否"在用"

    @TableField("OPERATORID")
    private String operatorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    @TableField("COMMENT_")
    private String comment;

    @TableField("PRICENUMBER")
    private Integer priceNumber;    //库存量

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("PRODUCEDATE")
    private LocalDateTime produceDate;
}
