package com.sitrumm.coronarki.model.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByLastNameStartsWithIgnoreCase(String lastName);
}
