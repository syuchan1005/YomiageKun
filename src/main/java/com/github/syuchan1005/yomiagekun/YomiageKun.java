package com.github.syuchan1005.yomiagekun;

import com.github.syuchan1005.yomiagekun.controllers.DiscordController;
import com.github.syuchan1005.yomiagekun.controllers.GeneralController;
import com.github.syuchan1005.yomiagekun.controllers.SDiscordController;
import com.github.syuchan1005.yomiagekun.controllers.SSkypeController;
import com.github.syuchan1005.yomiagekun.controllers.SkypeController;
import com.github.syuchan1005.yomiagekun.controllers.StudyController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class YomiageKun extends Application implements Initializable {
	@FXML
	private SkypeController skypeController;
	@FXML
	private DiscordController discordController;
	@FXML
	private StudyController studyController;
	@FXML
	private GeneralController generalController;
	@FXML
	private SSkypeController sSkypeController;
	@FXML
	private SDiscordController sDiscordController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		skypeController.setYomiageKun(this);
		discordController.setYomiageKun(this);
		studyController.setYomiageKun(this);
		generalController.setYomiageKun(this);
		sSkypeController.setYomiageKun(this);
		sDiscordController.setYomiageKun(this);
	}

	public SkypeController getSkypeController() {
		return skypeController;
	}

	public DiscordController getDiscordController() {
		return discordController;
	}

	public StudyController getStudyController() {
		return studyController;
	}

	public GeneralController getGeneralController() {
		return generalController;
	}

	public SSkypeController getsSkypeController() {
		return sSkypeController;
	}

	public SDiscordController getsDiscordController() {
		return sDiscordController;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("YomiageKun");
		URL resource = ClassLoader.getSystemResource("fxmls/Main.fxml");
		primaryStage.setScene(new Scene(FXMLLoader.load(resource), 800, 500));
		primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("icon.png")));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) {
		if (args.length >= 1 && args[0].equalsIgnoreCase("nogui")) {
			System.out.println("Starting CUI Mode!");
			System.out.println("UnImplements! Sorry!");
		} else {
			launch(args);
		}
	}
}
