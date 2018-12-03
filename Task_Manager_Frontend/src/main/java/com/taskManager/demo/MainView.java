package com.taskManager.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.LocalDate;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import static com.taskManager.demo.Constants.ADD_BTN_TEXT;
import static com.taskManager.demo.Constants.ALL_TAB_TEXT;
import static com.taskManager.demo.Constants.OUTSTANDING_TAB_TEXT;
import static com.taskManager.demo.Constants.NEAR_OVERDUE_TAB_TEXT;
import static com.taskManager.demo.Constants.OVERDUE_TAB_TEXT;
import static com.taskManager.demo.Constants.COMPLETED_TAB_TEXT;
import static com.taskManager.demo.Constants.TASK_FILTER_PLACEHOLDER_TEXT;
import static com.taskManager.demo.Constants.TASK_STATUS_OPTIONS;
import static com.taskManager.demo.Constants.UPDATE_OVERDUE_TASKS_URL;
import static com.taskManager.demo.Constants.GET_ALL_TASKS_URL;
import static com.taskManager.demo.Constants.GET_TASKS_NAME_STARTS_WITH_URL;
import static com.taskManager.demo.Constants.GET_EXISTING_TASKS_URL;
import static com.taskManager.demo.Constants.GET_NEAR_OVERDUE_TASKS_URL;
import static com.taskManager.demo.Constants.GET_OVERDUE_TASKS_URL;
import static com.taskManager.demo.Constants.GET_COMPLETED_TASKS_URL;
import static com.taskManager.demo.Constants.TASK_MKANAGER_SPLITTER_POS;
import static com.taskManager.demo.Constants.TASK_MANAGER_SPLITTER_WIDTH;
import static com.taskManager.demo.Constants.GRID_WIDTH;
import static com.taskManager.demo.Constants.STATUS_COLUMN_HEADED;

@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route
public class MainView extends VerticalLayout {

