package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Students {
    @SerializedName("IDStudent")
    @Expose
    private int IDStudent;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Surname")
    @Expose
    private String Surname;
    @SerializedName("IDGroup")
    @Expose
    private int IDGroup;


    public int getIDStudent() {
        return IDStudent;
    }

    public void setIDStudent(int IDStudent) {

        this.IDStudent = IDStudent;
    }

    public String getName() {

        return Name;
    }

    public void setName(String Name) {

        this.Name = Name;
    }

    public String getSurname() {

        return Surname;
    }

    public void setSurname(String Surname) {

        this.Surname = Surname;
    }

    public int getIDGroup() {

        return IDGroup;
    }

    public void setIDGroup(int IDGroup) {
        this.IDGroup = IDGroup;
    }

}
