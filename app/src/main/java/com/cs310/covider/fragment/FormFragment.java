package com.cs310.covider.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FormFragment extends MyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    public static HashMap<String, Integer> map = new HashMap<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment form.
     */
    // TODO: Rename and change types and number of parameters
    public static FormFragment newInstance(String param1, String param2) {
        FormFragment fragment = new FormFragment();
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
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner symptomsSpinner = rootView.findViewById(R.id.form_symptoms_selection);
        Spinner testSpiner = rootView.findViewById(R.id.form_test_selection);
        String[] symptomsItems = new String[]{"Yes", "No"};
        String[] testItems = new String[]{"Positive", "Negative"};
        ArrayAdapter<String> symptomsAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, symptomsItems);
        ArrayAdapter<String> testAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, testItems);
        symptomsSpinner.setAdapter(symptomsAdapter);
        testSpiner.setAdapter(testAdapter);
        Button button = rootView.findViewById(R.id.form_button);
        Util.getCurrentUserTask().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (Util.userDidTodayCheck(user)) {
                    ((MainActivity) getActivity()).changeToAuthedMenu();
                    openDialog("You already did today's check!");
                } else {
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CheckBox checkBox = rootView.findViewById(R.id.form_checkbox_agree);
                            if (!checkBox.isChecked()) {
                                openDialog("Agree to share your data is not checked!");
                                return;
                            }
                            boolean hasSymptoms = true;
                            boolean hasPositiveTest = true;
                            String symptomsSelection = symptomsSpinner.getSelectedItem().toString();
                            String message = "Are you sure you have covid symptoms ";
                            if (symptomsSelection.equals(symptomsItems[1])) {
                                message = "Are you sure you don't have covid symptoms ";
                                hasSymptoms = false;
                            }
                            String testSelection = testSpiner.getSelectedItem().toString();
                            if (testSelection.equals(testItems[0])) {
                                message += "and have a positive covid test result?";
                            } else {
                                hasPositiveTest = false;
                                message += "and have a negative covid test result?";
                            }
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
                            builder1.setMessage(message);
                            builder1.setCancelable(false);
                            boolean finalHasPositiveTest = hasPositiveTest;
                            boolean finalHasSymptoms = hasSymptoms;
                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            user.setLastCheckDate(new Date());
                                            String title = null;
                                            String message = null;
                                            if (finalHasPositiveTest) {
                                                user.setLastInfectionDate(new Date());
                                                title = "New Positive Test Alert";
                                                message = "User " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + " in your course reported positive COVID test! Please get checked ASAP!";
                                            }
                                            if (finalHasSymptoms) {
                                                user.setLastSymptomsDate(new Date());
                                                if (title == null && message == null) {
                                                    title = "New Symptoms Alert";
                                                    message = "User " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + " in your course reported likely COVID symptoms, stay safe!";
                                                }
                                            }
                                            String finalTitle = title;
                                            String finalMessage = message;
                                            Util.getCurrentUserTask().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    User user = documentSnapshot.toObject(User.class);
                                                    for (String courseID : user.getUserCoursesIDs()) {
                                                        ((MainActivity) getActivity()).sendNotificationToTopic(finalTitle, finalMessage, courseID);
                                                    }
                                                }
                                            });
                                            FirebaseFirestore.getInstance().collection("Users").document(user.getEmail()).set(user).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull @NotNull Exception e) {
                                                    openDialog(e.getMessage());
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    ((MainActivity) getActivity()).changeToAuthedMenu();
                                                    openDialog("Thank you for completing today's check!");
                                                }
                                            });
                                        }
                                    });
                            builder1.setNegativeButton("No", (dialog, id) -> dialog.cancel());
                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                    });
                }
            }
        });
    }
}