package com.dawei.transaction.service;

import com.dawei.transaction.mapper.TeacherDao;
import com.dawei.transaction.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author da wei
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherDao teacherDao;

    /**
     * 虽然 @Transactional 注解可以作用于接口、接口方法、类以及类方法上，
     * 但是 Spring 建议不要在接口或者接口方法上使用该注解，因为这只有在使用基于接口的代理时它才会生效。
     * 代理模式有JDK代理 和 cglib代理
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(Teacher teacher) {
        teacherDao.insert(teacher);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredException(Teacher teacher) {
        teacherDao.insert(teacher);
        //此处会抛出一个运行时异常
        int i = 1 / 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(Teacher teacher) {
        teacherDao.insert(teacher);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(Teacher teacher) {
        teacherDao.insert(teacher);
        //此处会抛出一个运行时异常
        int i = 1 / 0;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNested(Teacher teacher) {
        teacherDao.insert(teacher);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void addNestedException(Teacher teacher) {
        teacherDao.insert(teacher);
        int i = 1 / 0;
    }
}