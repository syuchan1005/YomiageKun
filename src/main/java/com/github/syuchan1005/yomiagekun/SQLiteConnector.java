package com.github.syuchan1005.yomiagekun;

import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by syuchan on 2016/12/31.
 */
public class SQLiteConnector {
	private static SQLiteConnector instance = new SQLiteConnector();
	private static Map<String, String> saveData;
	private static Connection connection;
	private static PreparedStatement saveStatement;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private SQLiteConnector() {
		saveData = new HashMap<>();
		if (connection == null) {
			Statement statement = null;
			try {
				connection = DriverManager.getConnection(JDBC.PREFIX + "setting.db");
				statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM StoreData");
				while (resultSet.next()) {
					String value = resultSet.getString(2);
					saveData.put(resultSet.getString(1), value.equals("NULL") ? "" : value);
				}
			} catch (SQLException e) {
				System.out.println("Database file not found.");
			}
			try {
				statement.execute("DROP TABLE IF EXISTS StoreData");
				statement.execute("CREATE TABLE StoreData(Key TEXT NOT NULL, Value TEXT NOT NULL)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static SQLiteConnector getInstance() {
		return instance;
	}

	public static Connection getConnection() {
		return connection;
	}

	public static Map<String, String> getSaveData() {
		return Collections.unmodifiableMap(saveData);
	}

	public static void setSaveData(String key, String value) {
		if (value.isEmpty()) value = "NULL";
		saveData.put(key, value);
	}

	public static void saveAll() {
		if (saveStatement == null) {
			try {
				saveStatement = connection.prepareStatement("INSERT INTO StoreData VALUES (?, ?)");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			for (Map.Entry<String, String> entry : saveData.entrySet()) {
				saveStatement.setString(1, entry.getKey());
				saveStatement.setString(2, entry.getValue());
				saveStatement.executeUpdate();
			}
			saveStatement.clearParameters();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
