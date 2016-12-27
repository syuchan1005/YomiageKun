package com.github.syuchan1005.yomiagekun.tablemodel;

import javax.swing.table.DefaultTableModel;

/**
 * Created by syuchan on 2016/04/03.
 */
public class StudyModel extends DefaultTableModel {
	private static String[] columns = {"BeforeText", "AfterText", "Priority", "isReg"};

	public StudyModel() {
		super(columns, 0);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public void removeAll() {
		this.setRowCount(0);
	}

}
