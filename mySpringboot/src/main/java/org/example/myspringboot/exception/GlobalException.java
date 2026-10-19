package org.example.myspringboot.exception;

import org.example.myspringboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 功能：捕获全局抛出的某些异常，并进行处理
 * 作者：yangjia
 * 日期：2024/8/16 下午5:58
 */

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler( ServiceException.class)      //当捕获到ServiceException时进行的处理：
    @ResponseBody     //将返回结果作为响应体返回（转化为JSON格式）
    public Result serviceException(ServiceException e) {
        return Result.error(e.getCode(),e.getMessage());
    }

    @ExceptionHandler( Exception.class)
    @ResponseBody     //将返回结果作为响应体返回（转化为JSON格式）
    public Result globalException(Exception e) {
        e.printStackTrace();         //打印错误信息
        return Result.error("500","系统错误！");
    }

}
