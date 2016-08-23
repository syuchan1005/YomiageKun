package com.github.syuchan1005.YomiageKun.panel;

import javax.swing.*;

/**
 * Created by syuchan on 2016/08/23.
 */
public class DiscordSetting {
	private static DiscordSetting instance = new DiscordSetting();
	private JPanel mainPanel;
	private JTextField tokenField;
	private JCheckBox storedCheckBox;

	private DiscordSetting() {

	}

	public static DiscordSetting getInstance() {
		return instance;
	}

	public static JPanel getPanel() {
		return instance.mainPanel;
	}

	public JTextField getDiscordTokenField() {
		return tokenField;
	}

	public JCheckBox getStoredCheckBox() {
		return storedCheckBox;
	}
}
