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
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

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
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0193977356, -118.2870559692)).title("acb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0192222595, -118.2856826782)).title("acc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0207061768, -118.2854461670)).title("adm"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0231590271, -118.2854080200)).title("aes"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0197903, -118.2851161)).title("ahf1"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.019714, -118.2847382)).title("ahf2"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0229301453, -118.2840957642)).title("ahn"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0194320679, -118.2828750610)).title("alm"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0204849243, -118.2873535156)).title("ann"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0218887329, -118.2861480713)).title("asc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0205535889, -118.2884750366)).title("bhe"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0222930908, -118.2857131958)).title("bit"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0207290649, -118.2864761353)).title("bks"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0227050781, -118.2846527100)).title("bmh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0188636780, -118.2858276367)).title("bri"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0216178894, -118.2824554443)).title("bsr"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0190429688, -118.2763519287)).title("cal"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0222358704, -118.2836227417)).title("cas"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0195159912, -118.2758483887)).title("cdf"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0194702148, -118.2866210938)).title("cem"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0250358582, -118.2857360840)).title("cic"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0231323242, -118.2844848633)).title("clh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0213203430, -118.2839584351)).title("cpa"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0258331299, -118.2843780518)).title("crc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0223122, -118.288927)).title("ctf"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0229988098, -118.2855987549)).title("ctv"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0216026306, -118.2805328369)).title("dcc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0240211487, -118.2863388062)).title("den"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0199241638, -118.2836761475)).title("dml"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0197792053, -118.2820129395)).title("dmt"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0210941, -118.2904382)).title("dps"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0214538574, -118.2910919189)).title("drb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0225753784, -118.2900848389)).title("drc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0196037292, -118.2823486328)).title("dxm"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0197181702, -118.2900390625)).title("eeb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0194435120, -118.2779388428)).title("elb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0251426697, -118.2887954712)).title("esh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0198516846, -118.2814712524)).title("fig"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0248641968, -118.2883529663)).title("flt"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0228767395, -118.2870712280)).title("gap"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0209541321, -118.2796630859)).title("gec"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0201416016, -118.2903900146)).title("ger"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0213356018, -118.2880020142)).title("gfs"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0190162659, -118.2872085571)).title("har"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0224685669, -118.2865676880)).title("her"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0207405090, -118.2878189087)).title("hnb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0187416077, -118.2852020264)).title("hoh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0203857422, -118.2870712280)).title("hsh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0242347717, -118.2772445679)).title("ift"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0186233521, -118.2885055542)).title("iyh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0247421265, -118.2868423462)).title("jef"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0228233337, -118.2839050293)).title("jep"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0185279846, -118.2825698853)).title("jff"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0195426941, -118.2841796875)).title("jhh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0190315247, -118.2826995850)).title("jkp"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0231246948, -118.2874984741)).title("jmc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0227966309, -118.2855758667)).title("jws"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0224037170, -118.2907791138)).title("kap"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0235900879, -118.2852706909)).title("kdc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0246772766, -118.2880172729)).title("koh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0226669312, -118.2832565308)).title("ksh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0188560486, -118.2846908569)).title("law"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0196838379, -118.2877883911)).title("lhi"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0199394226, -118.2870254517)).title("ljs"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0241508484, -118.2879943848)).title("lrc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0226249695, -118.2883682251)).title("lts"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0217933655, -118.2829208374)).title("lvl"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0222816467, -118.2872772217)).title("mar"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0217933655, -118.2895736694)).title("mcb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0248641968, -118.2873229980)).title("mcc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0258674622, -118.2860260010)).title("mhc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0188293457, -118.2868652344)).title("mhp"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0244026184, -118.2845993042)).title("mrc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0223083496, -118.2824020386)).title("mrf"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0231056213, -118.2906112671)).title("mtx"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0227699280, -118.2849426270)).title("mus"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0249710083, -118.2841720581)).title("nbc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0220947266, -118.2849044800)).title("nct"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0212821960, -118.2811431885)).title("nrc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0208396912, -118.2894287109)).title("ohe"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0218407, -118.2835146)).title("ois"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0201110840, -118.2887649536)).title("pce"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0214538574, -118.2866516113)).title("ped"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0191955566, -118.2888107300)).title("phe"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0194778442, -118.2906646729)).title("pks"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0187454224, -118.2895507812)).title("prb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0201072693, -118.2826004028)).title("ptd"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0192832947, -118.2834854126)).title("rgl"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0226249695, -118.2851943970)).title("rhm"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0200195312, -118.2874984741)).title("rrb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0221633911, -118.2901306152)).title("rri"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0200386047, -118.2898178101)).title("rth"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0242347717, -118.2792663574)).title("rzc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0196990967, -118.2895584106)).title("sal"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0234680176, -118.2864608765)).title("sca"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0239372253, -118.2870635986)).title("scb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0235481262, -118.2870941162)).title("scc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0206871033, -118.2912139893)).title("scd"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0230712891, -118.2863693237)).title("sce"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0239906311, -118.2875289917)).title("sci"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0243339539, -118.2803649902)).title("scs"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0232429504, -118.2868041992)).title("scx"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0211029053, -118.2891769409)).title("sgm"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0235595703, -118.2810745239)).title("shr"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0199394226, -118.2861480713)).title("sks"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0196762085, -118.2874755859)).title("slh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0219497681, -118.2838134766)).title("sos"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0198173523, -118.2892227173)).title("ssc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0202713013, -118.2869110107)).title("sto"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0201454163, -118.2856216431)).title("stu"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0225028992, -118.2822189331)).title("swc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0204544067, -118.2864074707)).title("tcc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0193099976, -118.2843627930)).title("tgf"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0221900940, -118.2845382690)).title("thh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0227508545, -118.2858581543)).title("tmc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0254554749, -118.2816619873)).title("trh"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0195999146, -118.2822113037)).title("tro"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0205802917, -118.2910156250)).title("ttl"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0240783691, -118.2886123657)).title("uac"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0198440552, -118.2804794312)).title("ugb"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0225982666, -118.2798690796)).title("ugw"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0234184265, -118.2846527100)).title("urc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0231437683, -118.2842254639)).title("uuc"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0201644897, -118.2881927490)).title("vhe"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0190467834, -118.2837905884)).title("vpd"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0193023682, -118.2878646851)).title("wah"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0219688416, -118.2837295532)).title("wph"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0246047974, -118.2877655029)).title("wto"));
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.0193061829, -118.2861404419)).title("zhs"));
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

    private void showDetails(Marker marker, View view) {
        double defaultRisk = 1.5;
        String buildingAbbrev = marker.getTitle();
        @SuppressLint("InflateParams") View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.building_details, null);
        PopupWindow pop = new PopupWindow(popupView, (int) (getResources().getDisplayMetrics().widthPixels * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        int building = getResources().getIdentifier(
                buildingAbbrev + "_comp", "string", "com.cs310.covider");
        ((TextView) pop.getContentView().findViewById(R.id.building_comp)).setText(getResources().getString(building));
        RatingBar bar = pop.getContentView().findViewById(R.id.ratingBar);
        TextView tv = pop.getContentView().findViewById(R.id.req);
        TextView way = pop.getContentView().findViewById(R.id.ways);
        assert buildingAbbrev != null;
        FirebaseFirestore.getInstance().collection("Buildings").document(buildingAbbrev).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
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
                                if (user.getLastInfectionDate() != null && Util.withInTwoWeeks(user.getLastInfectionDate())) {
                                    infectedCount++;
                                } else if (user.getLastSymptomsDate() != null && Util.withInTwoWeeks(user.getLastSymptomsDate())) {
                                    symptomsCount++;
                                }
                            }
                            double risk = defaultRisk + (1.0 * infectedCount + 0.5 * symptomsCount + 0.3 * totalVisitor) / totalVisitor;
                            displayPopUp(risk, bar, tv, currBuilding, way, pop, view);
                        }
                    });
                } else {
                    displayPopUp(defaultRisk, bar, tv, currBuilding, way, pop, view);
                }
            }
        }).addOnFailureListener(e -> openDialog(e.getMessage()));
    }

    private void displayPopUp(double risk, RatingBar bar, TextView tv, Building currBuilding, TextView way, PopupWindow pop, @NonNull View view) {
        bar.setRating((float) risk);
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
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}