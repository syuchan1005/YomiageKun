package com.github.syuchan1005.yomiagekun.panel;

import com.github.syuchan1005.yomiagekun.Window;
import com.github.syuchan1005.yomiagekun.util.Speech;
import jp.ne.docomo.smt.dev.common.exception.RestApiException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by syuchan on 2016/08/12.
 */
public class GeneralWindow {
	private static GeneralWindow generalWindow = new GeneralWindow();
	private JPanel mainPanel;
	private JButton speechTestButton;
	private JTextField speechText;
	private JCheckBox debugModeCheckBox;
	private JButton discordButton;

	private GeneralWindow() {
		speechText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					speechTestButton.doClick();
				}
			}
		});

		speechTestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					Speech.speakFemale(speechText.getText());
					speechText.setText("");
				} catch (RestApiException e1) {
					JOptionPane.showMessageDialog(((Component) e.getSource()), "正常に読み上げが出来ませんでした");
				}
			}
		});

		debugModeCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window instance = Window.getInstance();
				if (isDebugMode()) instance.setTitle("読み上げ君(DebugMode)");
				else instance.setTitle("読み上げ君");
			}
		});
		discordButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DiscordMain.getInstance().discordSpeech("", speechText.getText());
				} catch (RestApiException e1) {
					new ExceptionWindow(e1);
				}
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
