package com.example.socialmusic;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import Models.Adapter;
import Models.CardItem;

public class MainActivity extends DrawerLayoutActivity {

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        //This method sets the views in the abstract class DrawerLayoutActivity
        setMenuLayoutElements(R.layout.activity_main, R.id.toolbar_main, R.id.drawer_layout_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        RecyclerView recyclerView = findViewById(R.id.cardList_RV);
        List<CardItem> itemList = new ArrayList<>();
        itemList.add(new CardItem(R.drawable.background1, "Henk", R.drawable.profile_pic1));
        itemList.add(new CardItem(R.drawable.background2, "Chris", R.drawable.profile_pic2));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));
        itemList.add(new CardItem(R.drawable.background1, "Anonymous", R.drawable.profile_pic3));

        Adapter adapter = new Adapter(this, itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
