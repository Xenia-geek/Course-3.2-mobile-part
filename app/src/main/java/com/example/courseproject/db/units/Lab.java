package com.example.courseproject.db.units;

public class Lab {
    public String NameLab;
    public int IDLab;
    public String WeekDay;
    public int Quantity;
    public int IDSem;
    public int IDTeacher;
    public int IDGroup;

    public Lab(int IDGroup) {

        this.IDGroup = IDGroup;
    }

    public Lab(String NameLab, int IDLab) {

        this.NameLab = NameLab;
        this.IDLab = IDLab;
    }

    public Lab(String NameLab, String WeekDay, int IDLab) {
        this.NameLab = NameLab;
        this.WeekDay = WeekDay;
        this.IDLab = IDLab;
    }

    public Lab(int IDGroup, String NameLab, String WeekDay) {
        this.NameLab = NameLab;
        this.WeekDay = WeekDay;
        this.IDGroup = IDGroup;
    }

    public Lab(int IDLab, int Quantity, String WeekDay, int IDSem) {
        this.IDLab = IDLab;
        this.Quantity = Quantity;
        this.WeekDay = WeekDay;
        this.IDSem = IDSem;
    }

    public Lab(int IDLab, int Quantity, String WeekDay, int IDSem, int IDTeacher) {
        this.IDLab = IDLab;
        this.Quantity = Quantity;
        this.WeekDay = WeekDay;
        this.IDSem = IDSem;
        this.IDTeacher = IDTeacher;
    }

    public Lab(String NameLab, int Quantity, int IDTeacher, String WeekDay) {
        this.NameLab = NameLab;
        this.Quantity = Quantity;
        this.IDTeacher = IDTeacher;
        this.WeekDay = WeekDay;
    }

}
