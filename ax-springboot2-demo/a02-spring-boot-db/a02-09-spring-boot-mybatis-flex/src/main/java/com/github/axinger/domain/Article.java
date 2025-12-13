package com.github.axinger.domain;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

/**
 * 实体类。
 *
 * @author mybatis-flex-helper automatic generation
 * @since 1.0
 */
@Table(value = "tb_article")
@Data
public class Article {

    @Id(keyType = KeyType.Auto)
    private Integer id;

    @Column(value = "account_id")
    private Integer accountId;

    @Column(value = "title")
    private String title;

    @Column(value = "content")
    private String content;

    @Column(ignore = true)
    private String authorName;

    @Column(ignore = true)
    private int authorAge;

    //直接定义 Account 对象
    @Column(ignore = true)
//    @RelationOneToOne()
    private Account account;


}
