package com.ga.controller;

import com.ga.model.Employee;
import com.ga.service.EmployeeService;
import com.ga.service.SalaryService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SalaryService salaryService;

    public EmployeeController(EmployeeService employeeService, SalaryService salaryService) {
        this.employeeService = employeeService;
        this.salaryService = salaryService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return employeeService.getEmployee(id);
    }

    @PutMapping("/{id}/salary")
    public Employee updateSalary(@PathVariable int id) throws IOException {
        Employee employee = employeeService.getEmployee(id);

        salaryService.updateSalary(employee);
        employeeService.saveEmployees();

        return employee;
    }

    @PutMapping("/salaries")
    public List<Employee> updateAllSalaries()
            throws IOException, InterruptedException, ExecutionException {

        List<Employee> employees = employeeService.getAllEmployees();

        salaryService.updateAllSalaries(employees);
        employeeService.saveEmployees();

        return employees;
    }

    @GetMapping("/salary-processing/completed")
    public int getProcessingCount() {
        return salaryService.getCompletedTasks();
    }
}