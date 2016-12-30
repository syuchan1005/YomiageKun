package com.github.syuchan1005.yomiagekun.contents;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by syuchan on 2016/12/31.
 */
public class LicenseContent {
	private SimpleStringProperty libraryName;
	private SimpleStringProperty license;

	public LicenseContent(String libraryName, String license) {
		this.libraryName = new SimpleStringProperty(libraryName);
		this.license = new SimpleStringProperty(license);
	}

	public String getLibraryName() {
		return libraryName.get();
	}

	public void setLibraryName(String libraryName) {
		this.libraryName.set(libraryName);
	}

	public String getLicense() {
		return license.get();
	}

	public void setLicense(String license) {
		this.license.set(license);
	}
}
