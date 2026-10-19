<script lang="ts" setup>
import {
  getInStoreBillInfoWithPageFilter,
  getOutStoreBillInfoWithPageFilter
} from "@/api/drugAdministration/inwardoutboundquery";
import { exportDrugExpiryTable, exportTwoExpiryTable } from "@/utils/drugAdministration/excel";
import { throttle } from "echarts";
import { ElMessage, ElTable } from "element-plus";
import { computed, nextTick, reactive, ref, watch } from "vue";

const dateList = ref([]);

//查询条件
const searchForm: any = reactive({
  isInoutCheck: "按入库", //判断是按入库还是出库 默认入库
  startDate: dateList?.value[0], //开始时间
  endDate: dateList?.value[1], //结束时间
  supplyName: "", //供货商信息
  drugname: "", //药品信息
  drugclass: "", //药品分类
  invoiceNumber: "", //发票号
  deptId: "", //出库部门id
  dateType: "0" //类型日期   0审核日期/1发票日期
});
const tableData = ref([]); //表格数据
const tableDataRef: any = ref<InstanceType<typeof ElTable>>(); //高亮  //表格ref
const tableDatacurrentRow: any = ref(""); //当前行
const currentPage: any = ref(1); // 当前页码
const pageSize: any = ref(10); // 分页条数
const total: any = ref(0); // 表格总条数
const tableLoading = ref(false); //表格是否显示加载状态
const isrk = ref(false); //按入库判断显示隐藏  //是入库
const isck = ref(false); //按出库判断显示隐藏   //是出库
const deptName = ref(""); //出库相对部门名称
const outdeptnameList = ref([
  { id: "", deptName: "无" },
  { id: "102", deptName: "西药房" },
  { id: "202", deptName: "住院药房" },
  { id: "302", deptName: "中药房" },
  { id: "402", deptName: "病区药房" }
]); //出库部门名称列表

/** 选中按入库/出库 组件的变化(隐藏与显示) */
watch(
  () => searchForm.value.isInoutCheck,
  newVal => {
    if (newVal === "按入库") {
      isrk.value = true;
      isck.value = false;
      total.value = 0;
    } else if (newVal === "按出库") {
      isrk.value = false;
      isck.value = true;
      total.value = 0;
    }
  }
);

