import dayjs from "dayjs";
import { ref } from "vue";

export function isEndTimeMidnight(endTime: any) {
  // 判断结束时间是否为日期+00:00:00，如果是，则将其调整为日期+23:59:59
  if (endTime.endsWith(" 00:00:00")) {
    const endDate = new Date(endTime);
    endDate.setHours(23, 59, 59);
    endTime = endDate.toISOString().split("T")[0] + " 23:59:59";
  }
  return endTime;
}
/**日期范围组件 开始时间不能大于结束时间判断 */
export const checkDate = (start: any, end: any) => {
  const startDate = dayjs(start, "YYYY-MM-DD").startOf("day");
  const endDate = dayjs(end, "YYYY-MM-DD").endOf("day");
  return startDate.isAfter(endDate);
};

/** 获得现在的时间 */
export function getNowTimeInterval() {
  //现在的时间
  const dataTimeStart = ref("");
  const dataTimeEnd = ref("");
  const dataTime: any = ref([]);
  //时间戳
  const Dates = new Date();
  //年份
  const Year = Dates.getFullYear();
  //月份下标是0-11
  const Months = Dates.getMonth() + 1 < 10 ? "0" + (Dates.getMonth() + 1) : Dates.getMonth() + 1;
  //具体的天数
  const Day = Dates.getDate() < 10 ? "0" + Dates.getDate() : Dates.getDate();
  dataTimeStart.value = `${Year}-${Months}-${Day}`;
  dataTimeEnd.value = `${Year}-${Months}-${Day}`;
  dataTime.value[0] = dataTimeStart.value;
  dataTime.value[1] = dataTimeEnd.value;
  return dataTime.value;
}
/**
 * 日期格式 只显示年月日
 */
export function formatDate(dateTime: any) {
  const date = new Date(dateTime);
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, "0");
  const day = date.getDate().toString().padStart(2, "0");
  return `${year}-${month}-${day}`;
  // return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}
