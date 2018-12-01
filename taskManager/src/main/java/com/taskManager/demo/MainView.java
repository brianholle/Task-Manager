package com.taskManager.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.time.LocalDate;
import java.util.Collection;
import org.springframework.util.StringUtils;

@Theme(value = Lumo.class, variant = Lumo.DARK)
@Route
public class MainView extends VerticalLayout {

	private final TaskRepository repo;
	private final TaskEditor editor;
	private final Grid<Task> grid;
	private final TextField filter;

	private final Button addNewBtn;
	private final Tab showTasksTab;
	private final Tab showOverdueTab;
	private final Tab showNearOverdueTasksTab;
	private final Tab showCompletedTasksTab;
	private final Tabs tabBar;

	public MainView(TaskRepository repo, TaskEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid<>(Task.class);
		this.filter = new TextField();
		this.addNewBtn = new Button("New Task", VaadinIcon.PLUS.create());
		this.showTasksTab = new Tab("Show Existing Tasks");
		this.showNearOverdueTasksTab = new Tab("Show Nearly Overdue Tasks");
		this.showOverdueTab = new Tab("Show Overdue Tasks");
		this.showCompletedTasksTab = new Tab("Show Completed Tasks");
		this.tabBar = new Tabs(showTasksTab, showNearOverdueTasksTab, 
				showOverdueTab, showCompletedTasksTab);

		// Build layout
		HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn, tabBar);
		add(actions, grid, editor);

		// Build Grid
		grid.setHeight("300px");
		grid.setColumns("id", "name", "description");
		// Enable dynamic styling for coloring status column objects
		grid.addColumn(TemplateRenderer.<Task> of(
		        "<div><color style='background:[[item.color]];"
		        		+ "border-radius: 25px;"
		        		+ "padding: 20px; "
		        		+ "width: 200px;"
		        		+ "height: 150px;'>"
		        		+ "[[item.status]]</color></div>")
		        .withProperty("status", Task::getStatus).withProperty("color",
		                task -> {
		                	if (task.getDueDate().compareTo(LocalDate.now()) < 0)
		                		return "red";
		                	else if (task.getDueDate().compareTo(LocalDate.now()) >= 0 
		                			&& task.getDueDate().compareTo(LocalDate.now().plusDays(1)) <= 0)
		                		return "#cc6600"; //Yellowish Orange
		                	else
		                		return "green";
		                }
		                        ))
		        .setHeader("Status").setKey("status").setSortProperty("status");
		grid.getColumnByKey("status").setSortable(true);
		grid.getColumnByKey("status").setComparator((status1, status2) -> status1.getStatus().compareTo(status2.getStatus()));
		grid.addColumn("dueDate");
		grid.getColumnByKey("id").setVisible(false);
		grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
		grid.setColumnReorderingAllowed(true);

		filter.setPlaceholder("Filter by Task Name");

		// Wire logic to components
		// Update grid with filtered content when user changes filter
		filter.setValueChangeMode(ValueChangeMode.EAGER);
		filter.addValueChangeListener(e -> listTasks(e.getValue()));

		// Connect selected task to editor or hide if none is selected
		grid.asSingleSelect().addValueChangeListener(e -> {
			editor.editTask(e.getValue());
		});

		// Instantiate and edit new task
		addNewBtn.addClickListener(e -> {
			editor.editTask(new Task("", "", "New", null));
		});
		
		
		// Listen for changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listTasks(filter.getValue());
		});

		// Load grid
		listTasks(null);
	}

	void listTasks(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			repo.updateOverdueTasks();
			grid.setItems((Collection<Task>) repo.findAllTasks());
		}
		else {
			grid.setItems(repo.findByNameStartsWithIgnoreCase(filterText));
		}
	}
	
	void listExistingTasks() {
		repo.updateOverdueTasks();
		grid.setItems(repo.findAllTasks());
	}
	
	void listNearOverdueTasks() {
		repo.updateOverdueTasks();
		grid.setItems(repo.findNearOverdueTasks());
	}
	
	void listOverdueTasks() {
		repo.updateOverdueTasks();
		grid.setItems(repo.findOverdueTasks());
	}
	
	void listCompletedTasks() {
		repo.updateOverdueTasks();
		grid.setItems(repo.findCompletedTasks());
	}

}
