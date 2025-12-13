package com.github.axinger.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;
//https://juejin.cn/post/7322275119592996927
//@NotNull：  校验元素值不能为 null。如果元素为null，则验证失败。通常用于字段级别的验证。
//@NotBlank：  校验字符串元素值不能为 null 或空字符串。必须包含至少一个非空格字符(即执行trim()之后不为'')。如果元素为null或者‘‘，则验证失败。通常用于String类型的字段校验
//NotEmpty：  校验集合元素或数组元素或者字符串是否非空。通常作用于集合字段或数组字段，此时需要集合或者数字的元素个数大于0。也可以作用于字符串，此时校验字符串不能为null或空串（可以是一个空格）。注意与@NotBlank的使用区别。
//@Length：  校验字符串元素的长度。作用于字符串。注：Hibernate-Validator中注解，等同于spring-boot-starter-validation中的@Size。
//@Size：  校验集合元素个数或字符串的长度在指定范围内。在集合或字符串字段上添加 @Size 注解。
//@Min：  校验数字元素的最小值。
//@Max：  校验数字元素的最大值。
//@DecimalMin：  作用于BigDecimal类型字段， 校验字段的最小值，支持比较的值为字符串表示的十进制数。通常搭配它的inclusive()使用，区别边界问题。value 属性表示最小值，inclusive 属性表示是否包含最小值。
//@Email：  校验字符串元素是否为有效的电子邮件地址。可以通过regexp自定义邮箱匹配正则。
//@Pattern：  根据正则表达式校验字符串元素的格式。
//@Digits：  校验数字元素的整数部分和小数部分的位数。作用于BigDecimal，BigInteger，字符串，以及byte, short,int, long以及它们的包装类型。
//@Past：  校验日期或时间元素是否在当前时间之前。即是否是过去时间。作用于Date相关类型的字段。
//@Future：  校验日期或时间元素是否在当前时间之后。即是否是未来时间。作用于Date相关类型的字段。
//

//
//@Valid：对实体尽心校验
//@AssertFalse：所注参数为Boolean类型，值为false
//@AssertTrue：所注参数为Boolean类型，值为true
//@DecimalMax：所注参数为数字，值小于等于给定值
//@DecimalMin：所注参数为数字，值大于等于给定值
//@Digits：所注数为数字，且值必须是指定的位数
//@Future：所注参数为将来的日期
//@Max：所注参数为数字，值小于等于给定值
//@Min：所注参数为数字，值大于等于给定值
//@Range：所注参数需在指定区间内取值
//@NotNull：所注参数不能为NULL
//@NotBlank：所注参数有内容
//@Null：所注参数为NULL
//@Past：所注参数值为过去某个日期
//@PastOrPresent：所注参数值必须为过去某个日期，或当前日期
//@Pattern：所注参数值必须满足给定的正则表达式
//@Size：所注参数为String、List或Array，且长度在给定范围内
//@Email：所注参数必须满足Email的格式
//@Id：标明该字段是对应表的主键
//@Column：对应表的字段
//@RequestParam：获取请求参数的值
//@PathVariable：获取Url中的数据
//@RequestBody：读取请求的Body，将其参数绑定到对应处理方法的参数上


@Data
public class ParamDTO {
    @NotNull(message = "id不为空", groups = {MyGroups.Edit.class})
    private Long id;

    @NotEmpty(message = "name不为空", groups = {MyGroups.Add.class})
    @NotEmpty(message = "name不为空", groups = {MyGroups.Edit.class})
    private String name;

    @NotNull(message = "时间不为空") //（默认分组 Default.class）
    @PastOrPresent //@PastOrPresent 注解的字段不能指向未来的时间
    private Date date;
}
