package com.todolist.app.domain;

import com.todolist.app.repository.DatabaseManager;
import com.todolist.app.repository.TaskRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService todoService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() throws SQLException {
        // テストごとにデータベースを初期化
        DatabaseManager.initializeDatabase();
        taskRepository = new TaskRepository();
        todoService = new TodoService(taskRepository);

        // 各テストの前にテーブルを空にする
        clearDatabase();
    }

    @AfterEach
    void tearDown() throws SQLException {
        // 各テストの後にテーブルを空にする
        clearDatabase();
    }

    private void clearDatabase() throws SQLException {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM tasks");
            // AUTOINCREMENTのシーケンスもリセット
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='tasks'");
        }
    }

    @Test
    @DisplayName("新しいタスクをメモ付きで正常に追加できる")
    void addTask_shouldAddNewTaskWithMemo() {
        String description = "テストタスク";
        String memo = "これはメモです";
        Task addedTask = todoService.addTask(description, memo);

        assertNotNull(addedTask);
        assertEquals(description, addedTask.getDescription());
        assertEquals(memo, addedTask.getMemo());
        assertFalse(addedTask.isDone());

        List<Task> tasks = todoService.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(description, tasks.get(0).getDescription());
        assertEquals(memo, tasks.get(0).getMemo());
    }

    @Test
    @DisplayName("nullまたは空のタスクを追加しようとすると例外をスローする")
    void addTask_shouldThrowExceptionForNullOrEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask(null, ""));
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask("", ""));
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask("   ", ""));
    }

    @Test
    @DisplayName("存在するタスクを完了状態にできる")
    void completeTask_shouldMarkTaskAsDone() {
        Task task = todoService.addTask("完了テスト", "");
        boolean result = todoService.completeTask(task.getId());

        assertTrue(result);
        Optional<Task> updatedTask = taskRepository.findById(task.getId());
        assertTrue(updatedTask.isPresent());
        assertTrue(updatedTask.get().isDone());
    }

    @Test
    @DisplayName("存在するタスクのメモを更新できる")
    void updateMemo_shouldUpdateMemo() {
        Task task = todoService.addTask("メモ更新テスト", "古いメモ");
        String newMemo = "新しいメモ";
        boolean result = todoService.updateMemo(task.getId(), newMemo);

        assertTrue(result);
        Optional<Task> updatedTask = taskRepository.findById(task.getId());
        assertTrue(updatedTask.isPresent());
        assertEquals(newMemo, updatedTask.get().getMemo());
    }

    @Test
    @DisplayName("存在しないタスクを完了しようとしても失敗する")
    void completeTask_shouldFailForNonExistentTask() {
        boolean result = todoService.completeTask(999);
        assertFalse(result);
    }

    @Test
    @DisplayName("存在するタスクを削除できる")
    void deleteTask_shouldRemoveTask() {
        Task task = todoService.addTask("削除テスト", "");
        boolean result = todoService.deleteTask(task.getId());

        assertTrue(result);
        assertTrue(todoService.getAllTasks().isEmpty());
    }

    @Test
    @DisplayName("存在しないタスクを削除しようとしても失敗する")
    void deleteTask_shouldFailForNonExistentTask() {
        boolean result = todoService.deleteTask(999);
        assertFalse(result);
    }
}
