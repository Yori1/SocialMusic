package com.example.socialmusic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.Helpers.FirebaseHelper;

public class profile_normal extends AppCompatActivity {
    String userId;
    TextView displayNameView;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_normal);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        displayNameView = findViewById(R.id.displayNameView);

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
                displayNameView.setText(snapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                String error = databaseError.getMessage();
            }

        });

    }
}
