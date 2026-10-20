package com.ga.service;

import com.ga.model.Employee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Calculates employee salary increases based on role, years of service,
 * and project completion. Uses concurrency tools to manage salary
 * processing and track completed tasks.
 */
@Service
public class SalaryService {

    private final ReentrantLock lock = new ReentrantLock();
    private final Semaphore semaphore = new Semaphore(2);
    private final AtomicInteger completedTasks = new AtomicInteger(0);

    /**
     * Calculates the salary increase for a single employee while
     * holding a lock to prevent other threads from accessing the
     * protected operation simultaneously.
     *
     * @param employee the employee whose salary will be updated
     */
    public void updateSalary(Employee employee) {
        lock.lock();

        try {
            calculateSalary(employee);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Calculates the employee's years of service, increase percentage,
     * and new salary. Employees with project completion below 60%
     * receive no increase. Eligible employees receive a role-based
     * increase and an additional 2% for each completed year of service.
     *
     * @param employee the employee whose salary information is calculated
     * @throws IllegalArgumentException if the employee has an unknown role
     */
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

    /**
     * Calculates salary increases for all employees concurrently using
     * a fixed thread pool. A semaphore limits the number of threads
     * allowed to calculate salaries simultaneously, and an atomic
     * counter tracks successfully completed tasks.
     *
     * @param employees the list of employees whose salaries will be updated
     * @return the list of employees with updated salary information
     * @throws InterruptedException if the current thread is interrupted
     * @throws ExecutionException if a salary-processing task fails
     */
    public List<Employee> updateAllSalaries(List<Employee> employees) throws InterruptedException, ExecutionException {

        lock.lock();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        try {
            List<Future<?>> tasks = new ArrayList<>();

            for (Employee employee : employees) {
                tasks.add(executor.submit(() -> {
                    boolean acquired = false;

                    try {
                        semaphore.acquire();
                        acquired = true;

                        calculateSalary(employee);
                        completedTasks.incrementAndGet();

                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(exception);

                    } finally {
                        if (acquired) {
                            semaphore.release();
                        }
                    }
                }));
            }

            for (Future<?> task : tasks) {
                task.get();
            }

            return employees;

        } finally {
            executor.shutdown();
            lock.unlock();
        }
    }

    /**
     * Retrieves the number of successfully completed salary-processing tasks.
     *
     * @return the number of completed tasks
     */
    public int getCompletedTasks() {
        return completedTasks.get();
    }

}