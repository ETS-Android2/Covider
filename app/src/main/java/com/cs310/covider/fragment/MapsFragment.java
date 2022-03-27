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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {

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
            googleMap.addMarker(new MarkerOptions().position(new LatLng(34.022415, -118.285530)).title("thh"));
            LatLngBounds campusBounds = new LatLngBounds(
                    new LatLng(34.018469, -118.291323), // SW bounds
                    new LatLng(34.025292, -118.279989)  // NE bounds
            );
            googleMap.setLatLngBoundsForCameraTarget(campusBounds);
            googleMap.setOnMarkerClickListener(marker -> {
                showDetails(marker, getView());
                return true;
            });
        }
    };

//    private void openPop() {
//        Intent popupwindow = new Intent (MapsActivity.this,PopUpWindow.class);
//        startActivity(popupwindow);
//    }

    private void showDetails(Marker marker, View view) {
        String buildingAbbrev = marker.getTitle();
        @SuppressLint("InflateParams") View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.building_details, null);
        PopupWindow pop = new PopupWindow(popupView, (int) (getResources().getDisplayMetrics().widthPixels * 0.9), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        int building = getResources().getIdentifier(
                buildingAbbrev + "_comp", "string", "com.cs310.covider");
        ((TextView) pop.getContentView().findViewById(R.id.building_comp)).setText(getResources().getString(building));
        RatingBar bar = pop.getContentView().findViewById(R.id.ratingBar);
        bar.setRating(3.5F);
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