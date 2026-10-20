package com.ga.repository;

import com.ga.model.Employee;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Repository
public class EmployeeRepository {
    public List<String> readCSV() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/test_employees.csv");

        if (stream == null) {
            throw new FileNotFoundException("CSV file not found.");
        }

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    public void writeCSV(List<Employee> employees) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("src/main/resources/output_employees.csv"), StandardCharsets.UTF_8)) {

            writer.write(
                    "id,name,original_salary,joined_date,role,"
                            + "project_completion,years_worked,"
                            + "increase_percentage,new_salary"
            );
            writer.newLine();

            for (Employee employee : employees) {
                writer.write(String.format(
                        Locale.ROOT,
                        "%d,%s,%.2f,%s,%s,%.2f,%d,%.2f,%.2f",
                        employee.getId(),
                        employee.getName(),
                        employee.getSalary(),
                        employee.getJoinedDate(),
                        employee.getRole(),
                        employee.getProjectCompletion() * 100,
                        employee.getYearsWorked(),
                        employee.getIncreasePercentage(),
                        employee.getNewSalary()
                ));

                writer.newLine();
            }
        }
    }

}
