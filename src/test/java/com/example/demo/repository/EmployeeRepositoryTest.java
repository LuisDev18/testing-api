package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.model.Employee;


@DataJpaTest
public class EmployeeRepositoryTest {
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private Employee employee;
	@BeforeEach
	public void setup() {
		 employee = Employee.builder()
				.firstName("Luis")
				.lastName("Del Carpio")
				.email("luisdcm_18@hotmail.com")
				.build();
	}
/*	@DisplayName("Junit test for save employee operation")
	@Test
	public void givenEmployeeObject_whenSaven_thenReturnSavedEmployee() {
		
		//given - precondition or setup
//		Employee employee = Employee.builder()
//				.firstName("Luis")
//				.lastName("Del Carpio")
//				.email("luisdcm_18@hotmail.com")
//				.build();		
		//when-action
		Employee savedEmployee=employeeRepository.save(employee);
		
		//then-verify the output
		
		Assertions.assertThat(savedEmployee).isNotNull();
		//Ahora probaremos si la clave primaria guardada en la base
		//datos es mayor a 0 o no.
		Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
		
	}
	
	@DisplayName("JUnit test for get all employees operation")
	@Test
	public void givenEmployeeList_whenFindAll_thenEmployeesList() {
		//given - precondition or setup
//		Employee employee=Employee.builder()
//				.firstName("Lesly")
//				.lastName("Aguilar")
//				.email("les18@hotmail.com")
//				.build();
		
		Employee employee1=Employee.builder()
				.firstName("Eva")
				.lastName("Hasintol")
				.email("eva_90@hotmail.com")
				.build();
		
		employeeRepository.save(employee);
		employeeRepository.save(employee1);
		//when - action
		
		List<Employee>employeeList=employeeRepository.findAll();
		
		//then - verify out result
		Assertions.assertThat(employeeList).isNotNull();
		Assertions.assertThat(employeeList.size()).isEqualTo(2);	
		
	}
	
	@DisplayName("Junit test for get employee by ID")
	@Test
	public void givenEmployeeObject_whenFindEmplooye_thenEmployeeObject() {
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubai_17@hotmail.com")
//				 .build();
		 
		 employeeRepository.save(employee);
		//when
		Employee employeeDB= employeeRepository.findById(employee.getId()).get();
		//then
		Assertions.assertThat(employeeDB).isNotNull();
		
	}
	
	@DisplayName("Junit test for get employee by email")
	@Test
	public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubai_17@hotmail.com")
//				 .build();
//		 
		 employeeRepository.save(employee);
		//when
		Employee employeeDB= employeeRepository.findByEmail(employee.getEmail()).get();
		
		//then
		Assertions.assertThat(employeeDB).isNotNull();
		
	}
	
	@DisplayName("Junit test for update employee operation")
	@Test
	public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdateEmployee(){
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubi_dubi18@gmail.com")
//				 .build();
		 
		 employeeRepository.save(employee);
		//when
		Employee savedEmployee= employeeRepository.findById(employee.getId()).get();
		savedEmployee.setEmail("dubi_dubi178@gmail.com");
		savedEmployee.setFirstName("Wilian");
		Employee updateEmployee = employeeRepository.save(savedEmployee);
		//then
		Assertions.assertThat(updateEmployee.getEmail()).isEqualTo("dubi_dubi178@gmail.com");
		Assertions.assertThat(updateEmployee.getFirstName()).isEqualTo("Wilian");
		
	}
	
	@DisplayName("Junit test for delete employee operation")
	@Test
	public void givenEmployeeObject_whenDeleteEmployee_thenRemoveEmployee() {
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubi_dubi18@gmail.com")
//				 .build();
		 
		 employeeRepository.save(employee);
		//when
		employeeRepository.deleteById(employee.getId());
		Optional<Employee>employeeOptional=employeeRepository.findById(employee.getId());
		
		//then
		 Assertions.assertThat(employeeOptional).isEmpty();
	}
	
	@DisplayName("Junit test using JPQL with Index params")
	@Test
	public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubi_dubi18@gmail.com")
//				 .build();
		 employeeRepository.save(employee);
		 
			String firstName="Luis";
			String lastName="Del Carpio";
		//when
		Employee saveEmployee=employeeRepository.findByJPQL(firstName, lastName);
		//then
		Assertions.assertThat(saveEmployee).isNotNull();
	}
	
	@DisplayName("Junit test using JPQL with Named params")
	@Test
	public void givenFirstNameAndLastName_whenFindByJpqlNameParams_thenReturnEmployeeObject() {
		//given
//		 Employee employee= Employee.builder()
//				 .firstName("Dubais")
//				 .lastName("Velez")
//				 .email("dubi_dubi18@gmail.com")
//				 .build();
		 employeeRepository.save(employee);
		 
			String firstName="Luis";
			String lastName="Del Carpio";
		//when
		Employee saveEmployee=employeeRepository.findByJPQLNameParams(firstName, lastName);
		//then
		Assertions.assertThat(saveEmployee).isNotNull();
	}
	
	@DisplayName("Junit test query custom using SQL native with index")
	@Test
	public void givenFirstNameAndLastName_whenFindBySqlNative_thenReturnEmployeeObject() {
		//given
//		Employee employee=Employee.builder()
//				.firstName("Lisa")
//				.lastName("Simpson")
//				.email("lisa_oih")
//				.build();
		
		employeeRepository.save(employee);
		//when
		
		Employee savedEmployee= employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());
		//then
		Assertions.assertThat(savedEmployee).isNotNull();
	}
	
	@DisplayName("Junit test query custom using SQL native with named")
	@Test
	public void givenFirstNameAndLastName_whenFindBySqlNativeNamedParams_thenReturnEmployeeObject() {
		//given
//		Employee employee=Employee.builder()
//				.firstName("Lisa")
//				.lastName("Simpson")
//				.email("lisa_oih")
//				.build();
		
		employeeRepository.save(employee);
		//when
		
		Employee savedEmployee= employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());
		//then
		Assertions.assertThat(savedEmployee).isNotNull();
	}
*/
}
