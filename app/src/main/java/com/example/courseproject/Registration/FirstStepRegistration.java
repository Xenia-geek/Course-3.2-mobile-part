package com.example.courseproject.Registration;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.AuthorizationActivity;
import com.example.courseproject.MainActivity;
import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstStepRegistration extends AppCompatActivity {
    EditText enter_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void NextStep(View view) {
        JSONPlaceHolderApi apiJSON = NetworkService.getApi();
        enter_ID = (EditText) findViewById(R.id.idUser);
        int ID = Integer.parseInt(enter_ID.getText().toString());
        if (ID != 0) {
            apiJSON.getStudent(ID).enqueue(new Callback<Students>() {
                @Override
                public void onResponse(Call<Students> call, Response<Students> response) {
                    if (response.isSuccessful()) {
                        Students student = response.body();
                        //////
                        apiJSON.getStudentByID().enqueue(new Callback<List<LoginStudents>>() {
                            @Override
                            public void onResponse(Call<List<LoginStudents>> call, Response<List<LoginStudents>> response) {
                                if (response.isSuccessful()) {
                                    List<LoginStudents> all_students;
                                    all_students = new ArrayList<>();
                                    all_students.addAll(response.body());
                                    int i = 0;
                                    for (LoginStudents a : all_students) {
                                        if (a.getIDStudent() == ID) {
                                            i = 1;
                                            Toast.makeText(FirstStepRegistration.this, "You are have an account", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (i == 0) {
                                        Intent intent = new Intent(FirstStepRegistration.this, SecondStepRegistration.class);
                                        intent.putExtra("id_student", (Integer) student.getIDStudent());
                                        intent.putExtra("name", (String) student.getName());
                                        intent.putExtra("surname", (String) student.getSurname());
                                        intent.putExtra("id_group", (Integer) student.getIDGroup());
                                        startActivity(intent);
                                    }
                                }
                                call.cancel();

                            }

                            @Override
                            public void onFailure(Call<List<LoginStudents>> call, Throwable t) {
                                Toast.makeText(FirstStepRegistration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }

                        });
                    } else {
                        //Teachers

                        apiJSON.getTeacher(ID).enqueue(new Callback<Teachers>() {
                            @Override
                            public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                if (response.isSuccessful()) {
                                    Teachers teacher = response.body();
                                    call.cancel();
                                    //////
                                    apiJSON.getTeacherByID().enqueue(new Callback<List<LoginTeachers>>() {
                                        @Override
                                        public void onResponse(Call<List<LoginTeachers>> call, Response<List<LoginTeachers>> response) {
                                            if (response.isSuccessful()) {
                                                List<LoginTeachers> all_teachers;
                                                all_teachers = new ArrayList<>();
                                                all_teachers.addAll(response.body());
                                                int i = 0;
                                                for (LoginTeachers a : all_teachers) {
                                                    if (a.getIDTeacher() == ID) {
                                                        i = 1;
                                                        Toast.makeText(FirstStepRegistration.this, "You are have an account", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                if (i == 0) {
                                                    Intent intent = new Intent(FirstStepRegistration.this, SecondStepRegistration.class);
                                                    intent.putExtra("id_teacher", (Integer) teacher.getIDTeacher());
                                                    intent.putExtra("name_teacher", (String) teacher.getName());
                                                    intent.putExtra("surname_teacher", (String) teacher.getSurname());
                                                    startActivity(intent);
                                                }
                                            }
                                            call.cancel();

                                        }

                                        @Override
                                        public void onFailure(Call<List<LoginTeachers>> call, Throwable t) {
                                            Toast.makeText(FirstStepRegistration.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }

                                    });
                                } else {
                                    Toast.makeText(FirstStepRegistration.this, "You are not a FIT person", Toast.LENGTH_SHORT).show();
                                }

                                call.cancel();

                            }

                            @Override
                            public void onFailure(Call<Teachers> call, Throwable t) {
                                Toast.makeText(FirstStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }

                        })
                        ;
                    }

                    call.cancel();

                }

                @Override
                public void onFailure(Call<Students> call, Throwable t) {
                    Toast.makeText(FirstStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }

            })
            ;

        } else {
            Toast.makeText(FirstStepRegistration.this, enter_ID.getText(), Toast.LENGTH_SHORT).show();

        }
    }
}

