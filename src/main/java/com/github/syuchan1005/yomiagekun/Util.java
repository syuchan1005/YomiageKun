package com.github.syuchan1005.yomiagekun;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Created by syuchan on 2016/12/14.
 */
public class Util {

	public static <S, T> void setEditTableColumn(TableColumn tableColumn, String fieldName,
												 Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory,
												 BiConsumer<TableColumn.CellEditEvent<S, T>, S> consumer) {
		tableColumn.setCellValueFactory(new PropertyValueFactory<>(fieldName));
		tableColumn.setCellFactory(cellFactory);
		tableColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<S, T>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<S, T> event) {
				consumer.accept(event, event.getTableView().getItems().get(event.getTablePosition().getRow()));
			}
		});
	}

	public static String convertString(InputStream inputStream) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			br.lines().forEach(str -> lines.add(str));
		} catch(IOException ex) {}
		return String.join("\n", lines);
	}
}
