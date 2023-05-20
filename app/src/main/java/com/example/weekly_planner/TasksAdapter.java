package com.example.weekly_planner;

import static android.provider.Settings.System.getString;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TasksViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private onTaskCompleteListener onTaskCompleteListener;
    private onPopupMenuListener onPopupMenuListener;

    public void setOnPopupMenuListener(TasksAdapter.onPopupMenuListener onPopupMenuListener) {
        this.onPopupMenuListener = onPopupMenuListener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public void setOnTaskCompleteListener(onTaskCompleteListener onTaskCompleteListener) {
        this.onTaskCompleteListener = onTaskCompleteListener;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_item,
                parent,
                false
        );
        return new TasksViewHolder(view);
    }

    public void remove(int pos) {
        onPopupMenuListener.onDeleteTask(tasks.get(pos));
    }

    @Override
    public void onBindViewHolder(TasksViewHolder viewHolder, int position) {
        Task task = tasks.get(position);
        viewHolder.checkBox.setText(task.getText());
        viewHolder.checkBox.setChecked(task.isDone());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                task.setDone(b);
                onTaskCompleteListener.onTaskComplete(task);
            }
        });

        viewHolder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }

        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        private CheckBox checkBox;
        private final int editId = 1;
        private final int deleteId = 2;

        public TasksViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {


            MenuItem editMenu = contextMenu.add(this.getAdapterPosition(), editId, 0, R.string.popup_menu_editText);
            MenuItem deleteMenu = contextMenu.add(this.getAdapterPosition(), deleteId, 1, R.string.popup_menu_deleteText);
            editMenu.setOnMenuItemClickListener(onMenuHandler);
            deleteMenu.setOnMenuItemClickListener(onMenuHandler);
        }

        private final MenuItem.OnMenuItemClickListener onMenuHandler = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case editId:
                        onPopupMenuListener.onEditTask(tasks.get(menuItem.getGroupId()));
                        return true;
                    case deleteId:
                        onPopupMenuListener.onDeleteTask(tasks.get(menuItem.getGroupId()));
                        return true;
                    default:
                        return false;
                }
            }
        };

    }

    interface onTaskCompleteListener {
        void onTaskComplete(Task task);
    }

    interface onPopupMenuListener {
        void onDeleteTask(Task task);
        void onEditTask(Task task);
    }
}
