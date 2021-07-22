package com.example.courseproject.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.courseproject.InfoOfLab;
import com.example.courseproject.LoginOrRegistrationActivity;
import com.example.courseproject.R;
import com.example.courseproject.Registration.SecondStepRegistration;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.Course;
import com.example.courseproject.Retrofit.POJO.Group;
import com.example.courseproject.Retrofit.POJO.ListLabTeachers;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.Semesters;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.UpdateUserInfo;
import com.example.courseproject.adapters.NameLabs;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.PlansPass;
import com.example.courseproject.db.units.Student;
import com.example.courseproject.db.units.Teacher;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    SQLiteDatabase db;

    Cursor userCursor;
    Cursor findLab;
    Cursor userCursor1;
    Student student_logged;
    Button update_user_info;
    Button exit_user_info;

    private List<Lab> list_lab_find = new ArrayList();

    PlansPass plan_pass_for_one_of_date;
    Cursor find_plan_pass_for_date;
    NameLabs labAdapter;
    private List<Lab> labs = new ArrayList();


    private String login_student;
    private String name_student;
    private String group_student;
    private String email_student;
    private String about_me_student;


    private HomeViewModel homeViewModel;

    PlansPass plan;

    Cursor userCursor2;
    Cursor userCursor3;
    Teacher teachers;
    Student student;

    ListStudents list_student;
    private int student_id;
    private String login_teacher;
    private int teacher_id;
    private int id_group;

    List<Date> date_passes = new ArrayList<>();

    List<ListLabs> list_of_filter_labs = new ArrayList();
    Date now = null;
    Date first_start = null;
    Date first_end = null;
    Date second_start = null;
    Date second_end = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();

        userCursor2 = db.rawQuery("Select Login, IDStudent from Student", null);
        JSONPlaceHolderApi apiJSON = NetworkService.getApi();
        if (userCursor2.moveToFirst()) {
            while (!userCursor2.isClosed()) {
                student = new Student(userCursor2.getString(0), userCursor2.getInt(1));
                if (!userCursor2.isLast()) {
                    userCursor2.moveToNext();
                } else {
                    userCursor2.close();
                }
            }
        }
        login_student = student.Login;
        student_id = student.IDStudent;
        userCursor3 = db.rawQuery("Select IDGroup from ListStudents WHERE IDStudent=" + student_id, null);
        if (userCursor3.moveToFirst()) {
            while (!userCursor3.isClosed()) {
                list_student = new ListStudents(userCursor3.getInt(0));
                if (!userCursor3.isLast()) {
                    userCursor3.moveToNext();
                } else {
                    userCursor3.close();
                }
            }
        }
        id_group = list_student.IDGroup;


        userCursor = db.rawQuery("Select Login, Email, AboutMe, Groups, SubGroup, Course, Speciality, Name, Surname  from Student", null);
        userCursor1 = db.rawQuery("Select NameLab, WeekDay, IDLab from ListLab ORDER BY " +
                "     CASE WHEN WeekDay = 'Monday' THEN 1 WHEN WeekDay = 'Tuesday' THEN 2" +
                " WHEN WeekDay = 'Wednesday' THEN 3 WHEN WeekDay = 'Thursday' THEN 4" +
                " WHEN WeekDay = 'Friday' THEN 5 WHEN WeekDay = 'Saturday' THEN 6" +
                " WHEN WeekDay = 'Sunday' THEN 7" +
                " END ASC", null);

        if (userCursor.moveToFirst()) {
            while (!userCursor.isClosed()) {
                student_logged = new Student(userCursor.getString(0), userCursor.getString(1),
                        userCursor.getString(2), userCursor.getInt(3),
                        userCursor.getInt(4), userCursor.getInt(5),
                        userCursor.getString(6), userCursor.getString(7), userCursor.getString(8));
                if (!userCursor.isLast()) {
                    userCursor.moveToNext();
                } else {
                    userCursor.close();
                }
            }
            login_student = student_logged.Login;
            name_student = student_logged.Surname + " " + student_logged.Name;
            group_student = String.valueOf(student_logged.Course) + " course " + student_logged.Speciality + " "
                    + String.valueOf(student_logged.Group) + "." + String.valueOf(student_logged.SubGroup);
            email_student = "Email: " + student_logged.Email;
            about_me_student = "About Me: " + student_logged.About_Me;
        }
        labs.clear();
        if (userCursor1.moveToFirst()) {
            while (!userCursor1.isClosed()) {
                labs.add(new Lab(userCursor1.getString(0), userCursor1.getString(1), userCursor1.getInt(2)));
                if (!userCursor1.isLast()) {
                    userCursor1.moveToNext();
                } else {
                    userCursor1.close();
                }
            }
        }

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView login = root.findViewById(R.id.login_student);
        final TextView name = root.findViewById(R.id.name_student);
        final TextView group = root.findViewById(R.id.group_student);
        final TextView email = root.findViewById(R.id.email_student);
        final TextView about_me = root.findViewById(R.id.about_me_student);

        final ListView listLabs = root.findViewById(R.id.list_of_labs);

        labAdapter = new NameLabs(getActivity().getApplicationContext(), R.layout.list_item_labs, labs);
        listLabs.setAdapter(labAdapter);
        labAdapter.notifyDataSetChanged();

        listLabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ///

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

                                }
                                call.cancel();
                            }

                            @Override
                            public void onFailure(Call<Semesters> call, Throwable t) {
                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                call.cancel();
                            }
                        });

                        ////
                        Intent intent = new Intent(getActivity(), InfoOfLab.class);
                        intent.putExtra("id", (int) labs.get(position).IDLab);
                        intent.putExtra("id_stud", student.IDStudent);
                        startActivity(intent);
                    }
                }
            }
        });


        //SetValues

        homeViewModel.getLogin(login_student).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                login.setText(s);
            }
        });

        homeViewModel.getName(name_student).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                name.setText(s);
            }
        });

        homeViewModel.getGroup(group_student).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                group.setText(s);
            }
        });

        homeViewModel.getEmail(email_student).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                email.setText(s);
            }
        });

        homeViewModel.getAbout_Me(about_me_student).observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                about_me.setText(s);
            }
        });


        exit_user_info = root.findViewById(R.id.exit_button);
        exit_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.execSQL("DELETE FROM Student");
                db.execSQL("DELETE FROM ListStudents");
                db.execSQL("DELETE FROM ListLab ");
                db.execSQL("DELETE FROM TeachersForLab ");
                db.execSQL("DELETE FROM PlansPass ");
                db.execSQL("DELETE FROM ListLabTeacher ");
                db.execSQL("DELETE FROM StudentPass ");
                db.execSQL("DELETE FROM RatingOfStudents ");
                Intent intent = new Intent(getActivity(), LoginOrRegistrationActivity.class);
                startActivity(intent);
            }

        });


        //UpdateButton

        update_user_info = root.findViewById(R.id.update_button);
        update_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONPlaceHolderApi apiJSON = NetworkService.getApi();
                String login_user_update = login.getText().toString();
                if (login_user_update != null) {
                    apiJSON.getStudentByLogin(login_user_update).enqueue(new Callback<LoginStudents>() {
                        @Override
                        public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                            if (response.isSuccessful()) {
                                LoginStudents students = response.body();

                                int Password_for_update = students.getPassword();
                                int IDStudent_for_update = students.getIDStudent();
                                String Email_for_update = students.getEmail();
                                String About_Me_for_update = students.getAboutMe();

                                Intent intent = new Intent(getActivity(), UpdateUserInfo.class);
                                intent.putExtra("login", (String) login_user_update);
                                intent.putExtra("password", (Integer) Password_for_update);
                                intent.putExtra("id_student", (Integer) IDStudent_for_update);
                                intent.putExtra("email", (String) Email_for_update);
                                intent.putExtra("about_me", (String) About_Me_for_update);
                                startActivity(intent);
                            }
                            call.cancel();
                        }

                        @Override
                        public void onFailure(Call<LoginStudents> call, Throwable t) {
                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                }

            }
        });
        /////end update button

        return root;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();
        userCursor = db.rawQuery("Select Login, Email, AboutMe, Groups, SubGroup, Course, Speciality, Name, Surname  from Student", null);
        if (userCursor.moveToFirst()) {
            while (!userCursor.isClosed()) {
                student_logged = new Student(userCursor.getString(0), userCursor.getString(1),
                        userCursor.getString(2), userCursor.getInt(3),
                        userCursor.getInt(4), userCursor.getInt(5),
                        userCursor.getString(6), userCursor.getString(7), userCursor.getString(8));
                if (!userCursor.isLast()) {
                    userCursor.moveToNext();
                } else {
                    userCursor.close();
                }
            }
            login_student = student_logged.Login;
            name_student = student_logged.Surname + " " + student_logged.Name;
            group_student = String.valueOf(student_logged.Course) + " course " + student_logged.Speciality + " "
                    + String.valueOf(student_logged.Group) + "." + String.valueOf(student_logged.SubGroup);
            email_student = "Email: " + student_logged.Email;
            about_me_student = "About Me: " + student_logged.About_Me;
        }

        //update from server

        userCursor2 = db.rawQuery("Select Login, IDStudent from Student", null);
        JSONPlaceHolderApi apiJSON = NetworkService.getApi();
        if (userCursor2.moveToFirst()) {
            while (!userCursor2.isClosed()) {
                student = new Student(userCursor2.getString(0), userCursor2.getInt(1));
                if (!userCursor2.isLast()) {
                    userCursor2.moveToNext();
                } else {
                    userCursor2.close();
                }
            }
        }
        login_student = student.Login;
        student_id = student.IDStudent;
        userCursor3 = db.rawQuery("Select IDGroup from ListStudents WHERE IDStudent=" + student_id, null);
        if (userCursor3.moveToFirst()) {
            while (!userCursor3.isClosed()) {
                list_student = new ListStudents(userCursor3.getInt(0));
                if (!userCursor3.isLast()) {
                    userCursor3.moveToNext();
                } else {
                    userCursor3.close();
                }
            }
        }
        id_group = list_student.IDGroup;

        apiJSON.getStudentByLogin(login_student).enqueue(new Callback<LoginStudents>() {
            @Override
            public void onResponse(Call<LoginStudents> call, Response<LoginStudents> response) {
                if (response.isSuccessful()) {
                    LoginStudents student_by_login = response.body();
                    int Password = student_by_login.getPassword();
                    int ID = student_by_login.getIDStudent();
                    String Email = student_by_login.getEmail().toString();
                    String About_Me = student_by_login.getAboutMe().toString();
                    apiJSON.getStudent(ID).enqueue(new Callback<Students>() {
                        @Override
                        public void onResponse(Call<Students> call, Response<Students> response) {
                            if (response.isSuccessful()) {

                                Students student_by_id = response.body();
                                int ID_Groups = student_by_id.getIDGroup();
                                String Name = student_by_id.getName().toString();
                                String Surname = student_by_id.getSurname().toString();

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
                                                            Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
                                                        }


                                                        apiJSON.getGroup(ID_Groups).enqueue(new Callback<Group>() {
                                                            @Override
                                                            public void onResponse(Call<Group> call, Response<Group> response) {
                                                                if (response.isSuccessful()) {
                                                                    Group group = response.body();
                                                                    int Course_ID = group.getIDCource();
                                                                    int SubGroup = group.getIDSubGroup();
                                                                    int Group = group.getNumberGroup();
                                                                    String Speciality = group.getIDSpeciality().toString();
                                                                    ////
                                                                    apiJSON.getCourse(Course_ID).enqueue(new Callback<Course>() {
                                                                        @Override
                                                                        public void onResponse(Call<Course> call, Response<Course> response) {
                                                                            if (response.isSuccessful()) {
                                                                                Course course = response.body();

                                                                                int Cource = course.getNumberCource();
                                                                                //////What the semestr

                                                                                if (now.after(first_start) && now.before(first_end)) {

                                                                                    apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                                            if (response.isSuccessful()) {
                                                                                                List<ListLabs> list_of_all_labs = response.body();
                                                                                                for (ListLabs lab : list_of_all_labs) {
                                                                                                    String special = lab.getIDSpeciality();
                                                                                                    if (lab.getIDCource() == Course_ID && special.equals(Speciality) && lab.getIDSem() == second_sem.getIDSem()) {
                                                                                                        list_of_filter_labs.add(lab);
                                                                                                    } else {
                                                                                                        Toast.makeText(getActivity(), "You don't have labs", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }
                                                                                                if (!list_of_filter_labs.isEmpty()) {
                                                                                                    apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                                        @Override
                                                                                                        public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                                            if (response.isSuccessful()) {
                                                                                                                List<ListLabTeachers> list_of_all_teachers_lab = response.body();
                                                                                                                /////
                                                                                                                try {

                                                                                                                    db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + ID_Groups);

                                                                                                                    for (ListLabTeachers teachers_lab : list_of_all_teachers_lab) {

                                                                                                                        for (ListLabs list_lab : list_of_filter_labs) {
                                                                                                                            if (teachers_lab.getIDLab() == list_lab.getIDLab() && teachers_lab.getIDGroup() == ID_Groups) {


                                                                                                                                db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + list_lab.getIDLab() + ",'" + list_lab.getNameLab() + "'," + list_lab.getQuantity()
                                                                                                                                        + "," + teachers_lab.getIDTeacher() + ",'" + teachers_lab.getWeekName() + "',"
                                                                                                                                        + teachers_lab.getIDGroup() + "," + list_lab.getIDSem() + ")"
                                                                                                                                );

                                                                                                                            }
                                                                                                                        }
                                                                                                                    }
                                                                                                                    db.execSQL("update  Student set Password=" +
                                                                                                                            +Password + ", Email='" + Email + "', AboutMe ='" + About_Me + "', Name ='" + Name + "', Surname ='"
                                                                                                                            + Surname + "', Groups =" + Group + ", SubGroup =" + SubGroup + ", Course =" + Cource + ", Speciality = '"
                                                                                                                            + Speciality + "'"
                                                                                                                    );
                                                                                                                    db.execSQL("update  ListStudents set IDGroup=" +
                                                                                                                            +ID_Groups
                                                                                                                    );
                                                                                                                    call.cancel();
                                                                                                                } catch (Exception e) {
                                                                                                                    Toast.makeText(getActivity(), "Not added to db", Toast.LENGTH_SHORT).show();
                                                                                                                }

                                                                                                                /////
                                                                                                            } else {
                                                                                                                Toast.makeText(getActivity(), "ListLabTeacher", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                            call.cancel();
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                            call.cancel();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                            } else {
                                                                                                Toast.makeText(getActivity(), "Error listlab 1 sem", Toast.LENGTH_SHORT).show();

                                                                                            }
                                                                                            call.cancel();
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                            call.cancel();
                                                                                        }
                                                                                    });
                                                                                } else if (now.after(second_start) && now.before(second_end)) {
                                                                                    apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                                            if (response.isSuccessful()) {
                                                                                                List<ListLabs> list_of_all_labs = response.body();
                                                                                                if (!list_of_all_labs.isEmpty()) {
                                                                                                    for (ListLabs lab : list_of_all_labs) {
                                                                                                        String special = lab.getIDSpeciality();
                                                                                                        if (lab.getIDCource() == Course_ID && special.equals(Speciality) && lab.getIDSem() == second_sem.getIDSem()) {
                                                                                                            list_of_filter_labs.add(lab);
                                                                                                        } else {
                                                                                                            Toast.makeText(getActivity(), "You don't have labs", Toast.LENGTH_SHORT).show();

                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                if (!list_of_filter_labs.isEmpty()) {
                                                                                                    apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                                        @Override
                                                                                                        public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                                            if (response.isSuccessful()) {
                                                                                                                List<ListLabTeachers> list_of_all_teachers_lab = response.body();
                                                                                                                /////
                                                                                                                try {
                                                                                                                    db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + ID_Groups);

                                                                                                                    for (ListLabTeachers teachers_lab : list_of_all_teachers_lab) {

                                                                                                                        for (ListLabs list_lab : list_of_filter_labs) {
                                                                                                                            if (teachers_lab.getIDLab() == list_lab.getIDLab() && teachers_lab.getIDGroup() == ID_Groups) {


                                                                                                                                db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                        + list_lab.getIDLab() + ",'" + list_lab.getNameLab() + "'," + list_lab.getQuantity()
                                                                                                                                        + "," + teachers_lab.getIDTeacher() + ",'" + teachers_lab.getWeekName() + "',"
                                                                                                                                        + teachers_lab.getIDGroup() + "," + list_lab.getIDSem() + ")"
                                                                                                                                );

                                                                                                                            }
                                                                                                                        }
                                                                                                                    }

                                                                                                                    db.execSQL("update  Student set Password=" +
                                                                                                                            +Password + ", Email='" + Email + "', AboutMe ='" + About_Me + "', Name ='" + Name + "', Surname ='"
                                                                                                                            + Surname + "', Groups =" + Group + ", SubGroup =" + SubGroup + ", Course =" + Cource + ", Speciality = '"
                                                                                                                            + Speciality + "'"
                                                                                                                    );

                                                                                                                    db.execSQL("update  ListStudents set IDGroup=" +
                                                                                                                            +ID_Groups
                                                                                                                    );


                                                                                                                } catch (Exception e) {
                                                                                                                    Toast.makeText(getActivity(), "Not added to db", Toast.LENGTH_SHORT).show();
                                                                                                                }

                                                                                                                /////
                                                                                                            } else {
                                                                                                                Toast.makeText(getActivity(), "ListLabTeacher", Toast.LENGTH_SHORT).show();

                                                                                                            }
                                                                                                            call.cancel();
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                            call.cancel();
                                                                                                        }
                                                                                                    });
                                                                                                }

                                                                                            } else {
                                                                                                Toast.makeText(getActivity(), "Error listlab 2 sem", Toast.LENGTH_SHORT).show();

                                                                                            }
                                                                                            call.cancel();
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                            call.cancel();
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    if (now.before(second_start) || now.after(first_end)) {
                                                                                        apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                                                if (response.isSuccessful()) {
                                                                                                    List<ListLabs> list_of_all_labs = response.body();
                                                                                                    for (ListLabs lab : list_of_all_labs) {
                                                                                                        String special = lab.getIDSpeciality();
                                                                                                        if (lab.getIDCource() == Course_ID && special.equals(Speciality) && lab.getIDSem() == second_sem.getIDSem()) {
                                                                                                            list_of_filter_labs.add(lab);
                                                                                                        } else {
                                                                                                            Toast.makeText(getActivity(), "You don't have labs", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                    if (!list_of_filter_labs.isEmpty()) {
                                                                                                        apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                                                if (response.isSuccessful()) {
                                                                                                                    List<ListLabTeachers> list_of_all_teachers_lab = response.body();
                                                                                                                    /////
                                                                                                                    try {
                                                                                                                        db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + ID_Groups);
                                                                                                                        for (ListLabTeachers teachers_lab : list_of_all_teachers_lab) {

                                                                                                                            for (ListLabs list_lab : list_of_filter_labs) {
                                                                                                                                if (teachers_lab.getIDLab() == list_lab.getIDLab() && teachers_lab.getIDGroup() == ID_Groups) {


                                                                                                                                    db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                            + list_lab.getIDLab() + ",'" + list_lab.getNameLab() + "'," + list_lab.getQuantity()
                                                                                                                                            + "," + teachers_lab.getIDTeacher() + ",'" + teachers_lab.getWeekName() + "',"
                                                                                                                                            + teachers_lab.getIDGroup() + "," + list_lab.getIDSem() + ")"
                                                                                                                                    );

                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        db.execSQL("update  Student set Password=" +
                                                                                                                                +Password + ", Email='" + Email + "', AboutMe ='" + About_Me + "', Name ='" + Name + "', Surname ='"
                                                                                                                                + Surname + "', Groups =" + Group + ", SubGroup =" + SubGroup + ", Course =" + Cource + ", Speciality = '"
                                                                                                                                + Speciality + "'"
                                                                                                                        );
                                                                                                                        db.execSQL("update  ListStudents set IDGroup=" +
                                                                                                                                +ID_Groups
                                                                                                                        );
                                                                                                                        call.cancel();
                                                                                                                    } catch (Exception e) {
                                                                                                                        Toast.makeText(getActivity(), "Not added to db", Toast.LENGTH_SHORT).show();
                                                                                                                    }

                                                                                                                    /////
                                                                                                                } else {
                                                                                                                    Toast.makeText(getActivity(), "ListLabTeacher", Toast.LENGTH_SHORT).show();

                                                                                                                }
                                                                                                                call.cancel();
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                                call.cancel();
                                                                                                            }
                                                                                                        });
                                                                                                    }

                                                                                                } else {
                                                                                                    Toast.makeText(getActivity(), "Error listlab 3 sem", Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                                call.cancel();
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                call.cancel();
                                                                                            }
                                                                                        });
                                                                                    } else if (now.before(first_start) || now.after(second_end)) {
                                                                                        apiJSON.getListLab().enqueue(new Callback<List<ListLabs>>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<List<ListLabs>> call, Response<List<ListLabs>> response) {
                                                                                                if (response.isSuccessful()) {
                                                                                                    List<ListLabs> list_of_all_labs = response.body();
                                                                                                    for (ListLabs lab : list_of_all_labs) {
                                                                                                        String special = lab.getIDSpeciality();
                                                                                                        if (lab.getIDCource() == Course_ID && special.equals(Speciality) && lab.getIDSem() == second_sem.getIDSem()) {
                                                                                                            list_of_filter_labs.add(lab);
                                                                                                        } else {
                                                                                                            Toast.makeText(getActivity(), "You don't have labs", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    }
                                                                                                    if (!list_of_filter_labs.isEmpty()) {
                                                                                                        apiJSON.getListLabTeachers().enqueue(new Callback<List<ListLabTeachers>>() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call<List<ListLabTeachers>> call, Response<List<ListLabTeachers>> response) {
                                                                                                                if (response.isSuccessful()) {
                                                                                                                    List<ListLabTeachers> list_of_all_teachers_lab = response.body();
                                                                                                                    /////
                                                                                                                    try {
                                                                                                                        db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + ID_Groups);

                                                                                                                        for (ListLabTeachers teachers_lab : list_of_all_teachers_lab) {

                                                                                                                            for (ListLabs list_lab : list_of_filter_labs) {
                                                                                                                                if (teachers_lab.getIDLab() == list_lab.getIDLab() && teachers_lab.getIDGroup() == ID_Groups) {

                                                                                                                                    db.execSQL("insert into ListLab(IDLab, NameLab, Quantity, IDTeacher, WeekDay, IDGroup,IDSem) VALUES("
                                                                                                                                            + list_lab.getIDLab() + ",'" + list_lab.getNameLab() + "'," + list_lab.getQuantity()
                                                                                                                                            + "," + teachers_lab.getIDTeacher() + ",'" + teachers_lab.getWeekName() + "',"
                                                                                                                                            + teachers_lab.getIDGroup() + "," + list_lab.getIDSem() + ")"
                                                                                                                                    );

                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        db.execSQL("update  Student set Password=" +
                                                                                                                                +Password + ", Email='" + Email + "', AboutMe ='" + About_Me + "', Name ='" + Name + "', Surname ='"
                                                                                                                                + Surname + "', Groups =" + Group + ", SubGroup =" + SubGroup + ", Course =" + Cource + ", Speciality = '"
                                                                                                                                + Speciality + "'"
                                                                                                                        );
                                                                                                                        db.execSQL("update  ListStudents set IDGroup=" +
                                                                                                                                +ID_Groups
                                                                                                                        );
                                                                                                                        call.cancel();
                                                                                                                    } catch (Exception e) {
                                                                                                                        Toast.makeText(getActivity(), "Not added to db", Toast.LENGTH_SHORT).show();
                                                                                                                    }

                                                                                                                    /////
                                                                                                                } else {
                                                                                                                    Toast.makeText(getActivity(), "ListLabTeacher", Toast.LENGTH_SHORT).show();

                                                                                                                }
                                                                                                                call.cancel();
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<List<ListLabTeachers>> call, Throwable t) {
                                                                                                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                                call.cancel();
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                } else {
                                                                                                    Toast.makeText(getActivity(), "Error listlab 4 sem", Toast.LENGTH_SHORT).show();

                                                                                                }
                                                                                                call.cancel();
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<List<ListLabs>> call, Throwable t) {
                                                                                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                                                call.cancel();
                                                                                            }
                                                                                        });
                                                                                    }


                                                                                }

                                                                            } else {
                                                                                Toast.makeText(getActivity(), "Error course", Toast.LENGTH_SHORT).show();

                                                                            }
                                                                            call.cancel();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Course> call, Throwable t) {
                                                                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                            call.cancel();
                                                                        }
                                                                    });

                                                                } else {
                                                                    Toast.makeText(getActivity(), "Error group", Toast.LENGTH_SHORT).show();

                                                                }
                                                                call.cancel();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Group> call, Throwable t) {
                                                                Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                                call.cancel();
                                                            }
                                                        });

                                                    } else {
                                                        Toast.makeText(getActivity(), "Error semestr 2", Toast.LENGTH_SHORT).show();

                                                    }

                                                    call.cancel();
                                                }

                                                @Override
                                                public void onFailure(Call<Semesters> call, Throwable t) {
                                                    Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                                    call.cancel();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(getActivity(), "Semestr 1 error", Toast.LENGTH_SHORT).show();

                                        }
                                        call.cancel();
                                    }

                                    @Override
                                    public void onFailure(Call<Semesters> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                        call.cancel();
                                    }
                                });
                            }

                            call.cancel();
                        }

                        @Override
                        public void onFailure(Call<Students> call, Throwable t) {
                            Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                            call.cancel();
                        }
                    });
                } else {
                    try {
                        db.execSQL("DELETE FROM Student WHERE Login='" + login_student + "'");
                        db.execSQL("DELETE FROM ListStudents WHERE IDStudent=" + student_id);
                        db.execSQL("DELETE FROM ListLab WHERE IDGroup=" + id_group);
                        Toast.makeText(getActivity(), "You are deleted by admin", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), LoginOrRegistrationActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Can't delete table", Toast.LENGTH_SHORT).show();
                    }
                }
                call.cancel();
            }

            @Override
            public void onFailure(Call<LoginStudents> call, Throwable t) {
                call.cancel();
            }
        });
    }


}
/////end update from server