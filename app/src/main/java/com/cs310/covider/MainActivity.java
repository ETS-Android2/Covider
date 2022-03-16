package com.cs310.covider;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.fragment.NavHostFragment;
import com.cs310.covider.database.Database;
import com.cs310.covider.fragment.*;
import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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
        if(Database.getCurrentUser() != null) {
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_map_item), MapFragment.class);
        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.auth_menu);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            assert navHostFragment != null;
            navHostFragment.getNavController().setGraph(R.navigation.auth_nav_graph);
            changeToFragment(navigationView.getMenu().findItem(R.id.menu_login_item), LoginFragment.class);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(Database.getCurrentUser() != null)
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
            case R.id.menu_map_item: {
                fragmentClass = MapFragment.class;
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

    private void changeToFragment(MenuItem item, Class fragmentClass)
    {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        assert fragment != null;
        fragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit();

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