package com.example.TaskManagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Task Management application.
 * <p>
 * This class initializes and runs the Spring Boot application, configuring
 * all the necessary components and starting the application context.
 * </p>
 *
 * The {@link SpringBootApplication} annotation combines several essential Spring annotations:
 * <ul>
 *   <li>{@code @Configuration}: Marks this class as a source of bean definitions.</li>
 *   <li>{@code @EnableAutoConfiguration}: Enables automatic configuration of Spring components.</li>
 *   <li>{@code @ComponentScan}: Scans the package and sub-packages for Spring components.</li>
 * </ul>
 */
@SpringBootApplication
public class TaskManagementApplication {

	/**
	 * Main method to launch the application.
	 * <p>
	 * This method uses {@link SpringApplication#run(Class, String...)} to start the Spring
	 * Boot application.
	 * </p>
	 *
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(TaskManagementApplication.class, args);
	}
}
