package com.dawei.transaction.service;

import com.dawei.transaction.pojo.Teacher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author da wei
 */

public interface TeacherService {
    void addRequired(Teacher teacher);
    void addRequiredException(Teacher teacher);

    void addRequiresNew(Teacher teacher);

    void addRequiresNewException(Teacher teacher);

    void addNested(Teacher teacher);

    void addNestedException(Teacher teacher);
}
