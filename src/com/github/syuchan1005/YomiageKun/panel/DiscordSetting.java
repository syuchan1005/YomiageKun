package com.github.syuchan1005.YomiageKun.panel;

import javax.swing.*;

/**
 * Created by syuchan on 2016/08/23.
 */
public class DiscordSetting {
	private static DiscordSetting instance = new DiscordSetting();
	private JPanel mainPanel;
	private JRadioButton tokenRadioButton;
	private JTextField tokenField;
	private JCheckBox storedTokenCheckBox;
	private JRadioButton emailRadioButton;
	private JTextField emailField;
	private JPasswordField passwordField;
	private JCheckBox storedEmailCheckBox;
	private JCheckBox storedPasswordCheckBox;
	private JCheckBox isSpeakInCallCheckBox;

	private DiscordSetting() {

	}

	public static DiscordSetting getInstance() {
		return instance;
	}

	public static JPanel getPanel() {
		return instance.mainPanel;
	}

	public JRadioButton getTokenRadioButton() {
		return tokenRadioButton;
	}

	public JTextField getTokenField() {
		return tokenField;
	}

	public JRadioButton getEmailRadioButton() {
		return emailRadioButton;
	}

	public JTextField getEmailField() {
		return emailField;
	}

	public JPasswordField getPasswordField() {
		return passwordField;
	}

	public JCheckBox getStoredEmailCheckBox() {
		return storedEmailCheckBox;
	}

	public JCheckBox getStoredPasswordCheckBox() {
		return storedPasswordCheckBox;
	}

	public JTextField getDiscordTokenField() {
		return tokenField;
	}

	public JCheckBox getStoredTokenCheckBox() {
		return storedTokenCheckBox;
	}

	public JCheckBox getIsSpeakInCallCheckBox() {
		return isSpeakInCallCheckBox;
	}
}
