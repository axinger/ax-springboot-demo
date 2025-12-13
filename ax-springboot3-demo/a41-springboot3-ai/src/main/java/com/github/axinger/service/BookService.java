package com.github.axinger.service;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.awt.print.Book;
import java.util.List;

public interface BookService {
    @Tool(name = "findBooksByTitle", description = "根据书名模糊查询图书，支持部分标题匹配")
    List<Book> findBooksByTitle(@ToolParam(description = "书名关键词") String title);

    @Tool(name = "findBooksByAuthor", description = "根据作者精确查询图书")
    List<Book> findBooksByAuthor(@ToolParam(description = "作者姓名") String author);

    @Tool(name = "findBooksByCategory", description = "根据图书分类精确查询图书")
    List<Book> findBooksByCategory(@ToolParam(description = "图书分类") String category);
}
