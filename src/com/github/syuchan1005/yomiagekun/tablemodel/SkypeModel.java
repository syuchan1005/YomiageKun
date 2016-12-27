package com.github.syuchan1005.yomiagekun.tablemodel;

import javax.swing.table.DefaultTableModel;

/**
 * Created by syuchan on 2016/04/03.
 */
public class SkypeModel extends DefaultTableModel {
	private static String[] columns = {"DisplayName", "Target"};

	public SkypeModel() {
		super(columns, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public static String[] getColumns() {
		return columns;
	}
}
