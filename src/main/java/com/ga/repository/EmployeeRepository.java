package com.ga.repository;

import java.io.IOException;
import java.util.List;

public interface EmployeeRepository {
    List<String> findAll() throws IOException;
}
