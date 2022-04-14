package com.cs310.covider.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs310.covider.R;
import com.cs310.covider.model.Building;
import com.cs310.covider.model.Course;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuildingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuildingFragment extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuildingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static BuildingFragment newInstance(String param1, String param2) {
        BuildingFragment fragment = new BuildingFragment();
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
        return inflater.inflate(R.layout.fragment_building, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showSpecialBuildings();
        addListener(rootView);
    }

    private void showSpecialBuildings() {
        // call to show buildings in the current user's enrolled courses
        showBuildingsInDailySchedule();
        // call to show buildings frequently visited by the current user
        showFrequentlyVisitedBuildings();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showBuildingsInDailySchedule() {
        BuildingFragment buildingFragment = this;
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout scheduleBuildingsContainer = getActivity().findViewById(R.id.buildings_in_daily_schedule);
        scheduleBuildingsContainer.removeAllViews();
        Objects.requireNonNull(Util.getCurrentUserTask()).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            ArrayList<Task> courseTask = new ArrayList<>();
            assert user != null;
            if (user.getUserCoursesIDs() != null && !user.getUserCoursesIDs().isEmpty()) {
                for (String id : user.getUserCoursesIDs())
                    courseTask.add(FirebaseFirestore.getInstance().collection("Courses").document(id).get());
                Tasks.whenAllComplete(courseTask.toArray(new Task[0])).addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        openDialog(task);
                        if (getActivity() == null)
                            return;
                        getActivity().findViewById(R.id.daily_schedule).setVisibility(View.GONE);
                        return;
                    }
                    ArrayList<Course> enrolledCourses = new ArrayList<>();
                    for (Task task1 : task.getResult())
                        enrolledCourses.add(((DocumentSnapshot) task1.getResult()).toObject(Course.class));
                    for (Course course : enrolledCourses) {
                        if (getActivity() == null)
                            return;
                        int buildingId = getResources().getIdentifier(
                                course.getBuildingName() + "_comp", "string", "com.cs310.covider");
                        LinearLayout view = new LinearLayout(getContext());
                        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        view.setBackground(getResources().getDrawable(R.drawable.custom_border));
                        view.setMinimumHeight((int) (scale * 62));
                        view.setGravity(Gravity.CENTER_VERTICAL);
                        view.setContentDescription(course.getBuildingName());
                        view.setOnClickListener(buildingFragment::showDetails);
                        TextView building = new TextView(getContext());
                        building.setText(getString(buildingId));
                        building.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        building.setTextSize((float) 15);
                        building.setGravity(Gravity.CENTER_VERTICAL);
                        building.setPadding(33, 0, 0, 0);
                        view.addView(building);
                        scheduleBuildingsContainer.addView(view);
                        getActivity().findViewById(R.id.daily_schedule).setVisibility(View.VISIBLE);
                    }
                });
            } else {
                if (getActivity() == null)
                    return;
                getActivity().findViewById(R.id.daily_schedule).setVisibility(View.GONE);
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showFrequentlyVisitedBuildings() {
        final float scale = getResources().getDisplayMetrics().density;
        LinearLayout frequentBuildingsContainer = getActivity().findViewById(R.id.buildings_frequently_visited);
        frequentBuildingsContainer.removeAllViews();
        BuildingFragment buildingFragment = this;
        Objects.requireNonNull(Util.getCurrentUserTask()).addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            ArrayList<String> frequentlyVisitedBuildingAbbrevs = new ArrayList<>();
            assert user != null;
            if (user.getBuildingCheckedinTimes() != null && !user.getBuildingCheckedinTimes().isEmpty())
                for (Map.Entry<String, Integer> entry : user.getBuildingCheckedinTimes().entrySet())
                    if (entry.getValue() > 1)
                        frequentlyVisitedBuildingAbbrevs.add(String.valueOf(entry.getKey()));
            if (frequentlyVisitedBuildingAbbrevs.isEmpty()) {
                if (getActivity() == null)
                    return;
                getActivity().findViewById(R.id.frequent_visit).setVisibility(View.GONE);
            } else {
                for (String buildingAbbrev : frequentlyVisitedBuildingAbbrevs) {
                    if (getActivity() == null)
                        return;
                    int buildingId = getActivity().getResources().getIdentifier(
                            buildingAbbrev + "_comp", "string", "com.cs310.covider");
                    LinearLayout view = new LinearLayout(getContext());
                    view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    view.setMinimumHeight((int) (scale * 62));
                    view.setGravity(Gravity.CENTER_VERTICAL);
                    view.setBackground(getResources().getDrawable(R.drawable.custom_border));
                    view.setContentDescription(buildingAbbrev);
                    view.setOnClickListener(buildingFragment::showDetails);
                    TextView building = new TextView(getContext());
                    building.setText(getString(buildingId));
                    building.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    building.setTextSize((float) 15);
                    building.setGravity(Gravity.CENTER_VERTICAL);
                    building.setPadding(33, 0, 0, 0);
                    view.addView(building);
                    frequentBuildingsContainer.addView(view);
                }
                getActivity().findViewById(R.id.frequent_visit).setVisibility(View.VISIBLE);
            }
        });
    }

    private void showDetails(@NonNull View view) {
        float defaultRisk = 1.5F;
        String buildingAbbrev = view.getContentDescription().toString();
        @SuppressLint("InflateParams") View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.building_details, null);
        PopupWindow pop = new PopupWindow(popupView, (int) (getResources().getDisplayMetrics().widthPixels * 0.92), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        int building = getResources().getIdentifier(
                buildingAbbrev + "_comp", "string", "com.cs310.covider");
        ((TextView) pop.getContentView().findViewById(R.id.building_comp)).setText(getResources().getString(building));
        RatingBar bar = pop.getContentView().findViewById(R.id.ratingBar);
        TextView tv = pop.getContentView().findViewById(R.id.req);
        TextView way = pop.getContentView().findViewById(R.id.ways);
        FirebaseFirestore.getInstance().collection("Buildings").document(buildingAbbrev).get().addOnSuccessListener(documentSnapshot -> {
            Building currBuilding = documentSnapshot.toObject(Building.class);
            ArrayList<Task> checkedInUserTasks = new ArrayList<>();
            assert currBuilding != null;
            if (Util.buildingCheckinDataValidForToday(currBuilding)) {
                for (String checkedInEmail : currBuilding.getCheckedInUserEmails())
                    checkedInUserTasks.add(Util.getUserWithEmailTask(checkedInEmail));
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
                        float updatedRisk = defaultRisk + (float) (.7 * infectedCount + .2 * symptomsCount + .1 * totalVisitor);
                        displayPopUp(updatedRisk, bar, tv, currBuilding, way, pop, view);
                    }
                });
            } else
                displayPopUp(defaultRisk, bar, tv, currBuilding, way, pop, view);
        }).addOnFailureListener(e -> openDialog(e.getMessage()));
    }

    private void displayPopUp(float risk, RatingBar bar, TextView tv, Building currBuilding, TextView way, PopupWindow pop, @NonNull View view) {
        bar.setRating(risk);
        tv.setText(currBuilding.getEntryRequirement());
        way.setText(currBuilding.getHowToSatisfyRequirement());
        pop.showAtLocation(view, Gravity.CENTER, 0, 0);
        pop.getContentView().findViewById(R.id.return_to_previous).setOnClickListener((View popup) -> {
            pop.dismiss();
        });
    }

    private void addListener(View view) {
        LinearLayout.OnClickListener clickListener = this::showDetails;
        view.findViewById(R.id.acb).setOnClickListener(clickListener);
        view.findViewById(R.id.acc).setOnClickListener(clickListener);
        view.findViewById(R.id.adm).setOnClickListener(clickListener);
        view.findViewById(R.id.aes).setOnClickListener(clickListener);
        view.findViewById(R.id.ahf1).setOnClickListener(clickListener);
        view.findViewById(R.id.ahf2).setOnClickListener(clickListener);
        view.findViewById(R.id.ahn).setOnClickListener(clickListener);
        view.findViewById(R.id.alm).setOnClickListener(clickListener);
        view.findViewById(R.id.ann).setOnClickListener(clickListener);
        view.findViewById(R.id.asc).setOnClickListener(clickListener);
        view.findViewById(R.id.bhe).setOnClickListener(clickListener);
        view.findViewById(R.id.bit).setOnClickListener(clickListener);
        view.findViewById(R.id.bks).setOnClickListener(clickListener);
        view.findViewById(R.id.bmh).setOnClickListener(clickListener);
        view.findViewById(R.id.bri).setOnClickListener(clickListener);
        view.findViewById(R.id.bsr).setOnClickListener(clickListener);
        view.findViewById(R.id.cal).setOnClickListener(clickListener);
        view.findViewById(R.id.cas).setOnClickListener(clickListener);
        view.findViewById(R.id.cdf).setOnClickListener(clickListener);
        view.findViewById(R.id.cem).setOnClickListener(clickListener);
        view.findViewById(R.id.cic).setOnClickListener(clickListener);
        view.findViewById(R.id.clh).setOnClickListener(clickListener);
        view.findViewById(R.id.cpa).setOnClickListener(clickListener);
        view.findViewById(R.id.crc).setOnClickListener(clickListener);
        view.findViewById(R.id.ctf).setOnClickListener(clickListener);
        view.findViewById(R.id.ctv).setOnClickListener(clickListener);
        view.findViewById(R.id.dcc).setOnClickListener(clickListener);
        view.findViewById(R.id.den).setOnClickListener(clickListener);
        view.findViewById(R.id.dml).setOnClickListener(clickListener);
        view.findViewById(R.id.dmt).setOnClickListener(clickListener);
        view.findViewById(R.id.dps).setOnClickListener(clickListener);
        view.findViewById(R.id.drb).setOnClickListener(clickListener);
        view.findViewById(R.id.drc).setOnClickListener(clickListener);
        view.findViewById(R.id.dxm).setOnClickListener(clickListener);
        view.findViewById(R.id.eeb).setOnClickListener(clickListener);
        view.findViewById(R.id.elb).setOnClickListener(clickListener);
        view.findViewById(R.id.esh).setOnClickListener(clickListener);
        view.findViewById(R.id.fig).setOnClickListener(clickListener);
        view.findViewById(R.id.flt).setOnClickListener(clickListener);
        view.findViewById(R.id.gap).setOnClickListener(clickListener);
        view.findViewById(R.id.gec).setOnClickListener(clickListener);
        view.findViewById(R.id.ger).setOnClickListener(clickListener);
        view.findViewById(R.id.gfs).setOnClickListener(clickListener);
        view.findViewById(R.id.har).setOnClickListener(clickListener);
        view.findViewById(R.id.her).setOnClickListener(clickListener);
        view.findViewById(R.id.hnb).setOnClickListener(clickListener);
        view.findViewById(R.id.hoh).setOnClickListener(clickListener);
        view.findViewById(R.id.hsh).setOnClickListener(clickListener);
        view.findViewById(R.id.ift).setOnClickListener(clickListener);
        view.findViewById(R.id.iyh).setOnClickListener(clickListener);
        view.findViewById(R.id.jef).setOnClickListener(clickListener);
        view.findViewById(R.id.jep).setOnClickListener(clickListener);
        view.findViewById(R.id.jff).setOnClickListener(clickListener);
        view.findViewById(R.id.jhh).setOnClickListener(clickListener);
        view.findViewById(R.id.jkp).setOnClickListener(clickListener);
        view.findViewById(R.id.jmc).setOnClickListener(clickListener);
        view.findViewById(R.id.jws).setOnClickListener(clickListener);
        view.findViewById(R.id.kap).setOnClickListener(clickListener);
        view.findViewById(R.id.kdc).setOnClickListener(clickListener);
        view.findViewById(R.id.koh).setOnClickListener(clickListener);
        view.findViewById(R.id.ksh).setOnClickListener(clickListener);
        view.findViewById(R.id.law).setOnClickListener(clickListener);
        view.findViewById(R.id.lhi).setOnClickListener(clickListener);
        view.findViewById(R.id.ljs).setOnClickListener(clickListener);
        view.findViewById(R.id.lrc).setOnClickListener(clickListener);
        view.findViewById(R.id.lts).setOnClickListener(clickListener);
        view.findViewById(R.id.lvl).setOnClickListener(clickListener);
        view.findViewById(R.id.mar).setOnClickListener(clickListener);
        view.findViewById(R.id.mcb).setOnClickListener(clickListener);
        view.findViewById(R.id.mcc).setOnClickListener(clickListener);
        view.findViewById(R.id.mhc).setOnClickListener(clickListener);
        view.findViewById(R.id.mhp).setOnClickListener(clickListener);
        view.findViewById(R.id.mrc).setOnClickListener(clickListener);
        view.findViewById(R.id.mrf).setOnClickListener(clickListener);
        view.findViewById(R.id.mtx).setOnClickListener(clickListener);
        view.findViewById(R.id.mus).setOnClickListener(clickListener);
        view.findViewById(R.id.nbc).setOnClickListener(clickListener);
        view.findViewById(R.id.nct).setOnClickListener(clickListener);
        view.findViewById(R.id.nrc).setOnClickListener(clickListener);
        view.findViewById(R.id.ohe).setOnClickListener(clickListener);
        view.findViewById(R.id.ois).setOnClickListener(clickListener);
        view.findViewById(R.id.pce).setOnClickListener(clickListener);
        view.findViewById(R.id.ped).setOnClickListener(clickListener);
        view.findViewById(R.id.phe).setOnClickListener(clickListener);
        view.findViewById(R.id.pks).setOnClickListener(clickListener);
        view.findViewById(R.id.prb).setOnClickListener(clickListener);
        view.findViewById(R.id.ptd).setOnClickListener(clickListener);
        view.findViewById(R.id.rgl).setOnClickListener(clickListener);
        view.findViewById(R.id.rhm).setOnClickListener(clickListener);
        view.findViewById(R.id.rrb).setOnClickListener(clickListener);
        view.findViewById(R.id.rri).setOnClickListener(clickListener);
        view.findViewById(R.id.rth).setOnClickListener(clickListener);
        view.findViewById(R.id.rzc).setOnClickListener(clickListener);
        view.findViewById(R.id.sal).setOnClickListener(clickListener);
        view.findViewById(R.id.sca).setOnClickListener(clickListener);
        view.findViewById(R.id.scb).setOnClickListener(clickListener);
        view.findViewById(R.id.scc).setOnClickListener(clickListener);
        view.findViewById(R.id.scd).setOnClickListener(clickListener);
        view.findViewById(R.id.sce).setOnClickListener(clickListener);
        view.findViewById(R.id.sci).setOnClickListener(clickListener);
        view.findViewById(R.id.scs).setOnClickListener(clickListener);
        view.findViewById(R.id.scx).setOnClickListener(clickListener);
        view.findViewById(R.id.sgm).setOnClickListener(clickListener);
        view.findViewById(R.id.shr).setOnClickListener(clickListener);
        view.findViewById(R.id.sks).setOnClickListener(clickListener);
        view.findViewById(R.id.slh).setOnClickListener(clickListener);
        view.findViewById(R.id.sos).setOnClickListener(clickListener);
        view.findViewById(R.id.ssc).setOnClickListener(clickListener);
        view.findViewById(R.id.sto).setOnClickListener(clickListener);
        view.findViewById(R.id.stu).setOnClickListener(clickListener);
        view.findViewById(R.id.swc).setOnClickListener(clickListener);
        view.findViewById(R.id.tcc).setOnClickListener(clickListener);
        view.findViewById(R.id.tgf).setOnClickListener(clickListener);
        view.findViewById(R.id.thh).setOnClickListener(clickListener);
        view.findViewById(R.id.tmc).setOnClickListener(clickListener);
        view.findViewById(R.id.trh).setOnClickListener(clickListener);
        view.findViewById(R.id.tro).setOnClickListener(clickListener);
        view.findViewById(R.id.ttl).setOnClickListener(clickListener);
        view.findViewById(R.id.uac).setOnClickListener(clickListener);
        view.findViewById(R.id.ugb).setOnClickListener(clickListener);
        view.findViewById(R.id.ugw).setOnClickListener(clickListener);
        view.findViewById(R.id.urc).setOnClickListener(clickListener);
        view.findViewById(R.id.uuc).setOnClickListener(clickListener);
        view.findViewById(R.id.vhe).setOnClickListener(clickListener);
        view.findViewById(R.id.vpd).setOnClickListener(clickListener);
        view.findViewById(R.id.wah).setOnClickListener(clickListener);
        view.findViewById(R.id.wph).setOnClickListener(clickListener);
        view.findViewById(R.id.wto).setOnClickListener(clickListener);
        view.findViewById(R.id.zhs).setOnClickListener(clickListener);
    }
}