package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * 功能：
 * 作者：yangjia
 * 日期：2025/4/25 上午12:18
 */
@Data
@TableName("MEDI_OUTSTOREBILLMASTER")
public class MediOutStoreBillMaster {
    // --------------------- 主键及基础字段 ---------------------
    @TableId(value = "ID", type = IdType.ASSIGN_UUID) // UUID 主键策略
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

    // --------------------- 关联部门信息字段 ---------------------
    @TableField("RELATIVEDEPTID")
    private String relativeDeptId;

    @TableField("RECEIVINGDEPTINPUTCODE")
    private String receivingDeptInputCode;

    @TableField("RECEIVINGDEPTCODE")
    private String receivingDeptCode;

    @TableField("RECEIVINGDEPTNAME")
    private String receivingDeptName;

    // --------------------- 单据核心信息字段 ---------------------
    @TableField("BILLNO")
    private String billNo;

    @TableField("CHECKID")
    private String checkId;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 审核日期格式化
    @TableField("CHECKDATE")
    private LocalDateTime checkDate;

    @TableField("OUTSTOREMEMO")
    private String outStoreMemo;

    // --------------------- 状态与操作字段 ---------------------
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") // 输入日期含 6 位毫秒
    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    // --------------------- 特殊字段 ---------------------
    @TableField("COMMENT_") // 处理特殊字段名
    private String comment_;

    @TableField("INVOICENUMBER") // 发票号（数据库注释）
    private String invoiceNumber;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("INVOICEDATE") // 开票日期（数据库注释）
    private LocalDateTime invoiceDate;

    @TableField("RECEIVESTATUS") // 接收状态
    private String receiveStatus;

    @TableField("RECEIVEUSER") // 接收人
    private String receiveUser;

    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("RECEIVEDATE") // 接收日期
    private LocalDateTime receiveDate;
}
