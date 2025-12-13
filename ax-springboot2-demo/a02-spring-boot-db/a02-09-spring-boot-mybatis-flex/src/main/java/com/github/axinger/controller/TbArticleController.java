package com.github.axinger.controller;

import com.github.axinger.domain.Article;
import com.github.axinger.service.ITbArticleService;
import com.mybatisflex.core.paginate.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 控制层。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@RestController
@RequestMapping("/tbArticle")
public class TbArticleController {

    @Autowired
    private ITbArticleService tbArticleService;

    /**
     * 添加
     *
     * @param tbArticle
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    public boolean save(@RequestBody Article tbArticle) {
        return tbArticleService.save(tbArticle);
    }


    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return tbArticleService.removeById(id);
    }


    /**
     * 根据主键更新
     *
     * @param tbArticle
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public boolean update(@RequestBody Article tbArticle) {
        return tbArticleService.updateById(tbArticle);
    }


    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public List<Article> list() {
        return tbArticleService.list();
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id tbArticle主键
     * @return 详情
     */
    @GetMapping("/getInfo/{id}")
    public Article getInfo(@PathVariable Serializable id) {
        return tbArticleService.getById(id);
    }


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Page<Article> page(Page<Article> page) {
        return tbArticleService.page(page);
    }
}
