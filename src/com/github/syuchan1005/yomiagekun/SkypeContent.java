package com.github.syuchan1005.yomiagekun;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by syuchan on 2016/12/12.
 */
public class SkypeContent {
	private SimpleStringProperty name;
	private SimpleStringProperty target;

	public SkypeContent(String name, String target) {
		this.name = new SimpleStringProperty(name);
		this.target = new SimpleStringProperty(target);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public String getTarget() {
		return target.get();
	}

	public void setTarget(String target) {
		this.target.set(target);
	}
}
