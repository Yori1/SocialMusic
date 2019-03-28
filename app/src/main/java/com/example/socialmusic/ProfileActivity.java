package com.example.socialmusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends DrawerLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuLayoutElements(R.layout.activity_profile_normal, R.id.toolbar_profile, R.id.drawer_layout_profile);
        setButtonThoughSharingActivityEvent();
    }

    private void setButtonThoughSharingActivityEvent(){
        Button buttonThoughtActivity = findViewById(R.id.buttonShareThoughts);

        buttonThoughtActivity.setOnClickListener(c -> {
            Intent intent = buildIntentForActivity(ShareThoughtActivity.class);
            startActivity(intent);
        });
    }
}
