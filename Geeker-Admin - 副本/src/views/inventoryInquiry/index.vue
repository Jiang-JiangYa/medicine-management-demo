<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue";
import dayjs from "dayjs";
import { ElMessage, ElMessageBox, ElTable } from "element-plus";
import { getMediCatalogInfoList } from "@/api/drugAdministration/orgaDrugContents";
import { btnQueryDetail, btnQueryMaster } from "@/api/drugAdministration/inventoryInquiry";
/* eslint-disable */
const formData = reactive({
  mechanism: "毕设演示机构", //医疗机构
  deptName: "西药库", //部门名称
  deptId: "", //部门ID
  drugStoreInputCode: "", //药品名称(药品输入码)
  sCatalogID: "", //药品分类ID
  addRateNum: "-1", //基药标志(零差率  -1为全部  0为零差 1为非零差)
  sort: "true", //排序(按正序：true，按倒序：false)
  flag: false, //包含0库存
  lowFlag: false //到达下限库存
});
const orgName = ref("毕设演示机构"); //机构名称
const userId = ref(""); //当前操作员id
const userName = ref(""); //当前操作员名称
const sCatalogTxt = ref(""); //药品分类
const drugTypeList = ref([{ value: "", label: "", parentId: "" }]); //药品分类表数据
const currentPage = ref(1); // 当前页码
const pageSize = ref(10); // 分页条数
const total = ref(0); // 总条数
const mainTable = ref([]); //库存主表数据
const detailTable = ref([]); //库存明细表数据
const inventoryExportDialog = ref(false); //库存导出选择弹框-是否弹出
const currentRowMain = ref({
  //主表当前行    //注释的为不需字段
  id: "", //药房库存表ID
  orgaID: "", //机构ID
  deptId: "", //部门ID
  deptInputCode: "", //部门输入码
  deptCode: "", //部门代码
  deptName: "", //部门名称
  drugId: "", //药品ID
  catalogId: "", //药品分类ID
  drugInputCode: "", //药品输入码
  drugInputCode2: "", //药品输入码2
  drugInputCode3: "", //药品输入码3
  drugCode: "", //药品代码
  drugName: "", //药品名称
  drugSpecification: "", //规格

  unitID: "", //单位ID
  unitName: "", //单位名称
  packagQuantity: "", //包装量
  origin: "", //产地

  // purchasePrice: "", //批发价  0
  // retailPrice: "", //零售价  0

  inventory: "", //可用库存量

  // virtualInventory: "", //待发药数 0
  // realNumber: "", //库存量  0

  alterLower: "", //警戒下线
  alterTop: "", //警戒上线

  // purchaseMoney: "", //批发金额  0
  // retailMoney: "", //零售金额   0

  status: "", //状态
  operatorID: "", //修改人id
  inputDate: "", //修改时间
  createUser: "", //创建人
  createDate: "", //创建时间
  comment: "" //备注

  // catalogID: "", //药品分类ID
  // validityperiod: "", //有效期
  // catalogname: "", //药品分类名称
  // commodityName: "", //商品名称
  // inventorySort: "", //排序
  // basedrugmarkName: "", //基药标志
  // unitNameWhole: "", //整量单位
  // unitNameMargin: "", //余量单位
  // inventoryWhole: "", //库存量整量
  // inventoryMargin: "", //库存量余量
  // virtualinventoryWhole: "", //待发药数整量
  // virtualinventoryMargin: "", //待发药数余量
  // realnumberWhole: "", //可用库存整量
  // realnumberMargin: "", //可用库存余量
  // countryCode: "" //国家码
});
const sCatalogIDRef: any = ref(); //分类级联选择器REF
/** 药品分类数据下拉框选择改变时 */
const sCatalogIDChange = (val: any) => {
  // console.log(val, '123')
  if (val == null) {
    formData.sCatalogID = "";
  } else {
    val?.forEach((item: any) => {
      formData.sCatalogID = item; //只会赋值最后一个分类id
    });
  }
};
// 库存主数据查询信息（分页）
const mainTableRef: any = ref<InstanceType<typeof ElTable>>(); //主表高亮
/** 查询上表信息 */
const getPharmacyMainList = async (count: number): Promise<void> => {
  if (count == 1) {
    currentPage.value = 1;
  }
  mainTable.value = [];
  detailTable.value = [];
  // console.log(isStock.value, '是否药库', isDrug.value, '是否药房')
  btnQueryMaster(
    "0521", //机构id
    formData.sort, //排序（正序：true  降序 false）
    formData.drugStoreInputCode, //药品输入码
    "101", //部门ID
    formData.sCatalogID, //药品分类ID
    formData.flag, //是否包含零库存
    formData.lowFlag, //是否到达下线库存（true/false）
    pageSize.value, //分页条数
    currentPage.value //页码
  )
    .then((res: any) => {
      if (res?.code == "200") {
        // console.log(data, '主表数据')
        mainTable.value = res.data.DefineStockBillStockQueryInfo;
        total.value = res.data.resultCount;
        if (mainTable.value.length > 0) {
          // 高亮第一行
          nextTick(() => {
            mainTableRef.value.setCurrentRow(mainTable.value[0]);
          });
        }
      } else {
        ElMessage({ message: res.msg + "暂无数据!", type: "success" });
        mainTable.value = [];
        total.value = 0;
      }
    })
    .catch((e: Error) => {
      console.log(e.message || "失败！", "error");
      throw e;
    });
};
/** 点击查询按钮 */
const selectBtn = () => {
  getPharmacyMainList(1); //查询第一页
};
/** 切换主表当前行 */
const mainTableChange = (val: any) => {
  //console.log("切换当前行！");
  currentRowMain.value = val;
  //console.log(currentRowMain.value);
  if (currentRowMain.value != null) {
    getPharmacyDetailList();
  }
};
/** 获取明细表 接口 */
const getPharmacyDetailList = async (): Promise<void> => {
  btnQueryDetail(
    "0521", //机构ID
    "101", //部门ID
    currentRowMain.value.drugId, //药品ID
    formData.flag //是否包含零库存
  )
    .then((res: any) => {
      if (res?.code == "200") {
        // console.log(data, '药房库存明细数据')
        if (res.data.DefineStockBatchInfo.length > 0) {
          detailTable.value = res.data.DefineStockBatchInfo;
        } else {
          detailTable.value = [];
        }
      } else {
        detailTable.value = [];
        ElMessage({ message: res.msg, type: "warning" });
      }
    })
    .catch((e: Error) => {
      console.log(e.message || "失败！", "error");
      throw e;
    });
};