// 可选：计算属性返回状态
const isfocusrk = computed(() => {
  return searchForm.value.isInoutCheck === "按入库";
});
/** 日期选择器改变时 */
const changeDate = () => {
  if (searchForm.startDate && searchForm.endDate) {
    // searchForm.endDate = isEndTimeMidnight(searchForm.endDate);
    // if (checkDateTime(searchForm.startDate, searchForm.endDate)) {
    //   ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
    //   return;
    // }
  } else {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "error" });
    return;
  }
};
/** 查询功能 */
function selectBtn() {
  if (!searchForm.startDate || !searchForm.endDate) {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "error" });
    return;
  } else {
    if (searchForm.isInoutCheck == "按出库") {
      // console.log('按出库查询数据')
      InOrOutStoreBillInfoByOutWithPageFilter(0); //出库分页查询函数
    } else if (searchForm.isInoutCheck == "按入库") {
      // console.log('按入库查询数据')
      InOrOutStoreBillInfoByInWithPageFilter(0); //入库分页查询函数
    }
  }
}
/** 调接口-出库分页查询 */
const InOrOutStoreBillInfoByOutWithPageFilter = (count: number) => {
  if (count == 1) {
    currentPage.value = 1;
  }
  tableLoading.value = true;
  ////调接口查询出库
  getOutStoreBillInfoWithPageFilter(
    searchForm.deptId,
    "0521",
    searchForm.startDate,
    searchForm.endDate,
    searchForm.supplyName,
    searchForm.drugname,
    pageSize.value,
    currentPage.value
  )
    .then((res: any) => {
      if (res.code === "200") {
        tableData.value = res.data.DefineGetInOrOutStoreBillInfoByOut;
        tableDatacurrentRow.value = tableData.value[0];
        total.value = res.data.total; //总数
        if (tableDatacurrentRow.value == null || tableDatacurrentRow.value == "") {
          tableData.value = [];
          total.value = 0; //总数
          ElMessage({ message: "暂无数据", type: "success" });
        } else {
          // 高亮第一行
          nextTick(() => {
            tableDataRef.value.setCurrentRow(tableData.value[0]);
          });
        }
        tableLoading.value = false;
      } else {
        tableLoading.value = false;
        tableData.value = [];
        total.value = 0; //总数
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    })
    .catch(err => {
      tableLoading.value = false;
      tableData.value = [];
      total.value = 0; //总数
      ElMessage({ message: err || "error", type: "error" });
    });
};
/** 调接口-入库分页查询 */
const InOrOutStoreBillInfoByInWithPageFilter = (count: number) => {
  if (count == 1) {
    currentPage.value = 1;
  }
  tableLoading.value = true;
  getInStoreBillInfoWithPageFilter(
    "0521",
    searchForm.startDate,
    searchForm.endDate,
    searchForm.dateType,
    searchForm.invoiceNumber,
    searchForm.supplyName,
    searchForm.drugname,
    pageSize.value,
    currentPage.value
  )
    .then((res: any) => {
      if (res.code === "200") {
        tableData.value = res.data.DefineGetInOrOutStoreBillInfoByIn;
        tableDatacurrentRow.value = tableData.value[0];
        total.value = res.data.total; //总数
        if (tableDatacurrentRow.value == null || tableDatacurrentRow.value == "") {
          tableData.value = [];
          total.value = 0; //总数
          ElMessage({ message: "暂无数据", type: "success" });
        } else {
          // 高亮第一行
          nextTick(() => {
            tableDataRef.value.setCurrentRow(tableData.value[0]);
          });
        }
        tableLoading.value = false;
      } else {
        tableLoading.value = false;
        tableData.value = [];
        total.value = 0; //总数
        ElMessage({ message: res.msg || res.message, type: "error" });
      }
    })
    .catch((err: any) => {
      tableLoading.value = false;
      tableData.value = [];
      total.value = 0; //总数
      ElMessage({ message: err || "error", type: "error" });
    });
};
const tableDatadcxml: any = ref([]); //导出的列表
/** 点击导出 */
const Xml = throttle(() => {
  tableDatadcxml.value = [];
  if (tableData.value.length > 0) {
    tableDatadcxml.value = tableData.value;
  }
  if (searchForm.isInoutCheck == "按入库") {
    XmlrkData();
  } else {
    XmlckData();
  }
}, 3000);
/** 导出入库数据到excel */
function XmlrkData() {
  const headersArr = [
    "部门名称",
    "单据号",
    "发票号",
    "发票日期",
    "药品代码",
    "药品名称",
    "规格",
    "产地",
    "供货商代码",
    "供货商",
    "进价",
    "零售价",
    "数量",
    "单位",
    "效期",
    "批准文号",
    "剂型",
    "批号",
    "标志",
    "单据日期",
    "进价金额",
    "零售金额",
    "统筹编码"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    String(item.deptName || ""),
    String(item.billNo || ""),
    String(item.invoicenumber || ""),
    String(item.invoiceDate || ""),
    String(item.drugCode || ""),
    String(item.drugName || ""),
    String(item.drugSpecification || ""),
    String(item.origin || ""),
    String(item.supplyCode || ""),
    String(item.supplyName || ""),
    String(item.purchasePrice || ""),
    String(item.retailPrice || ""),
    String(item.number_ || ""),
    String(item.unitName || ""),
    String(item.validityPeriod || ""),
    String(item.appRovalNumber || ""),
    String(item.drugRouteName || ""),
    String(item.batchNumber || ""),
    String(item.flag || ""),
    String(item.inputDate || ""),
    String(item.purchaseMoney || ""),
    String(item.retailMoney || ""),
    String(item.insureCode || "")
  ]);
  //标题不要超过30个字符，否则报错
  exportDrugExpiryTable(headersArr, data, "浦东测试机构 入库查询明细");
}
/** 导出出库数据到excel */
function XmlckData() {
  const headersArr = [
    "部门名称",
    "单据号",
    "相对机构名称",
    "相对部门名称",
    "药品代码",
    "药品名称",
    "规格",
    "产地",
    "供货商代码",
    "供货商",
    "进价",
    "零售价",
    "数量",
    "单位",
    "效期",
    "批准文号",
    "剂型",
    "批号",
    "标志",
    "单据日期",
    "进价金额",
    "零售金额",
    "统筹编码"
  ];
  const data = tableDatadcxml.value.map((item: any) => [
    String(item.deptName || ""),
    String(item.billNo || ""),
    String(item.receivingOrgaName || ""),
    String(item.receivingDeptName || ""),
    String(item.drugCode || ""),
    String(item.drugName || ""),
    String(item.drugSpecification || ""),
    String(item.origin || ""),
    String(item.supplyCode || ""),
    String(item.supplyName || ""),
    String(item.purchasePrice || ""),
    String(item.retailPrice || ""),
    String(item.number_ || ""),
    String(item.unitName || ""),
    String(item.validityPeriod || ""),
    String(item.appRovalNumber || ""),
    String(item.drugRouteName || ""),
    String(item.batchNumber || ""),
    String(item.flag || ""),
    String(item.inputDate || ""),
    String(item.purchaseMoney || ""),
    String(item.retailMoney || ""),
    String(item.insureCode || "")
  ]);
  //标题不要超过30个字符，否则报错
  exportDrugExpiryTable(headersArr, data, "浦东测试机构 出库查询明细");
}

