package com.example.courseproject.db.units;

public class Student {

    public String Login;
    public int Password;
    public int IDStudent;
    public String Email;
    public String About_Me;
    public int Group;
    public int SubGroup;
    public int Course;
    public String Speciality;
    public String Name;
    public String Surname;

    public Student(String Login, int IDStudent) {
        this.Login = Login;
        this.IDStudent = IDStudent;
    }

    public Student(String Login) {
        this.Login = Login;
    }

    public Student(String Speciality, int Course, int Group) {
        this.Speciality = Speciality;
        this.Course = Course;
        this.Group = Group;
    }


    public Student() {

    }

    public Student(String Login, String Email, String About_Me,
                   int Group, int SubGroup, int Course, String Speciality, String Name, String Surname) {
        this.Login = Login;
        this.Email = Email;
        this.About_Me = About_Me;
        this.Group = Group;
        this.SubGroup = SubGroup;
        this.Course = Course;
        this.Speciality = Speciality;
        this.Name = Name;
        this.Surname = Surname;
    }

    public Student(String Login, int Password, int IDStudent, String Email, String About_Me, String Name, String Surname,
                   int Group, int SubGroup, int Course, String Speciality) {
        this.Login = Login;
        this.Password = Password;
        this.IDStudent = IDStudent;
        this.Email = Email;
        this.About_Me = About_Me;
        this.Group = Group;
        this.SubGroup = SubGroup;
        this.Course = Course;
        this.Speciality = Speciality;
        this.Name = Name;
        this.Surname = Surname;
    }
}
