<script setup lang="ts">
import {
  checkInStoreBill,
  getaddDrugWithdrawalInfo,
  getDrugInfoByCode,
  getInStoreBillDetailListWithDrawal,
  getInStoreBillMasterList,
  getwasterInStoreBill,
  getwasterInStoreBillDetail
} from "@/api/drugAdministration/drugStorageManagement";
import { checkDate, getDefaultQueryDates, isEndTimeMidnight } from "@/utils/drugAdministration";
import { exportDrugExpiryTable } from "@/utils/drugAdministration/excel";
import { throttle } from "echarts";
import { dayjs, ElMessage, ElMessageBox, ElTable, FormInstance, FormRules } from "element-plus";
import { nextTick, onMounted, reactive, ref } from "vue";
const labelWidth = ref("80px"); //页面样式
const dateList = ref<[string, string]>(getDefaultQueryDates());
const TableForm: any = ref<FormInstance>(); //右侧表格-表单REF
const isdy = ref(true); //控制能否打印，true不能打印
const isAdd = ref(true); //能否点击新增入库单(能：false，不能：true)
const chname: any = ref(""); //左边表格当前行的审核状态
/** 右侧按钮和表单是否“不可用” */
const isshzt = ref(true); //控制右边表单是否显示, 左侧选择当前行为已审核时， true：禁用  false：可用
const zujian = ref(""); // Add / Update, 判断保存操作是添加还是修改
const tableLeft: any = ref([]); //左边总表
const tableRight: any = ref([]); //右边明细表
const opacityStyle = ref(""); //主表的禁用样式
const tableLoading = ref(false); //表格是否处于加载状态
const tableLeftRef: any = ref<InstanceType<typeof ElTable>>(); //高亮
const tableLeftcurrentRow: any = ref(""); //左表当前行
const tableLoadingright = ref(false); //右侧把表格是否处于加载状态
const tableRightRef: any = ref<InstanceType<typeof ElTable>>(); // 右表 高亮
const tableRightcurrentRow: any = ref(""); //右表当前行
const tablexz: any = ref([]); //明细添加保存的表
const tablexg: any = ref([]); //明细修改保存的表
const isxz = ref(true); //判断是否对左边表格增加行
//入库单保存接口相关
const kzList = ref([]); //传空值的时候
const tableptrk = ref([]); //自定义通过采购平台生成入库单需要的其他信息
const bcpd = ref(""); //保存判断是新增还是修改
const isWithSameDrug = ref("否"); //？？
////添加药品相关
const DrugcodeInfoList = ref([]);
/** 入库类型-列表 */
const storageTypeList: any = ref([
  { id: "1", kvmemo: "票货相符" },
  { id: "2", kvmemo: "票货不符" },
  { id: "3", kvmemo: "赠送" }
]);
/** 招标类型-列表 */
const tenderTypeList: any = ref([
  { id: 1, kvmemo: "省招采购" },
  { id: 2, kvmemo: "市招采购" },
  { id: 3, kvmemo: "零星采购" }
]);
/** 查询表单 */
const formData: any = reactive({
  isInputOrCheckDate: "录单日期", //日期类型(录单日期/审核日期)  false:录单  true：审核
  startDate: dateList?.value[0] + " ", //日期-开始时间
  endDate: dateList?.value[1] + " ", //日期-结束时间
  checknameid: "", //审核人id
  checkId: "", //审核人id
  supplyName: "", //供应商拼音或代码
  inStoreTypeId: false, //是否票货不符
  isChecked: false //是否未审核 /未审核供货商
});
/** 表格上方表单 */
const tableForm: any = reactive({
  inStoreTypeId: "", //入库类型id
  inStoreTypeName: "", //入库类型
  publicSignId: "", //招标id
  publicSignName: "", //招标类型
  supplyName: "", //供货商
  invoiceNumber: "", //发票编号
  invoiceDate: getNowDate(new Date(), "d"), //发票时间
  invoiceAmount: "", //发票金额
  supplierId: "", //供货商id
  supplyInputCode: "", ///供货商输入码
  supplyCode: "", //供货商代码

  drugId: "", //药品ID
  drugInputCode: "", //药品输入码
  drugInputCode2: "", //药品输入码2
  drugInputCode3: "", //药品输入码3
  // catalogId: "", //药品分类ID     //无用
  priceinventorynum: "",
  inventorynum: "",
  // inventory: "", //库存量
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格
  origin: "", //产地
  purchasePrice: "", //批发价
  inStorePrice: "", //进价
  addRate: "", //加成率
  retailPrice: "", //零售价
  number: "", //入库数量
  unitName: "", //入库数量单位
  batchNumber: "", //批号
  validityPeriod: new Date(), //效期
  position: "", //仓位
  comment: "", //其他信息
  // lastPurchasePrice: "", //上次进价
  // lastRetailPrice: "", //上次售价
  stockPackagQuantity: "", //库房包装量
  stockPackagUnitId: "", //单位
  orgaDrugId: "", //机构药品id
  produceDate: "" //生产日期
});
/** 操作时间改变时 */
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
/** 点击查询 */
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

    InStoreBillMasterList(); //主表查询
  }
}
/** 左边主表查询 接口 */
const InStoreBillMasterList = () => {
  //phbfid()
  tableLoading.value = true;
  tableLoadingright.value = true;
  isAdd.value = true;
  // isbfp.value = true;
  opacityStyle.value = ""; //控制左侧是否可以点击
  tablexz.value = []; //新增清空
  tablexg.value = []; //修改清空
  /////调接口
  getInStoreBillMasterList(
    "0521",
    "101",
    formData.isInputOrCheckDate == "录单日期" ? "false" : "true",
    `${formData.startDate}${"00:00:00"}`,
    `${formData.endDate}${"23:59:59"}`,
    formData.checknameid,
    formData.inStoreTypeId,
    formData.isChecked == false ? "true" : "false",
    formData.supplyName
  )
    .then((res: any) => {
      if (res.code === "200") {
        tableLeft.value = res.data.DefineInStoreBillMasterInfo;
        // console.log(tableLeft.value, '获取入库单信息')
        tableLeftcurrentRow.value = tableLeft.value[0];
        chname.value = tableLeftcurrentRow.value.status; //审核状态
        if (tableLeftcurrentRow.value == null || tableLeftcurrentRow.value == "") {
          tableLeft.value = [];
          //没有查询到数据的时候 给右边表单赋值空
          NohandleChange();
        } else {
          // 高亮第一行
          nextTick(() => {
            tableLeftRef.value.setCurrentRow(tableLeft.value[0]);
          });
          //查询明细及其他操作
          //明细
          InStoreBillDetailListWithDrawal();
          //获取表单数据(主表和明细数据 一起的表单)
        }

        tableLoading.value = false;
        tableLoadingright.value = false;
      } else {
        tableLeft.value = [];
        NohandleChange();

        tableLoading.value = false;
        tableLoadingright.value = false;
        ElMessage({ message: res.msg, type: "error" });
      }
    })
    .catch(error => {
      tableLeft.value = [];
      NohandleChange();

      tableLoading.value = false;
      tableLoadingright.value = false;
    });
};
/** 点击新增入库单 */
function clickInsert() {
  isAdd.value = false;
  isshzt.value = false;
  isxz.value = false;
  opacityStyle.value = "opacity:0.3";
  // countrkxx.value = 0; //入库数
  // inStoremoney.value = 0; //进价总额
  // retailmoney.value = 0; //零售总额
  tableRight.value = [];
  tablexz.value = []; //新增清空
  tablexg.value = []; //修改清空
  tableLeftcurrentRow.value = ""; //清空选中左边数据，防止误删
  tableRightcurrentRow.value = ""; //清空选中右边数据，防止误删
  //右边表单恢复初始
  tableForm.inStoreTypeId = storageTypeList.value[0].id;
  tableForm.inStoreTypeName = storageTypeList.value[0].kvmemo;
  tableForm.publicSignId = tenderTypeList.value[0].id;
  tableForm.publicSignName = tenderTypeList.value[0].kvmemo;
  tableForm.supplyName = ""; //供货商
  tableForm.invoiceNumber = ""; //发票编号
  tableForm.invoiceDate = new Date(); //发票时间
  tableForm.invoiceAmount = ""; //发票金额

  // tableForm.inventory = ""; //库存量
  tableForm.drugCode = ""; //药品代码
  tableForm.drugName = ""; //药品名称
  tableForm.drugSpecification = ""; //规格
  tableForm.origin = ""; //产地
  tableForm.purchasePrice = ""; //批发价
  tableForm.inStorePrice = ""; //进价
  tableForm.addRate = ""; //加成率
  tableForm.retailPrice = ""; //零售价
  tableForm.number = ""; //入库数量
  tableForm.unitName = ""; //入库数量单位
  tableForm.batchNumber = ""; //批号
  tableForm.validityPeriod = new Date(); //效期
  tableForm.produceDate = ""; //生产日期
  tableForm.position = ""; //仓位
  tableForm.comment = ""; //其他信息
  // tableForm.lastPurchasePrice = ""; //上次进价
  // tableForm.lastRetailPrice = ""; //上次售价
  // formRefs["inStoreTypeName"].visible = true;
  // formRefs["inStoreTypeName"].focus();
}
/**点击保存 */
function clickSave() {
  if (zujian.value == "Add") {
    if (tableRight.value == null || tableRight.value.length == 0) {
      ElMessageBox.alert("</el-icon><span>请添加药品明细信息!</span> ", "提示消息", {
        dangerouslyUseHTMLString: true,
        confirmButtonText: "确认",
        type: "warning"
      });
      return;
    } else {
      //tableLeftcurrentRow.value == '' 新增清空， 要是直接在原来的基础上直接添加那就跳转修改接口
      if (isxz.value == false && tableLeftcurrentRow.value == "") {
        //tableLeft第一个记录
        tableLeft.value.unshift({
          id: "",
          orgaId: "0521", //机构id
          deptId: "101", //部门id
          deptInputCode: "", //部门输入码
          deptCode: "101", //部门代码
          deptName: "西药库", //部门名称
          checkDate: "", //审核时间
          checkId: "", //审核员Id
          invoiceNumber: tableForm.invoiceNumber, //发票编号
          invoiceDate: tableForm.invoiceDate, //发票时间
          invoiceAmount: tableForm.invoiceAmount, //发票金额
          supplierId: tableForm.supplierId, //供货商id
          supplyInputCode: tableForm.supplyInputCode, //供货商输入码
          supplyCode: tableForm.supplyCode, //供货商代码
          supplyName: tableForm.supplyName, //供货商
          billNo: "", //入库单号
          publicSignId: tableForm.publicSignId, //招标id
          publicSignName: tableForm.publicSignName, //招标类型
          inStoreTypeId: tableForm.inStoreTypeId, //入库类型id
          inStoreTypeName: tableForm.inStoreTypeName, //入库类型
          expenditureAmount: tableForm.invoiceAmount, //实支金额
          billMemo: "", //摘要
          isActive: "在用", //是否在用
          stopDate: "", //停用日期
          status: "未审核", //状态
          operatorId: "yang", //修改人id
          inputDate: new Date(),
          createUser: "yang", //创建人id
          createDate: new Date(),
          comment_: "add"
        });
      } else {
        zujian.value = "Update";
      }
      isAdd.value = true;
      isshzt.value = true;
      addDrugWithdrawalInfo(); //添加药品函数
    }
  } else if (zujian.value == "Update") {
    isAdd.value = true;
    isshzt.value = true;
    addDrugWithdrawalInfo();
  } else {
    ElMessageBox.alert("</el-icon><span>共有0条数据被修改!</span> ", "提示消息", {
      dangerouslyUseHTMLString: true,
      confirmButtonText: "确认",
      type: "warning"
    });
    return;
  }
}
/** 保存时添加入库单或药品？ */
const addDrugWithdrawalInfo = () => {
  // zcyid('IsqyTYYPBTPHLZTYDZS')
  console.log(zujian.value, "保存接口");
  if (zujian.value == "Add") {
    ///保存入库单
    getaddDrugWithdrawalInfo(
      isxz.value == false ? tableLeft.value[0] : tableLeftcurrentRow.value, //主表
      tableRight.value, //插入的明细表
      kzList.value
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: res.msg, type: "success" });

        zujian.value = "";
        bcpd.value = "";
        isxz.value = true;
        InStoreBillMasterList();
        // InStoreBillMasterMoneyCount()     //调接口获取价格函数
        // console.log('保存入库单成功')
      } else {
        ElMessage({ message: res.msg, type: "error" });
        zujian.value = "";
        bcpd.value = "";
        isxz.value = true;
        InStoreBillMasterList();
      }
    });
  } else if (zujian.value == "Update") {
    // console.log(isxz.value, '进来修改')

    getaddDrugWithdrawalInfo(
      tableLeftcurrentRow.value,
      tablexz.value, //新增入参明细
      tablexg.value //修改入参明细
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: res.msg, type: "success" });

        zujian.value = "";
        bcpd.value = "";

        InStoreBillMasterList();
        // InStoreBillMasterMoneyCount()
        // console.log('保存入库单成功')
      } else {
        // if (res.msg.includes('未维护标识码')) {
        //   isAdd.value = true //是否点击新增按钮
        //   isshzt.value = false //审核状态
        //   isxz.value = true //是否新增
        //   ElMessage({message: res.msg, type: 'error'})

        // } else {

        //   // InStoreBillMasterMoneyCount()
        // }
        ElMessage({ message: res.msg, type: "error" });
        zujian.value = "";
        bcpd.value = "";
        InStoreBillMasterList();
      }
    });
  }
};

