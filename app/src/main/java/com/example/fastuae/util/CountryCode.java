package com.example.fastuae.util;

import com.example.fastuae.model.CountryCodeModel;

import java.util.ArrayList;
import java.util.List;

public class CountryCode {

    public static List<CountryCodeModel> getList(){
        List<CountryCodeModel> list = new ArrayList<>();

        list.add(new CountryCodeModel("91",""));
        list.add(new CountryCodeModel("92",""));
        list.add(new CountryCodeModel("93",""));

        return list;
    }
}