/** 切换页数 */
function handleCurrentChange(val: number) {
  currentPage.value = val;
  getPharmacyMainList(0);
}
/** 切换条数 */
function handleSizeChange(val: number) {
  pageSize.value = val;
  getPharmacyMainList(1);
}
/** 点击删除明细表记录 */
const deleteDetail = (item: any) => {
  console.log(item, "删除行");
  ElMessageBox.confirm("是否清除此批次信息？", "提示信息", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  }).then(() => {
    clearDetail(item); //调接口
  });
};
/** 调接口-删除明细表记录 */
const clearDetail = async (detailData: any): Promise<void> => {
  const bNumber = ref(""); //药库可用库存
  const realNumber = ref(""); //药房可用库存
  const priceNumber = ref(""); //散装数
  // console.log(detailData, '111')
  // 药库时 赋值：
  bNumber.value = detailData.number;
  realNumber.value = "0";
  priceNumber.value = detailData.priceNumber;

  // const { data, code, msg } = await cleanStoreBatchInfo(
  //   detailData.id, //明细数据ID
  //   detailData.catalogId, //机构药品ID
  //   detailData.deptID, //部门ID
  //   orgId.value, //机构ID
  //   detailData.drugID, //药品ID
  //   detailData.batch, // 批次
  //   isStock.value, //是否药库（是/否）
  //   isDrug.value, // 是否药库（是/否）
  //   userId.value, // 操作员ID
  //   detailData.createDate, //批次创建日期
  //   bNumber.value, // 药库可用库存
  //   priceNumber.value, // 散装数
  //   realNumber.value, // 药房可用库存
  //   detailData.virtualInventoryReal // 待发数
  // )
  // if (code == 200) {
  //   ElMessageBox.alert(msg, '提示信息', {
  //     confirmButtonText: '确定',
  //     type: 'success',
  //   }).then(() => {
  //     getPharmacyMainList(0)
  //   })
  // } else {
  //   ElMessageBox.alert(msg, '提示信息', {
  //     confirmButtonText: '确定',
  //     type: 'error',
  //   }).then(() => {})
  // }
};
/** 点击导出，（1：库存总量/2.库存批次） */
const exportBtn = (type: any) => {
  inventoryExportDialog.value = false; //关闭弹窗
  excelDataSearch(type);
};
const exportList = ref([]); //需导出的数据
/** 调接口- 导出数据 */
const excelDataSearch = async (excelresult: string): Promise<void> => {
  exportList.value = [];
  if (excelresult == "1") {
    ////查询主表接口
    btnQueryMaster(
      "0521", //机构id
      formData.sort, //排序（正序：true  降序 false）
      formData.drugStoreInputCode, //药品输入码
      "101", //部门ID
      formData.sCatalogID, //药品分类ID
      formData.flag, //是否包含零库存
      formData.lowFlag, //是否到达下线库存（true/false）
      pageSize.value, //分页条数
      currentPage.value //页码
    ).then((res: any) => {
      if (res?.code == "200") {
        // console.log(data, '主表数据')
        exportList.value = res.data.DefineStockBillStockQueryInfo;
      } else {
        exportList.value = [];
      }
      exportXml(excelresult); //前端导出excel
    });
  }
  if (excelresult == "2") {
    ////查询明细接口
    btnQueryDetail(
      "0521", //机构ID
      "101", //部门ID
      currentRowMain.value.drugId, //药品ID
      formData.flag //是否包含零库存
    )
      .then((res: any) => {
        if (res?.code == "200") {
          if (res.data.DefineStockBatchInfo.length > 0) {
            exportList.value = res.data.DefineStockBatchInfo;
          }
        } else {
          exportList.value = [];
          ElMessage({ message: "导出失败！" + res.msg, type: "warning" });
        }
        exportXml(excelresult); //前端导出excel
      })
      .catch((e: Error) => {
        console.log(e.message || "失败！", "error");
        throw e;
      });
  }
};

