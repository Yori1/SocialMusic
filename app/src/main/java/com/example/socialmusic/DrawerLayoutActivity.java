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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public abstract class DrawerLayoutActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout = null;

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
