package com.ga.repository;

import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EmployeeRepository {
    public List<String> readCSV() throws IOException {
        InputStream stream =
                getClass().getResourceAsStream("/test_employees.csv");

        if (stream == null) {
            throw new FileNotFoundException("CSV file not found.");
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }
}
