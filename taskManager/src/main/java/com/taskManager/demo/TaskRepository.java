package com.taskManager.demo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface TaskRepository extends CrudRepository<Task, Long> {

	Collection<Task> findByNameStartsWithIgnoreCase(String name);
	
	@Query(value = "SELECT * FROM task ORDER BY due_date ASC", 
			  nativeQuery = true)
	Collection<Task> findAllTasks();
	
	//2. As a user, I need to be able to see a list of existing tasks. 
	@Query(value = "SELECT * FROM task  where status='New' OR status='In Progress'", 
			  nativeQuery = true)
	Collection<Task> findExistingTasks();
	
	//6. As a user, I need to be able to filter my list of tasks to just those tasks due tomorrow and / or today.
	@Query(value = "SELECT * from task WHERE DATE(task.due_date) <= DATE(NOW()) + interval '1 Day' AND DATE(task.due_date) >= DATE(NOW())",
			nativeQuery = true)
	Collection<Task> findNearOverdueTasks();
	
	//7. As a user, I need to be able to filter my list of tasks to just those tasks which are overdue. 
	@Query(value = "SELECT * FROM task  where status='Overdue'", 
			  nativeQuery = true)
	Collection<Task> findOverdueTasks();
	
	//8. As a user, I need to be able to filter my list of tasks to just those tacks which have been marked as completed. 
	@Query(value = "SELECT * FROM task where status='Completed'", 
			  nativeQuery = true)
	Collection<Task> findCompletedTasks();
	
	@Transactional 
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE task SET status = 'Overdue'  where  DATE(task.due_date) < DATE(NOW()) AND task.status != 'Completed'", 
			  nativeQuery = true)
	void updateOverdueTasks();
	
}

//DONE //As a user, I need to be able to create a task for myself providing a task name, description, and due date. 
//DONE //3. As a user, I need to be able to view the details of a specific task. 
//DONE //4. As a user, I need to be able to mark a task as completed. 
//DONE //5. As a user, I need to be able to remove a task. 
//DONE //9. As a user, I need a visual indication of which tasks are due tomorrow and / or today as well as those tasks which are past due (2 different indications). 
