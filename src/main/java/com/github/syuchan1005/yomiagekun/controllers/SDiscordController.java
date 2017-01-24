package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.SQLiteConnector;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.WindowEvent;

/**
 * Created by syuchan on 2016/12/08.
 */
public class SDiscordController extends Controller {
	@FXML
	private ToggleGroup discordLogin;
	@FXML
	private RadioButton tokenRadioButton;
	@FXML
	private RadioButton emailRadioButton;
	@FXML
	private TextField tokenField;
	@FXML
	private CheckBox tokenStored;
	@FXML
	private TextField emailField;
	@FXML
	private TextField passwordField;
	@FXML
	private CheckBox emailStored;
	@FXML
	private CheckBox passwordStored;

	@Override
	public void onClose(WindowEvent event) {
		SQLiteConnector.setSaveData("discordWithToken", String.valueOf(tokenRadioButton.isSelected()));
		SQLiteConnector.setSaveData("discordToken", tokenStored.isSelected() ? tokenField.getText() : "");
		SQLiteConnector.setSaveData("discordEmail", emailStored.isSelected() ? emailField.getText() : "");
		SQLiteConnector.setSaveData("discordPassword", passwordStored.isSelected() ? passwordField.getText() : "");
	}

	public boolean isTokenLogin() {
		return tokenRadioButton.isSelected();
	}

	public String getToken() {
		return tokenField.getText();
	}

	public String getEmail() {
		return emailField.getText();
	}

	public String getPassWord() {
		return passwordField.getText();
	}
}
