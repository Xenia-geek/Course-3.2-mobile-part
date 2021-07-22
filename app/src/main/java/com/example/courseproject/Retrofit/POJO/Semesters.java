package com.example.courseproject.Retrofit.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Semesters {
    @SerializedName("IDSem")
    @Expose
    private int IDSem;
    @SerializedName("MonthDayStart")
    @Expose
    private String MonthDayStart;
    @SerializedName("MonthDayEnd")
    @Expose
    private String MonthDayEnd;


    public int getIDSem() {
        return IDSem;
    }

    public void setIDSem(int IDSem) {

        this.IDSem = IDSem;
    }

    public String getMonthDayStart() {

        return MonthDayStart;
    }

    public void setMonthDayStart(String MonthDayStart) {

        this.MonthDayStart = MonthDayStart;
    }

    public String getMonthDayEnd() {

        return MonthDayEnd;
    }

    public void setMonthDayEnd(String MonthDayEnd) {

        this.MonthDayEnd = MonthDayEnd;
    }

}
