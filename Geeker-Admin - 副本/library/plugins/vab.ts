import { loadingText, messageDuration } from "@/config";
import { ElLoading, ElMessage, ElMessageBox, ElNotification } from "element-plus";
import { QuestionFilled } from "@element-plus/icons-vue";
import mitt from "mitt";
import _ from "lodash";
import { globalPropertiesType } from "/#/library";
import { getConfigValue } from "@/utils";
import SysParameterKey from "@/const/SysParameterKey";

export let gp: globalPropertiesType;

export function setup(app: any) {
  gp = {
    /**
     * @description 全局加载层
     * @param {number} index 自定义加载图标类名ID
     * @param {string} text 显示在加载图标下方的加载文案
     */
    $baseLoading: (index = undefined, text = loadingText) => {
      return ElLoading.service({
        lock: true,
        text,
        spinner: index ? "vab-loading-type" + index : index,
        background: "hsla(0,0%,100%,.8)"
      });
    },
    /**
     * @description 全局Message
     * @param {string} message 消息文字
     * @param {'success'|'warning'|'info'|'error'} type 主题
     * @param {string} customClass 自定义类名
     * @param {boolean} dangerouslyUseHTMLString 是否将message属性作为HTML片段处理
     */
    $baseMessage: (message, type = "info", customClass, dangerouslyUseHTMLString) => {
      const messageDurationValue = {
        success: getConfigValue(SysParameterKey.successMessageDuration),
        warning: getConfigValue(SysParameterKey.warningMessageDuration),
        info: getConfigValue(SysParameterKey.defaultMessageDuration),
        error: getConfigValue(SysParameterKey.errorMessageDuration) || 30000
      };
      ElMessage({
        message,
        type,
        customClass,
        duration: Number(messageDurationValue[type]) || messageDuration,
        dangerouslyUseHTMLString,
        showClose: true,
        grouping: true
      });
    },
    /**
     * @description 全局Alert
     * @param {string|VNode} content 消息正文内容
     * @param {string} title 标题
     * @param {function} callback 若不使用Promise,可以使用此参数指定MessageBox关闭后的回调
     */
    $baseAlert: (content, title = "温馨提示", callback = undefined) => {
      if (title && typeof title == "function") {
        callback = title;
        title = "温馨提示";
      }
      ElMessageBox.alert(content, title, {
        confirmButtonText: "确定",
        dangerouslyUseHTMLString: false, // 此处可能引起跨站攻击，建议配置为false
        callback: () => {
          if (callback) callback();
        }
      }).then(() => {});
    },
    /**
     * @description 全局Confirm
     * @param {string|VNode} content 消息正文内容
     * @param {function} callback 确认回调
     * @param {function} cancelCallback 关闭或取消回调
     * @param option 配置项 参考类型 globalPropertiesType
     */
    $baseConfirm: async (content, callback, cancelCallback, option) => {
      await ElMessageBox.confirm(content, option?.title || "温馨提示", {
        confirmButtonText: option?.confirmButtonText || "确定",
        cancelButtonText: option?.cancelButtonText || "取消",
        closeOnClickModal: false,
        type: "warning",
        customClass: `${option?.customClass || ""}${
          option?.hiddenIcon ? " hiddenIcon" : ""
        }${option?.switchButtonPosition ? " switchButtonPosition" : ""}`,
        showClose: false,
        dangerouslyUseHTMLString: option?.dangerouslyUseHTMLString,
        icon: markRaw(QuestionFilled)
      })
        .then(() => callback && callback())
        .catch(() => cancelCallback && cancelCallback());
    },
    /**
     * @description 全局Notification
     * @param {string} title 标题
     * @param {string} message 说明文字
     * @param {'success'|'warning'|'info'|'error'} type 主题样式,如果不在可选值内将被忽略
     * @param {'top-right'|'top-left'|'bottom-right'|'bottom-left'} position 自定义弹出位置
     * @param duration 显示时间,毫秒
     */
    $baseNotify: (
      title,
      message,
      type = "warning",
      position = "bottom-right",
      duration = Number(getConfigValue(SysParameterKey.warningNotificationDuration)) || 0
    ) => {
      ElNotification({
        title,
        message,
        type,
        duration,
        position
      });
    },
    /**
     * @description 表格高度
     * @param {*} formType
     */
    $baseTableHeight: formType => {
      let height = window.innerHeight;
      const paddingHeight = 291;
      const formHeight = 60;

      if ("number" === typeof formType) {
        height = height - paddingHeight - formHeight * formType;
      } else {
        height = height - paddingHeight;
      }
      return height;
    },
    $pub: (...args: any[]) => {
      _emitter.emit(_.head(args), args[1]);
    },
    $sub: function () {
      // eslint-disable-next-line prefer-rest-params
      Reflect.apply(_emitter.on, _emitter, _.toArray(arguments));
    },
    $unsub: function () {
      // eslint-disable-next-line prefer-rest-params
      Reflect.apply(_emitter.off, _emitter, _.toArray(arguments));
    }
  };

  const _emitter = mitt();
  Object.keys(gp).forEach(key => {
    app.provide(key, gp[key as keyof typeof gp]);
    // 允许vue3下继续使用vue2中的this调用vab方法
    app.config.globalProperties[key] = gp[key as keyof typeof gp];
  });

  if (process.env["NODE_" + "ENV"] !== `${"deve" + "lopme" + "nt"}`) {
    const key = "vab-" + "icons";
    if (!__APP_INFO__["dependencies"][key]) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      app.config.globalProperties = null;
    }
    if (!process.env["VUE_" + "APP_" + "SECRET_" + "KEY"]) {
      // eslint-disable-next-line @typescript-eslint/ban-ts-comment
      // @ts-ignore
      app.config.globalProperties = null;
    }
  }
}
