<script lang="ts">
// export default defineComponent({
//   name: 'OrgaDrugContents',
// })
</script>
<script setup lang="ts">
import {
  addOrgaDrugInfoAndChargeItemPrice,
  deleteOrgaDrugCheckStock,
  GetDrugInfoListForOrgaDrug2,
  getDrugStoreQuery,
  getinsertDrugStoreDJ,
  getMediCatalogInfoList,
  getOrgaDrugInfoListWithPageFilter,
  UpdataOrgaDrugInfoAndChargeItemPrice,
  updateOrgaDrugInfoIsActive
} from "@/api/drugAdministration/orgaDrugContents";
import { useUserStore } from "@/stores/modules/user";
import { exportDrugExpiryTable } from "@/utils/drugAdministration/excel";
import { throttle } from "@/utils/drugAdministration/throttle";
import { ElMessage, ElMessageBox, ElTable, FormInstance } from "element-plus";
import { nextTick, onMounted, reactive, ref } from "vue";

////常用变量//
const userInfo = ref(useUserStore().getUserInfo()); //获取当前登录账户的信息
const treeDataleft: any = ref([]); //左边左侧树
const tableDataright: any = ref([]); //右边表格
const singleTableRef = ref<InstanceType<typeof ElTable>>();
const orgList: any = ref([{ id: "0521", orgaName: "毕设演示机构" }]); //机构数据源
const treeArr: any = ref([]);
const refList: any = reactive({});
const tableLoading = ref(false);
//药品代码数据源
//const itemCodeList: any = ref([]);
const drugInfoLoading = ref(false);
const drugInfoKCList: any = ref([]); //弹窗库存表格

const IsYbItemCodeCheck = ref(""); ////判断是否走医保编码验证  从参数设置中读取
const IsOutpPackagQuantityNotChange = ref("否"); ////决定是否让门诊包装量不可改变
const GroupFlag: any = ref([]); //获取用户配置
const IsOutGroupFlag: any = ref([]); //获取决定是否让门诊包装量不可改变
const zujian: any = ref(""); //判断新增/修改
const dateTime = ref(""); //系统时间
const searchForm: any = reactive({
  //查询条件
  currentOrg: orgList.value[0], //机构
  drugname: "", //药品信息
  issfdj: false //是否冻结
});
//
const skinTypeList = ref([
  { id: 1, kvmemo: "青霉素" },
  { id: "2", kvmemo: "破伤风" },
  { id: "3", kvmemo: "左氧氟沙星" },
  { id: "4", kvmemo: "头孢类" },
  { id: "5", kvmemo: "普鲁卡因" },
  { id: "6", kvmemo: "磺胺类" },
  { id: "7", kvmemo: "氯霉素" },
  { id: "8", kvmemo: "黄芪" },
  { id: "9", kvmemo: "其他" }
]);

const currentTree = ref(); //左侧树
const handleNodeClick = (data: Tree) => {
  // console.log(data, '选中左侧目录树')
  currentTree.value = data;
  DrugBalanceMaster(0); //查询右侧表格
};
//右边表格
const tableDatarightRow: any = ref(""); //右侧表格当前行
//默认冻结/改变按钮状态
const flagShowCameras: any = ref(true); //true 为 “冻结”
const handleCurrentChange = (val: any) => {
  tableDatarightRow.value = val;
  if (tableDatarightRow.value.status == "冻结") {
    //解冻
    flagShowCameras.value = false;
  } else {
    //冻结
    flagShowCameras.value = true;
  }
  // console.log(flagShowCameras.value, '判断冻结按钮状态')
  // console.log(tableDatarightRow.value, '点击右边表格数据进行操作')
};
//表单
const formData: any = reactive({
  id: "",
  orgaID: "",
  drugInputCode: "",
  drugInputCode2: "",
  drugInputCode3: "",
  catalogId: "",
  varietyId: "",
  commodityName: "",
  drugSpecification: "",
  priceUnitId: "",
  priceUnitName: "",
  yldwName: "",
  packageSpec: "",
  insureCode: "",
  insurePropertyName: "",
  drugRoute: "",
  approvalNumber: "",
  registeredTrademark: "",
  gmpMark: "",
  octMark: "",
  baseDrugMarkId: "",
  baseDrugMarkName: "",
  serviceType: "",
  isActive: "",
  stopDate: "",
  inputDate: "",
  operatorId: "",
  createUser: "",
  createDate: "",
  comment_: "",
  drugFormId: "",
  dosimetryUnitId: "",
  dosimetryUnitName: "",
  varietyName: "",
  varietyCode: "",
  skinTestMark: "否",
  outpPackagQuantity: 1,
  outpPackagUnitId: "",
  outpPackagUnitName: "",
  stockPackagQuantity: 1,
  stockPackagUnitId: "",
  stockPackagUnitName: "",
  outpPackageSpec: "",
  stockPackageSpec: "",
  chargeItemClassId: "",
  itemClassName: "",
  prescAuthId: "",
  prescAuthName: "",
  antibioticId: "",
  antibioticName: "",
  isSelected: "",
  state: "",
  invoOutpId: "",
  invoOutpCode: "",
  invooutpname: "",
  invoiInpId: "",
  invoiInpCode: "",
  invoiInpName: "",
  drugFrequency: "",
  drugFrequencyname: "",
  drugDosage: "",
  useUnitName: "",
  OutpPerformedDeptId: "", //门诊执行科室id
  OutpPerformedDeptName: "",
  InpPerformedDeptName: "",
  InpPerformedDeptId: "", //住院执行科室id
  addRate: "0", //加成率
  OrderClassId: "", //医嘱类型id
  OrderClassname: "",
  FeeTypeMaskFlag: "不按费别计费", //费别计费标志
  skinTestTypeId: "",
  skinTestTypeName: "", //皮试方法
  TestTypeName: "",
  isDl: false, //是否带量
  isGpo: false, //是否GPO
  mzsy: true, //门诊使用
  zysy: true, //住院使用
  jzsy: true, //急诊使用
  CountryCode: "", //国家码
  ypid: "", //YPID
  itemCode: "", //项目代码/药品代码
  itemName: "", //项目名称/药品名称
  DrugFormName: "", //剂型
  Origin: "", //产地
  InsurePropertyID: "", //保险属性
  itemBasicUnit: "", //基本单位
  BasicDosimetry: "", //基本包装量
  itemMemo: "", //项目备注
  insureRate: "", //医保单位转换系数
  purchaseRate: "1", //采购转换系数
  jbjl: "",
  drugPrescauthId: "", //药品处方权限id
  drugPrescauthName: "", //药品处方权限名称
  sexLimit: "0", //性别限定 0-不限，1-男，2-女
  netDrugstore: false, //云药房标志 0-否 false ，1-是  true
  drugPropertyId: "", // 药品属性
  identifyCode: "" // 药品标识码
});
/**新增弹窗-表单中所有的下拉框： */
const formList: any = reactive({
  inAbutesList: [
    { id: 1, kvmemo: "医保完全可保" },
    { id: 2, kvmemo: "价高10%" },
    { id: 3, kvmemo: "价高20%" },
    { id: 4, kvmemo: "价高30%" },
    { id: 5, kvmemo: "价高40%" }
  ], //保险属性
  usPbnitList: [], //用法包装单位
  inpPerformedDeptList: [
    //门诊包装单位
    { id: 1, kvmemo: "片" },
    { id: 2, kvmemo: "盒" },
    { id: 3, kvmemo: "包" },
    { id: 4, kvmemo: "支" },
    { id: 5, kvmemo: "副" }
  ],
  stockPackagList: [
    { id: 1, kvmemo: "片" },
    { id: 2, kvmemo: "盒" },
    { id: 3, kvmemo: "包" },
    { id: 4, kvmemo: "支" },
    { id: 5, kvmemo: "副" }
  ], //库房包装单位
  orderTypeList: [
    { id: 1, kvmemo: "手术" },
    { id: 2, kvmemo: "护理" },
    { id: 3, kvmemo: "药品" },
    { id: 4, kvmemo: "医技" },
    { id: 5, kvmemo: "其他" }
  ], //医嘱类型
  defaultFreqList: [
    { id: 1, kvmemo: "不定时" },
    { id: 2, kvmemo: "持续性" },
    { id: 3, kvmemo: "隔日一次" },
    { id: 4, kvmemo: "每12小时一次" },
    { id: 5, kvmemo: "每8小时一次" }
  ], //默认频次
  outpDepartmentList: [
    { id: 1, kvmemo: "针灸科" },
    { id: 2, kvmemo: "病区药房" },
    { id: 3, kvmemo: "西药房" },
    { id: 4, kvmemo: "化验科" },
    { id: 5, kvmemo: "其他" }
  ], //门诊执行科室
  inexDepartmentList: [
    { id: 1, kvmemo: "针灸科" },
    { id: 2, kvmemo: "病区药房" },
    { id: 3, kvmemo: "西药房" },
    { id: 4, kvmemo: "康复科" },
    { id: 5, kvmemo: "其他" }
  ], //住院执行科室
  insurePropertList: [
    { id: 1, kvmemo: "西药费" },
    { id: 2, kvmemo: "中药费" }
  ], //收费类别
  dosageUnitList: [
    { id: 1, kvmemo: "ml" },
    { id: 2, kvmemo: "g" }
  ], //基本剂量单位
  drugPropertyList: [
    { id: 1, kvmemo: "普通药" },
    { id: 2, kvmemo: "麻醉药" },
    { id: 3, kvmemo: "毒性药" },
    { id: 4, kvmemo: "精神Ⅰ类" },
    { id: 5, kvmemo: "精神Ⅱ类" }
  ] // 药品属性
});
//新增弹窗-处方权限 下拉框数据
const permissionsList = ref([
  { id: 1, kvmemo: "不限" },
  { id: 2, kvmemo: "助理/师级" },
  { id: 3, kvmemo: "中级" },
  { id: 4, kvmemo: "副高" },
  { id: 5, kvmemo: "正高" }
]);
//校验必填信息
const formRefs: any = reactive([]);
const rules = reactive({
  itemCode: [
    {
      required: true,
      message: "请输入药品代码",
      trigger: "blur"
    }
  ],
  drugInputCode: [
    {
      required: true,
      message: "请输入输入码",
      trigger: "blur"
    }
  ],
  insurePropertyName: [
    {
      required: true,
      message: "请选择保险属性",
      trigger: "blur"
    }
  ],
  outpPackagUnitName: [
    {
      required: true,
      message: "请选择门诊包装单位",
      trigger: "blur"
    }
  ],
  outpPackageSpec: [
    {
      required: true,
      message: "请输入门诊包装规格",
      trigger: "blur"
    }
  ],
  stockPackagUnitName: [
    {
      required: true,
      message: "请选择库房包装单位",
      trigger: "blur"
    }
  ],
  stockPackageSpec: [
    {
      required: true,
      message: "请输入库房包装规格",
      trigger: "blur"
    }
  ],
  OutpPerformedDeptName: [
    {
      required: true,
      message: "请选择门诊执行科室",
      trigger: "blur"
    }
  ],
  InpPerformedDeptName: [
    {
      required: true,
      message: "请选择住院执行科室",
      trigger: "blur"
    }
  ],
  OrderClassname: [
    {
      required: true,
      message: "请选择医嘱类型",
      trigger: "blur"
    }
  ],
  FeeTypeMaskFlag: [
    {
      required: true,
      message: "请选择费别标志",
      trigger: "blur"
    }
  ],
  addRate: [
    {
      required: true,
      message: "请输入加成率",
      trigger: "blur"
    }
  ],
  skinTestMark: [
    {
      required: true,
      message: "请选择皮试标志",
      trigger: "blur"
    }
  ],
  drugPropertyId: [
    {
      required: true,
      message: "请选择药品属性",
      trigger: "blur"
    }
  ]
});

