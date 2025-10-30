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

        // イベントリスナーの登録
        this.todoFrame.addAddButtonListener(e -> addTask());
        this.todoFrame.addCompleteButtonListener(e -> completeTasks());
        this.todoFrame.addDeleteButtonListener(e -> deleteTasks());
        this.todoFrame.addSaveMemoButtonListener(e -> saveMemo());
        this.todoFrame.addTaskListSelectionListener(e -> onTaskSelection());
    }

    public void initView() {
        refreshTaskList();
    }

    private void addTask() {
        String description = todoFrame.getTaskInputField().getText().trim();
        if (description.isEmpty()) {
            todoFrame.showMessage("タスクの内容を入力してください。", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        todoService.addTask(description, ""); // GUIではメモは後から編集
        todoFrame.clearInputField();
        refreshTaskList();
    }

    private void completeTasks() {
        List<Task> selectedTasks = todoFrame.getTaskList().getSelectedValuesList();
        if (selectedTasks.isEmpty()) {
            todoFrame.showMessage("完了するタスクを選択してください。", "情報", JOptionPane.INFORMATION_MESSAGE);
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
            todoFrame.showMessage("削除するタスクを選択してください。", "情報", JOptionPane.INFORMATION_MESSAGE);
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
            todoFrame.setMemoText(""); // 削除後はメモエリアをクリア
        }
    }

    private void saveMemo() {
        Task selectedTask = todoFrame.getTaskList().getSelectedValue();
        if (selectedTask == null) {
            todoFrame.showMessage("メモを保存するタスクを選択してください。", "情報", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String memo = todoFrame.getMemoArea().getText();
        if (todoService.updateMemo(selectedTask.getId(), memo)) {
             todoFrame.showMessage("メモを保存しました。", "成功", JOptionPane.INFORMATION_MESSAGE);
             refreshTaskList(); // "(メモあり)" の表示を更新するためにリストも更新
        } else {
             todoFrame.showMessage("メモの保存に失敗しました。", "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onTaskSelection() {
        Task selectedTask = todoFrame.getTaskList().getSelectedValue();
        if (selectedTask != null) {
            todoFrame.setMemoText(selectedTask.getMemo());
        }
    }

    private void refreshTaskList() {
        List<Task> tasks = todoService.getAllTasks();
        todoFrame.updateTaskList(tasks);
    }
}
