package com.example.courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.courseproject.R;
import com.example.courseproject.db.units.PlansPass;
import com.example.courseproject.db.units.RatingStudents;
import com.example.courseproject.db.units.Student;

import java.util.List;

public class RatingOfStudentsAdapter extends ArrayAdapter<RatingStudents> {
    private LayoutInflater inflater;
    private int layout;
    private List<RatingStudents> student_rating;

    public RatingOfStudentsAdapter(Context context, int resource, List<RatingStudents> list) {
        super(context, resource, list);
        this.student_rating = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView namestudent = (TextView) view.findViewById(R.id.name_of_student);

        RatingStudents student = student_rating.get(position);

        namestudent.setText(String.valueOf(position + 1) + ".  " + student.Surname + " " + student.Name);

        return view;
    }

}


