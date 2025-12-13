package com.github.axinger;

import com.github.axinger.dao.BookDAO;
import com.github.axinger.dao.PersonJpaRepository;
import com.github.axinger.model.Book;
import com.github.axinger.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class JdbcDemo1Tests {

    // JpaRepository
    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // JpaRepository
    @Autowired
    private BookDAO bookDAO;

    @Test
    void test_book_save() {
        Book book = new Book();
        book.setId(1L);
        book.setBookName("海底两万里");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        book.setStartTime(dateTime);
        book.setEndTime(dateTime.plusHours(2));
        bookDAO.save(book);
    }

    @Test
    void test_book_save2() {
        Book book = new Book();
        book.setBookName("十万个为什么");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        book.setStartTime(dateTime);
        book.setEndTime(dateTime.plusHours(2));
        bookDAO.save(book);
    }

    @Test
    void test_book_saveAndFlush() {
        Book book = new Book();
        book.setId(1L);
        book.setBookName("海底两万里2");
        LocalDateTime dateTime = LocalDateTime.now().plusDays(-1);
        book.setStartTime(dateTime);
        book.setEndTime(dateTime.plusHours(2));
        bookDAO.saveAndFlush(book);
    }


    @Test
    void addUser() {
        Person user = new Person();
        user.setId(1);
        user.setUserName("jim");
        personJpaRepository.save(user);
    }


    @Test
    public void findAll() {
        List<Person> all = personJpaRepository.findAll();
        System.out.println("all = " + all);
    }

    @Test
    void test1() {
        String sql = "select * from book ";
        List<Book> query = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<>(Book.class));
        System.out.println("query = " + query);

        List<Book> list = jdbcTemplate.queryForList(sql, Book.class);
        System.out.println("list = " + list);
    }
}
