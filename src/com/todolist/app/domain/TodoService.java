package com.todolist.app.domain;

import com.todolist.app.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class TodoService {
    private final TaskRepository taskRepository;

    public TodoService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(String description, String memo) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("タスクの内容は空にできません。");
        }
        return taskRepository.create(description, memo);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public boolean completeTask(int id) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (!task.isDone()) {
                task.setDone(true);
                taskRepository.update(task); // データベースに更新を反映
            }
            return true;
        }
        return false;
    }

    public boolean updateMemo(int id, String memo) {
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.setMemo(memo);
            taskRepository.update(task); // データベースに更新を反映
            return true;
        }
        return false;
    }

    public boolean deleteTask(int id) {
        if (taskRepository.findById(id).isPresent()) {
            taskRepository.delete(id);
            return true;
        }
        return false;
    }
}
