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
 * 功能：MEDI_INSTOREBILLMASTER 表实体类，药品入库主表
 * 作者：yangjia
 * 日期：2025/3/21 下午3:46
 */
@Data
@TableName("MEDI_INSTOREBILLMASTER")
public class MediInstoreBillMaster {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // 主键为 UUID 分配策略
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("CHECKDATE")
    private LocalDateTime checkDate;

    @TableField("CHECKID")
    private String checkId;

    @TableField("INVOICENUMBER")
    private String invoiceNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("INVOICEDATE")
    private LocalDateTime invoiceDate;

    @TableField("INVOICEAMOUNT")
    private BigDecimal invoiceAmount;

    @TableField("SUPPLIERID")
    private String supplierId;

    @TableField("SUPPLYINPUTCODE")
    private String supplyInputCode;

    @TableField("SUPPLYCODE")
    private String supplyCode;

    @TableField("SUPPLYNAME")
    private String supplyName;

    @TableField("BILLNO")
    private String billNo;

    @TableField("PUBLICSIGNID")
    private String publicSignId;

    @TableField("PUBLICSIGNNAME")
    private String publicSignName;

    @TableField("INSTORETYPEID")
    private String inStoreTypeId;

    @TableField("INSTORETYPENAME")
    private String inStoreTypeName;

    @TableField("EXPENDITUREAMOUNT")
    private BigDecimal expenditureAmount;

    @TableField("BILLMEMO")
    private String billMemo;

    @TableField("ISACTIVE")
    private String isActive;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("STOPDATE")
    private LocalDateTime stopDate;

    @TableField("STATUS")
    private String status;

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
}
