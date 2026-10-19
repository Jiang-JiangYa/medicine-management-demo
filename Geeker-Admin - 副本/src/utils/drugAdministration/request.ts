import axios, { AxiosRequestConfig, AxiosResponse, Method } from "axios";
import router from "@/routers/index";
import { useUserStore } from "@/stores/modules/user";

// 创建一个新的axios对象
const request = axios.create({
  // 后端接口地址
  baseURL: import.meta.env.VITE_API_URL || "/api", // 注意！！这里是全局统一加上了 '/api' 前缀
  timeout: 5000 // 请求超时时间
});

/**
 * request 拦截器
 * 可以自请求发送前对请求做一些处理
 * 比如统一加token，对请求参数统一加密
 */
request.interceptors.request.use(
  (config: any) => {
    if (!(config.data instanceof FormData)) config.headers["Content-Type"] = "application/json;charset=utf-8";
    const userStore = useUserStore();
    if (userStore.token) config.headers["Token"] = userStore.token;

    return config;
  },
  (error: any) => {
    return Promise.reject(error); // 打印错误信息
  }
);

// response 拦截器
// 可以在接口响应后统一处理结果 响应头
request.interceptors.response.use(
  (response: AxiosResponse) => {
    let res = response.data;

    // 如果是返回的文件
    if (response.config.responseType === "blob") {
      return res;
    }
    // 兼容服务端返回的字符串数据，如果是字符串，将其解析成JSON对象
    if (typeof res === "string") {
      res = res ? JSON.parse(res) : res;
    }

    // 若返回code是401，跳转到登录页面。
    if (res.code === "401") {
      useUserStore().setToken("");
      router.replace("/login");
    }

    return res;
  },
  (error: any) => {
    if (error.response?.status === 401) {
      useUserStore().setToken("");
      router.replace("/login");
    }
    console.log("err" + error); // for debug 打印错误信息
    return Promise.reject(error);
  }
);

// 泛型请求函数
const Request = <T = unknown>(method: Method, url: string, data: object, params: object): Promise<T> => {
  return request({
    method,
    url,
    data,
    params
  });
};

export default {
  get<T>(url: string, params: object = {}): Promise<T> {
    return Request<T>("GET", url, {}, params);
  },
  post<T>(url: string, data: object = {}): Promise<T> {
    return Request<T>("POST", url, data, {});
  },
  put<T>(url: string, data: object = {}): Promise<T> {
    return Request<T>("PUT", url, data, {});
  },
  delete<T>(url: string, params: object = {}): Promise<T> {
    return Request<T>("DELETE", url, {}, params);
  },
  full<T>(method: Method, url: string, data: object = {}, params: object = {}): Promise<T> {
    return Request<T>(method, url, data, params);
  }
};

// import type { AxiosRequestConfig, AxiosResponse, Method, AxiosError } from "axios";
// import { useUserStore } from "@/store/modules/user";
// import { contentType, debounce, messageName, requestTimeout, statusName, successCode } from "@/config/drugAdministration";
// import qs from "qs";
// import router from "@/router";
// import { isArray } from "@/utils/validate";
// import { addErrorLog, needErrorLog } from "@vab/plugins/errorLog";
// import { refreshToken } from "@/api/refreshToken";
// import { gp } from "@gp";
// import { SsetItem } from "@/utils/storage";
// import { AES_Decrypt, AES_Encrypt } from "@/utils/aes";
// import { SM2_Decrypt, SM2_Encrypt } from "@/utils/sm2";
// import { SM4_Decrypt, SM4_Encrypt } from "@/utils/sm4";
// import { removeToken } from "@/utils/token";
// import SysParameterKey from "@/const/SysParameterKey";
// import { getConfigValue } from "@/utils/index";

// interface IConfigHeaders {
//   histoken: string;
//   "Content-Type": string;
// }

// let loadingInstance: any;

// let refreshToking = false;

