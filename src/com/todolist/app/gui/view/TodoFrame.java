package com.todolist.app.gui.view;

import com.todolist.app.domain.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TodoFrame {

    private JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private JTextField taskInputField;
    private JButton addButton;
    private JButton completeButton;
    private JButton deleteButton;

    public TodoFrame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("TODO List");
        frame.setBounds(100, 100, 500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- 上部: 入力パネル ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        taskInputField = new JTextField();
        addButton = new JButton("追加");
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        // --- 中部: タスクリスト ---
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // --- 下部: 操作ボタンパネル ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        completeButton = new JButton("完了");
        deleteButton = new JButton("削除");
        buttonPanel.add(completeButton);
        buttonPanel.add(deleteButton);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    public JFrame getFrame() {
        return frame;
    }

    public JTextField getTaskInputField() {
        return taskInputField;
    }

    public JList<Task> getTaskList() {
        return taskList;
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addCompleteButtonListener(ActionListener listener) {
        completeButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void updateTaskList(List<Task> tasks) {
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task);
        }
    }

    public void clearInputField() {
        taskInputField.setText("");
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(frame, message, "エラー", JOptionPane.ERROR_MESSAGE);
    }
}
