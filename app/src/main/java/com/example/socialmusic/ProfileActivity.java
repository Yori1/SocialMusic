package com.example.socialmusic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class ProfileActivity extends DrawerLayoutActivity {

    private TextView textViewDisplayName;
    private ImageView imageViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuLayoutElements(R.layout.activity_profile_normal, R.id.toolbar_profile, R.id.drawer_layout_profile);
        setButtonThroughSharingActivityEvent();

        textViewDisplayName = findViewById(R.id.textViewNameOtherUser);
        imageViewUser = findViewById(R.id.imageViewProfilePicture);

        String userId = getIntent().getStringExtra("userId");
        fireStore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                String displayName  = documentSnapshot.get("displayName").toString();
                String urlImage = documentSnapshot.get("image").toString();

                textViewDisplayName.setText(displayName);
                Picasso.get().load(urlImage).into(imageViewUser);
            }
        });


    }

    private void setButtonThroughSharingActivityEvent(){
        Button buttonThoughtActivity = findViewById(R.id.buttonShareThoughts);

        buttonThoughtActivity.setOnClickListener(c -> {
            Intent intent = buildIntentForActivity(ShareThoughtActivity.class);
            startActivity(intent);
        });
    }
}