// let requests: (() => void)[] = [];

// // 操作正常Code数组
// const codeVerificationArray = isArray(successCode) ? [...successCode] : [...[successCode]];
// // 代理的请求
// const proxyUrl = ["/templateJsonProxy"];

// const CODE_MESSAGE: Record<number, string> = {
//   200: "服务器成功返回请求数据",
//   201: "新建或修改数据成功",
//   202: "一个请求已经进入后台排队(异步任务)",
//   204: "删除数据成功",
//   400: "发出信息有误",
//   401: "用户没有权限(令牌失效、用户名、密码错误、登录过期)",
//   402: "令牌过期",
//   403: "用户得到授权，但是访问是被禁止的",
//   404: "访问资源不存在",
//   406: "请求格式不可得",
//   410: "请求资源被永久删除，且不会被看到",
//   500: "服务器发生错误",
//   502: "网关错误",
//   503: "服务不可用，服务器暂时过载或维护",
//   504: "网关超时",
//   10001: "用户名或密码不正确",
//   10002: "用户被禁用",
//   10003: "用户被锁定",
//   10004: "token无效",
//   10005: "token已过期",
//   10006: "用户被顶下线",
//   10007: "用户被踢下线",
//   10008: "当前会话未登录",
//   10401: "未提供token",
//   10402: "无此角色",
//   10403: "无此权限",
//   10404: "未查询到相关数据",
//   10009: "参数缺失",
//   10010: "重要参数未配置",
//   30000: "系统编码已存在",
//   30001: "菜单编码已存在",
//   30002: "用户名已存在",
//   30003: "机构编码已存在",
//   30004: "字典类型编码已存在",
//   30005: "角色编码已存在",
//   30006: "科室代码已存在"
// };

// /**
//  * @description axios初始化
//  */
// const instance = axios.create({
//   timeout: requestTimeout,
//   headers: {
//     "Content-Type": contentType
//   }
// });

// /**
//  * 泛型请求
//  * @param method 方法
//  * @param url 地址
//  * @param data body数据
//  * @param params 地址参数
//  * @constructor
//  */
// const Request = <T = unknown>(method: Method, url: string, data: object, params: object): Promise<T> => {
//   let encryptData = "";
//   let encryptParams = "";
//   // 是否启用加密
//   const isEncrypt = window.urlConfig.apiAES || window.urlConfig.apiSM2 || window.urlConfig.apiSM4;
//   //加密的判断

//   return new Promise((resolve, reject) => {
//     // 区分是否 http 全地址
//     if (url.startsWith("/")) {
//       // 区分是否开发环境代理地址
//       if (!proxyUrl.some(item => url.startsWith(item))) {
//         // 拼接接口地址
//         //url = window.urlConfig.serviceUrl + url
//         //本地后端端口地址
//         url = "localhost:8081" + url;
//       }
//     }
//     instance
//       .request<T, Record<string, any>>(
//         // url == '/his/systemManage/getChargeItemPriceExportList'
//         //   ? {
//         //       method,
//         //       url: url.startsWith('/') ? window.urlConfig.serviceUrl + url : url,
//         //       data: isEncrypt ? { params: encryptData } : data,
//         //       params: isEncrypt ? { params: encryptParams } : params,
//         //       responseType: 'blob',
//         //     }
//         //   :
//         {
//           method,
//           url,
//           data: isEncrypt ? { params: encryptData } : data,
//           params: isEncrypt ? { params: encryptParams } : params
//         }
//       )
//       .then((res: Record<string, any>) => {
//         //....解密操作
//         resolve(res as T);
//       })
//       .catch(error => reject(error));
//   });
// };

