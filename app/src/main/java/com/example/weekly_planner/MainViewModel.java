package com.example.weekly_planner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;

    public MainViewModel(@NonNull Application application) {
        super(application);
        tasksDatabase = TasksDatabase.getInstance(application);
    }

    public LiveData<Integer> getCountTasks(int day) {

        return tasksDatabase.taskDao().getCountTasks(day);
    }

    public LiveData<Integer> getCountCompletedTasks(int day) {
        return tasksDatabase.taskDao().getCountCompletedTasks(day);
    }

    public void newWeek() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tasksDatabase.taskDao().clearDatabase();
            }
        });
        thread.start();
    }


}
