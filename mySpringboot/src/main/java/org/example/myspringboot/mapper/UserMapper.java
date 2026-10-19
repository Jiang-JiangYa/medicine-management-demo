package org.example.myspringboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import org.apache.ibatis.annotations.*;
import org.example.myspringboot.entity.User;

import java.util.List;


public interface UserMapper extends BaseMapper<User> {
    //BaseMapper 里有封装很多方法，




     /**
      * mybatis 的写法
      */
//    //用insert注解，通过sql语句将user插入数据库; 通过 #{} 解析user对象的属性
//    @Insert("insert into `user` (username,password,name,phone,email,address,avatar) " +
//            "values (#{username},#{password},#{name},#{phone},#{email},#{address},#{avatar})")
//    void insert(User user);    //user来自Service层
//
//    @Update("update `user` set username = #{username}, password = #{password}, name = #{name}," +
//            "phone = #{phone}, email = #{email}, address = #{address}, avatar = #{avatar} where id = #{id}")
//    void update(User user);
//
//    @Delete("delete from `user` where id = #{id}")
//    void delete(int id);
//
//    @Select("select * from `user` order by id desc")
//    List<User> selectAll();
//
//    @Select("select * from `user` where id = #{id} ")
//    User selectById(int id);
//
//    @Select("select * from `user` where name = #{name}  and username= #{username}")
//    List<User> selectByMore(@Param("username") String username, @Param("name") String name);
//
//    @Select("select * from `user` where name like concat('%',#{name},'%')  or username like concat('%',#{username},'%')")
//    List<User> selectByMohu(@Param("username") String username, @Param("name") String name);
//
//    /**
//     * 分页多条件查询信息
//     * limit #{skipNum}, #{pageSize}  ：将以获得的查询结果跳过前面 skipNum 组数据，然后查询 pageSize个
//     */
//    @Select("select * from `user` where name like concat('%',#{name},'%')  or username like concat('%',#{username},'%') order by id desc limit #{skipNum}, #{pageSize}")
//    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize, @Param("username") String username, @Param("name") String name);
//
//    /**
//     *  count(id) 统计数据个数
//     */
//    @Select("select count(id) from `user` where name like concat('%',#{name},'%')  or username like concat('%',#{username},'%') ")
//    int selectByPageTotal(@Param("username")String username, @Param("name") String name);
//
//    @Select("select * from `user` where username = #{username} ")
//    User selectByUsername(String username);
}



























