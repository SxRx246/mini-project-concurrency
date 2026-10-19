//package com.ga;
//
//import com.ga.service.EmployeeService;
//import com.ga.exception.InvalidFieldsException;
//
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//public class Main {
//    public static void main(String[] args) {
////        EmployeeService csvProcessor = new EmployeeService();
//
//        new Thread(() -> {
//            try {
//                InputStream fileStream = csvProcessor.getEmployeesFile("/test_employees.csv");
//                System.out.println(Thread.currentThread().getName() + " reads: ");
//                csvProcessor.readFile(fileStream);
//            } catch (FileNotFoundException e) {
//                System.out.println(e.getMessage());
//
//            } catch (InvalidFieldsException e) {
//                System.out.println("CSV validation error: "
//                        + e.getMessage());
//            }
//        }).start();
//
//    }
//}
