package com.cs310.covider;
import android.location.Location;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cs310.covider.fragment.*;
import com.cs310.covider.model.Building;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

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
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_building_item), BuildingFragment.class);
        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.auth_menu);
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_login_item), LoginFragment.class);
        }
    }

    public void changeToAuthedMenu()
    {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_building_item), BuildingFragment.class);
    }

    public void changeToUnauthedMenu()
    {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.auth_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_login_item), LoginFragment.class);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            return onNavigationItemSelectedLoggedin(item);
        }
        else
        {
            return onNavigationItemSelectedAuth(item);
        }
    }

    private boolean onNavigationItemSelectedLoggedin(@NonNull @NotNull MenuItem item)
    {
        Class fragmentClass = null;
        switch (item.getItemId())
        {
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
            case R.id.menu_logout_item:{
                fragmentClass = LogoutFragment.class;
                break;
            }
            case R.id.menu_add_course_item:{
                fragmentClass = AddCourseFragment.class;
            }
            default:{
                break;
            }
        }
        changeToFragment(item,fragmentClass);
        return false;
    }

    private boolean onNavigationItemSelectedAuth(@NonNull @NotNull MenuItem item)
    {
        Class fragmentClass = null;
        switch (item.getItemId())
        {
            case R.id.menu_login_item: {
                fragmentClass = LoginFragment.class;
                break;
            }
            case R.id.menu_register_item: {
                fragmentClass = RegisterFragment.class;
                break;
            }
            default:{
                break;
            }
        }
        changeToFragment(item,fragmentClass);
        return false;
    }

    public void changeToFragment(MenuItem item, Class fragmentClass)
    {
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}