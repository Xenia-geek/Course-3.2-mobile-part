package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("IDGroup")
    @Expose
    private int IDGroup;
    @SerializedName("NumberGroup")
    @Expose
    private int NumberGroup;
    @SerializedName("IDSpeciality")
    @Expose
    private String IDSpeciality;
    @SerializedName("IDSubGroup")
    @Expose
    private int IDSubGroup;
    @SerializedName("IDCource")
    @Expose
    private int IDCource;


    public int getIDGroup() {
        return IDGroup;
    }

    public void setIDGroup(int IDGroup) {
        this.IDGroup = IDGroup;
    }

    public int getNumberGroup() {
        return NumberGroup;
    }

    public void setNumberGroup(int NumberGroup) {
        this.NumberGroup = NumberGroup;
    }

    public String getIDSpeciality() {
        return IDSpeciality;
    }

    public void setIDSpeciality(String IDSpeciality) {
        this.IDSpeciality = IDSpeciality;
    }

    public int getIDSubGroup() {
        return IDSubGroup;
    }

    public void setIDSubGroup(int IDSubGroup) {
        this.IDSubGroup = IDSubGroup;
    }

    public int getIDCource() {
        return IDCource;
    }

    public void setIDCource(int IDCource) {
        this.IDCource = IDCource;
    }


}
