package com.cs310.covider;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_building_item), BuildingFragment.class);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.auth_menu);
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_login_item), LoginFragment.class);
        }
    }

    public void changeToAuthedMenu() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);
        changeToFragment(navigationView.getMenu().findItem(R.id.menu_building_item), BuildingFragment.class);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}