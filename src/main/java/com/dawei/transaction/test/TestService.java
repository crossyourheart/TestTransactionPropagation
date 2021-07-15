package com.dawei.transaction.test;

/**
 * @author da wei
 */

public interface TestService {
    public void noTransactionExceptionRequiredRequired();

    void noTransactionRequiredRequiredException();
    public void transactionExceptionRequiredRequired();

    //1.2.2
    void transactionRequiredRequiredException();

    //1.2.3
    void transactionRequiredRequiredExceptionTry();

    //2.1.1
    void noTransactionExceptionRequiresNewRequiresNew();

    void noTransactionRequiresNewRequiresNewException();

    //2.2.1
    //@Transactional(propagation = Propagation.REQUIRED)
    void transactionExceptionRequiredRequiresNewRequiresNew();

    //2.2.2
    void transactionRequiredRequiresNewRequiresNewException();

    void transactionRequiredRequiresNewRequiresNewExceptionTry();


    void  noTransactionExceptionNestedNested();

    //3.1.2
    void noTransactionNestedNestedException();

    //3.2.1
    void transactionExceptionNestedNested();

    void transactionNestedNestedException();

    void transactionNestedNestedExceptionTry();
}
