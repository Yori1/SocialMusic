package com.example.socialmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import Models.AppUser;
import Models.CardItem;
import Models.Review;

public class MainActivity extends DrawerLayoutActivity {
    private int limitCardsToDisplay = 30;

    List<CardItem> recentCardItems;
    List<CardItem> followingCardItems;
    RecyclerFragmentAdapter recyclerFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        recentCardItems = new ArrayList<>();
        followingCardItems = new ArrayList<>();

        setMenuLayoutElements(R.layout.activity_main, R.id.toolbar_main, R.id.drawer_layout_main);
        setCardAdapterToReviewList();
        configureFireStoreToLoadNewReviewsIntoList();

        FloatingActionButton fab = findViewById(R.id.floatingActionButtonAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = buildIntentForActivity(ShareThoughtActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setCardAdapterToReviewList()
    {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        String userId = this.getIntent().getStringExtra("userId");

        recyclerFragmentAdapter = new RecyclerFragmentAdapter(getSupportFragmentManager(),
                MainActivity.this, recentCardItems, followingCardItems, userId);

        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(recyclerFragmentAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void configureFireStoreToLoadNewReviewsIntoList()
    {
        setupFragmentRecent();
        setupFragmentFollowing();
    }

    void setupFragmentFollowing()
    {
        String userId = getIntent().getStringExtra("userId");

        fireStore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    AppUser userFound = documentSnapshot.toObject(AppUser.class);
                    List<String> idsFollowing = userFound.getUsersFollowingIds();

                    fireStore.collection("reviews").limit(limitCardsToDisplay).orderBy("created", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            recyclerFragmentAdapter.getFragmentFollowing().getCardAdapter().clearList();
                            int count = queryDocumentSnapshots.size();
                            recyclerFragmentAdapter.getFragmentFollowing().getCardAdapter().maxSize = count;

                            queryDocumentSnapshots.forEach(reviewSnapshot -> {
                                Review review = reviewSnapshot.toObject(Review.class);
                                addCardItemToListFollowing(review, reviewSnapshot.getId(), idsFollowing);
                            });
                        }
                    });
                }

            }
        });
    }

    void setupFragmentRecent()
    {
        fireStore.collection("reviews").limit(limitCardsToDisplay).orderBy("created", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                recyclerFragmentAdapter.getFragmentRecent().getCardAdapter().clearList();
                int count = queryDocumentSnapshots.size();
                recyclerFragmentAdapter.getFragmentRecent().getCardAdapter().maxSize = count;

                queryDocumentSnapshots.forEach(reviewSnapshot -> {
                    Review review = reviewSnapshot.toObject(Review.class);
                    addCardItemToListRecent(review, reviewSnapshot.getId());
                });
            }
        });
    }

    private void addCardItemToListFollowing(Review review, String reviewId, List<String> userIdsFollowing)
    {
        fireStore.collection("users").whereArrayContains("reviewIds", reviewId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentsFound = queryDocumentSnapshots.getDocuments();
                if(documentsFound.size() > 0)
                {
                    if(userIdsFollowing.contains(documentsFound.get(0).getId()))
                    {
                        AppUser appUser = documentsFound.get(0).toObject(AppUser.class);
                        CardItem cardItem = new CardItem(documentsFound.get(0).getId(), appUser, review);
                        recyclerFragmentAdapter.getFragmentFollowing().getCardAdapter().addToList(cardItem);
                    }
                }
            }
        });
    }

    private void addCardItemToListRecent(Review review, String reviewId)
    {
        fireStore.collection("users").whereArrayContains("reviewIds", reviewId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<DocumentSnapshot> documentsFound = queryDocumentSnapshots.getDocuments();
                if(documentsFound.size() > 0)
                {
                    AppUser appUser = documentsFound.get(0).toObject(AppUser.class);
                    CardItem cardItem = new CardItem(documentsFound.get(0).getId(), appUser, review);
                    recyclerFragmentAdapter.getFragmentRecent().getCardAdapter().addToList(cardItem);
                }
            }
        });
    }
}
