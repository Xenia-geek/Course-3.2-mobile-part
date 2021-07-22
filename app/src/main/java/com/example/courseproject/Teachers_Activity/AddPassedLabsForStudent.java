package com.example.courseproject.Teachers_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.StudentGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPassedLabsForStudent extends AppCompatActivity {

    int id_lab;
    int id_teacher;
    int id_student;
    int id_group;
    Cursor find_name_lab;
    Cursor find_name_student;
    SQLiteDatabase db;
    Lab name_finded;
    StudentGroup name_student_finded;
    Button save;
    Button back_to_list_students;
    Button plus;
    Button minus;
    Button update;
    List<StudentPassess> list_of_all_students_passes = new ArrayList();
    int PassedQuantity;
    int IDStudentPass;
    int MaxQuantit;
    int Updated_Quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_passed_labs_student);


        Intent intent = getIntent();
        id_student = intent.getIntExtra("id_student", -1);
        id_lab = intent.getIntExtra("id_lab", -1);
        id_teacher = intent.getIntExtra("id_teacher", -1);
        id_group = intent.getIntExtra("id_group", -1);
        PassedQuantity = intent.getIntExtra("passed_quantit", -1);
        IDStudentPass = intent.getIntExtra("id_student_pass", -1);
        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        find_name_lab = db.rawQuery("SELECT NameLab, IDLab FROM ListLab1 WHERE IDLab=" + id_lab, null);
        if (find_name_lab.moveToFirst()) {
            while (!find_name_lab.isClosed()) {
                name_finded = new Lab(find_name_lab.getString(0), find_name_lab.getInt(1));
                if (!find_name_lab.isLast()) {
                    find_name_lab.moveToNext();
                } else {
                    find_name_lab.close();
                }
            }
        }

        find_name_student = db.rawQuery("SELECT Name, Surname, IDStudent FROM StudentInfoForTeacher WHERE IDStudent =" + id_student + " and IDGroup=" + id_group, null);
        if (find_name_student.moveToFirst()) {
            while (!find_name_student.isClosed()) {
                name_student_finded = new StudentGroup(find_name_student.getString(0), find_name_student.getString(1), find_name_student.getInt(2));
                if (!find_name_student.isLast()) {
                    find_name_student.moveToNext();
                } else {
                    find_name_student.close();
                }
            }
        }

        TextView name_lab = findViewById(R.id.title_of_labb_that_pass);
        TextView name_student = findViewById(R.id.textView3);
        EditText quantit_passed = findViewById(R.id.editText);

        name_lab.setText(name_finded.NameLab);
        name_student.setText(name_student_finded.Surname + " " + name_student_finded.Name);

        JSONPlaceHolderApi apiJSON = NetworkService.getApi();

        quantit_passed.setText(String.valueOf(PassedQuantity));


        //button back to previous  page
        back_to_list_students = findViewById(R.id.button_back_to_stud);

        back_to_list_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddPassedLabsForStudent.this, InfoListStudentTeacher.class);
                intent.putExtra("id_lab", id_lab);
                intent.putExtra("id_teacher", id_teacher);
                intent.putExtra("id_group", id_group);
                startActivity(intent);
            }
        });

        //button plus quantity

        plus = findViewById(R.id.button_plus);

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                    @Override
                    public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                        if (response.isSuccessful()) {
                            List<ListLabs> all_labs = response.body();
                            for (ListLabs one_lab_info : all_labs) {
                                if (one_lab_info.getIDLab() == id_lab) {
                                    MaxQuantit = one_lab_info.getQuantity();

                                    if (Integer.parseInt(quantit_passed.getText().toString()) < MaxQuantit) {
                                        int Quant = Integer.parseInt(quantit_passed.getText().toString()) + 1;
                                        quantit_passed.setText(String.valueOf(Quant));
                                    } else {
                                        Toast.makeText(AddPassedLabsForStudent.this, "This is maximum", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                        Toast.makeText(AddPassedLabsForStudent.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });

            }
        });

        //minus button

        minus = findViewById(R.id.button_minus);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(quantit_passed.getText().toString()) >= 1) {
                    int Quant = Integer.parseInt(quantit_passed.getText().toString()) - 1;
                    quantit_passed.setText(String.valueOf(Quant));
                } else {
                    Toast.makeText(AddPassedLabsForStudent.this, "Quantity can't be < 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ///Button update quantity

        update = findViewById(R.id.button_save_pass);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updated_Quantity = Integer.parseInt(quantit_passed.getText().toString());
                StudentPassess Pass = new StudentPassess();
                Pass.setIDLab(id_lab);
                Pass.setIDTeacher(id_teacher);
                Pass.setIDStudent(id_student);
                Pass.setIDStudentPass(IDStudentPass);
                Pass.setPassedQuantity(Updated_Quantity);

                apiJSON.updateStudentPasses(IDStudentPass, Pass).enqueue(new Callback<StudentPassess>() {
                    @Override
                    public void onResponse(Call<StudentPassess> call, Response<StudentPassess> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddPassedLabsForStudent.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPassedLabsForStudent.this, InfoListStudentTeacher.class);
                            intent.putExtra("id_lab", id_lab);
                            intent.putExtra("id_teacher", id_teacher);
                            intent.putExtra("id_group", id_group);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddPassedLabsForStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<StudentPassess> call, Throwable t) {
                        Toast.makeText(AddPassedLabsForStudent.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });

            }
        });


    }
}