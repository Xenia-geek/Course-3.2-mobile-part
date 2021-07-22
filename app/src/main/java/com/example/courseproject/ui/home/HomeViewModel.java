package com.example.courseproject.ui.home;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.courseproject.MainActivity;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Student;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> login;
    private MutableLiveData<String> name;
    private MutableLiveData<String> group;
    private MutableLiveData<String> email;
    private MutableLiveData<String> about_me;

    SQLiteDatabase db;

    public HomeViewModel() {
        login = new MutableLiveData<>();
        name = new MutableLiveData<>();
        group = new MutableLiveData<>();
        email = new MutableLiveData<>();
        about_me = new MutableLiveData<>();
    }

    public LiveData<String> getLogin(String login_student) {
        login.setValue(login_student);
        return login;
    }

    public LiveData<String> getName(String name_student) {
        name.setValue(name_student);
        return name;
    }

    public LiveData<String> getGroup(String group_student) {
        group.setValue(group_student);
        return group;
    }

    public LiveData<String> getEmail(String email_student) {
        email.setValue(email_student);
        return email;
    }

    public LiveData<String> getAbout_Me(String about_me_student) {
        about_me.setValue(about_me_student);
        return about_me;
    }


}