package com.ga.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents an employee and stores their personal information,
 * employment details, and salary calculation results.
 */
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    private int id;
    private String name;
    private double salary;
    private LocalDate joinedDate;
    private String role;
    private double projectCompletion;
    private long yearsWorked;
    private double increasePercentage;
    private double newSalary;

}
