package com.example.courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.adapters.PlanPassAdapter;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.PlansPass;

import java.util.ArrayList;
import java.util.List;


public class PlanPassActivity extends AppCompatActivity {

    PlanPassAdapter planAdapter;
    private List<PlansPass> plans = new ArrayList();
    Cursor find_plan_for_lab;
    SQLiteDatabase db;
    public int id_lab;
    public int id_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plans_pass);

        ListView listLabs = findViewById(R.id.list_plans);

        Intent intent = getIntent();
        id_lab = intent.getIntExtra("id", -1);
        id_student = intent.getIntExtra("id_stud", -1);


        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        find_plan_for_lab = db.rawQuery("Select Quantity, Date from PlansPass where IDLab=" + id_lab, null);

        plans.clear();
        if (find_plan_for_lab.moveToFirst()) {
            while (!find_plan_for_lab.isClosed()) {
                plans.add(new PlansPass(find_plan_for_lab.getInt(0), find_plan_for_lab.getString(1)));
                if (!find_plan_for_lab.isLast()) {
                    find_plan_for_lab.moveToNext();
                } else {
                    find_plan_for_lab.close();
                }
            }
        }

        planAdapter = new PlanPassAdapter(PlanPassActivity.this, R.layout.list_item_plans, plans);
        listLabs.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
    }

    public void BackToHome(View view) {
        Intent intent = new Intent(this, InfoOfLab.class);
        intent.putExtra("id", id_lab);
        intent.putExtra("id_stud", id_student);
        startActivity(intent);
    }
}
