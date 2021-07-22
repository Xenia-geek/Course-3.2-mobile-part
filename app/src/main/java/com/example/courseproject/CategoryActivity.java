package com.example.courseproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.xmlcalendar.Category;
import com.example.courseproject.xmlcalendar.Serializator;
import com.example.courseproject.xmlcalendar.Share;

public class CategoryActivity extends AppCompatActivity {
    EditText categoryEditText;
    Button addButton;
    Button changeButton;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        categoryEditText = findViewById(R.id.categoryEditText);
        addButton = findViewById(R.id.addButton);
        changeButton = findViewById(R.id.changeButton);

        if (getIntent().getBooleanExtra("isCreate", true)) {
            changeButton.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.GONE);
            category = Share.Categories.getList().get(getIntent().getIntExtra("id", 0));
            categoryEditText.setText(category.getName());
        }
    }

    public void addClick(View view) {
        Category category = new Category(categoryEditText.getText().toString());
        Share.Categories.add(category);
        Serializator.updateDoc(this);
        finish();
    }

    public void changeClick(View view) {
        category.setName(categoryEditText.getText().toString());
        Serializator.updateDoc(this);
        finish();
    }
}