//////加载左边 药品目录树
const defaultProps = {
  children: "children",
  label: "catalogName"
};
interface Tree {
  label: string;
  children?: Tree[];
  id: string;
}
interface treeItem {
  id: string;
  orgaId: string;
  parentId: string;
  catalogCode: string;
  catalogInputCode: string;
  catalogName: string;
  chargeItemClassID: string;
  inputDate: string;
  operatorID: string;
  createUser: string;
  createDate: string;
  children: [] | treeItem;
}
const formatTree = (tempObj: any, parentId: any): treeItem[] => {
  const result: any[] = [];
  if (!tempObj[parentId]) {
    return result;
  }
  for (const t of tempObj[parentId]) {
    t.children = formatTree(tempObj, t.id); //递归获取children
    result.push(t);
  }
  return sortTree(result);
};
/**@description 排序 */
const sortTree = (data: any[]) => {
  data.sort((a: any, b: any) => {
    // 升序
    if (a.priceKindCode < b.priceKindCode) return -1;
    else if (a.priceKindCode == b.priceKindCode) return 0;
    else return 1;
  });

  data.forEach(item => {
    if (item.children && item.children.length) {
      sortTree(item.children);
    }
  });
  return data;
};
/**获取目录列表 */
const MediCatalogInfoList = () => {
  getMediCatalogInfoList("0")
    .then((res: any) => {
      if (res.code == "200") {
        const tempObj: any = {};
        let idsStr = "";
        for (let i = 0; i < res.data.length; i++) {
          const key = res.data[i].parentId;
          if (tempObj[key]) {
            tempObj[key].push(res.data[i]);
          } else {
            tempObj[key] = [];
            tempObj[key].push(res.data[i]);
          }
          idsStr += idsStr === "" ? res.data[i].id : "," + res.data[i].id;
        }
        for (const i in tempObj) {
          if (idsStr.indexOf(i) === -1) {
            treeDataleft.value = formatTree(tempObj, i);
            currentTree.value = treeDataleft.value[0];
            setTimeout(() => {
              refList.treeRef.setCurrentKey(currentTree.value?.id);
            }, 0);
          }
        }
        const allDrugs = {
          ...treeDataleft.value[0],
          id: "",
          catalogCode: "ALL",
          catalogName: "全部药品",
          children: []
        };
        treeDataleft.value.unshift(allDrugs);
        currentTree.value = allDrugs;
        DrugBalanceMaster(0); //查询药品信息
      } else {
        treeDataleft.value = [];
        tableDataright.value = [];
        ElMessageBox.alert("获取药品分类目录失败！");
      }
    })
    .catch((error: Error) => {
      // console.log(error)
    });
};

// 切换页数(分页查询)
const currentPage = ref(1); // 当前页码
const pageSize = ref(10); // 分页条数
const total = ref(0); // 总条数 读接口查询数量
// 切换页数
function handleCurrentChange1(val: number) {
  currentPage.value = val;
  // console.log('切换页数')
  //查询
  DrugBalanceMaster(0);
}
// 切换条数
function handleSizeChange(val: number) {
  pageSize.value = val;
  //查询
  DrugBalanceMaster(1);
}

