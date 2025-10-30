package com.todolist.app.gui.controller;

import com.todolist.app.domain.Task;
import com.todolist.app.domain.TodoService;
import com.todolist.app.gui.view.TodoFrame;

import javax.swing.*;
import java.util.List;

public class GuiController {

    private final TodoService todoService;
    private final TodoFrame todoFrame;

    public GuiController(TodoService todoService, TodoFrame todoFrame) {
        this.todoService = todoService;
        this.todoFrame = todoFrame;

        // Viewにイベントリスナーを登録
        this.todoFrame.addAddButtonListener(e -> addTask());
        this.todoFrame.addCompleteButtonListener(e -> completeTasks());
        this.todoFrame.addDeleteButtonListener(e -> deleteTasks());
    }

    public void initView() {
        // 初期データを表示
        refreshTaskList();
    }

    private void addTask() {
        String description = todoFrame.getTaskInputField().getText().trim();
        if (description.isEmpty()) {
            todoFrame.showErrorMessage("タスクの内容を入力してください。");
            return;
        }

        try {
            todoService.addTask(description);
            todoFrame.clearInputField();
            refreshTaskList();
        } catch (IllegalArgumentException e) {
            todoFrame.showErrorMessage(e.getMessage());
        }
    }

    private void completeTasks() {
        List<Task> selectedTasks = todoFrame.getTaskList().getSelectedValuesList();
        if (selectedTasks.isEmpty()) {
            todoFrame.showErrorMessage("完了するタスクを選択してください。");
            return;
        }

        for (Task task : selectedTasks) {
            todoService.completeTask(task.getId());
        }
        refreshTaskList();
    }

    private void deleteTasks() {
        List<Task> selectedTasks = todoFrame.getTaskList().getSelectedValuesList();
        if (selectedTasks.isEmpty()) {
            todoFrame.showErrorMessage("削除するタスクを選択してください。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            todoFrame.getFrame(),
            selectedTasks.size() + "件のタスクを削除しますか？",
            "削除の確認",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            for (Task task : selectedTasks) {
                todoService.deleteTask(task.getId());
            }
            refreshTaskList();
        }
    }

    private void refreshTaskList() {
        List<Task> tasks = todoService.getAllTasks();
        todoFrame.updateTaskList(tasks);
    }
}
