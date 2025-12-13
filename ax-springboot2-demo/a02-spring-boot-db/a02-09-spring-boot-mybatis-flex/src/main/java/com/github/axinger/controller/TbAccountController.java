package com.github.axinger.controller;

import com.github.axinger.domain.Account;
import com.github.axinger.service.ITbAccountService;
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
@RequestMapping("/tbAccount")
public class TbAccountController {

    @Autowired
    private ITbAccountService tbAccountService;

    /**
     * 添加
     *
     * @param tbAccount
     * @return {@code true} 添加成功，{@code false} 添加失败
     */
    @PostMapping("/save")
    public boolean save(@RequestBody Account tbAccount) {
        return tbAccountService.save(tbAccount);
    }


    /**
     * 根据主键删除
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("/remove/{id}")
    public boolean remove(@PathVariable Serializable id) {
        return tbAccountService.removeById(id);
    }


    /**
     * 根据主键更新
     *
     * @param tbAccount
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("/update")
    public boolean update(@RequestBody Account tbAccount) {
        return tbAccountService.updateById(tbAccount);
    }


    /**
     * 查询所有
     *
     * @return 所有数据
     */
    @GetMapping("/list")
    public List<Account> list() {
        return tbAccountService.list();
    }


    /**
     * 根据主键获取详细信息。
     *
     * @param id tbAccount主键
     * @return 详情
     */
    @GetMapping("/getInfo/{id}")
    public Account getInfo(@PathVariable Serializable id) {
        return tbAccountService.getById(id);
    }


    /**
     * 分页查询
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("/page")
    public Page<Account> page(Page<Account> page) {
        return tbAccountService.page(page);
    }
}
