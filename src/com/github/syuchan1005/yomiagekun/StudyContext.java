package com.github.syuchan1005.yomiagekun;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by syuchan on 2016/12/12.
 */
public class StudyContext {
	private SimpleStringProperty beforeText;
	private SimpleStringProperty afterText;
	private SimpleIntegerProperty priority;
	private SimpleBooleanProperty isRegex;

	public StudyContext(String beforeText, String afterText, int priority, boolean isRegex) {
		this.beforeText = new SimpleStringProperty(beforeText);
		this.afterText = new SimpleStringProperty(afterText);
		this.priority = new SimpleIntegerProperty(priority);
		this.isRegex = new SimpleBooleanProperty(isRegex);
	}

	public String getBeforeText() {
		return beforeText.get();
	}

	public void setBeforeText(String beforeText) {
		this.beforeText.set(beforeText);
	}

	public String getAfterText() {
		return afterText.get();
	}

	public void setAfterText(String afterText) {
		this.afterText.set(afterText);
	}

	public int getPriority() {
		return priority.get();
	}

	public void setPriority(int priority) {
		this.priority.set(priority);
	}

	public boolean isRegex() {
		return isRegex.get();
	}

	public void setRegex(boolean regex) {
		isRegex.set(regex);
	}

	@Override
	public String toString() {
		return "StudyContext{" +
				"beforeText=" + beforeText +
				", afterText=" + afterText +
				", priority=" + priority +
				", isRegex=" + isRegex +
				'}';
	}
}
