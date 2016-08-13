package com.github.syuchan1005.YomiageKun.panel;

import com.github.syuchan1005.YomiageKun.util.Speech;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by syuchan on 2016/08/12.
 */
public class GeneralWindow {
	private static GeneralWindow generalWindow = new GeneralWindow();
	private JPanel mainPanel;
	private JButton speechTestButton;
	private JTextField speechText;
	private JCheckBox debugModeCheckBox;

	private GeneralWindow() {
		speechTestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Speech.speakFemale(speechText.getText());
			}
		});
	}

	public static JPanel getPanel() {
		return generalWindow.mainPanel;
	}

	public static GeneralWindow getInstance() {
		return generalWindow;
	}

	public boolean isDebugMode() {
		return debugModeCheckBox.isSelected();
	}
}
