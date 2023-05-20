package com.example.weekly_planner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskActivity extends AppCompatActivity {

    private static final String DAY_OF_THE_WEEK = "dayOfTheWeek";
    private static final String TASK_ID = "taskId";
    private static final String TO_CHANGE = "isAdd";

    private EditText taskTextView;
    private Button saveTaskButton;

    private AddTaskViewModel addTaskViewModel;

    private int dayOfTheWeek;
    private boolean toChange = false;
    private int taskId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        addTaskViewModel = new AddTaskViewModel(getApplication());
        initViews();
        initIntent();

        addTaskViewModel.getCanReturnTextTask().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean canGetOldText) {
                if(canGetOldText) {
                    taskTextView.setText(addTaskViewModel.getTextTask());
                }
            }
        });

        if(toChange) {
            addTaskViewModel.wantToChangeTask(taskId);
        }

        addTaskViewModel.getCanCloseScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldCloseScreen) {
                if(shouldCloseScreen) {
                    finish();
                }
            }
        });



        saveTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toChange) {
                    String newText = taskTextView.getText().toString().trim();
                    if(newText.equals("")) {
                        Toast.makeText(AddTaskActivity.this, "Заполните поле!", Toast.LENGTH_SHORT).show();
                    } else {
                        addTaskViewModel.changeTask(taskId, newText);
                    }

                } else {
                    String text = taskTextView.getText().toString().trim();
                    if(text.equals("")) {
                        Toast.makeText(AddTaskActivity.this, "Заполните поле!", Toast.LENGTH_SHORT).show();
                    } else {
                        addTaskViewModel.addTask(new Task(text, false, dayOfTheWeek));
                    }

                }
            }
        });
    }

    private void initIntent() {
        Intent intent = getIntent();
        toChange = intent.getBooleanExtra(TO_CHANGE, false);
        if(toChange) {
            taskId = intent.getIntExtra(TASK_ID, 1);
        } else {
            dayOfTheWeek = getIntent().getIntExtra(DAY_OF_THE_WEEK, 1);
        }
    }

    private void initViews() {
        taskTextView = findViewById(R.id.taskEditTextView);
        saveTaskButton = findViewById(R.id.saveButton);
    }


    public static Intent addTaskNewIntent(Context context, int day) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(DAY_OF_THE_WEEK, day);
        return intent;
    }

    public static Intent changeTaskIntent(Context context, int taskId) {
        Intent intent = new Intent(context, AddTaskActivity.class);
        intent.putExtra(TO_CHANGE, true);
        intent.putExtra(TASK_ID, taskId);
        return intent;
    }


}