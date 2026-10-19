package org.example.myspringboot.demos.web.repository.impl;

import org.example.myspringboot.demos.web.entity.Student;
import org.example.myspringboot.demos.web.repository.StudentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 功能：
 * 作者：yangjia
 * 日期：2024/8/13 上午10:22
 */
@Repository     //指定扫描该类
public class StudentRepositoryImpl implements StudentRepository {

    private static Map<Long, Student> studentMap ;
    static {
        studentMap = new HashMap<Long, Student>();
        studentMap.put(123L,new Student(123,"小张",20));
        studentMap.put(456L,new Student(456,"小李",20));
        studentMap.put(789L,new Student(789,"小王",20));
    }


    @Override
    public Student findById(long id) {
        return studentMap.get(id);
    }

    @Override
    public Collection<Student> findAll() {
        return studentMap.values();
    }

    @Override
    public void saveOrUpdate(Student student) {
        studentMap.put(student.getId(),student);
    }

    @Override
    public void deleteById(long id) {
        studentMap.remove(id);
    }
}
