package com.example.TestingAppApplication.Services.impl;


import com.example.TestingAppApplication.config.TestContainerConfiguration;
import com.example.TestingAppApplication.dto.EmployeeDto;
import com.example.TestingAppApplication.entities.Employee;
import com.example.TestingAppApplication.exceptions.EmployeeNotFoundException;
import com.example.TestingAppApplication.exceptions.ResourceNotFoundException;
import com.example.TestingAppApplication.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Import({TestContainerConfiguration.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
//@DataJpaTest
    @ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

//    @Autowired
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee MockEmployee;
    private EmployeeDto MockEmployeeDto;

    @BeforeEach

    void setUp() {
         MockEmployee=Employee.builder()
                .id(1L)
                .name("MockEmployee")
                .email("anuj@gmail.com")
                .salary(200L)
                .build();

        MockEmployeeDto=modelMapper.map(MockEmployee, EmployeeDto.class);
    }

    @Test
    void TestGetEmployeeById_WhenEmployeeIdIsPresent_ThenReturnEmployeeDto() {
//        employeeService.getEmployeeById(1L);
//        assign
        Long id=MockEmployee.getId();
      when(employeeRepository.findById(id)).thenReturn(Optional.of((MockEmployee)));    //stubbing

//        act
        EmployeeDto employeeDto=employeeService.getEmployeeById(id);

//        assert

        assertThat(employeeDto.getId()).isEqualTo(id);
    assertThat(employeeDto.getName()).isEqualTo("MockEmployee");
        assertThat(employeeDto.getEmail()).isEqualTo(MockEmployee.getEmail());
        verify(employeeRepository).findById(id);
    }

    @Test
    void TestGetEmployeeById_WhenEmployeeIdIsNotPresent_ThenThrowEmployeeNotFoundException() {

//        arrange
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

//        act and assert

        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Employee not found with id: 1");

                verify(employeeRepository).findById(1L);

    }



    @Test
    void testCreateNewEmployee_WhenValidEmployee_ThenCreateNewEmployee(){

//        assign
        when(employeeRepository.findByEmail(anyString())).thenReturn(List.of());
        when(employeeRepository.save(any(Employee.class))).thenReturn(MockEmployee);

//        act
        EmployeeDto employeeDto=employeeService.createNewEmployee(MockEmployeeDto);

//        assert
        assertThat(employeeDto).isNotNull();
//        assertThat(employeeDto.getName()).isEqualTo(MockEmployeeDto.getName());
        assertThat(employeeDto.getEmail()).isEqualTo(MockEmployeeDto.getEmail());
        ArgumentCaptor<Employee> employeeArgumentCaptor=ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeArgumentCaptor.capture());
        Employee captureEmployee=employeeArgumentCaptor.getValue();
        assertThat(captureEmployee.getEmail()).isEqualTo(MockEmployee.getEmail());
    }



    @Test
    void testCreateNewEmployee_WhenAttemptingToCreateEmployeeWithExistingEmail_ThenThrowEmployeeNotFoundException() {

//        arrange
        when(employeeRepository.findByEmail(MockEmployeeDto.getEmail())).thenReturn(List.of(MockEmployee));

//        act and assert
        assertThatThrownBy(() -> employeeService.createNewEmployee(MockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Employee already exists with email: " + MockEmployee.getEmail());
        verify(employeeRepository).findByEmail(MockEmployeeDto.getEmail());
        verify(employeeRepository, never()).save(any());


    }



    @Test
    void testUpdateEmployee_WhenEmployeeDoesNotExist_ThenThrowEmployeeNotFoundException() {

//        assign
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

//        act and assert

        assertThatThrownBy(() -> employeeService.updateEmployee(1L,MockEmployeeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: 1");
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).save(any());
    }



    @Test
    void testUpdateEmployee_WhenAttemptingToUpdateEmail_ThenThrowException(){

//        assign

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(MockEmployee));
        MockEmployeeDto.setName("random");
        MockEmployeeDto.setEmail("random@gmial.com");

//        act and assert

        assertThatThrownBy(() -> employeeService.updateEmployee(MockEmployeeDto.getId(),MockEmployeeDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("The email of the employee cannot be updated");
        verify(employeeRepository).findById(MockEmployeeDto.getId());
        verify(employeeRepository, never()).save(any());

    }


    @Test
    void testUpdateEmployee_WhenValidEmployee_ThenUpdateEmployee(){
        when(employeeRepository.findById(MockEmployeeDto.getId())).thenReturn(Optional.of(MockEmployee));
        MockEmployeeDto.setName("random Name");
        MockEmployeeDto.setSalary(199L);

        Employee newEmployee=modelMapper.map(MockEmployee, Employee.class);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);

//        act

        EmployeeDto UpdateEmployeeDto=employeeService.updateEmployee(MockEmployeeDto.getId(),MockEmployeeDto);

        assertThat(UpdateEmployeeDto.getName()).isEqualTo(MockEmployeeDto.getName());

        verify(employeeRepository).findById(1l);
        verify(employeeRepository).save(any());

    }




    @Test
    void testDeleteEmployee_WhenEmployeeDoesNotExist_ThenThrowEmployeeNotFoundException() {

       when(employeeRepository.existsById(1L)).thenReturn(false);

//       act

        assertThatThrownBy(() -> employeeService.deleteEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with id: " + 1L );

        verify(employeeRepository, never()).deleteById(anyLong());
    }


    @Test
    void testDeleteEmployee_WhenEmployeeIsValid_ThenDeleteEmployee(){

        when(employeeRepository.existsById(1L)).thenReturn(true);

        assertThatCode(() -> employeeService.deleteEmployeeById(1L))
                .doesNotThrowAnyException();
        verify(employeeRepository).deleteById(1L);
    }


    @Test
    void testGetAllEmployees_WhenNoEmployeesExist_ThenReturnEmptyList() {

        // Arrange
        when(employeeRepository.findAll())
                .thenReturn(Collections.emptyList());

        // Act
        List<EmployeeDto> employeeDtos = employeeService.getAllEmployees();

        // Assert
        assertThat(employeeDtos).isNotNull();
        assertThat(employeeDtos).isEmpty();

        // Verify
        verify(employeeRepository).findAll();

    }


}