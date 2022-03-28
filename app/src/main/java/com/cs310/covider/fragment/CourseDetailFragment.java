package com.cs310.covider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;

import com.cs310.covider.model.Course;
import com.cs310.covider.model.User;
import com.cs310.covider.model.UsersAdapter;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailFragment extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "course_id";

    // TODO: Rename and change types of parameters
    private String courseID;

    public CourseDetailFragment() {
        // Required empty public constructor
    }

    public static CourseDetailFragment newInstance(String param1) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseID = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore.getInstance().collection("Courses").document(courseID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Course course = documentSnapshot.toObject(Course.class);
                TextView nameAndSectionView = rootView.findViewById(R.id.course_detail_name_section);
                String[] fileds = course.getId().split("=");
                String nameAndSection = fileds[0] + " section " + fileds[1];
                nameAndSectionView.setText(nameAndSection);
                ArrayList<Task> instructorTasks = new ArrayList<>();
                ArrayList<Task> studentTasks = new ArrayList<>();
                for (String email : course.getInstructorsEmails()) {
                    instructorTasks.add(Util.getUserWithEmailTask(email));
                }
                if (course.getStudentsEmails().isEmpty()) {
                    rootView.findViewById(R.id.course_details_no_student_message).setVisibility(View.VISIBLE);
                } else {
                    for (String email : course.getStudentsEmails()) {
                        studentTasks.add(Util.getUserWithEmailTask(email));
                    }
                }
                Tasks.whenAllComplete(instructorTasks.toArray(new Task[0])).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                    @Override
                    public void onSuccess(List<Task<?>> tasks) {
                        UsersAdapter instructors = new UsersAdapter(getActivity(), new ArrayList<>());
                        for (Task task : tasks) {
                            instructors.add(((DocumentSnapshot) task.getResult()).toObject(User.class));

                        }
                        ((ListView) rootView.findViewById(R.id.courses_detail_instructors_list)).setAdapter(instructors);
                        MainActivity.ListUtils.setDynamicHeight(rootView.findViewById(R.id.courses_detail_instructors_list));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        openDialog(e.getMessage());
                    }
                });
                if (studentTasks.isEmpty()) {
                    return;
                }
                Tasks.whenAllComplete(studentTasks.toArray(new Task[0])).addOnSuccessListener(new OnSuccessListener<List<Task<?>>>() {
                    @Override
                    public void onSuccess(List<Task<?>> tasks) {
                        UsersAdapter students = new UsersAdapter(getActivity(), new ArrayList<>());
                        for (Task task : tasks) {
                            students.add(((DocumentSnapshot) task.getResult()).toObject(User.class));
                        }
                        ((ListView) rootView.findViewById(R.id.courses_detail_student_list)).setAdapter(students);
                        MainActivity.ListUtils.setDynamicHeight(rootView.findViewById(R.id.courses_detail_student_list));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        openDialog(e.getMessage());
                    }
                });
            }
        });
    }
}