const dicExport: any = ref([]);
const exportType = ref("");
/** 导出excel */
function exportXml(excelresult: any) {
  const headersArr: any = [];
  dicExport.value = [];
  exportType.value = excelresult;
  if (excelresult == "1") {
    // 库存总量
    headersArr.value = ["部门名称", "药品代码", "药品名称", "规格", "库存量", "单位", "库存下限", "状态", "药品分类id"];
    exportList.value.forEach((item: any) => {
      dicExport.value.push({
        部门名称: item.deptName,
        药品代码: item.drugCode,
        药品名称: item.drugName,
        规格: item.drugSpecification,
        库存量: item.inventory,
        单位: item.unitName,
        库存下限: item.alterLower,
        状态: item.status,
        药品分类id: item.catalogId
      });
    });
  } else {
    // 库存批次
    headersArr.value = [
      "部门名称",
      "药品代码",
      "药品名称",
      "规格",
      "批次",
      "批号",
      "生产日期",
      "有效期",
      "库存量",
      "供应商",
      "产地",
      "进价",
      "零售价	",
      "进价金额",
      "零售金额	",
      "单位",
      "国家码"
    ];
    exportList.value.forEach((item: any) => {
      dicExport.value.push({
        部门名称: item.deptName,
        药品代码: item.drugCode,
        药品名称: item.drugName,
        规格: item.drugSpecification,
        批次: item.batch,
        批号: item.batchNumber,
        生产日期: item.produceDate,
        有效期: item.validityPeriod,
        库存量: item.priceNumber,
        供应商: item.supplyName,
        产地: item.origin,
        进价: item.purchasePrice,
        零售价: item.retailPrice,
        进价金额: item.purchaseMoney,
        零售金额: item.retailMoney,
        单位: item.unitName,
        国家码: item.countryCode
      });
    });
  }
  // console.log(dicExport.value, 'dicExport.value')
  getExportDate(headersArr.value);
}

