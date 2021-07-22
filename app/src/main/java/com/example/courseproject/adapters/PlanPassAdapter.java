package com.example.courseproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.courseproject.MainActivity;
import com.example.courseproject.R;
import com.example.courseproject.db.units.PlansPass;

import java.util.List;

public class PlanPassAdapter extends ArrayAdapter<PlansPass> {
    private LayoutInflater inflater;
    private int layout;
    private List<PlansPass> plan_pass;

    public PlanPassAdapter(Context context, int resource, List<PlansPass> list) {
        super(context, resource, list);
        this.plan_pass = list;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView datepass = (TextView) view.findViewById(R.id.date_pass);
        TextView quantitypass = (TextView) view.findViewById(R.id.quantity_pass);

        PlansPass plan = plan_pass.get(position);

        datepass.setText("Date:  " + plan.Date);
        quantitypass.setText("Quantity to pass:  " + plan.Quantity);

        return view;
    }

}


