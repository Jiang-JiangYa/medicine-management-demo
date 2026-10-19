package org.example.myspringboot.demos.web.controller;

import org.example.myspringboot.demos.web.entity.Student;
import org.example.myspringboot.demos.web.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * 功能：
 * 作者：yangjia
 * 日期：2024/8/13 上午11:16
 */

@RestController                         //作为前后台反数据的服务
@RequestMapping("/student")          //定义了这个控制器处理的请求的基本路径
public class StudentHandler {

    @Autowired     //自动装载,Spring会自动查找StudentRepository的实现类。在实现类文件前用@Repository注释指定扫描该文件
    private StudentRepository studentRepository;

    @GetMapping("/findall")
    public Collection<Student> findAll() {    //返回集合
         return  studentRepository.findAll();
    }

    @GetMapping("/findById/{id}")    //前端传递的参数id
    public Student findById(@PathVariable("id") long id) {   //后端通过@PathVariable("id")接收参数
        return studentRepository.findById(id);
    }

    @PostMapping("/save")     //向数据库新增信息--post请求
    public void save(@RequestBody Student student){         //前端返回的数据是JSON格式，通过@RequestBody进行映射，将其与JAVA实体类Student对应起来
        studentRepository.saveOrUpdate(student);
    }

    @PutMapping("/update")     //修改数据库信息是 --Put请求
    public void update(@RequestBody Student student){
        studentRepository.saveOrUpdate(student);
    }

    @DeleteMapping("/deleteById/{id}")     //删除数据--delete请求
    public void deleteById(@PathVariable("id") long id){
        studentRepository.deleteById(id);
    }

}

