/** 点击表格时换行 */
const tableDataChange = (val: any) => {
  tableDatacurrentRow.value = val;
};
/** 切换页数 */
function handleCurrentChange(val: number) {
  currentPage.value = val;
  if (searchForm.isInoutCheck == "按出库") {
    // console.log('按出库查询数据')
    InOrOutStoreBillInfoByOutWithPageFilter(0);
  } else if (searchForm.isInoutCheck == "按入库") {
    // console.log('按入库查询数据')
    InOrOutStoreBillInfoByInWithPageFilter(0);
  }
}
/** 切换每页条数 */
function handleSizeChange(val: number) {
  pageSize.value = val;
  if (searchForm.isInoutCheck == "按出库") {
    // console.log('按出库查询数据')
    InOrOutStoreBillInfoByOutWithPageFilter(1);
  } else if (searchForm.isInoutCheck == "按入库") {
    // console.log('按入库查询数据')
    InOrOutStoreBillInfoByInWithPageFilter(1);
  }
}
/** 出库相对部门改变时 */
const changeoutdept = (val: any) => {
  searchForm.deptId = val.id;
  deptName.value = val.deptName;
  // console.log(searchForm.depardept, deptName.value, '出库相对部门')
};
/** 出入库类型改变时 */
const changeType = () => {
  console.log(searchForm.isInoutCheck, "切换类型");
  if (searchForm.isInoutCheck === "按入库") {
    isrk.value = true;
    isck.value = false;
    total.value = 0;
  } else if (searchForm.isInoutCheck === "按出库") {
    isrk.value = false;
    isck.value = true;
    total.value = 0;
    searchForm.dateType = "0";
  }
  selectBtn();
  // console.log(searchForm.dateType, "切换日期类型");
};
</script>

