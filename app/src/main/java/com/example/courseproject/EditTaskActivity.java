package com.example.courseproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.xmlcalendar.Category;
import com.example.courseproject.xmlcalendar.Serializator;
import com.example.courseproject.xmlcalendar.Share;
import com.example.courseproject.xmlcalendar.Task;

public class EditTaskActivity extends AppCompatActivity {

    EditText text;
    Spinner category;
    Button createTaskButton;
    Button changeTaskButton;
    Button deleteTaskButton;
    Task task;

    ArrayAdapter<Category> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        text = findViewById(R.id.textEditText);
        category = findViewById(R.id.categorySpinner);
        createTaskButton = findViewById(R.id.createTaskButton);
        changeTaskButton = findViewById(R.id.changeTaskButton);
        deleteTaskButton = findViewById(R.id.deleteTaskButton);

        adapter = new ArrayAdapter<Category>(this,
                R.layout.support_simple_spinner_dropdown_item,
                Share.Categories.getList());
        category.setAdapter(adapter);

        if (getIntent().getBooleanExtra("isCreate", true)) {
            changeTaskButton.setVisibility(View.GONE);
            deleteTaskButton.setVisibility(View.GONE);

        } else {
            createTaskButton.setVisibility(View.GONE);
            changeTaskButton.setVisibility(View.VISIBLE);
            deleteTaskButton.setVisibility(View.VISIBLE);
            task = Share.Tasks.getList().get(getIntent().getIntExtra("id", 0));
            text.setText(task.getText());
            category.setSelection(Share.Categories.getList().indexOf(task.getCategory()));
        }
    }

    public void createClick(View view) {
        Task task = new Task(text.getText().toString(),
                getIntent().getStringExtra("date"),
                (Category) category.getItemAtPosition(category.getSelectedItemPosition()));
        Share.Tasks.add(task);
        Serializator.updateDoc(this);
        finish();
    }

    public void changeClick(View view) {
        task.setText(text.getText().toString());
        task.setCategory((Category) category.getItemAtPosition(category.getSelectedItemPosition()));
        Serializator.updateDoc(this);
        finish();
    }

    public void deleteClick(View view) {
        task.setText(text.getText().toString());
        task.setCategory((Category) category.getItemAtPosition(category.getSelectedItemPosition()));
        Share.Tasks.delete(task);
        finish();
    }
}
