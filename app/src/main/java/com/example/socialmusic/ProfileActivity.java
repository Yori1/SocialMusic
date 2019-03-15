package com.example.socialmusic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends DrawerLayoutActivity {
    String userId;
    String userGoogleDisplayName;

    TextView displayNameTextView;
    FirebaseDatabase firebaseDatabase;
    boolean displayNameBeingSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuLayoutElements(R.layout.activity_profile_normal, R.id.toolbar_profile, R.id.drawer_layout_profile);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userGoogleDisplayName = intent.getStringExtra("googleDisplayName");

        displayNameTextView = findViewById(R.id.displayNameView);

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        setUserInformation();
    }

    void setUserInformation()
    {
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
                    displayNameTextView.setText(snapshot.getValue(String.class));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("firebase", databaseError.getMessage());
            }

        });


    }
}
