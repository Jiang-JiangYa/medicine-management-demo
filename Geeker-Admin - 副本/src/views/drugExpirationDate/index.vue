<script setup lang="ts">
import {
  storeBatchInfoList,
  storeBatchInfoListWithPageFilter,
  updateStoreBatchInfo
} from "@/api/drugAdministration/drugExpirationDate";
import { checkDate, getNowTimeInterval, isEndTimeMidnight } from "@/utils/drugAdministration";
import { exportDrugExpiryTable } from "@/utils/drugAdministration/excel";
import { ElMessage, FormInstance, FormRules } from "element-plus";
import { nextTick, onMounted, reactive, ref } from "vue";
import dayjs from "dayjs";
const dateList = ref<[Date, Date]>(getNowTimeInterval());
/**表单数据 */
const formData = reactive({
  orgaId: "0521",
  deptId: "101",
  isStock: "是", // 药库
  startDate: dateList?.value[0],
  endDate: dateList?.value[1],
  batchNumber: "", //批号
  drugNameOrCode: "" //药品名称/代码
});
const listLoading = ref(false); //是否加载列表
const mediStockBatchInfoRef = ref<any>(null); //表格REF
const mediStockBatchInfoList = ref([]); //表格数据
const mediStockBatchInfoData = ref<any>(null); //表格当前行信息
const exportList = ref([]); //导出数据列表
const pageIndex = ref(1); //当前页
const pageSize = ref(10);
const total = ref(0); //表格数据总数
const editDialogVisible = ref(false); //修改对话框是否可视
const editForm = reactive({
  //编辑对话框 表单绑定值
  drugName: "",
  produceDate: "",
  validityPeriod: "",
  batchNumber: ""
});
const ruleFormRef = ref<any>(null); //对话框ref
/** 对话框表单校验规则 */
const editRules = reactive<FormRules>({
  produceDate: [
    {
      type: "date",
      required: true,
      message: "请选择生产日期",
      trigger: "change"
    }
  ],
  validityPeriod: [
    {
      type: "date",
      required: true,
      message: "请选择效期",
      trigger: "change"
    }
  ],
  batchNumber: [
    {
      required: true,
      message: "请输入批号",
      trigger: "blur"
    }
  ]
});

