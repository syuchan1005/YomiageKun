package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.DocomoSpeech;
import com.github.syuchan1005.yomiagekun.players.InlineAudioPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;

/**
 * Created by syuchan on 2016/12/08.
 */
public class GeneralController extends Controller {
	private static InlineAudioPlayer inlineAudioPlayer = new InlineAudioPlayer();

	@FXML
	private TextField textField;
	@FXML
	private Button testButton;
	@FXML
	private Button discordTestButton;


	public void textClickAction(ActionEvent event) throws RestApiException {
		inlineAudioPlayer.addQueue(DocomoSpeech.speak(DocomoSpeech.Speaker.MAKI, textField.getText(), true));
	}
}
