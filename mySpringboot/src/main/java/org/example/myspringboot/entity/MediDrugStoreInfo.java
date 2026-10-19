package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.lang.Nullable;
/**
 * 功能：
 * 作者：yangjia
 * 日期：2025/3/10 下午3:20
 */
@Data
@TableName("MEDI_DRUGSTOREINFO")
public class MediDrugStoreInfo {
    @TableId(type = IdType.ASSIGN_UUID)  // 使用 UUID 生成主键
    public String id ;
    /// <summary>
    /// 机构ID
    /// </summary>
    @TableField("ORGAID")
    public String orgaID ;
    /// <summary>
    /// 部门ID
    /// </summary>
    @TableField("DEPTID")
    public String deptID ;
    /// <summary>
    /// 部门输入码
    /// </summary>
    @TableField("DEPTINPUTCODE")
    public String deptInputCode ;
    /// <summary>
    /// 部门代码
    /// </summary>
    @TableField("DEPTCODE")
    public String deptCode ;
    /// <summary>
    /// 部门名称
    /// </summary>
    @TableField("DEPTNAME")
    public String deptName ;
    /// <summary>
    /// 药品ID
    /// </summary>
    @TableField("DRUGID")
    public String drugID ;
    /// <summary>
    /// 机构药品ID
    /// </summary>
    @TableField("ORGADRUGID")
    public String orgaDrugID ;
    /// <summary>
    /// 药品输入码
    /// </summary>
    @TableField("DRUGINPUTCODE")
    public String drugInputCode ;
    /// <summary>
    /// 药品输入码2
    /// </summary>
    @TableField("DRUGINPUTCODE2")
    public String drugInputCode2 ;
    /// <summary>
    /// 药品输入码3
    /// </summary>
    @TableField("DRUGINPUTCODE3")
    public String drugInputCode3 ;
    /// <summary>
    /// 药品代码
    /// </summary>
    @TableField("DRUGCODE")
    public String drugCode ;
    /// <summary>
    /// 药品名称
    /// </summary>
    @TableField("DRUGNAME")
    public String drugName ;
    /// <summary>
    /// 药品规格
    /// </summary>
    @TableField("DRUGSPECIFICATION")
    public String drugSpecification ;
    /// <summary>
    /// 产地
    /// </summary>
    @TableField("ORIGIN")
    public String origin ;
    /// <summary>
    /// 供货商ID
//    /// </summary>
//    public String supplierID ;
//    /// <summary>
//    /// 供应商输入码
//    /// </summary>
//    public String supplyInputCode ;
//    /// <summary>
//    /// 供应商代码
//    /// </summary>
//    public String supplyCode ;
//    /// <summary>
//    /// 供应商名称
//    /// </summary>
//    public String supplyName ;
    /// <summary>
    /// 单位ID
    /// </summary>
    @TableField("UNITID")
    public String unitID ;
    /// <summary>
    /// 单位名称
    /// </summary>
    @TableField("UNITNAME")
    public String unitName ;
    /// <summary>
    /// 包装量
    /// </summary>
    @Nullable
    @TableField("PACKAGQUANTITY")
    public Long packagQuantity ;
    /// <summary>
    ///可发库存数量
    /// </summary>
//    public String number ;
    /// <summary>
    /// 待发药数
    /// </summary>
    @TableField("VIRTUALINVENTORY")
    public Long virtualInventory ;

    @TableField("INVENTORY")
    public Long inventory ;

    @TableField("ALTERLOWER")
    private Long alterLower;

    @TableField("ALTERTOP")
    private Long alterTop;

    /// <summary>
    /// 批发价
    /// </summary>
    @Nullable
    @TableField("PURCHASEPRICE")
    public BigDecimal purchasePrice ;
    /// <summary>
    /// 零售价
    /// </summary>
    @Nullable
    @TableField("RETAILPRICE")
    public BigDecimal retailPrice ;
    /// <summary>
    /// 批发金额
    /// </summary>
    @Nullable
    @TableField("PURCHASEMONEY")
    public BigDecimal purchaseMoney ;
    /// <summary>
    /// 零售金额
    /// </summary>
    @Nullable
    @TableField("RETAILMONEY")
    public BigDecimal retailMoney ;
//    /// <summary>
//    /// 仓位
//    /// </summary>
//    public String position ;
//    /// <summary>
//    /// 批号
//    /// </summary>
//    public String batchNumber ;
//    /// <summary>
//    /// 批次
//    /// </summary>
//    public String batch ;
    /// <summary>
    /// 效期
    /// </summary>
//    @Nullable
//    public LocalDateTime validityPeriod ;
    /// <summary>
    /// 状态
    /// </summary>
    @TableField("STATUS")
    public String status ;
    /// <summary>
    /// 修改人ID
    /// </summary>
    @TableField("OPERATORID")
    public String operatorID ;
    /// <summary>
    /// 修改时间
    /// </summary>
    @Nullable
    @TableField("INPUTDATE")
    public LocalDateTime inputDate ;
    /// <summary>
    /// 创建人ID
    /// </summary>
    @TableField("CREATEUSER")
    public String createUser ;
    /// <summary>
    /// 创建时间
    /// </summary>
    @Nullable
    @TableField("CREATEDATE")
    public LocalDateTime createDate ;
    /// <summary>
    /// 库存数量
    /// </summary>
    @TableField("COMMENT_")
    public String comment_ ;

    @TableField("OPERATION")
    private String operation;
}
