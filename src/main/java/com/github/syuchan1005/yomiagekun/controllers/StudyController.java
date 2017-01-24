package com.github.syuchan1005.yomiagekun.controllers;

import com.github.syuchan1005.yomiagekun.Controller;
import com.github.syuchan1005.yomiagekun.SQLiteConnector;
import com.github.syuchan1005.yomiagekun.Util;
import com.github.syuchan1005.yomiagekun.contents.StudyContext;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * Created by syuchan on 2016/12/08.
 */
public class StudyController extends Controller implements Initializable {
	@FXML
	private TableView<StudyContext> tableView;
	@FXML
	private TableColumn<StudyContext, String> beforeColumn;
	@FXML
	private TableColumn<StudyContext, String> afterColumn;
	@FXML
	private TableColumn<StudyContext, Integer> priorityColumn;
	@FXML
	private TableColumn<StudyContext, Boolean> isRegexColumn;

	private ObservableList<StudyContext> studyContexts = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableView.setItems(studyContexts);

		Callback<Integer, Void> pressedCallback = new Callback<Integer, Void>() {
			@Override
			public Void call(Integer index) {
				System.out.println(index);
				return null;
			}
		};

		Util.setEditTableColumn(beforeColumn, "beforeText",
				TextFieldTableCell.forTableColumn(),
				(event, studyContext) -> {
					String newValue = event.getNewValue().toUpperCase();
					int beforeIndex = getBeforeIndex(newValue);
					if (beforeIndex == -1) {
						studyContext.setBeforeText(newValue);
						studyContext.setPriority(newValue.length());
					} else {
						tableView.getSelectionModel().select(beforeIndex);
					}
					sort();
				}
		);

		Util.setEditTableColumn(afterColumn, "afterText",
				TextFieldTableCell.forTableColumn(),
				(event, studyContext) -> {
					// studyContext.setAfterText(event.getNewValue());
					sort();
				});

		Util.setEditTableColumn(priorityColumn, "priority",
				TextFieldTableCell.forTableColumn(new IntegerStringConverter()),
				(event, studyContext) -> {
					studyContext.setPriority(event.getNewValue());
					sort();
				});

		Util.setEditTableColumn(isRegexColumn, "isRegex",
				CheckBoxTableCell.forTableColumn(null, null),
				(event, studyContext) -> {
					studyContext.setRegex(event.getNewValue());
					sort();
				});

		Util.setContentMenu(tableView, event -> {
			if (getBeforeIndex("") == -1) {
				studyContexts.add(new StudyContext("", "", 0, false));
			}
		}, event -> {
			int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
			if (selectedIndex != -1) studyContexts.remove(selectedIndex);
		});
	}

	@Override
	public void onClose(WindowEvent event) {
		Connection connection = SQLiteConnector.getConnection();
		try {
			Statement statement = connection.createStatement();
			statement.execute("DROP TABLE IF EXISTS StudyData");
			statement.execute("CREATE TABLE IF NOT EXISTS StudyData(before TEXT NOT NULL, after TEXT, priority INTEGER NOT NULL, regex INTEGER NOT NULL)");
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO StudyData VALUES (?, ?, ?, ?)");
			for (StudyContext studyContext : studyContexts) {
				preparedStatement.setString(1, studyContext.getBeforeText());
				preparedStatement.setString(2, studyContext.getAfterText());
				preparedStatement.setInt(3, studyContext.getPriority());
				preparedStatement.setInt(4, studyContext.isRegex() ? 1 : 0);
				preparedStatement.execute();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private int getBeforeIndex(String newValue) {
		for (int i = 0; i < studyContexts.size(); i++) {
			if (studyContexts.get(i).getBeforeText().equalsIgnoreCase(newValue)) {
				return i;
			}
		}
		return -1;
	}

	private static Comparator<StudyContext> studyContextComparator = (s1, s2) -> {
		if (s1.isRegex() != s2.isRegex()) {
			return s1.isRegex() ? -1 : 1;
		}
		if (s1.getPriority() != s2.getPriority()) {
			return s2.getPriority() - s1.getPriority();
		}
		int l1 = s1.getBeforeText().length();
		int l2 = s2.getBeforeText().length();
		if (l1 != l2) {
			return l2 - l1;
		}
		return s2.getBeforeText().compareToIgnoreCase(s1.getBeforeText());
	};

	private void sort() {
		FXCollections.sort(studyContexts, studyContextComparator);
	}

}
