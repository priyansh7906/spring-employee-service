package com.example.TestingAppApplication.controllers;

import com.example.TestingAppApplication.config.TestContainerConfiguration;
import com.example.TestingAppApplication.dto.EmployeeDto;
import com.example.TestingAppApplication.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestContainerConfiguration.class)
@AutoConfigureWebTestClient(timeout = "100000")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationClass {

    @Autowired
     WebTestClient webTestClient;

   Employee testemployee = Employee.builder()
            .name("Priyansh")
                .email("priyansh@gmail.com")
                .salary(100L)
                .build();

   EmployeeDto testemployeeDto = EmployeeDto.builder()
            .id(1L)
                .name("Priyansh")
                .email("priyansh@gmail.com")
                .salary(100L)
                .build();
}
