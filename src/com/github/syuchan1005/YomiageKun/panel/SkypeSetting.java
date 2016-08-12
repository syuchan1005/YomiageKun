package com.github.syuchan1005.YomiageKun.panel;

import com.github.syuchan1005.YomiageKun.tablemodel.SkypeModel;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syuchan on 2016/04/03.
 */
public class SkypeSetting {
	private JPanel panel1;
		private JButton AddButton;
		private JButton DelButton;
		private JTextField skypeUserField;
		private JPasswordField skypePassField;
		private JTextField skypeReadField;
		private JComboBox skypeTypeComboBox;
		private JTable skypeReadTable;

	private static SkypeSetting skypeSetting;
	private static SkypeModel skypeModel;
	private static Map<String, SkypeChatType> skypeReadMap;

	private SkypeSetting() {
		skypeReadMap = new HashMap<>();
		this.skypeReadTable.getTableHeader().setReorderingAllowed(false);
		this.setTableModel();
		this.setButtonListener();
	}

	public void setTableModel() {
		skypeModel = new SkypeModel();
		this.skypeReadTable.setModel(skypeModel);
		TableColumn tableColumn = this.skypeReadTable.getColumn(SkypeModel.getColumns()[1]);
		tableColumn.setMinWidth(80);
		tableColumn.setMaxWidth(80);
		this.skypeTypeComboBox.setMinimumSize(new Dimension(80, 10));
	}

	public void setButtonListener() {
		AddButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(skypeReadField.getText().length() <= 0) return;
				addTableData(skypeReadField.getText(), (String)skypeTypeComboBox.getSelectedItem());
				skypeReadField.setText("");
			}
		});
		DelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] rows = skypeReadTable.getSelectedRows();
				for(int i = rows.length - 1; 0 <= i; i--) {
					skypeModel.removeRow(rows[i]); skypeReadMap.remove(rows[i]);
				}
			}
		});
	}

	public void addTableData(String name, String type) {
		skypeReadMap.put(name, SkypeChatType.getName(type));
		for(int i = 0; i < skypeReadTable.getRowCount(); i++) {
			if(skypeReadTable.getValueAt(i, 0).equals(name)) {
				skypeModel.removeRow(i);
				break;
			}
		}
		skypeModel.addRow(new Object[] {name, type});
	}

	public static SkypeSetting getInstance() {
		if(skypeSetting == null) skypeSetting = new SkypeSetting();
		return skypeSetting;
	}

	public String getSkypeUser() {
		return skypeUserField.getText();
	}

	public String getSkypePass() {
		return new String(skypePassField.getPassword());
	}

	public JTextField getSkypeUserField() {
		return skypeUserField;
	}

	public static Map<String, SkypeChatType> getSkypeReadMap() {
		return skypeReadMap;
	}

	public static JPanel getPanel() {
		return getInstance().panel1;
	}

	public enum SkypeChatType {
		GROUP("Group"),
		PRIVATE("Private"),
		BOTH("Both");

		private java.lang.String JPName;

		SkypeChatType(java.lang.String JPName) {
			this.JPName = JPName;
		}

		public java.lang.String getName() {
			return this.JPName;
		}

		public static java.lang.String[] getJPNames() {
			SkypeChatType[] values = values();
			String[] strs = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				strs[i] = values[i].getName();
			}
			return strs;
		}

		public static SkypeChatType getName(String name) {
			for(SkypeChatType type : values()) {
				if(type.getName().equals(name)) return type;
			}
			return null;
		}
	}

	private void createUIComponents() {
		this.skypeTypeComboBox = new JComboBox(SkypeChatType.getJPNames());
	}
}
