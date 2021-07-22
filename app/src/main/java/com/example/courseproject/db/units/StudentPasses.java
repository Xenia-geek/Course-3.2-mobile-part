package com.example.courseproject.db.units;

public class StudentPasses {
    public int PassedQuantity;
    public int IDLab;

    public StudentPasses(int PassedQuantity) {
        this.PassedQuantity = PassedQuantity;
    }

    public StudentPasses(int PassedQuantity, int IDLab) {
        this.IDLab = IDLab;
    }


}
