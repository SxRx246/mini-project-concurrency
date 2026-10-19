package com.ga.service;

import com.ga.exception.InvalidFieldsException;
import com.ga.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class CSVProcessor {

    private final EmployeeRepository repository;

    public CSVProcessor(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<String> readEmployees() throws IOException {
        List<String> employees = repository.readCSV();

        for (int i = 0; i < employees.size(); i++) {
            String[] fields = employees.get(i).trim().split(",");

            if (fields.length != 6) {
                throw new InvalidFieldsException(
                        "Invalid number of fields in line " + (i + 1));
            }
        }

        return employees;
    }

    }