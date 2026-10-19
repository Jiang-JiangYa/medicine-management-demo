<script setup lang="ts">
import {
  getcheckRefundingBill,
  getDrugInfoInStoreBatchByCode,
  getnewReverseRefundingBill,
  getRefundingBillDetailListWithDrawal,
  getRefundingBillMasterList,
  getsaveRefundingBillMasterNew,
  getwasterRefundingBillDetail,
  getwasterRefundingBillMaster
} from "@/api/drugAdministration/drugReturnManagement";
import { getDrugInfoByCode } from "@/api/drugAdministration/drugStorageManagement";
import { checkDate, getNowTimeInterval, isEndTimeMidnight } from "@/utils/drugAdministration";
import { exportDrugExpiryTable } from "@/utils/drugAdministration/excel";
import { formatDate } from "@/utils/drugAdministration/index";
import { throttle } from "echarts";
import { ElMessage, ElMessageBox, ElTable, FormInstance, FormRules } from "element-plus";
import { nextTick, reactive, ref } from "vue";

const dateList = ref<[Date, Date]>(getNowTimeInterval());
//提示信息
const tableLoading = ref(false);
const tableLoadingRight = ref(false);
const isAdd = ref(true); //是否 未点击新增？(新增：false，不新增：true)
const opacityStyle = ref(""); //主表的禁用样式
const loading = ref(false);
//左右表格 表单控制
const tableLeft: any = ref([]);
const tableRight: any = ref([]);
const tableLeftRef: any = ref<InstanceType<typeof ElTable>>(); //高亮
const tableLeftcurrentRow: any = ref("");
const tableRightRef: any = ref<InstanceType<typeof ElTable>>(); //高亮
const tableRightcurrentRow: any = ref("");
const chname = ref(" "); //左边当前行的审核状态？
const isshzt = ref(true); //控制右边表单是否可用。左侧选择当前行为已审核时， true：禁用  false：可用
const isxz = ref(true); ////判断是否新增左边表格行
const ghskz = ref(true); //供货商控制
// //右边表格明细添加
const zujian = ref(""); //Add/Update
/**查询条件 */
const formData: any = reactive({
  isInputOrCheckDate: "录单日期", //日期类型(录单日期/审核日期)
  startDate: dateList?.value[0] + " ", //日期-开始时间
  endDate: dateList?.value[1] + " ", //日期-结束时间
  checknameid: "", //审核人id
  checkId: "", //审核人
  supplyName: "", //供应商
  unreviewed: false, //未审核       // true 表示未审核按钮选择
  // reverseAll: false, //全部冲销
  isChecked: false
});
/** 右边表格上方表达 */
const tableForm = reactive({
  // inStoreTypeId: "", //入库类型id
  // inStoreTypeName: "", //入库类型
  // publicSignId: "", //招标id
  // publicSignName: "", //招标类型
  supplyName: "", //供货商
  invoiceNumber: "", //发票编号
  invoiceDate: getNowDate(new Date(), "d"), //发票时间
  invoiceAmount: "", //发票金额
  supplierId: "", //供货商id
  supplyInputCode: "", ///供货商输入码
  supplyCode: "", //供货商代码
  expenditureAmount: "", //实退金额
  drugId: "", //药品ID
  drugInputCode: "", //药品输入码
  drugInputCode2: "", //药品输入码2
  drugInputCode3: "", //药品输入码3
  catalogId: "", //药品分类ID
  priceinventorynum: "",
  inventorynum: "",
  inventory: 0, //库存量
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  origin: "", //产地
  purchasePrice: "", //批发价
  inStorePrice: 0, //进价
  addRate: "", //加成率
  retailPrice: 0, //零售价
  number: 0, //退库数量
  unitName: "", //退库数量单位
  batchNumber: "", //批号
  validityPeriod: "", //效期
  position: "", //仓位
  comment: "", //其他信息
  // lastPurchasePrice: "", //上次进价
  // lastRetailPrice: "", //上次售价
  stockPackagQuantity: "", //库房包装量
  stockPackagUnitId: "", //单位
  orgaDrugId: "", //机构药品id
  batch: "" //批次
});
////表单校验
const TableForm: any = ref<FormInstance>();
const rules = reactive<FormRules>({
  supplyName: [
    {
      required: true,
      message: "请输入正确供应商",
      trigger: "blur"
    }
  ],
  drugCode: [
    {
      required: true,
      message: "请输入正确药品代码",
      trigger: "blur"
    }
  ],
  number: [
    {
      required: true,
      message: "请输入正确退货数量",
      trigger: "blur"
    }
  ],
  expenditureAmount: [
    {
      required: true,
      message: "请输入实退金额",
      trigger: "blur"
    }
  ],
  invoiceNumber: [
    {
      required: true,
      message: "请输入正确的发票号",
      trigger: "blur"
    }
  ]
});
//获取右边表单焦点回车事件
const formRefs: any = reactive([]);
const henderEnter = (e: any, lastField: any, nextField: any, nowField?: any) => {
  if (e.key == "Enter") {
    //供应商
    if (lastField == formRefs["supplyName"]) {
      nextField.focus();
    }
    //药品代码
    if (lastField == formRefs["drugCode"]) {
      nextField.focus();
    }
    //退货数量
    if (lastField == formRefs["number"]) {
      nextField.focus();
    }
    //实退金额
    if (lastField == formRefs["expenditureAmount"]) {
      nextField.focus();
    }
    //发票编号
    if (lastField == formRefs["invoiceNumber"]) {
      nextField.focus();
    }
    //发票金额
    if (lastField == formRefs["invoiceAmount"]) {
      const d = document.querySelector("#tjbutton") as HTMLElement;
      if (d) {
        d.focus();
      }
    }
  }
};
/**时间选择器改变时 */
const changeDate = () => {
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
function selectBtn(formEl: FormInstance | undefined) {
  //console.log(formEl, 'chaxun')
  if (!formData.startDate || !formData.endDate) {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "error" });
    return;
  } else if (checkDate(formData.startDate, formData.endDate)) {
    ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
    return;
  } else {
    if (!formEl) return;
    formEl?.resetFields();

    RefundingBillMasterList(); //主表查询
  }
  //clearn()
}
/** 获取主表接口 */
const RefundingBillMasterList = () => {
  tableLoading.value = true;
  tableLoadingRight.value = true;
  isAdd.value = true;
  opacityStyle.value = "";
  getRefundingBillMasterList(
    "0521",
    "101",
    formData.isInputOrCheckDate == "录单日期" ? "true" : "false",
    `${formData.startDate}${"00:00:00"}`,
    `${formData.endDate}${"23:59:59"}`,
    formData.checkId,
    formData.unreviewed == true ? "未审核" : "",
    formData.supplyName
  )
    .then((res: any) => {
      if (res.code === "200") {
        tableLeft.value = res.data.DefineRefundingBillMasterInfo;
        // console.log(tableLeft.value, '获取药品退货主单信息')
        tableLeftcurrentRow.value = tableLeft.value[0];
        if (tableLeftcurrentRow.value == null || tableLeftcurrentRow.value == "") {
          tableLeft.value = [];
          //没有查询到数据的时候 给右边表单赋值空
          NohandleChange();
        } else {
          // purchaseMoneyCount.value = res.data.purchaseMoney //退货进价总金额
          // retailMoneyCount.value = res.data.retailMoney //退货总金额
          // 高亮第一行
          nextTick(() => {
            loading.value = false;
            tableLeftRef.value.setCurrentRow(tableLeft.value[0]);
          });
          // RefundingBillDetailListWithDrawal()
        }

        tableLoading.value = false;
        tableLoadingRight.value = false;
      } else {
        tableLeft.value = [];
        NohandleChange();

        tableLoading.value = false;
        tableLoadingRight.value = false;
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    })
    .catch((err: any) => {
      tableLeft.value = [];
      NohandleChange();

      tableLoading.value = false;
      tableLoadingRight.value = false;
      ElMessage({ message: err.msg || err.message, type: "error" });
    });
};
/**没有查到数据时处理，表单赋空值 */
function NohandleChange() {
  // console.log('没有检索到数据')

  tableForm.supplyName = ""; //供货商

  //赋值给右边表单
  tableForm.drugCode = ""; //药品代码
  tableForm.drugSpecification = ""; //药品规格
  tableForm.origin = ""; //产地
  tableForm.validityPeriod = ""; //有效期
  tableForm.drugName = ""; //药品名称
  tableForm.inStorePrice = 0; //进价
  tableForm.retailPrice = 0; //零售价

  tableForm.inventory = 0; //库存量

  tableForm.batchNumber = ""; //批号
  tableForm.unitName = ""; //库房单位
  tableForm.batch = ""; //批次
  tableForm.number = 0; //退货数量
  tableForm.unitName = ""; //退货数量单位
  tableForm.expenditureAmount = ""; //实退金额
  tableForm.invoiceNumber = ""; //发票编号
  tableForm.invoiceDate = getNowDate(new Date(), "d"); //发票时间
  tableForm.invoiceAmount = ""; //发票金额

  chname.value = "";
  // //控制全部冲销复选框是否显示
  // checkZT(chname.value)
  //查询明细
  tableRight.value = [];
  // inStoremoney.value = 0 //进价总金额
  // retailmoney.value = 0 //零售总金额
  // purchaseMoneyCount.value = 0
  // retailMoneyCount.value = 0
  // countrkxx.value = 0 //退货数
  ElMessage({ message: "暂无数据!", type: "success" });
}
/** 转换时间 yee(d:当前 ,m：明年) */
function getNowDate(Dates: any, yee: string) {
  ////明年当前日期
  // console.log(Dates, 'Dates')
  if (yee == "d") {
    //年份
    const Year = Dates.getFullYear();
    //月份
    const Months = Dates.getMonth() + 1 < 10 ? "0" + (Dates.getMonth() + 1) : Dates.getMonth() + 1;
    //天数
    const Day = Dates.getDate() < 10 ? "0" + Dates.getDate() : Dates.getDate();
    return Year + "-" + Months + "-" + Day;
  } else if (yee == "m") {
    //明年当前日期
    //年份
    const Year = Dates.getFullYear() + 1;
    //月份
    const Months = Dates.getMonth() + 1 < 10 ? "0" + (Dates.getMonth() + 1) : Dates.getMonth() + 1;
    //天数
    const Day = Dates.getDate() < 10 ? "0" + Dates.getDate() : Dates.getDate();
    return Year + "-" + Months + "-" + Day;
  }
}
/** 点击新增按钮 */
function clickInsert() {
  isAdd.value = false;
  isshzt.value = false;
  isxz.value = false;
  // ghskz.value = false      //供货商控制？？？
  opacityStyle.value = "opacity:0.3";
  // countrkxx.value = 0 //退货数？？
  tableRight.value = [];
  tableLeftcurrentRow.value = ""; //清空选中左边数据，防止误删
  tableRightcurrentRow.value = ""; //清空选中右边数据，防止误删
  tableForm.supplyName = ""; //供货商

  //赋值给右边表单
  tableForm.drugCode = ""; //药品代码
  tableForm.drugSpecification = ""; //药品规格
  tableForm.origin = ""; //产地
  tableForm.validityPeriod = ""; //有效期
  tableForm.drugName = ""; //药品名称
  tableForm.inStorePrice = 0; //进价
  tableForm.retailPrice = 0; //零售价

  tableForm.inventory = 0; //库存量

  tableForm.batchNumber = ""; //批号
  tableForm.unitName = ""; //库房单位
  tableForm.batch = ""; //批次
  tableForm.number = 0; //退货数量
  tableForm.unitName = ""; //退货数量单位
  tableForm.expenditureAmount = ""; //实退金额
  tableForm.invoiceNumber = ""; //发票编号
  tableForm.invoiceDate = getNowDate(new Date(), "d"); //发票时间
  tableForm.invoiceAmount = ""; //发票金额

  formRefs["supplyName"].focus();
  //formRefs['inStoreTypeName'].visible = true
  //formRefs['inStoreTypeName'].focus()
}
/** 点击保存按钮 */
function clickSave() {
  if (zujian.value == "Add") {
    if (tableRight.value == null || tableRight.value.length == 0) {
      ElMessageBox.alert("</el-icon><span>请添加退货药品明细信息!</span> ", "提示消息", {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认",
        type: "warning"
      });
    } else {
      if (isxz.value == false) {
        tableLeft.value.unshift({
          id: "",
          orgaID: "0521", //机构id
          deptID: "101", //部门id
          deptInputCode: "XYK", //部门输入码
          deptCode: "001", //部门代码
          deptName: "西药库", //部门名称
          checkDate: "", //审核时间
          checkID: "", //审核员Id
          billNo: "", //退货单号
          publicSignID: "", //
          publicSignName: "",
          inStoreTypeID: "",
          inStoreTypeName: "",
          expenditureAmount: tableForm.number * tableForm.inStorePrice, //实退金额
          supplierId: tableForm.supplierId, //供货商id
          supplyInputCode: tableForm.supplyInputCode, //供货商输入码
          supplyCode: tableForm.supplyCode, //供货商代码
          supplyName: tableForm.supplyName, //供货商
          refundingMemo: "",
          isActive: "在用", //是否在用
          stopDate: "", //停用日期
          status: "未审核", //状态
          statusColor: "", //状态颜色
          operatorID: "",
          inputDate: getNowDate(new Date(), "d"),
          createUser: "yang", //创建人id
          createDate: getNowDate(new Date(), "d"),
          comment: "",
          checkName: "yang", //审核人姓名
          reverseStatus: "未冲销",
          invoiceNumber: tableForm.invoiceNumber, //发票编号
          invoiceDate: tableForm.invoiceDate, //发票时间
          invoiceAmount: tableForm.invoiceAmount, //发票金额
          purchaseMoney: tableForm.inStorePrice, //进货价
          retailMoney: tableForm.retailPrice, //零售价
          operatorId: "yang" //修改人id
        });
      }
      isAdd.value = true;
      isshzt.value = true;

      // console.log(zujian.value, '保存判断add还是update')
      saveRefundingBillMasterNew(zujian.value);
    }
  } else if (zujian.value == "Update") {
    isAdd.value = true;
    isshzt.value = true;
    // console.log(zujian.value, '保存判断add还是update')
    saveRefundingBillMasterNew(zujian.value);
  } else {
    ElMessageBox.alert("</el-icon><span>共有0条数据被修改!</span> ", "提示消息", {
      dangerouslyUseHTMLString: true,
      confirmButtonText: "确认",
      type: "warning"
    });
    return;
  }
}
/**保存主表/明细表接口 */
const saveRefundingBillMasterNew = (tit: string) => {
  if (tit == "Add") {
    // console.log('进入新增保存')

    // console.log(isxz.value, tableLeftcurrentRow.value, 'isxz')
    getsaveRefundingBillMasterNew(
      isxz.value == false ? tableLeft.value[0] : tableLeftcurrentRow.value,
      tableRight.value,
      tit
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: "退货单保存成功!", type: "success" });
        RefundingBillMasterList();
        //  console.log('保存退货单保存成功')
        zujian.value = "";
        isxz.value = true;
      } else {
        zujian.value = "";
        isxz.value = true;

        ElMessage({ message: res.msg || res.message, type: "error" });
        RefundingBillMasterList();
      }
    });
  } else if (tit == "Update") {
    // console.log('进入修改保存')

    getsaveRefundingBillMasterNew(tableLeftcurrentRow.value, tableRight.value, tit).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: "退货单保存成功!", type: "success" });
        RefundingBillMasterList();
        //console.log('保存退货单保存成功')
        zujian.value = "";
      } else {
        zujian.value = "";
        ElMessage({ message: res.msg || res.message, type: "error" });

        RefundingBillMasterList();
      }
    });
  }
};
/** 点击作废退货单按钮， 调接口 */
const wasterRefundingBillMaster = () => {
  if (tableLeft.value.length > 0) {
    ElMessageBox.confirm(`是否停用单号为【${tableLeftcurrentRow.value.billNo}】 的退货单!`, "提示消息", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(() => {
      getwasterRefundingBillMaster(
        "0521",
        "101",
        tableLeftcurrentRow.value.id, //主表id
        "yang" //用户id
      ).then((res: any) => {
        if (res.code === "200") {
          ElMessage({ message: "退货单作废成功!", type: "success" });
          RefundingBillMasterList();
        } else {
          ElMessage({ message: res.msg || res.message, type: "error" });
        }
      });
    });
  } else {
    ElMessage({ message: "请选择需要作废的数据!", type: "warning" });
  }
};
/** 点击审核按钮 */
const InStoreBill = () => {
  if (tableLeft.value.length > 0) {
    ElMessageBox.confirm(
      `是否审核单号为【${tableLeftcurrentRow.value.billNo}】,  发票号 【${tableLeftcurrentRow.value.invoiceNumber}】 的退货单!`,
      "提示消息",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    ).then(() => {
      //审核接口
      getStoreBill(); //调接口
    });
  } else {
    ElMessage({ message: "请选择需要审核的数据!", type: "warning" });
  }
};
/**调审核接口 */
function getStoreBill() {
  getcheckRefundingBill(
    "0521",
    "yang", //用户id
    tableLeftcurrentRow.value.id //主表id
  ).then((res: any) => {
    if (res.code === "200") {
      ElMessage({ message: "退货单审核成功!", type: "success" });

      RefundingBillMasterList();
    } else {
      ElMessage({ message: res.msg || res.message, type: "error" });
    }
  });
}
/** 点击冲销按钮 */
//循环判断药品信息是否重复添加
const tjcf = ref("true");
/** 点击新增明细按钮 */
const addDetail = async (formEl: FormInstance | undefined) => {
  tjcf.value = "true";
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      if (isxz.value == false) {
        zujian.value = "Add";
      } else {
        zujian.value = "Update";
      }

      // console.log(tableForm.number, tableForm.inventory, '库存量')
      tableRight.value.forEach((item: any) => {
        if (item.drugCode == tableForm.drugCode) {
          tjcf.value = "false";
        }
      });

      if (tjcf.value == "false") {
        ElMessageBox.alert(
          "</el-icon><span>药品已存在!</span> ",
          "提示消息",

          {
            dangerouslyUseHTMLString: true,
            confirmButtonText: "确认",
            type: "warning"
          }
        ).then(() => {
          nextTick(() => {
            formRefs["drugCode"].focus();
          });
        });
      } else {
        if (tableForm.number > tableForm.inventory) {
          ElMessageBox.alert(
            "</el-icon><span>退货数量不能大于库存量!</span> ",
            "提示消息",

            {
              dangerouslyUseHTMLString: true,
              confirmButtonText: "确认",
              type: "warning"
            }
          );
        } else {
          if (Number(tableForm.number) > 0) {
            adddetailInfo();
          } else {
            ElMessageBox.alert(
              "</el-icon><span>退货数量必须大于0 !</span> ",
              "提示消息",

              {
                dangerouslyUseHTMLString: true,
                confirmButtonText: "确认",
                type: "warning"
              }
            );
          }
        }
      }
    }
  });
};
/**给明细表添加记录 */
function adddetailInfo() {
  //右边表格
  tableRight.value.push({
    id: "", //明细id
    orgaID: "0521", //机构id
    refundingBillID: "", //退货主表Id
    billNo: "", //退货单号
    drugID: tableForm.drugId, //药品id
    orgaDrugID: tableForm.orgaDrugId, //机构药品id
    drugInputCode: tableForm.drugInputCode, //药品输入码
    drugInputCode2: tableForm.drugInputCode2, //药品输入码2
    drugInputCode3: tableForm.drugInputCode3, //药品输入码3
    drugCode: tableForm.drugCode, //药品代码
    drugName: tableForm.drugName, //药品名称
    drugSpecification: tableForm.drugSpecification, //规格
    origin: tableForm.origin, //产地
    supplierID: tableForm.supplierId, //供货商id
    supplyInputCode: tableForm.supplyInputCode, //供货商输入码
    supplyCode: tableForm.supplyCode, //供货商代码
    supplyName: tableForm.supplyName, //供货商
    number: tableForm.number, //退货数量
    unitID: tableForm.stockPackagUnitId, //单位id
    unitName: tableForm.unitName, //入库数量单位
    packagQuantity: tableForm.stockPackagQuantity, //包装量
    expenditureAmount: tableForm.expenditureAmount, //实退金额
    purchasePrice: tableForm.inStorePrice, //进价
    retailPrice: tableForm.retailPrice, //零售价
    purchaseMoney: tableForm.inStorePrice * tableForm.number, //批发金额
    retailMoney: tableForm.retailPrice * tableForm.number, //零售金额
    position: tableForm.position, //仓位
    batch: tableForm.batch,
    batchNumber: tableForm.batchNumber, //批号
    validityPeriod: tableForm.validityPeriod, //效期
    isActive: "在用", //是否在用
    stopDate: "", //停用日期
    status: "未审核", //状态
    operatorId: "yang", //修改人id
    inputDate: getNowDate(new Date(), "d"),
    createUser: "yang", //创建人id
    createDate: getNowDate(new Date(), "d"),
    comment: "", //备注
    dwAmount: "", //冲销数量
    invoiceNumber: "",
    invoiceDate: "",
    invoiceAmount: "",
    purchaseRate: ""
  });
  // countrkxx.value = tableRight.value.length
  // inStoremoney.value = 0
  // retailmoney.value = 0
  // tableRight.value.forEach((item: any) => {
  //   inStoremoney.value = inStoremoney.value + item.purchaseMoney //进价总金额
  //   retailmoney.value = retailmoney.value + item.retailMoney //零售总金额
  // })
  //添加右边明细进去  清空数据
  rtableform();
  //定位药品代码焦点
  //定位药品代码焦点
  nextTick(() => {
    // DrugcodeInfoList.value = []
    formRefs["drugCode"].focus();
  });
}
/** 表单数据添加到表格后，清空表单数据 */
function rtableform() {
  ghskz.value = true;
  //tableForm.supplyName = '' //供货商
  //赋值给右边表单
  tableForm.drugCode = ""; //药品代码
  tableForm.drugSpecification = ""; //药品规格
  tableForm.origin = ""; //产地
  tableForm.validityPeriod = ""; //有效期
  tableForm.drugName = ""; //药品名称
  tableForm.inStorePrice = 0; //进价
  tableForm.retailPrice = 0; //零售价

  tableForm.inventory = 0; //库存量

  tableForm.batchNumber = ""; //批号
  tableForm.unitName = ""; //库房单位
  tableForm.batch = ""; //批次
  tableForm.number = 0; //退货数量
  tableForm.unitName = ""; //退货数量单位
  tableForm.expenditureAmount = ""; //实退金额
  tableForm.invoiceNumber = ""; //发票编号
  tableForm.invoiceDate = getNowDate(new Date(), "d"); //发票时间
  tableForm.invoiceAmount = ""; //发票金额
}
/** 点击修改明细按钮 */
const upDetail = async (formEl: FormInstance | undefined, tit: string) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      //循环右边表格数据进行修改
      zujian.value = tit;
      //赋值给选中的数据
      //tableRightcurrentRow.value.supplyName = tableForm.supplyName //供货商
      tableRightcurrentRow.value.number = tableForm.number; //退货数量
      tableRightcurrentRow.value.expenditureAmount = tableForm.expenditureAmount; //实退金额
      tableRightcurrentRow.value.inputDate = getNowDate(new Date(), "d"); //更新时间
      tableRightcurrentRow.value.createDate = ""; //创建时间
      tableRightcurrentRow.value.validityPeriod = formatDate(tableForm.validityPeriod); //有效期
      tableRightcurrentRow.value.validate = "";

      if (tableLeftcurrentRow.value != null && tableLeftcurrentRow.value != "") {
        tableLeftcurrentRow.value.invoiceNumber = tableForm.invoiceNumber; //发票编号
        tableLeftcurrentRow.value.invoiceDate = tableForm.invoiceDate; //发票时间
        tableLeftcurrentRow.value.invoiceAmount = tableForm.invoiceAmount; //发票金额
        tableLeftcurrentRow.value.expenditureAmount = tableForm.expenditureAmount; //实退金额
        tableLeftcurrentRow.value.inputDate = getNowDate(new Date(), "d");
      }
    }
  });
};
/** 点击删除明细按钮, 调接口 */
const wasterRefundingBillDetail = () => {
  const isOld = ref(false); //删除的数据是否为数据库内的数据
  if (tableLeftcurrentRow.value != "") {
    //console.log(tableRightcurrentRow.value,'进来删除id')

    isOld.value = true;
    getwasterRefundingBillDetail(
      "0521",
      tableLeftcurrentRow.value.id, //主表id
      tableRightcurrentRow.value.id, //明细id
      "yang" //用户id
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: "退货单明细删除成功!", type: "success" });
        RefundingBillMasterList();
        //RefundingBillDetailListWithDrawal()
      } else {
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    });
  }

  if (!isOld.value) {
    //console.log(tableRightcurrentRow.value,'进来没有id 的数据')

    if (tableRight.value.length > 0) {
      tableRight.value.forEach((item: any, index: any) => {
        if (tableRightcurrentRow.value.drugCode == item.drugCode) {
          tableRight.value.splice(index, 1);
        }
      });
    }
    // 清空数据
    rtableform();
    nextTick(() => {
      tableRightRef.value.setCurrentRow(tableRight.value[0]);
    });
    // countrkxx.value = tableRight.value.length
    // inStoremoney.value = 0
    // retailmoney.value = 0
    // if (tableRight.value.length > 0) {
    //   tableRight.value.forEach((item: any) => {
    //     inStoremoney.value = inStoremoney.value + item.purchaseMoney //进价总金额
    //     retailmoney.value = retailmoney.value + item.retailMoney //零售总金额
    //   })
    // }
  }
};
/** 输入药品代码后回车，查询药品信息,赋值给后面几个表单 */
const handleDrugCode = () => {
  getDrugInfoInStoreBatchByCode(tableForm.drugCode)
    .then((res: any) => {
      if (res?.code == "200" && res.data != null) {
        tableForm.drugId = res.data.drugId;
        tableForm.drugCode = res.data.drugCode;
        tableForm.drugName = res.data.drugName;
        tableForm.drugSpecification = res.data.drugSpecification;
        tableForm.origin = res.data.origin;
        tableForm.validityPeriod = res.data.validityPeriod; //
        tableForm.inStorePrice = res.data.purchasePrice; //进价
        tableForm.retailPrice = res.data.retailPrice; //零售价
        tableForm.inventory = res.data.priceNumber; // 库存量
        tableForm.unitName = res.data.unitName;
        tableForm.batchNumber = res.data.batchNumber;
        tableForm.batch = res.data.batch;
        tableForm.supplyName = res.data.supplyName;
        tableForm.supplierId = res.data.supplyId;
        tableForm.supplyCode = res.data.supplyCode;
        tableForm.supplyInputCode = res.data.supplyInputCode;
        tableForm.position = res.data.position;
        // tableForm.catalogId = res.data.catalogId;
        // tableForm.lastPurchasePrice = res.data.lastPurchasePrice;
        // tableForm.lastRetailPrice = res.data.lastRetailPrice;
        // console.log(tableForm, "药品信息");
      } else {
        ElMessage({ message: "未查询到该药品信息！", type: "error" });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
/** 点击主表当前行改变时 */
const tableLefthandleChange = (val: any) => {
  tableLeftcurrentRow.value = val;

  //console.log(tableLeftcurrentRow.value, '左边第一行数据')
  if (tableLeftcurrentRow.value == null) {
  } else {
    //点击新增则不给详细表赋值
    if (isAdd.value) {
      tableForm.invoiceNumber = tableLeftcurrentRow.value.invoiceNumber; //发票编号
      tableForm.invoiceDate = tableLeftcurrentRow.value.invoiceDate; //发票时间
      tableForm.invoiceAmount = tableLeftcurrentRow.value.invoiceAmount; //发票金额
      chname.value = tableLeftcurrentRow.value.status;
      // checkZT(chname.value)
      //查询明细
      RefundingBillDetailListWithDrawal(); //加载明细表数据
    } else {
      tableRight.value = [];
      tableLeftRef.value.setCurrentRow();
    }
  }
};
/**加载明细表数据 -接口 */
const RefundingBillDetailListWithDrawal = () => {
  tableLoadingRight.value = true;

  getRefundingBillDetailListWithDrawal("0521", "", tableLeftcurrentRow.value.id, "")
    .then((res: any) => {
      if (res.code === "200") {
        tableRight.value = res.data.DefineRefundingBillDetailInfoWithDrawal;
        //  console.log(tableRight.value, '获取药品退货明细信息')
        tableRightcurrentRow.value = tableRight.value[0];
        if (tableRightcurrentRow.value == null || tableRightcurrentRow.value == "") {
          //没有查询到数据的时候
          tableRight.value = [];
          NohandleChange();
        } else {
          // countrkxx.value = res.data.count //出库数
          // inStoremoney.value = res.data.purchaseMoney //进价总金额
          // retailmoney.value = res.data.retailMoney //零售总金额
          // 高亮第一行
          nextTick(() => {
            tableRightRef.value.setCurrentRow(tableRight.value[0]);
          });
        }

        tableLoadingRight.value = false;
      } else {
        // console.log(tableRightcurrentRow.value, '退货单明细信息报错77878777')
        tableRight.value = [];
        //没有查询到数据的时候 给右边表单赋值空
        NohandleChange();

        tableLoadingRight.value = false;
      }
    })
    .catch((err: any) => {
      tableRight.value = [];
      NohandleChange();

      tableLoadingRight.value = false;
    });
};
/** 点击右侧表格当前行改变时，赋值给上方表单 */
const tableRighthandleChange = (val: any) => {
  tableRightcurrentRow.value = val;
  //console.log(tableRightcurrentRow.value, '右边第一行数据')

  if (tableRightcurrentRow.value == null) {
  } else {
    //赋值给右边表单
    tableForm.supplyName = tableRightcurrentRow.value.supplyName; //供货商
    tableForm.drugCode = tableRightcurrentRow.value.drugCode; //药品代码
    tableForm.drugSpecification = tableRightcurrentRow.value.drugSpecification; //药品代码
    tableForm.origin = tableRightcurrentRow.value.origin; //产地
    tableForm.validityPeriod = tableRightcurrentRow.value.validityPeriod; //有效期
    tableForm.drugName = tableRightcurrentRow.value.drugName; //药品名称
    tableForm.inStorePrice = tableRightcurrentRow.value.purchasePrice; //进价
    tableForm.retailPrice = tableRightcurrentRow.value.retailPrice; //零售价

    tableForm.batchNumber = tableRightcurrentRow.value.batchNumber; //批号
    tableForm.unitName = tableRightcurrentRow.value.unitName; //库房单位
    tableForm.batch = tableRightcurrentRow.value.batch; //批次
    tableForm.number = tableRightcurrentRow.value.number; //退货数量
    tableForm.unitName = tableRightcurrentRow.value.unitName; //退货数量单位
    tableForm.expenditureAmount = tableRightcurrentRow.value.expenditureAmount; //实退金额
  }
};
const tableDatadcxml: any = ref([]); //导出的列表
/** 点击导出按钮 */
const exportXml = throttle(() => {
  tableDatadcxml.value = [];
  if (tableRight.value.length > 0) {
    //  查询明细数据
    getRefundingBillDetailListWithDrawal("0521", "", tableLeftcurrentRow.value.id, "")
      .then((res: any) => {
        if (res.code === "200") {
          tableDatadcxml.value = res.data.DefineRefundingBillDetailInfoWithDrawal;
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
    "药品名称",
    "规格",
    "产地",
    "供应商",
    "退货数量",
    "单位",
    "实退金额",
    "进价",
    "零售价",
    "进价金额",
    "零售金额",
    "批号",
    "效期",
    "批次"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    item.drugCode,
    item.drugName,
    item.drugSpecification,
    item.origin,
    item.supplyName,
    item.number,
    item.unitName,
    item.expenditureAmount,
    item.purchasePrice,
    item.retailPrice,
    item.purchaseMoney,
    item.retailMoney,
    item.batchNumber,
    item.validityPeriod,
    item.batch
  ]);
  //标题不要超过30个字符，否则报错
  exportDrugExpiryTable(headersArr, data, "浦东测试机构西药库" + "退库单" + tableLeftcurrentRow.value.billNo + "信息");
}
/** 点击冲销按钮 */
const newReverseRefundingBill = () => {
  if (tableLeft.value.length > 0) {
    getnewReverseRefundingBill(
      "0521",
      "101",
      tableLeftcurrentRow.value.id, //主表id
      "yang" //用户id
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: "冲销成功!", type: "success" });
        RefundingBillMasterList();
        RefundingBillDetailListWithDrawal();
      } else {
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    });
  } else {
    ElMessage({ message: "请选择需要冲销的数据!", type: "error" });
  }
};
</script>
<template>
  <div id="main" tabindex="0">
    <div class="form">
      <el-form ref="infoFormRef" inline label-width="auto" :model="formData">
        <el-row>
          <el-col class="col" :span="10" style="display: flex">
            <el-form-item style="width: 39%">
              <el-radio-group v-model="formData.isInputOrCheckDate" class="ml-4">
                <el-radio label="录单日期"> 录单日期 </el-radio>
                <el-radio label="审核日期"> 审核日期 </el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item style="width: 60%">
              <el-date-picker
                v-model="formData.startDate"
                format="YYYY-MM-DD"
                placeholder="开始时间"
                style="flex: 1"
                type="date"
                value-format="YYYY-MM-DD "
                @change="changeDate"
              />
              至
              <el-date-picker
                v-model="formData.endDate"
                format="YYYY-MM-DD "
                placeholder="结束时间"
                style="flex: 1"
                type="date"
                value-format="YYYY-MM-DD "
                @change="changeDate"
              />
            </el-form-item>
          </el-col>
          <el-col class="col" :span="4">
            <el-form-item label="审核人" style="width: 97%; margin-left: 10px">
              <el-select ref="checkname" v-model="formData.checkId" clearable placeholder="请输入审核人姓名...">
                <el-option label="张三" value="yang"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="4">
            <el-form-item label="供货商" style="width: 97%; margin-left: 10px">
              <el-input ref="supply" v-model="formData.supplyName" clearable placeholder="请输入供货商代码\拼音..."> </el-input>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="2"></el-col>
          <el-col class="col" :span="4" style="display: flex">
            <el-form-item style="width: 50%">
              <el-checkbox v-model="formData.unreviewed" label="未审核" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="button">
      <div style="display: inline-block">
        <el-button class="buttonStyle" @click="selectBtn(TableForm)">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/查询.png" />
          查询(F5)
        </el-button>
        <el-button class="buttonStyle" :disabled="!isAdd" @click="clickInsert()">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          新增退货单(F2)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="clickSave()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/保存.png" />
          保存(F3)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="wasterRefundingBillMaster()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/作废.png" />
          作废退货单(F4)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="InStoreBill()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/审核.png" />
          审核(F8)
        </el-button>

        <el-button class="buttonStyle" :disabled="chname == '已审核' ? false : true" @click="exportXml()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/打印.png" />
          导出(F7)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? false : true" @click="newReverseRefundingBill()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/冲销.png" />
          冲销
        </el-button>
        <el-button
          :ref="el => (formRefs['tjbutton'] = el)"
          id="tjbutton"
          class="buttonStyle"
          :disabled="chname == '已审核' ? isshzt : false"
          @click="addDetail(TableForm)"
          @keyup="henderEnter($event, formRefs['tjbutton'], formRefs['drugCode'])"
        >
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          添加药品(Ctrl+N)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="upDetail(TableForm, 'Update')">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/修改.png" />
          修改(Ctrl+M)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="wasterRefundingBillDetail()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/删除.png" />
          删除药品(DEL)
        </el-button>
      </div>
    </div>
    <div class="table">
      <div class="tableLeft" :style="opacityStyle">
        <el-table
          ref="tableLeftRef"
          border
          :data="tableLeft"
          v-loading="tableLoading"
          highlight-current-row
          stripe
          style="height: 100%"
          @current-change="tableLefthandleChange"
        >
          <el-table-column align="center" label="状态" prop="status" min-width="140px" show-overflow-tooltip sortable>
            <template #default="{ row }">
              <span :class="row.status === '未审核' ? 'fontStyle' : ''">
                {{ row.status }}
              </span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="退货单号" min-width="140px" show-overflow-tooltip sortable prop="billNo" />
          <el-table-column align="center" label="实退金额" min-width="140px" show-overflow-tooltip prop="expenditureAmount" />
          <el-table-column align="center" label="发票编号" min-width="140px" show-overflow-tooltip prop="invoiceNumber" />
          <el-table-column align="center" label="发票金额" min-width="140px" show-overflow-tooltip prop="invoiceAmount" />
          <el-table-column align="center" label="发票时间" min-width="140px" show-overflow-tooltip prop="invoiceDate">
            <template #default="{ row }">
              {{ formatDate(row.invoiceDate) }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="冲销状态" min-width="140px" show-overflow-tooltip prop="reverseStatus" />
          <el-table-column align="center" label="录单日期" min-width="140px" show-overflow-tooltip sortable prop="createDate" />
          <el-table-column align="center" label="审核日期" min-width="140px" show-overflow-tooltip sortable prop="checkDate" />
          <el-table-column align="center" label="审核人" min-width="140px" show-overflow-tooltip prop="checkName" />
        </el-table>
      </div>
      <div class="tableRight">
        <div class="tableForm">
          <el-form ref="TableForm" inline label-width="auto" :model="tableForm" :rules="rules">
            <!-- 第一行 -->
            <el-row>
              <el-col class="col" :span="12">
                <el-form-item label="供货商" :inline-message="true" prop="supplyName" label-position="right" label-width="82px">
                  <el-input
                    :ref="el => (formRefs['supplyName'] = el)"
                    v-model="tableForm.supplyName"
                    :disabled="chname == '已审核' ? isshzt : false"
                    style="width: 100%"
                    placeholder="请输入供货商代码\拼音..."
                    @keyup="henderEnter($event, formRefs['supplyName'], formRefs['supplyName'])"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="药品代码" :inline-message="true" prop="drugCode" label-position="right" label-width="82px">
                  <el-input
                    :ref="el => (formRefs['drugCode'] = el)"
                    v-model="tableForm.drugCode"
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入药品代码\拼音..."
                    @keyup.enter="handleDrugCode"
                    @keyup="henderEnter($event, formRefs['drugCode'], formRefs['drugCode'])"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6" prop="drugSpecification">
                <el-form-item label="规格" label-position="right" label-width="82px">
                  <el-input v-model="tableForm.drugSpecification" clearable disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="产地" prop="origin" label-position="right" label-width="82px">
                  <el-input ref="chandi" v-model="tableForm.origin" disabled placeholder="请输入产地代码\拼音..."> </el-input>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="效期" :inline-message="true" prop="validityPeriod" label-position="right" label-width="82px">
                  <div style="display: flex; flex-direction: row; width: 100%">
                    <el-date-picker
                      ref="erpicker"
                      v-model="tableForm.validityPeriod"
                      class="chushengriqi"
                      placeholder="选择日期"
                      style="width: 100%"
                      disabled
                      type="date"
                    />
                  </div>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="12">
                <el-form-item label="药品名称" prop="drugName" label-position="right" label-width="82px">
                  <el-input v-model="tableForm.drugName" clearable disabled />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="进价" :inline-message="true" prop="inStorePrice" label-position="right" label-width="82px">
                  <el-input
                    v-model="tableForm.inStorePrice"
                    clearable
                    disabled
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1','')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="零售价" prop="retailPrice" :inline-message="true" label-position="right" label-width="82px">
                  <el-input
                    v-model="tableForm.retailPrice"
                    clearable
                    disabled
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1','')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="库存量" prop="inventory" label-position="right" label-width="82px">
                  <el-input v-model="tableForm.inventory" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="批号" :inline-message="true" prop="batchNumber" label-position="right" label-width="82px">
                  <el-input
                    v-model="tableForm.batchNumber"
                    clearable
                    placeholder="请输入非中文..."
                    oninput="this.value=this.value.replace(/[^\w]/g,'')"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="库房单位" prop="unitName" label-position="right" label-width="82px">
                  <el-input v-model="tableForm.unitName" clearable disabled placeholder="请输入..." />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="批次" :inline-message="true" prop="batch" label-position="right" label-width="82px">
                  <el-input
                    v-model="tableForm.batch"
                    clearable
                    placeholder="请输入非中文..."
                    oninput="this.value=this.value.replace(/[^\w]/g,'')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item
                  style="float: left; width: 75%"
                  label="退货数量"
                  :inline-message="true"
                  prop="number"
                  label-position="right"
                  label-width="82px"
                >
                  <el-input
                    :ref="el => (formRefs['number'] = el)"
                    v-model="tableForm.number"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入正整数..."
                    oninput="this.value=value.replace(/[^\d]/g,'')"
                    @keyup="henderEnter($event, formRefs['number'], formRefs['expenditureAmount'])"
                  />
                </el-form-item>
                <el-form-item style="float: left; width: 25%" prop="unitName" label-position="right" label-width="82px">
                  <el-select v-model="tableForm.unitName" class="rkdw" disabled placeholder=" ">
                    <el-option label="" value="" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="实退金额" prop="expenditureAmount" label-position="right" label-width="82px">
                  <el-input
                    :ref="el => (formRefs['expenditureAmount'] = el)"
                    v-model="tableForm.expenditureAmount"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1','')"
                    @keyup="henderEnter($event, formRefs['expenditureAmount'], formRefs['invoiceNumber'])"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第五行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item
                  label="发票编号"
                  :inline-message="true"
                  prop="invoiceNumber"
                  label-position="right"
                  label-width="82px"
                >
                  <el-input
                    :ref="el => (formRefs['invoiceNumber'] = el)"
                    v-model="tableForm.invoiceNumber"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入非中文..."
                    oninput="this.value=this.value.replace(/[^\w]/g,'')"
                    @keyup="henderEnter($event, formRefs['invoiceNumber'], formRefs['invoiceAmount'])"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="发票金额" prop="invoiceAmount" label-position="right" label-width="82px">
                  <el-input
                    :ref="el => (formRefs['invoiceAmount'] = el)"
                    v-model="tableForm.invoiceAmount"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    @keyup="henderEnter($event, formRefs['invoiceAmount'], formRefs['tjbutton'])"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="发票时间" prop="invoiceDate" label-position="right" label-width="82px">
                  <div style="display: flex; flex-direction: row; width: 100%">
                    <el-date-picker
                      ref="yipicker"
                      v-model="tableForm.invoiceDate"
                      class="chushengriqi"
                      placeholder="选择日期"
                      style="width: 100%"
                      :disabled="chname == '已审核' ? isshzt : false"
                      type="date"
                    />
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <el-table
          border
          ref="tableRightRef"
          class="rightTableDStyle"
          :data="tableRight"
          v-loading="tableLoadingRight"
          highlight-current-row
          stripe
          @current-change="tableRighthandleChange"
        >
          <el-table-column align="center" label="药品代码" min-width="140px" show-overflow-tooltip sortable prop="drugCode" />
          <el-table-column align="center" label="药品名称" min-width="140px" show-overflow-tooltip sortable prop="drugName" />
          <el-table-column align="center" label="规格" min-width="140px" show-overflow-tooltip prop="drugSpecification" />
          <el-table-column align="center" label="产地" min-width="140px" show-overflow-tooltip prop="origin" />
          <el-table-column align="center" label="供应商" min-width="140px" show-overflow-tooltip prop="supplyName" />

          <el-table-column align="center" label="退货数量" min-width="140px" show-overflow-tooltip sortable prop="number" />
          <el-table-column align="center" label="单位" min-width="140px" show-overflow-tooltip prop="unitName" />
          <el-table-column align="center" label="实退金额" min-width="140px" show-overflow-tooltip prop="expenditureAmount" />
          <el-table-column align="center" label="进价" min-width="140px" show-overflow-tooltip prop="purchasePrice" />
          <el-table-column align="center" label="零售价" min-width="140px" show-overflow-tooltip prop="retailPrice" />
          <el-table-column align="center" label="进价金额" min-width="140px" show-overflow-tooltip prop="purchaseMoney" />
          <el-table-column align="center" label="零售金额" min-width="140px" show-overflow-tooltip prop="retailMoney" />
          <el-table-column align="center" label="批号" min-width="140px" show-overflow-tooltip sortable prop="batchNumber" />
          <el-table-column align="center" label="效期" min-width="140px" show-overflow-tooltip sortable prop="validityPeriod">
            <template #default="{ row }">
              {{ formatDate(row.validityPeriod) }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="批次" min-width="140px" show-overflow-tooltip prop="batch" />
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
  .buttonImg {
    width: 12px;
    margin-right: 5px;
  }
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
    width: 69% !important;
    height: calc(100vh - 270px) !important;
    margin-left: 0.5%;
    //   border: #41b584 1px solid;
    //   右边表单
    .tableForm {
      width: 100%;
      padding: 10px;
      margin-bottom: 10px;
      background: white;
      // height: calc(100vh - 290px) !important;
      .col {
        display: flex;
        padding-left: 1%;
        padding-top: 0.4%;
      }
    }
    :deep(.el-form-item .el-form-item__label-wrap) {
      margin-left: 0px !important;
    }
    :deep(.el-form-item__label:before) {
      padding-top: 3px;
      font-size: 1.25rem;
    }

    //常规
    .rightTableDStyle {
      height: calc((100vh - 479px));
    }
  }
}

.fontStyle {
  font-weight: 700;
  color: #dd5861;
}

//去除表格输入框的边框
:deep(.inputStyle) {
  padding: 0;
  .el-input__wrapper {
    box-shadow: none;
    background-color: transparent;
  }
}
</style>
