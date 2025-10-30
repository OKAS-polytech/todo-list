package com.todolist.app.domain;

import com.todolist.app.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoServiceTest {

    private TodoService todoService;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository = new TaskRepository();
        todoService = new TodoService(taskRepository);
    }

    @Test
    @DisplayName("新しいタスクを正常に追加できる")
    void addTask_shouldAddNewTask() {
        String description = "テストタスク";
        Task addedTask = todoService.addTask(description);

        assertNotNull(addedTask);
        assertEquals(description, addedTask.getDescription());
        assertFalse(addedTask.isDone());

        List<Task> tasks = todoService.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(description, tasks.get(0).getDescription());
    }

    @Test
    @DisplayName("nullまたは空のタスクを追加しようとすると例外をスローする")
    void addTask_shouldThrowExceptionForNullOrEmptyDescription() {
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask(null));
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask(""));
        assertThrows(IllegalArgumentException.class, () -> todoService.addTask("   "));
    }

    @Test
    @DisplayName("存在するタスクを完了状態にできる")
    void completeTask_shouldMarkTaskAsDone() {
        Task task = todoService.addTask("完了テスト");
        boolean result = todoService.completeTask(task.getId());

        assertTrue(result);
        assertTrue(task.isDone());
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
        Task task = todoService.addTask("削除テスト");
        int taskId = task.getId();

        boolean result = todoService.deleteTask(taskId);

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