// /**
//  * axios请求拦截器配置
//  * @param config
//  * @returns {AxiosRequestConfig}
//  */
// const requestConf = (config: AxiosRequestConfig): AxiosRequestConfig => {
//   const userStore = useUserStore();
//   const { token } = userStore;
//   // 延迟超时时间的接口列表
//   const timeoutExtend = getConfigValue(SysParameterKey.interfaceTimeoutExtend)?.split(",");
//   // 不规范写法 可根据setting.config.js tokenName配置随意自定义headers
//   // if (token) config.headers[tokenName] = token

//   // 规范写法 不可随意自定义
//   //请求拦截器
//   token && ((config.headers as unknown as IConfigHeaders).histoken = `${token}`);
//   if (
//     config.data &&
//     (config.headers as unknown as IConfigHeaders)["Content-Type"] === "application/x-www-form-urlencoded;charset=UTF-8"
//   ) {
//     config.data = qs.stringify(config.data);
//   }

//   if (timeoutExtend?.length && timeoutExtend.some((item: string) => config.url?.includes(item))) {
//     config.timeout = 600000;
//   }

//   if (debounce.some(item => (config.url as string).includes(item))) {
//     loadingInstance = gp.$baseLoading();
//   }
//   if ((config.url as string).includes("/his/systemManage/getChargeItemPriceExportList")) {
//     config.responseType = "blob";
//   }

//   return config;
// };

// /**
//  * 刷新令牌。 当 token 过期时，尝试刷新 token 并重试请求。
//  * @param config 过期请求配置
//  * @returns {Promise} 返回结果
//  */
// const tryRefreshToken = async (config: AxiosRequestConfig) => {
//   if (!refreshToking) {
//     refreshToking = true;
//     try {
//       const { access_token, refresh_token }: any = await refreshToken<Record<string, string>>();
//       if (access_token) {
//         const { setToken } = useUserStore();
//         setToken(access_token);
//         SsetItem("refreshToken", refresh_token);
//         // 已经刷新了token，将所有队列中的请求进行重试
//         requests.forEach((cb: any) => cb(access_token));
//         requests = [];
//         return instance(requestConf(config));
//       }
//     } catch (error) {
//       console.error("refreshToken error =>", error);
//       router.push({ path: "/login", replace: true }).then(() => {});
//     } finally {
//       refreshToking = false;
//     }
//   } else {
//     return new Promise(resolve => {
//       // 将resolve放进队列，用一个函数形式来保存，等token刷新后直接执行
//       requests.push(() => {
//         resolve(instance(requestConf(config)));
//       });
//     });
//   }
// };

// /**
//  * axios响应拦截器
//  * @param config 请求配置
//  * @param data response数据
//  * @param status HTTP status
//  * @param statusText HTTP status text
//  * @returns {Promise<*|*>}
//  */
// const handleData = async ({ config, data, status, statusText }: AxiosResponse) => {
//   const { resetAll, setToken } = useUserStore();
//   const skipUrl = ["/his/insure/speaker/sendMessage"];
//   if (loadingInstance) loadingInstance.close();
//   // 若data.code存在，覆盖默认code
//   let code = data && data[statusName] ? data[statusName] : status;
//   // 若code属于操作正常code，则status修改为200
//   if (codeVerificationArray.indexOf(data[statusName]) + 1) code = 200;
//   switch (code) {
//     case 200:
//       // 业务层级错误处理，以下是假定restful有一套统一输出格式(指不管成功与否都有相应的数据格式)情况下进行处理
//       // 例如响应内容：
//       // 错误内容：{ code: 1, msg: '非法参数' }
//       // 正确内容：{ code: 200, data: {  }, msg: '操作正常' }
//       // return data
//       return data;
//     case 201:
//       return data;
//     case 500:
//       if (!skipUrl.some(item => config?.url?.includes(item))) {
//         // 接口返回code500提示错误信息
//         gp.$baseMessage(
//           data && data[messageName] ? data[messageName] : CODE_MESSAGE[code] ? CODE_MESSAGE[code] : statusText,
//           "error",
//           "vab-hey-message-error",
//           false
//         );
//       }
//       return Promise.reject(data);
//     case 10009:
//       return data;
//     case 10404:
//       return data;
//     case 20000:
//       return data;
//     case 20001:
//       return data;
//     case 401:
//       // 避免路由拦截器循环拦截401而不触发resetAll
//       setToken("");
//       removeToken();
//       router
//         .push({
//           path: "/login",
//           // path: '/403',
//           replace: true
//         })
//         .then(() => {
//           resetAll().then(() => {});
//         });
//       break;
//     case 402:
//       return await tryRefreshToken(config);
//     case 403:
//       router.push({ path: "/403" }).then(() => {});
//       break;
//     case 1004:
//       return await tryRefreshToken(config);
//     case 10004:
//       // 避免路由拦截器循环拦截401而不触发resetAll
//       setToken("");
//       removeToken();
//       gp.$baseMessage("token失效, 请重新登录!", "error", "vab-hey-message-error", false);
//       router
//         .push({
//           path: "/login",
//           replace: true
//         })
//         .then(() => {
//           resetAll().then(() => {});
//         });
//       break;
//     case 10006:
//       setToken("");
//       removeToken();
//       gp.$baseMessage("用户被顶下线", "error", "vab-hey-message-error", false);
//       router.push({ path: "/login", replace: true }).then(() => {
//         resetAll().then(() => {});
//       });
//       break;
//   }

