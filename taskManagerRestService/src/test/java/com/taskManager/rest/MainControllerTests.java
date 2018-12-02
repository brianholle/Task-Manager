package com.taskManager.rest;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(MainController.class)
public class MainControllerTests {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepo;
    
    @Test
    public void getAllTasksFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "Completed", LocalDate.parse("2018-11-30")));
        when(taskRepo.getAllTasks()).thenReturn(tasks);
        this.mockMvc.perform(get("/taskManager/tasks/")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void getExistingTasksFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "In Progress", LocalDate.parse("2018-11-30")));
        when(taskRepo.getExistingTasks()).thenReturn(tasks);
        this.mockMvc.perform(get("/taskManager/tasks/existing")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void getNearOverdueTasksFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "In Progress", LocalDate.parse("2018-11-30")));
        when(taskRepo.getNearOverdueTasks()).thenReturn(tasks);
        this.mockMvc.perform(get("/taskManager/tasks/nearOverdue")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void getOverdueTasksFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "Overdue", LocalDate.parse("2018-11-30")));
        when(taskRepo.getOverdueTasks()).thenReturn(tasks);
        this.mockMvc.perform(get("/taskManager/tasks/overdue")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void getCompletedTasksFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "Completed", LocalDate.parse("2018-11-30")));
        when(taskRepo.getCompletedTasks()).thenReturn(tasks);
        this.mockMvc.perform(get("/taskManager/tasks/completed")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    public void getTaskByNameFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "Completed", LocalDate.parse("2018-11-30")));
        when(taskRepo.findByNameStartsWithIgnoreCase("Test")).thenReturn(tasks);
        this.mockMvc.perform(post("/taskManager/tasks/nameStarts").contentType(
                MediaType.APPLICATION_JSON).content(tasks.get(0).toString()))
        		.andDo(print()).andExpect(status().isOk());
    }
    
    @Test
    public void getIdFromService() throws Exception {
    	ArrayList<Task> tasks = new ArrayList<>();
    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", 
    			"Look into different options", "Completed", LocalDate.parse("2018-11-30")));
    	 Optional<Task> task = Optional.of(tasks.get(0));
    	when(taskRepo.findById((long) 1)).thenReturn(task);
        this.mockMvc.perform(post("/taskManager/tasks/id").contentType(
                MediaType.APPLICATION_JSON).content(tasks.get(0).getId().toString()))
        		.andDo(print()).andExpect(jsonPath("$.*", hasSize(5)));
    }
    
//    @Test
//    public void getInsertFromService() throws Exception {
//    	ArrayList<Task> tasks = new ArrayList<>();
//    	MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
//    	tasks.add(new Task(Long.parseLong("1"), "TestFramework", "Look into different options", "Completed", LocalDate.parse("2018-11-30")));
//    	when(taskRepo.save(tasks.get(0))).thenReturn(tasks.get(0));
//        this.mockMvc.perform(post("/taskManager/tasks/insert").contentType(APPLICATION_JSON_UTF8).content(tasks.get(0).toString()))
//        		.andDo(print()).andExpect(status().isOk());
//    }
}
