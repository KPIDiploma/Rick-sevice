package com.volontyr.service;

import com.volontyr.model.Gender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by volontyr on 03.05.17.
 */
public final class GenderService {

    public static Map<String, String> getGenders() {
        Map<String, String> genders = new HashMap<>();

        for (Gender g : Gender.values()) {
            genders.put(String.valueOf(g), String.valueOf(g));
        }

        return genders;
    }
}
