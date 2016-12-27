package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.ReadContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class SkypeController implements Initializable {
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn usernameColumn;
	@FXML
	private TableColumn textColumn;
	@FXML
	private TableColumn readTextColumn;
	@FXML
	private Button startButton;
	@FXML
	private Button stopButton;

	private ObservableList<ReadContent> readContents = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usernameColumn.setCellValueFactory(new PropertyValueFactory<ReadContent, String>("userName"));
		textColumn.setCellValueFactory(new PropertyValueFactory<ReadContent, String>("text"));
		readTextColumn.setCellValueFactory(new PropertyValueFactory<ReadContent, String>("readText"));
		tableView.setItems(readContents);
	}
}
