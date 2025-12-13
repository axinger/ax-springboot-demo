package com.github.axinger.item;

import com.github.axinger.domain.User;
import com.github.axinger.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
public class UserReader implements ItemReader<User> {

    private final UserService userService;
    JobParameters jobParameters;
    private Iterator<User> iterator;

    public UserReader(UserService userService) {
        this.userService = userService;
    }

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.jobParameters = stepExecution.getJobExecution().getJobParameters();
    }


    @Override
    public User read() throws Exception, ParseException, NonTransientResourceException {
        if (iterator == null) {
            this.iterator = readDataFromDatabase().iterator();
        }
        return iterator.hasNext() ? iterator.next() : null;
    }


    private List<User> readDataFromDatabase() {
        List<User> users = new ArrayList<>();
        userService.selectAll(context -> users.add(context.getResultObject()));
        log.info("读取数据={}", users);
        return users;
    }
}
