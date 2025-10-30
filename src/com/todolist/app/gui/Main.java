package com.todolist.app.gui;

import com.todolist.app.domain.TodoService;
import com.todolist.app.gui.controller.GuiController;
import com.todolist.app.gui.view.TodoFrame;
import com.todolist.app.repository.DatabaseManager;
import com.todolist.app.repository.TaskRepository;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // データベースの初期化
        DatabaseManager.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            // Modelの生成
            TaskRepository taskRepository = new TaskRepository();
            TodoService todoService = new TodoService(taskRepository);

            // Viewの生成
            TodoFrame todoFrame = new TodoFrame();

            // Controllerの生成 (ViewとModelを関連付ける)
            GuiController controller = new GuiController(todoService, todoFrame);

            // 初期表示とウィンドウの可視化
            controller.initView();
            todoFrame.getFrame().setVisible(true);
        });
    }
}