/** 时间改变时 */
const changeDate = () => {
  if (formData.startDate && formData.endDate) {
    formData.endDate = isEndTimeMidnight(formData.endDate);
    if (checkDate(formData.startDate, formData.endDate)) {
      ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
      return;
    }
  } else {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "warning" });
    return;
  }
};
/** 生产日期发生改变时 */
const changeProduceDate = () => {
  if (editForm.produceDate && editForm.validityPeriod) {
    if (checkDate(editForm.produceDate, editForm.validityPeriod)) {
      ElMessage({ message: "生产日期要小于效期时间!", type: "warning" });
      return false;
    }
    if (dayjs(editForm.produceDate).format("YYYY-MM-DD") === dayjs(editForm.validityPeriod).format("YYYY-MM-DD")) {
      ElMessage({ message: "生产日期不能等于效期时间!", type: "warning" });
      return false;
    }
  } else {
    ElMessage({ message: "生产日期或效期不能为空!", type: "warning" });
    return false;
  }
  return true;
};
/**点击查询触发 */
const goSearch = (): void => {
  if (!formData.startDate || !formData.endDate) {
    ElMessage({ message: "开始时间或结束时间不能为空!", type: "warning" });
    return;
  } else if (checkDate(formData.startDate, formData.endDate)) {
    ElMessage({ message: "开始时间要小于结束时间!", type: "warning" });
    return;
  } else {
    getSearch(1); //调接口
  }
};
/**调接口-查询主表。 传1设置为焦点回到第一条 */
const getSearch = async (type: number) => {
  listLoading.value = true;
  //调接口---
  await storeBatchInfoListWithPageFilter({
    orgaId: "0521",
    deptId: "101",
    // isDrugStore: formData.isDrugStore,
    isStock: formData.isStock,
    startDate: `${formData.startDate}${"T00:00:00"}`,
    endDate: `${formData.endDate}${"T23:59:59"}`,
    batchNumber: formData.batchNumber,
    drugNameOrCode: formData.drugNameOrCode?.toUpperCase(),
    pageIndex: pageIndex.value,
    pageSize: pageSize.value
  })
    .then(async (res: any) => {
      if (res.code === "200") {
        if (res.data?.MediStockBatchInfo?.length > 0) {
          mediStockBatchInfoList.value = res.data.MediStockBatchInfo;
          total.value = res.data.listCount;
          mediStockBatchInfoData.value = mediStockBatchInfoList.value[0];
          mediStockBatchInfoRef.value.setCurrentRow(mediStockBatchInfoList.value[0]);
        } else {
          ElMessage({ message: "暂无数据!", type: "success" });
          await cancel();
        }
      } else if (res.code === "10404") {
        ElMessage({ message: "暂无数据!", type: "success" });
        await cancel();
      } else {
        ElMessage({ message: res.msg, type: "error" });
      }
      listLoading.value = false;
    })
    .catch(e => {
      ElMessage({ message: "主表信息查询失败！", type: "error" });
      throw e;
    });
};
/** 初始化 */
const cancel = () => {
  mediStockBatchInfoList.value = [];
  mediStockBatchInfoData.value = null;
  total.value = 0;
};
/** 点击修改时 */
const handleEdit = () => {
  if (formData.isStock === "是") {
    if (!mediStockBatchInfoData.value) {
      ElMessage({ message: "请先选择一条信息！", type: "error" });
      return;
    }
    getAssignment(); //修改前的赋值
  }
};
/** 修改前的赋值 */
const getAssignment = () => {
  editForm.drugName = mediStockBatchInfoData.value?.drugName;
  editForm.produceDate = mediStockBatchInfoData.value?.produceDate;
  editForm.validityPeriod = mediStockBatchInfoData.value?.validityPeriod;
  editForm.batchNumber = mediStockBatchInfoData.value?.batchNumber;
  editDialogVisible.value = true;
};
/**点击导出时    处理excel */
const handleExcel = () => {
  getExcelSearch(); //调接口
};
/**调接口-准备导出的数据 */
const getExcelSearch = async () => {
  //////调接口
  await storeBatchInfoList({
    orgaId: "0521",
    deptId: "101",
    // isDrugStore: formData.isDrugStore,
    isStock: formData.isStock,
    startDate: `${formData.startDate}${"T00:00:00"}`,
    endDate: `${formData.endDate}${"T23:59:59"}`,
    batchNumber: formData.batchNumber,
    drugNameOrCode: formData.drugNameOrCode?.toUpperCase()
  })
    .then(async (res: any) => {
      if (res.code === "200") {
        if (res.data?.MediStockBatchInfo?.length > 0) {
          exportList.value = res.data.MediStockBatchInfo;
          handleExport();
        } else {
          ElMessage({ message: "暂无导出数据!", type: "success" });
        }
      } else if (res.code === "10404") {
        ElMessage({ message: "暂无导出数据!", type: "success" });
      } else {
        ElMessage({ message: res.msg, type: "error" });
      }
      listLoading.value = false;
    })
    .catch(e => {
      ElMessage({ message: "导出失败！", type: "error" });
      throw e;
    });
};