	private final TaskEditor editor;
	private final Grid<Task> grid;
	private final TextField filter;
	private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());;

	private final Button addNewBtn;
	private final Tab showAllTasksTab;
	private final Tab showExistingTasksTab;
	private final Tab showNearOverdueTasksTab;
	private final Tab showOverdueTab;
	private final Tab showCompletedTasksTab;
	private final Tabs tabBar;
	private final String TASK_GRID_HEIGHT = "600px";
	private final String TASK_GRID_ID_HEIGHT = "50px";
	private final String TASK_COLUMN_ID = "id";
	private final String TASK_COLUMN_NAME = "name";
	private final String TASK_COLUMN_DESC = "description";
	private final String TASK_COLUMN_STATUS = "status";
	private final String TASK_COLUMN_DUE_DATE = "dueDate";
	private final String TASK_COLOR_GOOD = "green";
	private final String TASK_COLOR_NEAR_OVERDUE = "#cc6600";
	private final String TASK_COLOR_PAST_DUE = "red";
	

	public MainView(TaskEditor editor) {
		this.editor = editor;
		this.grid = new Grid<>(Task.class);
		this.filter = new TextField();
		this.addNewBtn = new Button(ADD_BTN_TEXT, VaadinIcon.PLUS.create());
		this.showAllTasksTab = new Tab(ALL_TAB_TEXT);
		this.showExistingTasksTab = new Tab(OUTSTANDING_TAB_TEXT);
		this.showNearOverdueTasksTab = new Tab(NEAR_OVERDUE_TAB_TEXT);
		this.showOverdueTab = new Tab(OVERDUE_TAB_TEXT);
		this.showCompletedTasksTab = new Tab(COMPLETED_TAB_TEXT);
		this.tabBar = new Tabs(showAllTasksTab, showExistingTasksTab, showNearOverdueTasksTab, 
				showOverdueTab, showCompletedTasksTab);
		
		buildLayout();
		formatGrid();
		formatFilter();
		initializeListeners();
		
		// Load grid
		listTasks(null);
	}

	private void buildLayout() {
		// Build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, tabBar);
		HorizontalLayout editorPlaceHolder = new HorizontalLayout(editor);
		SplitLayout taskManager = new SplitLayout(grid, editorPlaceHolder);
		taskManager.setSplitterPosition(TASK_MKANAGER_SPLITTER_POS);
		taskManager.setWidth(TASK_MANAGER_SPLITTER_WIDTH);
		add(actions, taskManager);
	}

	private void formatFilter() {
		filter.setPlaceholder(TASK_FILTER_PLACEHOLDER_TEXT);
	}

	private void initializeListeners() {
		// Wire logic to components
		// Update grid with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(e -> listTasks(e.getValue()));
		tabBar.addSelectedChangeListener(e->{
			int index = e.getSource().getSelectedIndex();
			if (index == tabBar.indexOf(showAllTasksTab)) {
				listTasks(null);
			} else if (index == tabBar.indexOf(showExistingTasksTab)) {
				listExistingTasks();
			} else if (index == tabBar.indexOf(showNearOverdueTasksTab)) {
				listNearOverdueTasks();
			} else if (index == tabBar.indexOf(showOverdueTab)) {
				listOverdueTasks();
			} else if (index == tabBar.indexOf(showCompletedTasksTab)) {
				listCompletedTasks();
			}
		});

		// Connect selected task to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editTask(e.getValue());
		});

		// Instantiate and edit new task
		addNewBtn.addClickListener(e -> {
			editor.editTask(new Task("", "", TASK_STATUS_OPTIONS.get(0), null));
		});
					
		// Listen for changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listTasks(filter.getValue());
		});
	}

	private void formatGrid() {
		// Build Grid
		grid.setHeight(TASK_GRID_HEIGHT);
		grid.setWidth(GRID_WIDTH);
		grid.setColumns(TASK_COLUMN_ID, TASK_COLUMN_NAME, TASK_COLUMN_DESC);
		// Enable dynamic styling for coloring status column objects
		grid.addColumn(TemplateRenderer.<Task> of(
		        "<div><color style='background:[[item.color]];"
		        		+ "border-radius: 25px;"
		        		+ "padding: 20px; "
		        		+ "width: 200px;"
		        		+ "height: 150px;'>"
		        		+ "[[item."+TASK_COLUMN_STATUS+"]]</color></div>")
		        .withProperty("status", Task::getStatus).withProperty("color",
		                task -> {
		                	if (task.getDueDate().compareTo(LocalDate.now()) < 0) {
		                		return TASK_COLOR_PAST_DUE;
		                	} else if (task.getDueDate().compareTo(LocalDate.now()) >= 0 
		                			&& task.getDueDate().compareTo(LocalDate.now().plusDays(1)) <= 0) {
		                		return TASK_COLOR_NEAR_OVERDUE;
		                	} else {
		                		return TASK_COLOR_GOOD;
		                	}
		                }
		         )).setHeader(STATUS_COLUMN_HEADED)
		        .setKey(TASK_COLUMN_STATUS)
		        .setSortProperty(TASK_COLUMN_STATUS)
		        .setSortable(true)
		        .setComparator((status1, status2) -> status1.getStatus()
		        		.compareTo(status2.getStatus()));;
		grid.addColumn(TASK_COLUMN_DUE_DATE);
		grid.getColumnByKey(TASK_COLUMN_ID).setVisible(false);
		grid.getColumnByKey(TASK_COLUMN_ID).setWidth(TASK_GRID_ID_HEIGHT).setFlexGrow(0);
		grid.setColumnReorderingAllowed(true);
	}

	private void listTasks(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			restTemplate.getForObject(UPDATE_OVERDUE_TASKS_URL, int.class);
			grid.setItems(restTemplate.getForObject(GET_ALL_TASKS_URL, Task[].class));
		} else {
			grid.setItems(restTemplate.postForObject(GET_TASKS_NAME_STARTS_WITH_URL, filterText, Task[].class));
		}
	}
	
	private void listExistingTasks() {
		restTemplate.getForObject(UPDATE_OVERDUE_TASKS_URL, int.class);
		grid.setItems(restTemplate.getForObject(GET_EXISTING_TASKS_URL, Task[].class));
	}
	
	private void listNearOverdueTasks() {
		restTemplate.getForObject(UPDATE_OVERDUE_TASKS_URL, int.class);
		grid.setItems(restTemplate.getForObject(GET_NEAR_OVERDUE_TASKS_URL, Task[].class));
	}
	
	private void listOverdueTasks() {
		restTemplate.getForObject(UPDATE_OVERDUE_TASKS_URL, int.class);
		grid.setItems(restTemplate.getForObject(GET_OVERDUE_TASKS_URL, Task[].class));
	}
	
	private void listCompletedTasks() {
		restTemplate.getForObject(UPDATE_OVERDUE_TASKS_URL, int.class);
		grid.setItems(restTemplate.getForObject(GET_COMPLETED_TASKS_URL, Task[].class));
	}

}
