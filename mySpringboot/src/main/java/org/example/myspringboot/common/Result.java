package org.example.myspringboot.common;

import lombok.*;

/**
 * 功能：接口统一返回包装类
 * 作者：yangjia
 * 日期：2024/8/9 下午4:29
 */


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder     //链式构造方法
public class Result {

    public static final String CODE_SUCCESS = "200";
    public static final String CODE_AUTH_ERROR = "401";
    public static final String CODE_SYS_ERROR = "500";

    /**
     * 返回对象必包含的三个属性
     * code: 请求的返回编码200 400 401 500；表示请求是成功或失败
     * msg: 表示错误的详细信息
     * data: 前后端传递的数据
     */
    private String code;
    private String msg;
    private Object data;


    public static Result success() {
        return new Result(CODE_SUCCESS,"请求成功",null);
    }

    public static Result success(Object data) {
        return new Result(CODE_SUCCESS,"请求成功",data);
    }

    public static Result error(String msg) {
        return new Result(CODE_SYS_ERROR,msg,null);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg,null);
    }

    public static Result error() {
        return new Result(CODE_SYS_ERROR,"系统错误",null);
    }


}
