<script setup lang="ts">
import { getDrugInfoInStoreBatchByCode } from "@/api/drugAdministration/drugReturnManagement";
import {
  checkOutStoreBill,
  deleteOutStoreBillDetail,
  getOutStoreBillDetailListWithDrawal,
  getOutStoreBillMasterList,
  saveOutStoreBill,
  wasterOutStoreBill
} from "@/api/drugAdministration/outOfWarehouse";
import { checkDate, getDefaultQueryDates, isEndTimeMidnight } from "@/utils/drugAdministration";
import { exportDrugExpiryTable } from "@/utils/drugAdministration/excel";
import { throttle } from "echarts";
import { dayjs, ElMessage, ElMessageBox, ElTable, FormInstance, FormRules } from "element-plus";
import { tr } from "element-plus/es/locale";
import { nextTick, onMounted, reactive, ref } from "vue";
const dateList = getDefaultQueryDates();
/** 搜索表单  */
const formData = reactive({
  dateType: "1", //日期类型(录单日期/审核日期)
  startDate: dateList[0] + " 00:00:00", //日期-开始时间
  endDate: dateList[1] + " 23:59:59", //日期-结束时间
  executor: "", //审核人
  executorName: "", //审核人名称yang
  status: false //是/否 （未审核的按钮 是否勾选）
  // cmbpxfs: '0', //排序方式
});
/**主表数据 */
const mainTable = ref([
  {
    id: "",
    status: "", //状态
    billNo: "", //出库单号
    relativeDeptId: "", //相对部门id
    receivingDeptName: "", //相对部门
    money: "", //金额
    inputDate: "", //录单日期
    reserveStatus: "", //冲销状态
    checkDate: "", //审核日期
    checkName: "", //审核人
    outStoreMemo: "" //发票号
  }
]);
/** 明细表数据 */
const detailTable = ref([
  {
    id: "",
    drugCode: "", //药品代码
    drugName: "", //药品名称
    drugSpecification: "", //规格
    number: "", //出库数量
    packagQuantity: "",
    unitName: "", //单位
    purchasePrice: "", //进价
    retailPrice: "", //零售价
    batchNumber: "", //批号
    validityPeriod: "", //效期
    origin: "", //产地
    supplyName: "", //供货商
    batch: "", //批次
    orgaDrugId: "" //机构药品id
    // batch:'',	//库存量
  }
]);
/** 出库主表对象 */
const MediOutStoreBillMaster = ref({
  id: "", //主表id
  orgaId: "", //机构id
  deptId: "", //部门id
  deptInputCode: "", //部门输入码
  deptCode: "", //部门代码
  deptName: "", //部门名称
  relativeDeptId: "", //相对部门id
  receivingDeptInputCode: "", //相对部门输入码
  receivingDeptCode: "", //相对部门代码
  receivingDeptName: "", //相对部门
  billNo: "", //出库单号
  checkId: "", //审核id
  checkDate: "", //	审核日期
  outStoreMemo: "", //发票号
  isActive: "", //是否在用
  stopDate: "", //停用日期
  status: "", //状态
  // statusColor:'',//状态颜色
  operatorId: "", //操作员id
  inputDate: "", //	录单日期
  createUser: "", //创建人
  createDate: "", //创建时间
  comment: "", //备注
  // checkName:'',//审核人
  // reserveStatus:'',//冲销状态
  // money:'',//金额
  // orgaName:'',//机构名称
  invoiceNumber: "", //发票数量
  invoiceDate: "" //发票日期
});
/** 出库明细对象 */
const detailArray = ref({
  isSelected: "", //？
  id: "",
  orgaId: "", //机构id
  outStoreBillId: "", //出库主表id
  billNo: "", //出库单号
  drugId: "", //药品id
  orgaDrugId: "", //机构药品id
  drugInputCode: "", //机构药品id
  drugInputCode2: "", //药品输入码2
  drugInputCode3: "", //药品输入码3
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  origin: "", //产地
  supplierId: "", //供应商id
  supplyInputCode: "", //供应商输入码
  supplyCode: "", //供应商输入码
  supplyName: "", //供应商
  batchNumber: "", //批号
  batch: "", //批次
  validityPeriod: "", //效期
  purchasePrice: "", //进价
  retailPrice: "", //零售价
  number: "", //出库数量
  applyOrgaNumber: "", //
  outStoreNumber: "", //
  checkNumber: "", //审核数量
  actualNumber: "", //
  unitId: "", //单位id
  unitName: "", //单位
  packagQuantity: "", //库房包装量
  purchaseMoney: "", //金价总额
  retailMoney: "", //零售价总额
  isActive: "", //是否在用
  stopDate: "", //停用时间
  status: "", //状态
  operatorId: "", //操作员id
  inputDate: "", //录单时间
  createUser: "", //创建人
  createDate: "", //创建时间
  comment: "", //备注
  dwAmount: "", //冲销数量
  inStorePrice: "", //
  relaOrgan: "", //
  outBillNo: "", //
  inventory: "", //
  amount: "", //
  selected: "" //
});
const TableFormRef = ref<FormInstance>();
const isMainData = ref(true); //主表是否查询到数据(是：false，否：true)
const isAdd = ref(true); //是否点击新增(是：false，否：true)
const isShowAdd = ref(false); //是否显示新增(是：false，否：true)
const isPharmacy = ref("是"); //是否为药房
const opacityStyle = ref(""); //主表的禁用样式
const isDetailData = ref(false); //新增时详细表是否有数据
const relativeDeptIdRef: any = ref();
const isReview = ref(true); //是否已经审核(是：false，否：true)
const detailAllList = ref<Record<string, any>[]>([]); //出库详细表数据集   //保存时传递
const oldDrugList = ref([]); // 明细表的列表？？
const drugCodeRef: any = ref(); // 药品代码
const unitRow: any = ref({}); // 单位行
const unitNameList: any = ref([]); //单位数据集
/** 部门列表 */
const departmentList = ref([
  {
    id: "102",
    deptId: "102",
    deptName: "西药房"
  },
  {
    id: "202",
    deptId: "202",
    deptName: "住院药房"
  },
  {
    id: "302",
    deptId: "302",
    deptName: "中药房"
  }
]);
/** 表单校验规则 */
const rulesTable = reactive<FormRules>({
  drugCode: [
    {
      required: true,
      message: "请输入药品代码",
      trigger: "blur"
    }
  ],
  relativeDeptId: [
    {
      required: true,
      message: "请输入相对部门",
      trigger: "blur"
    }
  ],
  number: [
    {
      required: true,
      message: "请输入出库数量",
      trigger: "blur"
    }
  ]
});
const currentRowMain = ref({
  id: "",
  status: "", //状态
  billNo: "", //出库单号
  relativeDeptId: "", //相对部门id
  receivingDeptName: "", //相对部门
  money: "", //金额
  inputDate: "", //录单日期
  reserveStatus: "", //冲销状态
  checkDate: "", //审核日期
  checkName: "", //审核人
  outStoreMemo: "" //发票号
});
const currentRowDetail: any = ref({
  //明细表当前行
  id: "", //详细表id
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  number: "", //出库数量
  unitName: "", //单位
  purchasePrice: "", //进价
  retailPrice: "", //零售价
  batchNumber: "", //批号
  validityPeriod: "", //效期
  origin: "", //产地
  supplyName: "", //供货商
  batch: "", //批次
  orgaDrugId: "" //机构药品id
  // batch:'',	//库存量
});
/** 表格上方表单 */
const tableForm = reactive({
  id: "", //？
  relativeDeptId: "", //相对部门
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  origin: "", //产地
  supplyName: "", //供货商
  purchasePrice: "", //进价
  retailPrice: "", //零售价
  numberKC: "", //库存量
  unitName: "", //单位
  batchNumber: "", //批号
  validityPeriod: "", //有效期
  batch: "", //批次
  number: "" //出库数量
});
/** 选中的药品信息 */
const selectedDrugList: any = ref({
  isSelected: "",
  id: "",
  orgaId: "", //机构id
  outStoreBillId: "", //出库主表id
  billNo: "", //出库单号
  drugId: "", //药品id
  orgaDrugId: "", //机构药品id
  drugInputCode: "", //机构药品id
  drugInputCode2: "", //药品输入码2
  drugInputCode3: "", //药品输入码3
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  origin: "", //产地
  supplierId: "", //供应商id
  supplyInputCode: "", //供应商输入码
  supplyCode: "", //供应商输入码
  supplyName: "", //供应商
  batchNumber: "", //批号
  batch: "", //批次
  validityPeriod: "", //效期
  purchasePrice: "", //进价
  retailPrice: "", //零售价
  number: "", //出库数量
  applyOrgaNumber: "", //
  outStoreNumber: "", //
  checkNumber: "", //审核数量
  actualNumber: "", //
  unitId: "", //单位id
  unitName: "", //单位
  packagQuantity: "", //库房包装量
  purchaseMoney: "", //金价总额
  retailMoney: "", //零售价总额
  isActive: "", //是否在用
  stopDate: "", //停用时间
  status: "", //状态
  operatorId: "", //操作员id
  inputDate: "", //录单时间
  createUser: "", //创建人
  createDate: "", //创建时间
  comment: "", //备注
  dwAmount: "", //冲销数量
  inStorePrice: "", //
  relaOrgan: "", //
  outBillNo: "", //
  inventory: "", //
  amount: "", //
  selected: "" //
});
const changeDeptList = ref({
  id: "",
  deptName: "",
  deptCode: "",
  py: ""
});
/** 相对部门改变事件 */
const changeDept = (val: any) => {
  departmentList.value.forEach((item: any) => {
    if (item.id == val) {
      changeDeptList.value = item;
    }
  });
  nextTick(() => {
    relativeDeptIdRef.value.blur();
    drugCodeRef.value.focus();
  });
  clearn(false);
  // console.log(changeDeptList.value, '部门')
};
/**时间改变时 */
const changeDate = (val: any) => {
  // console.log(val,'val11');
  if (formData.startDate && formData.endDate) {
    formData.endDate = isEndTimeMidnight(formData.endDate);
    if (checkDate(formData.startDate, formData.endDate)) {
      ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
      return;
    }
  } else {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "error" });
    return;
  }
};
/**点击新增按钮 */
function selectBtn() {
  if (!TableFormRef.value) return;
  TableFormRef.value.resetFields();
  if (!formData.startDate || !formData.endDate) {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "error" });
    return;
  } else if (checkDate(formData.startDate, formData.endDate)) {
    ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
    return;
  } else {
    isAdd.value = true;
    isMainData.value = true;
    isShowAdd.value = false;
    opacityStyle.value = "";
    getMainList(); //获取主表，调接口
  }
}
const mainTableRef: any = ref<InstanceType<typeof ElTable>>(); //主表高亮
/**获取主表接口 */
const getMainList = async (): Promise<void> => {
  mainTable.value = [];
  detailTable.value = [];
  // outOfCount.value = ''    //出库数， 不需要
  getOutStoreBillMasterList(
    formData.dateType,
    formData.startDate,
    formData.endDate,
    "0521",
    "101",
    formData.executor,
    formData.status == true ? "是" : "否"
  )
    .then((res: any) => {
      if (res?.code == "200") {
        if (res.data.DefineOutStoreBillMasterInfo.length > 0) {
          isMainData.value = false;
          mainTable.value = res.data.DefineOutStoreBillMasterInfo;
          // console.log(data.DefineOutStoreBillMasterInfo, '主表数据')
          mainTableRef.value.setCurrentRow(mainTable.value[0]);
          currentRowMain.value = mainTable.value[0];
        } else {
          ElMessage({ message: "暂无数据!", type: "success" });
          isMainData.value = true;
          clearn(true);
        }
      } else {
        ElMessage({ message: res.msg || res.message || "失败！", type: "error" });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
const detailTableRef: any = ref<InstanceType<typeof ElTable>>(); //主表高亮
/** 查询明细表接口 */
const getDetailList = async (): Promise<void> => {
  detailAllList.value = [];
  getOutStoreBillDetailListWithDrawal(
    currentRowMain.value.id, //主表返回的id
    "0521" //机构id
  )
    .then((res: any) => {
      if (res?.code == "200") {
        if (res.data.DefineOutStoreBillDetailInfoWithDrawal.length > 0) {
          detailTable.value = JSON.parse(JSON.stringify(res.data.DefineOutStoreBillDetailInfoWithDrawal));
          // outOfCount.value = data.count // 出库数
          // purchasePriceTotal.value = data.total // 进价总额
          // retailTotal.value = data.total2 // 零售总额
          // console.log(data.DefineOutStoreBillDetailInfoWithDrawal, '主表数据')
          nextTick(() => {
            detailTableRef.value.setCurrentRow(detailTable.value[0]);
          });
          //修改及历史药品列表
          oldDrugList.value = JSON.parse(JSON.stringify(res.data.DefineOutStoreBillDetailInfoWithDrawal));
          // 获取详细表数据集
          res.data.DefineOutStoreBillDetailInfoWithDrawal.forEach((item: any) => {
            detailArray.value = item; //相同字段赋值
            detailArray.value.outStoreBillId = currentRowMain.value.id;
            detailArray.value.billNo = currentRowMain.value.billNo;
            detailArray.value.number = item.number;
            detailAllList.value.push(detailArray.value);
          });
          // detailAllList.value.splice(0, 1)
        } else {
          detailTable.value = [];
          detailAllList.value = [];
          // outOfCount.value = '' // 出库数
          // purchasePriceTotal.value = '' // 进价总额
          // retailTotal.value = '' // 零售总额
        }
      } else {
        ElMessageBox.alert(res.msg, "提示信息", {
          confirmButtonText: "确定",
          type: "error"
        }).then(() => {
          detailTable.value = [];
          detailAllList.value = [];
        });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 清空表格上方表单，传true清空所有（包括id，部门id）*/
function clearn(isAll: boolean) {
  console.log(isAll, "isAll");
  if (isAll) {
    tableForm.id = "";
    tableForm.relativeDeptId = ""; //相对部门
  }
  tableForm.drugCode = ""; //药品代码
  tableForm.drugName = ""; //药品名称
  tableForm.drugSpecification = ""; //规格
  tableForm.origin = ""; //产地
  tableForm.supplyName = ""; //供货商
  tableForm.purchasePrice = ""; //进价
  tableForm.retailPrice = ""; //零售价
  tableForm.numberKC = ""; //库存量(???)
  tableForm.unitName = ""; //单位
  tableForm.batchNumber = ""; //批号
  tableForm.validityPeriod = ""; //有效期
  tableForm.batch = ""; //批次
  tableForm.number = ""; //出库数量
}
/** 点击新增出库单按钮 */
function insertbtn() {
  isDetailData.value = false;
  nextTick(() => {
    relativeDeptIdRef.value.focus();
  });
  isAdd.value = false;
  isMainData.value = true;
  isShowAdd.value = true;
  isReview.value = false;
  detailTable.value = [];
  detailAllList.value = [];
  currentRowDetail.value = { id: "" };
  // outOfCount.value = ''    //出库数， 不需要
  opacityStyle.value = "opacity:0.3";
  mainTableRef.value.setCurrentRow();
  oldDrugList.value = [];
  clearn(true); //清除
}
/**点击保存按钮 */
const clickSave = throttle(function () {
  // console.log(detailTable.value.length, 'detailTable.value')
  if (detailTable.value.length > 0) {
    saveOutOfWarehouse(); //出库保存接口
  } else {
    ElMessageBox.alert("请填入出库药品信息再保存！", "提示信息", {
      confirmButtonText: "确定",
      type: "warning"
    });
  }
}, 3000);
/** 保存接口 */
const saveOutOfWarehouse = async (): Promise<void> => {
  // showLoading('出库单保存中…')
  // console.log(isAdd.value, 'isAdd')
  if (isAdd.value == false) {
    //不是新增入库单？
    MediOutStoreBillMaster.value.id = ""; //主表id
    MediOutStoreBillMaster.value.orgaId = "0521"; //机构id
    MediOutStoreBillMaster.value.deptId = "101"; //部门id
    MediOutStoreBillMaster.value.deptInputCode = "XYK"; //部门输入码
    MediOutStoreBillMaster.value.deptCode = "101"; //部门代码
    MediOutStoreBillMaster.value.deptName = "西药库"; //部门名称
    MediOutStoreBillMaster.value.relativeDeptId = changeDeptList.value.id; //相对部门id
    MediOutStoreBillMaster.value.receivingDeptInputCode = changeDeptList.value.py; //相对部门输入码
    MediOutStoreBillMaster.value.receivingDeptCode = changeDeptList.value.deptCode; //相对部门代码
    MediOutStoreBillMaster.value.receivingDeptName = changeDeptList.value.deptName; //相对部门
    MediOutStoreBillMaster.value.billNo = ""; //出库单号
    MediOutStoreBillMaster.value.checkId = ""; //审核id
    MediOutStoreBillMaster.value.checkDate = ""; //	审核日期
    MediOutStoreBillMaster.value.outStoreMemo = ""; //发票号
    MediOutStoreBillMaster.value.isActive = ""; //是否在用
    MediOutStoreBillMaster.value.stopDate = ""; //停用日期
    MediOutStoreBillMaster.value.status = ""; //状态
    MediOutStoreBillMaster.value.operatorId = "yang"; //操作员id
    MediOutStoreBillMaster.value.inputDate = ""; // getNowDate(new Date()) //	录单日期
    MediOutStoreBillMaster.value.createUser = "yang"; //创建人
    MediOutStoreBillMaster.value.createDate = ""; //getNowDate(new Date()) //创建时间
    MediOutStoreBillMaster.value.comment = ""; //备注
    MediOutStoreBillMaster.value.invoiceNumber = ""; //发票数量
    MediOutStoreBillMaster.value.invoiceDate = ""; //发票日期
  }
  // console.log(MediOutStoreBillMaster.value, '主表入参信息')
  saveOutStoreBill("是", MediOutStoreBillMaster.value, detailAllList.value)
    .then((res: any) => {
      if (res?.code == "200") {
        // addAllDialog.value = false   //一键新增弹窗
        ElMessageBox.alert("保存成功！", "提示信息", {
          confirmButtonText: "确定",
          type: "success"
        }).then(() => {
          isAdd.value = true;
          isShowAdd.value = false;
          opacityStyle.value = "";
          getMainList();
          // clearAddAll()   //清空一件新增弹框
        });
      } else {
        ElMessage({ message: res.msg || res.message || "失败！", type: "error" });
        // addAllDialog.value = false
        ElMessageBox.alert(res.msg, "提示信息", {
          confirmButtonText: "确定",
          type: "warning"
        });
        getMainList();
        // clearAddAll()
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 点击作废按钮 */
const voidBtn = throttle(function () {
  // console.log('作废')
  ElMessageBox.confirm("是否作废图库单号为" + currentRowMain.value.billNo + "的退库单号？", "提示信息", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    saveVoid(); //作废接口
  });
}, 3000);
/** 作废接口 */
const saveVoid = async (): Promise<void> => {
  wasterOutStoreBill(
    "0521",
    "101",
    currentRowMain.value.id, //主表ID
    "yang" //操作员ID
  )
    .then((res: any) => {
      if (res?.code == "200") {
        ElMessageBox.alert("作废成功!", "提示信息", {
          confirmButtonText: "确定",
          type: "success"
        }).then(() => {
          getMainList();
        });
      } else {
        ElMessageBox.alert(res.msg, "提示信息", {
          confirmButtonText: "确定",
          type: "success"
        });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 点击审核按钮 */
function reviewedBtn() {
  ElMessageBox.confirm("是否审核出库单号为" + currentRowMain.value.billNo + "的出库单?", "提示信息", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    outCheck(); //审核接口
  });
}
/** 审核接口 */
const outCheck = async (): Promise<void> => {
  // await showLoading('出库单审核中…')
  checkOutStoreBill(
    "yang", //操作员id
    "", //系统设置 药品一栏的 药库标志
    "", //是/否 （药品批次不同，是否继续执行）
    "0521", //机构id
    "101", //部门id
    currentRowMain.value.id //出库单主表id
  )
    .then((res: any) => {
      if (res?.code == "200") {
        ElMessageBox.alert("审核成功！", "提示信息", {
          confirmButtonText: "确定",
          type: "success"
        }).then(() => {
          getMainList();
        });
      } else {
        ElMessageBox.alert(res.msg, "提示信息", {
          confirmButtonText: "确定",
          type: "error"
        }).then(() => {
          getMainList();
        });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 点击添加药品按钮 */
const addToBtn = async () => {
  // if (!TableFormRef.value) return
  TableFormRef.value?.validate((valid, fields) => {
    if (valid) {
      // 如果药品代码一样，批次不一样可以添加，批次一样不能添加
      const isExisted = ref(false);
      if (detailTable.value.length > 0) {
        detailTable.value.forEach((item: any) => {
          if (item.drugCode == selectedDrugList.value.drugCode && item.batch == selectedDrugList.value.batch) {
            isExisted.value = true;
          }
        });
      }
      // console.log(isExisted.value, '是否已添加')
      if (isExisted.value) {
        ElMessageBox.alert("药品已存在！", "提示信息", {
          confirmButtonText: "确定",
          type: "warning"
        }).then(() => {
          drugCodeRef.value.focus();
        });
      } else {
        if (parseInt(tableForm.number) > parseInt(unitRow.value.all)) {
          // 如果出库数大于库存数
          ElMessageBox.alert("出库数不能大于库存数，请重新输入", "提示信息", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          });
        } else {
          isShowAdd.value = true;
          selectedDrugList.value.number = tableForm.number;
          selectedDrugList.value.outStoreBillId = currentRowMain.value.id;
          selectedDrugList.value.billNo = currentRowMain.value.billNo;
          selectedDrugList.value.batch = tableForm.batch;
          selectedDrugList.value.batchNumber = tableForm.batchNumber;
          selectedDrugList.value.orgaId = "0521";
          selectedDrugList.value.unitName = tableForm.unitName;
          selectedDrugList.value.purchasePrice = tableForm.purchasePrice;
          selectedDrugList.value.retailPrice = tableForm.retailPrice;
          selectedDrugList.value.packagQuantity = unitRow.value.number;
          selectedDrugList.value.operatorId = "yang";
          selectedDrugList.value.createUser = "yang";
          detailTable.value.push(selectedDrugList.value);
          // console.log(detailTable.value, 'detailTable.value')

          //将需添加的药品信息添加到详细表
          detailAllList.value.push(selectedDrugList.value);
          nextTick(() => {
            drugCodeRef.value.focus();
          });
          // 添加一条数据后禁用相对部门
          isDetailData.value = true;
        }
      }
    } else {
      console.log("error submit!", fields);
    }
  });
};
/** 点击修改按钮 */
const updateBtn = async () => {
  // if (!formEl) return
  unitNameList.value.forEach((val: any) => {
    if (tableForm.unitName == val.unitName) {
      unitRow.value = val;
    }
  });

  await TableFormRef.value?.validate((valid, fields) => {
    if (valid) {
      if (parseInt(tableForm.number) > parseInt(unitRow.value.all)) {
        // 如果出库数大于库存数
        ElMessageBox.alert("出库数不能大于库存数，请重新输入", "提示信息", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        });
      } else {
        detailTable.value.forEach((item: any, index: any) => {
          // 找到id 批次一样的那条记录，进行修改
          if (tableForm.id == item.id && tableForm.batch == item.batch) {
            // 如果修改后的药品跟已有药品的代码和批次一样则修改
            //修改detailTable的该条记录
            detailTable.value[index].number = tableForm.number;
            detailTable.value[index].unitName = tableForm.unitName;
            detailTable.value[index].packagQuantity = unitRow.value.number;
            detailTable.value[index].purchasePrice = tableForm.purchasePrice;
            detailTable.value[index].retailPrice = tableForm.retailPrice;
            // console.log(tableForm.id, item.id, '11')

            //修改detailAllList的该条记录
            detailAllList.value.forEach((allItem: any, allIndex: any) => {
              if (tableForm.id == allItem.id && tableForm.batch == allItem.batch) {
                // console.log( detailAllList.value[allIndex].drugName,'修改的药品')

                detailAllList.value[allIndex].number = tableForm.number;
                detailAllList.value[allIndex].unitName = tableForm.unitName;
                detailAllList.value[allIndex].packagQuantity = unitRow.value.number;
                isShowAdd.value = true;
              }
            });
          }
        });
      }
    } else {
      // console.log('error submit!', fields)
    }
  });
};
/** 点击删除按钮，判断是否是数据库的数据 */
const judgmentDetail = throttle(async (): Promise<void> => {
  clearn(false);
  const isOld = ref(false); //删除的数据是否为数据库内的数据
  console.log(currentRowDetail.value, 1111);
  if (currentRowDetail.value == null || currentRowDetail.value?.id == "") {
    currentRowDetail.value = detailTable.value[detailTable.value.length - 1];
  }
  if (oldDrugList.value.length > 0) {
    isOld.value = true;
    if (oldDrugList.value.length == 1) {
      await ElMessageBox.confirm("该数据是当前单据的最后一条明细，是否继续删除?", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        // console.log(currentRowDetail.value.id, 'currentRowDetail.value.id')
        deleteDetail(currentRowDetail.value.id); //删除接口
      });
    } else {
      // 如需删除的数据为已存在的数据
      oldDrugList.value.forEach((item: any) => {
        if (currentRowDetail.value?.id == item?.id && currentRowDetail.value?.batch == item?.batch) {
          deleteDetail(item.id);
        }
      });
    }
  }
  if (!isOld.value) {
    // 如需删除的数据为新添加的数据
    detailAllList.value.forEach((item: any, index: any) => {
      if (currentRowDetail.value?.id == item.id && currentRowDetail.value?.batch == item.batch) {
        detailAllList.value.splice(index, 1);
      }
    });
    // 所有需保存的药品数据
    detailTable.value.forEach((item: any, index: any) => {
      if (currentRowDetail.value?.id == item.id && currentRowDetail.value?.batch == item.batch) {
        detailTable.value.splice(index, 1);
      }
    });
    // nextTick(() => {
    //   detailTableRef.value.setCurrentRow(detailTable.value[0])
    // })
  }
  // 详细表没有数据则解禁相对部门
  if (detailTable.value?.length == 0) {
    isDetailData.value = false;
  }
  currentRowDetail.value = { id: "" };
}, 3000);
/** 删除接口 */
const deleteDetail = async (detailId: any): Promise<void> => {
  // showLoading('加载中…')
  deleteOutStoreBillDetail(
    "0521",
    "101",
    currentRowMain.value.id,
    detailId,
    "yang" //操作员ID
  )
    .then((res: any) => {
      if (res?.code == "200") {
        if (detailTable.value.length == 1) {
          getMainList();
        } else {
          getDetailList(); //查询明细表
        }
      } else {
        ElMessageBox.alert(res.msg, "提示信息", {
          confirmButtonText: "确定",
          type: "success"
        });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 主表当前行改变时 */
const mainTableChange = (val: any) => {
  currentRowMain.value = {
    id: "",
    status: "", //状态
    billNo: "", //出库单号
    relativeDeptId: "", //相对部门id
    receivingDeptName: "", //相对部门
    money: "", //金额
    inputDate: "", //录单日期
    reserveStatus: "", //冲销状态
    checkDate: "", //审核日期
    checkName: "", //审核人
    outStoreMemo: "" //发票号
  };
  if (val != null) {
    if (isShowAdd.value) {
      ElMessageBox.confirm("当前信息未保存，是否保存！", "提示信息", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          saveOutOfWarehouse();
        })
        .catch(() => {
          isShowAdd.value = false;
        });
    }
    currentRowMain.value = val;
    MediOutStoreBillMaster.value = val; //出库主表对象
    // getLumpSumList() // 获取符合条件的出库进总额、出库零售总额
    //点击新增则不给详细表赋值
    if (isAdd.value) {
      if (val.status == "未审核") {
        isReview.value = false;
      } else {
        isReview.value = true;
      }
      getDetailList(); // 加载明细表中的数据
    } else {
      detailTable.value = [];
      detailAllList.value = [];
      mainTableRef.value.setCurrentRow();
      // isReview.value = true
    }
  }
};
/** 输入药品代码后回车，查询药品信息,赋值给后面几个表单 */
const handleDrugCode = () => {
  getDrugInfoInStoreBatchByCode(tableForm.drugCode)
    .then((res: any) => {
      if (res?.code == "200" && res.data != null) {
        // tableForm.drugId = res.data.drugId;
        tableForm.drugCode = res.data.drugCode;
        tableForm.drugName = res.data.drugName;
        tableForm.drugSpecification = res.data.drugSpecification;
        tableForm.origin = res.data.origin;
        tableForm.validityPeriod = res.data.validityPeriod; //
        tableForm.purchasePrice = res.data.purchasePrice; //进价
        tableForm.retailPrice = res.data.retailPrice; //零售价
        tableForm.numberKC = res.data.priceNumber; // 库存量
        tableForm.unitName = res.data.unitName; // 单位
        tableForm.batchNumber = res.data.batchNumber;
        tableForm.batch = res.data.batch;
        tableForm.supplyName = res.data.supplyName;
        // tableForm.supplierId = res.data.supplyId;
        // tableForm.supplyCode = res.data.supplyCode;
        // tableForm.supplyInputCode = res.data.supplyInputCode;
        // tableForm.position = res.data.position;

        selectedDrugList.value = res.data;
        selectedDrugList.value.orgaId = "0521";
        selectedDrugList.value.createUser = "yang";
        selectedDrugList.value.operatorId = "yang";
        // console.log(detailTable.value, 'detailTable.value')

        // 添加一条数据后禁用相对部门
      } else {
        ElMessage({ message: "未查询到该药品信息！", type: "error" });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 明细表当前行改变时 */
const detailTableChange = (val: any) => {
  // console.log(val, 'val')
  currentRowDetail.value = val;
  // drugRow.value = val
  if (val != null) {
    if (isAdd.value) {
      // 点击新增之后禁用主表功能
      tableForm.relativeDeptId = currentRowMain.value.relativeDeptId; //相对部门
    }
    tableForm.id = val.id;
    tableForm.drugCode = val.drugCode; //药品代码
    tableForm.drugName = val.drugName; //药品名称
    tableForm.drugSpecification = val.drugSpecification; //规格
    tableForm.origin = val.origin; //产地
    tableForm.supplyName = val.supplyName; //供货商
    tableForm.purchasePrice = val.purchasePrice; //进价
    tableForm.retailPrice = val.retailPrice; //零售价
    tableForm.unitName = val.unitName; //单位
    tableForm.batchNumber = val.batchNumber; //批号
    tableForm.validityPeriod = val.validityPeriod; //有效期
    tableForm.batch = val.batch; //批次
    tableForm.number = val.number; //出库数量
    // getinventory()
  }
};
const tableDatadcxml: any = ref([]); //导出的列表
/** 点击导出按钮 */
const exportXml = throttle(() => {
  tableDatadcxml.value = [];
  if (detailTable.value.length > 0) {
    //  查询明细数据
    tableDatadcxml.value = detailTable.value;
  }
  Xml();
}, 3000);
/** 导出的数据集excel(主页面) */
function Xml() {
  const headersArr = [
    "出库相对部门",
    "药品代码",
    "药品名称",
    "规格",
    "出库数量",
    "单位",
    "进价",
    "零售价",
    "批号",
    "效期",
    "产地",
    "供应商",
    "批次"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    currentRowMain.value.receivingDeptName,
    item.drugCode,
    item.drugName,
    item.drugSpecification,
    item.number,
    item.unitName,
    item.purchasePrice,
    item.retailPrice,
    item.batchNumber,
    item.validityPeriod,
    item.origin,
    item.supplyName,
    item.batch
  ]);
  //标题不要超过30个字符，否则报错
  exportDrugExpiryTable(headersArr, data, "浦东测试机构西药库" + "出库单" + currentRowMain.value.billNo + "信息");
}
onMounted(() => {
  getMainList();
});
</script>
<template>
  <div id="main" tabindex="0">
    <div class="form">
      <el-form ref="infoFormRef" inline label-width="auto" :model="formData">
        <el-row>
          <el-col class="col" :span="10" style="display: flex">
            <el-form-item style="width: 39%">
              <el-radio-group v-model="formData.dateType" class="ml-4">
                <el-radio label="1"> 录单日期 </el-radio>
                <el-radio label="2"> 审核日期 </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="width: 60%">
              <el-date-picker
                v-model="formData.startDate"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="开始时间"
                style="flex: 1"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                @change="changeDate"
              />
              至
              <el-date-picker
                v-model="formData.endDate"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="结束时间"
                style="flex: 1"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                @change="changeDate"
              />
            </el-form-item>
          </el-col>
          <el-col class="col" :span="5">
            <el-form-item label="审核人" style="width: 97%">
              <el-select ref="reviewerRef" v-model="formData.executorName" clearable placeholder="请输入审核人姓名">
                <el-option label="张三" value="yang"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="5"> </el-col>
          <el-col class="col" :span="4" style="display: flex">
            <el-form-item style="width: 30%">
              <el-checkbox v-model="formData.status" label="未审核" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="button">
      <div style="display: inline-block">
        <el-button id="Inquire" class="buttonStyle" @click="selectBtn">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/查询.png" />
          查询(F5)
        </el-button>
        <el-button
          id="makeUp"
          class="buttonStyle"
          :disabled="isShowAdd == false && isPharmacy == '是' ? false : true"
          @click="insertbtn"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          新增出库单(F2)
        </el-button>
        <el-button
          id="list"
          class="buttonStyle"
          :disabled="isShowAdd == true && isPharmacy == '是' ? false : true"
          @click="clickSave"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/保存.png" />
          保存
        </el-button>
        <el-button
          id="compensation"
          class="buttonStyle"
          :disabled="isReview == false && isShowAdd == false && isPharmacy == '是' ? false : true"
          @click="voidBtn"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/作废.png" />
          作废(F4)
        </el-button>
        <el-button
          id="supplementary"
          class="buttonStyle"
          :disabled="isMainData == false && isReview == false && isPharmacy == '是' ? true : false"
          @click="exportXml()"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/打印.png" />
          导出(F7)
        </el-button>
        <el-button
          id="invoice"
          class="buttonStyle"
          :disabled="isReview == false && isShowAdd == false && isPharmacy == '是' ? false : true"
          @click="reviewedBtn"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/审核.png" />
          审核(F8)
        </el-button>
        <el-button
          id="invoice"
          class="buttonStyle"
          :disabled="isReview == false && isPharmacy == '是' ? false : true"
          @click="addToBtn()"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          添加药品(Ctrl+N)
        </el-button>
        <el-button
          id="invoice"
          class="buttonStyle"
          :disabled="isReview == false && isPharmacy == '是' ? false : true"
          @click="updateBtn()"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/修改.png" />
          修改(Ctrl+M)
        </el-button>
        <el-button
          id="invoice"
          class="buttonStyle"
          :disabled="isReview == false && isPharmacy == '是' ? false : true"
          @click="judgmentDetail"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/删除.png" />
          删除(DEL)
        </el-button>
      </div>
    </div>
    <div class="table">
      <div class="tableLeft" :style="opacityStyle">
        <el-table
          border
          ref="mainTableRef"
          :data="mainTable"
          highlight-current-row
          stripe
          style="z-index: 2; height: 100%"
          @current-change="mainTableChange"
        >
          <el-table-column align="center" show-overflow-tooltip label="状态" min-width="120px" prop="status">
            <template #default="scope">
              <span :class="scope.row.status == '未审核' ? 'fontStyle' : ''">
                {{ scope.row.status }}
              </span>
            </template>
          </el-table-column>
          <el-table-column align="center" show-overflow-tooltip label="出库单号" min-width="120px" prop="billNo" />
          <el-table-column align="center" show-overflow-tooltip label="相对部门" min-width="120px" prop="receivingDeptName" />
          <el-table-column align="center" show-overflow-tooltip label="金额" prop="money" />
          <el-table-column align="center" show-overflow-tooltip label="录单日期" min-width="120px" prop="inputDate" />
          <el-table-column align="center" show-overflow-tooltip label="冲销状态" min-width="120px" prop="reserveStatus" />
          <el-table-column align="center" show-overflow-tooltip label="审核日期" min-width="120px" prop="checkDate" />
          <el-table-column align="center" show-overflow-tooltip label="审核人" min-width="110px" prop="checkName" />
          <el-table-column align="center" show-overflow-tooltip label="发票号" min-width="110px" prop="outStoreMemo" />
          <!-- table没有数据是展示 -->
          <template #empty>
            <el-empty class="vab-data-empty" description="暂无数据" />
          </template>
        </el-table>
        <!-- </el-loading> -->
      </div>
      <div class="tableRight">
        <div class="tableForm">
          <el-form ref="TableFormRef" inline label-width="auto" :model="tableForm" :rules="rulesTable">
            <!-- 第一行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="相对部门" prop="relativeDeptId">
                  <el-select
                    ref="relativeDeptIdRef"
                    v-model="tableForm.relativeDeptId"
                    class="type"
                    clearable
                    :disabled="isDetailData == false && isAdd == false && isPharmacy == '是' ? false : true"
                    placeholder=" "
                    @change="changeDept"
                  >
                    <el-option v-for="item in departmentList" :key="item.id" :label="item.deptName" :value="item.id" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="药品代码" prop="drugCode">
                  <el-input
                    ref="drugCodeRef"
                    v-model="tableForm.drugCode"
                    placeholder="请输入药品代码\拼音..."
                    :disabled="isReview == false && isPharmacy == '是' ? false : true"
                    class="drugCodeStyle"
                    @keyup.enter="handleDrugCode"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="12">
                <el-form-item label="药品名称">
                  <el-input v-model="tableForm.drugName" clearable disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="规格">
                  <el-input v-model="tableForm.drugSpecification" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="产地">
                  <el-input v-model="tableForm.origin" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="12">
                <el-form-item label="供货商">
                  <el-input v-model="tableForm.supplyName" clearable disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="进价">
                  <el-input v-model="tableForm.purchasePrice" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="零售价">
                  <el-input v-model="tableForm.retailPrice" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="库存量">
                  <el-input v-model="tableForm.numberKC" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="单位">
                  <el-input v-model="tableForm.unitName" disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="批号">
                  <el-input v-model="tableForm.batchNumber" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="有效期">
                  <el-input v-model="tableForm.validityPeriod" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="批次">
                  <el-input v-model="tableForm.batch" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="出库数量" prop="number">
                  <el-input
                    ref="numberRef"
                    v-model="tableForm.number"
                    clearable
                    :disabled="isReview == false && isPharmacy == '是' ? false : true"
                    oninput="value=value.replace(/^0+(\d)|[^\d]+/g,'')"
                    @keyup.enter="addToBtn()"
                  />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-table
          border
          ref="detailTableRef"
          class="secondTable"
          :data="detailTable"
          highlight-current-row
          stripe
          @current-change="detailTableChange"
        >
          <el-table-column align="center" show-overflow-tooltip label="药品代码" min-width="120px" prop="drugCode" />
          <el-table-column align="center" show-overflow-tooltip label="药品名称" min-width="140px" prop="drugName" />
          <el-table-column align="center" show-overflow-tooltip label="规格" min-width="120px" prop="drugSpecification" />
          <el-table-column align="center" show-overflow-tooltip label="出库数量" min-width="90px" prop="number" />
          <el-table-column align="center" show-overflow-tooltip label="单位" min-width="90px" prop="unitName" />
          <el-table-column align="center" show-overflow-tooltip label="进价" min-width="90px" prop="purchasePrice" />
          <el-table-column align="center" show-overflow-tooltip label="零售价" min-width="110px" prop="retailPrice" />
          <el-table-column align="center" show-overflow-tooltip label="批号" min-width="90px" prop="batchNumber" />
          <el-table-column
            align="center"
            show-overflow-tooltip
            label="效期"
            min-width="120px"
            prop="validityPeriod"
            :formatter="
              ({ validityPeriod }) => {
                return dayjs(validityPeriod).format('YYYY-MM-DD');
              }
            "
          />
          <el-table-column align="center" show-overflow-tooltip label="产地" min-width="150px" prop="origin" />
          <el-table-column align="center" show-overflow-tooltip label="供货商" min-width="120px" prop="supplyName" />
          <el-table-column align="center" show-overflow-tooltip label="批次" min-width="120px" prop="batch" />
        </el-table>
      </div>
    </div>
  </div>
</template>
<style scoped lang="scss">
#main {
  min-height: calc(100vh - 150px);
  padding: 0 !important;
}
// 表单的必填
:deep(.el-form-item__label:before) {
  padding-top: 3px;
  font-size: 1.25rem;
}
// 必填提示
:deep(.el-form-item__error) {
  top: 9px;
  left: 35px;
}

// 表格字体大小
:deep(.cell) {
  color: #606266;
}
.form {
  width: 98%;
  padding: 10px 15px;
  margin: auto;
  margin-top: 7px;
  background: white;
  :deep(.el-form--inline .el-form-item) {
    margin-right: 0px !important;
  }
  :deep(.el-form-item__label-wrap) {
    margin-left: 0px !important;
  }
  :deep(.el-radio) {
    // margin-right: 10px;
    width: 35%;
  }
}

:deep(.el-select__popper.el-popper) {
  width: 80% !important;
  color: red !important;
}

.fontStyle {
  font-weight: 700;
  color: #dd5861;
}

:deep(.el-form-item) {
  width: 100%;
  margin-right: 5px !important;
  margin-bottom: 0px;
}

// 按钮部分
.button {
  width: 98%;
  padding: 10px 15px;
  margin: auto;
  margin-top: 7px;
  text-align: right;
  background: white;
}
.buttonImg {
  width: 12px;
  margin-right: 5px;
}
.buttonStyle {
  height: 27px;
}
//   表格部分
.table {
  display: flex;
  width: 100%;
  margin: auto;
  margin-top: 7px;
  // 左边部分
  .tableLeft {
    flex: 3;
    width: 30%;
    height: calc(100vh - 270px) !important;
    //   border: #41b584 1px solid;
  }
  // 右边部分
  .tableRight {
    width: 69.5% !important;
    height: calc(100vh - 270px) !important;
    margin-left: 0.5%;
    //   右边表单
    .tableForm {
      width: 100%;
      padding: 10px;
      margin-bottom: 10px;
      background: white;
      .col {
        display: flex;
        padding-left: 1%;
        padding-top: 0.4%;
      }
    }
    // 右边表格
    .secondTable {
      width: 100%;
      height: 100%;
      height: calc(100vh - 445px) !important;
    }
  }
}
</style>
