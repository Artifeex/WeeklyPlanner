package com.example.weekly_planner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AddTaskViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;
    private MutableLiveData<Boolean> canCloseScreen = new MutableLiveData<>();
    private String textTask;
    private MutableLiveData<Boolean> canReturnTextTask = new MutableLiveData<>();

    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        tasksDatabase = TasksDatabase.getInstance(application);
    }

    public void addTask(Task task) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tasksDatabase.taskDao().add(task);
                canCloseScreen.postValue(true);
            }
        });
        thread.start();
    }

    public void changeTask(int id, String newText) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tasksDatabase.taskDao().updateText(id, newText);
                canCloseScreen.postValue(true);
            }
        });
        thread.start();
    }

    public MutableLiveData<Boolean> getCanCloseScreen() {
        return canCloseScreen;
    }

    public String getTextTask() {
        return textTask;
    }

    public MutableLiveData<Boolean> getCanReturnTextTask() {
        return canReturnTextTask;
    }

    public void wantToChangeTask(int id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                textTask = tasksDatabase.taskDao().getTaskText(id);
                canReturnTextTask.postValue(true);
            }
        });
        thread.start();
    }

    public LiveData<Boolean> canCloseScreen() {
        return canCloseScreen;
    }
}
