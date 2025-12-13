package com.github.axinger.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.hibernate.annotations.Comment;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @TableName book
 */
@Data
@TableName(value = "book")
@Entity(name = "book")
// @Table(indexes = {
//         @Index(columnList = "book_name"),
//         @Index(columnList = "book_price")
// })
@Table(indexes = {
        @Index(columnList = "book_name"),
        @Index(columnList = "book_price")
})
// @Table(name = "book", uniqueConstraints = @UniqueConstraint(columnNames = {"book_name", "book_price"}))
public class BookEntity implements Serializable {

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 分组求个数
     * select 需要排除
     */
    @Transient
    @TableField(value = "count(*)",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER,
            select = false)
    public Long count;
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自增,有nextval('book_id_seq'::regclass),连续
    // @GeneratedValue(strategy = GenerationType.AUTO) // 自增但不连续
    @Column(name = "id")
    @Comment("主键")
    // 字段值为只读的，不允许插入和修改。通常用于主键和外键
    // @Column(name = "id", insertable = false, updatable = false)
    private Long id;
    /**
     *
     */
    @Column(name = "book_author")
    @Comment("作者")
    @TableField(value = "book_author")
    private String bookAuthor;
    /**
     * 字段“text”，指定建表时SQL语句 如“varchar(50) NOT NULL”
     */
    @Column(name = "book_name", length = 50, nullable = false)
    @Comment("书名")
//    @TableField(value = "book_name")
    @TableField(value = "book_name", condition = SqlCondition.LIKE)
    private String bookName;


    //  update = "%s+1" 不走 lambdaUpdate
    /**
     * 指定字段“totalAmount”交易金额的精度（长度）为10，小数点位数为2位，且值不能为null
     */
    @Column(name = "book_price", precision = 10, scale = 2)
    @Comment("价格")
    @TableField(value = "book_price")
    private BigDecimal bookPrice;
    /**
     * 该方式绑定在 entity 上,baseMapper提供的 update(entity,updateWrapper) 中的entity不能null,而且所有的update方法均不能再改变此值为字段实际的指定值
     */
    @Column(name = "update_count")
    @Comment("更新次数")
    @TableField(value = "update_count", updateStrategy = FieldStrategy.IGNORED, update = "%s+1")
    private int updateCount;
    /**
     * 分组求和
     */
    @Transient
    @TableField(value = "sum(book_price)",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER,
            select = false)
    private BigDecimal sumBookPrice;

    /**
     * 分组平均值
     */
    @Transient
    @TableField(value = "avg(book_price)",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER,
            select = false)
    private BigDecimal avgBookPrice;

    /**
     * 分组最小值
     */
    @Transient
    @TableField(value = "min(book_price)",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER,
            select = false)
    private BigDecimal minBookPrice;

    /**
     * 分组最大值
     */
    @Transient
    // @TableField(value = "max(book_price)", exist = false)// mbp这个不可以
    @TableField(value = "max(book_price)",
            insertStrategy = FieldStrategy.NEVER,
            updateStrategy = FieldStrategy.NEVER,
            select = false)
    private BigDecimal maxBookPrice;
}
