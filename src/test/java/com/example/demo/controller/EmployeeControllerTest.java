package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
 class EmployeeControllerTest {

	// Inyectamos un objeto MockMvc para q llame al API Rest
	@Autowired
	private MockMvc mockMvc;

	/*
	 * La anotación @MockBean crea un mock de EmployeeService y lo agrega al
	 * aplicación context entonces este es injectado en el EmployeeController.
	 */
	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void createEmployeeTest() throws Exception {
		// given
		Employee employee = Employee.builder().firstName("Anduin").lastName("Casimiro").email("anduin@gmail.com")
				.build();

		BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
				.willAnswer((invocation) -> invocation.getArgument(0));

		// when: call method
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));
		// then : verify the utput
		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
	}

	@Test
	void getAllEmployeesTest() throws Exception {
		// given - preparamos un lista de empleados
		List<Employee> listEmployee = new ArrayList<>();
		listEmployee
				.add(Employee.builder().firstName("Duais").lastName("fullDuais").email("pavita322@gmail.com").build());
		listEmployee
				.add(Employee.builder().firstName("Wills").lastName("fullWills").email("will322@gmail.com").build());
		listEmployee.add(
				Employee.builder().firstName("Howartz").lastName("fullHowarz").email("howartz322@gmail.com").build());

		BDDMockito.given(employeeService.getAllEmployees()).willReturn(listEmployee);

		// when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

		// then
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listEmployee.size())));
	}

	@Test
	// Junit test for GET employeeById positive scenario
	void getEmployeeByIdTest() throws Exception {

		// given-Creamos un objeto Employee
		long employeeId = 1L;
		Employee employee = Employee.builder().firstName("Willi").lastName("Picapiedra").email("willi322@gmail.com")
				.build();

		BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

		// when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

		// then-verificamos el response del API::Status-el response-objetoJson
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
	}

	@Test
	void getEmployeeByIdNOOK() throws Exception {
		// given-Creamos un objeto Employee
		long employeeId = 1L;

		BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

		// when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

		// then-verificamos el response del API::Status-el response-objetoJson
		response.andExpect(MockMvcResultMatchers.status().isNotFound()).andDo(MockMvcResultHandlers.print());

	}

	@Test
	void updateEmployeeByIdOK() throws Exception {
		// given
		// Objeto employee para un Id = 1L guardado en una base de datos
		long employeeId = 1L;

		Employee savedEmployee = Employee.builder().firstName("Willie")
				.lastName("DobleMitra")
				.email("willie322@gmail.com").build();

		// Objeto employee actualizado
		Employee updateEmployee = Employee.builder().firstName("Willie Smith")
				.lastName("Paredes")
				.email("WillSmith232@gmail.com").build();

		BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));

		BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
				.willAnswer((invocation) -> invocation.getArgument(0));

		// when-Simluando una peticion PutMapping
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateEmployee)));	

		// then
		response.andExpect(MockMvcResultMatchers.status().isOk())
		          .andDo(MockMvcResultHandlers.print())
		          .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(updateEmployee.getFirstName())))
		          .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updateEmployee.getLastName())))
		          .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updateEmployee.getEmail())));

	}
	
	@Test
	void updateEmployeeByIdNOOK() throws Exception {
		// given
				// Objeto employee para un Id = 1L guardado en una base de datos
				long employeeId = 1L;

				// Objeto employee actualizado
				Employee updateEmployee = Employee.builder().firstName("Willie Smith")
						.lastName("Paredes")
						.email("WillSmith232@gmail.com").build();

				BDDMockito.given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

				BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
						.willAnswer((invocation) -> invocation.getArgument(0));

				// when-Simluando una peticion PutMapping
				ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateEmployee)));	

				// then
				response.andExpect(MockMvcResultMatchers.status().isNotFound())
				          .andDo(MockMvcResultHandlers.print());
				         		
	}
	
	@Test
	void deleteEmployeeByIdOK() throws Exception {
		//given
		long employeeId = 1L;

	    BDDMockito.willDoNothing().given(employeeService).deleteEmployee(employeeId);
		//when
		ResultActions response= mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",employeeId));
		//then
		response.andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcResultHandlers.print());
	}

}
