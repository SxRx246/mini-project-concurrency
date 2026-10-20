package com.ga.service;

import com.ga.exception.AttributeNotFoundException;
import com.ga.model.Employee;
import com.ga.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages employee data by loading records from a CSV file,
 * validating employee information, and providing methods
 * to retrieve and save employee records.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final ConcurrentHashMap<Integer, Employee> employees = new ConcurrentHashMap<>();

    /**
     * Creates the employee service with the repository
     * used to read and write employee CSV data.
     *
     * @param repository the repository responsible for CSV operations
     */
    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads employee records from the input CSV file when
     * the service is initialized. Validates employee data
     * and rejects invalid records and duplicate employee IDs.
     *
     * @throws IOException if the CSV file cannot be read or
     *                     contains invalid data or duplicate IDs
     */
    @PostConstruct
    public void loadEmployees() throws IOException {
        List<String> lines = repository.readCSV();


        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).isBlank()) {
                continue;
            }

            String[] fields = lines.get(i).split(",", -1);

            if (fields.length != 6) {
                throw new IOException("Invalid CSV data on line " + (i + 1));
            }

            Employee employee = new Employee();

            try {
                employee.setId(Integer.parseInt(fields[0].trim()));
                employee.setName(fields[1].trim());

                double salary = Double.parseDouble(fields[2].trim());
                employee.setSalary(salary);

                employee.setJoinedDate(LocalDate.parse(fields[3].trim()));

                employee.setRole(fields[4].trim());

                double completion = Double.parseDouble(fields[5].trim());

                employee.setProjectCompletion(completion);

                if (employee.getId() <= 0 || employee.getName().isBlank() || salary < 0 || employee.getJoinedDate().isAfter(LocalDate.now()) || employee.getRole().isBlank() || completion < 0 || completion > 1) {
                    throw new IllegalArgumentException("Invalid employee values.");
                }

                if (employees.putIfAbsent(employee.getId(), employee) != null) {
                    throw new IOException("Duplicate employee ID: " + employee.getId());
                }

            } catch (IllegalArgumentException exception) {
                throw new IOException("Invalid employee data on line " + (i + 1), exception);
            }
        }
    }

    /**
     * Retrieves all employees currently stored in the service.
     *
     * @return a list containing all loaded employees
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    /**
     * Retrieves an employee using their unique ID.
     *
     * @param id the ID of the employee to retrieve
     * @return the employee matching the specified ID
     * @throws AttributeNotFoundException if no employee
     *                                    exists with the given ID
     */
    public Employee getEmployee(int id) {
        Employee employee = employees.get(id);

        if (employee == null) {
            throw new AttributeNotFoundException("Employee not found.");
        }

        return employee;
    }

    /**
     * Saves the currently loaded employee records to the
     * output CSV file through the repository.
     *
     * @throws IOException if an error occurs while writing
     *                     the employee records
     */
    public void saveEmployees() throws IOException {
        repository.writeCSV(getAllEmployees());
    }


}