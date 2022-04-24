package com.cs310.covider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.model.Building;
import com.cs310.covider.model.Course;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.messaging.FirebaseMessaging;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Objects;

public class AddCourseFragment extends MyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddCourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCourseFragment newInstance(String param1, String param2) {
        AddCourseFragment fragment = new AddCourseFragment();
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
        return inflater.inflate(R.layout.fragment_addcourse, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore.getInstance().collection("Buildings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Spinner spinner = rootView.findViewById(R.id.add_course_building_list);
                    ArrayList<Building> buildings = new ArrayList<>(task.getResult().toObjects(Building.class));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
                    for (Building building : buildings) {
                        adapter.add(((MainActivity) getActivity()).buildingAbrevToFullName(building.getName()));
                    }
                    spinner.setAdapter(adapter);
                    Button button = rootView.findViewById(R.id.add_course_button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String buildingName = buildings.get(spinner.getSelectedItemPosition()).getName();
                            String courseName = ((EditText) rootView.findViewById(R.id.add_course_course_name)).getText().toString().toUpperCase();
                            String section = ((EditText) rootView.findViewById(R.id.add_course_section)).getText().toString().toUpperCase();
                            String courseID = courseName + '=' + section;
                            FirebaseFirestore.getInstance().collection("Courses").document(courseID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        openDialog(task);
                                        redirectToHome();
                                        return;
                                    }
                                    if (task.getResult().exists()) {
                                        Course course = task.getResult().toObject(Course.class);
                                        if (!Objects.equals(course.getBuildingName(), buildingName)) {
                                            openDialog("Course building is incorrect!");
                                            return;
                                        }
                                        Objects.requireNonNull(Util.getCurrentUserTask()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                User currentUser = documentSnapshot.toObject(User.class);
                                                switch (Objects.requireNonNull(currentUser).getUserType()) {
                                                    case INSTRUCTOR: {
                                                        if (course.getInstructorsEmails().contains(currentUser.getEmail())) {
                                                            openDialog("Course already in your account!");
                                                            break;
                                                        }
                                                        course.getInstructorsEmails().add(currentUser.getEmail());
                                                        addExistingCourseToUser(currentUser, course, task);
                                                        break;
                                                    }
                                                    case STUDENT: {
                                                        if (course.getStudentsEmails().contains(currentUser.getEmail())) {
                                                            openDialog("Course already in your account!");
                                                            break;
                                                        }
                                                        course.getStudentsEmails().add(currentUser.getEmail());
                                                        addExistingCourseToUser(currentUser, course, task);
                                                        break;
                                                    }
                                                    default: {
                                                        break;
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(e -> openDialog(task));
                                    } else {
                                        Util.getCurrentUserTask().addOnSuccessListener(documentSnapshot -> {
                                            User currentUser = documentSnapshot.toObject(User.class);
                                            switch (currentUser.getUserType()) {
                                                case INSTRUCTOR: {
                                                    ArrayList<String> instructorEmails = new ArrayList<>();
                                                    instructorEmails.add(currentUser.getEmail());
                                                    Course newCourse = new Course(courseID, buildingName, new ArrayList<>(), instructorEmails, Course.CourseMode.INPERSON);
                                                    FirebaseFirestore.getInstance().collection("Courses").document(newCourse.getId()).set(newCourse).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull @NotNull Exception e) {
                                                            openDialog(task);
                                                        }
                                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            FirebaseFirestore.getInstance().collection("Buildings").document(buildingName).update("coursesIDs", FieldValue.arrayUnion(newCourse.getId()))
                                                                    .addOnFailureListener(e -> openDialog(task)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            FirebaseFirestore.getInstance().collection("Users").document(currentUser.getEmail()).update("userCoursesIDs", FieldValue.arrayUnion(newCourse.getId()))
                                                                                    .addOnFailureListener(e -> openDialog(task)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {
                                                                                            FirebaseMessaging.getInstance().subscribeToTopic(Base64.getEncoder().encodeToString(newCourse.getId().getBytes(StandardCharsets.UTF_8)).replace("=", ""));
                                                                                            openDialog("Success!");
                                                                                            redirectToHome();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                        }
                                                    });
                                                    break;
                                                }
                                                case STUDENT: {
                                                    openDialog("It's a new course, and new courses can only be added by instructors!");
                                                    break;
                                                }
                                                default: {
                                                    break;
                                                }
                                            }
                                        }).addOnFailureListener(e -> openDialog(task));
                                    }
                                }
                            });
                        }
                    });
                } else {
                    openDialog(task);
                }
            }
        });
    }

    private void addExistingCourseToUser(User currentUser, Course course, @NotNull @NonNull Task<DocumentSnapshot> task) {
        if (currentUser.getUserCoursesIDs() == null) {
            currentUser.setUserCoursesIDs(new ArrayList<>());
        }
        currentUser.getUserCoursesIDs().add(course.getId());
        FirebaseFirestore.getInstance().collection("Courses").document(course.getId()).set(course).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                openDialog(task);
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseFirestore.getInstance().collection("Users").document(currentUser.getEmail()).set(currentUser).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        openDialog(task);
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        FirebaseMessaging.getInstance().subscribeToTopic(Base64.getEncoder().encodeToString(course.getId().getBytes(StandardCharsets.UTF_8)).replace("=", ""));
                        redirectToHome();
                        openDialog("Success!");
                    }
                });
            }
        });
    }
}
