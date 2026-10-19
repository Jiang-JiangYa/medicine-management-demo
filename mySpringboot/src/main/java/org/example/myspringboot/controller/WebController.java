package org.example.myspringboot.controller;

import org.example.myspringboot.common.Result;
import org.example.myspringboot.common.Obj;
import org.springframework.web.bind.annotation.*;

/**
 * 功能：提供接口返回数据
 * 作者：yangjia
 * 日期：2024/8/9 下午3:18
 */
@RestController
@RequestMapping("/web")
public class WebController {

    @GetMapping("/hello")
    public Result hello(String name) {
        return Result.success("hello,"+name);
    }

    @PostMapping("/post")
    public Result post(@RequestBody Obj obj) {  //若没有@RequestBody 注解进行映射，只能把对象的属性通过Url参数(Params)传递,如?name=张三丰&school=sspu
        return Result.success(obj);
    }

    @PutMapping("/put")
    public Result put(@RequestBody Obj obj) {
        return Result.success(obj);
    }

    @DeleteMapping("/deleteByName/{name}")
    public Result delete(@PathVariable("name") String name) {
        return Result.success(name);
    }

}























