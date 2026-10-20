package com.ga.service;

import com.ga.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.concurrent.locks.ReentrantLock;

@Service
public class SalaryService {

    private final ReentrantLock lock = new ReentrantLock();

    public void updateSalary(Employee employee) {
        lock.lock();

        try {
            calculateSalary(employee);
        } finally {
            lock.unlock();
        }
    }

    private void calculateSalary(Employee employee) {
        long yearsWorked = ChronoUnit.YEARS.between(employee.getJoinedDate(), LocalDate.now());

        employee.setYearsWorked(yearsWorked);

        double increaseRate = 0;

        if (employee.getProjectCompletion() >= 0.60) {
            switch (employee.getRole().trim().toLowerCase()) {
                case "director":
                    increaseRate = 5;
                    break;

                case "manager":
                    increaseRate = 2;
                    break;

                case "employee":
                    increaseRate = 1;
                    if (employee.getProjectCompletion() > 0.80) {
                        increaseRate = 1.5;
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unknown role: " + employee.getRole());
            }

            increaseRate += yearsWorked * 2;

        }

        employee.setIncreasePercentage(increaseRate);

        employee.setNewSalary(employee.getSalary() * (1 + increaseRate / 100)
        );
    }

}