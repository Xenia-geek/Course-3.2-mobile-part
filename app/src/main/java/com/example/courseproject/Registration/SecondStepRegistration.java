package com.example.courseproject.Registration;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.AuthorizationActivity;
import com.example.courseproject.MainActivity;
import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.Group;
import com.example.courseproject.Retrofit.POJO.ListLabTeachers;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.Course;
import com.example.courseproject.Retrofit.POJO.Semesters;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.PlansPass;
import com.example.courseproject.db.units.Student;
import com.example.courseproject.db.units.StudentPasses;

import java.text.ParseException;
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

public class SecondStepRegistration extends AppCompatActivity {

    TextView id;
    TextView name;
    TextView surname;
    TextView id_group_student;
    EditText login;
    EditText password;
    EditText email;
    EditText about_me;
    SQLiteDatabase db;


    Group infoofgroup;

    private List<Lab> list_lab_find = new ArrayList();
    private List<Lab> to_add_null_passed = new ArrayList();
    List<Date> date_passes = new ArrayList<>();

    PlansPass plan_pass_for_one_of_date;
    PlansPass plan;

    Cursor findLab;
    Cursor findLab_to_add_null;
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
    List<Semesters> all_semester = new ArrayList();
    List<ListLabs> list_all_labs = new ArrayList();
    List<ListLabs> list_all_labs_filter = new ArrayList();
    List<ListLabTeachers> list_all_lab_teacher = new ArrayList();


    List<ListLabs> list_of_filter_labs = new ArrayList();

