package com.ga.repository;

import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CsvEmployeeRepository implements EmployeeRepository {

    @Override
    public List<String> findAll() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/test_employees.csv");

        if (stream == null) {
            throw new FileNotFoundException("Employees file not found: test_employees.csv");
        }

        List<String> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                employees.add(line);
            }
        }

        return employees;
    }
}
