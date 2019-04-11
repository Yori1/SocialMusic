package com.example.socialmusic;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import Models.Review;
import Models.ReviewId;

public class ShareThoughtActivity extends DrawerLayoutActivity {
    private Button submit;
    private EditText editTextSongName;
    private EditText editTextContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_thought);
        setMenuLayoutElements(R.layout.activity_share_thought, R.id.toolbar_profile, R.id.drawer_layout_share_thought);

        submit = this.findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(this::HandleSubmitButtonClick);
        editTextContent = this.findViewById(R.id.editTextContent);
        editTextSongName = this.findViewById(R.id.editTextSongName);
    }

    private void HandleSubmitButtonClick(View view)
    {
        String userId = getIntent().getStringExtra("userId");
        Review review = new Review(editTextSongName.getText().toString(), editTextContent.getText().toString());
        fireStore.collection("reviews").add(review).addOnSuccessListener(addedReview -> {
            fireStore.collection("users").document(userId).update("reviewIds", FieldValue.arrayUnion(addedReview.getId()));
        });

        Intent intentMain = buildIntentForActivity(MainActivity.class);
        startActivity(intentMain);
    }
}
