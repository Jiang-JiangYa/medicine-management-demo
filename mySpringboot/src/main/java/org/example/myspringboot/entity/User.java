package org.example.myspringboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 功能：
 * 作者：yangjia
 * 日期：2024/8/14 下午2:22
 */

@TableName("USERS")    //对应数据库中的表
@Data
public class User {

    @TableId(type = IdType.AUTO)    //标明该字段自动递增
    private Integer id;
    private String username;
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String role;

    @TableField(exist = false)   //该字段数据库没有
    private String token;
}



























