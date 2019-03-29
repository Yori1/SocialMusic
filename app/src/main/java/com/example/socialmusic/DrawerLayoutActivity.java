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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public abstract class DrawerLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout = null;

    protected FirebaseFirestore fireStore;


    private Intent intent;

    private boolean displayNameBeingSet;

    private TextView displayTextTextView;
    private ImageView imageView;

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

        imageView = headerView.findViewById(R.id.imageView);

        FirebaseApp.initializeApp(this);
        fireStore = FirebaseFirestore.getInstance();

        intent = getIntent();

        setUserInformation();
    }

    void setUserInformation()
    {
        String userId = intent.getStringExtra("userId");
        String userGoogleDisplayName = intent.getStringExtra("googleDisplayName");

        DocumentReference userReference = fireStore.collection("users").document(userId);
        userReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String displayName = documentSnapshot.getString("displayName");
                String imageUrl = documentSnapshot.getString("image");
                displayTextTextView.setText(displayName);
                Picasso.get().load(imageUrl).into(imageView);
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
