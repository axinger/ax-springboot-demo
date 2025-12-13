//package com.github.axinger;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.jta.JtaTransactionManager;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.transaction.TransactionManager;
//import javax.transaction.UserTransaction;
//
//@Configuration
//public class TransactionConfig {
//
//    @Bean
//    public TransactionManager transactionManager() throws NamingException {
//        Context context = new InitialContext();
//        return (TransactionManager) context.lookup("java:jboss/TransactionManager");
//    }
//
//    @Bean
//    public UserTransaction userTransaction() throws NamingException {
//        Context context = new InitialContext();
//        return (UserTransaction) context.lookup("java:jboss/UserTransaction");
//    }
//
//    @Bean
//    public PlatformTransactionManager platformTransactionManager(TransactionManager transactionManager, UserTransaction userTransaction) {
//        return new JtaTransactionManager(userTransaction, transactionManager);
//    }
//}
