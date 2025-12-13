package com.github.axinger.service.impl;

import com.github.axinger.dao.BookRepository;
import com.github.axinger.service.BookService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;


/**
 * 方法一：工具配置
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Resource
    private BookRepository bookRepository;


    @Tool(name = "findBooksByTitle", description = "根据书名模糊查询图书，支持部分标题匹配")
    @Override
    public List<Book> findBooksByTitle(@ToolParam(description = "书名关键词") String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Tool(name = "findBooksByAuthor", description = "根据作者精确查询图书")
    @Override
    public List<Book> findBooksByAuthor(@ToolParam(description = "作者姓名") String author) {
        return bookRepository.findByAuthor(author);
    }

    @Tool(name = "findBooksByCategory", description = "根据图书分类精确查询图书")
    @Override
    public List<Book> findBooksByCategory(@ToolParam(description = "图书分类") String category) {
        return bookRepository.findByCategory(category);
    }
}
