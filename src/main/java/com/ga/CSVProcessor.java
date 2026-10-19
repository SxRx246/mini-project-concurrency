package com.ga;

import com.ga.exception.InvalidFieldsException;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class CSVProcessor {
    private final ReentrantLock lock = new ReentrantLock();

    public InputStream getEmployeesFile(String filePath) throws FileNotFoundException {
        lock.lock();
        try {
           InputStream stream = CSVProcessor.class.getResourceAsStream(filePath);

           if (stream == null) {
               throw new FileNotFoundException("Employees file not found: " + filePath);
           } else {
               return stream;
           }
       }
       finally {
           lock.unlock();
       }

    }

    public void readFile(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {

            String line;
            int row = 0;

            System.out.println("id,name,salary,joined_date,role,percentage_completed_projects");
            while ((line = reader.readLine()) != null) {
                row++;
                String[] fields = line.trim().split(",");

                if (fields.length != 6) {
                    throw new InvalidFieldsException(
                            "Invalid number of fields in line " + (row + 1));
                }
                System.out.println(line);
            }

            } catch(IOException e){
                System.out.println("Error reading test_employees.csv: " + e.getMessage());
            }
        }

    }