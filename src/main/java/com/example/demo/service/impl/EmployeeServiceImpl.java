package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	EmployeeRepository employeeRepository;
	
	//Siempre que tengamos un constructor como injeccion de dependencia
	//no hay necesidad de agregarle el @Autowired al atributo
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		super();
		this.employeeRepository = employeeRepository;
	}


	@Override
	public Employee saveEmployee(Employee employee) {
		
	Optional<Employee> savedEmployee=employeeRepository.findByEmail(employee.getEmail());
	
	if(savedEmployee.isPresent()) {
		throw new ResourceNotFoundException("Employee already exist with given email:"+employee.getEmail());
	}
		return employeeRepository.save(employee);
	}


	@Override
	public List<Employee> getAllEmployees() {
		
		return employeeRepository.findAll();
	}


	@Override
	public Optional<Employee> getEmployeeById(Long id) {	
		return employeeRepository.findById(id);
	}


	@Override
	public Employee updateEmployee(Employee updateEmployee) {
		
		return employeeRepository.save(updateEmployee);
	}


	@Override
	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
		
	}
	

}
