package com.taskManager.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(TaskRepository repository) {
		return (args) -> {
			// save a couple of dummy tasks
			//repository.save(new Task("Task1", "Task 1 Description", "New", LocalDate.now()));
		};
	}

}
