package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.SQLiteConnector;
import com.github.syuchan1005.yomiagekun.Util;
import com.github.syuchan1005.yomiagekun.contents.SkypeContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.WindowEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class SSkypeController extends Controller implements Initializable {
	@FXML
	private TextField usernameField;
	@FXML
	private CheckBox usernameStored;
	@FXML
	private TextField passwordField;
	@FXML
	private CheckBox passwordStored;

	@FXML
	private TableView tableView;
	@FXML
	private TableColumn nameColumn;
	@FXML
	private TableColumn targetColumn;

	private ObservableList<SkypeContent> skypeContents = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setItems(skypeContents);

		Util.<SkypeContent, String>setEditTableColumn(nameColumn, "name",
				TextFieldTableCell.forTableColumn(), (event, skypeContent) -> skypeContent.setName(event.getNewValue()));
		Util.<SkypeContent, String>setEditTableColumn(targetColumn, "target",
				ComboBoxTableCell.forTableColumn("self", "group"), (event, skypeContent) -> skypeContent.setTarget(event.getNewValue()));

		Util.setContentMenu(tableView, event -> {
			if (skypeContents.stream().filter(s -> s.getName().equals("")).count() == 0) {
				skypeContents.add(new SkypeContent("", ""));
			}
		}, event -> {
			int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) skypeContents.remove(selectedIndex);
		});
	}

	@Override
	public void onClose(WindowEvent event) {
		SQLiteConnector.setSaveData("skypeUserName", usernameStored.isSelected() ? usernameField.getText() : "");
		SQLiteConnector.setSaveData("skypePassword", passwordStored.isSelected() ? passwordField.getText() : "");
	}
}
