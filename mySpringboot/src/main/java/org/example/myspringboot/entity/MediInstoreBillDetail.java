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
 * 功能：药品入库明细表 实体类
 * 作者：yangjia
 * 日期：2025/3/21 下午4:15
 */
@Data
@TableName("MEDI_INSTOREBILLDETAIL") // 指定完整表名
public class MediInstoreBillDetail {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // 主键为 UUID 分配策略
    private String id;

    @TableField("ORGAID")
    private String orgaId;

    @TableField("INSTOREBILLID")
    private String instoreBillId;      //主表id

    @TableField("BILLNO")
    private String billNo;

    @TableField("DRUGID")
    private String drugId;

    @TableField("CATALOGID")
    private String catalogId;   //改为药品目录id

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

    @TableField("NUMBER_")
    private Long number;

    @TableField("UNITID")
    private String unitId;

    @TableField("UNITNAME")
    private String unitName;

    @TableField("PACKAGQUANTITY")
    private Long packagQuantity;

    @TableField("INSTOREPRICE")
    private BigDecimal inStorePrice;

    @TableField("PURCHASEPRICE")
    private BigDecimal purchasePrice;

    @TableField("ADDRATE")
    private BigDecimal addRate;

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
    private String batch;    //批次

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("VALIDITYPERIOD")
    private LocalDateTime validityPeriod;

    @TableField("ISACTIVE")
    private String isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("STOPDATE")
    private LocalDateTime stopDate;

    @TableField("STATUS")
    private String status;   //是否审核

    @TableField("OPERATORID")
    private String operatorId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    @TableField("COMMENT_")
    private String comment;

    //数据字典未添加

    @TableField(value = "PURCHASEBILLDETAILID", exist = false)
    private String purchaseBillDetailId;    //采购订单明细id  --删除

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("PRODUCEDATE")
    private LocalDateTime produceDate;     //生产日期

    @TableField(value = "IDENTIFYCODE", exist = false)
    private String identifyCode;      //标识码   --删除字段
}
