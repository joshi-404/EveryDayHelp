package com.test.everyday.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.everyday.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{
	 Employee findByEmployeeId(String employeeId);
}
