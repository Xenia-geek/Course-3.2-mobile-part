package com.example.courseproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static DBHelper instance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "fit.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static public synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL("PRAGMA foreign_keys=ON");

        //ListStudents
        db.execSQL("CREATE TABLE IF NOT EXISTS ListStudents(IDStudent INTEGER PRIMARY KEY," +
                "IDGroup INTEGER NOT NULL)");

        //Student
        db.execSQL("CREATE TABLE IF NOT EXISTS Student(Login TEXT PRIMARY KEY," +
                "Password INTEGER NOT NULL," +
                "IDStudent INTEGER NOT NULL," +
                "Email TEXT," +
                "AboutMe TEXT," +
                "Name TEXT NOT NULL," +
                "Surname TEXT NOT NULL," +
                "Groups INTEGER NOT NULL," +
                "SubGroup INTEGER NOT NULL," +
                "Course INTEGER NOT NULL," +
                "Speciality TEXT NOT NULL)");

        //ListLab
        db.execSQL("CREATE TABLE IF NOT EXISTS ListLab(IDLab INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NameLab TEXT NOT NULL," +
                "Quantity INTEGER NOT NULL," +
                "IDTeacher INTEGER NOT NULL," +
                "WeekDay TEXT," +
                "IDGroup INTEGER NOT NULL," +
                "IDSem INTTEGER NOT NULL)");


        //ListLab
        db.execSQL("CREATE TABLE IF NOT EXISTS ListLab1(IDLabList INTEGER PRIMARY KEY AUTOINCREMENT, IDLab INTEGER NOT NULL," +
                "NameLab TEXT NOT NULL," +
                "Quantity INTEGER NOT NULL," +
                "IDTeacher INTEGER NOT NULL," +
                "WeekDay TEXT NOT NULL," +
                "IDGroup INTEGER NOT NULL," +
                "IDSem INTTEGER NOT NULL)");


        //Teacher that i pass lab
        db.execSQL("CREATE TABLE IF NOT EXISTS TeachersForLab(IDTeacher INTEGER NOT NULL," +
                "Name TEXT NOT NULL," +
                "Surname TEXT NOT NULL)");

        //PlansPass
        db.execSQL("CREATE TABLE IF NOT EXISTS PlansPass(IDPlan INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDLab INTEGER NOT NULL," +
                "Date TEXT NOT NULL," +
                "Quantity INTEGER NOT NULL)");

        //Teacher
        db.execSQL("CREATE TABLE IF NOT EXISTS Teacher(Login TEXT PRIMARY KEY," +
                "Password INTEGER NOT NULL," +
                "IDTeacher INTEGER NOT NULL," +
                "Email TEXT," +
                "AboutMe TEXT," +
                "Name TEXT NOT NULL," +
                "Surname TEXT NOT NULL)");

        //ListLabTeacher
        db.execSQL("CREATE TABLE IF NOT EXISTS ListLabTeacher(IDListLabTeacher INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDTeacher INTEGER NOT NULL," +
                "IDGroup INTEGER NOT NULL," +
                "IDLab INTEGER REFERENCES ListLab(IDLab) ON UPDATE CASCADE ON DELETE CASCADE," +
                "WeekName TEXT NOT NULL)");

        //StudentPass
        db.execSQL("CREATE TABLE IF NOT EXISTS StudentPass(IDStudentPass INTEGER PRIMARY KEY AUTOINCREMENT," +
                "IDLab INTEGER NOT NULL," +
                "PassedQuantity INTEGER )");

        db.execSQL("CREATE TABLE IF NOT EXISTS RatingOfStudents(IDRating INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Name TEXT NOT NULL," +
                "Surname TEXT NOT NULL," +
                "AllQuantity INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS StudentInfoForTeacher(IDStudent INTEGER PRIMARY KEY," +
                "IDGroup INTEGER NOT NULL," +
                "Name TEXT NOT NULL," +
                "Surname TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS GroupInfoForTeacher(IDGroup INTEGER PRIMARY KEY," +
                "Course INTEGER NOT NULL," +
                "Groupss INTEGER NOT NULL," +
                "SubGroup INTEGER NOT NULL)");

    }

    public void onUpgrade(SQLiteDatabase db, int OldV, int NewV) {
        db.execSQL("drop table if exists StudentPass");
        db.execSQL("drop table if exists ListLabTeacher");
        db.execSQL("drop table if exists Teacher");
        db.execSQL("drop table if exists PlansPass");
        db.execSQL("drop table if exists ListLab");
        db.execSQL("drop table if exists Student");
        db.execSQL("drop table if exists ListStudents");
        db.execSQL("drop table if exists Semestr");
        db.execSQL("drop table if exists TeachersForLab");
        db.execSQL("drop table if exists RatingOfStudents");
        db.execSQL("drop table if exists GroupInfoForTeacher");
        db.execSQL("drop table if exists StudentInfoForTeacher");
        onCreate(db);
    }

}
