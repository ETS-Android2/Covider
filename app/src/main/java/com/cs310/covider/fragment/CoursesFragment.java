package com.cs310.covider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.model.Course;
import com.cs310.covider.model.CoursesAdapter;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoursesFragment extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CoursesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment courses.
     */
    // TODO: Rename and change types and number of parameters
    public static CoursesFragment newInstance(String param1, String param2) {
        CoursesFragment fragment = new CoursesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_courses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RefrashList();
    }

    @Override
    public void onResume() {
        super.onResume();
        RefrashList();
    }

    public void RefrashList() {
        Util.getCurrentUserTask().addOnSuccessListener((DocumentSnapshot d) -> {
            User user = d.toObject(User.class);
            ArrayList<Task> tasks = new ArrayList<>();
            if (user.getUserCoursesIDs().isEmpty()) {
                openDialog("You have 0 courses added, please add course to your schedule!");
                redirectToHome();
                return;
            }
            for (String courseID : user.getUserCoursesIDs()) {
                tasks.add(FirebaseFirestore.getInstance().collection("Courses").document(courseID).get());
            }
            ListView listView = rootView.findViewById(R.id.courses_main_list);
            Tasks.whenAllComplete(tasks.toArray(new Task[0])).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                @Override
                public void onSuccess(List<Task<?>> tasks) {
                    ArrayList<Course> courses = new ArrayList<>();
                    for (Task task : tasks) {
                        courses.add(((DocumentSnapshot) task.getResult()).toObject(Course.class));

                    }
                    CoursesAdapter adapter = new CoursesAdapter(getActivity(), 0, courses);
                    listView.setAdapter(adapter);
                    MainActivity.ListUtils.setDynamicHeight(listView);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    openDialog(e.getMessage());
                }
            });
        });
    }
}