package com.github.syuchan1005.yomiagekun.panel;

import com.github.syuchan1005.yomiagekun.util.SSML;
import com.github.syuchan1005.yomiagekun.util.Speech;
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
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

/**
 * Created by syuchan on 2016/08/23.
 */
public class DiscordMain extends Thread {
	private static AudioFormat audioFormat = new AudioFormat(16000f, 16, 1, true, true);
	private static Queue<byte[]> queue = new ArrayDeque<>();
	public static String lastUsername = "";
	private static DiscordMain instance = new DiscordMain();
	private IDiscordClient discordClient;
	private Set<IVoiceChannel> voiceChannels = new HashSet<>();
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
				if (discordClient == null) {
					ClientBuilder clientBuilder = new ClientBuilder();
					DiscordSetting discordSetting = DiscordSetting.getInstance();
					if (discordSetting.getTokenRadioButton().isSelected()) {
						clientBuilder.withToken(discordSetting.getDiscordTokenField().getText());
					} else {
						clientBuilder.withLogin(discordSetting.getEmailField().getText(), new String(discordSetting.getPasswordField().getPassword()));
					}
					try {
						startButton.setEnabled(false);
						stopButton.setEnabled(true);
						discordClient = clientBuilder.build();
					} catch (DiscordException e1) {
						e1.printStackTrace();
						startButton.setEnabled(true);
						stopButton.setEnabled(false);
					}
				}
				try {
					startButton.setEnabled(false);
					stopButton.setEnabled(true);
					discordClient.login();
				} catch (DiscordException e1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "うまくログインできませんでした. Settings -> Discordより設定を確認してください");
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
				}
				EventDispatcher dispatcher = discordClient.getDispatcher();
				dispatcher.registerListener(new IListener<MessageReceivedEvent>() {
					@Override
					public void handle(MessageReceivedEvent event) {
						IMessage message = event.getMessage();
						if (startButton.isEnabled()) return;
						String topic = message.getChannel().getTopic();
						if (topic != null && topic.contains("!read")) return;
						if (event.getMessage().getAuthor().isBot()) return;
						try {
							String content = message.getContent();
							if (content.equalsIgnoreCase("!join")) {
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
							if (content.equalsIgnoreCase("!leave")) {
								IVoiceChannel channel = message.getAuthor().getConnectedVoiceChannels().get(0);
								voiceChannels.remove(channel);
								channel.leave();
							}
							Optional<String> nicknameForGuild = message.getAuthor().getNicknameForGuild(message.getGuild());
							String nick = nicknameForGuild.isPresent() ? nicknameForGuild.get() : message.getAuthor().getName();
							discordSpeech(nick, content);
						} catch (RestApiException e1) {
							/*
							try {
								message.reply("\\正常に読み上げが出来ませんでした(ち＞ω＜ぇ)/");
							} catch (MissingPermissionsException | RateLimitException | DiscordException e2) {
								e2.printStackTrace();
							}
							*/
						}
					}
				});
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
		text = text.replaceAll("<:(\\w+):\\d+>", ":$1:");
		discordLogArea.append(sender + ": " + text);
		String rep = StudyMain.getInstance().getReadingText(sender, text, !lastUsername.equalsIgnoreCase(sender));
		lastUsername = sender;
		if (GeneralWindow.getInstance().isDebugMode()) discordLogArea.append("(" + rep + ")");
		discordLogArea.append("\n");
		discordLogArea.setCaretPosition(discordLogArea.getText().length());
		if (text.startsWith("\\") || text.startsWith("!") || text.startsWith("`")) return;
		if (DiscordSetting.getInstance().getIsSpeakInCallCheckBox().isSelected()) {
			String convert = SSML.convert("(maki)" + rep);
			queue.add(Speech.speak(null, convert, true));
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
