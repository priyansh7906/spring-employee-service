package com.example.TestingAppApplication.repositories;

import com.example.TestingAppApplication.config.TestContainerConfiguration;
import com.example.TestingAppApplication.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@Import(TestContainerConfiguration.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    @BeforeEach
    void setup() {
        employee = Employee.builder()
                .name("Priyansh")
                .email("priyansh@gmail.com")
                .salary(100L)
                .build();
    }

    @Test
    void testFindByEmail_whenEmailIsValid_ThenReturnEmployee() {

        // Arrange, Given
        employeeRepository.save(employee);


        // Act , when
        List<Employee> employeeList=employeeRepository.findByEmail(employee.getEmail());


        // Assert, Then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.get(0).getEmail()).isEqualTo(employee.getEmail());

    }

    @Test
    void testFindByEmail_whenEmailIsNotFound_ThenReturnEmptyEmployeeList() {

        // Arrange, Given
        String email="notPresent.123@gmail.com";

        //when
        List<Employee> employeeList=employeeRepository.findByEmail(email);

        //then

        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();

    }
}