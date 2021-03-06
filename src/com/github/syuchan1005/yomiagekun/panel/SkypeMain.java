package com.github.syuchan1005.yomiagekun.panel;

import com.github.syuchan1005.yomiagekun.util.SSML;
import com.github.syuchan1005.yomiagekun.util.Speech;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.chat.IndividualChat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.SkypeException;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by syuchan on 2016/04/03.
 */
public class SkypeMain {
	public static String lastUsername = "";
	private JPanel panel1;
	private JTextArea skypeLogTextArea;
	private JButton startButton;
	private JButton stopButton;
	private static Skype skype;

	private static SkypeMain skypeMain = new SkypeMain();

	private SkypeMain() {
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkypeSetting account = SkypeSetting.getInstance();
				String skypeUser = account.getSkypeUser();
				String skypePass = account.getSkypePass();
				if (skypeUser == null || skypeUser.length() < 1||
						skypePass == null || skypePass.length() < 1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "UserNameかPassWordが足りません.\nSetting->Skypeより入力してください");
					return;
				}
				skype = new SkypeBuilder(skypeUser, skypePass).withAllResources().build();
				try {
					skype.login();
					skype.getEventDispatcher().registerListener(new MessageReceived());
					skype.subscribe();
				} catch (SkypeException e1) {
					new ExceptionWindow(e1, "Login Failed");
					e1.printStackTrace();
				}
				startButton.setEnabled(false);
				stopButton.setEnabled(true);
				try {
					skypeSpeech("", "Skype読み上げが起動しました");
				} catch (RestApiException e1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "正常に読み上げが出来ませんでした");
				}
			}
		});
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					skype.logout();
				} catch (ConnectionException e1) {
					new ExceptionWindow(e1, "Logout Failed");
					e1.printStackTrace();
				}
				startButton.setEnabled(true);
				stopButton.setEnabled(false);
			}
		});
		stopButton.setEnabled(false);
	}

	public static SkypeMain getInstance() {
		return skypeMain;
	}

	public static JPanel getPanel() {
		return skypeMain.panel1;
	}

	public JTextArea getSkypeLogTextArea() {
		return skypeLogTextArea;
	}

	static class MessageReceived implements Listener {
		@EventHandler
		public void onMessage(MessageReceivedEvent e) throws ConnectionException {
			Map<String, SkypeSetting.SkypeChatType> skypeReadMap = SkypeSetting.getSkypeReadMap();
			if (e.getChat() instanceof GroupChat) {
				GroupChat chat = (GroupChat) e.getChat();
				SkypeSetting.SkypeChatType skypeChatType = skypeReadMap.get(chat.getTopic());
				if (skypeChatType != SkypeSetting.SkypeChatType.GROUP && skypeChatType != SkypeSetting.SkypeChatType.BOTH) return;
			} else if (e.getChat() instanceof IndividualChat) {
				IndividualChat chat = (IndividualChat) e.getChat();
				SkypeSetting.SkypeChatType skypeChatType = skypeReadMap.get(chat.getPartner().getUsername());
				if (skypeChatType != SkypeSetting.SkypeChatType.PRIVATE && skypeChatType != SkypeSetting.SkypeChatType.BOTH) return;
			}
			String sender = e.getMessage().getSender().getDisplayName();
			String text = e.getMessage().getContent().asPlaintext();
			try {
				skypeSpeech(sender, text);
			} catch (RestApiException e1) {
				e.getChat().sendMessage("\\正常に読み上げが出来ませんでした(ち＞ω＜ぇ)/");
			}
		}
	}

	public static void skypeSpeech(String sender, String text) throws RestApiException {
		JTextArea skypeLogTextArea = SkypeMain.getInstance().getSkypeLogTextArea();
		skypeLogTextArea.append(sender + ": " + text);
		String rep = StudyMain.getInstance().getReadingText(sender, text, !lastUsername.equalsIgnoreCase(sender));
		lastUsername = sender;
		if (GeneralWindow.getInstance().isDebugMode()) skypeLogTextArea.append("(" + rep + ")");
		skypeLogTextArea.append("\n");
		skypeLogTextArea.setCaretPosition(skypeLogTextArea.getText().length());
		if (text.startsWith("\\") || text.startsWith("!")) return;
		Speech.speakFemale(SSML.convert(rep));
	}
}
