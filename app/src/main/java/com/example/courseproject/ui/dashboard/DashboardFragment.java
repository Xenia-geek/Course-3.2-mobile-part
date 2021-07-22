package com.example.courseproject.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.courseproject.CategoryActivity;
import com.example.courseproject.EditTaskActivity;
import com.example.courseproject.R;
import com.example.courseproject.xmlcalendar.Serializator;
import com.example.courseproject.xmlcalendar.Share;
import com.example.courseproject.xmlcalendar.Task;
import com.example.courseproject.xmlcalendar.WorkWithFile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.courseproject.R;
import com.example.courseproject.ui.dashboard.DashboardViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class DashboardFragment extends Fragment {

           /* private DashboardViewModel dashboardViewModel;

            public View onCreateView(@NonNull LayoutInflater inflater,
                                     ViewGroup container, Bundle savedInstanceState) {
                dashboardViewModel =
                        new ViewModelProvider(this).get(DashboardViewModel.class);
                View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
                final TextView textView = root.findViewById(R.id.text_dashboard);
                dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        textView.setText(s);
                    }
                });
                return root;
            }
        }

            */


    CalendarView calendarView;
    ListView listTask;
    Calendar dateAndTime = Calendar.getInstance();
    String date;
    ArrayAdapter<Task> adapterTask;

    Button addCategoryButton, addButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        calendarView = root.findViewById(R.id.calendarView);
        listTask = root.findViewById(R.id.listTask);

        File file = new File(getContext().getFilesDir(), "task.xml");
        if (file.exists() == true) {
            Share.document = WorkWithFile.Xml.parseFromFile(file);
            Serializator.parse(Share.document);
        }

        listTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                intent.putExtra("isCreate", false);
                intent.putExtra("id", position);
                startActivity(intent);
            }
        });

        addCategoryButton = root.findViewById(R.id.addButtonCategory);
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        addButton = root.findViewById(R.id.addButtonTask);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Share.Categories.getList().size() > 0) {
                    Intent intent = new Intent(getActivity(), EditTaskActivity.class);
                    intent.putExtra("isCreate", true);
                    intent.putExtra("date", date);

                    startActivity(intent);
                } else {
                    msg(getActivity(), "Вначале нужно добавить хотя бы одну категорию!");
                }
            }
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            date = DateUtils.formatDateTime(getContext(),
                    dateAndTime.getTimeInMillis(),
                    DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);

            search();
        });
        firstcheck();

        return root;
    }


    public void firstcheck() {
        date = DateUtils.formatDateTime(getContext(),
                calendarView.getDate(),
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
        search();
    }

    public void search() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        for (Task task : Share.Tasks.getList()) {
            if (task.getDate().equals(date) == true) {
                taskList.add(task);
            }
        }
        adapterTask = new ArrayAdapter<Task>(getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                taskList);
        listTask.setAdapter(adapterTask);
    }

    public void msg(Context context, String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }
}