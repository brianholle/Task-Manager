package com.taskManager.demo;

import java.time.LocalDate;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);
	private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	private final String apiUrl = "http://localhost:8080/taskManager";
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData() {
		return (args) -> {
			// save a couple of dummy tasks
//			ArrayList<Task> tasks = new ArrayList<>();
//			tasks.add(new Task(Long.parseLong("1"), "Research web frameworks", "Look into different options", "Completed", LocalDate.parse("2018-11-30")));
//			tasks.add(new Task(Long.parseLong("2"), "Design System Outline", "Detail how to implent system", "Completed", LocalDate.parse("2018-11-30")));
//			tasks.add(new Task(Long.parseLong("3"), "Outline Solution", "Reasearch best course of action", "Completed", LocalDate.parse("2018-11-30")));
//			tasks.add(new Task(Long.parseLong("4"), "Create Database", "Create database for project", "Completed", LocalDate.parse("2018-11-30")));
//			tasks.add(new Task(Long.parseLong("5"), "Create Mockup", "Quickly toss together single unit solution", "Completed", LocalDate.parse("2018-11-30")));
//			tasks.add(new Task(Long.parseLong("6"), "Create Rest API", "Separate Database calls", "Completed", LocalDate.parse("2018-12-01")));
//			tasks.add(new Task(Long.parseLong("7"), "Clean / Modularize Code", "Make sure everything is nice and readable", "In Progress", LocalDate.parse("2018-12-01")));
//			tasks.add(new Task(Long.parseLong("8"), "Write Unit Tests for Client", "Generate testing client side", "In Progress", LocalDate.parse("2018-12-01")));
//			tasks.add(new Task(Long.parseLong("9"), "Write Functional Tests for Project", "Write integration tests", "In Progress", LocalDate.parse("2018-12-01")));
//			tasks.add(new Task(Long.parseLong("10"), "Deploy to server", "Deploy to public domain", "New", LocalDate.parse("2018-12-01")));
//			tasks.add(new Task(Long.parseLong("11"), "Take a nap", "zZzZzZzZzZz...", "New", LocalDate.parse("2018-12-02")));
//			tasks.forEach(task-> restTemplate.patchForObject(apiUrl+"/tasks/insert" , task, Task.class));
//			Task task = new Task("Research web frameworks", "Look into different options", "Completed", LocalDate.parse("2018-11-30"));
//			restTemplate.postForObject(apiUrl+"/tasks/insert", task, Task.class);
//			Task[] tasks = restTemplate.getForObject(apiUrl+"/tasks/", Task[].class);
//			restTemplate.postForObject(apiUrl+"/tasks/delete", tasks[1], Task[].class);
			

		};
	}

}
