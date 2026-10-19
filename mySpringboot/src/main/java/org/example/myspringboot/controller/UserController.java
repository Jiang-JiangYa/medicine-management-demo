package org.example.myspringboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.exception.ServiceException;
import org.example.myspringboot.service.UserService;
import org.example.myspringboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
 * 功能：
 * 作者：yangjia
 * 日期：2024/8/14 下午3:55
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired   //从Spring容器中拿
    UserService userService;   //直接声明，自动创建无参对象

    @PostMapping("/add")
    public Result add(@RequestBody User user) {    //user来自前端传递
        try {
            if (StrUtil.isBlank(user.getPassword())) return Result.error("密码不能为空");
            user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(user.getPassword()));
            userService.save(user);       //继承的方法，用于插入用户信息
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                return Result.error("插入数据库错误，可能是用户名已存在");
            } else {
                return Result.error("系统错误！");
            }
        }
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(user.getPassword()));
        } else { user.setPassword(null); }
        userService.updateById(user);    //继承的方法，用于更新用户信息
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable("id") Integer id) {
        User currentUser = TokenUtils.getCurrentUser();
        if(id.equals(currentUser.getId())){
            throw new ServiceException("不能删除当前正在使用的账号！");
        }
        userService.removeById(id);      //继承的方法，用于删除用户信息
        return Result.success();
    }

    /**
     * 批量删除信息
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        User currentUser = TokenUtils.getCurrentUser();
        if(currentUser != null && currentUser.getId() != null && ids.contains(currentUser.getId())){
            throw new ServiceException("不能删除当前正在使用的账号！");
        }
        userService.removeBatchByIds(ids);          //继承的方法，用于批量删除用户信息
        return Result.success();
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        List<User> users = userService.list(new QueryWrapper<User>().orderByDesc("id"));  //查询所有信息，按id倒叙排列
        // select * from user order by id   desc   倒序
        return Result.success(users);
    }    //继承的方法，用于查询所有用户信息

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable("id") int id) {
        return Result.success(userService.getById(id));          //继承的方法，用于查询用户信息
    }

    /**
     * 分页多条件模糊查询 ， 返回统计数据个数
     * 返回 page ; page包含？
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam String username, @RequestParam String name,
                               @RequestParam int pageNum, @RequestParam int pageSize) {   //当前页码，当前页容量为多少条

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");  // 默认按id 倒序排列，最新数据在上
        // 执行了两次模糊查询  为空则不进行查询，，“name”对应数据库字段
        queryWrapper.like(StrUtil.isNotBlank(username), "username", username);
        queryWrapper.like(StrUtil.isNotBlank(name), "name", name);
        // select * from user where username like '%#{username}%' and name like '%#{name}%'   两个语句是 && 的关系

        Page<User> page = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        // page 有2个参数 （page, queryWrapper）
        return Result.success(page);
    }

}
