const alltableRight = ref([]); //右侧表格的所有数据
/** 查询右边表格药品信息 */
const DrugBalanceMaster = (count: number) => {
  tableLoading.value = true;
  if (count == 1) {
    currentPage.value = 1;
  }
  getOrgaDrugInfoListWithPageFilter(
    String(pageSize.value),
    String(currentPage.value),
    currentTree.value.id,
    "0521" /** 机构id 全部采用0521 */,
    searchForm.drugname,
    searchForm.issfdj == false ? "false" : "true"
  )
    .then((res: any) => {
      if (res.code === "200") {
        tableDataright.value = res.data.queryOrgaDrugInfo;
        alltableRight.value = tableDataright.value;
        tableDatarightRow.value = tableDataright.value[0];
        if (tableDatarightRow.value == null || tableDatarightRow.value == "") {
          tableDataright.value = [];
          total.value = 0;
        } else {
          total.value = res.data.pages; //总数
          // 高亮第一行
          nextTick(() => {
            singleTableRef.value?.setCurrentRow(tableDataright.value[0]);
          });
          if (tableDataright.value[0].status == "冻结") {
            //解冻
            flagShowCameras.value = false;
          } else {
            //冻结
            flagShowCameras.value = true;
          }
        }

        tableLoading.value = false;
      } else {
        tableDataright.value = [];
        total.value = 0;

        tableLoading.value = false;
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    })
    .catch((e: any) => {
      tableDataright.value = [];

      tableLoading.value = false;

      throw e;
    });
};

const instituDialogVisible = ref(false); //弹窗是否打开
/**点击新增按钮 */
function addbtn(tit: string) {
  zujian.value = tit;
  if (currentTree.value) {
    instituDialogVisible.value = true;
    // console.log(formList.orderTypeList.length,'医嘱类型')
    if (formList.orderTypeList.length > 0) {
      //医嘱类型
      formList.orderTypeList.forEach((item: any) => {
        if (item.kvmemo == "药品") {
          formData.OrderClassId = item.id;
          formData.OrderClassname = item.kvmemo;
          // console.log(formData.OrderClassId,formData.OrderClassname,'医嘱类型')
        }
      });
    }
    formData.drugPrescauthId = "";
    formData.drugPrescauthName = permissionsList.value[0].kvmemo;
    if (formList.drugPropertyList.length > 0) {
      formData.drugPropertyId = formList.drugPropertyList.at(-1).id;
    }
    //恢复默认
    //empty()
  } else {
    //没选中左侧的分类
    ElMessageBox.alert("</el-icon><span>请选择机构药品分类！</span> ", "提示消息", {
      dangerouslyUseHTMLString: true,
      confirmButtonText: "确认"
    });
  }
}
/**点击修改按钮 */
function updatabtn(tit: string) {
  zujian.value = tit; //zujian 为新增或修改 // 修改时，药品代码不可更改
  if (tableDatarightRow.value) {
    //赋值信息 进行修改  匹配接口
    //updatedrugInfo();//
    updataDrugInfo({});
    instituDialogVisible.value = true; //弹窗是否打开
  } else {
    ElMessageBox.alert(
      "</el-icon><span>请选择需要修改的一条药品数据！</span> ",
      "提示消息",

      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认"
      }
    );
  }
}
//修改接口 之前获取药品信息匹配
const ppxgsj = ref([]);
/** 调接口获取当前选中行数据，赋值到-修改对话框 */
const updatedrugInfo = ({}) => {
  GetDrugInfoListForOrgaDrug2("0521", tableDatarightRow.value.drugId, tableDatarightRow.value.drugCode).then((res: any) => {
    if (res?.code == "200") {
      ppxgsj.value = res.data.DefineDrugInfoForOrgaDrug;
      //赋值给修改框
      if (res.data.DefineDrugInfoForOrgaDrug.length > 0) {
        updataDrugInfo(ppxgsj.value[0]); //赋值到-修改对话框
      }
    } else {
      ElMessage({ message: res.msg || res.message, type: "error" });
    }
  });
};
const sType = ref([]);
const drugId = ref(""); //药品id
const chargeItemId = ref(""); //收费项目id
const pdxgsjRow: any = ref("");
/** 赋值到-修改对话框 */
const updataDrugInfo = async (val: any) => {
  dateTime.value = new Date().toLocaleTimeString(); //获取当前时间
  // pdxgsjRow.value = val;
  // console.log(tableDatarightRow.value, '修改前匹配数据')
  formData.id = tableDatarightRow.value.id;
  drugId.value = tableDatarightRow.value.drugId;
  chargeItemId.value = tableDatarightRow.value.chargeItemId;
  formData.orgaID = tableDatarightRow.value.orgaId;
  formData.drugInputCode = tableDatarightRow.value.drugInputCode;
  formData.drugInputCode2 = tableDatarightRow.value.drugInputCode2;
  formData.drugInputCode3 = tableDatarightRow.value.drugInputCode3;
  formData.catalogId = tableDatarightRow.value.catalogId;
  formData.varietyId = tableDatarightRow.value?.varietyId;
  formData.commodityName = tableDatarightRow.value.commodityName;
  formData.drugSpecification = tableDatarightRow.value.drugSpecification;
  formData.priceUnitId = tableDatarightRow.value?.useUnitName; //用法包装单位
  formData.priceUnitName = tableDatarightRow.value.priceUnitName;
  formData.yldwName = formData.priceUnitName;
  formData.packageSpec = tableDatarightRow.value.packageSpec;
  formData.insureCode = tableDatarightRow.value.insureCode;
  formData.insurePropertyName = tableDatarightRow.value.insurePropertyName;
  formData.drugRoute = tableDatarightRow.value?.drugRoute;
  formData.approvalNumber = tableDatarightRow.value.approvalNumber;
  formData.registeredTrademark = tableDatarightRow.value.registeredTrademark;
  // formData.gmpMark = pdxgsjRow.value?.gmpMark;
  formData.octMark = tableDatarightRow.value.octMark;
  // formData.baseDrugMarkId = pdxgsjRow.value?.baseDrugMarkId;
  formData.baseDrugMarkName = tableDatarightRow.value.baseDrugMarkName;
  formData.serviceType = tableDatarightRow.value.serviceType;
  formData.isActive = tableDatarightRow.value.isActive;
  formData.stopDate = dateTime.value;
  formData.inputDate = dateTime.value;
  formData.operatorId = userInfo.value.id;
  formData.createUser = userInfo.value.id;
  formData.createDate = dateTime.value;
  formData.comment_ = tableDatarightRow.value.comment_;
  formData.drugFormId = tableDatarightRow.value.drugFormID;
  // formData.dosimetryUnitId = pdxgsjRow.value.dosimetryUnitId;
  // formData.dosimetryUnitName = pdxgsjRow.value.dosimetryUnitName;
  formData.varietyName = tableDatarightRow.value.varietyName;
  formData.varietyCode = tableDatarightRow.value.varietyCode;
  formData.skinTestMark = tableDatarightRow.value.skinTestMark;
  formData.outpPackagQuantity = tableDatarightRow.value.outpPackagQuantity;
  formData.outpPackagUnitId = tableDatarightRow.value.outpPackagUnitId;
  formData.outpPackagUnitName = tableDatarightRow.value.outpPackagUnitName;
  formData.stockPackagQuantity = tableDatarightRow.value.stockPackagQuantity;
  formData.stockPackagUnitId = tableDatarightRow.value.stockPackagUnitId;
  formData.stockPackagUnitName = tableDatarightRow.value.stockPackagUnitName;
  formData.outpPackageSpec = tableDatarightRow.value.outpPackageSpec;
  formData.stockPackageSpec = tableDatarightRow.value.stockPackageSpec;
  formData.chargeItemClassId = tableDatarightRow.value.chargeItemClassID;
  // formData.prescAuthId = pdxgsjRow.value?.prescAuthId;
  // formData.prescAuthName = pdxgsjRow.value?.prescAuthName;
  // formData.antibioticId = pdxgsjRow.value?.antibioticId;
  // formData.antibioticName = pdxgsjRow.value?.antibioticName;
  // formData.isSelected = pdxgsjRow.value?.isSelected;
  formData.state = tableDatarightRow.value.status;
  // formData.invoOutpId = pdxgsjRow.value?.invoOutpId;
  // formData.invoOutpCode = pdxgsjRow.value?.invoOutpCode;
  // formData.invoiInpId = pdxgsjRow.value?.invoiInpId;
  // formData.invoiInpCode = pdxgsjRow.value?.invoiInpCode;
  formData.invoiInpName = tableDatarightRow.value.invoiInpName;
  formData.drugFrequency = tableDatarightRow.value.drugFrequency;
  formData.drugFrequencyname = tableDatarightRow.value.drugFrequency;
  formData.drugDosage = tableDatarightRow.value.drugDosage;
  // formData.useUnitName = pdxgsjRow.value?.useUnitName; //用法包装单位
  formData.OutpPerformedDeptId = tableDatarightRow.value.outpPerformedDeptId; //门诊执行科室id
  formData.InpPerformedDeptId = tableDatarightRow.value.inpPerformedDeptId; //住院执行科室id
  formData.addRate = tableDatarightRow.value.addRate; //加成率
  formData.OrderClassId = tableDatarightRow.value.orderClassId; //医嘱类型id
  formData.OrderClassname = tableDatarightRow.value.orderClassname;
  formData.FeeTypeMaskFlag = tableDatarightRow.value.feeTypeMaskFlag; //费别计费标志
  formData.skinTestTypeId = tableDatarightRow.value.skinTestTypeId; //皮试方法id
  formData.skinTestTypeName =
    tableDatarightRow.value.skinTestTypeId == "A8AC2C6C-0062-4B42-9888-96FA0FAD7001"
      ? "其他"
      : tableDatarightRow.value.skinTestTypeName; //皮试方法  根据皮试id判断，如果id是其他的id 那就赋值为其他
  formData.TestTypeName =
    tableDatarightRow.value.skinTestTypeId == "A8AC2C6C-0062-4B42-9888-96FA0FAD7001"
      ? tableDatarightRow.value.skinTestTypeName
      : "";
  formData.isDl = tableDatarightRow.value.isDL; //是否带量
  formData.isGpo = tableDatarightRow.value.isGpo; //是否GPO
  formData.mzsy = tableDatarightRow.value.mzsy; //门诊使用
  formData.zysy = tableDatarightRow.value.zysy; //住院使用
  formData.jzsy = tableDatarightRow.value.jzsy; //急诊使用
  formData.CountryCode = tableDatarightRow.value.countryCode; //国家码
  // formData.ypid = pdxgsjRow.value?.ypId; //YPID
  formData.itemCode = tableDatarightRow.value.drugCode; //项目代码/药品代码
  formData.itemName = tableDatarightRow.value.drugName; //项目名称/药品名称
  formData.DrugFormName = tableDatarightRow.value.drugFormName; //剂型
  formData.Origin = tableDatarightRow.value.origin; //产地
  // formData.InsurePropertyID = pdxgsjRow.value?.insurePropertyId; //保险属性
  formData.insurePropertyName = tableDatarightRow.value.insurePropertyName;
  formData.itemBasicUnit = tableDatarightRow.value.priceUnitName; //基本单位
  formData.BasicDosimetry = "1"; //基本包装量
  formData.jbjl = tableDatarightRow.value.basicDosimetry;
  formData.itemMemo = tableDatarightRow.value.drugMemo; //项目备注
  formData.insureRate = tableDatarightRow.value.insureRate; //医保单位转换系数
  formData.purchaseRate = tableDatarightRow.value.purchaseRate; //采购转换系数
  formData.OutpPerformedDeptName = tableDatarightRow.value.outpPerformedDeptName; //门诊执行科室
  formData.InpPerformedDeptName = tableDatarightRow.value.inpPerformedDeptName; //住院执行科室

  // formData.drugPrescauthId = pdxgsjRow.value?.drugPrescauthId; //药品处方权限id
  // formData.drugPrescauthName = pdxgsjRow.value?.drugPrescauthName; //药品处方权限名称

  // formData.drugPropertyId = pdxgsjRow.value?.drugPropertyId; //药品属性

  //判断性别限定 0-不限，1-男，2-女
  // if (pdxgsjRow.value.sexLimit == "0") {
  //   formData.sexLimit = "0";
  // } else if (pdxgsjRow.value.sexLimit == "1") {
  //   formData.sexLimit = "1";
  // } else if (pdxgsjRow.value.sexLimit == "2") {
  //   formData.sexLimit = "2";
  // }
  //判断云药房标志  0-否，1-是
  // if (pdxgsjRow.value.netDrugstore == "0") {
  //   formData.netDrugstore = false;
  // } else if (pdxgsjRow.value.netDrugstore == "1") {
  //   formData.netDrugstore = true;
  // }
  // formData.identifyCode = pdxgsjRow.value.identifyCode; //药品标识码
  //判断带量/gpo
  if (tableDatarightRow.value.isDL == "是") {
    formData.isDl = true;
  } else {
    formData.isDl = false;
  }
  if (tableDatarightRow.value.isGpo == "是") {
    formData.isGpo = true;
  } else {
    formData.isGpo = false;
  }
  //对门诊/住院/急诊 保存的字符串转换数组判断
  if (tableDatarightRow.value.serviceType != "" && tableDatarightRow.value.serviceType != null) {
    sType.value = tableDatarightRow.value.serviceType.split("");
    if (sType.value[0] == "1") {
      formData.mzsy = true; //门诊使用
    } else {
      formData.mzsy = false; //门诊使用
    }

    if (sType.value[1] == "1") {
      formData.zysy = true; //住院使用
    } else {
      formData.zysy = false; //住院使用
    }

    if (sType.value[2] == "1") {
      formData.jzsy = true; //急诊使用
    } else {
      formData.jzsy = false; //急诊使用
    }
  } else {
    formData.mzsy = false; //门诊使用
    formData.zysy = false; //住院使用
    formData.jzsy = false; //急诊使用
  }
  //门诊执行科室
  formList.outpDepartmentList.forEach((item: any) => {
    //console.log(formData.OutpPerformedDeptId, item, '门诊执行科室')
    if ((formData.OutpPerformedDeptName = item.deptName)) {
      formData.OutpPerformedDeptId == item.id;
    }
  });
  //住院执行科室
  formList.inexDepartmentList.forEach((item: any) => {
    if ((formData.InpPerformedDeptName = item.deptName)) {
      formData.InpPerformedDeptId == item.id;
    }
  });

  //保险属性
  formList.inAbutesList.forEach((item: any) => {
    if ((formData.insurePropertyName = item.kvmemo)) {
      // console.log(formData.InsurePropertyId, item, '保险属性')
      formData.InsurePropertyID == item.id;
    }
  });
  //用法包装单位
  formList.usPbnitList.forEach((item: any) => {
    //console.log(formData.priceUnitId, '修改前匹配用法包装单位')
    if (formData.priceUnitId == item.id) {
      formData.priceUnitName = item.kvmemo;
      formData.useUnitName = item.id;
      formData.yldwName = formData.priceUnitName;
      // console.log(formData.priceUnitId, item, '修改前匹配用法包装单位')
    }
  });
  //门诊包装单位
  formList.inpPerformedDeptList.forEach((item: any) => {
    if (formData.outpPackagUnitId == item.id) {
      formData.outpPackagUnitName = item.kvmemo;
    }
  });

  //库房包装单位
  formList.stockPackagList.forEach((item: any) => {
    if (formData.stockPackagUnitId == item.id) {
      formData.stockPackagUnitName = item.kvmemo;
    }
  });

  //收费类别
  formList.insurePropertList.forEach((item: any) => {
    if (formData.chargeItemClassId == item.id) {
      // console.log(formData.chargeItemClassId, item, '收费类别')
      formData.itemClassName = item.itemClassName;
    }
  });
  //默认频次
  formList.defaultFreqList.forEach((item: any) => {
    if (formData.drugFrequency == item.id) {
      formData.drugFrequencyname = item.kvmemo;
    }
  });
  //医嘱类型
  formList.orderTypeList.forEach((item: any) => {
    if (formData.OrderClassId == item.id) {
      formData.OrderClassname = item.kvmemo;
      // console.log(formData.OrderClassname, '医嘱类型')
    }
  });

  //剂量单位
  formList.dosageUnitList.forEach((item: any) => {
    if (formData.dosimetryUnitId == item.id) {
      formData.dosimetryUnitName = item.kvmemo;
      // console.log( formData.dosimetryUnitName, '剂量单位')
    }
  });
};
/** 库存弹窗-是否打开 */
const drugInfovisible = ref(false);
/** 点击库存按钮 */
function kcxxbtn() {
  if (tableDatarightRow.value) {
    drugStoreQuery(); //调接口查询药品库存信息
    drugInfovisible.value = true;
  } else {
    ElMessageBox.alert(
      "</el-icon><span>请选择需要查看的一条药品数据！</span> ",
      "提示消息",

      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认",
        type: "warning"
      }
    );
  }
}

