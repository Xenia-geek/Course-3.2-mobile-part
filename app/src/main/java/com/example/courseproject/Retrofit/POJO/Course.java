package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("IDCource")
    @Expose
    private int IDCource;
    @SerializedName("NumberCource")
    @Expose
    private int NumberCource;

    public int getIDCource() {
        return IDCource;
    }

    public void setIDCource(int IDCource) {
        this.IDCource = IDCource;
    }

    public int getNumberCource() {
        return NumberCource;
    }

    public void setNumberCource(int NumberCource) {
        this.NumberCource = NumberCource;
    }

}
