package com.example.courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.courseproject.R;
import com.example.courseproject.db.units.Lab;

import java.util.List;

public class NameLabsTeacherAdapter extends ArrayAdapter<Lab> {
    private LayoutInflater inflater;
    private int layout;
    private List<Lab> labs;

    public NameLabsTeacherAdapter(Context context, int resource, List<Lab> list) {
        super(context, resource, list);
        this.labs = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView nameLab = (TextView) view.findViewById(R.id.name_lab_teacher_1);

        Lab lab = labs.get(position);

        nameLab.setText(lab.NameLab);

        return view;
    }

}

