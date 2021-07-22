package com.example.courseproject.ui.rating;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.courseproject.R;
import com.example.courseproject.Retrofit.JSONPlaceHolderApi;
import com.example.courseproject.Retrofit.NetworkService;
import com.example.courseproject.Retrofit.POJO.StudentPassess;
import com.example.courseproject.Retrofit.POJO.Students;
import com.example.courseproject.adapters.PlanPassAdapter;
import com.example.courseproject.adapters.RatingOfStudentsAdapter;
import com.example.courseproject.db.DBHelper;
import com.example.courseproject.db.units.Lab;
import com.example.courseproject.db.units.ListStudents;
import com.example.courseproject.db.units.RatingStudents;
import com.example.courseproject.db.units.Student;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingFragment extends Fragment {

    private RatingViewModel ratingViewModel;
    RatingOfStudentsAdapter ratingAdapter;
    private List<RatingStudents> rating = new ArrayList();
    Cursor find_id_group_of_me;
    Cursor add_to_list_rating;
    RatingStudents rating_student;
    SQLiteDatabase db;
    ListStudents student_id_group;
    int Quantity_all;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //new AsyncTaskMy().execute();
        View root = inflater.inflate(R.layout.fragment_rating, container, false);

        ListView listRating = root.findViewById(R.id.list_of_rating);

        db = DBHelper.getInstance(getActivity().getApplicationContext()).getReadableDatabase();

        //////////////////////////////////////////////////////////////
        //////////finding students that have the same group///////////
        //////////////////////////////////////////////////////////////

        int ID_Group_of_Me;

        find_id_group_of_me = db.rawQuery("Select IDGroup from ListStudents", null);
        if (find_id_group_of_me.moveToFirst()) {
            while (!find_id_group_of_me.isClosed()) {
                student_id_group = new ListStudents(find_id_group_of_me.getInt(0));
                if (!find_id_group_of_me.isLast()) {
                    find_id_group_of_me.moveToNext();
                } else {
                    find_id_group_of_me.close();
                }
            }


            ID_Group_of_Me = student_id_group.IDGroup;


            JSONPlaceHolderApi apiJSON = NetworkService.getApi();
            apiJSON.getAllStudents().enqueue(new Callback<List<Students>>() {
                @Override
                public void onResponse(Call<List<Students>> call, Response<List<Students>> response) {
                    if (response.isSuccessful()) {
                        List<Students> all_students = response.body();
                        for (Students student_of_all : all_students) {
                            if (student_of_all.getIDGroup() == ID_Group_of_Me) {
                                int ID_Student_of_Group = student_of_all.getIDStudent();
                                String Name_Student_Of_Group = student_of_all.getName();
                                String Surname_Student_Of_group = student_of_all.getSurname();
                                db.execSQL("delete from RatingOfStudents where Name='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'");


                                apiJSON.getStudentPasses().enqueue(new Callback<List<StudentPassess>>() {
                                    @Override
                                    public void onResponse(Call<List<StudentPassess>> call, Response<List<StudentPassess>> response) {
                                        if (response.isSuccessful()) {
                                            List<StudentPassess> list_of_all_passed_labs = response.body();
                                            for (StudentPassess quantity_of_Student_of_Group : list_of_all_passed_labs) {
                                                if (quantity_of_Student_of_Group.getIDStudent() == ID_Student_of_Group) {
                                                    Quantity_all = quantity_of_Student_of_Group.getPassedQuantity();
                                                    Cursor find_stud = db.rawQuery("Select AllQuantity from  RatingOfStudents WHERE Name ='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'", null);
                                                    if (find_stud.moveToFirst()) {
                                                        while (!find_stud.isClosed()) {
                                                            rating_student = new RatingStudents(find_stud.getInt(0));
                                                            if (!find_stud.isLast()) {
                                                                find_stud.moveToNext();
                                                            } else {
                                                                find_stud.close();
                                                            }
                                                        }
                                                        Quantity_all = Quantity_all + rating_student.PassedQuantity;
                                                        db.execSQL("delete from RatingOfStudents where Name='" + Name_Student_Of_Group + "' AND Surname='" + Surname_Student_Of_group + "'");
                                                    }

                                                    db.execSQL("insert into RatingOfStudents(Name, Surname, AllQuantity) VALUES('"
                                                            + Name_Student_Of_Group + "','" + Surname_Student_Of_group + "'," + Quantity_all
                                                            + ")"
                                                    );
                                                }

                                            }

                                        }
                                        call.cancel();
                                    }

                                    @Override
                                    public void onFailure(Call<List<StudentPassess>> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                                        call.cancel();
                                    }
                                });
                            }
                        }
                    }
                    call.cancel();
                }

                @Override
                public void onFailure(Call<List<Students>> call, Throwable t) {
                    Toast.makeText(getActivity(), "Check your Internet connection", Toast.LENGTH_SHORT).show();
                    call.cancel();
                }
            });
        }


        add_to_list_rating = db.rawQuery("Select Name, Surname  from RatingOfStudents ORDER BY AllQuantity DESC ", null);

        rating.clear();
        if (add_to_list_rating.moveToFirst()) {
            while (!add_to_list_rating.isClosed()) {
                rating.add(new RatingStudents(add_to_list_rating.getString(0), add_to_list_rating.getString(1)));
                if (!add_to_list_rating.isLast()) {
                    add_to_list_rating.moveToNext();
                } else {
                    add_to_list_rating.close();
                }
            }
        }

        ratingAdapter = new RatingOfStudentsAdapter(getActivity().getApplicationContext(), R.layout.list_item_rating, rating);
        listRating.setAdapter(ratingAdapter);
        ratingAdapter.notifyDataSetChanged();


        return root;
    }


    class AsyncTaskMy extends AsyncTask<Void, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }
    }


}