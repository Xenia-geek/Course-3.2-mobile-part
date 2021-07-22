package com.example.courseproject.Teachers_Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.LoginOrRegistrationActivity;
import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.ListLabTeachers;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.Semesters;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;
import com.example.courseproject.adapters.NameLabsTeacherAdapter;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.Teacher;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityTeacher extends AppCompatActivity {

    Cursor info_teacher;
    Button update_user_info;
    Button reload_info;
    Button exit_from_teacher;
    Cursor add_lab;
    SQLiteDatabase db;
    Teacher find_info;
    List<ListLabs> list_of_filter_labs = new ArrayList();
    private List<Lab> labs = new ArrayList();
    private List<Lab> find_for_stud = new ArrayList();
    private List<Lab> IDGroupList = new ArrayList();
    Date now = null;
    Date first_start = null;
    Date first_end = null;
    Date second_start = null;
    Date second_end = null;
    NameLabsTeacherAdapter labteachAdapter;

    Cursor all_info_to_go_update;
    Teacher teacher;

    int ID_Sem;
    int Group;
    int Course;
    int IDCourse;
    int SubGroup;
    String Name_stud;
    String Surname_stud;
    int ID_stud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);

        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        TextView login = findViewById(R.id.login_teacher);
        TextView name = findViewById(R.id.name_teacher);
        TextView email = findViewById(R.id.email_teacher);
        TextView about_me = findViewById(R.id.about_me_teacher);

        JSONPlaceHolderApi apiJSON = NetworkService.getApi();


        info_teacher = db.rawQuery("Select Login, Name, Surname, IDTeacher, Email, AboutMe from Teacher ", null);
        if (info_teacher.moveToFirst()) {
            while (!info_teacher.isClosed()) {
                find_info = new Teacher(info_teacher.getString(0), info_teacher.getString(1), info_teacher.getString(2), info_teacher.getInt(3), info_teacher.getString(4), info_teacher.getString(5));
                if (!info_teacher.isLast()) {
                    info_teacher.moveToNext();
                } else {
                    info_teacher.close();
                }
            }
        }

        login.setText(find_info.Login);
        name.setText(find_info.Surname + " " + find_info.Name);
        email.setText("Email: " + find_info.Email);
        about_me.setText("About me: " + find_info.About_Me);


        add_lab = db.rawQuery("Select DISTINCT NameLab, IDLab from ListLab1 ORDER BY NameLab ", null);

        labs.clear();
        if (add_lab.moveToFirst()) {
            while (!add_lab.isClosed()) {
                labs.add(new Lab(add_lab.getString(0), add_lab.getInt(1)));
                if (!add_lab.isLast()) {
                    add_lab.moveToNext();
                } else {
                    add_lab.close();
                }
            }
        }
        ListView listLabs = findViewById(R.id.list_of_labs_teacher);

        labteachAdapter = new NameLabsTeacherAdapter(MainActivityTeacher.this, R.layout.list_item_labs_teacher, labs);
        listLabs.setAdapter(labteachAdapter);
        labteachAdapter.notifyDataSetChanged();


        //////////////////UPDATE INFO FROM SERVER///////////////////////
        ///////////////////////////////////////////////////////////////

        String Login = login.getText().toString();

       /* apiJSON.getTeacherByLogin(Login).enqueue(new Callback<LoginTeachers>() {
            @Override
            public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                if (response.isSuccessful()) {
                    LoginTeachers teachers = response.body();
                    int Password = teachers.getPassword();
                    int ID = teachers.getIDTeacher();
                    String Email = teachers.getEmail();
                    String About_Me = teachers.getAboutMe();
                    if (ID != 0) {
                        apiJSON.getTeacher(ID).enqueue(new Callback<Teachers>() {
                            @Override
                            public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                if (response.isSuccessful()) {
                                    Teachers teacher_by_id = response.body();
                                    String Name = teacher_by_id.getName().toString();
                                    String Surname = teacher_by_id.getSurname().toString();

                                    db.execSQL("update Teacher set Login='" + Login
                                            + "' ,  Password=" + Password + " , IDTeacher=" + ID + " , Email='"
                                            + Email + "', AboutMe='" + About_Me + "' , Name ='" + Name
                                            + "', Surname='" + Surname + "'"
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
                                                                Toast.makeText(MainActivityTeacher.this, "Invalid date", Toast.LENGTH_SHORT).show();
                                                            }


                                                            db.execSQL("DELETE FROM ListLab");

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


                                                                                                        db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                        );
                                                                                                    }
                                                                                                } else if (now.after(second_start) && now.before(second_end)) {
                                                                                                    if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 2) {


                                                                                                        db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                        );
                                                                                                    }
                                                                                                } else if (now.before(second_start) || now.after(first_end)) {
                                                                                                    if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 1) {


                                                                                                        db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                        );
                                                                                                    }
                                                                                                } else if (now.before(first_start) || now.after(second_end)) {
                                                                                                    if (lab.getIDLab() == l_o_a_t.getIDLab() && lab.getIDSem() == 2) {


                                                                                                        db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                + lab.getIDLab() + ",'" + lab.getNameLab() + "'," + lab.getQuantity()
                                                                                                                + "," + l_o_a_t.getIDTeacher() + ",'" + l_o_a_t.getWeekName() + "',"
                                                                                                                + l_o_a_t.getIDGroup() + "," + lab.getIDSem() + ")"
                                                                                                        );
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            Cursor find_all_lab_for_add_student;
                                                                                            find_all_lab_for_add_student = db.rawQuery("SELECT IDGroup from ListLab", null);
                                                                                            if (find_all_lab_for_add_student.moveToFirst()) {
                                                                                                while (!find_all_lab_for_add_student.isClosed()) {
                                                                                                    find_for_stud.add(new Lab(find_all_lab_for_add_student.getInt(0)));
                                                                                                    if (!find_all_lab_for_add_student.isLast()) {
                                                                                                        find_all_lab_for_add_student.moveToNext();
                                                                                                    } else {
                                                                                                        find_all_lab_for_add_student.close();
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                            apiJSON.getAllStudents().enqueue(new Callback<List<Students>>() {
                                                                                                @Override
                                                                                                public void onResponse(Call<List<Students>> call, Response<List<Students>> response) {
                                                                                                    if (response.isSuccessful()) {
                                                                                                        List<Students> all_stud = response.body();
                                                                                                        for (Students group_stud : all_stud) {

                                                                                                            for (Lab id_group_to_find : find_for_stud) {

                                                                                                                apiJSON.getGroup(id_group_to_find.IDGroup).enqueue(new Callback<Group>() {
                                                                                                                    @Override
                                                                                                                    public void onResponse(Call<Group> call, Response<Group> response) {
                                                                                                                        if (response.isSuccessful()) {
                                                                                                                            Group group_info = response.body();
                                                                                                                            Group = group_info.getNumberGroup();
                                                                                                                            SubGroup = group_info.getIDSubGroup();
                                                                                                                            IDCourse = group_info.getIDCource();
                                                                                                                            apiJSON.getCourse(IDCourse).enqueue(new Callback<com.example.courseproject.Retrofit.POJO.Course>() {
                                                                                                                                @Override
                                                                                                                                public void onResponse(Call<Course> call, Response<Course> response) {
                                                                                                                                    if (response.isSuccessful()) {

                                                                                                                                        Course course_info = response.body();
                                                                                                                                        Course = course_info.getNumberCource();
                                                                                                                                        ///////find students

                                                                                                                                        if (group_stud.getIDGroup() == id_group_to_find.IDGroup) {
                                                                                                                                            Name_stud = group_stud.getName();
                                                                                                                                            Surname_stud = group_stud.getSurname();
                                                                                                                                            ID_stud = group_stud.getIDStudent();

                                                                                                                                            Cursor stud_find_in_bd;
                                                                                                                                            stud_find_in_bd = db.rawQuery("SELECT Name from GroupInfoForTeacher WHERE IDStudent=" + ID_stud, null);
                                                                                                                                            if (!stud_find_in_bd.moveToFirst()) {

                                                                                                                                                db.execSQL("insert into GroupInfoForTeacher(IDStudent, Cource, Groups, SubGroup, ID_Group, Name,Surname) VALUES("
                                                                                                                                                        + ID_stud + "," + Course + "," + Group
                                                                                                                                                        + "," + SubGroup + "," + id_group_to_find.IDGroup + ",'"
                                                                                                                                                        + Name_stud + "','" + Surname_stud + "')"
                                                                                                                                                );
                                                                                                                                            }


                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    call.cancel();
                                                                                                                                }

                                                                                                                                @Override
                                                                                                                                public void onFailure(Call<Course> call, Throwable t) {
                                                                                                                                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                                                    call.cancel();
                                                                                                                                }
                                                                                                                            });
                                                                                                                        }
                                                                                                                        call.cancel();
                                                                                                                    }

                                                                                                                    @Override
                                                                                                                    public void onFailure(Call<Group> call, Throwable t) {
                                                                                                                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
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
                                                                                                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                    call.cancel();
                                                                                                }
                                                                                            });

                                                                                        }


                                                                                        call.cancel();
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                    call.cancel();
                                                                }
                                                            });


                                                        }
                                                        call.cancel();
                                                    }

                                                    @Override

                                                    public void onFailure(Call<Semesters> call, Throwable t) {
                                                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                        call.cancel();
                                                    }
                                                });
                                            }
                                            call.cancel();
                                        }

                                        public void onFailure(Call<Semesters> call, Throwable t) {
                                            Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }
                                    });


                                } else {

                                    Toast.makeText(MainActivityTeacher.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<Teachers> call, Throwable t) {
                                Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });
                    }
                } else {
                    try {
                        db.execSQL("DELETE FROM Teacher");
                        db.execSQL("DELETE FROM ListLab ");
                        Toast.makeText(MainActivityTeacher.this, "You are deleted by admin", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivityTeacher.this, "Can't delete table", Toast.LENGTH_SHORT).show();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<LoginTeachers> call, Throwable t) {
                Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

        */

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String now_date = sdf.format(new Date());

        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.getDefault());
        String now_year = year.format(new Date());
        apiJSON.getTeacherByLogin(Login).enqueue(new Callback<LoginTeachers>() {
            @Override
            public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                if (response.isSuccessful()) {
                    LoginTeachers teachers = response.body();
                    int Password = teachers.getPassword();
                    int ID = teachers.getIDTeacher();
                    String Email = teachers.getEmail();
                    String About_Me = teachers.getAboutMe();
                    if (ID != 0) {
                        apiJSON.getTeacher(ID).enqueue(new Callback<Teachers>() {
                            @Override
                            public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                if (response.isSuccessful()) {
                                    Teachers teacher_by_id = response.body();
                                    String Name = teacher_by_id.getName().toString();
                                    String Surname = teacher_by_id.getSurname().toString();

                                    db.execSQL("update Teacher set Login='" + Login
                                            + "' ,  Password=" + Password + " , IDTeacher=" + ID + " , Email='"
                                            + Email + "', AboutMe='" + About_Me + "' , Name ='" + Name
                                            + "', Surname='" + Surname + "'"
                                    );

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
                                                                Toast.makeText(MainActivityTeacher.this, "Invalid date", Toast.LENGTH_SHORT).show();
                                                            }

                                                            if (now.before(first_end) && now.after(first_start)) {
                                                                ID_Sem = 1;
                                                            }
                                                            if (now.after(second_start) && now.before(second_end)) {
                                                                ID_Sem = 2;
                                                            }
                                                            if (now.before(second_start) && now.after(first_end)) {
                                                                ID_Sem = 1;
                                                            }
                                                            if (now.before(first_start) && now.after(second_end)) {
                                                                ID_Sem = 2;
                                                            }


                                                            ///////////////////////


                                                            apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                @Override
                                                                public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                    if (response.isSuccessful()) {
                                                                        List<ListLabs> all_labs = response.body();
                                                                        for (ListLabs one_lab : all_labs) {
                                                                            if (one_lab.getIDSem() == ID_Sem) {
                                                                                int IDLab = one_lab.getIDLab();
                                                                                String NameLab = one_lab.getNameLab();
                                                                                int Quantity = one_lab.getQuantity();
                                                                                int ID_Cource = one_lab.getIDCource();
                                                                                String ID_Speciality = one_lab.getIDSpeciality();
                                                                                apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                        if (response.isSuccessful()) {
                                                                                            List<ListLabTeachers> all_labs_teacher = response.body();
                                                                                            for (ListLabTeachers one_lab_teacher : all_labs_teacher) {
                                                                                                if (IDLab == one_lab_teacher.getIDLab() && one_lab_teacher.getIDTeacher() == ID) {
                                                                                                    int ID_Group = one_lab_teacher.getIDGroup();
                                                                                                    String WeekName = one_lab_teacher.getWeekName();
                                                                                                    Cursor find_somthing_inListLab;
                                                                                                    find_somthing_inListLab = db.rawQuery("SELECT NameLab FROM ListLab1 WHERE IDLab=" + IDLab, null);
                                                                                                    if (find_somthing_inListLab.moveToFirst()) {
                                                                                                        db.execSQL("delete from ListLab1 WHERE IDLab=" + IDLab);
                                                                                                    }
                                                                                                    db.execSQL("insert into ListLab1(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                            + IDLab + ",'" + NameLab + "'," + Quantity
                                                                                                            + "," + ID + ",'" + WeekName + "',"
                                                                                                            + ID_Group + "," + ID_Sem + ")"
                                                                                                    );
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        call.cancel();
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                        call.cancel();
                                                                                    }
                                                                                });
                                                                            }
                                                                        }
                                                                    }
                                                                    call.cancel();
                                                                }

                                                                @Override
                                                                public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                    call.cancel();
                                                                }
                                                            });
                                                        }
                                                        call.cancel();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Semesters> call, Throwable t) {
                                                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                        call.cancel();
                                                    }
                                                });
                                            }
                                            call.cancel();
                                        }

                                        @Override
                                        public void onFailure(Call<Semesters> call, Throwable t) {
                                            Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                            call.cancel();
                                        }
                                    });
                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<Teachers> call, Throwable t) {
                                Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<LoginTeachers> call, Throwable t) {
                Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });
        Cursor GetMyIDGroups;
        GetMyIDGroups = db.rawQuery("SELECT IDGroup FROM ListLab1", null);
        if (GetMyIDGroups.moveToFirst()) {
            while (!GetMyIDGroups.isClosed()) {
                IDGroupList.add(new Lab(GetMyIDGroups.getInt(0)));
                if (!GetMyIDGroups.isLast()) {
                    GetMyIDGroups.moveToNext();
                } else {
                    GetMyIDGroups.close();
                }
            }
        }
        for (Lab group_info : IDGroupList) {
            int Group_ID = group_info.IDGroup;
            apiJSON.getGroup(Group_ID).enqueue(new Callback<com.example.courseproject.Retrofit.POJO.Group>() {
                @Override
                public void onResponse(Call<com.example.courseproject.Retrofit.POJO.Group> call, Response<com.example.courseproject.Retrofit.POJO.Group> response) {
                    if (response.isSuccessful()) {
                        com.example.courseproject.Retrofit.POJO.Group group = response.body();
                        int Course_ID_of_group = group.getIDCource();
                        int SubGroup_of_group = group.getIDSubGroup();
                        int Group_numb_of_group = group.getNumberGroup();
                        String Speciality_of_group = group.getIDSpeciality().toString();
                        if (Course_ID_of_group != 0) {
                            apiJSON.getCourse(Course_ID_of_group).enqueue(new Callback<com.example.courseproject.Retrofit.POJO.Course>() {
                                @Override
                                public void onResponse(Call<com.example.courseproject.Retrofit.POJO.Course> call, Response<com.example.courseproject.Retrofit.POJO.Course> response) {
                                    if (response.isSuccessful()) {
                                        com.example.courseproject.Retrofit.POJO.Course course = response.body();

                                        int Cource_of_roup = course.getNumberCource();

                                        Cursor find_something_in_group_info;
                                        find_something_in_group_info = db.rawQuery("SELECT Course FROM GroupInfoForTeacher WHERE IDGroup=" + Group_ID, null);
                                        if (find_something_in_group_info.moveToFirst()) {
                                            db.execSQL("DELETE FROM GroupInfoForTeacher WHERE IDGroup=" + Group_ID);
                                        }
                                        db.execSQL("insert into GroupInfoForTeacher(IDGroup,Course,Groupss,SubGroup) VALUES(" +
                                                +Group_ID + "," + Cource_of_roup + "," + Group_numb_of_group
                                                + "," + SubGroup_of_group + ")"
                                        );
                                    }
                                    call.cancel();
                                }

                                @Override
                                public void onFailure(Call<com.example.courseproject.Retrofit.POJO.Course> call, Throwable t) {
                                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                    call.cancel();
                                }
                            });
                        }
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<com.example.courseproject.Retrofit.POJO.Group> call, Throwable t) {
                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
            apiJSON.getAllStudents().enqueue(new Callback<List<Students>>() {
                @Override
                public void onResponse(Call<List<Students>> call, Response<List<Students>> response) {
                    if (response.isSuccessful()) {
                        List<Students> all_students = response.body();
                        for (Students one_student : all_students) {
                            if (one_student.getIDGroup() == Group_ID) {
                                String Name_student = one_student.getName();
                                String Surname_student = one_student.getSurname();
                                int IDStudent_for_teach = one_student.getIDStudent();
                                Cursor find_something_in_student_info;
                                find_something_in_student_info = db.rawQuery("SELECT Name FROM StudentInfoForTeacher WHERE IDStudent=" + IDStudent_for_teach, null);
                                if (find_something_in_student_info.moveToFirst()) {
                                    db.execSQL("DELETE FROM StudentInfoForTeacher WHERE IDStudent=" + IDStudent_for_teach);
                                }
                                db.execSQL("insert into StudentInfoForTeacher(IDStudent,IDGroup,Name,Surname) VALUES(" +
                                        +IDStudent_for_teach + "," + Group_ID + ",'" + Name_student
                                        + "','" + Surname_student + "')"
                                );
                            }
                        }
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<List<Students>> call, Throwable t) {
                    Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }


        //reload button

        reload_info = findViewById(R.id.button_reload_teacher);

        reload_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityTeacher.this, MainActivityTeacher.class);
                startActivity(intent);
            }
        });

        //exit button

        exit_from_teacher = findViewById(R.id.exit_button_teacher);

        exit_from_teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("DELETE FROM Teacher");
                db.execSQL("DELETE FROM ListLab1 ");
                db.execSQL("DELETE FROM StudentInfoForTeacher");
                db.execSQL("DELETE FROM GroupInfoForTeacher");
                Intent intent = new Intent(MainActivityTeacher.this, LoginOrRegistrationActivity.class);
                startActivity(intent);
            }
        });

        //UpdateButton

        update_user_info = findViewById(R.id.update_button_teacher);
        update_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
                all_info_to_go_update = db.rawQuery("Select Login from Teacher", null);
                if (all_info_to_go_update.moveToFirst()) {
                    while (!all_info_to_go_update.isClosed()) {
                        teacher = new Teacher(all_info_to_go_update.getString(0));
                        if (!all_info_to_go_update.isLast()) {
                            all_info_to_go_update.moveToNext();
                        } else {
                            all_info_to_go_update.close();
                        }
                    }
                }
                apiJSON.getTeacherByLogin(teacher.Login).enqueue(new Callback<LoginTeachers>() {
                    @Override
                    public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                        if (response.isSuccessful()) {
                            LoginTeachers teach = response.body();

                            int Password_for_update = teach.getPassword();
                            int IDTeacher_for_update = teach.getIDTeacher();
                            String Email_for_update = teach.getEmail();
                            String About_Me_for_update = teach.getAboutMe();


                            Intent intent = new Intent(MainActivityTeacher.this, UpdateLoginInfoActivity.class);
                            intent.putExtra("login", (String) teacher.Login);
                            intent.putExtra("password", (Integer) Password_for_update);
                            intent.putExtra("id_teacher", (Integer) IDTeacher_for_update);
                            intent.putExtra("email", (String) Email_for_update);
                            intent.putExtra("about_me", (String) About_Me_for_update);
                            startActivity(intent);
                        }
                        call.cancel();
                    }

                    @Override
                    public void onFailure(Call<LoginTeachers> call, Throwable t) {
                        Toast.makeText(MainActivityTeacher.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                        call.cancel();
                    }
                });


            }
        });

        //click on item list


        listLabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                info_teacher = db.rawQuery("Select Login, IDTeacher from Teacher ", null);
                if (info_teacher.moveToFirst()) {
                    while (!info_teacher.isClosed()) {
                        find_info = new Teacher(info_teacher.getString(0), info_teacher.getInt(1));
                        if (!info_teacher.isLast()) {
                            info_teacher.moveToNext();
                        } else {
                            info_teacher.close();
                        }
                    }
                }

                int id_teac = find_info.IDTeacher;


                Intent intent = new Intent(MainActivityTeacher.this, InfoOfLabTeacher.class);
                intent.putExtra("id", (int) labs.get(position).IDLab);
                intent.putExtra("id_techer", id_teac);
                startActivity(intent);
            }
        });


    }


}
