package com.example.weekly_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLayoutDays;
    private TextView newWeekTextView;
    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        viewModel = new MainViewModel(getApplication());

        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_WEEK) - 1;
        Log.println(Log.DEBUG, "days", "currentDay = " + currentDay);
        if(currentDay == 0) {
            currentDay = 7;
        }
        initDays(currentDay);

        newWeekTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.newWeek();
            }
        });

    }

    private void initViews() {
        linearLayoutDays = findViewById(R.id.linearLayoutDays);
        newWeekTextView = findViewById(R.id.clear_text_view);
    }

    private void initDays(int currentWeekDay) {
        for(int day = 1; day <= 7; day++) {
            View view = getLayoutInflater().inflate(
                    R.layout.day_of_the_week_item,
                    linearLayoutDays,
                    false);
            int viewDay = day;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView textView = view.findViewById(R.id.day_of_the_week_view);
                    String text = textView.getText().toString();
                    startActivity(TasksActivity.newIntent(MainActivity.this, viewDay, currentWeekDay));
                }
            });
            TextView dayTextView = view.findViewById(R.id.day_of_the_week_view);
            if(day == currentWeekDay) {
                int colorResId = android.R.color.holo_orange_light;
                int color = ContextCompat.getColor(view.getContext(), colorResId);
                View line = view.findViewById(R.id.splitLine);
                line.setBackgroundColor(color);
                dayTextView.setTextColor(color);
            }
            TextView progress = view.findViewById(R.id.progress_value_textview);
            progress.setText("0/10");

            viewModel.getCountTasks(day).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer countTasks) {
                    progress.setText(updateCountTasks(countTasks, progress.getText().toString()));
                }
            });

            viewModel.getCountCompletedTasks(day).observe(this, new Observer<Integer>() {
                @Override
                public void onChanged(Integer countCompletedTasks) {
                    progress.setText(updateCompletedTasks(countCompletedTasks, progress.getText().toString()));
                }
            });

            dayTextView.setText(getNameDay(day));
            linearLayoutDays.addView(view);
        }
    }

    private int getDifferens(int currentDay, int day) {
        return currentDay - day;
    }

    private String updateCountTasks(int newCountTasks, String progress) {
        int startIndex = progress.indexOf("/") + 1;
        return progress.substring(0, startIndex) + newCountTasks;
    }

    private String updateCompletedTasks(int newCountTasks, String progress) {
        int startIndex = progress.indexOf("/");
        return newCountTasks + progress.substring(startIndex);
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

}