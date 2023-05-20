package com.example.weekly_planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TasksActivity extends AppCompatActivity {

    private static final String DAY_OT_THE_WEEK = "dayOfTheWeek";
    private static final String CURRENT_DAY = "curDay";
    private int day;

    private TextView dayNameTextView;
    private View splitLine;
    private FloatingActionButton addButton;

    private RecyclerView recyclerView;
    private TasksDatabase database;
    private TasksAdapter adapter;

    private TasksViewModel tasksViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        initViews();
        tasksViewModel = new TasksViewModel(getApplication());
        database = TasksDatabase.getInstance(getApplication());
        initIntent();

        adapter = new TasksAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnTaskCompleteListener(new TasksAdapter.onTaskCompleteListener() {
            @Override
            public void onTaskComplete(Task task) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        database.taskDao().actionWithTask(task.getId(), task.isDone());
                    }
                });
                thread.start();
            }
        });

        adapter.setOnPopupMenuListener(new TasksAdapter.onPopupMenuListener() {
            @Override
            public void onDeleteTask(Task task) {
                tasksViewModel.deleteTask(task);
            }

            @Override
            public void onEditTask(Task task) {
                Intent intent = AddTaskActivity.changeTaskIntent(TasksActivity.this, task.getId());
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddTaskActivity.addTaskNewIntent(TasksActivity.this, day);
                startActivity(intent);
            }
        });

        tasksViewModel.getTasks(day).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                adapter.setTasks(tasks);
            }
        });

    }

    private void initViews() {
        dayNameTextView = findViewById(R.id.day_of_the_week_in_task_text);
        recyclerView = findViewById(R.id.recyclerViewTasks);
        addButton = findViewById(R.id.buttonAddNote);
        splitLine = findViewById(R.id.splitLine);
    }


    private void initIntent() {
        Intent intent = getIntent();
        day = intent.getIntExtra(DAY_OT_THE_WEEK, 1);
        if(day == intent.getIntExtra(CURRENT_DAY, -1)) {
            int colorResId = android.R.color.holo_orange_light;
            int color = ContextCompat.getColor(this, colorResId);
            dayNameTextView.setTextColor(color);
            splitLine.setBackgroundColor(color);
        }
        dayNameTextView.setText(getNameDay(day));
    }

    private String getNameDay(int day) {
        String result;
        switch (day) {
            case 1:
                result = getString(R.string.monday);
                break;
            case 2:
                result = getString(R.string.tuesday);
                break;
            case 3:
                result = getString(R.string.wednesday);
                break;
            case 4:
                result = getString(R.string.thursday);
                break;
            case 5:
                result = getString(R.string.friday);
                break;
            case 6:
                result = getString(R.string.saturday);
                break;
            default:
                result = getString(R.string.sunday);
                break;
        }
        return result;
    }

    public static Intent newIntent(Context context, int day, int currentDay) {
        Intent intent = new Intent(context, TasksActivity.class);
        intent.putExtra(DAY_OT_THE_WEEK, day);
        intent.putExtra(CURRENT_DAY, currentDay);
        return intent;
    }

}