/** 导出到浏览器 */
const handleExport = () => {
  const exportData = exportList.value;
  const headerList = [
    "部门名称",
    "药品代码",
    "药品名称",
    "规格",
    "批号",
    "生产日期",
    "有效期",
    "库存量",
    "进价",
    "零售价",
    "厂家"
  ];
  const dataList = exportData.map((item: any) => [
    item.deptName,
    item.drugCode,
    item.drugName,
    item.drugSpecification,
    item.batchNumber,
    item.produceDate,
    item.validityPeriod,
    item.number + item.unitName,
    item.purchasePrice,
    item.retailMoney,
    item.origin
  ]);
  exportDrugExpiryTable(headerList, dataList, "浦东测试机构药品效期管理");
};
/** 点击表格换行 */
const handleRowClick = (row: any) => {
  mediStockBatchInfoData.value = row;
};
/** 切换页数 */
const handleCurrentPage = (val: number) => {
  pageIndex.value = val;
  getSearch(1);
};
/** 切换条数 */
const handleCurrentSize = (val: number) => {
  pageSize.value = val;
};
/** 关闭对话框 */
const handleClose = () => {
  editDialogVisible.value = false;
};
/** 弹窗点击确定时 */
const handleDetermine = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid: any) => {
    if (valid) {
      const canProceed = changeProduceDate();
      if (!canProceed) return;
      editDialogVisible.value = false;
      await getUpdateDrugAndStoreBatchInfo(); //调接口修改
    }
  });
};
/** 调接口--修改数据 */
const getUpdateDrugAndStoreBatchInfo = async () => {
  ///调接口
  await updateStoreBatchInfo({
    // isDrugStore: formData.isDrugStore,
    isStock: formData.isStock,
    MediStockBatchInfo:
      formData.isStock === "是"
        ? {
            ...mediStockBatchInfoData.value,
            produceDate: editForm.produceDate,
            validityPeriod: editForm.validityPeriod,
            batchNumber: editForm.batchNumber
          }
        : null
  })
    .then(async (res: any) => {
      if (res.code === "200") {
        ElMessage({ message: "修改成功!", type: "success" });
        getSearch(0);
      } else {
        ElMessage({ message: res.msg, type: "error" });
      }
    })
    .catch(e => {
      ElMessage({ message: "修改失败！", type: "error" });
      throw e;
    });
};
onMounted(async () => {
  //页面初始化时，将endDate设为三年后
  // if (dateList.value && dateList.value[1]) {
  //   const originalEndDate = dateList.value[1];
  //   const newEndDate = new Date(originalEndDate);
  //   newEndDate.setFullYear(newEndDate.getFullYear() + 3); // 年份加三年
  //   formData.endDate = newEndDate;
  // }
  getSearch(1);
});
</script>

