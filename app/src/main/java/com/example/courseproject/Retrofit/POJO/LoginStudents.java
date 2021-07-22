package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginStudents {
    @SerializedName("Login")
    @Expose
    private String Login;
    @SerializedName("Password")
    @Expose
    private int Password;
    @SerializedName("IDStudent")
    @Expose
    private int IDStudent;
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

    public int getIDStudent() {
        return IDStudent;
    }

    public void setIDStudent(int IDStudent) {
        this.IDStudent = IDStudent;
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
