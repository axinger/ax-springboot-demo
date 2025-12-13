package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    @Transactional
    public void insertDataInNewThread(Long id, String name, BigDecimal balance) {
        log.info("currentThread={}", Thread.currentThread().getName());
        try {
            Thread.sleep(1000); // 模拟耗时操作
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Account account = new Account();
        account.setName(name);
        account.setBalance(balance);
        accountRepository.save(account);

        if (id == 3) {
            throw new RuntimeException("Simulated error to trigger rollback");
        }
    }


    @Transactional(rollbackFor = {Exception.class})
    public void saveSysUserAddressByTransaMan(PlatformTransactionManager transactionManager, List<TransactionStatus> transactionStatuses) {


        //将事务状态都放在同一个事务里面
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(Propagation.REQUIRES_NEW.value());                   // 事物隔离级别，每个线程都开启新事务，会比较安全一些
        TransactionStatus transactionStatus = transactionManager.getTransaction(def);   // 获得事务状态
        transactionStatuses.add(transactionStatus);


        System.out.println("子线程：" + Thread.currentThread().getName());
    }
}