/** 转换时间，yee(d:当前 ,m：明年) */
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
/** 点击审核 */
const InStoreBill = () => {
  if (tableLeft.value.length > 0) {
    ElMessageBox.confirm(
      `是否审核单号为【${tableLeftcurrentRow.value.billNo}】,  发票号 【${tableLeftcurrentRow.value.invoiceNumber}】 的入库单!`,
      "提示消息",
      {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }
    ).then(() => {
      //审核接口
      getStoreBill(); //审核接口
    });
  } else {
    ElMessage({ message: "请选择需要审核的数据!", type: "error" });
  }
};
/** 审核接口 */
function getStoreBill() {
  checkInStoreBill(
    "0521",
    "101",
    tableLeftcurrentRow.value.id, //主表id
    "yang" //用户id
  ).then((res: any) => {
    if (res.code === "200") {
      ElMessage({ message: res.msg, type: "success" });

      InStoreBillMasterList();
      // InStoreBillMasterMoneyCount()
      // console.log('入库单审核成功')
    } else {
      ElMessage({ message: res.msg, type: "error" });
    }
  });
}
/** 点击添加药品 右侧药品信息 */
const addDetail = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      const canProceed = changeProduceDate();
      if (!canProceed) return;
      isxz.value = false;
      //循环判断药品信息是否重复添加
      if (isxz.value == false) {
        zujian.value = "Add";
        bcpd.value = "新增";

        // 如果药品代码一样，批次不一样可以添加，批次一样不能添加
        const isExisted = ref(false);

        if (tableRight.value.length > 0) {
          tableRight.value.forEach((item: any) => {
            if (item.orgaDrugId == tableForm.orgaDrugId && item.batchNumber == tableForm.batchNumber) {
              isExisted.value = true;
            }
          });
        }
        if (isExisted.value) {
          ElMessageBox.confirm(`药品批号【${tableForm.batchNumber}】已存在，请重新输入!`, "提示消息", {
            cancelButtonText: "取消",
            confirmButtonText: "确认",
            type: "warning"
          }).then(() => {
            nextTick(() => {
              DrugcodeInfoList.value = [];
            });
          });
        } else {
          // console.log(zujian.value, 'zujian.value')
          if (tableForm.number.length > 10) {
            ElMessageBox.alert("</el-icon><span>入库数量最大不能超过10位数!</span> ", "提示消息", {
              dangerouslyUseHTMLString: true,
              confirmButtonText: "确认",
              type: "warning"
            });
          } else {
            //判断进价批发价为0的判断
            if (tableForm.inStorePrice == "0" && tableForm.purchasePrice == "0") {
              ElMessageBox.confirm(
                `${tableForm.drugName} 的进价和批发价都为 0元,是否入库?`,
                "提示消息",

                {
                  cancelButtonText: "取消",
                  confirmButtonText: "确认",
                  type: "warning"
                }
              ).then(() => {
                //调用接口
                adddetailInfo();
              });
            } else {
              adddetailInfo();
            }
          }
        }
      } else {
        zujian.value = "Update";
        // console.log(zujian.value, '功能')
        upDetail(TableForm);
      }
    }
  });
};
/** 调接口，添加右侧明细表格 */
function adddetailInfo() {
  //右边表格
  tableRight.value.unshift({
    id: "", //id
    orgaId: "0521", //机构id
    inStoreBillId: "", //入库单主表Id
    billNo: "", //入库单号
    drugId: tableForm.drugId, //药品id
    orgaDrugId: tableForm.orgaDrugId,
    drugInputCode: tableForm.drugInputCode, //药品输入码
    drugInputCode2: tableForm.drugInputCode2, //药品输入码2
    drugInputCode3: tableForm.drugInputCode3, //药品输入码3
    drugCode: tableForm.drugCode, //药品代码
    drugName: tableForm.drugName, //药品名称
    drugSpecification: tableForm.drugSpecification, //规格
    origin: tableForm.origin, //产地
    supplierId: tableForm.supplierId, //供货商id
    supplyInputCode: tableForm.supplyInputCode, //供货商输入码
    supplyCode: tableForm.supplyCode, //供货商代码
    supplyName: tableForm.supplyName, //供货商
    number: tableForm.number, //入库数量
    unitId: tableForm.stockPackagUnitId, //单位id
    unitName: tableForm.unitName, //入库数量单位
    packagQuantity: tableForm.stockPackagQuantity, //包装量
    inStorePrice: tableForm.purchasePrice, //批发价
    purchasePrice: tableForm.inStorePrice, //进价
    addRate: tableForm.addRate, //加成率
    retailPrice: tableForm.inStorePrice * (tableForm.addRate / 100 + 1), //零售价 进价*(加成率/100+1)
    purchaseMoney: tableForm.inStorePrice * tableForm.number, //批发金额
    retailMoney: tableForm.retailPrice * tableForm.number, //零售金额
    position: tableForm.position, //仓位
    batchNumber: tableForm.batchNumber, //批号
    batch: "", //批次
    validityPeriod: tableForm.validityPeriod, //效期
    produceDate: tableForm.produceDate, //生产日期
    isActive: "在用", //是否在用
    stopDate: "", //停用日期
    status: "未审核", //状态
    operatorId: "yang", //修改人id
    inputDate: new Date(),
    createUser: "yang", //创建人id
    createDate: new Date(),
    comment: tableForm.comment, //备注
    dwAmount: "", //冲销数量
    // inventory: tableForm.inventorynum, //库存量
    priceinventory: tableForm.priceinventorynum //库存余量
    // catalogid: tableForm.catalogId, //药品分类id
    // lastPurchasePrice: tableForm.lastPurchasePrice, //上次进价
    // lastRetailPrice: tableForm.lastRetailPrice //上次售价
  });

  tableRightcurrentRow.value = tableRight.value[0];
  tablexz.value.push(tableRightcurrentRow.value); //新增的数据
  console.log(tablexz.value, "新增");

  if (tableLeftcurrentRow.value != null && tableLeftcurrentRow.value != "") {
    tableLeftcurrentRow.value.inStoreTypeId = tableForm.inStoreTypeId; //入库类型id
    tableLeftcurrentRow.value.inStoreTypeName = tableForm.inStoreTypeName; //入库类型
    tableLeftcurrentRow.value.publicSignId = tableForm.publicSignId; //入库类型id
    tableLeftcurrentRow.value.publicSignName = tableForm.publicSignName; //招标类型
    tableLeftcurrentRow.value.invoiceNumber = tableForm.invoiceNumber; //发票编号
    tableLeftcurrentRow.value.invoiceDate = tableForm.invoiceDate; //发票时间
    tableLeftcurrentRow.value.invoiceAmount = tableForm.invoiceAmount; //发票金额
    tableLeftcurrentRow.value.inputDate = new Date(); //更新时间
    tableLeftcurrentRow.value.comment_ = "";
  }
  //console.log(inStoremoney.value, retailmoney.value, '明细添加数据')
  // //默认第一条
  // nextTick(() => {
  //         tableRightRef.value.setCurrentRow(tableRight.value[0])
  //       })
  //添加右边明细进去  清空数据
  rtableform();
  //定位药品代码焦点
  nextTick(() => {
    DrugcodeInfoList.value = [];
  });
  // drugcode.value.blur()
}
/** 添加右边明细进去  清空数据 */
function rtableform() {
  // tableForm.inventory = ""; //库存量
  tableForm.drugCode = ""; //药品代码
  tableForm.drugName = ""; //药品名称
  tableForm.drugSpecification = ""; //规格
  tableForm.origin = ""; //产地
  tableForm.purchasePrice = ""; //批发价
  tableForm.inStorePrice = ""; //进价
  tableForm.addRate = ""; //加成率
  tableForm.retailPrice = ""; //零售价
  tableForm.number = ""; //入库数量
  tableForm.unitName = ""; //入库数量单位
  tableForm.batchNumber = ""; //批号
  tableForm.validityPeriod = new Date(); //效期
  tableForm.produceDate = ""; //生产日期
  tableForm.position = ""; //仓位
  tableForm.comment = ""; //其他信息
  // tableForm.lastPurchasePrice = ""; //上次进价
  // tableForm.lastRetailPrice = ""; //上次售价
}
/** 生产日期改变时 */
const changeProduceDate = () => {
  if (tableForm.produceDate && tableForm.validityPeriod) {
    if (checkDate(tableForm.produceDate, tableForm.validityPeriod)) {
      ElMessage({ message: "生产日期要小于效期时间!", type: "warning" });
      return false;
    }
    if (dayjs(tableForm.produceDate).format("YYYY-MM-DD") === dayjs(tableForm.validityPeriod).format("YYYY-MM-DD")) {
      ElMessage({ message: "生产日期不能等于效期时间!", type: "warning" });
      return false;
    }
  } else {
    ElMessage({ message: "生产日期或效期不能为空!", type: "warning" });
    return false;
  }
  return true;
};
/** 修改数据 */
const upDetail = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(valid => {
    if (valid) {
      const canProceed = changeProduceDate();
      if (!canProceed) return;
      zujian.value = "Update";
      bcpd.value = "修改";
      isxz.value = true;
      // tableLeftcurrentRow.value.supplyName = tableForm.supplyName
      // tableLeftcurrentRow.value.supplierId = tableForm.supplierId //供货商id
      // tableLeftcurrentRow.value.supplyInputCode = tableForm.supplyInputCode ///供货商输入码
      // tableLeftcurrentRow.value.supplyCode = tableForm.supplyCode //供货商代码
      //右边表格数据
      tableRightcurrentRow.value.supplyName = tableForm.supplyName;
      tableRightcurrentRow.value.supplierId = tableForm.supplierId; //adddetailInfo供货商id
      tableRightcurrentRow.value.supplyInputCode = tableForm.supplyInputCode; ///供货商输入码
      tableRightcurrentRow.value.supplyCode = tableForm.supplyCode; //供货商代码
      // tableRightcurrentRow.value.inventory = tableForm.inventorynum; //库存量
      tableRightcurrentRow.value.drugCode = tableForm.drugCode; //药品代码
      tableRightcurrentRow.value.drugName = tableForm.drugName; //药品名称
      tableRightcurrentRow.value.drugSpecification = tableForm.drugSpecification; //规格
      tableRightcurrentRow.value.supplyName = tableForm.supplyName; //产地
      tableRightcurrentRow.value.purchasePrice = tableForm.purchasePrice; //进价
      tableRightcurrentRow.value.inStorePrice = tableForm.inStorePrice; //批发价
      tableRightcurrentRow.value.addRate = tableForm.addRate; //加成率
      tableRightcurrentRow.value.retailPrice = tableForm.retailPrice; //零售价

      tableRightcurrentRow.value.number = tableForm.number; //入库数量
      tableRightcurrentRow.value.unitId = tableForm.stockPackagUnitId; //单位id
      tableRightcurrentRow.value.unitName = tableForm.unitName; //入库数量单位
      tableRightcurrentRow.value.batchNumber = tableForm.batchNumber; //批号
      tableRightcurrentRow.value.inputDate = new Date(); //更新时间
      tableRightcurrentRow.value.position = tableForm.position; //仓位
      tableRightcurrentRow.value.comment = tableForm.comment; //其他信息
      tableRightcurrentRow.value.validityPeriod = tableForm.validityPeriod; //效期
      tableRightcurrentRow.value.produceDate = tableForm.produceDate; //生产日期
      // tableRightcurrentRow.value.lastPurchasePrice = tableForm.lastPurchasePrice; //上次进价
      // tableRightcurrentRow.value.lastRetailPrice = tableForm.lastRetailPrice; //上次售价
      tableRightcurrentRow.value.purchaseMoney = Number(tableForm.number) * Number(tableForm.inStorePrice); //进价总金额
      tableRightcurrentRow.value.retailMoney = Number(tableForm.number) * Number(tableForm.retailPrice); //零售总金额
      tablexg.value.push(tableRightcurrentRow.value); //修改的数据

      // tableRight.value.forEach((item: any) => {
      //   inStoremoney.value = inStoremoney.value + item.purchaseMoney; //进价总金额
      // });
      // if (inStoremoney.value != 0) {
      //   tableForm.invoiceAmount = inStoremoney.value.toFixed(2); //赋值发票金额
      // }

      //左边表格数据
      if (tableLeftcurrentRow.value != null && tableLeftcurrentRow.value != "") {
        tableLeftcurrentRow.value.inStoreTypeId = tableForm.inStoreTypeId; //入库类型id
        tableLeftcurrentRow.value.inStoreTypeName = tableForm.inStoreTypeName; //入库类型
        tableLeftcurrentRow.value.publicSignId = tableForm.publicSignId; //入库类型id
        tableLeftcurrentRow.value.publicSignName = tableForm.publicSignName; //招标类型
        tableLeftcurrentRow.value.invoiceNumber = tableForm.invoiceNumber; //发票编号
        tableLeftcurrentRow.value.invoiceDate = tableForm.invoiceDate; //发票时间
        tableLeftcurrentRow.value.invoiceAmount = tableForm.invoiceAmount; //发票金额
        tableLeftcurrentRow.value.inputDate = new Date(); //更新时间
        tableLeftcurrentRow.value.comment_ = "";
      }
      //添加右边明细进去  清空数据
      rtableform();
      //定位药品代码焦点
      nextTick(() => {
        DrugcodeInfoList.value = [];
      });
      // console.log(tablexg.value, '修改的数据tablexzxg')
    }
  });
};
/** 点击删除, 调接口删除入库单明细*/
const wasterInStoreBillDetail = () => {
  const isOld = ref(false); //删除的数据是否为数据库内的数据
  if (tableLeftcurrentRow.value != "") {
    isOld.value = true;
    getwasterInStoreBillDetail(
      "0521",
      "101",
      tableLeftcurrentRow.value.id, //主表id
      tableRightcurrentRow.value.id, //明细id
      "yang" //用户id
    ).then((res: any) => {
      if (res.code === "200") {
        ElMessage({ message: res.msg, type: "success" });
        isAdd.value = true;
        isshzt.value = true;
        InStoreBillMasterList();
        // InStoreBillMasterMoneyCount()
      } else {
        ElMessage({ message: res.msg, type: "error" });
      }
    });
  }

  if (!isOld.value) {
    //为false时，即tableLeftcurrentRow.value = ""  //即该入库单还未保存到数据库，直接删除
    if (tableRight.value.length > 0) {
      tablexz.value.forEach((item: any, index: any) => {
        if (tableRightcurrentRow.value.drugCode == item.drugCode) {
          tablexz.value.splice(index, 1);
        }
      });

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

    // if (inStoremoney.value != 0) {
    //   tableForm.invoiceAmount = inStoremoney.value.toFixed(2); //赋值发票金额
    // }
    // console.log(inStoremoney.value, tableForm.invoiceAmount, '发票金额')
  }
};
/** 点击左表某行，切换当前行 */
const tableLefthandleChange = (val: any) => {
  tableLeftcurrentRow.value = val;
  //console.log(tableLeftcurrentRow.value, '左边表个数据')

  if (tableLeftcurrentRow.value == null) {
    // console.log('这里是没有检索到数据')
  } else {
    //点击新增则不给详细表赋值
    chname.value = tableLeftcurrentRow.value.status;
    // console.log(chname.value + "点击左边表格状态");
    if (isAdd.value) {
      //赋值给右边表单
      tableForm.inStoreTypeId = tableLeftcurrentRow.value.inStoreTypeId; //入库id
      tableForm.inStoreTypeName = tableLeftcurrentRow.value.inStoreTypeName; //入库类型
      tableForm.publicSignId = tableLeftcurrentRow.value.publicSignIdz; //招标id
      tableForm.publicSignName = tableLeftcurrentRow.value.publicSignName; //招标类型
      tableForm.invoiceNumber = tableLeftcurrentRow.value.invoiceNumber; //发票编号
      tableForm.invoiceDate = tableLeftcurrentRow.value.invoiceDate; //发票时间
      tableForm.invoiceAmount = tableLeftcurrentRow.value.invoiceAmount; //发票金额
      //控制全部冲销复选框是否显示
      chname.value = tableLeftcurrentRow.value.status;
      //控制全部冲销复选框是否显示
      // checkZT(chname.value)
      //明细
      InStoreBillDetailListWithDrawal(); //获取入库单明细表
    } else {
      tableRight.value = [];
      tableLeftRef.value.setCurrentRow();
    }
  }
};
/** 调接口-获取入库单明细 */
const InStoreBillDetailListWithDrawal = () => {
  // if (formData.cmbpxfs == '药品代码') {
  //   cmbpx.value = '0'
  // } else if (formData.cmbpxfs == '药品名称') {
  //   cmbpx.value = '1'
  // } else {
  //   cmbpx.value = ''
  // }

  tableLoadingright.value = true;
  getInStoreBillDetailListWithDrawal("0521", tableLeftcurrentRow.value.id, "101", "cmbpx.value排序规则")
    .then((res: any) => {
      if (res.code === "200") {
        tableRight.value = res.data.DefineInStoreBillDetailInfoWithDrawal;
        tableRightcurrentRow.value = tableRight.value[0];
        if (tableRightcurrentRow.value == null || tableRightcurrentRow.value == "") {
          //没有查询到数据的时候
          tableRight.value = [];
          ElMessage({ message: "明细表暂无数据", type: "success" });
        } else {
          // 高亮第一行
          nextTick(() => {
            tableRightRef.value.setCurrentRow(tableRight.value[0]);
          });
          // console.log(tableRightcurrentRow.value, '默认选中右边表第一行')
          //表单
          RightForm();
        }
        tableLoadingright.value = false;
      } else {
        tableRight.value = [];
        NohandleChange(); //
        ElMessage({ message: res.msg, type: "error" });
        tableLoadingright.value = false;
      }
    })
    .catch(error => {
      tableRight.value = [];
      NohandleChange();

      tableLoadingright.value = false;
    });
};
/** 没有查询到数据的时候处理 */
function NohandleChange() {
  //没有查询到数据的时候
  isAdd.value = true;
  // console.log('没有检索到数据')
  tableForm.inStoreTypeId = ""; //入库类型id
  tableForm.inStoreTypeName = ""; //入库类型
  tableForm.publicSignId = ""; //招标id
  tableForm.publicSignName = ""; //招标类型
  tableForm.supplyName = ""; //供货商
  tableForm.invoiceNumber = ""; //发票编号
  tableForm.invoiceDate = new Date(); //发票时间
  tableForm.invoiceAmount = ""; //发票金额

  // tableForm.inventory = ""; //库存量
  tableForm.drugCode = ""; //药品代码
  tableForm.drugName = ""; //药品名称
  tableForm.drugSpecification = ""; //规格
  tableForm.origin = ""; //产地
  tableForm.purchasePrice = ""; //批发价
  tableForm.inStorePrice = ""; //进价
  tableForm.addRate = ""; //加成率
  tableForm.retailPrice = ""; //零售价
  tableForm.number = ""; //入库数量
  tableForm.unitName = ""; //入库数量单位
  tableForm.batchNumber = ""; //批号
  tableForm.validityPeriod = new Date(); //效期
  tableForm.produceDate = ""; //生产日期
  tableForm.position = ""; //仓位
  tableForm.comment = ""; //其他信息
  // tableForm.lastPurchasePrice = ""; //上次进价
  // tableForm.lastRetailPrice = ""; //上次售价
  chname.value = "";
  //查询明细
  tableRight.value = [];
  ElMessage({ message: "暂无数据!", type: "success" });
}
/** 获取右侧表单数据，赋值 */
function RightForm() {
  //赋值给右边表单
  tableForm.inStoreTypeId = tableLeftcurrentRow.value.inStoreTypeId; //入库类型id
  tableForm.inStoreTypeName = tableLeftcurrentRow.value.inStoreTypeName; //入库类型
  tableForm.publicSignId = tableLeftcurrentRow.value.publicSignId; //招标id
  tableForm.publicSignName = tableLeftcurrentRow.value.publicSignName; //招标类型
  tableForm.invoiceNumber = tableLeftcurrentRow.value.invoiceNumber; //发票编号
  tableForm.invoiceDate = tableLeftcurrentRow.value.invoiceDate; //发票时间
  tableForm.invoiceAmount = tableLeftcurrentRow.value.invoiceAmount; //发票金额

  tableForm.inventorynum == tableRightcurrentRow.value.inventory;
  tableForm.priceinventorynum == tableRightcurrentRow.value.priceinventory;
  //   tableForm.inventory=tableRightcurrentRow.value.inventory + dwdy.value
  // }
  tableForm.supplyName = tableRightcurrentRow.value.supplyName; //供货商
  tableForm.supplierId = tableRightcurrentRow.value.supplierId; //供货商id
  tableForm.supplyInputCode = tableRightcurrentRow.value.supplyInputCode; ///供货商输入码
  tableForm.supplyCode = tableRightcurrentRow.value.supplyCode; //供货商代码

  // tableForm.inventory = tableRightcurrentRow.value.inventory + tableRightcurrentRow.value.unitName; //库存量
  tableForm.drugCode = tableRightcurrentRow.value.drugCode; //药品代码
  tableForm.drugName = tableRightcurrentRow.value.drugName; //药品名称
  tableForm.drugSpecification = tableRightcurrentRow.value.drugSpecification; //规格
  tableForm.origin = tableRightcurrentRow.value.origin; //产地
  tableForm.purchasePrice = tableRightcurrentRow.value.purchasePrice; //批发价
  tableForm.inStorePrice = tableRightcurrentRow.value.inStorePrice; //进价
  tableForm.addRate = tableRightcurrentRow.value.addRate; //加成率
  tableForm.retailPrice = tableRightcurrentRow.value.retailPrice; //零售价
  tableForm.number = tableRightcurrentRow.value.number; //入库数量
  tableForm.unitName = tableRightcurrentRow.value.unitName; //入库数量单位
  tableForm.batchNumber = tableRightcurrentRow.value.batchNumber; //批号
  tableForm.validityPeriod = tableRightcurrentRow.value.validityPeriod; //效期
  tableForm.produceDate = tableRightcurrentRow.value.produceDate; //生产日期
  tableForm.position = tableRightcurrentRow.value.position; //仓位
  tableForm.comment = tableRightcurrentRow.value.comment; //其他信息
  // tableForm.lastPurchasePrice = tableRightcurrentRow.value.lastPurchasePrice; //上次进价
  // tableForm.lastRetailPrice = tableRightcurrentRow.value.lastRetailPrice; //上次售价
  tableForm.orgaDrugId = tableRightcurrentRow.value.orgaDrugId; //药品id
  // inStoremoney.value = 0
  // retailmoney.value = 0
  //计算明细进价总金额、零售总金额
  if (tableRight.value != null) {
    // console.log('选中左边表格123')
    // tableRight.value.forEach((item: any) => {
    //   inStoremoney.value = inStoremoney.value + item.purchaseMoney; //进价总金额
    //   retailmoney.value = retailmoney.value + item.retailMoney; //零售总金额
    //   tableForm.invoiceAmount = inStoremoney.value.toFixed(2); //发票金额
    // });
  }

  //控制全部冲销复选框是否显示
  chname.value = tableLeftcurrentRow.value.status;
  // console.log(chname.value, '审核状态')

  //控制全部冲销复选框是否显示
  // checkZT(chname.value)
  // InStoreBillMasterMoneyCount()
}
/** 点击右表行 */
const tableRighthandleChange = (val: any) => {
  tableRightcurrentRow.value = val;
  //console.log(tableRightcurrentRow.value, '右边表格数据1')
  if (tableLeftcurrentRow.value == null) {
    // console.log('这里是没有检索到数据')
  } else {
    ///赋值给上方表单
    tableForm.supplyName = tableRightcurrentRow.value.supplyName; //供货商
    tableForm.supplierId = tableRightcurrentRow.value.supplierId; //供货商id
    tableForm.supplyInputCode = tableRightcurrentRow.value.supplyInputCode; ///供货商输入码
    tableForm.supplyCode = tableRightcurrentRow.value.supplyCode; //供货商代码

    // tableForm.inventory = tableRightcurrentRow.value.inventory + tableRightcurrentRow.value.unitName; //库存量
    tableForm.drugCode = tableRightcurrentRow.value.drugCode; //药品代码
    tableForm.drugName = tableRightcurrentRow.value.drugName; //药品名称
    tableForm.drugSpecification = tableRightcurrentRow.value.drugSpecification; //规格
    tableForm.origin = tableRightcurrentRow.value.origin; //产地
    tableForm.purchasePrice = tableRightcurrentRow.value.purchasePrice; //批发价
    tableForm.inStorePrice = tableRightcurrentRow.value.inStorePrice; //进价
    tableForm.addRate = tableRightcurrentRow.value.addRate; //加成率
    tableForm.retailPrice = tableRightcurrentRow.value.retailPrice; //零售价
    tableForm.number = tableRightcurrentRow.value.number; //入库数量
    tableForm.unitName = tableRightcurrentRow.value.unitName; //入库数量单位
    tableForm.batchNumber = tableRightcurrentRow.value.batchNumber; //批号
    tableForm.validityPeriod = tableRightcurrentRow.value.validityPeriod; //效期
    tableForm.produceDate = tableRightcurrentRow.value.produceDate; //生产日期
    tableForm.position = tableRightcurrentRow.value.position; //仓位
    tableForm.comment = tableRightcurrentRow.value.comment; //其他信息
    // tableForm.lastPurchasePrice = tableRightcurrentRow.value.lastPurchasePrice; //上次进价
    // tableForm.lastRetailPrice = tableRightcurrentRow.value.lastRetailPrice; //上次售价
    tableForm.orgaDrugId = tableRightcurrentRow.value.orgaDrugId; //药品id
    //console.log(tableRightcurrentRow.value, '右边数据')
  }
};
/** 表单校验规则 */
const rules = reactive<FormRules>({
  invoiceNumber: [
    {
      required: true,
      message: "请输入发票编号",
      trigger: "blur"
    }
  ],
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
  purchasePrice: [
    {
      required: true,
      message: "请输入正确批发价",
      trigger: "blur"
    }
  ],
  inStorePrice: [
    {
      required: true,
      message: "请输入正确进价",
      trigger: "blur"
    }
  ],
  addRate: [
    {
      required: true,
      message: "请输入正确加成率",
      trigger: "blur"
    }
  ],
  retailPrice: [
    {
      required: true,
      message: "请输入正确零售价",
      trigger: "blur"
    }
  ],
  number: [
    {
      required: true,
      message: "请检查入库数量",
      trigger: "blur"
    }
  ],
  batchNumber: [
    {
      required: true,
      message: "请输入正确批号",
      trigger: "blur"
    }
  ],
  validityPeriod: [
    {
      required: true,
      message: "请输入正确有效期",
      trigger: "blur"
    }
  ],
  produceDate: [
    {
      required: true,
      message: "请输入正确生产日期",
      trigger: "blur"
    }
  ]
});
/** 入库类型改变时 */
const storageType = (event: any) => {
  tableForm.inStoreTypeId = event.id;
  tableForm.inStoreTypeName = event.kvmemo;
};
/** 招标类型-改变时 */
const tenderType = (event: any) => {
  tableForm.publicSignId = event.id;
  tableForm.publicSignName = event.kvmemo;
};
/** 批发价失去焦点时 */
const purchasePriceblur = () => {
  tableForm.inStorePrice = tableForm.purchasePrice; //进价
  if (!Number.isNaN(tableForm.addRate)) {
    if (tableForm.addRate != null && !Number.isNaN(tableForm.addRate)) {
      tableForm.retailPrice = (parseFloat(tableForm.inStorePrice) * (parseFloat(tableForm.addRate) / 100 + 1))
        .toFixed(4)
        .toString(); //零售价
      if (tableForm.retailPrice == "NaN") {
        tableForm.retailPrice = "";
      }
    } else if (tableForm.addRate == "") {
      tableForm.retailPrice = "";
    }
  }
};
/** 进价失去焦点时 */
const inStorePriceblur = () => {
  if (!Number.isNaN(tableForm.addRate)) {
    if (tableForm.addRate != null && !Number.isNaN(tableForm.addRate)) {
      tableForm.retailPrice = (parseFloat(tableForm.inStorePrice) * (parseFloat(tableForm.addRate) / 100 + 1))
        .toFixed(4)
        .toString(); //零售价
      if (tableForm.retailPrice == "NaN") {
        tableForm.retailPrice = "";
      }
    } else if (tableForm.addRate == "") {
      tableForm.retailPrice = "";
    }
  }
};
/** 加成率失去焦点时 */
const addRateblur = () => {
  if (!Number.isNaN(tableForm.addRate)) {
    if (tableForm.addRate != null && !Number.isNaN(tableForm.addRate)) {
      tableForm.retailPrice = (parseFloat(tableForm.inStorePrice) * (parseFloat(tableForm.addRate) / 100 + 1))
        .toFixed(4)
        .toString(); //零售价
      if (tableForm.retailPrice == "NaN") {
        tableForm.retailPrice = "";
      }
    } else if (tableForm.addRate == "") {
      tableForm.retailPrice = "";
    }
  }
};
/** 点击作废，调接口删除该入库单 */
const wasterInStoreBill = () => {
  //判断是否选中左侧表格数据
  if (tableLeft.value.length > 0) {
    // console.log(tableLeft.value, tableLeft.value.length, '进入作废')
    ElMessageBox.confirm(`是否停用单号为【${tableLeftcurrentRow.value.billNo}】 的入库单!`, "提示消息", {
      confirmButtonText: "确定",
      cancelButtonText: "取消",
      type: "warning"
    }).then(() => {
      getwasterInStoreBill(
        "0521",
        "101",
        tableLeftcurrentRow.value.id, //主表id
        "yang" //用户id
      ).then((res: any) => {
        if (res.code === "200") {
          ElMessage({ message: res.msg, type: "success" });

          InStoreBillMasterList();
          // InStoreBillMasterMoneyCount()
        } else {
          ElMessage({ message: res.msg, type: "error" });
        }
      });
    });
  } else {
    ElMessage({ message: "请选择需要作废的入库单！", type: "error" });
  }
};
// /**清除表单药品代码数据时调用  */
// function DrugcodeInfoListChange() {
//     tableForm.inventorynum = ''
//     tableForm.priceinventorynum = ''
//     tableForm.inventory = '' //库存量
//     tableForm.drugCode = '' //药品代码
//     tableForm.drugName = '' //药品名称
//     tableForm.drugSpecification = '' //规格
//     tableForm.origin = '' //产地
//     tableForm.purchasePrice = '' //批发价
//     tableForm.inStorePrice = '' //进价
//     tableForm.addRate = '' //加成率
//     tableForm.retailPrice = '' //零售价
//     tableForm.number = '' //入库数量
//     tableForm.unitName = '' //入库数量单位
//     tableForm.batchNumber = '' //批号
//     //  tableForm.validityPeriod=val.validityPeriod//效期
//     //  tableForm.produceDate=val.produceDate//生产日期
//     tableForm.position = '' //仓位
//     tableForm.comment = '' //其他信息
//     // tableForm.lastPurchasePrice = '' //上次进价
//     // tableForm.lastRetailPrice = '' //上次售价

//     tableForm.drugId = '' //药品ID
//     tableForm.drugInputCode = '' //药品输入码
//     tableForm.drugInputCode2 = '' //药品输入码2
//     tableForm.drugInputCode3 = '' //药品输入码3
//     tableForm.catalogId = '' //药品分类ID
//     tableForm.stockPackagQuantity = '' //库房包装量
//     tableForm.stockPackagUnitId = '' //库房包装单位ID
//     tableForm.orgaDrugId = '' //机构药品id
//   }
//   //选中药品代码信息
//   const addDrugcodeForm = reactive({
//     id: '', //机构药品id
//     drugId: '', //药品ID
//     orgaId: '', //该OrgaID为机构药品中的ID
//     drugCode: '', //药品代码
//     drugName: '', //药品名称
//     drugInputCode: '', //药品输入码
//     drugInputCode2: '', //药品输入码2
//     drugInputCode3: '', //药品输入码3
//     catalogId: '', //药品分类ID
//     basicDosimetry: '', //基本剂量
//     commodityName: '', //商品名称
//     origin: '', //产地
//     priceUnitName: '', //计价单位名称
//     insureCode: '', //医保编码
//     insurePropertyName: '', //医保属性名称
//     approvalNumber: '', //批准文号
//     registeredTrademark: '', //注册商标
//     outpPackagQuantity: '', //门诊包装量
//     outpPackagUnitId: '', //门诊包装单位ID
//     outpPackagUnitName: '', //门诊包装单位名称
//     bigPackagQuantity: '', //大包装量
//     bigPackagUnitID: '', //大包装单位ID
//     bigPackagUnitName: '', //大包装单位名称
//     stockPackagQuantity: '', //库房包装量
//     stockPackagUnitId: '', //库房包装单位ID
//     stockPackagUnitName: '', //库房包装单位名称
//     packageSpec: '', //计价包装规格
//     outpPackageSpec: '', //门诊包装规格
//     bigPackageSpec: '', //大包装规格
//     stockPackageSpec: '', //库房包装规格
//     addRate: '', //加成率
//     outpPerformedDeptId: '', //门诊执行科室ID
//     inpPerformedDeptId: '', //住院执行科室ID
//     orderClassId: '', //医嘱类型ID
//     feeTypeMaskFlag: '', //费别计费标志
//     serviceType: '', //服务使用对象
//     drugMemo: '', //药品备注
//     isActive: '', //是否在用
//     chargeItemId: '', //收费项目ID
//     chargeItemClassID: '', //收费项目类别ID
//     purchasePrice: '', //批发价
//     retailPrice: '', //零售价
//     drugFormID: '', //剂型ID
//     drugFormName: '', //剂型名称
//     varietyName: '', //品种名称
//     varietyCode: '', //品种代码
//     drugSpecification: '', //装规格(说明书）
//     skinTestMark: '', //皮试标志
//     skinTestTypeId: '', //皮试ID
//     skinTestTypeName: '', //皮试名称
//     inventory: '', //库存量
//     priceinventory: '', //库存余量
//     baseDrugMarkName: '', //基药标志名称
//     invoOutpName: '', //门诊发票分类名称
//     invoiInpName: '', //住院发票分类名称
//     lastPurchasePrice: '', //上次进价
//     lastRetailPrice: '', //上次售价
//     drugFrequency: '', //用法
//     drugDosage: '', //用量
//     orgaDrugMemo: '', //机构药品备注：冻结：冻结，解冻：空
//     insureRate: '', //医保单位转换系数
//     deptID: '', //部门ID
//     deptCode: '', //部门编码
//     deptName: '', //部门名称
//     useUnitName: '', //用法单位
//     isDL: '', //是否带量
//     countryCode: '', //国家码
//     purchaseRate: '', //采购转换系数
//     isGpo: '', //是否GPO
//     decoctionMethod: '', //默认煎法
//     position: '', //仓位
//   })
// const DrugcodeTable: any = ref<InstanceType<typeof ElTable>>() //  高亮第一行
// /** 药品输入码按下回车时调用 */
// const enterClickdrugCodeform = (event: any) => {
//     //如果 有选中高亮行，则赋值
//     if (DrugcodeTable.value) {
//       drugcodeChange(
//         DrugcodeInfoList.value[0]
//       )
//     } else {
//       drugcodeInfoList(event)
//     }
//   }
// /** 点击药品信息表某行,进行赋值处理  */
// const drugcodeChange = (val: any) => {
//     tableForm.inventorynum = val.inventory
//     tableForm.priceinventorynum = val.priceInventory

//     tableForm.inventory = val.inventory + val.stockPackagUnitName //库存量
//     tableForm.drugCode = val.drugCode //药品代码
//     tableForm.drugName = val.drugName //药品名称
//     tableForm.drugSpecification = val.drugSpecification //规格
//     tableForm.origin = val.origin //产地
//     tableForm.purchasePrice = val.purchasePrice //批发价
//     tableForm.inStorePrice = val.purchasePrice //进价
//     tableForm.addRate = val.addRate //加成率
//     tableForm.retailPrice = val.retailPrice //零售价
//     tableForm.number = val.number //入库数量
//     tableForm.unitName = val.stockPackagUnitName //入库数量单位
//     tableForm.batchNumber = val.batchNumber //批号
//     //  tableForm.validityPeriod=val.validityPeriod//效期
//     //  tableForm.produceDate=val.produceDate//生产 日期
//     tableForm.position = val.position //仓位
//     tableForm.comment = val.comment //其他信息
//     tableForm.lastPurchasePrice = val.lastPurchasePrice //上次进价
//     tableForm.lastRetailPrice = val.lastRetailPrice //上次售价

//     tableForm.drugId = val.drugId //药品ID
//     tableForm.drugInputCode = val.drugInputCode //药品输入码
//     tableForm.drugInputCode2 = val.drugInputCode2 //药品输入码2
//     tableForm.drugInputCode3 = val.drugInputCode3 //药品输入码3
//     tableForm.catalogId = val.catalogId //药品分类ID
//     tableForm.stockPackagQuantity = val.stockPackagQuantity //库房包装量
//     tableForm.stockPackagUnitId = val.stockPackagUnitId //库房包装单位ID
//     tableForm.orgaDrugId = val.id //机构药品id
//     //  drugcode.value.blur()
//     //药品代码
//     nextTick(() => {
//       DrugcodeInfoList.value = []
//       DrugcodeTable.visible = false
//     })
//     addDrugcodeForm.id = val.id //机构药品id
//     addDrugcodeForm.drugId = val.drugId //药品ID
//     addDrugcodeForm.orgaId = val.orgaId //该OrgaID为机构药品中的ID
//     addDrugcodeForm.drugCode = val.drugCode //药品代码
//     addDrugcodeForm.drugName = val.drugName //药品名称
//     addDrugcodeForm.drugInputCode = val.drugInputCode //药品输入码
//     addDrugcodeForm.drugInputCode2 = val.drugInputCode2 //药品输入码2
//     addDrugcodeForm.drugInputCode3 = val.drugInputCode3 //药品输入码3
//     addDrugcodeForm.catalogId = val.catalogId //药品分类ID
//     addDrugcodeForm.basicDosimetry = val.basicDosimetry //基本剂量
//     addDrugcodeForm.commodityName = val.commodityName //商品名称
//     addDrugcodeForm.origin = val.origin //产地
//     addDrugcodeForm.priceUnitName = val.priceUnitName //计价单位名称
//     addDrugcodeForm.insureCode = val.insureCode //医保编码
//     addDrugcodeForm.insurePropertyName = val.insurePropertyName //医保属性名称
//     addDrugcodeForm.approvalNumber = val.approvalNumber //批准文号
//     addDrugcodeForm.registeredTrademark = val.registeredTrademark //注册商标
//     addDrugcodeForm.outpPackagQuantity = val.outpPackagQuantity //门诊包装量
//     addDrugcodeForm.outpPackagUnitId = val.outpPackagUnitId //门诊包装单位ID
//     addDrugcodeForm.outpPackagUnitName = val.outpPackagUnitName //门诊包装单位名称
//     addDrugcodeForm.bigPackagQuantity = val.bigPackagQuantity //大包装量
//     addDrugcodeForm.bigPackagUnitID = val.bigPackagUnitID //大包装单位ID
//     addDrugcodeForm.bigPackagUnitName = val.bigPackagUnitName //大包装单位名称
//     addDrugcodeForm.stockPackagQuantity = val.stockPackagQuantity //库房包装量
//     addDrugcodeForm.stockPackagUnitId = val.stockPackagUnitId //库房包装单位ID
//     addDrugcodeForm.stockPackagUnitName = val.stockPackagUnitName //库房包装单位名称
//     addDrugcodeForm.packageSpec = val.packageSpec //计价包装规格
//     addDrugcodeForm.outpPackageSpec = val.outpPackageSpec //门诊包装规格
//     addDrugcodeForm.bigPackageSpec = val.bigPackageSpec //大包装规格
//     addDrugcodeForm.stockPackageSpec = val.stockPackageSpec //库房包装规格
//     addDrugcodeForm.addRate = val.addRate //加成率
//     addDrugcodeForm.outpPerformedDeptId = val.outpPerformedDeptId //门诊执行科室ID
//     addDrugcodeForm.inpPerformedDeptId = val.inpPerformedDeptId //住院执行科室ID
//     addDrugcodeForm.orderClassId = val.orderClassId //医嘱类型ID
//     addDrugcodeForm.feeTypeMaskFlag = val.feeTypeMaskFlag //费别计费标志
//     addDrugcodeForm.serviceType = val.serviceType //服务使用对象
//     addDrugcodeForm.drugMemo = val.drugMemo //药品备注
//     addDrugcodeForm.isActive = val.isActive //是否在用
//     addDrugcodeForm.chargeItemId = val.chargeItemId //收费项目ID
//     addDrugcodeForm.chargeItemClassID = val.chargeItemClassID //收费项目类别ID
//     addDrugcodeForm.purchasePrice = val.purchasePrice //批发价
//     addDrugcodeForm.retailPrice = val.retailPrice //零售价
//     addDrugcodeForm.drugFormID = val.drugFormID //剂型ID
//     addDrugcodeForm.drugFormName = val.drugFormName //剂型名称
//     addDrugcodeForm.varietyName = val.varietyName //品种名称
//     addDrugcodeForm.varietyCode = val.varietyCode //品种代码
//     addDrugcodeForm.drugSpecification = val.drugSpecification //装规格(说明书）
//     addDrugcodeForm.skinTestMark = val.skinTestMark //皮试标志
//     addDrugcodeForm.skinTestTypeId = val.skinTestTypeId //皮试ID
//     addDrugcodeForm.skinTestTypeName = val.skinTestTypeName //皮试名称
//     addDrugcodeForm.inventory = val.inventory //库存量
//     addDrugcodeForm.priceinventory = val.priceInventory //库存余量
//     addDrugcodeForm.baseDrugMarkName = val.baseDrugMarkName //基药标志名称
//     addDrugcodeForm.invoOutpName = val.invoOutpName //门诊发票分类名称
//     addDrugcodeForm.invoiInpName = val.invoiInpName //住院发票分类名称
//     addDrugcodeForm.lastPurchasePrice = val.lastPurchasePrice //上次进价
//     addDrugcodeForm.lastRetailPrice = val.lastRetailPrice //上次售价
//     addDrugcodeForm.drugFrequency = val.drugFrequency //用法
//     addDrugcodeForm.drugDosage = val.drugDosage //用量
//     addDrugcodeForm.orgaDrugMemo = val.orgaDrugMemo //机构药品备注：冻结：冻结，解冻：空
//     addDrugcodeForm.insureRate = val.insureRate //医保单位转换系数
//     addDrugcodeForm.deptID = val.deptID //部门ID
//     addDrugcodeForm.deptCode = val.deptCode //部门编码
//     addDrugcodeForm.deptName = val.deptName //部门名称
//     addDrugcodeForm.useUnitName = val.useUnitName //用法单位
//     addDrugcodeForm.isDL = val.isDL //是否带量
//     addDrugcodeForm.countryCode = val.countryCode //国家码
//     addDrugcodeForm.purchaseRate = val.purchaseRate //采购转换系数
//     addDrugcodeForm.isGpo = val.isGpo //是否GPO
//     addDrugcodeForm.decoctionMethod = val.decoctionMethod //默认煎法
//     addDrugcodeForm.position = val.position //仓位
//     // console.log(addDrugcodeForm, '药品信息')
//   }
// /**获取药品信息表 */
// const drugcodeInfoList = async (event: any): Promise<void> => {
//     // showLoading('正在加载药品信息…')
//     if (event) {
//       zcyid('IsOpenFilterFreezingOrgaDrug')
//       zcyid('HerbMedPriceItemID')
//       selectdrugCode.value = event.target.value
//       //console.log(tableForm.drugCode, '药品代码入参')
//       tabLodding.value = true
//       // const { data, code, msg, message } = await getOrgaDrugInfoListByTopClass(
//       //   orgId.value,
//       //   selectdrugCode.value,
//       //   _HerbMedFlag.value,
//       //   HerbMedPriceItemID.value,
//       //   deptid.value,
//       //   IsOpenFilterFreezingOrgaDrug.value
//       // )
//       // if (code === 200) {
//       //   // hideLoading()
//       //   tabLodding.value = false
//       //   DrugcodeInfoList.value =
//       //     data.QueryOrgaDrugInfo != null || data.QueryOrgaDrugInfo.length > 0
//       //       ? data.QueryOrgaDrugInfo
//       //       : []
//       //   // console.log(DrugcodeInfoList.value, '获取药品信息')
//       //   // 高亮第一行
//       //   if (DrugcodeInfoList.value.length > 0) {
//       //     nextTick(() => {
//       //       tabLodding.value = false
//       //       DrugcodeTable.value.setCurrentRow(DrugcodeInfoList.value[0])
//       //     })
//       //   }
//       // } else {
//       //   // hideLoading()
//       //   loadingdrugcode.value = false
//       //   gp.$baseMessage(msg || message, 'error')
//       // }
//       tabLodding.value = false
//     }
//   }
/** 输入药品代码后回车，查询药品信息,赋值给后面几个表单 */
const handleDrugCode = () => {
  getDrugInfoByCode(tableForm.drugCode)
    .then((res: any) => {
      if (res?.code == "200" && res.data != null) {
        tableForm.drugId = res.data.id;
        tableForm.drugCode = res.data.drugCode;
        tableForm.drugName = res.data.drugName;
        tableForm.drugSpecification = res.data.drugSpecification;
        tableForm.origin = res.data.origin;
      } else {
        ElMessage({ message: "未查询到该药品信息！", type: "error" });
      }
    })
    .catch((e: Error) => {
      ElMessage({ message: e.message || "失败！", type: "error" });
      throw e;
    });
};
const tableDatadcxml: any = ref([]); //导出的列表
/** 点击导出按钮 */
const exportXml = throttle(() => {
  tableDatadcxml.value = [];
  if (tableRight.value.length > 0) {
    //  查询数据
    getInStoreBillDetailListWithDrawal("0521", tableLeftcurrentRow.value.id, "101", "cmbpx.value排序规则")
      .then((res: any) => {
        if (res.code === "200") {
          tableDatadcxml.value = res.data.DefineInStoreBillDetailInfoWithDrawal;
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
    "生产日期",
    "规格",
    "入库数量",
    "单位",
    "进价",
    "零售价",
    "进价金额",
    "零售金额",
    "批号",
    "效期",
    "产地",
    "批次",
    "供应商",
    "其他信息"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    item.drugCode,
    item.drugName,
    item.produceDate,
    item.drugSpecification,
    item.number,
    item.unitName,
    item.purchasePrice,
    item.retailPrice,
    item.purchaseMoney,
    item.retailMoney,
    item.batchNumber,
    item.validityPeriod,
    item.origin,
    item.batch,
    item.supplyName,
    item.comment
  ]);
  exportDrugExpiryTable(headersArr, data, "浦东测试机构西药库" + "入库单" + tableLeftcurrentRow.value.billNo + "信息");
}
/** 工具函数：将后端数组转换为 YYYY-MM-DD */
const formatProduceDate = (dateArray: number[]) => {
  if (!dateArray || dateArray.length < 3) return ""; // 防御空值

  // 注意：后端月份从 1 开始，JavaScript Date 月份从 0 开始
  const [year, month, day] = dateArray;
  const jsDate = new Date(year, month - 1, day); // 关键！调整月份

  return dayjs(jsDate).format("YYYY-MM-DD");
};
onMounted(() => {
  InStoreBillMasterList();
});
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
          <el-col class="col" :span="3">
            <el-form-item label="审核人" style="width: 97%">
              <el-select ref="checkname" v-model="formData.checkId" clearable placeholder="请输入审核人姓名...">
                <el-option label="张三" value="1"></el-option>
                <el-option label="李四" value="2"></el-option>
                <el-option label="王五" value="3"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="3">
            <el-form-item label="供货商" style="width: 97%">
              <el-input ref="supplytj" v-model="formData.supplyName" clearable placeholder="请输入供货商代码\拼音..."> </el-input>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="4"> </el-col>
          <el-col class="col" :span="4" style="display: flex">
            <el-form-item style="width: 50%; margin-left: 1%">
              <el-checkbox v-model="formData.inStoreTypeId" label="票货不符" />
            </el-form-item>
            <el-form-item style="width: 50%">
              <el-checkbox v-model="formData.isChecked" label="未审核" />
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
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? false : true" @click="exportXml()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/打印.png" />
          导出(F7)
        </el-button>
        <el-button class="buttonStyle" :disabled="!isAdd" @click="clickInsert()">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          新增入库单
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="clickSave()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/保存.png" />
          保存(F3)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="wasterInStoreBill()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/作废.png" />
          作废入库单
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="InStoreBill()">
          <img class="buttonImg" src="@/assets/drugAdministration/drugStorageManagement/审核.png" />
          审核(F8)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="addDetail(TableForm)">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/新增.png" />
          添加药品
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="upDetail(TableForm)">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/修改.png" />
          修改(Ctrl+M)
        </el-button>
        <el-button class="buttonStyle" :disabled="chname == '已审核' ? isshzt : false" @click="wasterInStoreBillDetail()">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/删除.png" />
          删除药品信息
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
          @row-click="tableLefthandleChange"
        >
          <el-table-column align="center" label="状态" prop="status" min-width="140px" show-overflow-tooltip sortable>
            <template #default="{ row }">
              <span :class="row.status === '未审核' ? 'fontStyle' : ''">
                {{ row.status }}
              </span>
            </template>
          </el-table-column>

          <el-table-column align="center" label="入库单号" min-width="140px" show-overflow-tooltip prop="billNo" />
          <el-table-column align="center" label="发票编号" min-width="140px" show-overflow-tooltip prop="invoiceNumber" />
          <el-table-column
            align="center"
            label="发票金额"
            min-width="140px"
            show-overflow-tooltip
            sortable
            prop="invoiceAmount"
          />
          <el-table-column align="center" label="审核人" min-width="140px" show-overflow-tooltip sortable prop="checkName" />
          <el-table-column align="center" label="录单日期" min-width="140px" show-overflow-tooltip sortable prop="createDate" />
          <el-table-column align="center" label="审核日期" min-width="140px" show-overflow-tooltip sortable prop="checkDate" />
          <el-table-column
            align="center"
            label="入库类型"
            min-width="140px"
            show-overflow-tooltip
            sortable
            prop="inStoreTypeName"
          />
          <el-table-column align="center" label="发票时间" min-width="140px" show-overflow-tooltip sortable prop="invoiceDate" />
        </el-table>
      </div>
      <div class="tableRight">
        <div class="tableForm">
          <el-form ref="TableForm" inline label-width="auto" :model="tableForm" :rules="rules">
            <!-- 第一行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="入库类型" prop="inStoreTypeName">
                  <el-select
                    v-model="tableForm.inStoreTypeName"
                    class="type"
                    clearable
                    autofocus
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder=" "
                    @change="storageType"
                  >
                    <el-option v-for="item in storageTypeList" :key="item.id" :label="item.kvmemo" :value="item" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="招标类型" prop="publicSignName">
                  <el-select
                    v-model="tableForm.publicSignName"
                    class="type"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder=" "
                    @change="tenderType"
                  >
                    <el-option v-for="item in tenderTypeList" :key="item.id" :label="item.kvmemo" :value="item" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="12">
                <el-form-item label="供货商" :inline-message="true" prop="supplyName">
                  <el-input
                    v-model="tableForm.supplyName"
                    :disabled="chname == '已审核' ? isshzt : false"
                    style="width: 100%"
                    placeholder="请输入供货商名称"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第二行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="发票编号" :inline-message="true" prop="invoiceNumber">
                  <el-input
                    v-model="tableForm.invoiceNumber"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入非中文..."
                    oninput="this.value=this.value.replace(/[^\w]/g,'')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="发票时间" prop="invoiceDate">
                  <div style="display: flex; flex-direction: row; width: 100%">
                    <el-date-picker
                      v-model="tableForm.invoiceDate"
                      format="YYYY-MM-DD"
                      placeholder="选择日期"
                      style="width: 100%"
                      :disabled="chname == '已审核' ? isshzt : false"
                      type="date"
                      value-format="YYYY-MM-DD"
                    />
                  </div>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item prop="invoiceAmount" label="发票金额">
                  <el-input
                    v-model="tableForm.invoiceAmount"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace(/^\D*(\d*(?:\.\d{0,4})?).*$/g, '$1','')"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第三行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="药品代码" prop="drugCode">
                  <el-input
                    v-model="tableForm.drugCode"
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入药品代码\拼音..."
                    @keyup.enter="handleDrugCode"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="药品名称" prop="drugName">
                  <el-input v-model="tableForm.drugName" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="规格" prop="drugSpecification">
                  <el-input v-model="tableForm.drugSpecification" clearable disabled />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="产地" prop="origin">
                  <el-input
                    ref="chandi"
                    v-model="tableForm.origin"
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入产地"
                  >
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第四行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item label="批发价" :inline-message="true" prop="purchasePrice">
                  <el-input
                    v-model="tableForm.purchasePrice"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,4})?).*$/g, '$1','')"
                    @blur="purchasePriceblur()"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="进价" :inline-message="true" prop="inStorePrice">
                  <el-input
                    v-model="tableForm.inStorePrice"
                    clearable
                    placeholder="可输入小数,不可输入负数..."
                    :disabled="chname == '已审核' ? isshzt : false"
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,4})?).*$/g, '$1','')"
                    @blur="inStorePriceblur()"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="加成率" :inline-message="true" prop="addRate">
                  <el-input
                    v-model="tableForm.addRate"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,4})?).*$/g, '$1','')"
                    @blur="addRateblur()"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="零售价" prop="retailPrice" :inline-message="true">
                  <el-input
                    v-model="tableForm.retailPrice"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="可输入小数,不可输入负数..."
                    oninput="this.value=value.replace( /^\D*(\d*(?:\.\d{0,4})?).*$/g, '$1','')"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第五行 -->
            <el-row>
              <el-col class="col" :span="6">
                <el-form-item style="float: left; width: 75%" label="入库数量" :inline-message="true" prop="number">
                  <el-input
                    v-model="tableForm.number"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入正整数..."
                    oninput="this.value=value.replace(/[^\d]/g,'')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="批号" :inline-message="true" prop="batchNumber">
                  <el-input
                    v-model="tableForm.batchNumber"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入非中文..."
                    oninput="this.value=this.value.replace(/[^\w]/g,'')"
                  />
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="效期" :inline-message="true" prop="validityPeriod">
                  <div style="display: flex; flex-direction: row; width: 100%">
                    <el-date-picker
                      v-model="tableForm.validityPeriod"
                      format="YYYY-MM-DD"
                      placeholder="选择日期"
                      style="width: 100%"
                      :disabled="chname == '已审核' ? isshzt : false"
                      type="date"
                      value-format="YYYY-MM-DD"
                      @change="changeProduceDate"
                    />
                  </div>
                </el-form-item>
              </el-col>
              <el-col class="col" :span="6">
                <el-form-item label="生产日期" :inline-message="true" prop="produceDate">
                  <div style="display: flex; flex-direction: row; width: 100%">
                    <el-date-picker
                      v-model="tableForm.produceDate"
                      format="YYYY-MM-DD"
                      placeholder="选择日期"
                      style="width: 100%"
                      :disabled="chname == '已审核' ? isshzt : false"
                      type="date"
                      @change="changeProduceDate"
                    />
                  </div>
                </el-form-item>
              </el-col>
            </el-row>
            <!-- 第六行 -->
            <el-row>
              <el-col class="col" :span="18">
                <el-form-item label="其他信息" prop="comment">
                  <el-input
                    v-model="tableForm.comment"
                    clearable
                    :disabled="chname == '已审核' ? isshzt : false"
                    placeholder="请输入..."
                  />
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
          v-loading="tableLoadingright"
          highlight-current-row
          stripe
          @row-click="tableRighthandleChange"
        >
          <el-table-column align="center" label="药品代码" min-width="140px" show-overflow-tooltip sortable prop="drugCode" />
          <el-table-column align="center" label="药品名称" min-width="140px" show-overflow-tooltip sortable prop="drugName" />
          <el-table-column align="center" label="生产日期" min-width="140px" show-overflow-tooltip sortable prop="produceDate">
            <template #default="{ row }">
              {{ row?.produceDate ? dayjs(row?.produceDate)?.format("YYYY-MM-DD") : "" }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="规格" min-width="140px" show-overflow-tooltip prop="drugSpecification" />
          <el-table-column align="center" label="入库数量" min-width="140px" show-overflow-tooltip sortable prop="number" />
          <el-table-column align="center" label="单位" min-width="140px" show-overflow-tooltip prop="unitName" />
          <el-table-column align="center" label="进价" min-width="140px" show-overflow-tooltip prop="purchasePrice" />
          <el-table-column align="center" label="零售价" min-width="140px" show-overflow-tooltip prop="retailPrice" />
          <el-table-column align="center" label="进价金额" min-width="140px" show-overflow-tooltip prop="purchaseMoney" />
          <el-table-column align="center" label="零售金额" min-width="140px" show-overflow-tooltip prop="retailMoney" />
          <el-table-column align="center" label="批号" min-width="140px" show-overflow-tooltip sortable prop="batchNumber" />
          <el-table-column align="center" label="效期" min-width="140px" show-overflow-tooltip sortable prop="validityPeriod">
            <template #default="{ row }">
              {{ row?.validityPeriod ? dayjs(row?.validityPeriod)?.format("YYYY-MM-DD") : "" }}
            </template>
          </el-table-column>
          <el-table-column align="center" label="产地" min-width="140px" show-overflow-tooltip prop="origin" />
          <el-table-column align="center" label="批次" min-width="140px" show-overflow-tooltip prop="batch" />
          <el-table-column align="center" label="供应商" min-width="140px" show-overflow-tooltip prop="supplyName" />
          <el-table-column align="center" label="其他信息" min-width="140px" show-overflow-tooltip prop="comment" />
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
// 表单部分
:deep(.el-form-item__label) {
  width: v-bind(labelWidth) !important;
  padding: 0 9px 0 0;
  // width:75px;
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
  }
  // 右边部分
  .tableRight {
    width: 69.5% !important;
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
        :deep(.el-form-item__label) {
          // width: 75px;
          padding: 0 9px 0 0;
        }

        :deep(.stockamount .el-input__inner) {
          -webkit-text-fill-color: red;
          font-weight: bold;
        }
      }
    }
    :deep(.el-form-item .el-form-item__label-wrap) {
      margin-left: 0px !important;
    }

    :deep(.el-form-item__label:before) {
      padding-top: 3px;
      font-size: 1.25rem;
    }
    // 右边表格
    //常规
    .rightTableDStyle {
      height: calc((100vh - 515px));
    }
  }
}

:deep(.el-form-item__label) {
  width: v-bind(labelWidth) !important;
  padding: 0 9px 0 0;
  // width:75px;
}

// 表格字体大小
:deep(.cell) {
  color: #606266;
}

.fontStyle {
  font-weight: 700;
  color: #dd5861;
}
</style>
