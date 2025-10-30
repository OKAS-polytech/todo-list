package com.todolist.app;

import com.todolist.app.domain.TodoService;
import com.todolist.app.repository.TaskRepository;
import com.todolist.app.ui.ConsoleController;

public class Main {
    public static void main(String[] args) {
        // 依存関係の注入 (Dependency Injection)
        TaskRepository taskRepository = new TaskRepository();
        TodoService todoService = new TodoService(taskRepository);
        ConsoleController consoleController = new ConsoleController(todoService);

        // アプリケーションの開始
        consoleController.start();
    }
}
