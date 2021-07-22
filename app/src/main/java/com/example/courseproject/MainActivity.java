package com.example.courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.RatingStudents;
import com.example.courseproject.db.units.Student;
import com.example.courseproject.db.units.Teacher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Cursor userCursor;
    Cursor userCursor1;
    Cursor userCursor2;
    Teacher teachers;
    List<Student> student = new ArrayList();
    List<ListStudents> list_student = new ArrayList();
    private String login_student;
    private int student_id;
    private String login_teacher;
    private int teacher_id;
    private int id_group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            int a = new AsyncTaskMy().execute().get();
            if (a == 1) {
                setContentView(R.layout.activity_main);
                BottomNavigationView navView = findViewById(R.id.nav_view);
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_rating)
                        .build();
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
                NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navView, navController);
            } else {
                Toast.makeText(MainActivity.this, "Your BD is empty", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginOrRegistrationActivity.class);
                startActivity(intent);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    class AsyncTaskMy extends AsyncTask<Void, Void, Integer> {

        int g = 1;
        RatingStudents rating_student;
        ListStudents student_id_group;
        int ID_Group_of_Me;
        int Quantity_all;
        Cursor find_id_group_of_me;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Integer doInBackground(Void... voids) {

            db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
            userCursor = db.rawQuery("Select Login from Student", null);
            userCursor1 = db.rawQuery("Select Login from Teacher", null);
            JSONPlaceHolderApi apiJSON = NetworkService.getApi();
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    student.add(new Student(userCursor.getString(0)));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }
                for (Student find_student : student) {
                    login_student = find_student.Login;

                    userCursor2 = db.rawQuery("Select IDStudent from ListStudents", null);
                    if (userCursor2.moveToFirst()) {
                        while (!userCursor2.isClosed()) {
                            list_student.add(new ListStudents(userCursor2.getInt(0)));
                            if (!userCursor2.isLast()) {
                                userCursor2.moveToNext();
                            } else {
                                userCursor2.close();
                            }
                        }
                        for (ListStudents list_students_find : list_student) {
                            int id_group = list_students_find.IDStudent;

                            apiJSON.getStudentByLogin(login_student).enqueue(new Callback<LoginStudents>() {
                                @Override
                                public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                                    if (response.isSuccessful()) {
                                        LoginStudents student_by_login = response.body();
                                        int ID = student_by_login.getIDStudent();
                                        apiJSON.getStudent(id_group).enqueue(new Callback<Students>() {
                                            @Override
                                            public void onResponse(Call<Students> call, Response<Students> response) {
                                                if (response.isSuccessful()) {
                                                    g = 1;
                                                }

                                                call.cancel();
                                            }

                                            @Override
                                            public void onFailure(Call<Students> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                call.cancel();
                                            }
                                        });
                                    } else {
                                        try {
                                            db.execSQL("DELETE FROM Student WHERE Login='" + login_student + "'");
                                            db.execSQL("DELETE FROM ListStudents WHERE IDStudent=" + student_id);
                                            db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + id_group);
                                            Toast.makeText(MainActivity.this, "You are deleted by admin", Toast.LENGTH_SHORT).show();
                                            g = 0;
                                        } catch (Exception e) {
                                            Toast.makeText(MainActivity.this, "Can't delete table", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    call.cancel();
                                }

                                @Override
                                public void onFailure(Call<LoginStudents> call, Throwable t) {
                                    g = 1;
                                    call.cancel();
                                }
                            });
                        }
                    }
                }

            } else if (userCursor1.moveToFirst()) {
                while (!userCursor1.isClosed()) {
                    teachers = new Teacher(userCursor1.getString(0));
                    if (!userCursor1.isLast()) {
                        userCursor1.moveToNext();
                    } else {
                        userCursor1.close();
                    }
                }

                login_teacher = teachers.Login;
                apiJSON.getTeacherByLogin(login_teacher).enqueue(new Callback<LoginTeachers>() {
                    @Override
                    public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                        if (response.isSuccessful()) {

                            LoginTeachers teacher_by_login = response.body();
                            int Password = teacher_by_login.getPassword();
                            int ID_Teacher = teacher_by_login.getIDTeacher();
                            String Email = teacher_by_login.getEmail();
                            String About_Me = teacher_by_login.getAboutMe();

                            apiJSON.getTeacher(ID_Teacher).enqueue(new Callback<Teachers>() {
                                @Override
                                public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                    if (response.isSuccessful()) {

                                        Teachers teacher_by_id = response.body();

                                        String Name = teacher_by_id.getName();
                                        String Surname = teacher_by_id.getSurname();
                                        try {
                                            db.execSQL("insert into Teacher(Login,Password,IDTeacher,Email,AboutMe,Name,Surname) VALUES('"
                                                    + login_teacher + "'," + Password + "," + ID_Teacher + ",'" + Email + "','" + About_Me + "','"
                                                    + Name + "','" + Surname + "')");

                                            g = 1;
                                        } catch (Exception e) {
                                            Toast.makeText(MainActivity.this, "Not added to db", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                    }
                                    call.cancel();
                                }

                                @Override
                                public void onFailure(Call<Teachers> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                    call.cancel();
                                }
                            });

                        } else {
                            try {
                                db.execSQL("DELETE FROM Teacher WHERE Login='" + login_teacher + "'");
                                Toast.makeText(MainActivity.this, "You are deleted by admin", Toast.LENGTH_SHORT).show();
                                g = 0;
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "Can't delete table", Toast.LENGTH_SHORT).show();
                            }

                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<LoginTeachers> call, Throwable t) {
                        g = 1;
                        call.cancel();
                    }
                });
            } else {
                g = 0;
            }


            find_id_group_of_me = db.rawQuery("Select IDGroup from ListStudents", null);
            if (find_id_group_of_me.moveToFirst()) {
                while (!find_id_group_of_me.isClosed()) {
                    student_id_group = new ListStudents(find_id_group_of_me.getInt(0));
                    if (!find_id_group_of_me.isLast()) {
                        find_id_group_of_me.moveToNext();
                    } else {
                        find_id_group_of_me.close();
                    }
                }


                ID_Group_of_Me = student_id_group.IDGroup;
                apiJSON.getAllStudents().enqueue(new Callback<List<Students>>() {
                    @Override
                    public void onResponse(Call<List<Students>> call, Response<List<Students>> response) {
                        if (response.isSuccessful()) {
                            List<Students> all_students = response.body();
                            for (Students student_of_all : all_students) {
                                if (student_of_all.getIDGroup() == ID_Group_of_Me) {
                                    int ID_Student_of_Group = student_of_all.getIDStudent();
                                    String Name_Student_Of_Group = student_of_all.getName();
                                    String Surname_Student_Of_group = student_of_all.getSurname();
                                    Cursor find_stud = db.rawQuery("Select AllQuantity from  RatingOfStudents WHERE Name ='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'", null);
                                    if (find_stud.moveToFirst()) {
                                        db.execSQL("delete from RatingOfStudents where Name='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'");
                                    }
                                    apiJSON.getStudentPasses().enqueue(new Callback<List<StudentPassess>>() {
                                        @Override
                                        public void onResponse(Call<List<StudentPassess>> call, Response<List<StudentPassess>> response) {
                                            if (response.isSuccessful()) {
                                                List<StudentPassess> list_of_all_passed_labs = response.body();
                                                for (StudentPassess quantity_of_Student_of_Group : list_of_all_passed_labs) {
                                                    if (quantity_of_Student_of_Group.getIDStudent() == ID_Student_of_Group) {
                                                        Quantity_all = quantity_of_Student_of_Group.getPassedQuantity();
                                                        Cursor find_stud = db.rawQuery("Select AllQuantity from  RatingOfStudents WHERE Name ='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'", null);
                                                        if (find_stud.moveToFirst()) {
                                                            while (!find_stud.isClosed()) {
                                                                rating_student = new RatingStudents(find_stud.getInt(0));
                                                                if (!find_stud.isLast()) {
                                                                    find_stud.moveToNext();
                                                                } else {
                                                                    find_stud.close();
                                                                }
                                                            }
                                                            Quantity_all = Quantity_all + rating_student.PassedQuantity;
                                                            db.execSQL("delete from RatingOfStudents where Name='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'");
                                                        }

                                                        db.execSQL("insert into RatingOfStudents(Name, Surname, AllQuantity) VALUES('"
                                                                + Name_Student_Of_Group + "','" + Surname_Student_Of_group + "'," + Quantity_all
                                                                + ")"
                                                        );
                                                    }

                                                }

                                            }
                                            call.cancel();
                                        }

                                        @Override
                                        public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                                            Toast.makeText(MainActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }
                                    });
                                }
                            }
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<List<Students>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }


            return g;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);


        }
    }


}