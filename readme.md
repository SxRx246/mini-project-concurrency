# Concurrent CSV Data Processor

## 1. Project Overview

The Concurrent CSV Data Processor is a Java Spring Boot application that reads employee information from a CSV file, calculates salary increases according to company rules, and processes multiple employee records concurrently.

The application provides REST API endpoints to retrieve employee information, update individual salaries, and update all employee salaries. The calculated results are saved to a separate output CSV file.

## 2. Project Objectives

The objectives of this project are to:

- Read employee information from a CSV file.
- Calculate salary increases based on employee roles, years of service, and project completion.
- Use multithreading to process employee records concurrently.
- Apply concurrency control to help protect shared data.
- Use an atomic variable to count completed processing tasks.
- Expose REST API endpoints using Spring Boot.
- Save the processed results to a CSV output file.

## 3. Technologies Used

- **Java 17:** Programming language.
- **Spring Boot 3.5.10:** Application framework.
- **Spring Web:** REST API development.
- **Maven:** Dependency management and project building.
- **Lombok:** Reduces boilerplate code for the Employee model.
- **Java Concurrency API:** Provides thread pools, locks, semaphores, and atomic variables.
- **CSV:** Stores employee input and processed output data.
- **Postman:** Tests the REST API.
- **Git:** Tracks project changes and versions.

## 4. Project Structure

```text
src/
├── main/
│   ├── java/com/ga/
│   │   └── Application.java
│   ├── java/com/ga/model/
│   │   └── Employee.java
│   ├── java/com/ga/repository/
│   │   └── EmployeeRepository.java
│   ├── java/com/ga/service/
│   │   ├── EmployeeService.java
│   │   └── SalaryService.java
│   ├── java/com/ga/controller/
│   │   └── EmployeeController.java
│   └── resources/
│       └── test_employees.csv
└── test/
    └── java/
```

The generated `output_employees.csv` file contains the processed results.

## 5. Application Components

### Employee

Represents an employee and stores their ID, name, current salary, joining date, role, project completion percentage, completed years of service, calculated increase percentage, and new salary.

### EmployeeRepository

Reads employee records from `test_employees.csv` and writes the processed employee information to the output CSV file.

### EmployeeService

Loads employee records when the application starts, stores them in a `ConcurrentHashMap`, and provides methods to retrieve employees and save results.

### SalaryService

Calculates salary increases and processes multiple employees using a thread pool. It also uses concurrency control mechanisms to coordinate processing.

### EmployeeController

Exposes REST API endpoints for retrieving employees, updating salaries, and checking the number of completed processing tasks.

### Application

The main Spring Boot class that starts the application.

## 6. Setup Instructions

### Prerequisites

Install the following:

- Java Development Kit (JDK) 17 or later.
- Maven, or use the Maven wrapper if provided.
- IntelliJ IDEA or another Java IDE.
- Postman for API testing.

### Run the Application

1. Clone or download the project.
2. Open the project in your IDE.
3. Make sure the Maven dependencies are loaded.
4. Place `test_employees.csv` inside `src/main/resources/`.
5. Run the `Application.java` class.
6. The application starts at `http://localhost:8080`.

The input CSV must contain six comma-separated fields per row:

```text
id,name,salary,joined_date,role,percentage_completed_projects
```

Dates must use the `yyyy-MM-dd` format. Project completion is represented as a decimal between `0` and `1`, where `0.80` means 80%.

## 7. Salary Calculation Rules

The application applies the following rules:

1. Employees who complete less than 60% of their projects receive no salary increase.
2. Employees who qualify receive a role-based increase:
    - Director: 5%.
    - Manager: 2%.
    - Employee: 1%.
3. Employees receive an additional 2 percentage points for each completed year of service.
4. Employees receive a 1% increase rate, which rises to 1.5% if their project completion is strictly greater than 80%.
5. Employees with less than one completed year of service do not receive the years-of-service bonus.

The role-based increase rate and years-of-service bonus are added together. The 1.5% employee rate is used instead of the standard 1% rate when project completion exceeds 80%.

**Example:** An employee with the Employee role, three completed years of service, and 90% project completion has an increase rate of 1.5% + (3 × 2%) = 7.5%.

The new salary is calculated from the employee's original salary, avoiding repeated increases when the update endpoint is called more than once.

## 8. Concurrency Implementation

The application uses several Java concurrency mechanisms.

### ExecutorService

A fixed thread pool of four threads allows multiple employee salary calculations to run concurrently.

### ReentrantLock

A lock coordinates salary update operations to prevent overlapping update requests from interfering with one another.

### Semaphore

A semaphore with two permits limits the number of concurrent tasks that can enter the protected processing section to two.

### AtomicInteger

An atomic integer counts successfully processed employee tasks without requiring a separate synchronized increment operation.

These mechanisms demonstrate thread pooling, coordination, and shared-state management.

## 9. REST API Endpoints

The following endpoints are available:

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/employees` | Retrieves all employees. |
| GET | `/api/employees/{id}` | Retrieves an employee by ID. |
| PUT | `/api/employees/{id}/salary` | Calculates the salary increase for one employee. |
| PUT | `/api/employees/salaries` | Calculates salary increases for all employees concurrently. |
| GET | `/api/employees/salary-processing/completed` | Returns the cumulative count of completed bulk-processing tasks. |

### Example Requests

Retrieve all employees:

```http
GET http://localhost:8080/api/employees
```

Update one employee's salary:

```http
PUT http://localhost:8080/api/employees/1/salary
```

Update all employee salaries:

```http
PUT http://localhost:8080/api/employees/salaries
```

Check the processing count:

```http
GET http://localhost:8080/api/employees/salary-processing/completed
```

The update endpoints do not require a request body.

## 10. CSV Output

The application writes processed employee information to `output_employees.csv`, including:

- Employee ID and name.
- Original salary.
- Joining date and role.
- Project completion percentage.
- Completed years of service.
- Salary increase percentage.
- New salary.

The original input file should remain unchanged. The output file is generated separately.

## 11. Testing

The application can be tested manually to verify the following:

- CSV records are loaded correctly.
- Invalid records and duplicate employee IDs are handled.
- Employees below the 60% threshold receive no increase.
- Each role receives the correct base percentage.
- Completed years of service are calculated correctly.
- The 80% threshold is handled correctly.
- Individual and bulk API requests return the expected results.
- Concurrent processing completes without losing updates.
- The input CSV remains unchanged after processing.

## 12. Project Report

### Implementation Summary

The project combines CSV file handling, salary calculation, REST API development, and concurrent processing in a Spring Boot application. Employee information is loaded into a concurrent map, making it accessible to the application's service methods.

Salary calculations are handled by a dedicated service. Bulk salary updates use an executor thread pool, while a semaphore limits concurrent processing and a lock coordinates update operations. An atomic counter tracks completed bulk-processing tasks.

The controller exposes the functionality through REST endpoints, allowing the application to be tested using Postman without requiring a frontend.

### Expected Outcomes

The application is intended to:

- Read employee data from a CSV file.
- Calculate salary increases according to the defined rules.
- Process multiple employees concurrently.
- Return employee information through REST endpoints.
- Save the updated salary results to a separate CSV file.
- Demonstrate the use of Java concurrency mechanisms.

Actual test results should be recorded after running the application and its automated tests.

### Limitations and Future Improvements

Potential future improvements include adding a simple frontend, allowing salary rules to be configured through API inputs, improving CSV support for quoted fields, and adding automated tests.

## 13. Version Control

Git is used to track project development. The project requirements specify at least 20 meaningful commits with clear messages describing individual changes.

