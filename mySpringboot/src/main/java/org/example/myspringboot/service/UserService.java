package org.example.myspringboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.el.parser.Token;
import org.example.myspringboot.common.Result;
import org.example.myspringboot.entity.User;
import org.example.myspringboot.exception.ServiceException;
import org.example.myspringboot.mapper.UserMapper;
import org.example.myspringboot.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 功能：User 服务层 , 包括登录，注册，找回密码
 * 作者：yangjia
 * 日期：2024/8/14 下午3:55
 */
@Service   //注入Spring容器，让其管理     //ServiceImpl 封装了很多查询方法
public class UserService extends ServiceImpl<UserMapper, User> {

    @Resource
    UserMapper userMapper;

    public User selectByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();  // 查询构建器    //条件查询器
        queryWrapper.eq("username", username);   //  where username = #{username}   eq => ==
        return getOne(queryWrapper);   // select * from user where username = #{username}
    }

    @Override
    public boolean removeById(User entity) {

        return super.removeById(entity);
    }

    @Override
    public boolean removeBatchByIds(Collection<?> list) {

        return super.removeBatchByIds(list);
    }

    /**
     *  登录逻辑：检验用户名，密码是否合法；生成token
     */
    public User login(User user) {
        // 根据用户名查询到的数据库信息放入 dbuser
        User dbuser = selectByUsername(user.getUsername());

        if(dbuser == null) {    //该用户不存在，抛出自定义异常
            throw new ServiceException("用户名或密码错误！");
            //抛出异常后会被全局异常捕获器捕获到。返回响应体
        }

        // 对比两条信息的密码是否相同
        if(!new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(user.getPassword(), dbuser.getPassword())) {
            throw new ServiceException("用户名或密码错误！");
        }

        //能登陆成功时，生成token，并将token传递回前端
        String token = TokenUtils.createToken(dbuser.getId().toString(), dbuser.getPassword());
        dbuser.setToken(token);
        return dbuser;       //返回数据库的user，(包含token)
    }

    /**
     *  注册逻辑
     */
    public void register(User user) {
        throw new ServiceException("403", "公开注册已关闭");
    }

    /**
     * 重置密码
     */
    public void resetPassword(User user) {
        throw new ServiceException("403", "找回密码已关闭，请联系管理员");
    }
}



/**
 * mybatis 写法
 */
//    public void insertUser(User user) {      //此处 user来自 controller层
//        userMapper.insert(user);
//    }
//
//    public void updateUser(User user) {      //此处 user来自 controller层
//        userMapper.update(user);
//    }
//
//    public void deleteUser(int id) {
//        userMapper.delete(id);
//    }
//
//    public void batchDeleteUser(List<Integer> ids) {
//        for (Integer id : ids) {
//            userMapper.delete(id);
//        }
//    }
//
//    public List<User> selectAllUser() {
//        return userMapper.selectAll();
//    }
//
//    public User selectUserById(int id) {
//        return userMapper.selectById(id);
//    }
//
//    public List<User> selectUserByMore(String username, String name) {
//        return userMapper.selectByMore(username,name);
//    }
//
//    public List<User> selectUserByMohu(String username, String name) {
//        return userMapper.selectByMohu(username,name);
//    }
//
//    public List<User> selectByPage(Integer pageNum,Integer pageSize,String username, String name) {
//        Integer skipNum = (pageNum - 1) * pageSize;   //skipNum 是跳过的个数
//        return userMapper.selectByPage(skipNum,pageSize,username,name);
//    }
//
//    public int selectByPageTotal(String username, String name) {
//        return userMapper.selectByPageTotal(username,name);
//    }





















