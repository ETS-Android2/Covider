package com.cs310.covider.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs310.covider.MainActivity;
import com.cs310.covider.R;
import com.cs310.covider.model.Building;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckInFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckInFormFragment extends MyFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    //    public static HashMap<String, Integer> map = new HashMap<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckInFormFragment() {
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
    public static CheckInFormFragment newInstance(String param1, String param2) {
        CheckInFormFragment fragment = new CheckInFormFragment();
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
        return inflater.inflate(R.layout.fragment_checkin_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseFirestore.getInstance().collection("Buildings").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (!task.isSuccessful()) {
                    openDialog(task);
                    redirectToHome();
                    return;
                }
                Spinner spinner = rootView.findViewById(R.id.checkin_buildings_selection);
                ArrayList<Building> buildings = new ArrayList<>(task.getResult().toObjects(Building.class));
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
                for (Building building : buildings) {
                    adapter.add(((MainActivity) (getActivity())).buildingAbrevToFullName(building.getName()));
                }
                spinner.setAdapter(adapter);
                Button button = rootView.findViewById(R.id.checkin_submit_button);
                View child = rootView;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Building building = buildings.get(i);
                        ArrayList<Task> checkedInUserTasks = new ArrayList<>();
                        assert building != null;
                        if (Util.buildingCheckinDataValidForToday(building)) {
                            for (String checkedInEmail : building.getCheckedInUserEmails()) {
                                checkedInUserTasks.add(Util.getUserWithEmailTask(checkedInEmail));
                            }
                            Tasks.whenAllComplete(checkedInUserTasks.toArray(new Task[0])).addOnCompleteListener(tasks -> {
                                if (!tasks.isSuccessful()) {
                                    openDialog(tasks);
                                    redirectToHome();
                                    return;
                                }
                                ArrayList<User> visitors = new ArrayList<>();
                                for (Task task : tasks.getResult())
                                    visitors.add(((DocumentSnapshot) task.getResult()).toObject(User.class));
                                if (!visitors.isEmpty()) {
                                    int totalVisitor = visitors.size(), infectedCount = 0, symptomsCount = 0;
                                    for (User user : visitors) {
                                        if (user.getLastInfectionDate() != null && Util.withInTwoWeeks(user.getLastInfectionDate()))
                                            infectedCount++;
                                        if (user.getLastSymptomsDate() != null && Util.withInTwoWeeks(user.getLastSymptomsDate()))
                                            symptomsCount++;
                                    }
                                    float updatedRisk = 1.5f + (float) (.7 * infectedCount + .2 * symptomsCount + .1 * totalVisitor);
                                    ((TextView) child.findViewById(R.id.building_comp)).setText(((MainActivity) (getActivity())).buildingAbrevToFullName(building.getName()));
                                    RatingBar bar = child.findViewById(R.id.ratingBar);
                                    TextView tv = child.findViewById(R.id.req);
                                    TextView way = child.findViewById(R.id.ways);
                                    bar.setRating(updatedRisk);
                                    tv.setText(building.getEntryRequirement());
                                    way.setText(building.getHowToSatisfyRequirement());
                                }
                            });
                        } else {
                            float updatedRisk = 1.5f;
                            ((TextView) child.findViewById(R.id.building_comp)).setText(((MainActivity) (getActivity())).buildingAbrevToFullName(building.getName()));
                            RatingBar bar = child.findViewById(R.id.ratingBar);
                            TextView tv = child.findViewById(R.id.req);
                            TextView way = child.findViewById(R.id.ways);
                            bar.setRating(updatedRisk);
                            tv.setText(building.getEntryRequirement());
                            way.setText(building.getHowToSatisfyRequirement());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckBox checkBox = rootView.findViewById(R.id.checkin_checkbox_confirm);
                        if (!checkBox.isChecked()) {
                            openDialog("You have not confirmed that you meet the entry requirements!");
                            return;
                        }
                        Building selection = buildings.get(spinner.getSelectedItemPosition());
                        if (Util.userCheckedIn(selection, FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            openDialog("You already checked in to this building!");
                            redirectToHome();
                            return;
                        }
                        showYesNoDialog(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (!Util.buildingCheckinDataValidForToday(selection)) {
                                    selection.setCheckedInUserEmails(new ArrayList<>());
                                    selection.setCheckInDataValidDate(new Date());
                                }
                                selection.getCheckedInUserEmails().add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                FirebaseFirestore.getInstance().collection("Buildings").document(selection.getName()).set(selection).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Util.getCurrentUserTask().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                User user = documentSnapshot.toObject(User.class);
                                                if (user.getBuildingCheckedinTimes() == null || user.getCheckedinTimesValidSinceDate() == null || !Util.withInTwoWeeks(user.getCheckedinTimesValidSinceDate())) {
                                                    user.setBuildingCheckedinTimes(new HashMap<>());
                                                    user.setCheckedinTimesValidSinceDate(new Date());
                                                }
                                                user.getBuildingCheckedinTimes().merge(selection.getName(), 1, Integer::sum);
                                                FirebaseFirestore.getInstance().collection("Users").document(user.getEmail()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        openDialog("Success");
                                                        redirectToHome();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        openDialog(e.getMessage());
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull @NotNull Exception e) {
                                                openDialog(e.getMessage());
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        openDialog(e.getMessage());
                                    }
                                });
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }, "Are you sure you want to check into " + ((MainActivity) (getActivity())).buildingAbrevToFullName(selection.getName()) + "?");
                    }
                });
            }
        });
    }
}