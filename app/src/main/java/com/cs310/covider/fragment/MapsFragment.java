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

import com.cs310.covider.R;
import com.cs310.covider.model.Building;
import com.cs310.covider.model.Course;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

public class MapsFragment extends MyFragment {

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng usc = new LatLng(34.022415, -118.285530);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(usc, 17));

            FirebaseFirestore.getInstance().collection("Buildings").get().addOnSuccessListener(queryDocumentSnapshots -> {
                ArrayList<Building> buildingArrayList = new ArrayList<>(queryDocumentSnapshots.toObjects(Building.class));
                HashMap<String, Building> buildingHashMap = new HashMap<>();
                for (Building building : buildingArrayList)
                    buildingHashMap.put(building.getName(), building);
                ArrayList<Building> scheduleBuildings = new ArrayList<>(), frequentBuildings = new ArrayList<>();
                Objects.requireNonNull(Util.getCurrentUserTask()).addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);
                    ArrayList<Task> courseTask = new ArrayList<>();
                    assert user != null;
                    if (user.getUserCoursesIDs() != null && !user.getUserCoursesIDs().isEmpty()) {
                        for (String id : user.getUserCoursesIDs())
                            courseTask.add(FirebaseFirestore.getInstance().collection("Courses").document(id).get());
                        Tasks.whenAllComplete(courseTask.toArray(new Task[0]))
                                .addOnCompleteListener(
                                        task -> {
                                            if (!task.isSuccessful()) {
                                                openDialog(task);
                                                return;
                                            }
                                            ArrayList<Course> enrolledCourses = new ArrayList<>();
                                            for (Task t : task.getResult())
                                                enrolledCourses.add(((DocumentSnapshot) t.getResult()).toObject(Course.class));
                                            for (Course course : enrolledCourses)
                                                if (buildingHashMap.containsKey(course.getBuildingName())) {
                                                    scheduleBuildings.add(buildingHashMap.get(course.getBuildingName()));
                                                    buildingHashMap.remove(course.getBuildingName());
                                                }
                                            addMarkers(user, buildingHashMap, frequentBuildings, scheduleBuildings, googleMap);
                                        }
                                );
                    } else
                        addMarkers(user, buildingHashMap, frequentBuildings, scheduleBuildings, googleMap);
                }).addOnFailureListener(e -> openDialog(e.getMessage()));
            }).addOnFailureListener(e -> openDialog(e.getMessage()));
            LatLngBounds campusBounds = new LatLngBounds(
                    new LatLng(34.018469, -118.291199), // SW bounds
                    new LatLng(34.026839, -118.276909)  // NE bounds
            );
            googleMap.setLatLngBoundsForCameraTarget(campusBounds);
            googleMap.setOnMarkerClickListener(marker -> {
                showDetails(marker, getView());
                return true;
            });
        }
    };

    private void addMarkers(User user, HashMap<String, Building> buildingHashMap, ArrayList<Building> frequentBuildings, ArrayList<Building> scheduleBuildings, GoogleMap googleMap) {
        HashSet<String> frequentlyVisitedBuildingAbbrevs = new HashSet<>();
        if (user.getBuildingCheckedinTimes() != null && !user.getBuildingCheckedinTimes().isEmpty())
            for (Map.Entry<String, Integer> entry : user.getBuildingCheckedinTimes().entrySet())
                if (entry.getValue() > 1 && user.getLastCheckDate()!= null && Util.withInTwoWeeks(user.getLastCheckDate()))
                    frequentlyVisitedBuildingAbbrevs.add(String.valueOf(entry.getKey()));

        for (String name : frequentlyVisitedBuildingAbbrevs)
            if (buildingHashMap.containsKey(name)) {
                frequentBuildings.add(buildingHashMap.get(name));
                buildingHashMap.remove(name);
            }

        for (Building b : scheduleBuildings)
            googleMap.addMarker(new MarkerOptions().position(new LatLng(b.getLocation().getLatitude(), b.getLocation().getLongitude())).title(b.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).alpha((float) .7));

        for (Building b : frequentBuildings)
            googleMap.addMarker(new MarkerOptions().position(new LatLng(b.getLocation().getLatitude(), b.getLocation().getLongitude())).title(b.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).alpha((float) .7));

        for (Map.Entry<String, Building> entry : buildingHashMap.entrySet()) {
            Building b = entry.getValue();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(b.getLocation().getLatitude(), b.getLocation().getLongitude())).title(b.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).alpha((float) .7));
        }
    }

    private void showDetails(Marker marker, View view) {
        float defaultRisk = 1.5F;
        String buildingAbbrev = marker.getTitle();
        @SuppressLint("InflateParams") View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.building_details, null);
        PopupWindow pop = new PopupWindow(popupView, (int) (getResources().getDisplayMetrics().widthPixels * 0.93), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        int building = getResources().getIdentifier(
                buildingAbbrev + "_comp", "string", "com.cs310.covider");
        ((TextView) pop.getContentView().findViewById(R.id.building_comp)).setText(getResources().getString(building));
        RatingBar bar = pop.getContentView().findViewById(R.id.ratingBar);
        TextView tv = pop.getContentView().findViewById(R.id.req);
        TextView way = pop.getContentView().findViewById(R.id.ways);
        assert buildingAbbrev != null;
        FirebaseFirestore.getInstance().collection("Buildings").document(buildingAbbrev).get().addOnSuccessListener(documentSnapshot -> {
            Building currBuilding = documentSnapshot.toObject(Building.class);
            ArrayList<Task> checkedInUserTasks = new ArrayList<>();
            assert currBuilding != null;
            if (Util.buildingCheckinDataValidForToday(currBuilding)) {
                for (String checkedInEmail : currBuilding.getCheckedInUserEmails()) {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(callback);
    }
}