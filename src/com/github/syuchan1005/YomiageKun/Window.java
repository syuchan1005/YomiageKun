package com.github.syuchan1005.YomiageKun;

import com.github.syuchan1005.YomiageKun.panel.GeneralWindow;
import com.github.syuchan1005.YomiageKun.panel.SkypeSetting;
import com.github.syuchan1005.YomiageKun.panel.StudyMain;
import com.github.syuchan1005.YomiageKun.panel.SkypeMain;
import com.github.syuchan1005.YomiageKun.util.Util;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Created by syuchan on 2016/04/02.
 */
public class Window extends JFrame {
	private JPanel panel1;
		private JTabbedPane tabbedPane1;
			private JPanel SkypeMainPanel;
			private JPanel StudyMainPanel;
			private JTabbedPane AccountTabPane;
				private JPanel SkypeAccountPanel;
	private JPanel GeneralPanel;

	public Window() {
		this.setFrameComponent();
		this.setFrameData();
		this.setLocation(50, 50);
		this.setWindowListener();
		Util.setLookAndFeel(this);
		this.setVisible(true);
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
						if (line.startsWith("userName:\t")) {
							SkypeSetting.getInstance().getSkypeUserField().setText(line.substring(10));
							continue;
						}
						String[] split = line.split("\t");
						if (split.length != 2) continue;
						SkypeSetting.getInstance().addTableData(split[0], split[1]);
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
					String user = SkypeSetting.getInstance().getSkypeUser();
					if (user != null && user.length() > 0) pw.println("userName:\t" + user);
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
	}

	private void createUIComponents() {
		this.SkypeMainPanel = SkypeMain.getPanel();
		this.StudyMainPanel = StudyMain.getPanel();
		this.SkypeAccountPanel = SkypeSetting.getPanel();
		this.GeneralPanel = GeneralWindow.getPanel();
	}
}
