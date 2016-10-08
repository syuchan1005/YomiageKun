package com.github.syuchan1005.YomiageKun.panel;

import com.github.syuchan1005.YomiageKun.util.SSML;
import com.github.syuchan1005.YomiageKun.util.Speech;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by syuchan on 2016/08/23.
 */
public class DiscordMain extends Thread {
	private static AudioFormat audioFormat = new AudioFormat(16000f, 16, 1, true, true);
	private static Queue<byte[]> queue = new ArrayDeque<>();
	private static DiscordMain instance = new DiscordMain();
	private IDiscordClient discordClient;
	private List<IVoiceChannel> voiceChannels = new ArrayList<>();
	private JPanel mainPanel;
	private JTextArea discordLogArea;
	private JButton startButton;
	private JButton stopButton;

	private DiscordMain() {
		instance = this;
		instance.start();
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				String token = DiscordSetting.getInstance().getDiscordTokenField().getText();
				if (token == null || token.length() < 1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "Tokenが足りません.\nSetting->Discordより入力してください");
					return;
				}
				if (discordClient == null) {
					final ClientBuilder clientBuilder = new ClientBuilder().withToken(token);
					try {
						startButton.setEnabled(false);
						stopButton.setEnabled(true);
						discordClient = clientBuilder.login();
					} catch (DiscordException e1) {
						e1.printStackTrace();
						startButton.setEnabled(true);
						stopButton.setEnabled(false);
					}
					EventDispatcher dispatcher = discordClient.getDispatcher();
					dispatcher.registerListener(new IListener<MessageReceivedEvent>() {
						@Override
						public void handle(MessageReceivedEvent event) {
							IMessage message = event.getMessage();
							if (startButton.isEnabled()) return;
							try {
								String content = message.getContent();
								if (content.equalsIgnoreCase("VoiceStart")) {
									IVoiceChannel channel = message.getAuthor().getConnectedVoiceChannels().get(0);
									voiceChannels.add(channel);
									try {
										channel.join();
									} catch (MissingPermissionsException e1) {
										try {
											message.getChannel().sendMessage("\\通話に参加出来ませんでした(ち＞ω＜ぇ)/");
										} catch (MissingPermissionsException | RateLimitException | DiscordException e2) {
											e2.printStackTrace();
										}
									}
								}
								if (content.equalsIgnoreCase("VoiceLeave")) {
									IVoiceChannel channel = message.getAuthor().getConnectedVoiceChannels().get(0);
									voiceChannels.remove(channel);
									channel.leave();
								}
								discordSpeech(message.getAuthor().getName(), content);
							} catch (RestApiException e1) {
								try {
									message.reply("\\正常に読み上げが出来ませんでした(ち＞ω＜ぇ)/");
								} catch (MissingPermissionsException | RateLimitException | DiscordException e2) {
									e2.printStackTrace();
								}
							}
						}
					});
				}
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					discordClient.logout();
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
				} catch (RateLimitException | DiscordException e1) {
					e1.printStackTrace();
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
				}
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

	public void discordSpeech(String sender, String text) throws RestApiException {
		JTextArea discordLogArea = DiscordMain.getInstance().getDiscordLogArea();
		discordLogArea.append(sender + ": " + text);
		String rep = StudyMain.getInstance().replace(sender, text);
		if (GeneralWindow.getInstance().isDebugMode()) discordLogArea.append("(" + rep + ")");
		discordLogArea.append("\n");
		discordLogArea.setCaretPosition(discordLogArea.getText().length());
		if (text.startsWith("\\")) return;
		if (DiscordSetting.getInstance().getIsSpeakInCallCheckBox().isSelected()) {
			queue.add(Speech.speakFemale(SSML.convert(rep)));
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(100);
				byte[] bytes = queue.poll();
				if (bytes == null) continue;
				AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(bytes), audioFormat, bytes.length);
				for (IVoiceChannel voiceChannel : voiceChannels) {
					AudioPlayer audioPlayer = AudioPlayer.getAudioPlayerForGuild(voiceChannel.getGuild());
					audioPlayer.queue(audioInputStream);
				}
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
