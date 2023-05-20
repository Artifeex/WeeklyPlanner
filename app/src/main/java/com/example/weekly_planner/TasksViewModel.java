package com.example.weekly_planner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TasksViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;

    public TasksViewModel(@NonNull Application application) {
        super(application);
        tasksDatabase = TasksDatabase.getInstance(application);
    }

    public void deleteTask(Task task) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tasksDatabase.taskDao().delete(task.getId());
            }
        });
        thread.start();
    }

    LiveData<List<Task>> getTasks(int day) {
        return tasksDatabase.taskDao().getTasks(day);
    }

    public void deleteTask(int index) {

    }
}
