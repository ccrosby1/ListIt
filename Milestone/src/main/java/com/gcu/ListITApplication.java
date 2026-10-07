/**
 * ListITApplication.java
 * 
 * This is the main class for the Spring Boot application.
 * It contains the main method that starts the application.
 */
package com.gcu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main class for the Senior Project.
 * It is annotated with @SpringBootApplication to enable Spring Boot features.
 * It also uses @ComponentScan to specify the base package for component scanning.
 */
@ComponentScan({ "com.gcu" })
@SpringBootApplication
public class ListITApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ListITApplication.class, args);
	}

}