//   // 异常处理
//   // 若data.msg存在，覆盖默认提醒消息
//   let errMsg = `${data && data[messageName] ? data[messageName] : CODE_MESSAGE[code] ? CODE_MESSAGE[code] : statusText}`;
//   // 针对code自定义提示语
//   code === 401 && (errMsg = "登录失效，请重新登陆");
//   // 是否显示高亮错误(与errorHandler钩子触发逻辑一致)
//   gp.$baseMessage(errMsg, "error", "vab-hey-message-error", false);
//   if (needErrorLog()) {
//     addErrorLog({ message: errMsg, stack: data, isRequest: true });
//   }
//   return Promise.reject(data);
// };

// /**
//  * @description axios请求拦截器。在请求发送前对请求进行配置，如添加 token、序列化请求数据等。
//  */
// instance.interceptors.request.use(requestConf, error => {
//   return Promise.reject(error);
// });

// /**
//  * @description axios响应拦截器
//  */
// instance.interceptors.response.use(
//   response => handleData(response),
//   (error: AxiosError) => {
//     const { response, config } = error;
//     if (response === undefined) {
//       if (loadingInstance) loadingInstance.close();
//       // 跳过普通提醒的接口（配有自定义的catch提醒）
//       const skipMsgArray = ["/zntx/activate_hlyy_client"];
//       if (skipMsgArray.every(item => !config?.url?.includes(item))) {
//         gp.$baseMessage(
//           "连接后台接口失败，可能由以下原因造成：后端不支持跨域CORS、接口地址不存在、请求超时等，请联系管理员排查后端接口问题 ",
//           "error",
//           "vab-hey-message-error",
//           false
//         );
//       }
//       // reject失败接口
//       return Promise.reject({
//         msg: "request error",
//         config
//       });
//     } else return handleData(response);
//   }
// );

// export default {
//   get<T>(url: string, params: object = {}): Promise<T> {
//     return Request<T>("GET", url, {}, params);
//   },
//   post<T>(url: string, data: object = {}): Promise<T> {
//     return Request<T>("POST", url, data, {});
//   },
//   put<T>(url: string, data: object = {}): Promise<T> {
//     return Request<T>("PUT", url, data, {});
//   },
//   delete<T>(url: string, params: object = {}): Promise<T> {
//     return Request<T>("DELETE", url, {}, params);
//   },
//   full<T>(method: Method, url: string, data: object = {}, params: object = {}): Promise<T> {
//     return Request<T>(method, url, data, params);
//   }
// };
