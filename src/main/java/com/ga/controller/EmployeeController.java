package com.ga.controller;

import com.ga.model.Employee;
import com.ga.service.EmployeeService;
import com.ga.service.SalaryService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Provides REST API endpoints for retrieving employee records,
 * updating individual or all employee salaries, and monitoring
 * completed salary-processing tasks.
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final SalaryService salaryService;

    /**
     * Creates the controller with the services required to
     * manage employee records and calculate salary increases.
     *
     * @param employeeService the service for retrieving and saving employees
     * @param salaryService the service for calculating salary increases
     */
    public EmployeeController(EmployeeService employeeService, SalaryService salaryService) {
        this.employeeService = employeeService;
        this.salaryService = salaryService;
    }

    /**
     * Retrieves all employee records.
     *
     * @return a list of all employees
     */
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    /**
     * Retrieves an employee by their unique ID.
     *
     * @param id the ID of the employee to retrieve
     * @return the employee matching the specified ID
     */
    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return employeeService.getEmployee(id);
    }

    /**
     * Calculates and saves the salary increase for a single employee.
     *
     * @param id the ID of the employee whose salary will be updated
     * @return the employee with the updated salary information
     * @throws IOException if an error occurs while saving employee data
     */
    @PutMapping("/{id}/salary")
    public Employee updateSalary(@PathVariable int id) throws IOException {
        Employee employee = employeeService.getEmployee(id);

        salaryService.updateSalary(employee);
        employeeService.saveEmployees();

        return employee;
    }

    /**
     * Calculates salary increases for all employees and saves
     * the updated employee records to the output CSV file.
     *
     * @return the list of employees with updated salary information
     * @throws IOException if an error occurs while saving employee data
     * @throws InterruptedException if the current thread is interrupted
     * @throws ExecutionException if a salary-processing task fails
     */
    @PutMapping("/salaries")
    public List<Employee> updateAllSalaries()
            throws IOException, InterruptedException, ExecutionException {

        List<Employee> employees = employeeService.getAllEmployees();

        salaryService.updateAllSalaries(employees);
        employeeService.saveEmployees();

        return employees;
    }

    /**
     * Retrieves the number of completed salary-processing tasks.
     *
     * @return the number of completed tasks
     */
    @GetMapping("/salary-processing/completed")
    public int getProcessingCount() {
        return salaryService.getCompletedTasks();
    }
}