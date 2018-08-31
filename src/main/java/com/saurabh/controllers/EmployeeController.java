package com.saurabh.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saurabh.entities.Employee;
import com.saurabh.services.EmployeeService;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	/*@GetMapping()
	public List<Employee> getEmployees() {
		return employeeService.getEmployees();
	}*/
	
	@GetMapping("/{id}")
	public Employee getEmployee(@PathVariable("id") Integer id) {
		System.out.println(id + " :: id as found in controller");
		return employeeService.getEmployee(id);
	}
	
	@GetMapping()
	public void saveEmployee() {
		employeeService.saveEmployee();
	}
}
