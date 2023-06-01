package com.example.weekly_planner;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddTaskViewModel extends AndroidViewModel {

    private TasksDatabase tasksDatabase;
    private MutableLiveData<Boolean> canCloseScreen = new MutableLiveData<>();
    private String textTask;
    private MutableLiveData<Boolean> canReturnTextTask = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public AddTaskViewModel(@NonNull Application application) {
        super(application);
        tasksDatabase = TasksDatabase.getInstance(application);
    }

    public void addTask(Task task) {
        Disposable disposable = tasksDatabase.taskDao().add(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                               @Override
                               public void run() throws Throwable {
                                   canCloseScreen.setValue(true);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.e("AddTaskViewModel", "Error, when try to add task");
                            }
                        });
        compositeDisposable.add(disposable);
    }

    public void changeTextTask(int id, String newText) {
        Disposable disposable = tasksDatabase.taskDao().updateText(id, newText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action() {
                               @Override
                               public void run() throws Throwable {
                                   canCloseScreen.setValue(true);
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Throwable {
                                Log.e("AddTaskViewModel", "Error, when try to changeTextTask");
                            }
                        });
        compositeDisposable.add(disposable);
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
        Disposable disposable = tasksDatabase.taskDao().getTaskText(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        textTask = s;
                        canReturnTextTask.setValue(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.e("AddTaskViewModel", "Error when try to change task");
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }

    public LiveData<Boolean> canCloseScreen() {
        return canCloseScreen;
    }
}
