package com.example.courseproject.Teachers_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.adapters.StudentTeacherGroupAdapter;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.StudentGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoListStudentTeacher extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor find_students_by_id_group;
    int id_group;
    int id_lab;
    List<StudentGroup> list_of_students_by_id_group = new ArrayList();
    StudentTeacherGroupAdapter groupAdapter;
    Button back_to_group;
    int id_teacher;
    List<StudentPassess> list_of_all_students_passes = new ArrayList();
    int PassedQuantity;
    int IDStudentPass;
    int MaxQuantit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_of_roup_teacher);

        Intent intent = getIntent();
        id_group = intent.getIntExtra("id_group", -1);
        id_lab = intent.getIntExtra("id_lab", -1);
        id_teacher = intent.getIntExtra("id_teacher", -1);


        ListView listLabs = findViewById(R.id.list_student_teacher_lab);


        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        find_students_by_id_group = db.rawQuery("SELECT Name, Surname, IDStudent from StudentInfoForTeacher WHERE IDGroup=" + id_group + " ORDER BY Surname", null);

        if (find_students_by_id_group.moveToFirst()) {
            while (!find_students_by_id_group.isClosed()) {
                list_of_students_by_id_group.add(new StudentGroup(find_students_by_id_group.getString(0), find_students_by_id_group.getString(1), find_students_by_id_group.getInt(2)));
                if (!find_students_by_id_group.isLast()) {
                    find_students_by_id_group.moveToNext();
                } else {
                    find_students_by_id_group.close();
                }
            }
        }

        groupAdapter = new StudentTeacherGroupAdapter(InfoListStudentTeacher.this, R.layout.list_item_student_group, list_of_students_by_id_group);
        listLabs.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();


        //exit button

        back_to_group = findViewById(R.id.back_button_to_info_teacher_group);

        back_to_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoListStudentTeacher.this, InfoOfLabTeacher.class);
                intent.putExtra("id", id_lab);
                intent.putExtra("id_techer", id_teacher);
                startActivity(intent);
            }
        });

        JSONPlaceHolderApi apiJSON = NetworkService.getApi();


        //to add passed labs
        ////////go to the list student
        listLabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                apiJSON.getStudentPasses().enqueue(new Callback<List<StudentPassess>>() {
                    @Override
                    public void onResponse(Call<List<StudentPassess>> call, Response<List<StudentPassess>> response) {
                        if (response.isSuccessful()) {
                            list_of_all_students_passes = response.body();
                            for (StudentPassess studPass : list_of_all_students_passes) {
                                int id_stud = (int) list_of_students_by_id_group.get(position).IDStudent;
                                if (studPass.getIDStudent() == id_stud && studPass.getIDLab() == id_lab) {
                                    PassedQuantity = studPass.getPassedQuantity();
                                    IDStudentPass = studPass.getIDStudentPass();


                                    Intent intent = new Intent(InfoListStudentTeacher.this, AddPassedLabsForStudent.class);
                                    intent.putExtra("id_lab", id_lab);
                                    intent.putExtra("id_student", (int) list_of_students_by_id_group.get(position).IDStudent);
                                    intent.putExtra("id_teacher", id_teacher);
                                    intent.putExtra("id_group", id_group);
                                    intent.putExtra("passed_quantit", PassedQuantity);
                                    intent.putExtra("id_student_pass", IDStudentPass);
                                    startActivity(intent);

                                }
                            }

                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                        Toast.makeText(InfoListStudentTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }
        });

    }
}
