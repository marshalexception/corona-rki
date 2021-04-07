package com.sitrumm.coronarki;

import com.sitrumm.coronarki.model.employee.Employee;
import com.sitrumm.coronarki.model.employee.EmployeeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CoronaRkiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoronaRkiApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadMockData(EmployeeRepository employeeRepository) {
		return (args) -> {
			employeeRepository.save(new Employee("Bill", "Gates"));
			employeeRepository.save(new Employee("Mark", "Zuckerberg"));
			employeeRepository.save(new Employee("Sundar", "Pichai"));
			employeeRepository.save(new Employee("Jeff", "Bezos"));
		};
	}

//	https://www.baeldung.com/spring-boot-vaadin todo
//	https://documenter.getpostman.com/view/10808728/SzS8rjbc
}
