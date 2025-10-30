package com.todolist.app.ui;

import com.todolist.app.domain.Task;
import com.todolist.app.domain.TodoService;

import java.util.List;
import java.util.Scanner;

public class ConsoleController {
    private final TodoService todoService;
    private final Scanner scanner;

    public ConsoleController(TodoService todoService) {
        this.todoService = todoService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Todoリストアプリケーションへようこそ！");
        while (true) {
            System.out.println("コマンドを入力してください (add, list, done, delete, exit):");
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] parts = input.trim().split(" ", 2);
            String command = parts[0];

            switch (command) {
                case "add":
                    if (parts.length > 1) {
                        addTask(parts[1]);
                    } else {
                        System.out.println("エラー: タスクの内容を入力してください。");
                    }
                    break;
                case "list":
                    displayTasks();
                    break;
                case "done":
                    if (parts.length > 1) {
                        completeTask(parts[1]);
                    } else {
                        System.out.println("エラー: タスクIDを入力してください。");
                    }
                    break;
                case "delete":
                    if (parts.length > 1) {
                        deleteTask(parts[1]);
                    } else {
                        System.out.println("エラー: タスクIDを入力してください。");
                    }
                    break;
                case "exit":
                    System.out.println("アプリケーションを終了します。");
                    return;
                default:
                    System.out.println("エラー: 不正なコマンドです。");
                    break;
            }
        }
    }

    private void addTask(String description) {
        try {
            todoService.addTask(description);
            System.out.println("タスクを追加しました。");
        } catch (IllegalArgumentException e) {
            System.out.println("エラー: " + e.getMessage());
        }
    }

    private void displayTasks() {
        List<Task> tasks = todoService.getAllTasks();
        System.out.println("--- TODOリスト ---");
        if (tasks.isEmpty()) {
            System.out.println("(タスクはありません)");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
        System.out.println("------------------");
    }

    private void completeTask(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (todoService.completeTask(id)) {
                System.out.println("タスク(ID: " + id + ")を完了しました。");
            } else {
                System.out.println("エラー: 指定されたIDのタスクは存在しません。");
            }
        } catch (NumberFormatException e) {
            System.out.println("エラー: IDは数値で入力してください。");
        }
    }

    private void deleteTask(String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            if (todoService.deleteTask(id)) {
                System.out.println("タスク(ID: " + id + ")を削除しました。");
            } else {
                System.out.println("エラー: 指定されたIDのタスクは存在しません。");
            }
        } catch (NumberFormatException e) {
            System.out.println("エラー: IDは数値で入力してください。");
        }
    }
}
