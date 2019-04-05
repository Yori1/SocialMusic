package com.example.socialmusic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import Models.AppUser;
import Models.CardItem;
import Models.Review;

public class OtherUserProfileActivity extends DrawerLayoutActivity {

    private ArrayList<CardItem> cardItems;
    private CardAdapter cardAdapter;
    private String ownerProfilePageFirestoreId;
    private Button buttonFollow;

    boolean following;
    String followString = "Follow";
    String stopFollowingString = "stopFollowing";
    ListenerRegistration registrationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMenuLayoutElements(R.layout.activity_other_user_profile, R.id.toolbar_profile_other, R.id.drawer_layout_profile_other);
        buttonFollow = findViewById(R.id.buttonFollow);
        ownerProfilePageFirestoreId = getIntent().getStringExtra("userId");

        cardItems = new ArrayList<>();
        loadGeneralUserInformation();
        setCardAdapterToReviewList();
        configureFireStoreToLoadNewReviewsIntoList();

        setOnChangeUsersFollowedUsers();
        setFollowButtonFireBaseFunctionality();

    }

    private void setOnChangeUsersFollowedUsers()
    {
        String currentUsersUserId = getIntent().getStringExtra("userId");
        fireStore.collection("users").document(currentUsersUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                AppUser appUser = documentSnapshot.toObject(AppUser.class);
                if(appUser.getUsersFollowingIds() == null ||
                        appUser.getUsersFollowingIds().stream().noneMatch(userId -> userId == ownerProfilePageFirestoreId))
                {
                    following = false;
                    buttonFollow.setText(followString);
                }
                else
                {
                    following = true;
                    buttonFollow.setText(stopFollowingString);
                }
            }
        });
    }

    private void setFollowButtonFireBaseFunctionality()
    {
        Button followButton = findViewById(R.id.buttonFollow);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUsersUserId = getIntent().getStringExtra("userId");

                DocumentReference currentUserDocument = fireStore.collection("users").document(currentUsersUserId);
                 registrationListener = currentUserDocument.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                AppUser appUser = documentSnapshot.toObject(AppUser.class);
                                if(appUser.getUsersFollowingIds() == null ||
                                        appUser.getUsersFollowingIds().stream().noneMatch(userId -> userId == ownerProfilePageFirestoreId))
                                {
                                    currentUserDocument.update("usersFollowingIds", FieldValue.arrayUnion(ownerProfilePageFirestoreId))
                                            .onSuccessTask(new SuccessContinuation<Void, Object>() {
                                                @NonNull
                                                @Override
                                                public Task<Object> then(@android.support.annotation.Nullable Void aVoid) {
                                                    registrationListener.remove();
                                                    return null;
                                                }
                                            });
                                }

                                else
                                {
                                    currentUserDocument.update("usersFollowingIds", FieldValue.arrayRemove(ownerProfilePageFirestoreId))
                                            .onSuccessTask(new SuccessContinuation<Void, Object>() {
                                                @NonNull
                                                @Override
                                                public Task<Object> then(@android.support.annotation.Nullable Void aVoid) {
                                                    registrationListener.remove();
                                                    return null;
                                                }
                                            });

                                }
                            }
                        });
            }
        });
    }

    private void loadGeneralUserInformation()
    {
        fireStore.collection("users").document(ownerProfilePageFirestoreId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e!=null)
                    System.out.println(e.getMessage());
                ImageView profileImage = findViewById(R.id.imageViewProfilePictureOtherUser);
                Picasso.get().load(documentSnapshot.get("image").toString()).into(profileImage);

                TextView textViewDisplayName = findViewById(R.id.textViewNameOtherUser);
                textViewDisplayName.setText(documentSnapshot.get("displayName").toString());
            }
        });
    }

    private void setCardAdapterToReviewList()
    {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RecyclerView recyclerView = findViewById(R.id.cardList_profile_other);
        cardItems = new ArrayList<>();

        cardAdapter = new CardAdapter(this, cardItems);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureFireStoreToLoadNewReviewsIntoList()
    {
        fireStore.collection("reviews").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                cardAdapter.clearList();
                queryDocumentSnapshots.forEach(reviewSnapshot -> {
                    Review review = reviewSnapshot.toObject(Review.class);
                    addCardItemToListIfFromUser(review, reviewSnapshot.getId());
                });
            }
        });
    }

    private void addCardItemToListIfFromUser(Review review, String reviewId)
    {
        fireStore.collection("users").whereArrayContains("reviewIds", reviewId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentsFound = queryDocumentSnapshots.getDocuments();
                if(documentsFound.size() > 0)
                {
                    String foundUserId = documentsFound.get(0).getId();
                    if(foundUserId.equals(ownerProfilePageFirestoreId)) {
                        AppUser appUser = documentsFound.get(0).toObject(AppUser.class);
                        CardItem cardItem = new CardItem(documentsFound.get(0).getId(), appUser, review);
                        cardAdapter.addToList(cardItem);
                    }
                }
            }
        });
    }
}
