package com.github.axinger.model.enums;


import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GenderConverter implements Converter<String, Gender> {
    @Override
    public @Nullable Gender convert(@Nullable String source) {
        return Gender.convert(source);
    }
}