<template>
  <div class="main" tabindex="0">
    <el-card shadow="never" class="top">
      <el-form inline label-width="auto" :model="searchForm">
        <el-row>
          <el-col :span="6" style="display: flex; align-items: center">
            <el-form-item>
              <el-radio-group v-model="searchForm.isInoutCheck" @change="changeType">
                <el-radio :class="isfocusrk" label="按入库" style="width: 35%"> 按入库 </el-radio>
                <el-radio :class="isfocusrk" label="按出库" style="width: 35%"> 按出库 </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8" style="display: flex; align-items: center; flex-wrap: nowrap; gap: 10px">
            <el-form-item style="margin-bottom: 0">
              <el-select v-model="searchForm.dateType" placeholder="请选择日期类型" style="width: 100px" :disabled="isck">
                <el-option label="审核日期" value="0" />
                <el-option label="发票日期" value="1" />
              </el-select>
            </el-form-item>
            <div style="display: flex; align-items: center; gap: 8px">
              <el-date-picker
                v-model="searchForm.startDate"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="开始时间"
                style="width: 150px"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                @change="changeDate"
              />
              至
              <el-date-picker
                v-model="searchForm.endDate"
                format="YYYY-MM-DD HH:mm:ss"
                placeholder="结束时间"
                style="width: 150px"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                @change="changeDate"
              />
            </div>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
    <el-card shadow="never" class="button-box">
      <div style="margin-bottom: -17px">
        <div style="display: inline-block; width: 59%">
          <el-form :model="searchForm">
            <el-row>
              <el-col :span="7">
                <el-form-item label="供货商" style="margin-left: 10px">
                  <el-input ref="supplytj" v-model="searchForm.supplyName" clearable placeholder="请输入供货商代码\拼音...">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7" style="margin-left: 20px">
                <el-form-item label="药品">
                  <el-input ref="drugn" v-model="searchForm.drugname" clearable placeholder="请输入药品代码/拼音"> </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7" style="margin-left: 20px" v-if="isrk">
                <el-form-item label="发票号">
                  <el-input clearable placeholder="请输入..." v-model="searchForm.invoiceNumber" @keyup.enter="selectBtn()" />
                </el-form-item>
              </el-col>
              <el-col :span="8" style="margin-left: 20px" v-if="isck">
                <el-form-item label="出库相对部门">
                  <el-select v-model="deptName" filterable placeholder=" " @change="changeoutdept">
                    <el-option v-for="item in outdeptnameList" :key="item.id" :label="item.deptName" :value="item" />
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <div style="display: inline-block; width: 41%">
          <el-button class="buttonStyle" @click="selectBtn()">
            <img class="buttonImg" src="@/assets/drugAdministration/inwardoutboundquery/查询.png" />
            查询(F5)
          </el-button>
          <el-button class="buttonStyle" @click="Xml()">
            <img class="buttonImg" src="@/assets/drugAdministration/inwardoutboundquery/导出.png" />
            导出
          </el-button>
        </div>
      </div>
    </el-card>

    <el-card shadow="never" class="table-box">
      <el-table
        border
        ref="tableDataRef"
        class="firstTables"
        :data="tableData"
        v-loading="tableLoading"
        stripe
        highlight-current-row
        :cell-style="{ boxSizing: 'border-box', padding: '5px' }"
        :row-style="{ height: '38px' }"
        :header-row-style="{ height: '40px' }"
        @row-click="tableDataChange"
      >
        <!-- <el-table-column align="center" label="部门名称" prop="deptName" min-width="140px" show-overflow-tooltip /> -->
        <el-table-column align="center" label="单据号" prop="billNo" min-width="100px" show-overflow-tooltip />
        <!-- <el-table-column v-if="isrk" align="center" label="发票号" prop="invoicenumber" min-width="100px" show-overflow-tooltip /> -->
        <!-- <el-table-column v-if="isrk" align="center" label="发票日期" prop="invoiceDate" min-width="140px" show-overflow-tooltip /> -->
        <!-- <el-table-column
          v-if="isck"
          align="center"
          label="相对机构名称"
          prop="receivingOrgaName"
          min-width="160px"
          show-overflow-tooltip
        />
        <el-table-column
          v-if="isck"
          align="center"
          label="相对部门名称"
          prop="receivingDeptName"
          min-width="160px"
          show-overflow-tooltip
        /> -->
        <el-table-column align="center" label="药品代码" prop="drugCode" min-width="140px" show-overflow-tooltip />
        <el-table-column align="center" label="药品名称" prop="drugName" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="规格" prop="drugSpecification" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="产地" prop="origin" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="供货商代码" prop="supplyCode" min-width="150px" show-overflow-tooltip />
        <el-table-column align="center" label="供货商" prop="supplyName" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="进价" prop="purchasePrice" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="零售价" prop="retailPrice" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="数量" prop="number" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="单位" prop="unitName" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="效期" prop="validityPeriod" min-width="120px" show-overflow-tooltip />
        <!-- <el-table-column align="center" label="批准文号" prop="appRovalNumber" min-width="120px" show-overflow-tooltip /> -->
        <!-- <el-table-column align="center" label="剂型" prop="drugRouteName" min-width="120px" show-overflow-tooltip /> -->
        <el-table-column align="center" label="批号" prop="batchNumber" min-width="120px" show-overflow-tooltip />
        <!-- <el-table-column align="center" label="标志" prop="flag" min-width="120px" show-overflow-tooltip /> -->
        <el-table-column align="center" label="单据日期" prop="inputDate" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="进价金额" prop="purchaseMoney" min-width="120px" show-overflow-tooltip />
        <el-table-column align="center" label="零售金额" prop="retailMoney" min-width="120px" show-overflow-tooltip />
        <!-- <el-table-column align="center" label="统筹编码" prop="insureCode" min-width="120px" show-overflow-tooltip /> -->
      </el-table>
      <el-pagination
        :background="true"
        class="flex justify-end mt-4"
        :current-page="currentPage"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        size="small"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </el-card>
  </div>
