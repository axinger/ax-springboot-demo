package com.github.axinger.item;

import com.github.axinger.domain.User;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User item) {
        // 在这里添加处理逻辑
        item.setName(item.getName().toUpperCase());
        return item;
    }
}
