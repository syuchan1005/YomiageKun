package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.Util;
import com.github.syuchan1005.yomiagekun.contents.LicenseContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/31.
 */
public class LicenseController extends Controller implements Initializable {
	@FXML
	private TextArea thisApplication;
	@FXML
	private TableView tableView;
	@FXML
	private TableColumn libraryName;
	@FXML
	private TableColumn license;
	@FXML
	private TextArea apacheLicense2;

	private ObservableList<LicenseContent> licenseContents = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		thisApplication.setText(Util.convertString(ClassLoader.getSystemResourceAsStream("LICENSE")));
		libraryName.setCellValueFactory(new PropertyValueFactory<LicenseContent, String>("libraryName"));
		license.setCellValueFactory(new PropertyValueFactory<LicenseContent, String>("license"));
		tableView.setItems(licenseContents);
		licenseContents.add(new LicenseContent("Docomo Aitalk", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("commons-codec", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("commons-logging", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("httpcore", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("httpclient", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("httpmime", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("jackson", "Apache License 2.0"));
		licenseContents.add(new LicenseContent("logback", "LGPL 2.1 & EPL 1.0 License"));
		licenseContents.add(new LicenseContent("sqlite", "Apache License 2.0"));
		apacheLicense2.setText(Util.convertString(ClassLoader.getSystemResourceAsStream("ApacheLicense2")));
	}
}
