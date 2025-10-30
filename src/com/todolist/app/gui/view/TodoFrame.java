package com.todolist.app.gui.view;

import com.todolist.app.domain.Task;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class TodoFrame {

    private JFrame frame;
    private JList<Task> taskList;
    private DefaultListModel<Task> listModel;
    private JTextField taskInputField;
    private JTextArea memoArea;
    private JButton addButton;
    private JButton completeButton;
    private JButton deleteButton;
    private JButton saveMemoButton;

    public TodoFrame() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("TODO List");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout(10, 10));

        // --- 上部: 入力パネル ---
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taskInputField = new JTextField();
        addButton = new JButton("追加");
        inputPanel.add(taskInputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        // --- 中央: JSplitPaneでリストとメモエリアを分割 ---
        // タスクリスト
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(taskList);

        // メモエリア
        JPanel memoPanel = new JPanel(new BorderLayout(0, 5));
        memoPanel.setBorder(BorderFactory.createTitledBorder("メモ"));
        memoArea = new JTextArea();
        memoArea.setLineWrap(true);
        memoArea.setWrapStyleWord(true);
        JScrollPane memoScrollPane = new JScrollPane(memoArea);
        saveMemoButton = new JButton("メモ保存");
        memoPanel.add(memoScrollPane, BorderLayout.CENTER);
        memoPanel.add(saveMemoButton, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScrollPane, memoPanel);
        splitPane.setDividerLocation(200);
        splitPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        frame.getContentPane().add(splitPane, BorderLayout.CENTER);

        // --- 下部: 操作ボタンパネル ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        completeButton = new JButton("選択したタスクを完了");
        deleteButton = new JButton("選択したタスクを削除");
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

    public JTextArea getMemoArea() {
        return memoArea;
    }

    public void addAddButtonListener(ActionListener listener) { addButton.addActionListener(listener); }
    public void addCompleteButtonListener(ActionListener listener) { completeButton.addActionListener(listener); }
    public void addDeleteButtonListener(ActionListener listener) { deleteButton.addActionListener(listener); }
    public void addSaveMemoButtonListener(ActionListener listener) { saveMemoButton.addActionListener(listener); }
    public void addTaskListSelectionListener(ListSelectionListener listener) { taskList.addListSelectionListener(listener); }

    public void updateTaskList(List<Task> tasks) {
        // 選択状態を保持するため、選択されていたIDを覚えておく
        List<Task> selectedTasks = taskList.getSelectedValuesList();

        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task);
        }

        // 選択状態を復元
        for (Task selectedTask : selectedTasks) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).getId() == selectedTask.getId()) {
                    taskList.addSelectionInterval(i, i);
                    break;
                }
            }
        }
    }

    public void clearInputField() {
        taskInputField.setText("");
    }

    public void setMemoText(String text) {
        memoArea.setText(text);
    }

    public void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(frame, message, title, messageType);
    }
}
