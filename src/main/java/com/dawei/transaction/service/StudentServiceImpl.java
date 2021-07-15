package com.dawei.transaction.service;

import com.dawei.transaction.mapper.StudentDao;
import com.dawei.transaction.pojo.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author da wei
 * @description
 * @create 2021/7/15 11:07
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(Student student) {
        studentDao.insert(student);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(Student student) {
        studentDao.insert(student);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(Student student) {
        studentDao.insert(student);
    }

}