function getExportDate(headersArr: any) {
  // 得到一个二维数组  this.get2Array最终的返回值是一个二维数组
  const arr2 = get2Array(dicExport.value, headersArr);
  // console.log('arr2' + arr2) // 二维数组
  import("@/vendor/Export2Excel.js").then(excel => {
    excel.export_json_to_excel({
      header: headersArr, // 表头
      data: arr2, //数据
      filename: exportType.value == "1" ? orgName.value + "药品库存" : orgName.value + "药品批次库存", //文件名
      autoWidth: true,
      bookType: "xlsx"
    });
  });
}

/** 处理二维数组的方法 --未优化 */
function get2Array(rows: any, headersArr: any) {
  const array = new Array();
  let i = 0;
  const res = rows.forEach((item: any) => {
    // item是对象(每一个员工的详细信息)
    const arr = [""];
    headersArr.forEach((str: any) => {
      // str是每一个headerArr的字符串
      arr.push(item[str]);
    });
    array[i] = arr;
    array[i].splice(0, 1);
    i = i + 1;
  });
  // array.splice(0, 1)
  // console.log(array, 'array')
  return array;
}
/** 获取药品分类目录树 */
const getListMediCatalogInfo = async (): Promise<void> => {
  getMediCatalogInfoList("0")
    .then((res: any) => {
      if (res.code == "200") {
        const newData = ref([{ value: "", label: "", parentId: "" }]);
        const result = ref([{ value: "", label: "", parentId: "" }]);
        const map = ref({});
        for (let i = 0; i < res.data.length; i++) {
          newData.value[i] = {
            value: res.data[i].id,
            label: res.data[i].catalogName,
            parentId: res.data[i].parentId
          };
        }
        // 将数据变为键值对形式的对象，key为id
        newData.value.forEach(item => {
          map.value[item.value] = item;
        });
        // console.log(map,'第一级');
        // 将一维数组转换为多维
        newData.value.forEach(item => {
          // 这儿需要注意 item 的父级 是 map[item.pid]
          // 第二次类似浅拷贝赋值
          let parent = map.value[item.parentId];
          if (!parent) {
            result.value.push(map.value[item.value]); // 直接item也可以
            return;
          }
          // 表示这个项存在父级
          // 即使原来的父级还没有创建，由于浅拷贝 data和map上的数据还是会收到影响
          (parent.children || (parent.children = [])).push(item);
        });
        drugTypeList.value = result.value;
        drugTypeList.value.splice(0, 1);
        // console.log(drugTypeList.value, 'drugTypeList.value')
      } else {
        drugTypeList.value = [];
        ElMessageBox.alert("获取药品分类目录失败！");
      }
    })
    .catch((error: Error) => {
      console.log(error);
    });
};
onMounted(async () => {
  getListMediCatalogInfo(); // 加载药品分类目录树
  getPharmacyMainList(1); //加载主表
});
</script>
<template>
  <div id="main" tabindex="0">
    <div class="form">
      <el-form ref="infoFormRef" inline label-width="auto" :model="formData">
        <!-- 第一行 -->
        <el-row>
          <el-col class="col" :span="5">
            <el-form-item label="医疗机构" style="width: 97%">
              <el-select placeholder="请输入医疗机构" v-model="formData.mechanism">
                <el-option label="毕设演示机构" value="1" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="4">
            <el-form-item label="部门" style="width: 97%; margin-left: 7px" class="depart">
              <el-select placeholder="请选择部门" v-model="formData.deptName">
                <el-option label="西药库" value="1"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="5">
            <el-form-item label="药品名称" style="width: 97%; margin-left: 10px">
              <el-input ref="drugNameRef" v-model="formData.drugStoreInputCode" clearable placeholder="代码/名称/输入码">
              </el-input>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="5">
            <el-form-item label="药品分类" style="width: 97%; margin-left: 10px">
              <el-cascader
                ref="sCatalogIDRef"
                placeholder="请选择药品分类"
                v-model="sCatalogTxt"
                :options="drugTypeList"
                :show-all-levels="false"
                :props="{ checkStrictly: true }"
                filterable
                clearable
                @change="sCatalogIDChange"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 第二行 -->
        <el-row style="margin-top: 2px">
          <el-col class="col" :span="8">
            <el-form-item class="ml-4">
              <el-radio-group v-model="formData.sort" @change="getPharmacyMainList(1)">
                <el-radio label="true"> 按库存量正序 </el-radio>
                <el-radio label="false"> 按库存量倒序 </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col class="col" :span="6"> </el-col>
          <el-col class="col" :span="8" :offset="2" style="padding-left: 45px">
            <el-form-item style="width: 25%">
              <el-checkbox v-model="formData.flag" label="包含0库存" />
            </el-form-item>
            <el-form-item style="width: 27%">
              <el-checkbox label="到达下限库存" v-model="formData.lowFlag" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="button">
      <div style="display: inline-block">
        <el-button class="buttonStyle" @click="selectBtn">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/查询.png" />
          查询(F5)
        </el-button>
        <el-button class="buttonStyle" @click="inventoryExportDialog = true">
          <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/导出.png" />
          导出Excel(F8)
        </el-button>
      </div>
    </div>
    <div class="tableTop">
      <el-table
        border
        ref="mainTableRef"
        :data="mainTable"
        highlight-current-row
        stripe
        style="height: 100%"
        @current-change="mainTableChange"
      >
        <el-table-column align="center" show-overflow-tooltip label="部门名称" min-width="120px" prop="deptName" />
        <el-table-column align="center" show-overflow-tooltip label="药品代码" min-width="120px" prop="drugCode" />
        <el-table-column align="center" show-overflow-tooltip label="药品名称" min-width="120px" prop="drugName" />
        <el-table-column align="center" show-overflow-tooltip label="商品名" min-width="120px" prop="" />
        <el-table-column align="center" show-overflow-tooltip label="规格" min-width="120px" prop="drugSpecification" />
        <el-table-column align="center" show-overflow-tooltip label="库存量" min-width="120px" prop="inventory" />
        <el-table-column align="center" show-overflow-tooltip label="药品单位" min-width="110px" prop="unitName" />
        <el-table-column align="center" show-overflow-tooltip label="库存下限" min-width="120px" prop="alterLower" />
        <el-table-column align="center" show-overflow-tooltip label="状态" min-width="90px" prop="status" />
        <el-table-column align="center" show-overflow-tooltip label="药品分类id" min-width="120px" prop="catalogId" />
        <el-table-column align="center" show-overflow-tooltip label="进价金额" min-width="120px" prop="" />
        <el-table-column align="center" show-overflow-tooltip label="零售金额" min-width="120px" prop="" />
        <el-table-column align="center" show-overflow-tooltip label="国家码" min-width="120px" prop="" />
      </el-table>
    </div>
    <div class="paging">
      <el-pagination
        :background="true"
        class="flex justify-end mt-4"
        layout="total, sizes, prev, pager, next, jumper"
        size="small"
        :current-page="currentPage"
        :page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
    <div class="tableBottom">
      <el-table border highlight-current-row stripe style="height: 100%" :data="detailTable">
        <el-table-column align="center" show-overflow-tooltip label="批次" min-width="140px" prop="batch" />
        <el-table-column align="center" show-overflow-tooltip label="批号" min-width="110px" prop="batchNumber" />
        <el-table-column align="center" label="生产日期" min-width="140px" show-overflow-tooltip sortable prop="produceDate">
        </el-table-column>
        <el-table-column
          align="center"
          show-overflow-tooltip
          label="有效期"
          min-width="140px"
          prop="validityPeriod"
          :formatter="
            ({ validityPeriod }) => {
              if (!validityPeriod) return '-';
              const date = Array.isArray(validityPeriod)
                ? dayjs(new Date(validityPeriod[0], validityPeriod[1] - 1, validityPeriod[2]))
                : dayjs(validityPeriod);
              return date.isValid() ? date.format('YYYY-MM-DD') : '-';
            }
          "
        />
        <el-table-column align="center" show-overflow-tooltip label="库存量" min-width="110px" prop="priceNumber" />
        <el-table-column align="center" show-overflow-tooltip label="供应商" min-width="130px" prop="supplyName" />
        <el-table-column align="center" show-overflow-tooltip label="产地" min-width="130px" prop="origin" />
        <el-table-column align="center" show-overflow-tooltip label="进价" min-width="90px" prop="purchasePrice" />
        <el-table-column align="center" show-overflow-tooltip label="零售价" min-width="110px" prop="retailPrice" />
        <el-table-column align="center" show-overflow-tooltip label="进价金额" min-width="120px" prop="purchaseMoney" />
        <el-table-column align="center" show-overflow-tooltip label="零售金额" min-width="120px" prop="retailMoney" />
        <el-table-column align="center" show-overflow-tooltip label="操作" min-width="110px" prop="name">
          <template #default="scoped">
            <el-button class="tableButton" @click="deleteDetail(scoped.row)">
              <img class="buttonImg" src="@/assets/drugAdministration/inventoryInquiry/删除.png" />
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 库存导出选择 -->
    <el-dialog v-model="inventoryExportDialog" :close-on-click-modal="false" style="width: 23%">
      <template #header>
        <div class="dialogHeader">
          <span>库存导出选择</span>
        </div>
        <div>
          <el-divider style="width: 105%; margin: 10px 0px 10px -10px" />
        </div>
      </template>
      <div style="padding: 0px 26%; margin-top: -10%">
        <el-button class="buttonStyleDialog" @click="exportBtn('1')">
          <img class="buttonImgeDialog" src="@/assets/drugAdministration/inventoryInquiry/弹框导出.png" />
          <span class="buttonFontDialog">导出库存总量</span>
        </el-button>
        <el-button class="buttonStyleDialog" @click="exportBtn('2')">
          <img class="buttonImgeDialog" src="@/assets/drugAdministration/inventoryInquiry/弹框导出.png" />
          <span class="buttonFontDialog">导出库存批次</span>
        </el-button>
        <el-button class="dialogCancel" @click="inventoryExportDialog = false">
          <img class="buttonImgeDialog" src="@/assets/drugAdministration/inventoryInquiry/取消.png" />
          <span class="buttonFontDialog">取消</span>
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<style scoped lang="scss">
#main {
  min-height: calc(100vh - 125px);
  padding: 0 !important;
}
/*表单部分*/
.col {
  position: relative;
  display: flex;
  padding-right: 1%;
  :deep(.el-checkbox__label) {
    text-align: right;
  }
}
/* 药品名称框 */
.radioStyle {
  height: 300px;
  /* font-size: v-bind(size); */
}
.form {
  width: 98%;
  padding: 10px 15px;
  margin: auto;
  margin-top: 0px;
  background: white;
}
:deep(.el-form-item) {
  margin-bottom: 0px;
}
:deep(.el-form--inline .el-form-item) {
  width: 100%;
  margin-right: 0px !important;
}
:deep(.el-radio) {
  float: left !important;
}
:deep(.el-select) {
  width: 100% !important;
}
:deep(.el-form-item__label-wrap) {
  margin-left: 1px !important;
}

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

