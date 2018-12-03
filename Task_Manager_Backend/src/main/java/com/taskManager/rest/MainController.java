package com.taskManager.rest;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path="/taskManager")
public class MainController {
	@Autowired 
	private TaskRepository taskRepository;
	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	
	//Ex. localhost:8080/taskManager/tasks/update?id=106&name=task2RestTest&description=Task 2 Updated via REST api&status=New&dueDate=2018-12-01
	@PostMapping(path="/tasks/insert")
	public @ResponseBody Task insertTask (@RequestBody Task task) {
		return taskRepository.save(task);
	}
	
	@GetMapping(path="/updateOverdueTasks")
	public @ResponseBody int updateOverdueTasks() {
		return taskRepository.updateOverdueTasks();
	}
	
	@PostMapping(path="/tasks/delete")
	public @ResponseBody void deleteTask (@RequestBody Task task) {
			taskRepository.deleteById(task.getId());
	}

	@GetMapping(path="/tasks")
	public @ResponseBody Iterable<Task> getAllTasks() {
		return taskRepository.getAllTasks();
	}
	
	@GetMapping(path="/tasks/existing")
	public @ResponseBody Iterable<Task> getExistingTasks() {
		return taskRepository.getExistingTasks();
	}
	
	@GetMapping(path="/tasks/nearOverdue")
	public @ResponseBody Iterable<Task> getNearOverdueTasks() {
		return taskRepository.getNearOverdueTasks();
	}
	
	@GetMapping(path="/tasks/overdue")
	public @ResponseBody Iterable<Task> getOverdueTasks() {
		return taskRepository.getOverdueTasks();
	}
	
	@GetMapping(path="/tasks/completed")
	public @ResponseBody Iterable<Task> getCompletedTasks() {
		return taskRepository.getCompletedTasks();
	}
	
	
	@PostMapping(path="/tasks/nameStarts")
	public @ResponseBody Iterable<Task> getTaskByNameStartsWith(@RequestBody String name) {
		return taskRepository.findByNameStartsWithIgnoreCase(name);
	}
	
	@PostMapping(path="/tasks/id")
	public @ResponseBody Optional<Task> getTaskById(@RequestBody Long id) {
		return taskRepository.findById(id);
	}
}