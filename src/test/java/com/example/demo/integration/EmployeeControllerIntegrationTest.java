package com.example.demo.integration;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	//Para serializar y descerializar
	private ObjectMapper objectMapper;
	
	@BeforeEach
	//metodo para limpiar los registros de la bd para cada unitTest
	void setup() {
		employeeRepository.deleteAll();
	}

	//Integration Test for createEmployee REST API
	@Test
	public void createEmployeeTest() throws Exception {
		// given
		Employee employee = Employee.builder().firstName("Rygelon")
				.lastName("Pavita")
				.email("anduin@gmail.com")
				.build();
		// when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(employee)));
		// then
		response.andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
	}
	
	//Integration Test for getAllEmployees
	@Test
	public void getAllEmployeesIntTest() throws Exception {
		
		//given-preparar una lista de empleados
		List<Employee>listEmployee=new ArrayList<>();
		listEmployee.add(Employee.builder().firstName("Rygelon").lastName("Sepulcher").email("rygelon12@gmail.com").build());
		listEmployee.add(Employee.builder().firstName("Anduin").lastName("SepulcherFirst").email("anduino2@gmail.com").build());
		
		employeeRepository.saveAll(listEmployee);
		//when
		ResultActions response=mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));
		
		//when
		response.andExpect(MockMvcResultMatchers.status().isOk())
		          .andDo(MockMvcResultHandlers.print())
		          .andExpect(MockMvcResultMatchers.jsonPath("$.size()",
		        		  CoreMatchers.is(listEmployee.size())));		
	}
	
	//Integration Test for getEmployeeById()
	@Test
	public void getEmployeeByIdTest() throws Exception {

		// given-Creamos un objeto Employee y lo grabamos en la bd
		Employee employee = Employee.builder().firstName("Willi")
				.lastName("Picapiedra").email("willi322@gmail.com")
				.build();
		employeeRepository.save(employee);
		// when - obtenemos el registro guardado consumiendo el api
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));

		// then-verificamos el response del API::Status-el response-objetoJson
		response.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
	}
	//Integration Test for getEmployeeById()-Scenario (-)
	@Test
	public void getEmployeeByIdNOOK() throws Exception {
		// given-Asignamos un valor al id del employee a buscar
		long employeeId = 1L;
		// given-Creamos un objeto Employee
		Employee employee = Employee.builder().firstName("Willi")
				.lastName("Picapiedra").email("willi322@gmail.com")
				.build();
		employeeRepository.save(employee);
		// when
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

		// then-verificamos el response del API::Status-el response-objetoJson
		response.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andDo(MockMvcResultHandlers.print());

	}
	
	//Integration Test for updateEmployeeByIdOK()
	@Test
	public void updateEmployeeByIdOK() throws Exception {
		// given	- Graba un objeto employee en la base de datos.
		Employee saveEmployee = Employee.builder()
				.firstName("Thrall")
				.lastName("horda")
				.email("thral_14@gmail.com")
				.build();

		employeeRepository.save(saveEmployee);

		// Objeto employee actualizado
		Employee updateEmployee = Employee.builder().
				 firstName("Willie Smith")
				.lastName("Paredes")
				.email("WillSmith232@gmail.com").build();


		// when-Simluando una peticion PutMapping
		ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", saveEmployee.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateEmployee)));	

		// then-verificamos el response del API
		response.andExpect(MockMvcResultMatchers.status().isOk())
		          .andDo(MockMvcResultHandlers.print())
		          .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",CoreMatchers.is(updateEmployee.getFirstName())))
		          .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updateEmployee.getLastName())))
		          .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updateEmployee.getEmail())));

	}
	
	//Integration Test for updateEmployeeByIdNOOK
	@Test
	public void updateEmployeeByIdNOOK() throws Exception {
		// given
				// Objeto employee para un Id = 1L guardado en una base de datos
				long employeeId = 1L;
				
				Employee savedEmployee = Employee.builder().
						firstName("PinWillie")
						.lastName("DobleMitra")
						.email("willie322@gmail.com").build();
				employeeRepository.save(savedEmployee);

				// Objeto employee actualizado
				Employee updateEmployee = Employee.builder()
						.firstName(" Smith")
						.lastName("Paredes")
						.email("WillSmith232@gmail.com").build();				

				// when-Simluando una peticion PutMapping
				ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateEmployee)));	

				// then
				response.andExpect(MockMvcResultMatchers.status().isNotFound())
				          .andDo(MockMvcResultHandlers.print());
				         		
	}
	
	//IntegrationTest for deleteEmployeeById
	@Test
	public void deleteEmployeeByIdOK() throws Exception {
		//given	
		Employee savedEmployee = Employee.builder().
				firstName("PinWillie")
				.lastName("DobleMitra")
				.email("willie322@gmail.com").build();
		employeeRepository.save(savedEmployee);

		//when
		ResultActions response= mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}",savedEmployee.getId()));
		//then
		response.andExpect(MockMvcResultMatchers.status().isOk())
		        .andDo(MockMvcResultHandlers.print());
	}
}
