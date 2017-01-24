package com.github.syuchan1005.yomiagekun;

import com.github.syuchan1005.yomiagekun.controllers.DiscordController;
import com.github.syuchan1005.yomiagekun.controllers.GeneralController;
import com.github.syuchan1005.yomiagekun.controllers.LicenseController;
import com.github.syuchan1005.yomiagekun.controllers.SDiscordController;
import com.github.syuchan1005.yomiagekun.controllers.SSkypeController;
import com.github.syuchan1005.yomiagekun.controllers.SkypeController;
import com.github.syuchan1005.yomiagekun.controllers.StudyController;
import com.github.syuchan1005.yomiagekun.players.DiscordAudioPlayer;
import com.github.syuchan1005.yomiagekun.players.InlineAudioPlayer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
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
	@FXML
	private LicenseController licenseController;

	private static YomiageKun instance;
	private static InlineAudioPlayer inlineAudioPlayer = new InlineAudioPlayer();
	private static DiscordAudioPlayer discordAudioPlayer = new DiscordAudioPlayer();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		skypeController.setYomiageKun(this);
		discordController.setYomiageKun(this);
		studyController.setYomiageKun(this);
		generalController.setYomiageKun(this);
		sSkypeController.setYomiageKun(this);
		sDiscordController.setYomiageKun(this);
		licenseController.setYomiageKun(this);
		DiscordWrapper.setDiscordController(discordController);
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

	public LicenseController getLicenseController() {
		return licenseController;
	}

	public static YomiageKun getInstance() {
		return instance;
	}

	public static InlineAudioPlayer getInlineAudioPlayer() {
		return inlineAudioPlayer;
	}

	public static DiscordAudioPlayer getDiscordAudioPlayer() {
		return discordAudioPlayer;
	}

	public String studyText(String nick, String text, boolean isNickSpeak) {
		return (isNickSpeak ? nick : "") + "  " + text;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		primaryStage.setOnCloseRequest(event -> {
			skypeController.onClose(event);
			discordController.onClose(event);
			studyController.onClose(event);
			generalController.onClose(event);
			sSkypeController.onClose(event);
			sDiscordController.onClose(event);
			licenseController.onClose(event);
			SQLiteConnector.saveAll();
			try {
				Platform.exit();
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		primaryStage.setTitle("YomiageKun");
		URL resource = ClassLoader.getSystemResource("fxmls/Main.fxml");
		FXMLLoader fxmlLoader = new FXMLLoader(resource);
		fxmlLoader.setController(this);
		primaryStage.setScene(new Scene(fxmlLoader.load(), 800, 500));
		primaryStage.getIcons().add(new Image(ClassLoader.getSystemResourceAsStream("icon.png")));
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public static void main(String[] args) throws IOException {
		propertiesInit();
		if (args.length >= 1 && args[0].equalsIgnoreCase("nogui")) {
			System.out.println("Starting CUI Mode!");
			System.out.println("UnImplements! Sorry!");
		} else {
			launch(args);
		}
	}

	private static void propertiesInit() throws IOException {
		Properties api = new Properties();
		api.load(ClassLoader.getSystemResourceAsStream("APIKey.properties"));
		DocomoSpeech.init(api.getProperty("DocomoTTS"));
	}
}