/* 表格改变颜色部分*/
:deep(.el-table .MianRedList) {
  color: red;
}

/*表格内的按钮 */
.tableButton {
  /*width: 40px;  */
  height: 27px;
}

/*第一个表格部分 */
.tableTop {
  width: 100%;
  height: calc(100vh - 540px) !important;
  margin: auto;
  margin-top: 5px;
}
/*分页部分 */
.paging {
  width: 100%;
  margin: auto;
  margin-top: 5px !important;
  background: white;
  display: flex;
  justify-content: center;
  :deep(.el-pagination) {
    padding: 5px 0px;
    margin: 0px;
  }
}
/*第二个表格部分 */
.tableBottom {
  width: 100%;
  height: calc(100vh - 542px) !important;
  margin: auto;
  margin-top: 5px;
}

/*导出弹框 */
.buttonStyleDialog {
  display: flex;
  height: 27px;
  margin: 7px 0px !important;
}
.dialogCancel {
  display: flex;
  height: 27px;
  margin: 30px 0px !important;
  color: white;
  background: #ee5b57;
}
.buttonImgeDialog {
  width: 12px;
  margin-right: 5px;
}
.buttonFontDialog {
  width: 80%;
}

/*冻结弹框 */
.freezeBottom {
  margin-top: 25px;
  text-align: center;
  .freezeDialogConfirm {
    width: 90px;
    height: 27px;
    color: white;
    background: #55b584;
  }
  .freezeDialogCancel {
    width: 90px;
    height: 27px;
    color: white;
    background: #ee5b57;
  }
  .buttonImg {
    width: 12px;
    margin-right: 5px;
  }
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

/* 下拉框最大表格的宽度 */
.el-select-dropdown {
  max-width: 500px;
}
</style>
