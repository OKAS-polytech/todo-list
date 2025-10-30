package com.todolist.app.repository;

import com.todolist.app.domain.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskRepository {
    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public Task create(String description) {
        int id = nextId.getAndIncrement();
        Task newTask = new Task(id, description);
        tasks.put(id, newTask);
        return newTask;
    }

    public List<Task> findAll() {
        List<Task> taskList = new ArrayList<>(tasks.values());
        taskList.sort(Comparator.comparingInt(Task::getId));
        return Collections.unmodifiableList(taskList);
    }

    public Optional<Task> findById(int id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public void delete(int id) {
        tasks.remove(id);
    }
}
