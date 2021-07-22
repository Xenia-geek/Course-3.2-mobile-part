package com.example.courseproject.db.units;

public class RatingStudents {
    public String Name;
    public String Surname;
    public int PassedQuantity;

    public RatingStudents(String Name, String Surname, int PassedQuantity) {
        this.Name = Name;
        this.Surname = Surname;
        this.PassedQuantity = PassedQuantity;
    }

    public RatingStudents(String Name, String Surname) {
        this.Name = Name;
        this.Surname = Surname;
    }

    public RatingStudents(int PassedQuantity) {
        this.PassedQuantity = PassedQuantity;
    }
}
