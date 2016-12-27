package com.github.syuchan1005.yomiagekun;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by syuchan on 2016/12/08.
 */
public class YomiageKun extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("YomiageKun");
		primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("fxmls/Main.fxml")), 800, 500));
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
