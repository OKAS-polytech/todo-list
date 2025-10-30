package com.todolist.app.domain;

public class Task {
    private final int id;
    private final String description;
    private String memo;
    private boolean isDone;

    public Task(int id, String description, String memo, boolean isDone) {
        this.id = id;
        this.description = description;
        this.memo = (memo == null) ? "" : memo;
        this.isDone = isDone;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        String memoIndicator = (memo != null && !memo.isEmpty()) ? " (メモあり)" : "";
        return "ID: " + id + ", 内容: " + description + ", 状態: [" + (isDone ? "完了" : "未完了") + "]" + memoIndicator;
    }
}
