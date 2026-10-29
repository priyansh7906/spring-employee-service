package com.example.TestingAppApplication.controllers;

import com.example.TestingAppApplication.config.TestContainerConfiguration;
import com.example.TestingAppApplication.dto.EmployeeDto;
import com.example.TestingAppApplication.entities.Employee;
import com.example.TestingAppApplication.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class EmployeeControllerTestIT extends AbstractIntegrationClass {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {

        employeeRepository.deleteAll();
    }


    @Test
    void testGetEmployeeById_Success() {
      Employee savedEmployee =  employeeRepository.save(testemployee);

      webTestClient.get()
              .uri("/employees/{id}", savedEmployee.getId())
              .exchange()
              .expectStatus().isOk()
              .expectBody(EmployeeDto.class)
              .isEqualTo(testemployeeDto);
//              .value(employeeDto -> {
//                  assertThat(employeeDto.getId()).isEqualTo(savedEmployee.getId());
//                  assertThat(employeeDto.getEmail()).isEqualTo(savedEmployee.getEmail());
//
//              });
    }
    
    @Test
    void TestGetEmployeeById_Failure() {

        webTestClient.get()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void TestCreateNewEmployee_WhenEmployeeExists_Success() {
        Employee savedEmployee = employeeRepository.save(testemployee);
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }


    @Test
    void testCreateNewEmployee_WhenEmployeeDoesNotExist_ThenCreateEmployee() {

        testemployeeDto.setId(null);
        testemployeeDto.setEmail(
                "test-" + java.util.UUID.randomUUID() + "@example.com"
        );
        webTestClient.post()
                .uri("/employees")
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.email")
                .isEqualTo(testemployeeDto.getEmail())
                .jsonPath("$.name")
                .isEqualTo(testemployeeDto.getName());
    }

    @Test
    void TestUpdateEmployee_WhenEmployeeDoesNotExists_thenThrowException() {
       webTestClient.put()
               .uri("/employees/999")
               .bodyValue(testemployeeDto)
               .exchange()
               .expectStatus().isNotFound();
    }

    @Test
    void testUpdateEmployee_WhenAttemptingToEmail_EmployeeExists_ThenThrowException() {
        Employee savedEmployee = employeeRepository.save(testemployee);
            testemployeeDto.setName("Priyanshi");
            testemployeeDto.setEmail("random@gmail.com");

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().is5xxServerError();
    }


    @Test
    void testUpdateEmployee_WhenEmployeeIsValid_ThenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testemployee);

        testemployeeDto.setId(savedEmployee.getId());
        testemployeeDto.setName("Priyanshi");
        testemployeeDto.setSalary(250L);

        webTestClient.put()
                .uri("/employees/{id}", savedEmployee.getId())
                .bodyValue(testemployeeDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeDto.class)
                .isEqualTo(testemployeeDto);
    }

    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExists_ThenThrowEmployee() {
        webTestClient.delete()
                .uri("/employees/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_WhenEmployeeExists_ThenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testemployee);
        webTestClient.delete()
                .uri("/employees/{id}", savedEmployee.getId())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody(Void.class);

        webTestClient.delete()
                .uri("/employees/1", savedEmployee.getId())
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testGetAllEmployees_WhenEmployeesExist_ThenReturnEmployeeList() {

        // Arrange: Save employees in test database
        Employee employee1 = new Employee(
                null, "priyansh@gmail.com", "Priyansh", 25000L
        );

        Employee employee2 = new Employee(
                null, "rahul@gmail.com", "Rahul", 30000L
        );

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        // Act & Assert
        webTestClient.get()
                .uri("/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeDto.class)
                .hasSize(2);
    }
    }