    Date now = null;
    Date first_start = null;
    Date first_end = null;
    Date second_start = null;
    Date second_end = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_second);
        Intent intent = getIntent();
        //parameters Student
        int id_student = intent.getIntExtra("id_student", -1);
        String name_student = intent.getStringExtra("name");
        String surname_student = intent.getStringExtra("surname");
        int id_group = intent.getIntExtra("id_group", -1);
        //parameter Teacher
        int id_teacher = intent.getIntExtra("id_teacher", -1);
        String name_teacher = intent.getStringExtra("name_teacher");
        String surname_teacher = intent.getStringExtra("surname_teacher");
        name = (TextView) findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        id = (TextView) findViewById(R.id.id_user);
        id_group_student = (TextView) findViewById(R.id.id_group);
        if (id_student != -1 && id_group != -1 && name_student != null && surname_student != null) {
            name.setText(String.valueOf(name_student));
            surname.setText((String.valueOf(surname_student)));
            id.setText((String.valueOf(id_student)));
            id_group_student.setText((String.valueOf(id_group)));
        }
        if (id_teacher != -1 && name_teacher != null && surname_teacher != null) {
            name.setText(String.valueOf(name_teacher));
            surname.setText((String.valueOf(surname_teacher)));
            id.setText((String.valueOf(id_teacher)));
        }

        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

    }

    public void NextStep(View view) {
        login = (EditText) findViewById(R.id.user_login);
        name = (TextView) findViewById(R.id.name);
        surname = (TextView) findViewById(R.id.surname);
        Name = name.getText().toString();
        Surname = surname.getText().toString();
        id = (TextView) findViewById(R.id.id_user);
        JSONPlaceHolderApi apiJSON = NetworkService.getApi();
        ID = Integer.parseInt(id.getText().toString());
        Login = login.getText().toString();
        if (!Login.equals("")) {
            Toast.makeText(SecondStepRegistration.this, Login, Toast.LENGTH_SHORT).show();
            apiJSON.getStudentByLogin(Login).enqueue(new Callback<LoginStudents>() {
                @Override
                public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SecondStepRegistration.this, "Enter another login", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SecondStepRegistration.this, Login, Toast.LENGTH_SHORT).show();
                        //Teachers
                        apiJSON.getTeacherByLogin(Login).enqueue(new Callback<LoginTeachers>() {
                            @Override
                            public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                                if (response.isSuccessful()) {
                                    Toast.makeText(SecondStepRegistration.this, "Enter another login", Toast.LENGTH_SHORT).show();
                                } else {
                                    password = (EditText) findViewById(R.id.user_password);
                                    email = (EditText) findViewById(R.id.user_email);
                                    about_me = (EditText) findViewById(R.id.user_about);
                                    Email = email.getText().toString();
                                    About_Me = about_me.getText().toString();

                                    if (!password.getText().toString().equals("")) {
                                        Password = Integer.parseInt(password.getText().toString());
                                        if (ID != 0) {
                                            apiJSON.getStudent(ID).enqueue(new Callback<Students>() {
                                                @Override
                                                public void onResponse(Call<Students> call, Response<Students> response) {
                                                    if (response.isSuccessful()) {
                                                        Students student_by_id = response.body();
                                                        ID_Groups = student_by_id.getIDGroup();
                                                        if (ID_Groups != 0) {
                                                            apiJSON.getGroup(ID_Groups).enqueue(new Callback<Group>() {
                                                                @Override
                                                                public void onResponse(Call<Group> call, Response<Group> response) {
                                                                    if (response.isSuccessful()) {
                                                                        Group group = response.body();
                                                                        Course_ID = group.getIDCource();
                                                                        SubGroup = group.getIDSubGroup();
                                                                        Group = group.getNumberGroup();
                                                                        String Speciality = group.getIDSpeciality().toString();

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

                                                                                        LoginStudents add_info = new LoginStudents();
                                                                                        add_info.setLogin(Login);
                                                                                        add_info.setPassword(Password);
                                                                                        add_info.setIDStudent(ID);
                                                                                        add_info.setAboutMe(About_Me);
                                                                                        add_info.setEmail(Email);

                                                                                        apiJSON.addStudent(add_info).enqueue(new Callback<LoginStudents>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                                                                                                if (response.isSuccessful()) {


                                                                                                    try {
                                                                                                        int a = new AsyncForRegistration().execute().get();
                                                                                                        if (a == 1) {

                                                                                                            Intent intent = new Intent(SecondStepRegistration.this, MainActivity.class);
                                                                                                            startActivity(intent);
                                                                                                        } else {
                                                                                                            Toast.makeText(SecondStepRegistration.this, "Stupid gay", Toast.LENGTH_SHORT).show();

                                                                                                        }

                                                                                                    } catch (ExecutionException e) {
                                                                                                        e.printStackTrace();
                                                                                                    } catch (InterruptedException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }


                                                                                                }
                                                                                                call.cancel();
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<LoginStudents> call, Throwable t) {
                                                                                                Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                call.cancel();
                                                                                            }
                                                                                        });

                                                                                    } else {
                                                                                        Toast.makeText(SecondStepRegistration.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                                                                    }
                                                                                    call.cancel();
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<Course> call, Throwable t) {
                                                                                    Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                    call.cancel();
                                                                                }
                                                                            });


                                                                        }

                                                                    } else {
                                                                        Toast.makeText(SecondStepRegistration.this, "Something went wrong with IDGroup", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    call.cancel();
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Group> call, Throwable t) {
                                                                    Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                    call.cancel();
                                                                }
                                                            });


                                                        }

                                                    } else {
                                                        Toast.makeText(SecondStepRegistration.this, "Something went wrong with IDStudent", Toast.LENGTH_SHORT).show();

                                                    }
                                                    call.cancel();
                                                }

                                                @Override
                                                public void onFailure(Call<Students> call, Throwable t) {
                                                    Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                    call.cancel();
                                                }
                                            });


                                        }
                                    } else {
                                        Toast.makeText(SecondStepRegistration.this, "Password or Login not correct", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<LoginTeachers> call, Throwable t) {
                                Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<LoginStudents> call, Throwable t) {
                    Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }

            });

        } else {
            Toast.makeText(SecondStepRegistration.this, "Enter Login please", Toast.LENGTH_SHORT).show();

        }
    }


    class AsyncForRegistration extends AsyncTask<Void, Void, Integer> {

        ListStudents list_stud_id;
        SQLiteDatabase db;
        Cursor findID;
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
                int ID_Groups = ls.IDGroup;
                if (ID_Groups != 0) {

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
                                                                        Toast.makeText(SecondStepRegistration.this, "No lab for your group", Toast.LENGTH_SHORT).show();
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
                                                                                Toast.makeText(SecondStepRegistration.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                                                            }


                                                                            call.cancel();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                            Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                            call.cancel();
                                                                        }
                                                                    });


                                                                }


                                                            }


                                                            call.cancel();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                            Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                            call.cancel();
                                                        }
                                                    });


                                                }
                                            }
                                        } else {
                                            Toast.makeText(SecondStepRegistration.this, "Something went wrong with IDCourse", Toast.LENGTH_SHORT).show();

                                        }
                                        call.cancel();
                                    }

                                    @Override
                                    public void onFailure(Call<List<Semesters>> call, Throwable t) {
                                        Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                        call.cancel();
                                    }
                                });
                            }
                            call.cancel();
                        }

                        @Override
                        public void onFailure(Call<Group> call, Throwable t) {
                            Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
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
                                int a = 0;
                                for (StudentPassess passed_of_stud : passed_student_info) {

                                    if (passed_of_stud.getIDStudent() == ID_Stud_f) {
                                        int IDLab_that_passed = passed_of_stud.getIDLab();
                                        int Quantity_passed = passed_of_stud.getPassedQuantity();

                                        db.execSQL("insert into StudentPass(IDLab, PassedQuantity) VALUES("
                                                + IDLab_that_passed + "," + Quantity_passed + ")"
                                        );

                                    }

                                }

                            }
                            call.cancel();
                        }

                        @Override
                        public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                            Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
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
                        int ID_Teacher_lab_add = fined_lab.IDTeacher;

                        apiJSON.getTeacher(ID_Teacher_lab_add).enqueue(new Callback<Teachers>() {
                            @Override
                            public void onResponse(Call<Teachers> call, Response<Teachers> response) {
                                if (response.isSuccessful()) {
                                    Teachers teacher_info = response.body();

                                    String Name_Teacher = teacher_info.getName();
                                    String Surname_Teacher = teacher_info.getSurname();

                                    db.execSQL("insert into TeachersForLab(IDTeacher, Name, Surname) VALUES("
                                            + ID_Teacher_lab_add + ",'" + Name_Teacher + "','" + Surname_Teacher
                                            + "')"
                                    );
                                    publishProgress();
                                    g = 1;
                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<Teachers> call, Throwable t) {
                                Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });


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

                                        if (date_passes.size() > 0) {

                                            ///////////////////////////////////////
                                            //////// Logic Plan Pass add  /////////
                                            ///////////////////////////////////////

                                            int all_quantity_od_days_to_pass = date_passes.size() - 1;

                                            if (all_quantity_od_days_to_pass / all_quantity_of_lab > 0.5 && all_quantity_od_days_to_pass / all_quantity_of_lab <= 1) {

                                                double quantity = all_quantity_of_lab;
                                                for (Date date1 : date_passes) {

                                                    int Quantity_to_pass = 1;
                                                    SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                    String Date_pass = format1.format(date1);
                                                    int IDLab_pass = ID_Lab_for_pass;

                                                    db.execSQL("insert into PlansPass( IDLab, Date, Quantity) VALUES("
                                                            + IDLab_pass + ",'" + Date_pass
                                                            + "'," + Quantity_to_pass + ")"
                                                    );

                                                    g = 1;
                                                    quantity--;

                                                }
                                                while (quantity > 0) {
                                                    all_quantity_od_days_to_pass = date_passes.size() - 1;
                                                    List<Date> date_change_1 = date_passes;

                                                    if (all_quantity_od_days_to_pass / quantity > 1) {
                                                        int day_plus = (int) Math.round(all_quantity_od_days_to_pass / quantity);
                                                        if (day_plus > 0) {
                                                            day_plus--;
                                                        }

                                                        int previous_3 = 0;
                                                        for (int k = 0; k < date_change_1.size(); k++) {
                                                            if (quantity > 0) {

                                                                previous_3 = previous_3 + (day_plus);
                                                                if (previous_3 < date_change_1.size()) {


                                                                    SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                                    String Date_pass = format1.format(date_change_1.get(previous_3));

                                                                    int IDLab_pass = ID_Lab_for_pass;

                                                                    Cursor getQuantityByDate = db.rawQuery("Select Quantity from PlansPass where IDLab=" + ID_Lab_for_pass + " AND Date ='" + Date_pass + "' LIMIT 1", null);
                                                                    int i = 0;

                                                                    if (getQuantityByDate.moveToFirst()) {
                                                                        plan = new PlansPass(getQuantityByDate.getInt(0));
                                                                    }
                                                                    int Quantity_to_pass = plan.Quantity;

                                                                    Quantity_to_pass = Quantity_to_pass + 1;

                                                                    db.execSQL("update PlansPass set Quantity=" + Quantity_to_pass
                                                                            + " WHERE Date='" + Date_pass
                                                                            + "' AND IDLab=" + IDLab_pass
                                                                    );
                                                                    g = 1;
                                                                    quantity--;
                                                                    date_passes.remove(previous_3);
                                                                }
                                                            }
                                                        }

                                                    } else if (all_quantity_od_days_to_pass / quantity > 0.5 && all_quantity_od_days_to_pass / quantity <= 1) {
                                                        for (Date date1 : date_passes) {
                                                            find_plan_pass_for_date = db.rawQuery("Select Quantity from PlansPass WHERE Date='" + date1.toString() + "' and IDLab=" + ID_Lab_for_pass, null);
                                                            if (find_plan_pass_for_date.moveToFirst()) {
                                                                while (!find_plan_pass_for_date.isClosed()) {
                                                                    plan_pass_for_one_of_date = new PlansPass(find_plan_pass_for_date.getInt(0));
                                                                    if (!find_plan_pass_for_date.isLast()) {
                                                                        find_plan_pass_for_date.moveToNext();
                                                                    } else {
                                                                        find_plan_pass_for_date.close();
                                                                    }
                                                                }
                                                            }
                                                            int Quantity_to_pass = plan_pass_for_one_of_date.Quantity + 1;

                                                            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                            String Date_pass = format1.format(date1);

                                                            int IDLab_pass = ID_Lab_for_pass;

                                                            db.execSQL("update  PlansPass set Quantity =" + Quantity_to_pass
                                                                    + " WHERE Date='" + Date_pass + "' and IDLab=" + IDLab_pass
                                                            );

                                                            g = 1;
                                                            quantity--;
                                                        }

                                                    }
                                                }
                                            } else if (all_quantity_od_days_to_pass / all_quantity_of_lab <= 0.5) {
                                                double quantity = all_quantity_of_lab;
                                                for (Date date1 : date_passes) {
                                                    if (quantity > 1) {
                                                        int Quantity_to_pass = 2;

                                                        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                        String Date_pass = format1.format(date1);

                                                        int IDLab_pass = ID_Lab_for_pass;

                                                        db.execSQL("insert into PlansPass( IDLab, Date, Quantity) VALUES("
                                                                + IDLab_pass + ",'" + Date_pass
                                                                + "'," + Quantity_to_pass + ")"
                                                        );


                                                        g = 1;
                                                        quantity = quantity - 2;
                                                    }
                                                }

                                                while (quantity > 0) {
                                                    all_quantity_od_days_to_pass = date_passes.size() - 1;
                                                    List<Date> date_change_1 = date_passes;

                                                    if (all_quantity_od_days_to_pass / quantity > 1) {
                                                        int day_plus = (int) Math.round(all_quantity_od_days_to_pass / quantity);
                                                        if (day_plus > 0) {
                                                            day_plus--;
                                                        }

                                                        int previous_3 = 0;
                                                        for (int k = 0; k < date_change_1.size(); k++) {
                                                            if (quantity > 0) {

                                                                previous_3 = previous_3 + (day_plus);
                                                                if (previous_3 < date_change_1.size()) {

                                                                    SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                                    String Date_pass = format1.format(date_change_1.get(previous_3));


                                                                    int IDLab_pass = ID_Lab_for_pass;

                                                                    Cursor getQuantityByDate = db.rawQuery("Select Quantity from PlansPass where IDLab=" + ID_Lab_for_pass + " AND Date ='" + Date_pass + "' LIMIT 1", null);
                                                                    int i = 0;

                                                                    if (getQuantityByDate.moveToFirst()) {
                                                                        plan = new PlansPass(getQuantityByDate.getInt(0));
                                                                    }
                                                                    int Quantity_to_pass = plan.Quantity;

                                                                    Quantity_to_pass = Quantity_to_pass + 1;

                                                                    db.execSQL("update PlansPass set Quantity=" + Quantity_to_pass
                                                                            + " WHERE Date='" + Date_pass
                                                                            + "' AND IDLab=" + IDLab_pass
                                                                    );

                                                                    g = 1;

                                                                    quantity--;

                                                                    date_passes.remove(previous_3);


                                                                }

                                                            }


                                                        }

                                                    }

                                                }
                                            } else if (all_quantity_od_days_to_pass / all_quantity_of_lab > 1) {
                                                double quantity = all_quantity_of_lab;
                                                int day_plus = (int) Math.round(all_quantity_od_days_to_pass / all_quantity_of_lab);
                                                if (day_plus > 0) {
                                                    day_plus--;
                                                }
                                                List<Date> date_change = date_passes;
                                                int previous_1 = 0;
                                                for (int i = 0; i < date_change.size(); i++) {
                                                    if (quantity > 0) {
                                                        previous_1 = previous_1 + (day_plus);
                                                        if (previous_1 < date_change.size()) {
                                                            int Quantity_to_pass = 1;

                                                            SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                            String Date_pass = format1.format(date_change.get(previous_1));

                                                            int IDLab_pass = ID_Lab_for_pass;

                                                            db.execSQL("insert into PlansPass( IDLab, Date, Quantity) VALUES("
                                                                    + IDLab_pass + ",'" + Date_pass
                                                                    + "'," + Quantity_to_pass + ")"
                                                            );
                                                            g = 1;

                                                            quantity--;
                                                            date_passes.remove(previous_1);

                                                        }
                                                    }
                                                }
                                                while (quantity > 0) {
                                                    all_quantity_od_days_to_pass = date_passes.size() - 1;
                                                    List<Date> date_change_1 = date_passes;

                                                    if (all_quantity_od_days_to_pass / quantity > 1) {
                                                        day_plus = (int) Math.round(all_quantity_od_days_to_pass / quantity);
                                                        if (day_plus > 0) {
                                                            day_plus--;
                                                        }

                                                        int previous_3 = 0;
                                                        for (int k = 0; k < date_change_1.size(); k++) {
                                                            if (quantity > 0) {

                                                                previous_3 = previous_3 + (day_plus);
                                                                if (previous_3 < date_change_1.size()) {

                                                                    SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
                                                                    String Date_pass = format1.format(date_change_1.get(previous_3));


                                                                    int IDLab_pass = ID_Lab_for_pass;

                                                                    Cursor getQuantityByDate = db.rawQuery("Select Quantity from PlansPass where IDLab=" + ID_Lab_for_pass + " AND Date ='" + Date_pass + "' LIMIT 1", null);
                                                                    int i = 0;

                                                                    if (getQuantityByDate.moveToFirst()) {
                                                                        plan = new PlansPass(getQuantityByDate.getInt(0));
                                                                    }
                                                                    int Quantity_to_pass = plan.Quantity;

                                                                    Quantity_to_pass = Quantity_to_pass + 1;

                                                                    db.execSQL("update PlansPass set Quantity=" + Quantity_to_pass
                                                                            + " WHERE Date='" + Date_pass
                                                                            + "' AND IDLab=" + IDLab_pass
                                                                    );

                                                                    g = 1;

                                                                    quantity--;

                                                                    date_passes.remove(previous_3);


                                                                }

                                                            }


                                                        }

                                                    }

                                                }

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
                                Toast.makeText(SecondStepRegistration.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });
                    }
                }
            }
            }

            return g;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            super.onPostExecute(aVoid);


        }

    }
}
