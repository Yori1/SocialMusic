package com.example.socialmusic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import Models.AppUser;
import Models.CardAdapter;
import Models.CardItem;
import Models.Review;

public class MainActivity extends DrawerLayoutActivity {
    private final long limitCardsToDisplay = 30;

    List<CardItem> itemList;
    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        //This method sets the views in the abstract class DrawerLayoutActivity
        setMenuLayoutElements(R.layout.activity_main, R.id.toolbar_main, R.id.drawer_layout_main);
        setCardAdapterToReviewList();
        configureFireStoreToLoadNewReviewsIntoList();

    }

    private void setCardAdapterToReviewList()
    {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RecyclerView recyclerView = findViewById(R.id.cardList_RV);
        itemList = new ArrayList<>();
        itemList.add(new CardItem(new AppUser("a", "a", "a"), new Review("a","a")));

        cardAdapter = new CardAdapter(this, itemList);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void configureFireStoreToLoadNewReviewsIntoList()
    {
        fireStore.collection("reviews").limit(limitCardsToDisplay).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                itemList.clear();
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
                    cardAdapter.addToList(new CardItem(appUser, review));

                }
            }
        });
    }
}
