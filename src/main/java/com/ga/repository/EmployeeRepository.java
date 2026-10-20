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

/**
 * Handles reading employee records from the input CSV file
 * and writing processed employee data to the output CSV file.
 */
@Repository
public class EmployeeRepository {

    /**
     * Reads all lines from the employee input CSV file using UTF-8
     * encoding and returns them as a list of strings.
     *
     * @return a list containing the lines read from the CSV file
     * @throws IOException if the file cannot be found or an error
     *                     occurs while reading it
     */
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

    /**
     * Writes employee records and their salary calculations to the
     * output CSV file using UTF-8 encoding. The output includes
     * employee details, years worked, increase percentage, and new salary.
     *
     * @param employees the list of employees to write to the CSV file
     * @throws IOException if an error occurs while writing the file
     */
    public void writeCSV(List<Employee> employees) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("src/main/resources/output_employees.csv"), StandardCharsets.UTF_8)) {

            writer.write(
                    "id,name,salary,joined_date,role,"
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
