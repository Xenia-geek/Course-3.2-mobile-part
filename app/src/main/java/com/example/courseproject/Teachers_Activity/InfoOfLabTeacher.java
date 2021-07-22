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

import com.example.courseproject.R;
import com.example.courseproject.adapters.GroupsTeachersLabAdapter;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.GroupLab;
import com.example.courseproject.db.units.Lab;

import java.util.ArrayList;
import java.util.List;

public class InfoOfLabTeacher extends AppCompatActivity {

    SQLiteDatabase db;
    public int id_lab;
    int IDGroup_stud;
    private List<GroupLab> list_labs_info = new ArrayList();
    Lab nam;
    GroupsTeachersLabAdapter groupAdapter;
    Cursor find_lab;
    Cursor name_lab;
    Button back_to_labs;
    int id_teacher;

    String WeekDay;
    int Groupss;
    int SubGroup;
    int Cource;
    int IDGroup;
    private List<GroupLab> sg = new ArrayList();
    private List<Lab> InfoOfListLab = new ArrayList();
    private List<GroupLab> InfoOfgroup = new ArrayList();
    public List<GroupLab> ListOfAllGgroupOfLabTeacher = new ArrayList();
    Cursor find_weekday_and_id_group;
    Cursor find_id_student_with_idgroup;
    Cursor find_info_of_listlab;
    Cursor find_info_of_groupinfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_of_lab_teacher);

        ListView listLabs = findViewById(R.id.list_group_teachers_lab);

        Intent intent = getIntent();
        id_lab = intent.getIntExtra("id", -1);
        id_teacher = intent.getIntExtra("id_techer", -1);


        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();


        name_lab = db.rawQuery("SELECT NameLab, IDLab FROM ListLab1 WHERE IDLab=" + id_lab, null);
        if (name_lab.moveToFirst()) {
            while (!name_lab.isClosed()) {
                nam = new Lab(name_lab.getString(0), name_lab.getInt(1));
                if (!name_lab.isLast()) {
                    name_lab.moveToNext();
                } else {
                    name_lab.close();
                }
            }
        }

        TextView name = findViewById(R.id.title_of_lab);
        name.setText(nam.NameLab);


        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();

        //exit button

        back_to_labs = findViewById(R.id.back_button_to_info_teacher_lab);

        back_to_labs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoOfLabTeacher.this, MainActivityTeacher.class);
                startActivity(intent);
            }
        });


        ///////////////////group info to list/////////////////
        /////////////////////////////////////////////////////

        find_info_of_listlab = db.rawQuery("SELECT IDGroup, NameLab, WeekDay FROM ListLab1 WHERE IDLab=" + id_lab + " AND IDTeacher=" + id_teacher, null);
        if (find_info_of_listlab.moveToFirst()) {
            while (!find_info_of_listlab.isClosed()) {
                InfoOfListLab.add(new Lab(find_info_of_listlab.getInt(0), find_info_of_listlab.getString(1), find_info_of_listlab.getString(2)));
                if (!find_info_of_listlab.isLast()) {
                    find_info_of_listlab.moveToNext();
                } else {
                    find_info_of_listlab.close();
                }
            }

            for (Lab info_lab : InfoOfListLab) {
                String WeekDay_Lab_pass = info_lab.WeekDay;
                int IDGroup_that_pass = info_lab.IDGroup;


                find_info_of_groupinfo = db.rawQuery("SELECT Course, Groupss, SubGroup FROM GroupInfoForTeacher WHERE IDGroup=" + IDGroup_that_pass, null);
                if (find_info_of_groupinfo.moveToFirst()) {
                    while (!find_info_of_groupinfo.isClosed()) {
                        InfoOfgroup.add(new GroupLab(find_info_of_groupinfo.getInt(0), find_info_of_groupinfo.getInt(1), find_info_of_groupinfo.getInt(2)));
                        if (!find_info_of_groupinfo.isLast()) {
                            find_info_of_groupinfo.moveToNext();
                        } else {
                            find_info_of_groupinfo.close();
                        }
                    }

                    for (GroupLab info_group : InfoOfgroup) {
                        int Course_lab_pass = info_group.Course;
                        int Group_lab_pass = info_group.Group;
                        int SubGroup_lab_pass = info_group.SubGroup;
                        ListOfAllGgroupOfLabTeacher.add(new GroupLab(IDGroup_that_pass, WeekDay_Lab_pass, Course_lab_pass, Group_lab_pass, SubGroup_lab_pass));
                    }
                }

            }
        }


        groupAdapter = new GroupsTeachersLabAdapter(InfoOfLabTeacher.this, R.layout.list_item_group_lab, ListOfAllGgroupOfLabTeacher);
        groupAdapter.notifyDataSetChanged();
        listLabs.setAdapter(groupAdapter);


        ////////go to the list student
        listLabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(InfoOfLabTeacher.this, InfoListStudentTeacher.class);
                intent.putExtra("id_group", (int) ListOfAllGgroupOfLabTeacher.get(position).IDGroup);
                intent.putExtra("id_lab", id_lab);
                intent.putExtra("id_teacher", (Integer) id_teacher);
                startActivity(intent);
            }
        });


        /*

        find_weekday_and_id_group = db.rawQuery("SELECT NameLab, IDGroup, WeekDay FROM ListLab WHERE IDLab=" + id_lab + " and IDTeacher=" + id_teacher, null);
        if (find_weekday_and_id_group.moveToFirst()) {
            Lab lab_info = new Lab(find_weekday_and_id_group.getString(0), find_weekday_and_id_group.getInt(1), find_weekday_and_id_group.getString(2));
            IDGroup = lab_info.IDGroup;
            find_id_student_with_idgroup = db.rawQuery("SELECT IDStudent FROM GroupInfoForTeacher WHERE ID_Group=" + IDGroup, null);
            if (find_id_student_with_idgroup.moveToFirst()) {
                List<GroupLab> all_student_with_idgroup = new ArrayList();
                while (!find_id_student_with_idgroup.isClosed()) {
                    all_student_with_idgroup.add(new GroupLab(find_id_student_with_idgroup.getInt(0)));
                    if (!find_id_student_with_idgroup.isLast()) {
                        find_id_student_with_idgroup.moveToNext();
                    } else {
                        find_id_student_with_idgroup.close();
                    }
                }
                for (GroupLab l : all_student_with_idgroup) {
                    Cursor find_course_group_subgroup;
                    find_course_group_subgroup = db.rawQuery("SELECT Cource, Groups, SubGroup FROM GroupInfoForTeacher WHERE IDStudent="+l.IDStudent,null);
                    if(find_course_group_subgroup.moveToFirst()){
                        GroupLab hj = new GroupLab(find_course_group_subgroup.getInt(0), find_course_group_subgroup.getInt(1), find_course_group_subgroup.getInt(2));
                        Toast.makeText(InfoOfLabTeacher.this, String.valueOf(hj.Course)+" "+String.valueOf(hj.Group)+" "+ String.valueOf(hj.SubGroup), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

         */


        //////////////////////////////////////////////////////
        /*
        groupAdapter = new GroupsTeachersLabAdapter(InfoOfLabTeacher.this, R.layout.list_item_group_lab, sg);
        groupAdapter.notifyDataSetChanged();
        listLabs.setAdapter(groupAdapter);


        ////////go to the list student
        listLabs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(InfoOfLabTeacher.this, String.valueOf(id_teacher), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(InfoOfLabTeacher.this, InfoListStudentTeacher.class);
                intent.putExtra("id_group", (int) list_labs_info.get(position).IDGroup);
                intent.putExtra("id_lab", id_lab);
                intent.putExtra("id_teacher", (Integer) id_teacher);
                startActivity(intent);
            }
        });
        */
    }
}