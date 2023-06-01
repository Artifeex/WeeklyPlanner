package com.example.weekly_planner;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM TASKS WHERE owner = :day")
    public LiveData<List<Task>> getTasks(int day);

    @Insert
    public Completable add(Task task);

    @Query("DELETE FROM TASKS WHERE id = :id")
    public Completable delete(int id);

    @Query("SELECT COUNT(*) FROM TASKS WHERE owner =:day")
    public LiveData<Integer> getCountTasks(int day);

    @Query("SELECT COUNT(*) FROM TASKS WHERE owner =:day and isDone = 1")
    public LiveData<Integer> getCountCompletedTasks(int day);

    @Query("UPDATE TASKS SET isDone = :isDone WHERE id = :id")
    public Completable actionWithTask(int id, boolean isDone);

    @Query("SELECT * FROM TASKS WHERE id = :id")
    public Task getTaskById(int id);

    @Query("UPDATE TASKS SET text = :text WHERE id = :id")
    public Completable updateText(int id, String text);

    @Query("DELETE FROM TASKS")
    public Completable clearDatabase();

    @Query("SELECT TEXT FROM TASKS WHERE id = :id")
    public Single<String> getTaskText(int id);

}
