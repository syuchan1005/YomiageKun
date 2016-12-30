package com.github.syuchan1005.yomiagekun;

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
}
