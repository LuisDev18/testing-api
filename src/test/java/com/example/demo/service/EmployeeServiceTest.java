package com.example.demo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.impl.EmployeeServiceImpl;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	
  @Mock
  private EmployeeRepository employeeRepository;
  
  @InjectMocks
  private EmployeeServiceImpl employeeService;
  
  private Employee employee;
  
  @BeforeEach
  public void setup() {
	  
	  //employeeRepository=Mockito.mock(EmployeeRepository.class);
	   //employeeService=new EmployeeServiceImpl(employeeRepository);
	  employee=Employee.builder()
			  .id(1L)
			  .firstName("Luis")
			  .lastName("Martinez")
			  .email("luis@gmail.com")
			  .build();
  }
  
  @DisplayName("Junit test for savedEmployee method")
  @Test
  public void givenExistingEmail_whenSaveEmployee_thenThrowException() {
	  //given 
	  BDDMockito.given(employeeRepository.findByEmail(employee.getEmail()))
	     .willReturn(Optional.of(employee));
	  System.out.println(employee.getFirstName());
	    
	  //when
	 org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
		 employeeService.saveEmployee(employee); 
	 });
	 
	 //then
	 Mockito.verify(employeeRepository,Mockito.never()).save(any(Employee.class));
  }
  
  @DisplayName("getAllEmployeeTest")
  @Test
  public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList() {  
	  //given
	 Employee employee1=Employee.builder()
			  .id(2L)
			  .firstName("Dubai")
			  .lastName("Fernandez")
			  .email("dubidubi@gmail.com")
			  .build();
	 
	  BDDMockito.given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));
	  
	  //when  
	  List<Employee> employeeList= employeeService.getAllEmployees();
	  
	  //then-verify the output
	  
	  Assertions.assertThat(employeeList).isNotNull();
	  Assertions.assertThat(employeeList.size()).isEqualTo(2);
  }

  @DisplayName("getAllEmployeeNOOKTest")
  @Test
  public void givenEmployeesListEmpty_whenGetAllEmployees_thenReturnEmployeeList() {  
	  //given
	  //Aca seteamos el reultado cuando llamamos al metodo findAll()
	  BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());
	  
	  //when  
	  List<Employee> employeeList= employeeService.getAllEmployees();
	  
	  //then-verify the output
	  
	  Assertions.assertThat(employeeList).isEmpty();
	  Assertions.assertThat(employeeList.size()).isEqualTo(0);
  }
  
  @DisplayName("getEmployeeByIdTest")
  @Test
  public void givenEmployeeById_whenGetEmployeeById_thenReturnEmployeeObject() {
	  //given
	  BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));
	  
	  //when
	 Employee savedEmployee= employeeService.getEmployeeById(employee.getId()).get();
	  
	  //then
	 Assertions.assertThat(savedEmployee).isNotNull();
  }
  
  @Test
  public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdateEmployee() {
	  //given
	  BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
	  
      employee.setEmail("ale_218@gmail.com");
      employee.setFirstName("Kilua");
	  
	  //when
	  Employee updateEmployee= employeeService.updateEmployee(employee);
	  //then
	  
	  Assertions.assertThat(updateEmployee.getEmail()).isEqualTo("ale_218@gmail.com");
	  Assertions.assertThat(updateEmployee.getFirstName()).isEqualTo("Kilua");
  }
  
  @Test
  public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
	  //given
	  long employeeId=1L;
	 //Para setear un metodo de tipo void, usamos el metodo willDoNothing()
	  //Luego llamamos a nuestro objeto mock employeeRepository
	  BDDMockito.willDoNothing().given(employeeRepository).deleteById(employeeId);
      
	  //when
       employeeService.deleteEmployee(employeeId);
	  
	  //then:: Verificamos el numero de llamadas al metodo deleteById
       verify(employeeRepository, Mockito.times(1)).deleteById(employeeId);
	  

  }
}