const drugInfoKCListRow: any = ref(""); //库存弹窗-当前行
const drugInfoKCListRef: any = ref<InstanceType<typeof ElTable>>(); //库存弹窗 - ref
const hdrugInfoKCChange = (val: any) => {
  drugInfoKCListRow.value = val;
  // console.log(drugInfoKCListRow.value, '获取弹窗页面数据')
};
/** 调接口查询药品库存信息 */
const drugStoreQuery = () => {
  getDrugStoreQuery({
    drugId: tableDatarightRow.value.id //药品id
  }).then((res: any) => {
    if (res.code === "200") {
      drugInfoKCList.value = res.data;
      drugInfoKCListRow.value = tableDataright.value[0];
      if (drugInfoKCListRow.value == null || drugInfoKCListRow.value == "") {
        drugInfoKCList.value = [];
      } else {
        // 高亮第一行
        nextTick(() => {
          drugInfoKCListRef.value.setCurrentRow(drugInfoKCList.value[0]);
        });
      }
    } else {
      drugInfoKCList.value = [];
      ElMessage({ message: res.msg || res.message, type: "error" });
    }
  });
};

/** 点击 冻结/解冻 按钮 */
function djypbtn() {
  //判断是否选中数据
  if (tableDatarightRow.value == "" || tableDatarightRow.value == null) {
    ElMessageBox.alert(
      "</el-icon><span>请选择一行数据!</span> ",
      "提示消息",

      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认",
        type: "warning"
      }
    );
  } else {
    DrugStoreDJ();
  }
}
const jdzt = ref(""); //当前操作名称，“冻结"/”解冻“
/** 调 冻结/解冻 接口 */
const DrugStoreDJ = () => {
  if (tableDatarightRow.value.status == "冻结") {
    jdzt.value = "解冻";
  } else {
    jdzt.value = "冻结";
  }
  getinsertDrugStoreDJ(
    tableDatarightRow.value.id, //id
    "0521",
    tableDatarightRow.value.drugId, //药品id
    jdzt.value,
    tableDatarightRow.value.drugName,
    tableDatarightRow.value.drugCode,
    tableDatarightRow.value.deptID,
    tableDatarightRow.value.deptCode,
    tableDatarightRow.value.deptName,
    userInfo.value.name //登录用户姓名
  ).then((res: any) => {
    if (res?.code == "200") {
      ElMessage({ message: jdzt.value + res.msg, type: "success" });
      DrugBalanceMaster(0); //查询药品信息
    } else {
      ElMessage({ message: res.msg || res.message, type: "error" });
      DrugBalanceMaster(0); //查询药品信息
    }
  });
};

///////////////////////
////删除 按钮  接口/////
//  1. 先调用检查是否有库存 ，2. 删除/启用药品
/** 点击删除按钮。  先检查是否有库存 */
const deleteOrgaDrugInfo = () => {
  if (tableDatarightRow.value) {
    if (tableDatarightRow.value.isActive == "停用") {
      ElMessageBox.alert(
        "</el-icon><span>药品状态已经是停用！</span> ",
        "提示消息",

        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "确认"
        }
      );
    } else {
      //检查是否有库存
      deleteOrgaDrugCheckStock(
        "0521",
        tableDatarightRow.value.drugId //药品id
      ).then((res: any) => {
        if (res?.code == "200") {
          if (res.data != "" && res.data != null) {
            ElMessageBox.confirm(`${res.data}!是否要删除?`, "提示消息", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(() => {
              //删除/启用药品信息
              updatedrugInfoIsActive();
            });
          } else {
            // ElMessageBox.confirm(
            //   `${res.data}库存为空，不能删除!`,
            //   '提示消息',
            //   {
            //     confirmButtonText: '确定',
            //     cancelButtonText: '取消',
            //     type: 'warning',
            //   }
            // )
            //删除/启用药品信息
            updatedrugInfoIsActive();
          }
        }
      });
    }
  } else {
    ElMessageBox.alert(
      "</el-icon><span>请选择需要删除的一条药品数据！</span> ",
      "提示消息",

      {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认",
        type: "warning"
      }
    );
  }
};

// 2.删除/启用药品信息
const updatedrugInfoIsActive = async () => {
  dateTime.value = new Date().toLocaleTimeString(); //获取当前时间
  updateOrgaDrugInfoIsActive({
    id: tableDatarightRow.value.id, //id
    orgaId: "0521",
    chargeItemID: tableDatarightRow.value.chargeItemId,
    isActive: "停用",
    inputDate: dateTime.value,
    operatorId: userInfo.value.id
  }).then((res: any) => {
    if (res?.code == "200") {
      ElMessage({ message: "删除成功！", type: "success" });
      DrugBalanceMaster(0); //查询药品信息
    }
  });
};
const tableDatadcxml: any = ref([]); //导出的列表
/**点击导出按钮 */
const exportXml = throttle(() => {
  tableDatadcxml.value = [];
  if (tableDataright.value.length > 0) {
    getOrgaDrugInfoListWithPageFilter(
      "16777216",
      "1",
      currentTree.value.id,
      "0521",
      searchForm.drugname,
      searchForm.issfdj == false ? "false" : "true"
    )
      .then((res: any) => {
        if (res.code === "200") {
          tableDatadcxml.value = res.data.queryOrgaDrugInfo;
          Xml();
        } else {
          tableDatadcxml.value = [];
          Xml();
        }
      })
      .catch((e: any) => {
        tableDatadcxml.value = [];
        Xml();
        throw e;
      });
  } else {
    tableDatadcxml.value = [];
    Xml();
  }
}, 3000);
/** 导出的数据集excel(主页面) */
function Xml() {
  const headersArr = [
    "药品代码",
    "医保编码",
    "国家码",
    "药品名称",
    "产地",
    "门诊包装规格",
    "库房包装规格",
    "品种代码",
    "品种名称",
    "剂型",
    "门诊发票分类",
    "住院发票分类",
    "基药标志",
    "商品名",
    "计价单位",
    "保险属性",
    "批准文号",
    "基本剂量",
    "门诊包装量",
    "门诊包装单位",
    "库房包装量",
    "库房包装单位",
    "计价包装规格",
    "加成率",
    "是否带量",
    "药品备注",
    "是否在用"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    item.drugCode,
    item.insureCode,
    item.countryCode,
    item.drugName,
    item.origin,
    item.outpPackageSpec,
    item.stockPackageSpec,
    item.varietyCode,
    item.varietyName,
    item.drugFormName,
    item.invoOutpName,
    item.invoiInpName,
    item.baseDrugMarkName,
    item.commodityName,
    item.priceUnitName,
    item.insurePropertyName,
    item.approvalNumber,
    item.basicDosimetry,
    item.outpPackagQuantity,
    item.outpPackagUnitName,
    item.stockPackagQuantity,
    item.stockPackagUnitName,
    item.packageSpec,
    item.addRate,
    item.isDL,
    item.drugMemo,
    item.isActive
  ]);
  exportDrugExpiryTable(headersArr, data, "浦东测试" + "机构药品目录");
}

