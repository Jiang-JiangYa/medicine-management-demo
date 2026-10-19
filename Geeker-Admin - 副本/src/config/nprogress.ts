/**用于在页面加载时显示一个进度条，给用户一个加载中的反馈。 */
/**在请求开始时显示进度条  NProgress.start();  
// 在请求完成时隐藏进度条  NProgress.done(); */
import NProgress from "nprogress";
import "nprogress/nprogress.css";

NProgress.configure({
  easing: "ease", // 动画方式
  speed: 500, // 递增进度条的速度
  showSpinner: true, // 是否显示加载ico
  trickleSpeed: 200, // 自动递增间隔
  minimum: 0.3 // 初始化时的最小百分比
});

export default NProgress;
