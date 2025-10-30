package com.todolist.app.domain;

public class Task {
    private final int id;
    private final String description;
    private boolean isDone;

    public Task(int id, String description) {
        this.id = id;
        this.description = description;
        this.isDone = false;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", 内容: " + description + ", 状態: [" + (isDone ? "完了" : "未完了") + "]";
    }
}
