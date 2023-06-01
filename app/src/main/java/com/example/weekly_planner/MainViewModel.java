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

public class MainViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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
        Disposable disposable = tasksDatabase.taskDao().clearDatabase()
                .subscribeOn(Schedulers.io())
                .subscribe(new Action() {
                    @Override
                    public void run() throws Throwable {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.e("MainViewModel", "Error, when new week");
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
