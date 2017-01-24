package com.github.syuchan1005.yomiagekun;

import javafx.stage.WindowEvent;

/**
 * Created by syuchan on 2016/12/30.
 */
public abstract class Controller {
	private YomiageKun yomiageKun;

	public YomiageKun getYomiageKun() {
		return yomiageKun;
	}

	public void setYomiageKun(YomiageKun yomiageKun) {
		this.yomiageKun = yomiageKun;
	}

	public void onClose(WindowEvent event) {}
}
