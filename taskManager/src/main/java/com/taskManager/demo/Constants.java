package com.taskManager.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Constants {

	private Constants() {}
	
	public static final String API_BASE_URL = "http://task-manager-backend:8080/taskManager";
	public static final String UPDATE_OVERDUE_TASKS_URL = API_BASE_URL + "/updateOverdueTasks";
	public static final String GET_ALL_TASKS_URL = API_BASE_URL + "/tasks";
	public static final String GET_TASKS_NAME_STARTS_WITH_URL = API_BASE_URL + "/tasks/nameStarts";
	public static final String GET_EXISTING_TASKS_URL = API_BASE_URL + "/tasks/existing";
	public static final String GET_NEAR_OVERDUE_TASKS_URL = API_BASE_URL + "/tasks/nearOverdue";
	public static final String GET_OVERDUE_TASKS_URL = API_BASE_URL + "/tasks/overdue";
	public static final String GET_COMPLETED_TASKS_URL = API_BASE_URL+"/tasks/completed";
	public static final String GET_TASK_BY_ID_URL = API_BASE_URL+"/tasks/id";
	public static final String DELETE_TASKS_URL = API_BASE_URL+"/tasks/delete";
	public static final String INSERT_TASKS_URL = API_BASE_URL+"/tasks/insert";
	
	
	public static final String ADD_BTN_TEXT = "New Task";
	public static final String ALL_TAB_TEXT = "All Tasks";
	public static final String OUTSTANDING_TAB_TEXT = "Outstanding Tasks";
	public static final String NEAR_OVERDUE_TAB_TEXT = "Nearly Due Tasks";
	public static final String OVERDUE_TAB_TEXT = "Overdue Tasks";
	public static final String COMPLETED_TAB_TEXT = "Completed Tasks";
	public static final String TASK_FILTER_PLACEHOLDER_TEXT = "Filter by Task Name";
	
	public static final String EDITOR_NAME_LABEL = "Task Name";
	public static final String EDITOR_DESC_LABEL = "Task Description";
	public static final String EDITOR_STATUS_LABEL = "Set Task Status";
	public static final String EDITOR_DATE_LABEL = "Due Date";
	public static final String EDITOR_SAVE_LABEL = "Save";
	public static final String EDITOR_CANCEL_LABEL = "Cancel";
	public static final String EDITOR_DELETE_LABEL = "Delete";
	public static final String EDITOR_SAVE_THEME = "primary";
	public static final String EDITOR_DELETE_THEME = "error";
	
	public static final String EDITOR_DATE_ERROR_MSG = "Please correct the Date and try again.";
	public static final String EDITOR_DATE_ERROR_MSG_COLOR = "#ff3333";

	
	public static final List<String> TASK_STATUS_OPTIONS = new ArrayList<String>(
			Arrays.asList("New","In Progress", "Completed", "Overdue"));
}
