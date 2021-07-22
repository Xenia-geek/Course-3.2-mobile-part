package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Teachers {
    @SerializedName("IDTeacher")
    @Expose
    private int IDTeacher;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Surname")
    @Expose
    private String Surname;


    public int getIDTeacher() {
        return IDTeacher;
    }

    public void setIDTeacher(int IDTeacher) {

        this.IDTeacher = IDTeacher;
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


}
