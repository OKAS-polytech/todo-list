package com.todolist.app.repository;

import com.todolist.app.domain.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepository {

    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, description, memo, isDone FROM tasks ORDER BY id";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("memo"),
                        rs.getBoolean("isDone")
                ));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return tasks;
    }

    public Optional<Task> findById(int id) {
        String sql = "SELECT id, description, memo, isDone FROM tasks WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(new Task(
                        rs.getInt("id"),
                        rs.getString("description"),
                        rs.getString("memo"),
                        rs.getBoolean("isDone")
                ));
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return Optional.empty();
    }

    public Task create(String description, String memo) {
        String sql = "INSERT INTO tasks(description, memo, isDone) VALUES(?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, description);
            pstmt.setString(2, memo);
            pstmt.setBoolean(3, false);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        return new Task(id, description, memo, false);
                    }
                }
            }
        } catch (SQLException e) {
            handleSQLException(e);
        }
        // 作成に失敗した場合はnullを返すか、例外をスローする
        // ここではアプリケーションをシンプルに保つため、nullの可能性を示唆するが、
        // 実際にはカスタム例外をスローする方が堅牢
        return null;
    }

    public void update(Task task) {
        String sql = "UPDATE tasks SET description = ?, memo = ?, isDone = ? WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getDescription());
            pstmt.setString(2, task.getMemo());
            pstmt.setBoolean(3, task.isDone());
            pstmt.setInt(4, task.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    private void handleSQLException(SQLException e) {
        System.err.println("データベース操作中にエラーが発生しました: " + e.getMessage());
        // ログ記録などの処理をここに実装できる
        // 簡潔さのため、ここではコンソール出力のみ
    }
}
