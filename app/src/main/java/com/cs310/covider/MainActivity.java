package com.cs310.covider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cs310.covider.fragment.AddCourseFragment;
import com.cs310.covider.fragment.BuildingFragment;
import com.cs310.covider.fragment.CoursesFragment;
import com.cs310.covider.fragment.FormFragment;
import com.cs310.covider.fragment.LoginFragment;
import com.cs310.covider.fragment.LogoutFragment;
import com.cs310.covider.fragment.MapsFragment;
import com.cs310.covider.fragment.RegisterFragment;
import com.cs310.covider.model.User;
import com.cs310.covider.model.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public Toolbar toolbar;
    public DrawerLayout drawerLayout;
    public NavigationView navigationView;

    private void dummy() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dummy();
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
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();
                        }
                    });
                } else {
                    //show hamburger
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    actionBarDrawerToggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    });
                }
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            changeToAuthedMenu();
        } else {
            changeToUnauthedMenu();
        }
    }

    public void changeToAuthedMenu() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_building_item), BuildingFragment.class);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                if(user.getLastCheckDate() == null || !Util.sameDay(new Date(), user.getLastCheckDate()))
                {
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
            return onNavigationItemSelectedLoggedin(item);
        } else {
            return onNavigationItemSelectedAuth(item);
        }
    }

    private boolean onNavigationItemSelectedLoggedin(@NonNull @NotNull MenuItem item) {
        Class fragmentClass = null;
        switch (item.getItemId()) {
            case R.id.menu_building_item: {
                fragmentClass = BuildingFragment.class;
                break;
            }
            case R.id.menu_map_item: {
                fragmentClass = MapsFragment.class;
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
}