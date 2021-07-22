package com.example.courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.courseproject.R;
import com.example.courseproject.db.units.StudentGroup;

import java.util.List;

public class StudentTeacherGroupAdapter extends ArrayAdapter<StudentGroup> {
    private LayoutInflater inflater;
    private int layout;
    private List<StudentGroup> list_labs_info;

    public StudentTeacherGroupAdapter(Context context, int resource, List<StudentGroup> list) {
        super(context, resource, list);
        this.list_labs_info = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView student = (TextView) view.findViewById(R.id.students_name_group);

        StudentGroup lab = list_labs_info.get(position);

        student.setText(lab.Surname + " " + lab.Name);

        return view;
    }

}
