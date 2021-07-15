package com.dawei.transaction.service;

import com.dawei.transaction.pojo.Student;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author da wei
 */

public interface StudentService {
   void addRequired(Student student);

   void  addRequiresNew(Student student);

   void addNested(Student student);
}
