package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.SkypeContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class SSkypeController implements Initializable {
	@FXML
	private TextField usernameField;
	@FXML
	private CheckBox usernameStored;
	@FXML
	private TextField passwordField;
	@FXML
	private CheckBox passwordStored;
	@FXML
	private TextField nameField;
	@FXML
	private ChoiceBox choiceBox;
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn nameColumn;
	@FXML
	private TableColumn targetColumn;

	private ObservableList<SkypeContent> skypeContentList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameColumn.setCellValueFactory(new PropertyValueFactory<SkypeContent, String>("name"));
		targetColumn.setCellValueFactory(new PropertyValueFactory<SkypeContent, String>("target"));
		tableView.setItems(skypeContentList);
	}
}
