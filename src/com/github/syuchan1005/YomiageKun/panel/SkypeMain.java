package com.github.syuchan1005.YomiageKun.panel;

import com.github.syuchan1005.YomiageKun.util.Speech;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.SkypeBuilder;
import com.samczsun.skype4j.chat.GroupChat;
import com.samczsun.skype4j.chat.IndividualChat;
import com.samczsun.skype4j.events.EventHandler;
import com.samczsun.skype4j.events.Listener;
import com.samczsun.skype4j.events.chat.message.MessageReceivedEvent;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.exceptions.SkypeException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Created by syuchan on 2016/04/03.
 */
public class SkypeMain {
	private JPanel panel1;
	private JTextArea skypeLogTextArea;
	private JButton lunchButton;
	private JButton stopButton;
	private static Skype skype;

	private static SkypeMain skypeMain = new SkypeMain();

	private SkypeMain() {
		lunchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SkypeSetting account = SkypeSetting.getInstance();
				String skypeUser = account.getSkypeUser();
				String skypePass = account.getSkypePass();
				if (skypeUser == null || skypeUser.length() == 0||
						skypePass == null || skypePass.length() == 0) {
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
				lunchButton.setEnabled(false);
				stopButton.setEnabled(true);
				skypeSpeech("", "Skype読み上げが起動しました");
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
				lunchButton.setEnabled(true);
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
			if (text.startsWith("\\")) return;
			skypeSpeech(sender, text);
		}
	}

	public static void skypeSpeech(String sender, String text) {
		JTextArea skypeLogTextArea = SkypeMain.getInstance().getSkypeLogTextArea();
		skypeLogTextArea.append(sender + ": " + text + "\n");
		skypeLogTextArea.setCaretPosition(skypeLogTextArea.getText().length());
		Speech.speakFemale(StudyMain.getInstance().replace(sender, text));
	}
}
