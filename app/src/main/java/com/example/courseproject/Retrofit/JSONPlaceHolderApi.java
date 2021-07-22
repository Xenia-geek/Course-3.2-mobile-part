package com.example.courseproject.Retrofit;

import com.example.courseproject.Retrofit.POJO.Course;
import com.example.courseproject.Retrofit.POJO.Group;
import com.example.courseproject.Retrofit.POJO.ListLabTeachers;
import com.example.courseproject.Retrofit.POJO.ListLabs;
import com.example.courseproject.Retrofit.POJO.LoginStudents;
import com.example.courseproject.Retrofit.POJO.LoginTeachers;
import com.example.courseproject.Retrofit.POJO.Semesters;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.Retrofit.POJO.Teachers;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface JSONPlaceHolderApi {
    @GET("Students/{login}")
    public Call<LoginStudents> getStudentByLogin(@Path("login") String Login);

    @GET("Teachers/{login}")
    public Call<LoginTeachers> getTeacherByLogin(@Path("login") String Login);

    @GET("ListStudents/{id}")
    public Call<Students> getStudent(@Path("id") int IDStudent);

    @GET("ListStudents")
    public Call<List<Students>> getAllStudents();


    @GET("ListTeachers/{id}")
    public Call<Teachers> getTeacher(@Path("id") int ID_Teacher_lab_add);

    @GET("Students")
    public Call<List<LoginStudents>> getStudentByID();

    @GET("Teachers")
    public Call<List<LoginTeachers>> getTeacherByID();

    @POST("Students")
    public Call<LoginStudents> addStudent(@Body LoginStudents Student);

    @POST("Teachers")
    public Call<LoginTeachers> addTeacher(@Body LoginTeachers Teacher);

    @GET("Groups/{id}")
    public Call<Group> getGroup(@Path("id") int IDGroup);

    @GET("Cources/{id}")
    public Call<Course> getCourse(@Path("id") int IDCource);

    @PUT("Students/{login}")
    public Call<LoginStudents> updateStudent(@Body LoginStudents Student, @Path("login") String Login);

    @PUT("Teachers/{login}")
    public Call<LoginTeachers> updateTeacher(@Body LoginTeachers Teacher, @Path("login") String Login);


    @GET("Semestrs/{id}")
    public Call<Semesters> getSemesterByID(@Path("id") int IDSem);

    @GET("ListLabs")
    public Call<List<ListLabs>> getListLab();

    @GET("ListLabs/{id}")
    public Call<ListLabs> getListLabByID(@Path("id") int IDLab);

    @GET("ListLabTeachers")
    public Call<List<ListLabTeachers>> getListLabTeachers();

    @GET("Semestrs")
    public Call<List<Semesters>> getSemesterAll();

    @GET("StudentPasses")
    public Call<List<StudentPassess>> getStudentPasses();

    @POST("StudentPasses")
    public Call<StudentPassess> addStudentPasses(@Body StudentPassess Pass);

    @POST("StudentPasses/{id}")
    public Call<StudentPassess> updateStudentPasses(@Path("id") int IDStudentPass, @Body StudentPassess Pass);


}
