package com.example.courseproject.db.units;

public class PlansPass {
    public int Quantity;
    public int IDPlan;
    public int IDGroup;
    public int IDLab;
    public String Date;

    public PlansPass(int IDPlan, int Quantity) {
        this.IDPlan = IDPlan;
        this.Quantity = Quantity;
    }

    public PlansPass(int Quantity, String Date) {
        this.Date = Date;
        this.Quantity = Quantity;
    }

    public PlansPass(int Quantity) {
        this.Quantity = Quantity;
    }

    public PlansPass() {

    }
}
