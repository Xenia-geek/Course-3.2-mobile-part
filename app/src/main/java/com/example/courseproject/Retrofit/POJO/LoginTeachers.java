package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginTeachers {
    @SerializedName("Login")
    @Expose
    private String Login;
    @SerializedName("Password")
    @Expose
    private int Password;
    @SerializedName("IDTeacher")
    @Expose
    private int IDTeacher;
    @SerializedName("Email")
    @Expose
    private String Email;
    @SerializedName("AboutMe")
    @Expose
    private String AboutMe;


    public String getLogin() {
        return Login;
    }

    public void setLogin(String Login) {
        this.Login = Login;
    }

    public int getPassword() {
        return Password;
    }

    public void setPassword(int Password) {
        this.Password = Password;
    }

    public int getIDTeacher() {
        return IDTeacher;
    }

    public void setIDTeacher(int IDTeacher) {
        this.IDTeacher = IDTeacher;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getAboutMe() {
        return AboutMe;
    }

    public void setAboutMe(String AboutMe) {
        this.AboutMe = AboutMe;
    }


}
