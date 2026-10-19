package org.example.myspringboot.exception;

import lombok.Getter;

/**
 * 功能：自定义异常
 * 作者：yangjia
 * 日期：2024/8/16 下午6:02
 */
@Getter
public class ServiceException extends RuntimeException {

    private final  String code;

    //运行时异常,表示在程序运行过程中可能发生的异常情况
    public ServiceException(String msg) {
        super(msg);   // ServiceException 构造函数的 msg 参数传递给RuntimeException的构造函数。
        this.code = "500";
    }

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