<template>
  <div class="main">
    <div class="form">
      <el-form ref="infoFormRef" class="formStyle" inline label-width="auto" :model="formData">
        <el-row :gutter="10">
          <el-col :span="5">
            <el-form-item label="医疗机构" prop="orgaId">
              <el-select v-model="formData.orgaId" clearable style="flex: 1; margin-right: 10px">
                <el-option label="浦东测试机构" value="0521" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item label="部门" prop="deptId">
              <el-select v-model="formData.deptId" clearable style="flex: 1; margin-right: 10px">
                <el-option label="西药库" value="101" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="">
              <el-date-picker
                v-model="formData.startDate"
                format="YYYY-MM-DD"
                placeholder="开始时间"
                style="flex: 1"
                type="date"
                value-format="YYYY-MM-DD"
                @change="changeDate"
              />
              至
              <el-date-picker
                v-model="formData.endDate"
                format="YYYY-MM-DD"
                placeholder="结束时间"
                style="flex: 1"
                type="date"
                value-format="YYYY-MM-DD"
                @change="changeDate"
              />
            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-form-item label="批号">
              <el-input v-model="formData.batchNumber" clearable placeholder="请输入批号" />
            </el-form-item>
          </el-col>
          <el-col :span="3" style="display: flex; text-align: right">
            <el-form-item label="药品">
              <el-input v-model="formData.drugNameOrCode" clearable placeholder="请输入药品" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="button">
      <div style="display: inline-block">
        <el-button @click="goSearch">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/查询.png" />
          查询(F5)
        </el-button>

        <el-button @click="handleEdit">
          <img class="buttonImg" src="@/assets/drugAdministration/orgaDrugContents/修改.png" />
          修改(F8)
        </el-button>
        <el-button @click="handleExcel"> 导出EXcel(F6) </el-button>
      </div>
    </div>
    <div class="Table">
      <el-table
        ref="mediStockBatchInfoRef"
        v-loading="listLoading"
        border
        class="leftTableD"
        :data="mediStockBatchInfoList"
        highlight-current-row
        stripe
        @row-click="handleRowClick"
      >
        <el-table-column align="center" label="部门名称" min-width="180" prop="deptName" show-overflow-tooltip />
        <el-table-column align="center" label="药品代码" min-width="140" prop="drugCode" show-overflow-tooltip />
        <el-table-column align="center" label="药品名称" min-width="140" prop="drugName" show-overflow-tooltip />
        <el-table-column align="center" label="规格" min-width="150" prop="drugSpecification" show-overflow-tooltip />
        <el-table-column align="center" label="厂家" min-width="160" prop="origin" show-overflow-tooltip />
        <el-table-column align="center" label="生产日期" min-width="120" prop="produceDate">
          <template #default="{ row }">
            {{ row?.produceDate ? dayjs(row?.produceDate)?.format("YYYY-MM-DD") : "" }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="效期" min-width="120" prop="validityPeriod">
          <template #default="{ row }">
            {{ row?.validityPeriod ? dayjs(row?.validityPeriod)?.format("YYYY-MM-DD") : "" }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="库存量" min-width="130" show-overflow-tooltip>
          <template #default="{ row }"> {{ row.number }}{{ row.unitName }} </template>
        </el-table-column>
        <el-table-column align="center" label="批号" min-width="130" prop="batchNumber" show-overflow-tooltip />
        <el-table-column align="center" label="进价" min-width="120" prop="purchasePrice" />
        <el-table-column align="center" label="零售价" min-width="120" prop="retailMoney" show-overflow-tooltip />
      </el-table>
      <el-pagination
        :background="true"
        class="flex justify-end mt-4"
        :current-page="pageIndex"
        layout="total, sizes, prev, pager, next, jumper"
        :page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        size="small"
        :total="total"
        @current-change="handleCurrentPage"
        @size-change="handleCurrentSize"
      />
    </div>
    <el-dialog v-model="editDialogVisible" :before-close="handleClose" title="药品效期及批号编辑" width="30%">
      <el-form ref="ruleFormRef" :model="editForm" :rules="editRules">
        <el-form-item label="药品名称: " label-width="100" prop="drugName">
          <el-input v-model="editForm.drugName" clearable disabled placeholder="请输入..." style="width: 100%" />
        </el-form-item>
        <el-form-item label="生产日期: " label-width="100" prop="produceDate">
          <el-date-picker
            v-model="editForm.produceDate"
            format="YYYY-MM-DD"
            placeholder="请选择生产日期"
            type="date"
            value-format="YYYY-MM-DD"
            @change="changeProduceDate"
          />
        </el-form-item>
        <el-form-item label="效期: " label-width="100" prop="validityPeriod">
          <el-date-picker
            v-model="editForm.validityPeriod"
            format="YYYY-MM-DD"
            placeholder="请选择效期"
            type="date"
            value-format="YYYY-MM-DD"
            @change="changeProduceDate"
          />
        </el-form-item>
        <el-form-item label="批号: " label-width="100" prop="batchNumber">
          <el-input v-model="editForm.batchNumber" clearable placeholder="请输入批号..." style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="handleDetermine(ruleFormRef)"> 确定 </el-button>
          <el-button type="primary" @click="editDialogVisible = false"> 取消 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<style lang="scss" scoped>
.main {
  min-height: calc(100vh - 132px);
  padding: 0 !important;
}
// 上部分表单部分
.form {
  width: 98%;
  padding: 8px 15px;
  margin: auto;
  background: white;
  .formStyle {
    :deep(.el-form-item) {
      width: -webkit-fill-available;
      margin-bottom: 0px;
      .el-input__wrapper {
        width: 100%;
      }
    }
  }
}
:deep(.el-form-item__label-wrap) {
  margin-left: 0px !important;
}
:deep(.el-form--inline .el-form-item) {
  margin-right: 0;
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
  width: 10px;
  margin-right: 5px;
}
// 表格部分
.Table {
  margin: auto;
  margin-top: 7px;
  background-color: #fff;
  .leftTableD {
    height: calc(100vh - 290px) !important;
  }
}
//页签
:deep(.el-pagination) {
  padding: 10px 0;
  margin: 0 !important;
  justify-content: center;
}
</style>
