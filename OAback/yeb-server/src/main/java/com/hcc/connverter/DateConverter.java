package com.hcc.connverter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 日期转换
 */
@Component
public class DateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String s) {

        return LocalDate.parse(s,DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    }
}
