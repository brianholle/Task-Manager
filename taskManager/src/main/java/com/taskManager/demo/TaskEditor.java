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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import static com.taskManager.demo.Constants.TASK_STATUS_OPTIONS;
import static com.taskManager.demo.Constants.EDITOR_NAME_LABEL;
import static com.taskManager.demo.Constants.EDITOR_DESC_LABEL;
import static com.taskManager.demo.Constants.EDITOR_STATUS_LABEL;
import static com.taskManager.demo.Constants.EDITOR_DATE_LABEL;
import static com.taskManager.demo.Constants.EDITOR_SAVE_LABEL;
import static com.taskManager.demo.Constants.EDITOR_CANCEL_LABEL;
import static com.taskManager.demo.Constants.EDITOR_DELETE_LABEL;
import static com.taskManager.demo.Constants.EDITOR_SAVE_THEME;
import static com.taskManager.demo.Constants.EDITOR_DELETE_THEME;
import static com.taskManager.demo.Constants.DELETE_TASKS_URL;
import static com.taskManager.demo.Constants.INSERT_TASKS_URL;
import static com.taskManager.demo.Constants.EDITOR_DATE_ERROR_MSG;
import static com.taskManager.demo.Constants.EDITOR_DATE_ERROR_MSG_COLOR;
import static com.taskManager.demo.Constants.GET_TASK_BY_ID_URL;

@SpringComponent
@UIScope
public class TaskEditor extends VerticalLayout implements KeyNotifier {

	private final RestTemplate restTemplate;

	private Task task;

	// Task Form Fields
	TextField name = new TextField(EDITOR_NAME_LABEL);
	TextField description = new TextField(EDITOR_DESC_LABEL);
	ComboBox<String> status = new ComboBox<>(EDITOR_STATUS_LABEL);
	DatePicker dueDate = new DatePicker(EDITOR_DATE_LABEL);
	
	

	// Action Buttons
	Button save = new Button(EDITOR_SAVE_LABEL, VaadinIcon.CHECK.create());
	Button cancel = new Button(EDITOR_CANCEL_LABEL);
	Button delete = new Button(EDITOR_DELETE_LABEL, VaadinIcon.TRASH.create());
	
	HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
	VerticalLayout form = new VerticalLayout
			(name, description, status, dueDate);

	Binder<Task> binder = new Binder<>(Task.class);
	private ChangeHandler changeHandler;

	@Autowired
	public TaskEditor() {
		this.restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
		status.setItems(TASK_STATUS_OPTIONS);
		status.setValue(TASK_STATUS_OPTIONS.get(0));
		add(form, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// Configure and style components
		setSpacing(true);
		setSizeFull();
		save.getElement().getThemeList().add(EDITOR_SAVE_THEME);
		delete.getElement().getThemeList().add(EDITOR_DELETE_THEME);

		addKeyPressListener(Key.ENTER, e -> save());

		// wire action buttons
		save.addClickListener(e -> save());
		delete.addClickListener(e -> delete());
		cancel.addClickListener(e -> changeHandler.onChange());
		setVisible(false);
	}

	void delete() {
		if (task.getId() != null) restTemplate.postForObject(DELETE_TASKS_URL , task, int.class);
		changeHandler.onChange();
	}

	void save() {
		if(dueDate.isEmpty() || dueDate.isInvalid()) {
			Notification n = generateNotification(
					EDITOR_DATE_ERROR_MSG, EDITOR_DATE_ERROR_MSG_COLOR
			);
			n.open();
		} else if (!TASK_STATUS_OPTIONS.contains(status.getValue())) {
			status.setValue(TASK_STATUS_OPTIONS.get(0));
		} else {
			restTemplate.postForObject(INSERT_TASKS_URL, task, Task.class);
			changeHandler.onChange();
		}
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
			task = restTemplate.postForObject(GET_TASK_BY_ID_URL , c.getId(), Task.class);;
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
		notification.setPosition(Notification.Position.TOP_END);

		String styles = ".notification-style {   color: " + color + ";}";

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
