package com.github.syuchan1005.yomiagekun.contents;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by syuchan on 2016/12/12.
 */
public class ReadContent {
	private SimpleStringProperty userName;
	private SimpleStringProperty text;
	private SimpleStringProperty readText;

	public ReadContent(String userName, String text, String readText) {
		this.userName = new SimpleStringProperty(userName);
		this.text = new SimpleStringProperty(text);
		this.readText = new SimpleStringProperty(readText);
	}

	public String getUserName() {
		return userName.get();
	}

	public void setUserName(String userName) {
		this.userName.set(userName);
	}

	public String getText() {
		return text.get();
	}

	public void setText(String text) {
		this.text.set(text);
	}

	public String getReadText() {
		return readText.get();
	}

	public void setReadText(String readText) {
		this.readText.set(readText);
	}
}
