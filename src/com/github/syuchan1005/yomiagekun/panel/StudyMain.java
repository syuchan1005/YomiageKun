package com.github.syuchan1005.yomiagekun.panel;

import com.github.syuchan1005.yomiagekun.tablemodel.StudyModel;
import com.github.syuchan1005.yomiagekun.util.Util;
import jp.hishidama.eval.EvalException;
import jp.hishidama.eval.ExpRuleFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syuchan on 2016/04/03.
 */
public class StudyMain {
	private JPanel panel1;
	private JButton AddButton;
	private JButton DelButton;
	private JTable studyTable;
	private JTextField beforeField;
	private JTextField afterField;
	private JFormattedTextField priorityField;
	private JScrollPane studyScrollPane;
	private JButton bouyomiButton;
	private JButton bouyomiRegButton;
	private JCheckBox isRegenCheckBox;

	private static JFileChooser jFileChooser = new JFileChooser();
	private static StudyMain studyMain;
	private static StudyModel studyModel;
	private static List<StudyContent> studyContentList;
	private static Pattern pattern = Pattern.compile("(教育|忘却)\\((.*)\\)");

	private StudyMain() {
		studyContentList = new ArrayList<>();
		this.studyTable.getTableHeader().setReorderingAllowed(false);
		this.setTableModel();
		this.setButtonListener();
	}

	public static StudyMain getInstance() {
		if (studyMain == null) studyMain = new StudyMain();
		return studyMain;
	}

	public static JPanel getPanel() {
		return getInstance().panel1;
	}

	public void setTableModel() {
		studyModel = new StudyModel();
		this.studyTable.setModel(studyModel);
	}

	public void setButtonListener() {
		AddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (beforeField.getText().length() <= 0 ||
						afterField.getText().length() <= 0 ||
						!Util.isInt(priorityField.getText())) return;
				addListData(beforeField.getText(), afterField.getText(),
						Integer.parseInt(priorityField.getText()), isRegenCheckBox.isSelected());
				beforeField.setText("");
				afterField.setText("");
				priorityField.setText("");
			}
		});
		DelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = studyTable.getSelectedRows();
				for (int i = rows.length - 1; i > -1; i--) {
					studyContentList.remove(rows[i]);
				}
				sort();
			}
		});
		bouyomiButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFileChooser.showOpenDialog(((Component) e.getSource()));
				if (jFileChooser.getFileSelectionMode() != JFileChooser.APPROVE_OPTION) return;
				try {
					BufferedReader br = new BufferedReader(new FileReader(jFileChooser.getSelectedFile()));
					List<StudyMain.StudyContent> studyContentList = StudyMain.getStudyContentList();
					String line;
					while ((line = br.readLine()) != null) {
						String[] split = line.split("\t");
						if (split.length != 4) continue;
						studyContentList.add(new StudyMain.StudyContent(split[2], split[3], Integer.parseInt(split[0]), false));
					}
					StudyMain.getInstance().sort();
				} catch (IOException e1) {
				}
			}
		});
		bouyomiRegButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jFileChooser.showOpenDialog(((Component) e.getSource()));
				if (jFileChooser.getFileSelectionMode() != JFileChooser.APPROVE_OPTION) return;
				try {
					BufferedReader br = new BufferedReader(new FileReader(jFileChooser.getSelectedFile()));
					List<StudyMain.StudyContent> studyContentList = StudyMain.getStudyContentList();
					String line;
					while ((line = br.readLine()) != null) {
						String[] split = line.split("\t");
						if (split.length != 4) continue;
						studyContentList.add(new StudyMain.StudyContent(split[2], split[3], Integer.parseInt(split[0]), true));
					}
					StudyMain.getInstance().sort();
				} catch (IOException e1) {
				}
			}
		});
	}

	public void addListData(String before, String after, int priority, boolean isReg) {
		if (!isReg) before = before.toUpperCase();
		removeListData(before);
		studyContentList.add(new StudyContent(before, after, Integer.valueOf(priority), isReg));
		sort();
	}

	public void removeListData(String before) {
		before = before.toUpperCase();
		if (studyContentList.size() == 0) return;
		for (int i = studyContentList.size() - 1; i > -1; i--) {
			if (studyContentList.get(i).getBeforeText().equals(before)) {
				studyContentList.remove(i);
				break;
			}
		}
		sort();
	}

	public void sort() {
		Collections.sort(studyContentList, new Comparator<StudyContent>() {
			@Override
			public int compare(StudyContent s1, StudyContent s2) {
				int priority = s2.getPriority() - s1.getPriority();
				if (priority == 0) return s1.getBeforeText().compareTo(s2.getBeforeText());
				return priority;
			}
		});
		studyModel.removeAll();
		for (StudyContent s : studyContentList) {
			studyModel.addRow(new Object[]{s.getBeforeText(), s.getAfterText(), s.getPriority(), s.isReg()});
		}
	}

	public static class StudyContent {
		private String beforeText;
		private String afterText;
		private int priority;
		private boolean isReg;

		public StudyContent(String beforeText, String afterText, int priority, boolean isReg) {
			this.beforeText = beforeText;
			this.afterText = afterText;
			this.priority = priority;
			this.isReg = isReg;
		}

		public String getBeforeText() {
			return beforeText;
		}

		public void setBeforeText(String beforeText) {
			this.beforeText = beforeText;
		}

		public String getAfterText() {
			return afterText;
		}

		public void setAfterText(String afterText) {
			this.afterText = afterText;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public boolean isReg() {
			return isReg;
		}

		public void setReg(boolean reg) {
			isReg = reg;
		}
	}

	public static List<StudyContent> getStudyContentList() {
		return studyContentList;
	}

	public String getReadingText(String user, String text, boolean isUserSpeak) {
		if (isUserSpeak) {
			user = studyReplace(user.toUpperCase());
			user += " ";
		} else {
			user = "";
		}
		Matcher matcher = pattern.matcher(text);
		if (matcher.find()) {
			switch (matcher.group(1)) {
				case "教育":
					String group = matcher.group(2);
					boolean isReg = false;
					if (group.contains("==")) {
						group = group.replace("==", "=");
					} else if (group.contains("=R")) {
						isReg = true;
						group = group.replace("=R", "=");
					} else if (!group.contains("=")) {
						break;
					}
					String[] split = group.split("=");
					if (split.length != 2) break;
					if (split[0].length() == 0 || split[1].length() == 0) break;
					if (split[1].length() > 20) return "20文字以上は教育できません";
					addListData(split[0], split[1], 0, isReg);
					return user + split[0] + (isReg ? "を正規表現で" : "を") + split[1] + "と覚えました";
				case "忘却":
					removeListData(matcher.group(2));
					return user + matcher.group(2) + "を忘れました";
			}
		}
		if (text.startsWith("=")) {
			try {
				text = String.valueOf(ExpRuleFactory.getDefaultRule().parse(text.substring(1)).evalDouble());
			} catch (EvalException e) {	}
			return user + text;
		}
		text = text.toUpperCase();
		text = text.replaceAll("HTTPS?:\\/\\/.*", "URL省略");
		text = studyReplace(text);
		return user + text;
	}

	private String studyReplace(String str) {
		for (StudyContent studyContent : studyContentList) {
			String beforeText = studyContent.getBeforeText();
			if (!studyContent.isReg()) beforeText = Pattern.quote(beforeText);
			str = str.replaceAll(beforeText, studyContent.afterText);
		}
		return str;
	}

	private void createUIComponents() {
		this.priorityField = new JFormattedTextField(NumberFormat.getIntegerInstance());
	}
}