</template>

<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  min-height: calc(100vh - 146px);
  padding: 0 !important;

  :deep(.top) {
    margin-bottom: 5px;
    background: white;
    // .el-card__body {
    //   padding: 7px;
    // }
    .el-form {
      .el-form-item {
        width: -webkit-fill-available;
        margin-right: 10px;
        margin-bottom: 0;
      }
      .end-time {
        .el-form-item__label-wrap {
          margin-left: 0 !important;
        }
      }
      .operation-time {
        flex: 1;
        //   max-width: 160px;
      }
      .data-button {
        padding: 5px;
        margin-left: 3px;
      }
      .el-select {
        width: 100%;
      }
    }

    :deep(.el-form-item) {
      .el-input__wrapper {
        width: 100%;
      }
    }
  }

  // 按钮部分
  .button-box {
    text-align: right;
    background: white;
    margin-bottom: 5px;
    .buttonImg {
      width: 10px;
      margin-right: 5px;
    }
  }

  :deep(.table-box) {
    .el-card__body {
      padding: 0;
    }
    margin: auto;
    background-color: #fff;
    .firstTables {
      height: calc(100vh - 272px);
      // width: v-bind(labelWidth);
    }
    .el-pagination {
      margin: 5px;
      //width: v-bind(labelWidth);
    }
  }

  // 表格字体大小
  :deep(.cell) {
    color: #606266;
  }
  //   计算部分
  // :deep(.calculate) {
  //   padding: 10px;
  //   margin: 0 10px 10px 10px;
  //   text-align: right;
  //   background: white;
  //   .el-card__body {
  //     display: flex;
  //     justify-content: space-between;
  //   }
  //   .cal-right {
  //     display: flex;
  //     flex: 1;
  //     justify-content: flex-end;
  //     .num {
  //       display: flex;
  //       align-items: center;
  //       margin: 0 20px;
  //       .right-text {
  //         margin: 0px 5px;
  //         color: #a0a5aa;
  //       }
  //       .red-total {
  //         font-weight: bold;
  //         color: #dd5861;
  //       }
  //     }
  //   }
  // }

  :deep() {
    .top,
    .button-box,
    .calculate {
      .el-card__body {
        padding: 10px;
      }
    }
  }
}
//控制条件里的供货商弹出表格高度
.tjradioStyle {
  height: 400px;
}
//控制药品代码弹出表格高度
.drugcodeStyle {
  height: 400px;
}
</style>
<style rel="stylesheet/less">
/* 去除级联选择器的单选框并点击文字可选择  */
.el-cascader-panel .el-radio {
  position: absolute;
  top: 10px;
  right: 10px;
  z-index: 10;
  width: 100%;
  height: 100%;
}
.el-cascader-panel .el-radio__input {
  visibility: hidden;
}
.el-cascader-panel .el-cascader-node__postfix {
  top: 10px;
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
