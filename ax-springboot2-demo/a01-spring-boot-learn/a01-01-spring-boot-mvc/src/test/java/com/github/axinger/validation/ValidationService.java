package com.github.axinger.validation;

import com.github.axinger.model.dto.DateDTO;
import com.github.axinger.model.dto.LoginDTO;
import com.github.axinger.model.dto.MyGroups;
import com.github.axinger.model.dto.ParamDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.List;

/**
 * "
 *
 * @NotBlank: 这个注解用于检查字符串是否为非空且不为仅空白字符。它会检查字段是否为 null、长度为0或只有空白字符（如空格、制表符等）。
 * 对于 String 类型来说，这表示字符串必须至少包含一个非空白字符。
 * @NotEmpty: 这个注解比 @NotBlank 更宽松一点，它只检查字段是否为 null 或者集合、数组、Map 是否为空（即长度或大小为0）。
 * 对于 String 类型，它允许字符串包含空白字符，只要不是 null 并且长度不为0即可。
 * 至于 .List 的使用，比如 @NotBlank.List 和 @NotEmpty.List，这是为了能够在同一个字段上应用多个相同类型的约束
 */
@Service
@Validated
public class ValidationService {
    public void strNo(String name) {

    }

    public void strNotBlank(@NotBlank(message = "不能空字符串,也不能空格") String name) {
    }

    public void strNotEmpty(@NotEmpty(message = "字符串不能空,可以空格") String name) {
    }

    public void strNotNull(@NotNull(message = "字符串不能为null") String name) {
    }


    public void strNotNull2(@NotBlank.List(value = {
            @NotBlank(message = "Field cannot be blank for group A", groups = MyGroups.GroupA.class),
            @NotBlank(message = "Field cannot be blank for group B", groups = MyGroups.GroupB.class)
    }) String name) {
    }


    public void strNotNull3(@NotEmpty.List(value = {@NotEmpty(message = "111")}) String name) {
    }

    public void listNotEmpty(@NotEmpty(message = "list不能空") List<String> list) {
    }

    /// 能被校验
    public void loginDTO(@Valid LoginDTO dto) {
    }

    /// 不能被校验
    public void loginDTO2(LoginDTO dto) {
    }

    /// 不能被校验
    public void loginDTO3(@Validated LoginDTO dto) {
    }

    public void loginDTO4(@Validated @Valid LoginDTO dto) {
    }

    /// 不能被校验
    @Validated
    public void loginDTO5(LoginDTO dto) {
    }

    /// 能被校验
    /// 不在Controller,时候必须放方法上面
    @Validated({MyGroups.Add.class, Default.class})
    public void funOne(@Valid ParamDTO one) {
    }
//    public void funOne(@Valid  @Validated({DIY.Add.class, Default.class}) ObjectDTO one){} // 不可以
//    public void funOne(@Validated({DIY.Add.class, Default.class}) ObjectDTO one){} // 不可以
//    public void funOne(ObjectDTO one){} // 不可以

    @Validated({MyGroups.Edit.class})
    public void funList(@Valid List<ParamDTO> list) {
    }

    @Validated
    public void testDate(@Valid DateDTO dto) {
    }

}
