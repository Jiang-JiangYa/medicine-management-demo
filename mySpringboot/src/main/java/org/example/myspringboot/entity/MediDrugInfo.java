package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("Medi_DrugInfo")
public class MediDrugInfo {
    /// ID
    @TableId(type = IdType.ASSIGN_UUID)  // 使用 UUID 生成主键
    public String id ;
    /// 机构ID
    @TableField("ORGAID")
    public String orgaId;
    /// 药品代码
    @TableField("DRUGCODE")
    public String drugCode ;
    /// 药品名称
    @TableField("DRUGNAME")
    public String drugName ;
    /// 药品输入码
    @TableField("DRUGINPUTCODE")
    public String drugInputCode ;
    /// 药品输入码2
    @TableField("DRUGINPUTCODE2")
    public String drugInputCode2 ;
    /// 药品输入码3
    @TableField("DRUGINPUTCODE3")
    public String drugInputCode3 ;
    /// 药品分类ID
    @TableField("CATALOGID")
    public String catalogID ;
    /// 药品品种ID
    @TableField("VARIETYID")
    public String varietyID ;
    /// 商品名称
    @TableField("COMMODITYNAME")
    public String commodityName ;
    /// 包装规格(说明书）
    @TableField("DRUGSPECIFICATION")
    public String drugSpecification ;
    /// 基本剂量
    @TableField("BASICDOSIMETRY")
    public BigDecimal basicDosimetry ;
    /// 计价单位ID
    @TableField("PRICEUNITID")
    public String priceUnitID ;
    /// 计价单位名称
    @TableField("PRICEUNITNAME")
    public String priceUnitName ;
    /// 计价包装规格
    @TableField("PACKAGESPEC")
    public String packageSpec ;
    /// 医保编码
    @TableField("INSURECODE")
    public String insureCode ;
    /// 医保属性ID
    @TableField("INSUREPROPERTYID")
    public String insurePropertyID ;
    /// 医保属性名称
    @TableField("INSUREPROPERTYNAME")
    public String insurePropertyName ;
    //public String DrugRoute ;
    /// 用药途径ID
    @TableField("DRUGROUTEID")
    public String drugRouteID ;
    /// 用药途径名称
    @TableField("DRUGROUTENAME")
    public String drugRouteName ;
    /// <summary>
    /// DDD值
    @TableField("DDDVALUE")
    public BigDecimal dddValue ;
    /// 产地
    @TableField("ORIGIN")
    public String origin ;
    /// 批准文号
    @TableField("APPROVALNUMBER")
    public String approvalNumber ;
    /// 注册商标
    @TableField("REGISTEREDTRADEMARK")
    public String registeredTrademark ;
    /// GMP标志
    @TableField("GMPMARK")
    public String gmpMark ;
    /// OCT标志
    @TableField("OCTMARK")
    public String octMark ;
    /// 皮试标志
    @TableField("SKINTESTMARK")
    public String skinTestMark ;
    /// 基药标志ID
    @TableField("BASEDRUGMARKID")
    public String baseDrugMarkID ;
    /// 基药标志名称
    @TableField("BASEDRUGMARKNAME")
    public String baseDrugMarkName ;
    /// 服务使用对象
    @TableField("SERVICETYPE")
    public String serviceType ;
    /// 核算分类ID
    @TableField("RECKID")
    public String reckID ;
    /// 核算分类代码
    @TableField("RECKCODE")
    public String reckCode ;
    /// 核算分类名称
    @TableField("RECKNAME")
    public String reckName ;
    /// 门诊发票分类ID
    @TableField("INVOOUTPID")
    public String invoOutpID ;
    /// 门诊发票分类代码
    @TableField("INVOOUTPCODE")
    public String invoOutpCode ;
    /// 门诊发票分类名称
    @TableField("INVOOUTPNAME")
    public String invoOutpName ;
    /// 住院发票分类ID
    @TableField("INVOIINPID")
    public String invoiInpID ;
    /// 住院发票分类代码
    @TableField("INVOIINPCODE")
    public String invoiInpCode ;
    /// 住院发票分类名称
    @TableField("INVOIINPNAME")
    public String invoiInpName ;
    /// 物价分类ID
    @TableField("PRICEKINDID")
    public String priceKindID ;
    /// 物价分类代码
    @TableField("PRICEKINDCODE")
    public String priceKindCode ;
    /// 物价分类名称
    @TableField("PRICEKINDNAME")
    public String priceKindName ;
    /// 病案分类ID
    @TableField("CASECLASSID")
    public String caseClassID ;
    /// 病案分类代码
    @TableField("CASECLASSCODE")
    public String caseClassCode ;
    /// 病案分类名称
    @TableField("CASECLASSNAME")
    public String caseClassName ;
    /// 是否在用
    @TableField("ISACTIVE")
    public String isActive ;
    /// 停用日期

    @Nullable
    @TableField("STOPDATE")
    public LocalDateTime stopDate ;
    /// 修改时间
    @Nullable
    @TableField("INPUTDATE")
    public LocalDateTime inputDate ;
    /// 修改人ID
    @TableField("OPERATORID")
    public String operatorID ;
    /// 创建人ID
    @TableField("CREATEUSER")
    public String createUser ;
    /// 创建时间
    @Nullable
    @TableField("CREATEDATE")
    public LocalDateTime createDate ;
    /// 备注
    @TableField("COMMENT_")
    public String comment_ ;
    @Nullable
    @TableField("OUTPPACKAGQUANTITY")
    public BigDecimal outpPackagQuantity ;
    @TableField("OUTPPACKAGUNITID")
    public String outpPackagUnitID ;
    @TableField("OUTPPACKAGUNITNAME")
    public String outpPackagUnitName ;
    @Nullable
    @TableField("STOCKPACKAGQUANTITY")
    public BigDecimal stockPackagQuantity ;
    @TableField("STOCKPACKAGUNITID")
    public String stockPackagUnitID ;
    @TableField("STOCKPACKAGUNITNAME")
    public String stockPackagUnitName ;

    @TableField("OUTPPACKAGESPEC")
    public String outpPackageSpec; // 出库包装规格
    @TableField("STOCKPACKAGESPEC")
    public String stockPackageSpec; // 库存包装规格
    @TableField("CHARGEITEMCLASSID")
    public String chargeItemClassID; // 收费项目分类ID
    @TableField("DRUGFORMID")
    public String drugFormID; // 药品剂型ID
    @TableField("DRUGFORMNAME")
    public String drugFormName; // 药品剂型名称
    @TableField("DOSIMETRYUNITID")
    public String dosimetryUnitID; // 剂量单位ID
    @TableField("DOSIMETRYUNITNAME")
    public String dosimetryUnitName; // 剂量单位名称
    @TableField("ANTIBIOTICID")
    public String antibioticID; // 抗生素ID
    @TableField("ANTIBIOTICNAME")
    public String antibioticName; // 抗生素名称
    /** 默认频次 */
    @TableField("DRUGFREQUENCY")
    public String drugFrequency; // 默认频次
    /// 默认用量
    @Nullable
    @TableField("DRUGDOSAGE")
    public BigDecimal drugDosage; // 默认用量
    /// 增加：默认供货商（20210311 up by jx）
    @TableField("SUPPLIERID")
    public String supplierid; // 默认供货商ID

    @TableField("COUNTRYCODE")
    public String countryCode; // 国家代码
    @TableField("STATUS")
    public String status;     //药品状态，冻结 / 解冻
}
