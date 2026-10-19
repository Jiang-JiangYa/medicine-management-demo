package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.sql.Date;

/**
 * 功能： 药品分类信息
 * 作者：yangjia
 * 日期：2025/2/27 下午4:58
 */
@TableName("MEDI_CATALOGINFO")
@Data
public class MediCatalogInfo {
    /// ID
    public String id ;
    /// 机构ID
    @TableField("ORGAID")
    public String orgaId ;
    /// 上一级分类ID
    @TableField("PARENTID")
    public String parentId ;
    /// 药品分类代码
    @TableField("CATALOGCODE")
    public String catalogCode ;
    /// 药品分类输入码
    /** 药品分类输入码*/
    @TableField("CATALOGINPUTCODE")
    public String catalogInputCode ;
    /// 药品分类名称
    @TableField("CATALOGNAME")
    public String catalogName ;
    /// 收费项目分类ID
    @TableField("CHARGEITEMCLASSID")
    public String chargeItemClassID ;
    /// 修改时间
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("INPUTDATE")
    public Date inputDate ;
    /// 修改人ID
    @TableField("OPERATORID")
    public String operatorID ;
    /// 创建人ID
    @TableField("CREATEUSER")
    public String createUser ;
    /// 创建时间
    @Nullable
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("CREATEDATE")
    public Date createDate ;
    /// 备注
    @TableField("COMMENT_")
    public String comment_ ;
}
