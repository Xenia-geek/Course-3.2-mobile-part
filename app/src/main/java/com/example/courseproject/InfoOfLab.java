package com.example.courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Teachers;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.PlansPass;
import com.example.courseproject.db.units.StudentPasses;
import com.example.courseproject.db.units.Teacher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoOfLab extends AppCompatActivity {

    Cursor wanted_quantity_Curser;
    Cursor all_about_lab_Cursor;
    Cursor teacher_of_lab_Cursor;
    Cursor teacher_of_lab_Cursor_find;
    Cursor passed_quantity_Cursor;
    Cursor r;
    List<PlansPass> plansPass = new ArrayList();
    Lab InfoLab;
    Teacher TeacherInfo;
    StudentPasses StudentPassesInfo;
    SQLiteDatabase db;
    TextView name_lab;
    TextView quantity_lab;
    TextView teacher_lab;
    TextView week_day_lab;
    TextView quantity_passed_lab;
    TextView quantity_ostalos_lab;
    TextView status_lab;
    public int id_lab;
    public int id_student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_of_lab);
        Intent intent = getIntent();
        //parameters Student
        id_lab = intent.getIntExtra("id", -1);
        id_student = intent.getIntExtra("id_stud", -1);

        JSONPlaceHolderApi apiJSON = NetworkService.getApi();

        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        name_lab = (TextView) findViewById(R.id.name_of_lab_info);
        quantity_lab = (TextView) findViewById(R.id.quantity_of_lab_info_value);
        week_day_lab = (TextView) findViewById(R.id.week_of_lab_info_value);
        teacher_lab = (TextView) findViewById(R.id.teacher_of_lab_info_value);
        quantity_passed_lab = (TextView) findViewById(R.id.passes_of_lab_info_value);
        quantity_ostalos_lab = (TextView) findViewById(R.id.ostalos_of_lab_info_value);
        status_lab = (TextView) findViewById(R.id.status_of_lab_info_value);

        all_about_lab_Cursor = db.rawQuery("Select NameLab, Quantity, IDTeacher, WeekDay from ListLab where IDLab=" + id_lab, null);
        if (all_about_lab_Cursor.moveToFirst()) {
            while (!all_about_lab_Cursor.isClosed()) {

                InfoLab = new Lab(all_about_lab_Cursor.getString(0), all_about_lab_Cursor.getInt(1), all_about_lab_Cursor.getInt(2), all_about_lab_Cursor.getString(3));
                if (!all_about_lab_Cursor.isLast()) {
                    all_about_lab_Cursor.moveToNext();
                } else {
                    all_about_lab_Cursor.close();
                }
            }
            name_lab.setText(InfoLab.NameLab);
            quantity_lab.setText(String.valueOf(InfoLab.Quantity));
            week_day_lab.setText(String.valueOf(InfoLab.WeekDay));

        }
        apiJSON.getTeacher(InfoLab.IDTeacher).enqueue(new Callback<Teachers>() {
            @Override
            public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                if (response.isSuccessful()) {
                    Teachers teacher_info = response.body();
                    String Name_Teacher = teacher_info.getName();
                    String Surname_Teacher = teacher_info.getSurname();

                    teacher_of_lab_Cursor_find = db.rawQuery("Select Name, Surname from TeachersForLab where IDTeacher=" + InfoLab.IDTeacher, null);
                    if (teacher_of_lab_Cursor_find.moveToFirst()) {
                        db.execSQL("DELETE FROM  TeachersForLab where IDTeacher=" + InfoLab.IDTeacher);

                    }

                    db.execSQL("insert into TeachersForLab(IDTeacher, Name, Surname) VALUES("
                            + InfoLab.IDTeacher + ",'" + Name_Teacher + "','" + Surname_Teacher
                            + "')"
                    );

                    teacher_of_lab_Cursor = db.rawQuery("Select Name, Surname from TeachersForLab where IDTeacher=" + InfoLab.IDTeacher, null);
                    if (teacher_of_lab_Cursor.moveToFirst()) {
                        while (!teacher_of_lab_Cursor.isClosed()) {

                            TeacherInfo = new Teacher(teacher_of_lab_Cursor.getString(0), teacher_of_lab_Cursor.getString(1));
                            if (!teacher_of_lab_Cursor.isLast()) {
                                teacher_of_lab_Cursor.moveToNext();
                            } else {
                                teacher_of_lab_Cursor.close();
                            }
                            teacher_lab.setText(TeacherInfo.Surname + " " + TeacherInfo.Name);
                        }
                    } else {
                        Toast.makeText(InfoOfLab.this, "Where is my teacher", Toast.LENGTH_SHORT).show();
                    }


                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<Teachers> call, Throwable t) {
                Toast.makeText(InfoOfLab.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                teacher_of_lab_Cursor = db.rawQuery("Select Name, Surname from TeachersForLab where IDTeacher=" + InfoLab.IDTeacher, null);
                if (teacher_of_lab_Cursor.moveToFirst()) {
                    while (!teacher_of_lab_Cursor.isClosed()) {

                        TeacherInfo = new Teacher(teacher_of_lab_Cursor.getString(0), teacher_of_lab_Cursor.getString(1));
                        if (!teacher_of_lab_Cursor.isLast()) {
                            teacher_of_lab_Cursor.moveToNext();
                        } else {
                            teacher_of_lab_Cursor.close();
                        }
                        teacher_lab.setText(TeacherInfo.Surname + " " + TeacherInfo.Name);
                    }
                } else {
                    Toast.makeText(InfoOfLab.this, "Where is my teacher", Toast.LENGTH_SHORT).show();
                }

                call.cancel();
            }
        });


        apiJSON.getStudentPasses().enqueue(new Callback<List<StudentPassess>>() {
            @Override
            public void onResponse(Call<List<StudentPassess>> call, Response<List<StudentPassess>> response) {
                if (response.isSuccessful()) {
                    List<StudentPassess> passed_student_info = response.body();
                    int a = 0;
                    for (StudentPassess sp : passed_student_info) {

                        if (sp.getIDLab() == id_lab && sp.getIDStudent() == id_student) {
                            a = 1;
                            db.execSQL("update  StudentPass set PassedQuantity=" +
                                    +sp.getPassedQuantity() + " WHERE IDLab =" + id_lab
                            );
                        }
                    }
                    if (a == 0) {
                        StudentPassess Pass = new StudentPassess();
                        Pass.setPassedQuantity(0);
                        Pass.setIDLab(id_lab);
                        Pass.setIDStudent(id_student);
                        Pass.setIDTeacher(InfoLab.IDTeacher);
                        apiJSON.addStudentPasses(Pass).enqueue(new Callback<StudentPassess>() {
                            @Override
                            public void onResponse(Call<StudentPassess> call, Response<StudentPassess> response) {
                                if (response.isSuccessful()) {

                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<StudentPassess> call, Throwable t) {
                                Toast.makeText(InfoOfLab.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });

                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                Toast.makeText(InfoOfLab.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

        passed_quantity_Cursor = db.rawQuery("Select PassedQuantity from StudentPass where IDLab=" + id_lab, null);
        if (passed_quantity_Cursor.moveToFirst()) {
            while (!passed_quantity_Cursor.isClosed()) {
                StudentPassesInfo = new StudentPasses(passed_quantity_Cursor.getInt(0));
                if (!passed_quantity_Cursor.isLast()) {
                    passed_quantity_Cursor.moveToNext();
                } else {
                    passed_quantity_Cursor.close();
                }
                quantity_passed_lab.setText(String.valueOf(StudentPassesInfo.PassedQuantity));
            }
        } else {
            db.execSQL("insert into StudentPass(IDLab, PassedQuantity) VALUES("
                    + id_lab + "," + 0 + ")"
            );
            r = db.rawQuery("Select PassedQuantity from StudentPass where IDLab=" + id_lab, null);

            if (r.moveToFirst()) {
                while (!r.isClosed()) {

                    StudentPassesInfo = new StudentPasses(r.getInt(0));
                    if (!r.isLast()) {
                        r.moveToNext();
                    } else {
                        r.close();
                    }
                    quantity_passed_lab.setText(String.valueOf(StudentPassesInfo.PassedQuantity));
                }
            }
        }


        Date now = new Date();

        int quant_that_needed = 0;

        wanted_quantity_Curser = db.rawQuery("Select Quantity, Date from PlansPass where IDLab=" + id_lab, null);
        if (wanted_quantity_Curser.moveToFirst()) {
            while (!wanted_quantity_Curser.isClosed()) {

                plansPass.add(new PlansPass(wanted_quantity_Curser.getInt(0), wanted_quantity_Curser.getString(1)));
                if (!wanted_quantity_Curser.isLast()) {
                    wanted_quantity_Curser.moveToNext();
                } else {
                    wanted_quantity_Curser.close();
                }
            }

            for (PlansPass planpass : plansPass) {

                SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                String nowDate_str = format1.format(new Date());

                SimpleDateFormat format = new SimpleDateFormat();
                format.applyPattern("dd.MM.yyyy");
                Date plansDate = new Date();
                Date nowDate = new Date();
                try {
                    plansDate = format.parse(planpass.Date);
                    nowDate = format.parse(nowDate_str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (nowDate.after(plansDate)) {
                    quant_that_needed = quant_that_needed + planpass.Quantity;

                }


            }


        }


        if (StudentPassesInfo.PassedQuantity >= quant_that_needed) {
            status_lab.setText("Good!");
        } else {
            status_lab.setText("So bed((");
        }

        quantity_ostalos_lab.setText(String.valueOf(InfoLab.Quantity - StudentPassesInfo.PassedQuantity));


    }

    public void BackToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        db.execSQL("DELETE FROM PlansPass");
        startActivity(intent);
    }

    public void ViewPlansPass(View view) {
        Intent intent = new Intent(this, PlanPassActivity.class);
        intent.putExtra("id", id_lab);
        intent.putExtra("id_stud", id_student);
        startActivity(intent);
    }
}
