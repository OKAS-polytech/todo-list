package com.todolist.app.cui;

import com.todolist.app.domain.TodoService;
import com.todolist.app.repository.DatabaseManager;
import com.todolist.app.repository.TaskRepository;

public class Main {
    public static void main(String[] args) {
        // データベースの初期化
        DatabaseManager.initializeDatabase();

        // 依存関係の注入 (Dependency Injection)
        TaskRepository taskRepository = new TaskRepository();
        TodoService todoService = new TodoService(taskRepository);
        ConsoleController consoleController = new ConsoleController(todoService);

        // アプリケーションの開始
        consoleController.start();
    }
}
