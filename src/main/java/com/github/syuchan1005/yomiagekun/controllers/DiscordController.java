package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.DiscordWrapper;
import com.github.syuchan1005.yomiagekun.SQLiteConnector;
import com.github.syuchan1005.yomiagekun.contents.ReadContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.WindowEvent;
import sx.blah.discord.util.DiscordException;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class DiscordController extends Controller implements Initializable {
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
	@FXML
	private CheckBox checkBox;

	private ObservableList<ReadContent> readContents = FXCollections.observableArrayList();
	private DiscordWrapper discordWrapper;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		usernameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
		textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
		readTextColumn.setCellValueFactory(new PropertyValueFactory<>("readText"));
		tableView.setItems(readContents);
	}

	@Override
	public void onClose(WindowEvent event) {
		SQLiteConnector.setSaveData("discordIsSpeak", String.valueOf(isDiscordSpeak()));
	}

	@FXML
	private void onStartButton() {
		if (discordWrapper == null) {
			SDiscordController sDiscordController = getYomiageKun().getsDiscordController();
			try {
				if (sDiscordController.isTokenLogin()) {
					discordWrapper = new DiscordWrapper(sDiscordController.getToken());
				} else {
					discordWrapper = new DiscordWrapper(sDiscordController.getEmail(), sDiscordController.getPassWord());
				}
			} catch (DiscordException e) {
				e.printStackTrace();
			}
		}
		discordWrapper.readStart();
	}

	@FXML
	private void onStopButton() {
		discordWrapper.readStop();
	}

	public void addReadingList(String nick, String text, String rep) {
		readContents.add(new ReadContent(nick, text, rep));
	}

	public boolean isDiscordSpeak() {
		return checkBox.isSelected();
	}
}
