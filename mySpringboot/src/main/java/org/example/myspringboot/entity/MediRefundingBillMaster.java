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
 * 功能：退货单主表 实体类
 * 作者：yangjia
 * 日期：2025/4/23 下午8:12
 */
@Data
@TableName("MEDI_REFUNDINGBILLMASTER")
public class MediRefundingBillMaster {
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // 主键为 UUID 分配策略
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

    @TableField("CHECKID")
    private String checkID;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CHECKDATE")
    private LocalDateTime checkDate;

    @TableField("BILLNO")
    private String billNo;

    @TableField("PUBLICSIGNID")
    private String publicSignID;

    @TableField("PUBLICSIGNNAME")
    private String publicSignName;

    @TableField("INSTORETYPEID")
    private String inStoreTypeID;

    @TableField("INSTORETYPENAME")
    private String inStoreTypeName;

    @TableField("EXPENDITUREAMOUNT")
    private BigDecimal expenditureAmount;

    @TableField("SUPPLIERID")
    private String supplierID;

    @TableField("SUPPLYINPUTCODE")
    private String supplyInputCode;

    @TableField("SUPPLYCODE")
    private String supplyCode;

    @TableField("SUPPLYNAME")
    private String supplyName;

    @TableField("REFUNDINGMEMO")
    private String refundingMemo;

    @TableField("ISACTIVE")
    private String isActive;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("STOPDATE")
    private LocalDateTime stopDate;

    @TableField("STATUS")
    private String status;

    @TableField("OPERATORID")
    private String operatorID;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    @TableField("COMMENT_")  // 处理特殊字段名
    private String comment;

    @TableField("INVOICENUMBER")
    private String invoiceNumber;

    @Nullable
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INVOICEDATE")
    private LocalDateTime invoiceDate;

    @Nullable
    @TableField("INVOICEAMOUNT")
    private BigDecimal invoiceAmount;

    @TableField("SJTHRQ")
    private String sjthrq;  // 实际退货日期（按数据库注释为字符串格式）
}
