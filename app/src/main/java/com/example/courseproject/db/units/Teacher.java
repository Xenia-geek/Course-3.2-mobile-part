package com.example.courseproject.db.units;

public class Teacher {
    public String Login;
    public int Password;
    public int IDTeacher;
    public String Email;
    public String About_Me;
    public String Name;
    public String Surname;

    public Teacher(String Login) {
        this.Login = Login;
    }

    public Teacher(String Name, String Surname) {

        this.Name = Name;
        this.Surname = Surname;
    }

    public Teacher(String Login, String Name, String Surname, int IDTeacher, String Email, String About_Me) {

        this.Login = Login;
        this.Name = Name;
        this.Surname = Surname;
        this.IDTeacher = IDTeacher;
        this.Email = Email;
        this.About_Me = About_Me;
    }

    public Teacher(String Login, int IDTeacher) {
        this.Login = Login;
        this.IDTeacher = IDTeacher;
    }

}
