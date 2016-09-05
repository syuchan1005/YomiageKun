package com.github.syuchan1005.YomiageKun.util;

import com.github.syuchan1005.YomiageKun.panel.ExceptionWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by syuchan on 2016/04/02.
 */
public class Util {
	public static Image getImageIcon(JFrame frame, String path) {
		return new ImageIcon(frame.getClass().getClassLoader().getResource(path)).getImage();
	}

	public static String getURLTitle(String URL) {
		try {
			URLConnection conn = new URL(URL).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Arrays.asList(Arrays.asList(conn.getContentType().split(";")).get(1).split("=")).get(1)));
			StringBuffer response = new StringBuffer();
			String line;
			while ((line = in.readLine()) != null) response.append(line + "\n");
			in.close();
			Pattern title_pattern1 = Pattern.compile("<title>([^<]+)</title>", Pattern.CASE_INSENSITIVE);
			Matcher matcher1 = title_pattern1.matcher(response.toString());
			if (matcher1.find()) {
				String text = matcher1.group(1);
				if (text.length() > 10) text = text.substring(0, 10) + "以下略";
				return text;
			}
		} catch (Exception e) {
			new ExceptionWindow(e, "");
		}
		return "省略";
	}

	public static boolean isInt(String str) {
		try {
			Integer.valueOf(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void setClipboard(String text) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
	}

	public static void setLookAndFeel(JFrame jFrame) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(jFrame);
	}
}
