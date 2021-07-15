package com.dawei.transaction.test;

import com.dawei.transaction.pojo.Student;
import com.dawei.transaction.pojo.Teacher;
import com.dawei.transaction.service.StudentService;
import com.dawei.transaction.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author da wei
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    StudentService studentService;
    @Autowired
    TeacherService teacherService;

    /*-----------------------------1.  测试   propagation = Propagation.REQUIRED---------------------------------------------------------------------------------*/

    /**
     * 1.1  外网没有开启事务
     */

    //1.1.1  在单元测试中调用该方法。 测试结果： 外部未开启事务，两个添加方法在自己的事务中独立运行。外部的异常不影响内部两个方法的插入。
    @Override
    public void noTransactionExceptionRequiredRequired() {
        Student student = new Student();
        student.setName("学生1.1.1");
        studentService.addRequired(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师1.1.1");
        teacherService.addRequired(teacher);
        int i = 1 / 0;
    }

    //1.1.2  在单元测试中调用该方法。 测试结果：学生2 成功插入到数据库。  外围方法没有事务，两个插入的方法独自创建自己的事务，教师添加遇到异常 导致 回滚，学生添加成功。
    @Override
    public void noTransactionRequiredRequiredException() {
        Student student = new Student();
        student.setName("学生1.1.2");
        studentService.addRequired(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师1.1.2");
        teacherService.addRequiredException(teacher);
    }
    //结论：通过上面两个方法我们证明了在外围方法未开启事务的情况下Propagation.REQUIRED修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。

    /**
     * 1.2 外围方法开启事务，这个是使用率比较高的场景。
     */

    //1.2.1 在单元测试中调用该方法。  测试结果： 学生和教师均保存失败。 外围方法开启事务，内部方法加入外围方法事务，外围方法回滚，内部方法也要回滚。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionExceptionRequiredRequired() {
        Student student = new Student();
        student.setName("学生1.2.1");
        studentService.addRequired(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师1.2.1");
        teacherService.addRequired(teacher);
        int i = 1 / 0;

    }

    //1.2.2 测试结果：全部插入失败。  外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，外围方法感知异常致使整体事务回滚。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiredException() {
        Student student = new Student();
        student.setName("学生1.2.2");
        studentService.addRequired(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师1.2.2");
        teacherService.addRequiredException(teacher);

    }

    //1.2.3  全部保存失败。 外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，即使方法被catch不被外围方法感知，整个事务依然回滚。
    //这里我特地测试了1.2.1中的方法，捕获 1/0 异常， 发现两个均能插入成功。  与上面对比，即 捕获普通方法 是不会使事务中止的。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiredExceptionTry() {
        Student student = new Student();
        student.setName("学生1.2.3");
        studentService.addRequired(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师1.2.3");
        try {
            teacherService.addRequiredException(teacher);
        } catch (Exception e) {
            System.out.println("出现异常");
        }
    }
    //结论：以上试验结果我们证明在外围方法开启事务的情况下Propagation.REQUIRED修饰的内部方法会加入到外围方法的事务中，所有Propagation.REQUIRED修饰的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚。

    /*-----------------------------2.  测试   propagation = Propagation.REQUIRES_NEW--------------------------------------------------------------------------------*/

    /**
     * 2.1 外围方法没有开启事务。
     */

    //2.1.1 测试结果 都保存成功。 外围方法没有事务，插入学生、教师方法都在自己的事务中独立运行,外围方法抛出异常回滚不会影响内部方法。
    @Override
    public void noTransactionExceptionRequiresNewRequiresNew() {
        Student student = new Student();
        student.setName("学生2.1.1");
        studentService.addRequiresNew(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师2.1.1");
        teacherService.addRequiresNew(teacher);
        int i = 1 / 0;
    }

    //2.1.2 仅有学生保存成功。外围方法没有开启事务，插入学生方法和插入教师方法分别开启自己的事务，插入教师方法抛出异常回滚，其他事务不受影响。
    @Override
    public void noTransactionRequiresNewRequiresNewException() {
        Student student = new Student();
        student.setName("学生2.1.2");
        studentService.addRequiresNew(student);

        Teacher teacher = new Teacher();
        teacher.setName("教师2.1.2");
        teacherService.addRequiresNewException(teacher);
    }
    //结论：通过这两个方法我们证明了在外围方法未开启事务的情况下Propagation.REQUIRES_NEW修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。

    /**
     * 2.2 外围方法开启事务。
     */

    //2.2.1 学生插入失败，教师都插入成功。 外围方法开启事务，插入 学生2.2.1 方法和外围方法一个事务，插入 教师2.2.1-1方法、插入教师2.2.1-2方法分别在独立的新建事务中，外围方法抛出异常只回滚和外围方法同一事务的方法，故插入学生2.2.1的方法回滚。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionExceptionRequiredRequiresNewRequiresNew() {
        Student student = new Student();
        student.setName("学生2.2.1");
        studentService.addRequired(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师2.2.1-1");
        teacherService.addRequiresNew(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setName("教师2.2.1-2");
        teacherService.addRequiresNew(teacher2);
        int i = 1 / 0;
    }

    //2.2.2 仅有 教师2.2.2-1 插入成功。外围方法开启事务，插入“学生2.2.2“方法和外围方法一个事务，插入“教师2.2.2-1”方法、插入“教师2.2.2-2”方法分别在独立的新建事务中。插入“教师2.2.2-2”方法抛出异常，首先插入 “教师2.2.2-2”方法的事务被回滚，异常继续抛出被外围方法感知，外围方法事务亦被回滚，故插入“学生2.2.2”方法也被回滚。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiresNewRequiresNewException() {
        Student student = new Student();
        student.setName("学生2.2.2");
        studentService.addRequired(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师2.2.2-1");
        teacherService.addRequiresNew(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setName("教师2.2.2-2");
        teacherService.addRequiresNewException(teacher2);
    }

    //2.2.3 学生2.2.3 和 教师2.2.3-1 插入成功。
    //外围方法开启事务，插入“学生2.2.3”方法和外围方法一个事务，插入“教师2.2.3-1”方法、插入“教师2.2.3-2”方法分别在独立的新建事务中。插入“教师2.2.3-2”方法抛出异常，首先插入“教师2.2.3-2”方法的事务被回滚，异常被catch不会被外围方法感知，外围方法事务不回滚，故插入“学生2.2.3”方法插入成功。
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void transactionRequiredRequiresNewRequiresNewExceptionTry() {
        Student student = new Student();
        student.setName("学生2.2.3");
        studentService.addRequired(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师2.2.3-1");
        teacherService.addRequiresNew(teacher1);

        Teacher teacher2 = new Teacher();
        teacher2.setName("教师2.2.3-2");
        try {
            teacherService.addRequiresNewException(teacher2);
        } catch (Exception e) {
            System.out.println("异常");
        }
    }
    //结论：在外围方法开启事务的情况下Propagation.REQUIRES_NEW修饰的内部方法依然会单独开启独立事务，且与外部方法事务也独立，内部方法之间、内部方法和外部方法事务均相互独立，互不干扰。

    /*-----------------------------3.  测试   propagation = Propagation.NESTED--------------------------------------------------------------------------------*/

    /**
     * 3.1 外围方法没有开启事务。
     */

    //3.1.1 两个都插入成功。 外围方法未开启事务，插入“学生3.1.1”、“教师3.1.1”方法在自己的事务中独立运行，外围方法异常不影响内部插入“学生3.1.1”、“教师3.1.1”方法独立的事务。
    @Override
    public void noTransactionExceptionNestedNested() {
        Student student = new Student();
        student.setName("学生3.1.1");
        studentService.addNested(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师3.1.1");
        teacherService.addNested(teacher1);
        int i = 1 / 0;
    }

    //3.1.2  仅学生3.1.2插入成功。 外围方法没有事务，插入“学生3.1.2”、“教师3.1.2”方法都在自己的事务中独立运行,所以插入“教师3.1.2”方法抛出异常只会回滚插入“教师3.1.2”方法，插入“学生3.1.2”方法不受影响。
    @Override
    public void noTransactionNestedNestedException() {
        Student student = new Student();
        student.setName("学生3.1.2");
        studentService.addNested(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师3.1.2");
        teacherService.addNestedException(teacher1);
    }
    //结论：通过这两个方法我们证明了在外围方法未开启事务的情况下Propagation.NESTED和Propagation.REQUIRED作用相同，修饰的内部方法都会新开启自己的事务，且开启的事务相互独立，互不干扰。

    /**
     * 3.2 外围方法开启事务
     */
    //3.2.1 都插入失败。 外围方法开启事务，内部事务为外围事务的子事务，外围方法回滚，内部方法也要回滚。
    @Override
    @Transactional
    public void transactionExceptionNestedNested() {
        Student student = new Student();
        student.setName("学生3.2.1");
        studentService.addNested(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师3.2.1");
        teacherService.addNested(teacher1);
        int i = 1 / 0;
    }

    //3.2.2 都插入失败。外围方法开启事务，内部事务为外围事务的子事务，内部方法抛出异常回滚，且外围方法感知异常致使整体事务回滚。
    @Override
    @Transactional
    public void transactionNestedNestedException() {
        Student student = new Student();
        student.setName("学生3.2.2");
        studentService.addNested(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师3.2.2");
        teacherService.addNestedException(teacher1);
    }

    //3.2.3 仅学生3.2.3 插入成功。 外围方法开启事务，内部事务为外围事务的子事务，插入“教师3.2.3”内部方法抛出异常，可以单独对子事务回滚。
    @Override
    @Transactional
    public void transactionNestedNestedExceptionTry() {
        Student student = new Student();
        student.setName("学生3.2.3");
        studentService.addNested(student);

        Teacher teacher1 = new Teacher();
        teacher1.setName("教师3.2.3");
        try {
            teacherService.addNestedException(teacher1);
        } catch (Exception e) {
            System.out.println("exception");
        }
        //结论：以上试验结果我们证明在外围方法开启事务的情况下Propagation.NESTED修饰的内部方法属于外部事务的子事务，外围主事务回滚，子事务一定回滚，而内部子事务可以单独回滚而不影响外围主事务和其他子事务
    }



}
