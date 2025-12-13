package com.github.axinger.item;

import com.github.axinger.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserWriter implements ItemWriter<User> {


    @Override
    public void write(List<? extends User> items) throws Exception {
        // 在这里添加写入逻辑
//        for (User user : items) {
//            log.info("写入数据={}",user);
//        }

        log.info("写入数据={}", items);
    }
}
