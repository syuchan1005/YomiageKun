package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.contents.StudyContext;
import com.github.syuchan1005.yomiagekun.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class StudyController extends Controller implements Initializable {
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn beforeColumn;
	@FXML
	private TableColumn afterColumn;
	@FXML
	private TableColumn priorityColumn;
	@FXML
	private TableColumn isRegexColumn;

	private ObservableList<StudyContext> studyContexts = FXCollections.observableArrayList();

	private ContextMenu contextMenu = new ContextMenu();
	private MenuItem addItem = new MenuItem("Add");
	private MenuItem deleteItem = new MenuItem("Delete");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setItems(studyContexts);
		tableView.setEditable(true);

		Util.<StudyContext, String>setEditTableColumn(beforeColumn, "beforeText",
				TextFieldTableCell.forTableColumn(), (event, studyContext) -> studyContext.setBeforeText(event.getNewValue()));

		Util.<StudyContext, String>setEditTableColumn(afterColumn, "afterText",
				TextFieldTableCell.forTableColumn(), (event, studyContext) -> studyContext.setAfterText(event.getNewValue()));

		Util.<StudyContext, Integer>setEditTableColumn(priorityColumn, "priority",
				TextFieldTableCell.forTableColumn(new IntegerStringConverter()), (event, studyContext) -> studyContext.setPriority(event.getNewValue()));

		Util.<StudyContext, Boolean>setEditTableColumn(isRegexColumn, "isRegex",
				CheckBoxTableCell.forTableColumn(isRegexColumn), (event, studyContext) -> studyContext.setRegex(event.getNewValue()));

		addItem.setOnAction(event -> {
			if (studyContexts.stream().filter(s -> s.getBeforeText().equals("")).count() == 0) {
				studyContexts.add(new StudyContext("", "", 0, false));
			}
		});
		deleteItem.setOnAction(event -> studyContexts.remove(tableView.getSelectionModel().getSelectedIndex()));
		contextMenu.getItems().addAll(addItem, deleteItem);
		tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			switch (event.getButton()) {
				case PRIMARY:
					contextMenu.hide();
					break;
				case SECONDARY:
					contextMenu.show(tableView, event.getScreenX(), event.getScreenY());
					break;
			}
		});
	}
}
