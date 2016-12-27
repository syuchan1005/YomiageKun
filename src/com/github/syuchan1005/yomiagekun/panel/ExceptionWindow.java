package com.github.syuchan1005.yomiagekun.panel;

import com.github.syuchan1005.yomiagekun.util.Util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by syuchan on 2016/04/05.
 */
public class ExceptionWindow extends JFrame {
	private JPanel panel1;
	private JTextArea textArea1;
	private JButton CopyButton;

	public ExceptionWindow(Exception e, String... strs) {
		this.setTitle("!!!!Error!!!!");
		this.setSize(430, 180);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setContentPane(panel1);
		this.textArea1.setText(EtoString(e) + VtoString(strs));
		CopyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Util.setClipboard(textArea1.getText());
			}
		});
		Util.setLookAndFeel(this);
		this.setResizable(false);
		this.setVisible(true);
	}

	private static String VtoString(String... strs) {
		if (strs == null) return "";
		List<String> list = new ArrayList<>();
		list.addAll(Arrays.asList(strs));
		if (list.size() % 2 == 1) list.add("null");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size(); i += 2) {
			stringBuilder.append(list.get(i));
			stringBuilder.append(" : ");
			stringBuilder.append(list.get(i + 1));
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}

	private static String EtoString(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.flush();
		String[] strs = sw.toString().split("\n");
		return strs[0] + strs[1];
	}
}
