package com.example.itemorganizer.HomePage;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.itemorganizer.AccountLogin;
import com.example.itemorganizer.BackendItem;
import com.example.itemorganizer.BackendReq;
import com.example.itemorganizer.R;
import com.example.itemorganizer.UserSingleton;
import com.example.itemorganizer.UtilityFunctions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView name;

    private final static String USERURL = "user/info/";

    private final static String TAG = "HomePage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.draw_layout);

        //Set navigation button listeners.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        name = v.findViewById(R.id.nav_user_name);

        //Action Bar (Navbar) Init
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ItemFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_items);
        }

        //Set Firebase Authentication Token
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Log_out();
                }
            }
        };

        //set user name
        if(UserSingleton.getInstance().getName() == null){
            setUserInfo();
        }
        else {
            name.setText(UserSingleton.getInstance().getName());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //Begin Authentication listener
        mAuth.addAuthStateListener(mAuthListener);
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


            //Log Out
            case R.id.nav_logout:
                mAuth.signOut();
                break;

        }
        //Close the Navbar.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //    Code for the menu toggle button on the top left
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void Log_out() {
        //Log out from
        UtilityFunctions.clearUser();
        Intent intent = new Intent(this, AccountLogin.class);
        startActivity(intent);
    }

    private void setUserInfo(){
        BackendItem item = new BackendItem(UserSingleton.IP + USERURL, BackendItem.GET);
        item.setHeaders(new HashMap<String, String>());

        item = BackendReq.send_req(item);

        try{
            JSONObject object = new JSONObject(item.getResponse());

            //display user name
            String userName = object.getString("name");
            UserSingleton.getInstance().setName(userName);
            name.setText(userName);
            UserSingleton.getInstance().setFamilyToken(object.getString("currentfamily"));
        } catch (JSONException e){
            Log.e(TAG, "setUserInfo: ",e);
        }


    }
}
