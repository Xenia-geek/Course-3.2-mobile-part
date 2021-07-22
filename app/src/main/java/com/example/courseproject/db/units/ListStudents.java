package com.example.courseproject.db.units;

public class ListStudents {
    public int IDStudent;
    public int IDGroup;

    public ListStudents(int IDGroup) {
        this.IDGroup = IDGroup;
    }

    public ListStudents(int IDStudent, int IDGroup) {
        this.IDStudent = IDStudent;
        this.IDGroup = IDGroup;
    }

}
