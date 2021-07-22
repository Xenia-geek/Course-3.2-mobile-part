package com.example.courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.courseproject.R;
import com.example.courseproject.db.units.GroupLab;

import java.util.List;

public class GroupsTeachersLabAdapter extends ArrayAdapter<GroupLab> {
    private LayoutInflater inflater;
    private int layout;
    private List<GroupLab> ListOfAllGgroupOfLabTeacher;

    public GroupsTeachersLabAdapter(Context context, int resource, List<GroupLab> list) {
        super(context, resource, list);
        this.ListOfAllGgroupOfLabTeacher = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView group_lab = (TextView) view.findViewById(R.id.group_of_pass_gr);
        TextView weekLab = (TextView) view.findViewById(R.id.weekday_of_pass_gr);

        GroupLab lab = ListOfAllGgroupOfLabTeacher.get(position);

        group_lab.setText(String.valueOf(lab.Course) + " course " + String.valueOf(lab.Group) + " group " + String.valueOf(lab.SubGroup) + " subgroup");
        weekLab.setText("WeekDay pass: " + lab.WeekDay);

        return view;
    }

}
