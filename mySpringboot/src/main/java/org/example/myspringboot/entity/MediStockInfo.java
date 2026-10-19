package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 功能： 药库库存表
 * 作者：yangjia
 * 日期：2025/3/13 上午11:28
 */
@Data
@TableName("MEDI_STOCKINFO")  // 指定表名和模式
public class MediStockInfo {

    @TableId(value = "ID", type = IdType.ASSIGN_UUID)  // 假设主键由 MyBatis-Plus 自动生成 UUID
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

    @TableField("INVENTORY")
    private Long inventory;   //库存量

    @TableField("ALTERLOWER")
    private Long alterLower;   //警戒下线

    @TableField("ALTERTOP")
    private Long alterTop;    //警戒上线

    @TableField("UNITID")
    private String unitId;     //单位id

    @TableField("UNITNAME")
    private String unitName;

    @TableField("PACKAGQUANTITY")
    private Long packageQuantity;    //包装量

    @TableField("STATUS")
    private String status;

    @TableField("OPERATORID")
    private String operatorId;

    @TableField("INPUTDATE")
    private LocalDateTime inputDate;

    @TableField("CREATEUSER")
    private String createUser;

    @TableField("CREATEDATE")
    private LocalDateTime createDate;

    @TableField("COMMENT_")  // 处理字段名中的下划线
    private String comment;

    @TableField("PRICEINVENTORY")
    private Long priceInventory = 0L;  // 默认值直接初始化   //库存余量
}