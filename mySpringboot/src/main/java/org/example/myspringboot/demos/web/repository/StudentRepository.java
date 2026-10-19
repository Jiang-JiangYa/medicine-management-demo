package org.example.myspringboot.demos.web.repository;

import org.example.myspringboot.demos.web.entity.Student;

import java.util.Collection;

public interface StudentRepository {
    public Student findById(long id);
    public Collection<Student> findAll();
    public void saveOrUpdate(Student student);
    public void deleteById(long id);
}
