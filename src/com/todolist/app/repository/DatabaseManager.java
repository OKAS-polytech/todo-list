package com.todolist.app.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:todolist.db";

    public static Connection getConnection() throws SQLException {
        try {
            // JDBCドライバをロードする
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBCドライバが見つかりません。");
            throw new SQLException("JDBC Driver not found", e);
        }
        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS tasks ("
                   + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " description TEXT NOT NULL,"
                   + " memo TEXT,"
                   + " isDone BOOLEAN NOT NULL CHECK (isDone IN (0, 1))"
                   + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("データベースの初期化に失敗しました: " + e.getMessage());
            // アプリケーションを続行できないため、ランタイム例外でラップしてスローする
            throw new RuntimeException(e);
        }
    }
}
