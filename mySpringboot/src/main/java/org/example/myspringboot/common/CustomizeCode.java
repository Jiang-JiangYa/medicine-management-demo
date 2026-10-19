package org.example.myspringboot.common;

public interface CustomizeCode {

    String SUCCESS = "200"; // 成功
    String FAIL = "500"; // 失败
    String LOGIN_FAIL = "10001"; // 用户名或密码不正确
    String ACCOUNT_DISABLED = "10002"; // 用户被禁用
    String ACCOUNT_LOCKED = "10003"; // 用户被锁定
    String INVALID_TOKEN = "10004"; // token无效
    String TOKEN_TIMEOUT = "10005"; // token已过期
    String BE_REPLACED = "10006"; // 用户被顶下线
    String KICK_OUT = "10007"; // 用户被踢下线
    String NOT_LOGIN = "10008"; // 当前会话未登录
    String NO_TOKEN = "10401"; // 未提供token
    String NO_ROLE = "10402"; // 无此角色
    String NO_PERMISSION = "10403"; // 无此权限
    String DATA_NOT_FOUND = "10404";// 未查询到相关数据
    String MISSING_PARAMETER = "10009";// 参数缺失
    String MAJOR_PARAMETERS_ARE_NOT_CONFIGURED = "10010";// 重要参数未配置
    String ALERADY_CONSULTATION = "10011";// 已被其他医生接诊
    String ALERADY_EXIST_CONSULT = "10012";// 此患者今日在本科室已存在就诊记录
    String PATIENTINFO_NOT_EXIST = "10013";// 患者信息不存在
    String OLD_PASSWORD_WRONG = "10014";// 原密码错误
    String CONFIGURATION_ERROR = "10015";// 配置有误
    String DATA_ALREADY_UPLOAD = "10016";// 该药品已经上传平台，不能退费
    String REFUND_ALREADY_OPERATE = "10017";// 该药品已经上传平台，不能退费
    String PROJECT_EXECUTED_REFUND = "10018";// 项目已经执行不能执行退费操作
    String RETURN_QUANTITY_MISTAKE = "10019";// 数量错误
    String QUANTITY_NOT_DECIMAL = "10022";// 数量不可输入小数
    String RECIPE_IN_CHARGE = "10023";// 当前处方正在收费中，不可操作!
    String USER_NOT_NULL = "10025";// 用户信息不能为空！
    String DETAILS_ARE_MISSING = "10026";// 未查到医技明细！
    String ILLEGAL_PARAMETER = "10027";// 参数值非法
    String ORDER_STATUS_UPDATE = "10028";// 医嘱状态已更新，请刷新后重试
    String STATUS_UPDATE = "10029";// 状态已更新，请刷新后重试
    String TEMPLATE_NAME_REPEAT = "10030"; //模板名称已存在
    String APPOINT_FULL = "10031"; //该时段已约满，请刷新后重试
    String APPOINT_UNRECOVERED = "10036"; //该患者存在未履约的医技项目无法继续预约!
    String PRESCRIPTION_REVIEW_FAILED = "10032";// 处方审核不通过
    String PRE_SCRIPTING_INTERFACE_CALL_EXCEPTION = "10033";
    String CHANNEL_REPEAT = "10034";//渠道代码或名称已存在
    String ITEM_REPEAT = "10035";//项目代码或名称已存在
    String PARAM_IS_NULL = "10037";//必填参数不能为空
    String NO_AVAILABLE_ORG = "10038";//未查询到有效的机构信息，请检查机构代码是否有误
    String STARTDATE_LT_NOW = "10039";//开始日期不能小于当前日期
    String STARTDATE_GT_ENDDATE = "10040";//结束日期不能小于开始日期
    String ORG_INVALID = "10042";//未查询到此机构信息
    String REG_APPOINT_UNRECOVERED = "10043"; //该患者已预约，不可重复预约!
    String ORG_UNREGISTER = "10044"; //机构信息未注册
    String DEPT_UNREGISTER = "10045"; //科室信息未注册
    String DOCTOR_UNREGISTER = "10046"; //医生信息未注册
    String SELF_ERROR = "20000";// 自定义错误
    String SELF_ALERT = "20001";// 自定义弹窗


}
