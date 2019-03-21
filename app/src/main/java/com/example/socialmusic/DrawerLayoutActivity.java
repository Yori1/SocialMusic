package com.example.socialmusic;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public abstract class DrawerLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout = null;

    protected FirebaseDatabase firebaseDatabase;

    private Intent intent;

    private boolean displayNameBeingSet;

    private TextView displayTextTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setMenuLayoutElements(int contentViewId, int toolbarId, int drawerLayoutId)
    {
        setContentView(contentViewId);
        Toolbar toolbar = (Toolbar) this.findViewById(toolbarId);
        setSupportActionBar(toolbar);

        drawerLayout = this.findViewById(drawerLayoutId);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        View headerView = navigationView.getHeaderView(0);
        displayTextTextView = headerView.findViewById(R.id.displayNameTextManu);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        intent = getIntent();

        setUserInformation();
    }

    void setUserInformation()
    {
        String userId = intent.getStringExtra("userId");
        String userGoogleDisplayName = intent.getStringExtra("googleDisplayName");


        DatabaseReference userReference = firebaseDatabase.getReference("users").child(userId);
        userReference.child("displayName").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists() && !displayNameBeingSet)
                {
                    userReference.child(userId).setValue(userGoogleDisplayName);
                    displayNameBeingSet = true;
                }
                else
                {
                    displayTextTextView.setText(snapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", databaseError.getMessage());
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_Main:
                startActivity(buildIntentForActivity(MainActivity.class));
                break;

            case R.id.nav_Profile:
                startActivity(buildIntentForActivity(ProfileActivity.class));
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    protected Intent buildIntentForActivity(Class activityClass)
    {
        Intent thisIntent = this.getIntent();
        String userId = thisIntent.getStringExtra("userId");
        String userGoogleDisplayName = thisIntent.getStringExtra("googleDisplayName");

        Intent newIntent = new Intent(this, activityClass);
        newIntent.putExtra("userId", userId);
        newIntent.putExtra("googleDisplayName", userGoogleDisplayName);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return newIntent;
    }
}
