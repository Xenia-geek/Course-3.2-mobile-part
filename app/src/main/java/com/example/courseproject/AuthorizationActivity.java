package com.example.courseproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;

import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.Course;
import com.example.courseproject.Retrofit.POJO.Group;
import com.example.courseproject.Retrofit.POJO.ListLabTeachers;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.Semesters;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;
import com.example.courseproject.Teachers_Activity.MainActivityTeacher;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.PlansPass;
import com.example.courseproject.db.units.Student;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthorizationActivity extends AppCompatActivity {

    SQLiteDatabase db;

    private List<Lab> list_lab_find = new ArrayList();
    List<Date> date_passes = new ArrayList<>();

    PlansPass plan_pass_for_one_of_date;
    PlansPass plan;

    Cursor findLab;
    Cursor find_plan_pass_for_date;

    EditText enter_login;
    EditText enter_password;
    String Login;
    int Password;
    int ID;
    String Email;
    String About_Me;
    int ID_Groups;
    String Name;
    String Surname;
    int Course_ID;
    int SubGroup;
    int Group;
    int Cource;
    int ID_Sem;
    String Speciality;

    Date now = null;
    Date first_start = null;
    Date first_end = null;
    Date second_start = null;
    Date second_end = null;

    List<Semesters> all_semester = new ArrayList();
    Group infoofgroup;
    List<ListLabs> list_all_labs = new ArrayList();
    List<ListLabs> list_all_labs_filter = new ArrayList();
    List<ListLabTeachers> list_all_lab_teacher = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

    }

    JSONPlaceHolderApi apiJSON = NetworkService.getApi();


    public void Login(View view) {

        enter_login = (EditText) findViewById(R.id.login_value);
        enter_password = (EditText) findViewById(R.id.password_value);
        Login = enter_login.getText().toString();
        if (Login != null) {

            apiJSON.getStudentByLogin(Login).enqueue(new Callback<LoginStudents>() {
                @Override
                public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                    if (response.isSuccessful()) {
                        LoginStudents students = response.body();
                        if (enter_password != null) {
                            String password_response = String.valueOf(students.getPassword());
                            if (password_response.equals(enter_password.getText().toString())) {

                                Password = students.getPassword();
                                ID = students.getIDStudent();
                                Email = students.getEmail();
                                About_Me = students.getAboutMe();

                                if (ID != 0) {
                                    apiJSON.getStudent(ID).enqueue(new Callback<Students>() {
                                        @Override
                                        public void onResponse(Call<Students> call, Response<Students> response) {
                                            if (response.isSuccessful()) {
                                                Students student_by_id = response.body();
                                                ID_Groups = student_by_id.getIDGroup();
                                                Name = student_by_id.getName().toString();
                                                Surname = student_by_id.getSurname().toString();
                                                if (ID_Groups != 0) {
                                                    apiJSON.getGroup(ID_Groups).enqueue(new Callback<Group>() {
                                                        @Override
                                                        public void onResponse(Call<Group> call, Response<Group> response) {
                                                            if (response.isSuccessful()) {
                                                                Group group = response.body();
                                                                Course_ID = group.getIDCource();
                                                                SubGroup = group.getIDSubGroup();
                                                                Group = group.getNumberGroup();
                                                                Speciality = group.getIDSpeciality().toString();

                                                                if (Course_ID != 0) {
                                                                    apiJSON.getCourse(Course_ID).enqueue(new Callback<Course>() {
                                                                        @Override
                                                                        public void onResponse(Call<Course> call, Response<Course> response) {
                                                                            if (response.isSuccessful()) {
                                                                                Course course = response.body();

                                                                                Cource = course.getNumberCource();

                                                                                //////////////////
                                                                                //insert student//
                                                                                //////////////////
                                                                                db.execSQL("insert into Student(Login,Password,IDStudent,Email,AboutMe,Name,Surname,Groups,SubGroup,Course,Speciality) VALUES('"
                                                                                        + Login + "'," + Password + "," + ID + ",'" + Email + "','" + About_Me + "','"
                                                                                        + Name + "','" + Surname + "'," + Group + "," + SubGroup + "," + Cource + ",'" + Speciality + "')"
                                                                                );

                                                                                db.execSQL("insert into ListStudents(IDStudent,IDGroup) VALUES(" + ID + "," + ID_Groups + ")");

                                                                                try {
                                                                                    int a = new AsyncTaskMy().execute().get();
                                                                                    if (a == 1) {

                                                                                        Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
                                                                                        startActivity(intent);
                                                                                    } else {
                                                                                        Toast.makeText(AuthorizationActivity.this, "Stupid gay", Toast.LENGTH_SHORT).show();

                                                                                    }

                                                                                } catch (ExecutionException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (InterruptedException e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            } else {
                                                                                Toast.makeText(AuthorizationActivity.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            call.cancel();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Course> call, Throwable t) {
                                                                            Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                            call.cancel();
                                                                        }
                                                                    });


                                                                }

                                                            } else {
                                                                Toast.makeText(AuthorizationActivity.this, "Something went wrong with IDGroup", Toast.LENGTH_SHORT).show();
                                                            }
                                                            call.cancel();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Group> call, Throwable t) {
                                                            Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                            call.cancel();
                                                        }
                                                    });


                                                }

                                            } else {
                                                Toast.makeText(AuthorizationActivity.this, "Something went wrong with IDStudent", Toast.LENGTH_SHORT).show();

                                            }
                                            call.cancel();
                                        }

                                        @Override
                                        public void onFailure(Call<Students> call, Throwable t) {
                                            Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }
                                    });


                                }
                            } else {
                                Toast.makeText(AuthorizationActivity.this, "Password is not correct", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AuthorizationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        /////////////////////////
                        /////You a teacher?//////
                        /////////////////////////
                        apiJSON.getTeacherByLogin(Login).enqueue(new Callback<LoginTeachers>() {
                            @Override
                            public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                                if (response.isSuccessful()) {
                                    LoginTeachers teachers = response.body();
                                    if (enter_password != null) {
                                        String password_response = String.valueOf(teachers.getPassword());
                                        if (password_response.equals(enter_password.getText().toString())) {
                                            Password = teachers.getPassword();
                                            ID = teachers.getIDTeacher();
                                            Email = teachers.getEmail();
                                            About_Me = teachers.getAboutMe();

                                            if (ID != 0) {
                                                apiJSON.getTeacher(ID).enqueue(new Callback<Teachers>() {
                                                    @Override
                                                    public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                                        if (response.isSuccessful()) {
                                                            Teachers teacher_by_id = response.body();
                                                            String Name = teacher_by_id.getName().toString();
                                                            String Surname = teacher_by_id.getSurname().toString();

                                                            db.execSQL("insert into Teacher(Login,Password,IDTeacher,Email,AboutMe,Name,Surname) VALUES('"
                                                                    + Login + "'," + Password + "," + ID + ",'" + Email + "','" + About_Me + "','"
                                                                    + Name + "','" + Surname + "')"
                                                            );


                                                            ///////////
                                                            //sem//
                                                            //////////

                                                            ///check semestr

                                                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                                            String now_date = sdf.format(new Date());

                                                            SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                                                            String now_year = year.format(new Date());

                                                            apiJSON.getSemesterByID(1).enqueue(new Callback<Semesters>() {
                                                                @Override
                                                                public void onResponse(Call<Semesters> call, Response<Semesters> response) {
                                                                    if (response.isSuccessful()) {
                                                                        Semesters first_sem = response.body();
                                                                        String first_date_start = first_sem.getMonthDayStart() + "." + now_year;
                                                                        String first_date_end = first_sem.getMonthDayEnd() + "." + now_year;
                                                                        apiJSON.getSemesterByID(2).enqueue(new Callback<Semesters>() {
                                                                            @Override
                                                                            public void onResponse(Call<Semesters> call, Response<Semesters> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Semesters second_sem = response.body();
                                                                                    String second_date_start = second_sem.getMonthDayStart() + "." + now_year;
                                                                                    String second_date_end = second_sem.getMonthDayEnd() + "." + now_year;

                                                                                    SimpleDateFormat format_date = new SimpleDateFormat();
                                                                                    format_date.applyPattern("dd.MM.yyyy");
                                                                                    try {
                                                                                        now = format_date.parse(now_date);
                                                                                        first_start = format_date.parse(first_date_start);
                                                                                        first_end = format_date.parse(first_date_end);
                                                                                        second_start = format_date.parse(second_date_start);
                                                                                        second_end = format_date.parse(second_date_end);
                                                                                    } catch (Exception e) {
                                                                                        Toast.makeText(AuthorizationActivity.this, "Invalid date", Toast.LENGTH_SHORT).show();
                                                                                    }


                                                                                    db.execSQL("DELETE FROM ListLab1");

                                                                                    apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                            if (response.isSuccessful()) {
                                                                                                List<ListLabTeachers> list_of_all_teachers_lab = response.body();
                                                                                                //////

                                                                                                for (ListLabTeachers l_o_a_t : list_of_all_teachers_lab) {
                                                                                                    if (l_o_a_t.getIDTeacher() == ID) {

                                                                                                        apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                                                                if (response.isSuccessful()) {
                                                                                                                    List<ListLabs> list_of_all_labs = response.body();
                                                                                                                    for (ListLabs lab : list_of_all_labs) {
                                                                                                                        if (now.after(first_start) && now.before(first_end)) {


                                                                                                                            if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 1) {


                                                                                                                                db.execSQL("insert into ListLab1(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                                        + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                                        + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                                                );
                                                                                                                            }
                                                                                                                        } else if (now.after(second_start) && now.before(second_end)) {
                                                                                                                            if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 2) {


                                                                                                                                db.execSQL("insert into ListLab1(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                                        + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                                        + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                                                );
                                                                                                                            }
                                                                                                                        } else if (now.before(second_start) || now.after(first_end)) {
                                                                                                                            if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 1) {


                                                                                                                                db.execSQL("insert into ListLab1(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                                        + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                                        + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                                                );
                                                                                                                            }
                                                                                                                        } else if (now.before(first_start) || now.after(second_end)) {
                                                                                                                            if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 2) {


                                                                                                                                db.execSQL("insert into ListLab1(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                                        + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                                        + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                                                );
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                    Intent intent = new Intent(AuthorizationActivity.this, MainActivityTeacher.class);
                                                                                                                    startActivity(intent);


                                                                                                                }
                                                                                                                call.cancel();
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                                                Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                                call.cancel();
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            call.cancel();
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                            Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                            call.cancel();
                                                                                        }
                                                                                    });


                                                                                }
                                                                                call.cancel();
                                                                            }

                                                                            @Override

                                                                            public void onFailure(Call<Semesters> call, Throwable t) {
                                                                                Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                call.cancel();
                                                                            }
                                                                        });
                                                                    }
                                                                    call.cancel();
                                                                }

                                                                public void onFailure(Call<Semesters> call, Throwable t) {
                                                                    Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                    call.cancel();
                                                                }
                                                            });


                                                        } else {

                                                            Toast.makeText(AuthorizationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                                        }
                                                        call.cancel();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Teachers> call, Throwable t) {
                                                        Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                        call.cancel();
                                                    }
                                                });
                                            }
                                        } else {
                                            Toast.makeText(AuthorizationActivity.this, "Password is not correct", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(AuthorizationActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(AuthorizationActivity.this, "No one has registration ", Toast.LENGTH_SHORT).show();
                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<LoginTeachers> call, Throwable t) {
                                Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });

                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<LoginStudents> call, Throwable t) {
                    Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });

        }

    }



    class AsyncTaskMy extends AsyncTask<Void, Void, Integer> {

        ListStudents list_stud_id;
        SQLiteDatabase db;
        Cursor findID;
        Cursor findIDGroup;
        Student student;
        ListStudents ls;
        Cursor userCursor_u;
        int g = 1;
        //declare a delegate with type of protocol declared in this task

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Integer doInBackground(Void... voids) {


            db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
            Cursor userCursor = db.rawQuery("Select Login,Password,IDStudent,Email,AboutMe,Name,Surname,Groups,SubGroup,Course,Speciality from Student", null);

            Cursor findIDGroup = db.rawQuery("Select IDGroup from ListStudents", null);


            JSONPlaceHolderApi apiJSON = NetworkService.getApi();
            ///////

            if (findIDGroup.moveToFirst()) {
                while (!findIDGroup.isClosed()) {
                    ls = new ListStudents(findIDGroup.getInt(0));
                    if (!findIDGroup.isLast()) {
                        findIDGroup.moveToNext();
                    } else {
                        findIDGroup.close();
                    }
                }
                ID_Groups = ls.IDGroup;

                apiJSON.getGroup(ID_Groups).enqueue(new Callback<Group>() {
                    @Override
                    public void onResponse(Call<Group> call, Response<Group> response) {
                        if (response.isSuccessful()) {
                            infoofgroup = response.body();
                            Course_ID = infoofgroup.getIDCource();
                            Speciality = infoofgroup.getIDSpeciality();


                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                            String now_date_str = sdf.format(new Date());

                            SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                            String now_year = year.format(new Date());

                            apiJSON.getSemesterAll().enqueue(new Callback<List<Semesters>>() {
                                @Override
                                public void onResponse(Call<List<Semesters>> call, Response<List<Semesters>> response) {
                                    if (response.isSuccessful()) {
                                        all_semester = response.body();
                                        if (all_semester.size() > 0) {

                                            for (Semesters sem : all_semester) {
                                                String start_sem_str = sem.getMonthDayStart() + "." + now_year;
                                                String end_sem_str = sem.getMonthDayEnd() + "." + now_year;

                                                SimpleDateFormat format_date = new SimpleDateFormat();
                                                format_date.applyPattern("dd.MM.yyyy");
                                                try {
                                                    Date now_date = format_date.parse(now_date_str);
                                                    Date start_sem = format_date.parse(start_sem_str);
                                                    Date end_sem = format_date.parse(end_sem_str);
                                                    if (now_date.compareTo(end_sem) <= 0 && now_date.compareTo(start_sem) >= 0) {
                                                        ID_Sem = sem.getIDSem();
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            if (ID_Sem != 0) {
                                                apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                    @Override
                                                    public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                        if (response.isSuccessful()) {
                                                            list_all_labs = response.body();
                                                            for (ListLabs lab : list_all_labs) {
                                                                if (lab.getIDCource() == Course_ID && lab.getIDSpeciality().equals(Speciality) && lab.getIDSem() == ID_Sem) {
                                                                    list_all_labs_filter.add(lab);
                                                                } else {
                                                                    Toast.makeText(AuthorizationActivity.this, "No lab for your group", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                            if (list_all_labs_filter.size() != 0) {
                                                                apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                    @Override
                                                                    public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                        if (response.isSuccessful()) {
                                                                            list_all_lab_teacher = response.body();
                                                                            for (ListLabs lab : list_all_labs_filter) {
                                                                                for (ListLabTeachers lab_teacher : list_all_lab_teacher) {
                                                                                    if (lab.getIDLab() == lab_teacher.getIDLab() && lab_teacher.getIDGroup() == ID_Groups) {
                                                                                        int ID_Lab_add = lab.getIDLab();
                                                                                        String NameLab_add = lab.getNameLab();
                                                                                        int Quantity_lab_add = lab.getQuantity();
                                                                                        int ID_Teacher_lab_add = lab_teacher.getIDTeacher();
                                                                                        String WeekName_lab_add = lab_teacher.getWeekName();
                                                                                        int ID_Groups_lab_add = lab_teacher.getIDGroup();
                                                                                        int ID_Sem_lab_add = lab.getIDSem();

                                                                                        db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup, IDSem) VALUES("
                                                                                                + ID_Lab_add + ",'" + NameLab_add + "'," + Quantity_lab_add
                                                                                                + "," + ID_Teacher_lab_add + ",'" + WeekName_lab_add + "',"
                                                                                                + ID_Groups_lab_add + "," + ID_Sem_lab_add + ")"
                                                                                        );


                                                                                    }
                                                                                }
                                                                            }


                                                                        } else {
                                                                            Toast.makeText(AuthorizationActivity.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                                                        }


                                                                        call.cancel();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                        Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                        call.cancel();
                                                                    }
                                                                });


                                                            }


                                                        }


                                                        call.cancel();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                        Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                        call.cancel();
                                                    }
                                                });


                                            }
                                        }
                                    } else {
                                        Toast.makeText(AuthorizationActivity.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                    }
                                    call.cancel();
                                }

                                @Override
                                public void onFailure(Call<List<Semesters>> call, Throwable t) {
                                    Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                    call.cancel();
                                }
                            });
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<Group> call, Throwable t) {
                        Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });
            }


            //////
            if (userCursor.moveToFirst()) {
                while (!userCursor.isClosed()) {
                    student = new Student(userCursor.getString(0), userCursor.getInt(1), userCursor.getInt(2),
                            userCursor.getString(3), userCursor.getString(4), userCursor.getString(5),
                            userCursor.getString(6), userCursor.getInt(7), userCursor.getInt(8),
                            userCursor.getInt(9), userCursor.getString(10));
                    if (!userCursor.isLast()) {
                        userCursor.moveToNext();
                    } else {
                        userCursor.close();
                    }
                }

                Password = student.Password;
                ID = student.IDStudent;
                Email = student.Email;
                About_Me = student.About_Me;
                SubGroup = student.SubGroup;
                Group = student.Group;
                Speciality = student.Speciality;
                Cource = student.Course;

                findID = db.rawQuery("Select IDStudent, IDGroup from ListStudents", null);
                if (findID.moveToFirst()) {
                    while (!findID.isClosed()) {
                        list_stud_id = new ListStudents(findID.getInt(0), findID.getInt(1));
                        if (!findID.isLast()) {
                            findID.moveToNext();
                        } else {
                            findID.close();
                        }
                    }

                    int ID_Stud_f = list_stud_id.IDStudent;

                    apiJSON.getStudentPasses().enqueue(new Callback<List<StudentPassess>>() {
                        @Override
                        public void onResponse(Call<List<StudentPassess>> call, Response<List<StudentPassess>> response) {
                            if (response.isSuccessful()) {
                                List<StudentPassess> passed_student_info = response.body();
                                for (StudentPassess passed_of_stud : passed_student_info) {
                                    if (passed_of_stud.getIDStudent() == ID_Stud_f) {
                                        int IDLab_that_passed = passed_of_stud.getIDLab();
                                        int Quantity_passed = passed_of_stud.getPassedQuantity();

                                        db.execSQL("insert into StudentPass(IDLab, PassedQuantity) VALUES("
                                                + IDLab_that_passed + "," + Quantity_passed + ")"
                                        );
                                        g = 1;

                                    }
                                }
                            }
                            call.cancel();
                        }

                        @Override
                        public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                            Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                }


                //////////////////
                //insert listlab//
                //////////////////
                findLab = db.rawQuery("Select IDLab, Quantity, WeekDay, IDSem from ListLab", null);
                if (findLab.moveToFirst()) {
                    while (!findLab.isClosed()) {
                        list_lab_find.add(new Lab(findLab.getInt(0), findLab.getInt(1), findLab.getString(2), findLab.getInt(3)));
                        if (!findLab.isLast()) {
                            findLab.moveToNext();
                        } else {
                            findLab.close();
                        }
                    }
                    for (Lab fined_lab : list_lab_find) {
                        ////
                        int ID_Lab_for_pass = fined_lab.IDLab;
                        double all_quantity_of_lab = (double) fined_lab.Quantity;
                        String day_week_for_pass_lab = fined_lab.WeekDay;
                        int ID_Sem_of_pass_lab = fined_lab.IDSem;
                        int ID_Teacher_lab_add_to = fined_lab.IDTeacher;



                        ////
                        apiJSON.getSemesterByID(ID_Sem_of_pass_lab).enqueue(new Callback<Semesters>() {
                            @Override
                            public void onResponse(Call<Semesters> call, Response<Semesters> response) {
                                if (response.isSuccessful()) {
                                    Semesters my_sem_info = response.body();

                                    SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
                                    String now_year = year.format(new Date());

                                    String date_start_str = my_sem_info.getMonthDayStart() + "." + now_year;
                                    String date_end_str = my_sem_info.getMonthDayEnd() + "." + now_year;

                                    SimpleDateFormat format_date = new SimpleDateFormat();
                                    format_date.applyPattern("dd.MM.yyyy");

                                    try {

                                        Calendar start_sem_for_pass = Calendar.getInstance();
                                        start_sem_for_pass.setTime(format_date.parse(date_start_str));

                                        Calendar end_sem_for_pass = Calendar.getInstance();
                                        end_sem_for_pass.setTime(format_date.parse(date_end_str));


                                        Calendar date_to_increm = start_sem_for_pass;
                                        date_to_increm.roll(Calendar.DATE, 1);
                                        date_passes.clear();
                                        for (int i = 0; date_to_increm.compareTo(end_sem_for_pass) < 0; i++) {
                                            date_to_increm.add(Calendar.DATE, 1);
                                            Calendar date = date_to_increm;
                                            String week_of_day = (new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));

                                            if (week_of_day.equals(day_week_for_pass_lab)) {

                                                date_passes.add(date.getTime());

                                            }
                                        }



                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    g = 1;
                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<Semesters> call, Throwable t) {
                                Toast.makeText(AuthorizationActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });


                    }
                }
            }






            return g;
        }

        @Override
        protected void onPostExecute (Integer aVoid) {
            super.onPostExecute(aVoid);


        }

    }


}