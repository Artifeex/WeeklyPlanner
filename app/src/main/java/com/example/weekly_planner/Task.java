package com.example.weekly_planner;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String text;
    private boolean isDone;
    private int owner;

    public Task(int id, String text, boolean isDone, int owner) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
        this.owner = owner;
    }

    @Ignore
    public Task(String text, boolean isDone, int owner) {
        this.text = text;
        this.isDone = isDone;
        this.owner = owner;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getText() {
        return text;
    }

    public boolean isDone() {
        return isDone;
    }

    public int getOwner() {
        return owner;
    }
}
