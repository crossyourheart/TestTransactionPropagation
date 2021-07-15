package com.dawei.transaction;

import com.dawei.transaction.test.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wang wei
 * @description
 * @create 2021/7/15 13:34
 */
@SpringBootTest
public class TestTransactionPropagation {
    @Autowired
    TestService testService;

    @Test
    public void test1() {
        testService.noTransactionExceptionRequiredRequired();
    }
    @Test
    public void test2() {
        testService.noTransactionRequiredRequiredException();
    }
    @Test
    public void test3() {
        testService.transactionExceptionRequiredRequired();
    }
    @Test
    public void test4() {
        testService.transactionRequiredRequiredException();
    }
    @Test
    public void test5() {
        testService.transactionRequiredRequiredExceptionTry();
    }

    /*-----------------2.1------------------*/
    @Test
    public void test6() {
        testService.noTransactionExceptionRequiresNewRequiresNew();
    }
    @Test
    public void test7() {
        testService.noTransactionRequiresNewRequiresNewException();
    }
    /*-----------------2.2------------------*/
    @Test
    public void test8() {
        testService.transactionExceptionRequiredRequiresNewRequiresNew();
    }
    @Test
    public void test9() {
        testService.transactionRequiredRequiresNewRequiresNewException();
    }

    @Test
    public void test10() {
        testService.transactionRequiredRequiresNewRequiresNewExceptionTry();
    }
    /*-----------------3.1------------------*/
    @Test
    public void test11() {
        testService.noTransactionExceptionNestedNested();
    }
    @Test
    public void test12() {
        testService.noTransactionNestedNestedException();
    }
    /*-----------------3.2------------------*/
    @Test
    public void test13() {
        testService.transactionExceptionNestedNested();
    }
    @Test
    public void test14() {
        testService.transactionNestedNestedException();
    }
    @Test
    public void test15() {
        testService.transactionNestedNestedExceptionTry();
    }


}
