package com.github.axinger.model.enums;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class GenderConverter implements Converter<String, Gender> {
    @Override
    public @Nullable Gender convert(@Nullable String source) {
        return Gender.convert(source);
    }
}
