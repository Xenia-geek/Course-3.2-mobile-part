package com.example.courseproject.db.units;

public class GroupLab {

    public int IDGroup;
    public int Group;
    public int Course;
    public int SubGroup;
    public String WeekDay;
    public int IDStudent;

    public GroupLab(int IDStudent) {
        this.IDStudent = IDStudent;
    }

    public GroupLab(int Course, int Group, int SubGroup) {
        this.Course = Course;
        this.Group = Group;
        this.SubGroup = SubGroup;
    }

    public GroupLab(int IDGroup, String WeekDay, int Course, int Group, int SubGroup) {
        this.IDGroup = IDGroup;
        this.WeekDay = WeekDay;
        this.Course = Course;
        this.Group = Group;
        this.SubGroup = SubGroup;
    }


}
