package com.example.weekly_planner;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TasksViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public TasksViewModel(@NonNull Application application) {
        super(application);
        tasksDatabase = TasksDatabase.getInstance(application);
    }

    public void deleteTask(Task task) {
        Disposable disposable = tasksDatabase.taskDao().delete(task.getId())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.e("TasksViewModel", "Error, when try to delete task");

                    }
                });
        compositeDisposable.add(disposable);
    }

    public void actionWithTask(int id, boolean isDone) {
        Disposable disposable = tasksDatabase.taskDao().actionWithTask(id, isDone)
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {

                    }
                });
        compositeDisposable.add(disposable);
    }

    LiveData<List<Task>> getTasks(int day) {
        return tasksDatabase.taskDao().getTasks(day);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}


