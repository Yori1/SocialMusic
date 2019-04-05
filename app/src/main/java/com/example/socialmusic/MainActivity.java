package com.example.socialmusic;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import Models.AppUser;
import Models.CardItem;
import Models.Review;

public class MainActivity extends DrawerLayoutActivity {
    private final long limitCardsToDisplay = 30;

    List<CardItem> recentCardItems;
    List<CardItem> followingCardItems;
    RecyclerFragmentAdapter recyclerFragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);

        recentCardItems = new ArrayList<>();
        followingCardItems = new ArrayList<>();

        //This method sets the views in the abstract class DrawerLayoutActivity
        setMenuLayoutElements(R.layout.activity_main, R.id.toolbar_main, R.id.drawer_layout_main);
        setCardAdapterToReviewList();
        configureFireStoreToLoadNewReviewsIntoList();
    }

    private void setCardAdapterToReviewList()
    {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        recyclerFragmentAdapter = new RecyclerFragmentAdapter(getSupportFragmentManager(),
                MainActivity.this, recentCardItems, followingCardItems);

        ViewPager viewPager = findViewById(R.id.viewpager);

        viewPager.setAdapter(recyclerFragmentAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void configureFireStoreToLoadNewReviewsIntoList()
    {
        fireStore.collection("reviews").limit(limitCardsToDisplay).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                recyclerFragmentAdapter.getFragmentRecent().getCardAdapter().clearList();
                queryDocumentSnapshots.forEach(reviewSnapshot -> {
                    Review review = reviewSnapshot.toObject(Review.class);
                    addCardItemToList(review, reviewSnapshot.getId());
                });
            }
        });
    }

    private void addCardItemToList(Review review, String reviewId)
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
