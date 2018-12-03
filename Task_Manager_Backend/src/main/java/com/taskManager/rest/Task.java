package com.taskManager.rest;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Task {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String description;
	
	private String status;
	
	private LocalDate dueDate;

	protected Task() {
	}

	public Task(String name, String description, String status, LocalDate dueDate) {
		this.name = name;
		this.description = description;
		this.status = status;
		this.dueDate = dueDate;
	}
	
	public Task(Long id, String name, String description, String status, LocalDate dueDate) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.status = status;
		this.dueDate = dueDate;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	
	public LocalDate getDueDate() {
		return dueDate;
	}

	@Override
	public String toString() {
		return "Task{" +
		"id='" + this.id + '\'' +
		", name='" + this.name + '\'' +
		", description='" + this.description + '\'' +
		", status='" + this.status + '\'' +
		", dueDate='" + this.dueDate + 
		'}';
	}

}
