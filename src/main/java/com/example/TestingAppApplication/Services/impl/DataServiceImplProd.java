package com.example.TestingAppApplication.Services.impl;

import com.example.TestingAppApplication.Services.DataService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


@Service
@Profile("prod")
public class DataServiceImplProd implements DataService {

    @Override
    public String getData() {
        return "DataServiceImplProd";
    }

}

