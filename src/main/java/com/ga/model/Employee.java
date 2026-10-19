package com.ga.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Employee {

    private final int id;
    private final String name;
    private final double salary;
    private final LocalDate joinedDate;
    private final String role;
    private final double projectCompletion;

    private long yearsWorked;
    private double increasePercentage;
    private double newSalary;

    public Employee(int id, String name, double salary, LocalDate joinedDate, String role, double projectCompletion) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.joinedDate = joinedDate;
        this.role = role;
        this.projectCompletion = projectCompletion;
        this.newSalary = salary;
    }
}