const formRef: any = ref<FormInstance>();
/** 关闭 新增/修改 对话框时， 清空表格 */
const resetForm = (formName: FormInstance | undefined) => {
  if (!formName) return;
  formName.resetFields();
  empty(); //清空表格
};
/** 清空-新增/修改对话框-表格 */
const empty = () => {
  formData.id = "";
  formData.orgaID = "";
  formData.drugInputCode = "";
  formData.drugInputCode2 = "";
  formData.drugInputCode3 = "";
  formData.catalogId = "";
  formData.varietyId = "";
  formData.commodityName = "";
  formData.drugSpecification = "";
  formData.priceUnitId = "";
  formData.priceUnitName = "";
  formData.yldwName = "";
  formData.packageSpec = "";
  formData.insureCode = "";
  formData.insurePropertyName = "";
  formData.drugRoute = "";
  formData.approvalNumber = "";
  formData.registeredTrademark = "";
  formData.gmpMark = "";
  formData.octMark = "";
  formData.baseDrugMarkId = "";
  formData.baseDrugMarkName = "";
  formData.serviceType = "";
  formData.isActive = "";
  formData.stopDate = "";
  formData.inputDate = "";
  formData.operatorId = "";
  formData.createUser = "";
  formData.createDate = "";
  formData.comment_ = "";
  formData.drugFormId = "";
  formData.dosimetryUnitId = "";
  formData.dosimetryUnitName = "";
  formData.varietyName = "";
  formData.varietyCode = "";
  formData.skinTestMark = "否";
  formData.outpPackagQuantity = 1;
  formData.outpPackagUnitId = "";
  formData.outpPackagUnitName = "";
  formData.stockPackagQuantity = 1;
  formData.stockPackagUnitId = "";
  formData.stockPackagUnitName = "";
  formData.outpPackageSpec = "";
  formData.stockPackageSpec = "";
  formData.chargeItemClassId = "";
  formData.prescAuthId = "";
  formData.prescAuthName = "";
  formData.antibioticId = "";
  formData.antibioticName = "";
  formData.isSelected = "";
  formData.state = "";
  formData.invoOutpId = "";
  formData.invoOutpCode = "";
  formData.invooutpname = "";
  formData.invoiInpId = "";
  formData.invoiInpCode = "";
  formData.invoiInpName = "";
  formData.drugFrequency = "";
  formData.drugFrequencyname = "";
  formData.drugDosage = "";
  formData.useUnitName = "";
  formData.OutpPerformedDeptId = ""; //门诊执行科室id
  formData.InpPerformedDeptId = ""; //住院执行科室id
  formData.OutpPerformedDeptName = ""; //门诊执行科室
  formData.InpPerformedDeptName = ""; //住院执行科室

  formData.addRate = "0"; //加成率
  formData.OrderClassId = ""; //医嘱类型id
  formData.OrderClassname = "";
  formData.FeeTypeMaskFlag = "不按费别计费"; //费别计费标志
  formData.skinTestTypeId = "";
  formData.skinTestTypeName = ""; //皮试方法
  formData.TestTypeName = "";
  formData.isDl = false; //是否带量
  formData.isGpo = false; //是否GPO
  formData.mzsy = true; //门诊使用
  formData.zysy = true; //住院使用
  formData.jzsy = true; //急诊使用
  formData.CountryCode = ""; //国家码
  formData.ypid = ""; //YPID
  formData.itemCode = ""; //项目代码/药品代码
  formData.itemName = ""; //项目名称/药品名称
  formData.DrugFormName = ""; //剂型
  formData.Origin = ""; //产地
  formData.InsurePropertyID = ""; //保险属性
  formData.itemBasicUnit = ""; //基本单位
  formData.BasicDosimetry = ""; //基本包装量
  formData.jbjl = "";
  formData.itemMemo = ""; //项目备注
  formData.insureRate = ""; //医保单位转换系数
  formData.purchaseRate = "1"; //采购转换系数
  formData.drugPrescauthId = ""; //药品处方权限id
  formData.drugPrescauthName = ""; //药品处方权限名称
  formData.sexLimit = "0"; //性别限定 0-不限，1-男，2-女
  //判断云药房标志  0-否，1-是
  formData.netDrugstore = false;
  formData.identifyCode = ""; //药品标识码
  zujian.value = "";
  instituDialogVisible.value = false;
};
/** 移除中文字符 */
const removeChineseCharacter = (value: string) => value.replace(/[\u4E00-\u9FA5]/g, "").toUpperCase();
/** 新增/修改弹窗 皮试方式改变时： */
const PsInfoListChange = (val: any) => {
  formData.skinTestTypeName = val?.kvmemo;
  formData.skinTestTypeId = val?.id;
  // console.log(formData.skinTestTypeName, formData.skinTestTypeId, '皮试方式')
};
//新增验证项目药品信息是否机构已存
const mzsyvalue = ref("1"); //门诊使用
const zysyvalue = ref("1"); //住院使用
const jzsyvalue = ref("1"); //急诊使用
const isdlvalue = ref(""); //是否带量
const isgpovalue = ref(""); //是否gpo
const isybzvalue = ref(""); //是否云药房标志
/** 新增/修改弹窗 点击确认时： */
const verify = async (formEl: FormInstance | undefined) => {
  if (formData.mzsy == true) {
    mzsyvalue.value = "1";
  } else {
    mzsyvalue.value = "0";
  }
  if (formData.zysy == true) {
    zysyvalue.value = "1";
  } else {
    zysyvalue.value = "0";
  }
  if (formData.jzsy == true) {
    jzsyvalue.value = "1";
  } else {
    jzsyvalue.value = "0";
  }
  const serType = mzsyvalue.value + zysyvalue.value + jzsyvalue.value;
  formData.serviceType = serType;
  if (formData.isDl == true) {
    isdlvalue.value = "是";
  } else {
    isdlvalue.value = "否";
  }
  if (formData.isGpo == true) {
    isgpovalue.value = "是";
  } else {
    isgpovalue.value = "否";
  }
  //云药房标志 0-否，1-是
  if (formData.netDrugstore == true) {
    isybzvalue.value = "1";
  } else {
    isybzvalue.value = "0";
  }

  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      if (zujian.value == "新增") {
        //新增机构药品信息
        addOrgaDrugInfo(formData);
      } else {
        if (zujian.value == "修改") {
          //修改机构药品信息
          UpdataOrgaDrugInfo(formData);
        }
      }
    } else {
      // console.log('error submit!', fields)
    }
  });
};
/** 新增药品信息 */
const addOrgaDrugInfo = async (formData: any) => {
  dateTime.value = new Date().toLocaleTimeString();
  addOrgaDrugInfoAndChargeItemPrice(
    {
      orgaId: "0521",
      drugCode: formData.itemCode,
      drugName: formData.itemName,
      drugInputCode: formData.drugInputCode,
      drugInputCode2: formData.drugInputCode2,
      drugInputCode3: formData.drugInputCode3,
      catalogID: currentTree.value.id, //分类id
      varietyID: "",
      commodityName: formData.commodityName, /// 商品名称
      drugSpecification: "", // 包装规格(说明书）
      basicDosimetry: formData.basicDosimetry, // 基本剂量
      priceUnitID: formData.priceUnitID, // 计价单位ID
      priceUnitName: formData.priceUnitName, // 计价单位名称
      packageSpec: formData.packageSpec, // 计价包装规格
      insureCode: formData.insureCode, // 医保编码
      insurePropertyID: formData.insurePropertyID, // 医保属性ID
      insurePropertyName: formData.insurePropertyName, // 医保属性名称
      drugRouteID: formData.drugRouteID, // 用药途径ID
      drugRouteName: formData.drugRouteName, // 用药途径名称
      dddValue: formData.dddValue, // DDD值
      origin: formData.origin,
      approvalNumber: formData.approvalNumber,
      registeredTrademark: formData.registeredTrademark,
      gmpMark: formData.gmpMark,
      octMark: formData.octMark,
      skinTestMark: formData.skinTestMark,
      baseDrugMarkID: formData.baseDrugMarkID,
      baseDrugMarkName: formData.baseDrugMarkName,
      serviceType: formData.serviceType,
      reckID: formData.reckID,
      reckCode: formData.reckCode,
      reckName: formData.reckName,
      invoOutpID: formData.invoOutpID,
      invoOutpCode: formData.invoOutpCode,
      invoOutpName: formData.invoOutpName,
      invoiInpID: formData.invoiInpID,
      invoiInpCode: formData.invoiInpCode,
      invoiInpName: formData.invoiInpName,
      priceKindID: formData.priceKindID,
      priceKindCode: formData.priceKindCode,
      priceKindName: formData.priceKindName,
      caseClassID: formData.caseClassID,
      caseClassCode: formData.caseClassCode,
      caseClassName: formData.caseClassName,
      isActive: formData.isActive,
      stopDate: formData.stopDate,
      inputDate: formData.inputDate,
      operatorID: userInfo.value.id,
      createUser: userInfo.value.username,
      createDate: "",
      comment_: formData.itemMemo,
      outpPackagQuantity: formData.outpPackagQuantity,
      outpPackagUnitID: formData.outpPackagUnitID,
      outpPackagUnitName: formData.outpPackagUnitName,
      stockPackagQuantity: formData.stockPackagQuantity,
      stockPackagUnitID: formData.stockPackagUnitID,
      stockPackagUnitName: formData.stockPackagUnitName,
      outpPackageSpec: formData.outpPackageSpec,
      stockPackageSpec: formData.stockPackageSpec,
      chargeItemClassID: formData.chargeItemClassID,
      drugFormID: formData.drugFormID,
      drugFormName: formData.drugFormName,
      dosimetryUnitID: formData.dosimetryUnitID,
      dosimetryUnitName: formData.dosimetryUnitName, // 剂量单位名称
      antibioticID: formData.antibioticID,
      antibioticName: formData.antibioticName,
      drugFrequency: formData.drugFrequencyname, // 默认频次
      drugDosage: formData.drugDosage,
      supplierid: formData.supplierid,
      countryCode: formData.CountryCode,
      status: "解冻"
    }
    // {
    //   id: "",
    //   regionChargeitemId: "", //全区收费项目id
    //   orgaId: "0521", //机构id
    //   itemClassId: formData.chargeItemClassId, //收费项目类别id
    //   itemCode: formData.itemCode, //项目代码
    //   itemybCode: formData.insureCode, //医保项目代码
    //   itemOtherCode: "", //项目其他代码
    //   inputCode: formData.drugInputCode, //输入码
    //   inputCode2: formData.drugInputCode2, //输入码2
    //   inputCode3: formData.drugInputCode3, //输入码3
    //   itemName: formData.itemName, //项目名称
    //   itemBasicSpec: formData.packageSpec, //基本包装规格
    //   itemOutpSpec: formData.outpPackageSpec, //门诊包装规格
    //   itemInpSpec: formData.packageSpec, //住院包装规格
    //   itemBasicUnit: formData.priceUnitName, //基本单位
    //   itemOutpUnit: formData.outpPackagUnitName, //门诊单位
    //   itemInpUnit: formData.priceUnitName, //住院单位
    //   outpTransFactor: formData.outpPackagQuantity, //门诊包装因子
    //   inpTransFactor: "1", //住院包装因子
    //   itemPrice: "0", //单价
    //   itemMaxPrice: "0", //最大价
    //   itemNoPrice: "0", //不可报价
    //   itemPreferPrice: "0", //优惠价
    //   outpPerformedDeptId: formData.OutpPerformedDeptId, //门诊执行科室id
    //   inpPerformedDeptId: formData.InpPerformedDeptId, //住院执行科室id
    //   insurePropertId: formData.InsurePropertyID, //保险属性id
    //   drugOritemFlag: "药品", //药品/项目标志
    //   orderClassId: formData.OrderClassId, //医嘱类型id
    //   orderName: "",
    //   itemBusiClassId: "", //业务类别id
    //   feeTypeMaskFlag: formData.FeeTypeMaskFlag, //费别计费标志
    //   ybCatalogClassId: "", //医保目录id
    //   ybBrandName: "", //医用材料品牌
    //   ybCertifyNo: "", //注册证号
    //   itemMemo: formData.itemMemo, //项目备注
    //   inputLimit: "0", //限制用量
    //   serviceType: formData.serviceType, //服务使用对象
    //   isActive: formData.isActive, //是否在用
    //   stopDate: dateTime.value, //停用日期
    //   createUser: formData.createUser, //创建人
    //   createDate: dateTime.value, //创建日期
    //   comment_: formData.comment_, //备注
    //   isVariable: "", //是否变价
    //   insureRate: formData.insureRate, //医保单位转换系数
    //   countryCode: formData.CountryCode, //国家码
    //   purchaseRate: formData.purchaseRate //采购转换系数
    // },
    // IsYbItemCodeCheck.value
  ).then((res: any) => {
    if (res?.code == "200") {
      instituDialogVisible.value = false;

      ElMessage({ message: "添加成功！", type: "success" });
      DrugBalanceMaster(0); //查询药品信息
    } else {
      ElMessage({ message: res.msg, type: "error" });

      DrugBalanceMaster(0); //查询药品信息
    }
  });
};
//////////////////////
/** 修改药品信息 */
const UpdataOrgaDrugInfo = async (formData: any) => {
  dateTime.value = new Date().toLocaleTimeString(); //获取当前时间
  UpdataOrgaDrugInfoAndChargeItemPrice({
    id: formData.id, //药品id
    orgaId: "0521",
    drugCode: formData.itemCode,
    drugName: formData.itemName,
    drugInputCode: formData.drugInputCode,
    drugInputCode2: formData.drugInputCode2,
    drugInputCode3: formData.drugInputCode3,
    catalogID: currentTree.value.id, //分类id
    varietyID: "",
    commodityName: formData.commodityName, /// 商品名称
    drugSpecification: "", // 包装规格(说明书）
    basicDosimetry: formData.basicDosimetry, // 基本剂量
    priceUnitID: formData.priceUnitID, // 计价单位ID
    priceUnitName: formData.priceUnitName, // 计价单位名称
    packageSpec: formData.packageSpec, // 计价包装规格
    insureCode: formData.insureCode, // 医保编码
    insurePropertyID: formData.insurePropertyID, // 医保属性ID
    insurePropertyName: formData.insurePropertyName, // 医保属性名称
    drugRouteID: formData.drugRouteID, // 用药途径ID
    drugRouteName: formData.drugRouteName, // 用药途径名称
    dddValue: formData.dddValue, // DDD值
    origin: formData.origin,
    approvalNumber: formData.approvalNumber,
    registeredTrademark: formData.registeredTrademark,
    gmpMark: formData.gmpMark,
    octMark: formData.octMark,
    skinTestMark: formData.skinTestMark,
    baseDrugMarkID: formData.baseDrugMarkID,
    baseDrugMarkName: formData.baseDrugMarkName,
    serviceType: formData.serviceType,
    reckID: formData.reckID,
    reckCode: formData.reckCode,
    reckName: formData.reckName,
    invoOutpID: formData.invoOutpID,
    invoOutpCode: formData.invoOutpCode,
    invoOutpName: formData.invoOutpName,
    invoiInpID: formData.invoiInpID,
    invoiInpCode: formData.invoiInpCode,
    invoiInpName: formData.invoiInpName,
    priceKindID: formData.priceKindID,
    priceKindCode: formData.priceKindCode,
    priceKindName: formData.priceKindName,
    caseClassID: formData.caseClassID,
    caseClassCode: formData.caseClassCode,
    caseClassName: formData.caseClassName,
    isActive: formData.isActive,
    stopDate: formData.stopDate,
    inputDate: formData.inputDate,
    operatorID: userInfo.value.id,
    createUser: userInfo.value.username,
    createDate: "",
    comment_: formData.itemMemo,
    outpPackagQuantity: formData.outpPackagQuantity,
    outpPackagUnitID: formData.outpPackagUnitID,
    outpPackagUnitName: formData.outpPackagUnitName,
    stockPackagQuantity: formData.stockPackagQuantity,
    stockPackagUnitID: formData.stockPackagUnitID,
    stockPackagUnitName: formData.stockPackagUnitName,
    outpPackageSpec: formData.outpPackageSpec,
    stockPackageSpec: formData.stockPackageSpec,
    chargeItemClassID: formData.chargeItemClassID,
    drugFormID: formData.drugFormID,
    drugFormName: formData.drugFormName,
    dosimetryUnitID: formData.dosimetryUnitID,
    dosimetryUnitName: formData.dosimetryUnitName, // 剂量单位名称
    antibioticID: formData.antibioticID,
    antibioticName: formData.antibioticName,
    drugFrequency: formData.drugFrequencyname, // 默认频次
    drugDosage: formData.drugDosage,
    supplierid: formData.supplierid,
    countryCode: formData.CountryCode,
    status: tableDatarightRow.value.status
    // orgaId: "0521", //机构id
    // drugId: drugId.value, //药品id
    // outpPerformedDeptId: formData.OutpPerformedDeptId, //门诊执行科室id
    // inpPerformedDeptId: formData.InpPerformedDeptId, //住院执行科室id
    // isActive: formData.isActive, //是否在用
    // outpPackagQuantity: formData.outpPackagQuantity, //门诊包装量
    // outpPackagUnitId: formData.outpPackagUnitId, //门诊包装单位id
    // outpPackagUnitName: formData.outpPackagUnitName, //门诊包装单位名称
    // bigPackagQuantity: "", //大包装量
    // bigPackagUnitId: "", //大包装单位id
    // bigPackagUnitName: "", //大包装单位名称
    // stockPackagQuantity: formData.stockPackagQuantity, //库房包装量
    // stockPackagUnitId: formData.stockPackagUnitId, //库房包装单位id
    // stockPackagUnitName: formData.stockPackagUnitName, //库房包装单位名称
    // packageSpec: formData.packageSpec, //计价包装规格
    // outpPackageSpec: formData.outpPackageSpec, //门诊包装规格
    // bigPackageSpec: "", //大包装规格
    // stockPackageSpec: formData.stockPackageSpec, //库房包装规格
    // addRate: formData.addRate, //加成率
    // orderClassId: formData.OrderClassId, //医嘱类型id
    // feeTypeMaskFlag: formData.FeeTypeMaskFlag, //费别计费标志
    // drugMemo: formData.itemMemo, //药品备注
    // purchasePrice: "", //批发价
    // retailPrice: "", //零售价
    // stopDate: dateTime.value, //停用日期
    // inputDate: dateTime.value, //修改日期
    // operatorId: formData.operatorId, //修改人id
    // createUser: formData.createUser, //创建人id
    // createDate: dateTime.value, //创建时间
    // comment_: formData.comment_, //备注
    // serviceType: formData.serviceType, //服务使用对象
    // chargeItemId: chargeItemId.value, //收费项目id
    // chargeItemClassId: formData.chargeItemClassId, //收费项目类别id
    // skinTestMark: formData.skinTestMark, //皮试标志
    // skinTestTypeId: formData.skinTestTypeId,
    // skinTestTypeName: formData.skinTestTypeName == "其他" ? formData.TestTypeName : formData.skinTestTypeName,
    // drugFrequency: formData.drugFrequency, //默认频次
    // drugDosage: formData.drugDosage, //默认用量
    // useUnitName: formData.yldwName, //用法单位
    // isDl: isdlvalue.value, //是否带量
    // isGpo: isgpovalue.value, //是否GPO
    // decoctionMethod: "", //煎煮法
    // negotiatedPrice: "", //议价价格
    // countryCode: formData.CountryCode, //国家码
    // ypid: formData.ypid, //YPID
    // prescauthId: formData.drugPrescauthId, //处方权限id
    // prescauthName: formData.drugPrescauthName, //处方权限名称
    // sexLimit: formData.sexLimit, //性别限定 0-不限，1-男，2-女
    // netDrugstore: isybzvalue.value, //云药房标志 0-否，1-是
    // identifyCode: formData.identifyCode, //药品标识码
    // drugPropertyId: formData.drugPropertyId, //药品属性id
    // drugPropertyName: formList.drugPropertyList.find((i: any) => i.id === formData.drugPropertyId).kvmemo //药品属性名称
  }).then((res: any) => {
    if (res?.code == "200") {
      instituDialogVisible.value = false;

      ElMessage({ message: res.msg || res.message, type: "success" });

      DrugBalanceMaster(0); //查询药品信息
    } else {
      ElMessage({ message: res.msg || res.message, type: "error" });

      DrugBalanceMaster(0); //查询药品信息
    }
  });
};
/** 关闭库存弹窗 */
const close = () => {
  drugInfovisible.value = false;
};
onMounted(() => {
  MediCatalogInfoList(); //加载药品目录树
});
</script>

