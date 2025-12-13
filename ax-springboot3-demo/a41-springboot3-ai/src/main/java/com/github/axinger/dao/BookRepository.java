package com.github.axinger.dao;

import java.awt.print.Book;
import java.util.List;

public interface BookRepository {
    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthor(String author);

    List<Book> findByCategory(String category);
}
