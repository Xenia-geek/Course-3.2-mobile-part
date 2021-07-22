package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListLabTeachers {
    @SerializedName("IDListLabTeacher")
    @Expose
    private int IDListLabTeacher;
    @SerializedName("IDTeacher")
    @Expose
    private int IDTeacher;
    @SerializedName("IDGroup")
    @Expose
    private int IDGroup;
    @SerializedName("IDLab")
    @Expose
    private int IDLab;
    @SerializedName("WeekName")
    @Expose
    private String WeekName;


    public int getIDListLabTeacher() {
        return IDListLabTeacher;
    }

    public void setIDListLabTeacher(int IDListLabTeacher) {
        this.IDListLabTeacher = IDListLabTeacher;
    }

    public int getIDTeacher() {
        return IDTeacher;
    }

    public void setIDTeacher(int IDTeacher) {

        this.IDTeacher = IDTeacher;
    }

    public int getIDGroup() {
        return IDGroup;
    }

    public void setIDGroup(int IDGroup) {

        this.IDGroup = IDGroup;
    }

    public int getIDLab() {
        return IDLab;
    }

    public void setIDLab(int IDLab) {

        this.IDLab = IDLab;
    }

    public String getWeekName() {
        return WeekName;
    }

    public void setWeekName(String WeekName) {
        this.WeekName = WeekName;
    }


}
