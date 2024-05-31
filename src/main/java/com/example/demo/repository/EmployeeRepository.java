package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	Optional<Employee> findByEmail(String email);

	// Custom query JPQL with index params
	@Query("select e from Employee e where e.firstName=?1 and e.lastName=?2")
	Employee findByJPQL(String firstName, String lastName);

	// Custom query with JPQL with named params
	@Query("select e from Employee e where e.firstName=:firstName and e.lastName=:lastName")
	Employee findByJPQLNameParams(@Param("firstName") String firstName, @Param("lastName") String lastName);

	// Custom query using SQL native with index params
	@Query(value = "select * from employees e where e.fist_name=?1 and e.last_name=?2", nativeQuery = true)
	Employee findByNativeSQL(String firstName, String lastName);

	// Custom query using SQL native with named params
	@Query(value = "select * from employees e where e.fist_name=:firstName and e.last_name=:lastName", nativeQuery = true)
	Employee findByNativeSQLNamed(@Param("firstName") String firstName, @Param("lastName") String lastName);

}
