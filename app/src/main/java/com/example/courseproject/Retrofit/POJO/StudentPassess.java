package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StudentPassess {
    @SerializedName("IDStudentPass")
    @Expose
    private int IDStudentPass;
    @SerializedName("IDStudent")
    @Expose
    private int IDStudent;
    @SerializedName("IDTeacher")
    @Expose
    private int IDTeacher;
    @SerializedName("IDLab")
    @Expose
    private int IDLab;
    @SerializedName("PassedQuantity")
    @Expose
    private int PassedQuantity;


    public int getIDStudentPass() {
        return IDStudentPass;
    }

    public void setIDStudentPass(int IDStudentPass) {

        this.IDStudentPass = IDStudentPass;
    }

    public int getIDStudent() {

        return IDStudent;
    }

    public void setIDStudent(int IDStudent) {

        this.IDStudent = IDStudent;
    }

    public int getIDTeacher() {

        return IDTeacher;
    }

    public void setIDTeacher(int IDTeacher) {

        this.IDTeacher = IDTeacher;
    }

    public int getIDLab() {

        return IDLab;
    }

    public void setIDLab(int IDLab) {

        this.IDLab = IDLab;
    }

    public int getPassedQuantity() {

        return PassedQuantity;
    }

    public void setPassedQuantity(int PassedQuantity) {

        this.PassedQuantity = PassedQuantity;
    }

}
