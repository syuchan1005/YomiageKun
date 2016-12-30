package com.github.syuchan1005.yomiagekun;

import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
}
