package com.example.itemorganizer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.draw_layout);

//      Set navigation button listeners.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ItemFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_items);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) //Check which items are clicked.
        {
            //Account.
            case R.id.nav_account:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;

            //Family
            case R.id.nav_family:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FamilyFragment()).commit();
                break;

            //Items
            case R.id.nav_items:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ItemFragment()).commit();
                break;

            //Settings
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;

            //Log Out
            case R.id.nav_logout:
                //Log Out Code//
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //    Code for the menu toggle button on the top left
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
