package com.github.syuchan1005.YomiageKun;

import com.github.syuchan1005.YomiageKun.panel.*;
import com.github.syuchan1005.YomiageKun.util.Speech;
import com.github.syuchan1005.YomiageKun.util.Util;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by syuchan on 2016/04/02.
 */
public class Window extends JFrame {
	private static Window window;
	private Properties APIKeyProperties;
	private JPanel panel1;
	private JTabbedPane tabbedPane1;
	private JPanel SkypeMainPanel;
	private JPanel StudyMainPanel;
	private JTabbedPane AccountTabPane;
	private JPanel SkypeAccountPanel;
	private JPanel DiscordMainPanel;
	private JPanel DiscordSettingPanel;
	private JPanel GeneralPanel;

	private Window() throws IOException {
		APIKeyProperties = new Properties();
		InputStream inputStream = this.getClass().getClassLoader().getResource("resources/APIKey.properties")
				.openConnection().getInputStream();
		APIKeyProperties.load(inputStream);
		inputStream.close();
		Speech.init(APIKeyProperties.getProperty("DocomoSpeechToText"));
		this.setFrameComponent();
		this.setFrameData();
		this.setLocation(50, 50);
		this.setWindowListener();
		Util.setLookAndFeel(this);
	}

	public void setFrameData() {
		this.setTitle("読み上げ君");
		this.setIconImage(Util.getImageIcon(this, "resources/icon.png"));
		this.setSize(800, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setFrameComponent() {
		this.add(panel1);
	}

	public void setWindowListener() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(new File("study.txt")));
					List<StudyMain.StudyContent> studyContentList = StudyMain.getStudyContentList();
					String line;
					while ((line = br.readLine()) != null) {
						String[] split = line.split("\t");
						if (split.length < 3) continue;
						if (split.length == 3) {
							studyContentList.add(new StudyMain.StudyContent(split[0], split[1], Integer.parseInt(split[2]), false));
						} else if (split.length == 4) {
							studyContentList.add(new StudyMain.StudyContent(split[0], split[1], Integer.parseInt(split[2]), Boolean.parseBoolean(split[3])));
						}
					}
					StudyMain.getInstance().sort();
				} catch (IOException e1) {
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					File file = new File("study.txt");
					file.createNewFile();
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
					for (StudyMain.StudyContent studyContent : StudyMain.getStudyContentList()) {
						String s = studyContent.getBeforeText() +
								"\t" + studyContent.getAfterText() +
								"\t" + studyContent.getPriority() +
								"\t" + studyContent.isReg();
						pw.println(s);
					}
					pw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}); // Save study
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(new File("skype.txt")));
					String line;
					while ((line = br.readLine()) != null) {
						SkypeSetting skypeSetting = SkypeSetting.getInstance();
						if (line.startsWith("userName:\t")) {
							skypeSetting.getSkypeUserField().setText(line.substring(10));
							skypeSetting.getStoredUserCheckBox().setSelected(true);
							continue;
						}
						if (line.startsWith("passWord:\t")) {
							skypeSetting.getSkypePassField().setText(line.substring(10));
							skypeSetting.getStoredPassCheckBox().setSelected(true);
							continue;
						}
						String[] split = line.split("\t");
						if (split.length != 2) continue;
						skypeSetting.addTableData(split[0], split[1]);
					}
				} catch (IOException e1) {
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					File file = new File("skype.txt");
					file.createNewFile();
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
					SkypeSetting skypeSetting = SkypeSetting.getInstance();
					String user = skypeSetting.getSkypeUser();
					String pass = skypeSetting.getSkypePass();
					if (user != null && user.length() > 0 && skypeSetting.getStoredUserCheckBox().isSelected())
						pw.println("userName:\t" + user);
					if (pass != null && pass.length() > 0 && skypeSetting.getStoredPassCheckBox().isSelected())
						pw.println("passWord:\t" + pass);
					for (Map.Entry<String, SkypeSetting.SkypeChatType> entry : SkypeSetting.getSkypeReadMap().entrySet()) {
						String s = entry.getKey() + "\t" + entry.getValue().getName();
						pw.println(s);
					}
					pw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}); // Save Skype
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				try {
					BufferedReader br = new BufferedReader(new FileReader(new File("discord.txt")));
					String line;
					while ((line = br.readLine()) != null) {
						DiscordSetting discordSetting = DiscordSetting.getInstance();
						if (line.startsWith("select:\t")) {
							boolean b = Boolean.parseBoolean(line.substring(8));
							discordSetting.getTokenRadioButton().setSelected(b);
							discordSetting.getEmailRadioButton().setSelected(!b);
							continue;
						}
						if (line.startsWith("token:\t")) {
							discordSetting.getDiscordTokenField().setText(line.substring(7));
							discordSetting.getStoredTokenCheckBox().setSelected(true);
							continue;
						}
						if (line.startsWith("email:\t")) {
							discordSetting.getEmailField().setText(line.substring(7));
							discordSetting.getStoredEmailCheckBox().setSelected(true);
							continue;
						}
						if (line.startsWith("pass:\t")) {
							discordSetting.getPasswordField().setText(line.substring(6));
							discordSetting.getStoredPasswordCheckBox().setSelected(true);
							continue;
						}
						if (line.startsWith("isSpeakInCall:\t")) {
							discordSetting.getIsSpeakInCallCheckBox().setSelected(Boolean.parseBoolean(line.substring(15)));
							continue;
						}
					}
				} catch (IOException e1) {
				}
			}

			@Override
			public void windowClosing(WindowEvent e) {
				try {
					File file = new File("discord.txt");
					file.createNewFile();
					PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
					DiscordSetting discordSetting = DiscordSetting.getInstance();
					pw.println("select:\t" + discordSetting.getTokenRadioButton().isSelected());
					String token = discordSetting.getDiscordTokenField().getText();
					if (token != null && token.length() > 0 && discordSetting.getStoredTokenCheckBox().isSelected()) pw.println("token:\t" + token);
					String email = discordSetting.getEmailField().getText();
					if (email != null && email.length() > 0 && discordSetting.getStoredEmailCheckBox().isSelected()) pw.println("email:\t" + email);
					String pass = new String(discordSetting.getPasswordField().getPassword());
					if (pass != null && pass.length() > 0 && discordSetting.getStoredPasswordCheckBox().isSelected()) pw.println("pass:\t" + pass);
					pw.println("isSpeakInCall:\t" + discordSetting.getIsSpeakInCallCheckBox().isSelected());
					pw.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}); // Save Discord
	}

	private void createUIComponents() {
		this.SkypeMainPanel = SkypeMain.getPanel();
		this.StudyMainPanel = StudyMain.getPanel();
		this.SkypeAccountPanel = SkypeSetting.getPanel();
		this.DiscordMainPanel = DiscordMain.getPanel();
		this.DiscordSettingPanel = DiscordSetting.getPanel();
		this.GeneralPanel = GeneralWindow.getPanel();
	}

	public static Window getInstance() {
		if (window == null) {
			try {
				window = new Window();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return window;
	}
}
