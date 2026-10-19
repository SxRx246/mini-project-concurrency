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

@Service
public class EmployeeService {

    private final EmployeeRepository repository;
    private final ConcurrentHashMap<Integer, Employee> employees = new ConcurrentHashMap<>();

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void loadEmployees() throws IOException {
        List<String> lines = repository.readCSV();


        for (int i = 1; i < lines.size(); i++) {
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

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees.values());
    }

    public Employee getEmployee(int id) {
        Employee employee = employees.get(id);

        if (employee == null) {
            throw new AttributeNotFoundException("Employee not found.");
        }

        return employee;
    }


}