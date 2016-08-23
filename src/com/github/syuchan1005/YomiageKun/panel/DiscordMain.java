package com.github.syuchan1005.YomiageKun.panel;

import com.github.syuchan1005.YomiageKun.util.Speech;
import com.google.common.util.concurrent.FutureCallback;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by syuchan on 2016/08/23.
 */
public class DiscordMain {
	private static DiscordMain instance = new DiscordMain();
	private DiscordAPI discordAPI;
	private JPanel mainPanel;
	private JTextArea discordLogArea;
	private JButton startButton;
	private JButton stopButton;

	private DiscordMain() {
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String token = DiscordSetting.getInstance().getDiscordTokenField().getText();
				if (token == null || token.length() < 1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "Tokenが足りません.\nSetting->Discordより入力してください");
					return;
				}
				if (discordAPI == null) {
					discordAPI = Javacord.getApi(token, true);
					discordAPI.connect(new FutureCallback<DiscordAPI>() {
						@Override
						public void onSuccess(DiscordAPI api) {
							api.registerListener(new MessageCreateListener() {
								@Override
								public void onMessageCreate(DiscordAPI api, Message message) {
									if (startButton.isEnabled()) return;
									try {
										discordSpeech(message.getAuthor().getName(), message.getContent());
									} catch (RestApiException e1) {
										message.reply("\\正常に読み上げが出来ませんでした(ち＞ω＜ぇ)/");
									}
								}
							});
						}

						@Override
						public void onFailure(Throwable t) {
							t.printStackTrace();
						}
					});
				}
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
		stopButton.setEnabled(false);
	}

	public static DiscordMain getInstance() {
		return instance;
	}

	public static JPanel getPanel() {
		return instance.mainPanel;
	}

	public JTextArea getDiscordLogArea() {
		return discordLogArea;
	}

	public static void discordSpeech(String sender, String text) throws RestApiException {
		JTextArea discordLogArea = DiscordMain.getInstance().getDiscordLogArea();
		discordLogArea.append(sender + ": " + text);
		String rep = StudyMain.getInstance().replace(sender, text);
		if (GeneralWindow.getInstance().isDebugMode()) discordLogArea.append("(" + rep + ")");
		discordLogArea.append("\n");
		discordLogArea.setCaretPosition(discordLogArea.getText().length());
		if (text.startsWith("\\")) return;
		Speech.speakFemale(rep);
	}
}
