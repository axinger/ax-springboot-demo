package com.github.axinger.dto;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

//@Converter(autoApply = false) // 设置为 false，仅在需要时手动应用
@Converter(autoApply = true) // 设置为 false，仅在需要时手动应用
public class JpaGenderConverter implements AttributeConverter<Gender, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Gender attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return Gender.fromCode(dbData);
    }
}
