package com.volontyr.converter;

import com.volontyr.model.Gender;
import org.springframework.core.convert.converter.Converter;

/**
 * Created by volontyr on 04.05.17.
 */
public class StringToGender implements Converter<String, Gender> {

    @Override
    public Gender convert(String gender) {
        return Gender.valueOf(gender);
    }
}
