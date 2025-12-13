package com.github.axinger;

import com.github.axinger.dao.UserRepository;
import com.github.axinger.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class DataRedisTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test1() {

        final User.Book book = User.Book.builder()
                .id(1)
                .name("海底两万里")
                .build();

        final User user = User.builder()
                .id(1)
                .name("jim")
                .age(21)
                .date(new Date())
                .updateTime(LocalDateTime.now())
                .books(List.of(book))
                .build();


        User save = userRepository.save(user);
        System.out.println("save = " + save);
    }

    @Test
    public void test3() {

        List<User> list = new ArrayList<>();


        {

            //        final User.Book book = User.Book.builder()
//                .id(1)
//                .name("海底两万里")
//                .build();

            final User user = User.builder()
                    .id(1)
                    .name("jim")
                    .age(21)
                    .date(new Date())
//                .books(List.of(book))
                    .build();

            list.add(user);
        }

        {

            //        final User.Book book = User.Book.builder()
//                .id(1)
//                .name("海底两万里")
//                .build();

            final User user = User.builder()
                    .id(2)
                    .name("jim")
                    .age(21)
                    .date(new Date())
//                .books(List.of(book))
                    .build();

            list.add(user);
        }

        Iterable<User> users = userRepository.saveAll(list);
        System.out.println("users = " + users);
    }

    @Test
    public void test2() {
        Optional<User> byId = userRepository.findById(1L);
        System.out.println("byId = " + byId);
    }
}
