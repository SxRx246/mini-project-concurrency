package com.ga.repository;

import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface EmployeeRepository {
    List<String> findAll() throws IOException;
}
