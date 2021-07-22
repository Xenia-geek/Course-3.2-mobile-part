package com.example.courseproject.Teachers_Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.db.DBHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateLoginInfoActivity extends AppCompatActivity {

    SQLiteDatabase db;
    TextView login_value;
    EditText password_value;
    EditText email_value;
    EditText about_me_value;
    int IDTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);
        Intent intent = getIntent();
        //parameters Teacher
        String Login = intent.getStringExtra("login");
        int Password = intent.getIntExtra("password", -1);
        IDTeacher = intent.getIntExtra("id_teacher", -1);
        String Email = intent.getStringExtra("email");
        String About_Me = intent.getStringExtra("about_me");

        login_value = (TextView) findViewById(R.id.login_value);
        password_value = (EditText) findViewById(R.id.user_update_password);
        email_value = (EditText) findViewById(R.id.user_email_update);
        about_me_value = (EditText) findViewById(R.id.user_about_me_update);

        login_value.setText(Login);
        password_value.setText(String.valueOf(Password));
        email_value.setText(Email);
        about_me_value.setText(About_Me);
    }

    public void Back(View view) {
        Intent intent = new Intent(this, MainActivityTeacher.class);
        startActivity(intent);
    }

    public void Update(View view) {
        JSONPlaceHolderApi apiJSON = NetworkService.getApi();
        db = DBHelper.getInstance(getApplicationContext()).getReadableDatabase();
        String Login = login_value.getText().toString();
        if (Login != null) {
            int Password_add = Integer.parseInt(password_value.getText().toString());
            String Email_add = email_value.getText().toString();
            String About_Me_add = about_me_value.getText().toString();

            LoginTeachers teacher_for_update = new LoginTeachers();
            teacher_for_update.setLogin(Login);
            teacher_for_update.setPassword(Password_add);
            teacher_for_update.setIDTeacher(IDTeacher);
            teacher_for_update.setEmail(Email_add);
            teacher_for_update.setAboutMe(About_Me_add);
            apiJSON.updateTeacher(teacher_for_update, Login).enqueue(new Callback<LoginTeachers>() {
                @Override
                public void onResponse(Call<LoginTeachers> call, Response<LoginTeachers> response) {
                    if (response.isSuccessful()) {
                        try {
                            db.execSQL("update  Teacher set Password=" +
                                    +Password_add + ", Email='" + Email_add + "', AboutMe ='" + About_Me_add + "'");
                            Toast.makeText(UpdateLoginInfoActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UpdateLoginInfoActivity.this, MainActivityTeacher.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(UpdateLoginInfoActivity.this, "Not updated", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(UpdateLoginInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<LoginTeachers> call, Throwable t) {
                    Toast.makeText(UpdateLoginInfoActivity.this, "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }

    }
}