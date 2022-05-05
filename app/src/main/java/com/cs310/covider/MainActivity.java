package com.cs310.covider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cs310.covider.fragment.*;
import com.cs310.covider.model.Pair;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    public void updateDeviceToken() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    FirebaseFirestore.getInstance().collection("DeviceTokens").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(new Pair("id", s)).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            openDialog(e.getMessage());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Util.getCurrentUserTask().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    User user = documentSnapshot.toObject(User.class);
                                    if (user.getUserCoursesIDs() != null) {
                                        for (String courseID : user.getUserCoursesIDs()) {
                                            FirebaseMessaging.getInstance().subscribeToTopic(Base64.getEncoder().encodeToString(courseID.getBytes(StandardCharsets.UTF_8)));
                                        }
                                    }
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
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    openDialog(e.getMessage());
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(v -> onBackPressed());
                } else {
                    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
                    actionBarDrawerToggle.syncState();
                    toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
                }
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateDeviceToken();
            changeToAuthedMenu();
        } else {
            changeToUnauthedMenu();
        }
    }

    public void changeToAuthedMenu() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_map_item), MapsFragment.class);
        FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if (user.getUserCoursesIDs() != null) {
                    for (String courseID : user.getUserCoursesIDs()) {
                        FirebaseMessaging.getInstance().subscribeToTopic(Base64.getEncoder().encodeToString(courseID.getBytes(StandardCharsets.UTF_8)).replace("=", "")).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                int i = 0;
                            }
                        });
                    }
                }
                if (!Util.userDidTodayCheck(user)) {
                    openDialog("You have not completed today's check form!");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                openDialog(e.getMessage());
            }
        });
    }

    public void changeToUnauthedMenu() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.auth_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_login_item), LoginFragment.class);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            return onNavigationItemSelectedLoggedIn(item);
        } else {
            return onNavigationItemSelectedAuth(item);
        }
    }

    private boolean onNavigationItemSelectedLoggedIn(@NonNull @NotNull MenuItem item) {
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.menu_map_item: {
                fragmentClass = MapsFragment.class;
                break;
            }
            case R.id.menu_building_item: {
                fragmentClass = BuildingFragment.class;
                break;
            }
            case R.id.menu_courses_item: {
                fragmentClass = CoursesFragment.class;
                break;
            }
            case R.id.menu_form_item: {
                fragmentClass = FormFragment.class;
                break;
            }
            case R.id.menu_logout_item: {
                fragmentClass = LogoutFragment.class;
                break;
            }
            case R.id.menu_add_course_item: {
                fragmentClass = AddCourseFragment.class;
                break;
            }
            case R.id.menu_checkin_item: {
                fragmentClass = CheckInFormFragment.class;
                break;
            }
            case R.id.menu_my_info_item: {
                fragmentClass = MyInfoFragment.class;
            }
            default: {
                break;
            }
        }
        changeToFragment(item, fragmentClass);
        return false;
    }

    private boolean onNavigationItemSelectedAuth(@NonNull @NotNull MenuItem item) {
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.menu_login_item: {
                fragmentClass = LoginFragment.class;
                break;
            }
            case R.id.menu_register_item: {
                fragmentClass = RegisterFragment.class;
                break;
            }
            default: {
                break;
            }
        }
        changeToFragment(item, fragmentClass);
        return false;
    }

    public void changeToFragment(MenuItem item, Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).setPrimaryNavigationFragment(fragment).commit();

        // Highlight the selected item has been done by NavigationView
        navigationView.setCheckedItem(item.getItemId());
        // Set action bar title
        setTitle(item.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    public void openDialog(@NotNull @NonNull Task task) {
        openDialog(Objects.requireNonNull(task.getException()).getMessage());
    }

    public void openDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Close",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public String buildingAbrevToFullName(String buildingAbbrev) {
        int building = getResources().getIdentifier(
                buildingAbbrev + "_comp", "string", "com.cs310.covider");
        return getResources().getString(building);
    }

    public void sendNotificationToTopic(String title, String body, String courseID) {
        String topic = Base64.getEncoder().encodeToString(courseID.getBytes(StandardCharsets.UTF_8)).replace("=", "");
        RequestQueue mRequestQue = Volley.newRequestQueue(this);
        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", body);
            notificationObj.put("priority", 10);
            json.put("notification", notificationObj);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    response -> {
                    },
                    error -> {
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=");
                    return header;
                }
            };
            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }
}