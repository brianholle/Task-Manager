package com.taskManager.demo;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class TaskEditor extends VerticalLayout implements KeyNotifier {

	private final TaskRepository repository;

	private Task task;

	// Task Form Fields
	TextField name = new TextField("Task Name");
	TextField description = new TextField("Task Description");
	ComboBox<String> status = new ComboBox<>("Set Task Status");
	DatePicker dueDate = new DatePicker("Due Date");
	private final Set<String> statusOptions = new HashSet<String>(
			Arrays.asList("New","In Progress", "Completed", "Overdue"));
	
	

	// Action Buttons
	Button save = new Button("Save", VaadinIcon.CHECK.create());
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", VaadinIcon.TRASH.create());
	
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
	HorizontalLayout form = new HorizontalLayout
			(name, description, status, dueDate);

	Binder<Task> binder = new Binder<>(Task.class);
	private ChangeHandler changeHandler;

	@Autowired
	public TaskEditor(TaskRepository repository) {
		this.repository = repository;
		status.setItems(statusOptions);
		status.setValue("New");
		add(form, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		setSizeFull();
		save.getElement().getThemeList().add("primary");
		delete.getElement().getThemeList().add("error");

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> editTask(task));
		setVisible(false);
	}

	void delete() {
		repository.delete(task);
		changeHandler.onChange();
	}

	void save() {
		if(dueDate.isInvalid()) {
			Notification n = generateNotification(
					"Please correct the Date and try again.", "#ff3333"
			);
			n.open();
		} else if (!statusOptions.contains(status.getValue())) {
			status.setValue("New");
		}
		repository.save(task);
		changeHandler.onChange();
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editTask(Task c) {
		if (c == null) {
			setVisible(false);
			return;
		}
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			task = repository.findById(c.getId()).get();
		}
		else {
			task = c;
		}
		cancel.setVisible(persisted);

		// Bind Task properties to similarly named fields
		binder.setBean(task);

		setVisible(true);

		// Focus name initially
		name.focus();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		changeHandler = h;
	}
	
	public Notification generateNotification(String message, String color) {
		Div content = new Div();
		content.addClassName("notification-style");
		content.setText(message);

		Notification notification = new Notification(content);
		notification.setDuration(3000);

		String styles = ".notification-style { "
		        + "  color: " + color + ";"
		        + " }";

		StreamRegistration resource = UI.getCurrent().getSession()
		        .getResourceRegistry()
		        .registerResource(new StreamResource("styles.css", () -> {
		            byte[] bytes = styles.getBytes(StandardCharsets.UTF_8);
		            return new ByteArrayInputStream(bytes);
		        }));
		UI.getCurrent().getPage().addStyleSheet(
		        "base://" + resource.getResourceUri().toString());
		
		return notification;
	}
}