<template>
  <div class="main" tabindex="-1">
    <el-card class="top-form" shadow="never">
      <el-form inline label-width="70" :model="searchForm">
        <el-row>
          <el-col :span="7" style="margin-left: 15px">
            <el-form-item label="医疗机构" prop="fullName">
              <el-select v-model="searchForm.currentOrg" placeholder="Select" value-key="id">
                <el-option v-for="item in orgList" :key="item.id" :label="item.orgaName" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="7" style="margin-left: 15px">
            <el-form-item label="药品信息 " prop="userName">
              <el-input v-model="searchForm.drugname" placeholder="代码/名称/输入码/名称首字母..." />
            </el-form-item>
          </el-col>
          <el-col :span="7" style="margin-left: 15px">
            <el-form-item>
              <el-checkbox v-model="searchForm.issfdj" label="是否冻结" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <el-card class="btn-row" shadow="never">
      <div class="btn-box">
        <el-button class="operation-btn" @click="DrugBalanceMaster(1)">
          <img alt="" src="@/assets/drugAdministration/inventoryInquiry/查询.png" />
          查询(F5)
        </el-button>
        <el-button class="operation-btn" @click="addbtn('新增')">
          <img alt="" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          新增(Ctrl+N)
        </el-button>
        <el-button class="operation-btn" @click="updatabtn('修改')">
          <img alt="" src="@/assets/drugAdministration/orgaDrugContents/修改.png" />
          修改(Ctrl+M)
        </el-button>
        <el-button class="operation-btn" @click="kcxxbtn()">
          <img alt="" src="@/assets/drugAdministration/orgaDrugContents/库存.png" />
          库存
        </el-button>
        <el-button class="operation-btn" @click="djypbtn()">
          <img alt="" src="@/assets/drugAdministration/orgaDrugContents/冻结.png" />
          {{ flagShowCameras == true ? "冻结" : "解冻" }}
        </el-button>
        <el-button class="operation-btn" @click="deleteOrgaDrugInfo()">
          <img alt="" src="@/assets/drugAdministration/inventoryInquiry/删除.png" />
          删除(F10)
        </el-button>
        <el-button class="operation-btn" @click="exportXml()">
          <img alt="" src="@/assets/drugAdministration/orgaDrugContents/导出.png" />
          导出(F8)
        </el-button>
      </div>
    </el-card>
    <div class="table-box">
      <div class="left-table-box">
        <el-scrollbar>
          <el-tree
            :ref="el => (refList.treeRef = el)"
            accordion
            :data="treeDataleft"
            :default-expanded-keys="treeArr"
            :expand-on-click-node="false"
            :highlight-current="true"
            node-key="id"
            :props="defaultProps"
            @node-click="handleNodeClick"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node">
                <span v-show="node.label != '全部'">
                  <el-icon class="List" />
                </span>
                <span>{{ "[" + data.catalogCode + "]" }}</span>
                <span>{{ node.label }}</span>
              </span>
            </template>
          </el-tree>
        </el-scrollbar>
      </div>
      <div class="right-table-box">
        <el-table
          ref="singleTableRef"
          v-loading="tableLoading"
          border
          :cell-style="{
            boxSizing: 'border-box',
            padding: '0',
            userSelect: 'none'
          }"
          class="right-table"
          :data="tableDataright"
          :header-cell-style="{ padding: '0', height: '36px' }"
          highlight-current-row
          :row-style="{ height: '32px' }"
          stripe
          @row-click="handleCurrentChange"
        >
          <el-table-column align="center" label="是否冻结" prop="status" show-overflow-tooltip width="100">
            <template #default="{ row }">
              <span :class="row.status === '冻结' ? 'fontStyle' : ''">
                {{ row.status }}
              </span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="药品代码" prop="drugCode" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="医保编码" prop="insureCode" show-overflow-tooltip width="200" />
          <el-table-column align="center" label="国家码" prop="countryCode" show-overflow-tooltip width="200" />
          <el-table-column align="center" label="药品名称" prop="drugName" show-overflow-tooltip width="200" />
          <el-table-column align="center" label="产地" prop="origin" show-overflow-tooltip width="200" />
          <el-table-column align="center" label="门诊包装规格" prop="outpPackageSpec" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="库房包装规格" prop="stockPackageSpec" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="品种代码" prop="varietyCode" show-overflow-tooltip width="100" />
          <el-table-column align="center" label="品种名称" prop="varietyName" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="剂型" prop="drugFormName" show-overflow-tooltip width="100" />
          <el-table-column align="center" label="门诊发票分类" prop="invoOutpName" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="住院发票分类" prop="invoiInpName" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="基药标志" prop="baseDrugMarkName" show-overflow-tooltip width="100" />
          <el-table-column align="center" label="商品名" prop="commodityName" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="计价单位" prop="priceUnitName" show-overflow-tooltip width="90" />
          <el-table-column align="center" label="保险属性" prop="insurePropertyName" show-overflow-tooltip width="100" />
          <el-table-column align="center" label="批准文号" prop="approvalNumber" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="基本剂量" prop="basicDosimetry" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="门诊包装量" prop="outpPackagQuantity" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="门诊包装单位" prop="outpPackagUnitName" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="库房包装量" prop="stockPackagQuantity" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="库房包装单位" prop="stockPackagUnitName" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="计价包装规格" prop="packageSpec" show-overflow-tooltip width="140" />
          <el-table-column align="center" label="加成率" prop="addRate" show-overflow-tooltip width="90" />
          <el-table-column align="center" label="是否带量" prop="isDL" show-overflow-tooltip width="90" />
          <el-table-column align="center" label="药品备注" prop="drugMemo" show-overflow-tooltip width="120" />
          <el-table-column align="center" label="是否在用" prop="isActive" show-overflow-tooltip width="90" />
          <template #empty>
            <el-empty :image-size="100" description="暂无数据" />
          </template>
        </el-table>
        <el-pagination
          :background="true"
          class="mt-4"
          :current-page="currentPage"
          layout="total, sizes, prev, pager, next, jumper"
          :page-size="pageSize"
          :page-sizes="[10, 20, 30, 50]"
          :total="total"
          @current-change="handleCurrentChange1"
          @size-change="handleSizeChange"
        />
      </div>
    </div>
    <!-- 新增/修改 -->
    <el-dialog
      v-model="instituDialogVisible"
      class="pub-dialog"
      :close-on-click-modal="false"
      :rules="rules"
      style="width: 900px"
      title="机构药品信息维护"
      top="60px"
      @close="resetForm(formRef)"
    >
      <div>
        <el-divider style="width: 102%; margin: -25px 0px 30px -8px" />
      </div>
      <el-form ref="formRef" inline label-width="auto" :model="formData" :rules="rules">
        <!-- 第一行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item class="itemCode" label="药品代码" prop="itemCode">
              <el-input
                :ref="
                  el => {
                    formRefs['itemCode'] = el;
                  }
                "
                v-model="formData.itemCode"
                class="drugCodeStyle"
                :disabled="zujian == '修改' ? true : false"
                placeholder="输入项目代码\拼音..."
                reserve-keyword
              >
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="药品名称" prop="">
              <el-input v-model="formData.itemName" clearable placeholder=" " :disabled="zujian == '修改' ? true : false" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品名" prop="commodityName">
              <el-input v-model="formData.commodityName" clearable placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="输入码" prop="drugInputCode">
              <el-input
                v-model="formData.drugInputCode"
                :formatter="removeChineseCharacter"
                :parser="removeChineseCharacter"
                placeholder="请输入非中文..."
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="输入码2">
              <el-input
                v-model="formData.drugInputCode2"
                :formatter="removeChineseCharacter"
                :parser="removeChineseCharacter"
                placeholder="请输入非中文..."
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="输入码3">
              <el-input
                v-model="formData.drugInputCode3"
                :formatter="removeChineseCharacter"
                :parser="removeChineseCharacter"
                placeholder="请输入非中文..."
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第二行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="剂型" prop="DrugFormName">
              <el-input v-model="formData.DrugFormName" clearable placeholder="" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="产地" prop="Origin">
              <el-input v-model="formData.Origin" clearable placeholder="" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第三行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="医保编码" prop="insureCode">
              <el-input
                :ref="
                  el => {
                    formRefs['insureCode'] = el;
                  }
                "
                v-model="formData.insureCode"
                clearable
                placeholder=""
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="保险属性" prop="insurePropertyName">
              <el-select
                :ref="
                  el => {
                    formRefs['insurePropertyName'] = el;
                  }
                "
                v-model="formData.insurePropertyName"
                placeholder=""
                value-key="id"
              >
                <el-option v-for="item in formList.inAbutesList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="基本剂量">
              <div style="display: flex; flex-direction: row; width: 100%">
                <el-input v-model="formData.jbjl" clearable placeholder="" />
                <el-select v-model="formData.dosimetryUnitName" clearable placeholder="" value-key="id">
                  <el-option v-for="item in formList.dosageUnitList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
                </el-select>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第四行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="基本包装量" prop="BasicDosimetry">
              <el-input v-model="formData.BasicDosimetry" placeholder=" " />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计价包装单位" prop="priceUnitName">
              <el-input v-model="formData.priceUnitName" placeholder=" " />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="计价包装规格" prop="packageSpec">
              <el-input v-model="formData.packageSpec" placeholder=" " />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第五行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="门诊包装量" prop="outpPackagQuantity">
              <el-input-number
                v-model="formData.outpPackagQuantity"
                controls-position="right"
                :disabled="IsOutpPackagQuantityNotChange == '是' ? true : false"
                :max="99999999"
                maxlength="8"
                :min="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="门诊包装单位" prop="outpPackagUnitName">
              <el-select
                v-model="formData.outpPackagUnitName"
                :disabled="IsOutpPackagQuantityNotChange == '是' ? true : false"
                placeholder=""
              >
                <el-option
                  v-for="item in formList.inpPerformedDeptList"
                  :key="item.id"
                  :label="item.kvmemo"
                  :value="item.kvmemo"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="门诊包装规格" prop="outpPackageSpec">
              <el-input
                v-model="formData.outpPackageSpec"
                clearable
                :disabled="IsOutpPackagQuantityNotChange == '是' ? true : false"
                placeholder=" "
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第六行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="库房包装量" prop="stockPackagQuantity">
              <el-input-number
                v-model="formData.stockPackagQuantity"
                controls-position="right"
                :max="99999999"
                maxlength="8"
                :min="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="库房包装单位" prop="stockPackagUnitName">
              <el-select v-model="formData.stockPackagUnitName" placeholder="">
                <el-option v-for="item in formList.stockPackagList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="库房包装规格" prop="stockPackageSpec">
              <el-input v-model="formData.stockPackageSpec" clearable placeholder=" " />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第七行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="门诊执行科室" prop="OutpPerformedDeptName">
              <el-select
                :ref="
                  el => {
                    formRefs['OutpPerformedDeptName'] = el;
                  }
                "
                v-model="formData.OutpPerformedDeptName"
                placeholder=""
                value-key="id"
              >
                <el-option v-for="item in formList.outpDepartmentList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="住院执行科室" prop="InpPerformedDeptName">
              <el-select
                :ref="
                  el => {
                    formRefs['InpPerformedDeptName'] = el;
                  }
                "
                v-model="formData.InpPerformedDeptName"
                placeholder=""
                value-key="id"
              >
                <el-option v-for="item in formList.inexDepartmentList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="医嘱类型" prop="OrderClassname">
              <el-select
                :ref="
                  el => {
                    formRefs['OrderClassname'] = el;
                  }
                "
                v-model="formData.OrderClassname"
                placeholder=""
                value-key="id"
              >
                <el-option v-for="item in formList.orderTypeList" :key="item.id" :label="item.kvmemo" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第八行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="费别标志" prop="FeeTypeMaskFlag">
              <el-select
                :ref="
                  el => {
                    formRefs['FeeTypeMaskFlag'] = el;
                  }
                "
                v-model="formData.FeeTypeMaskFlag"
                clearable
                placeholder=""
              >
                <el-option key="1" label="按费别计费" value="按费别计费" />
                <el-option key="2" label="不按费别计费" value="不按费别计费" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="收费类别" prop="chargeItemClassId">
              <el-select v-model="formData.chargeItemClassId" clearable placeholder="">
                <el-option v-for="item in formList.insurePropertList" :key="item.id" :label="item.kvmemo" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item class="addRate" label="加成率" prop="addRate">
              <el-input
                :ref="
                  el => {
                    formRefs['addRate'] = el;
                  }
                "
                v-model="formData.addRate"
                clearable
                placeholder=" "
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第九行 -->
        <el-row>
          <el-col :span="20">
            <el-form-item label=" " prop="">
              <el-checkbox v-model="formData.mzsy" label="门诊使用" />
              <el-checkbox v-model="formData.zysy" label="住院使用" />
              <el-checkbox v-model="formData.jzsy" label="急诊使用" />
              <el-checkbox v-model="formData.isDl" label="带量" />
              <el-checkbox v-model="formData.isGpo" label="GPO" />
              <el-checkbox v-model="formData.netDrugstore" label="云药房" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第十行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="皮试标志" prop="skinTestMark">
              <el-select
                :ref="
                  el => {
                    formRefs['skinTestMark'] = el;
                  }
                "
                v-model="formData.skinTestMark"
                placeholder=""
              >
                <el-option key="1" label="是" value="是" />
                <el-option key="2" label="否" value="否" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="皮试方式" prop="skinTestTypeName">
              <div style="display: flex; flex-direction: row; width: 100%">
                <el-select
                  ref="ps"
                  v-model="formData.skinTestTypeName"
                  clearable
                  :disabled="formData.skinTestMark == '是' ? false : true"
                  placeholder=""
                  value-key="id"
                >
                  <el-option v-for="item in skinTypeList" :key="item.id" :label="item.kvmemo" :value="item.id" />
                </el-select>
                <el-input
                  v-if="formData.skinTestTypeName == '其他'"
                  v-model="formData.TestTypeName"
                  clearable
                  placeholder="请输入..."
                />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="YPID" prop="ypid">
              <el-input
                :ref="
                  el => {
                    formRefs['ypid'] = el;
                  }
                "
                v-model="formData.ypid"
                clearable
                placeholder=" "
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第十一行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="默认频次" prop="drugFrequencyname">
              <el-select
                :ref="
                  el => {
                    formRefs['drugFrequencyname'] = el;
                  }
                "
                v-model="formData.drugFrequencyname"
                clearable
                placeholder=""
                value-key="id"
              >
                <el-option v-for="item in formList.defaultFreqList" :key="item.id" :label="item.kvmemo" :value="item.kvmemo" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="默认用量" prop="drugDosage">
              <div style="display: flex; flex-direction: row; width: 100%">
                <el-input
                  :ref="
                    el => {
                      formRefs['drugDosage'] = el;
                    }
                  "
                  v-model="formData.drugDosage"
                  clearable
                  oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1','')"
                  placeholder="可输入小数,不可输入负数"
                />
                <el-input v-model="formData.yldwName" clearable placeholder=" " />
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="国家码" prop="CountryCode">
              <el-input
                :ref="
                  el => {
                    formRefs['CountryCode'] = el;
                  }
                "
                v-model="formData.CountryCode"
                clearable
                placeholder=" "
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第十二行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item class="insureRate" label="医保系数" prop="insureRate">
              <el-input
                :ref="
                  el => {
                    formRefs['insureRate'] = el;
                  }
                "
                v-model="formData.insureRate"
                clearable
                placeholder="请输入..."
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item class="purchaseRate" label="采购系数" prop="purchaseRate">
              <el-input
                :ref="
                  el => {
                    formRefs['purchaseRate'] = el;
                  }
                "
                v-model="formData.purchaseRate"
                clearable
                placeholder=" "
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="处方权限" prop="drugPrescauthName">
              <el-select
                :ref="
                  el => {
                    formRefs['drugPrescauthName'] = el;
                  }
                "
                v-model="formData.drugPrescauthName"
                clearable
                placeholder=" "
              >
                <el-option v-for="item in permissionsList" :key="item.id" :label="item.kvmemo" :value="item" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第十三行 -->
        <el-row>
          <el-col :span="8">
            <el-form-item label="性别限制" prop="sexLimit">
              <el-select
                :ref="
                  el => {
                    formRefs['sexLimit'] = el;
                  }
                "
                v-model="formData.sexLimit"
                placeholder=""
              >
                <el-option key="0" label="不限" value="0" />
                <el-option key="1" label="男" value="1" />
                <el-option key="2" label="女" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item class="identifyCode" label="药品标识码" prop="identifyCode">
              <el-input
                :ref="
                  el => {
                    formRefs['identifyCode'] = el;
                  }
                "
                v-model="formData.identifyCode"
                clearable
                placeholder="请输入..."
                readonly
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="药品属性" prop="drugPropertyId">
              <el-select
                :ref="
                  el => {
                    formRefs['drugProperty'] = el;
                  }
                "
                v-model="formData.drugPropertyId"
                placeholder="请选择"
              >
                <el-option v-for="item in formList.drugPropertyList" :key="item.id" :label="item.kvmemo" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第十四行 -->
        <el-row>
          <el-col :span="24">
            <el-form-item label="药品备注" prop="notes">
              <el-input
                :ref="
                  el => {
                    formRefs['itemMemo'] = el;
                  }
                "
                v-model="formData.itemMemo"
                :autosize="{ minRows: 4, maxRows: 8 }"
                placeholder="请输入..."
                type="textarea"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div>
          <el-button
            id="xzbutton"
            :ref="
              el => {
                formRefs['xzqueren'] = el;
              }
            "
            type="primary"
            @click="verify(formRef)"
          >
            确认
          </el-button>
          <el-button type="danger" @click="resetForm(formRef)">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <!-- 库存信息 -->
    <el-dialog v-model="drugInfovisible" align-center width="26%" @close="close">
      <div class="dialogHeader">
        <span class="toubu">库存信息</span>
      </div>
      <div>
        <el-divider style="width: 104%; margin: 8px 0px 10px -10px" />
      </div>
      <div>
        <el-table
          ref="drugbalanceRef"
          border
          :data="drugInfoKCList"
          :header-row-style="{ height: '10px' }"
          highlight-current-row
          :row-style="{ height: '8px' }"
          stripe
          style="height: 200px"
          tabindex="-1"
          @row-click="hdrugInfoKCChange"
        >
          <el-table-column align="center" label="部门编号" min-width="100" prop="deptCode" show-overflow-tooltip />
          <el-table-column align="center" label="部门名称" min-width="100" prop="deptName" show-overflow-tooltip />
          <el-table-column align="center" label="数量" min-width="100" prop="inventory" show-overflow-tooltip />
        </el-table>
      </div>
      <div style="display: flex; justify-content: right; margin-top: 15px">
        <el-button class="xzbuttonStyle" type="primary" @click="close()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/确定.png" />
          确定
        </el-button>
        <el-button class="xzbuttonStyle" type="danger" @click="close()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/取消.png" />
          取消
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style lang="scss">
.el-popper.is-dark {
  max-width: 25vw;
  background-color: var(--el-color-primary);
  border: none !important;

  .el-popper__arrow::before {
    background-color: var(--el-color-primary);
    border: none !important;
  }
}
</style>
<style lang="scss" scoped>
:deep(*) {
  .el-form-item {
    width: -webkit-fill-available;
    margin-right: 16px;
    margin-bottom: 3px;
    .el-form-item__error {
      margin-top: -7px;
    }
  }
  .el-input,
  .el-select {
    width: -webkit-fill-available;
  }
  .bill-notice {
    .el-dialog__header {
      padding: 20px 10px;
      margin: 0;
      font-size: 1.25em;
      letter-spacing: 5px;
    }
    .el-dialog__body {
      padding: 5px 10px;
      font-size: 1.125em;
      text-align: center;
      letter-spacing: 2px;
    }
    .el-dialog__footer {
      padding: 20px 10px 10px 10px;
    }
  }
}
::-webkit-scrollbar {
  display: none;
}
.main {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 146px);
  // padding: 6px;
  // background-color: #f6f8f9;
  .el-card {
    margin-bottom: 6px;
  }

  :deep(.el-card__body) {
    padding: 5px 5px 5px 20px;
  }
  :deep(.top-form) {
    .el-form-item__label {
      justify-content: center;
    }
    .el-form-item__content {
      display: flex;
      .el-input {
        flex: 1;
      }
    }
  }
  :deep(.btn-row) {
    display: flex;
    flex-direction: column;
    justify-content: center;

    .el-card__body {
      text-align: right;

      .btn-box {
        display: inline-block;

        .operation-btn {
          display: inline-flex;
          align-items: center;
          justify-content: center;
          // min-width: v-bind(minbtnWidth);
          // max-width: v-bind(maxbtnWidth);
          // height: 30px;
          // margin: 5px;
          // font-size: v-bind(size);
          line-height: 32px;
          border: 1px solid #d5d5d8;
          border-radius: 2px;

          img {
            margin-right: 5px;
          }
        }
      }
    }
  }
  :deep(.table-box) {
    display: flex;
    flex-direction: row;
    height: calc(100vh - 232px);
    .left-table-box {
      width: calc(25%);
      padding: 20px 15px;

      background-color: white;
      .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content {
        color: var(--el-color-primary);
        .el-tree-node__expand-icon {
          color: var(--el-color-primary);
        }
      }
    }
    .right-table-box {
      width: calc(75% - 6px);
      margin-left: 6px;
      background-color: white;
      .right-table {
        width: 100%;
        height: 93%;
      }
      .mt-4 {
        margin-top: 7px;
        display: flex;
        justify-content: center;
      }
    }
  }
  :deep(.itemCode) {
    // width: unset;
    .el-input-group__prepend {
      position: absolute;
      top: 30px;
      left: 0px;
      z-index: 10;
      padding: 10px;
      background-color: white;
      box-shadow: var(--el-box-shadow-light);
    }

    .el-table {
      max-width: 700px;
      height: 400px;
      margin-bottom: 0;
      .selected-cell {
        background-color: var(--el-color-primary-light-9);
        border: 2px solid var(--el-color-primary) !important;
      }
      .el-table__header {
        border-spacing: 1px 0px;
      }
      .el-table__body {
        border-spacing: 1px 2px;
      }
    }
  }
}
.fontStyle {
  font-weight: 700;
  color: #dd5861;
}
.dialogHeader {
  margin-top: -40px;
}
.xzbuttonStyle {
  width: -webkit-fill-available;
  .buttonImg {
    width: 10px;
    height: 10px;
    margin-right: 5px;
  }
}
.toubu {
  width: -webkit-fill-available;
}

//控制药品代码弹出表格高度
.drugcodeStyle {
  min-width: 742px !important;
  height: 484px !important;
  .el-table .el-table__body-wrapper::-webkit-scrollbar {
    width: 100px;
    height: 100px;
    background-color: transparent;
  }
}
</style>
<style>
/* 下拉框最大表格的宽度 */
.el-select-dropdown {
  max-width: 500px;
}
/* 下拉框的小标签去除 */
.el-popper__arrow {
  display: none !important;
}
</style>
