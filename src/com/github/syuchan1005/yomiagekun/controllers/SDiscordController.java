package com.github.syuchan1005.yomiagekun.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Created by syuchan on 2016/12/08.
 */
public class SDiscordController {
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
}
