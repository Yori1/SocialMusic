package com.example.socialmusic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import Models.CardItem;

public class RecyclerViewFragment extends Fragment {
    List<CardItem> cardItemList;
    Context context;

    int idLayoutFragment;
    int idRecyclerViewToUse;
    String userId;

    private CardAdapter cardAdapter;

    public static RecyclerViewFragment newInstance(List<CardItem> cardItems, @LayoutRes int idLayoutFragment, int idRecyclerViewToUse, String userId) {
        Bundle args = new Bundle();
        CardItemCollection cardItemCollection = new CardItemCollection(cardItems);
        Gson gson = new Gson();
        args.putString("cardItems", gson.toJson(cardItemCollection));
        args.putInt("idLayoutFragment", idLayoutFragment);
        args.putInt("idRecyclerViewToUse", idRecyclerViewToUse);
        args.putString("userId", userId);
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<CardItem> cardItemCollectionJsonToList(String jsonCardItemCol)
    {
        Gson gson = new Gson();
        CardItemCollection coll = gson.fromJson(jsonCardItemCol, CardItemCollection.class);
        return coll.getCardItems();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity();
        this.idRecyclerViewToUse = getArguments().getInt("idRecyclerViewToUse");
        this.idLayoutFragment = getArguments().getInt("idLayoutFragment");
        this.userId = getArguments().getString("userId");
        String jsonCardItems = getArguments().getString("cardItems");
        cardItemList = cardItemCollectionJsonToList(jsonCardItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(idLayoutFragment, container, false);
        RecyclerView recyclerViewToSetAdapter  = view.findViewById(idRecyclerViewToUse);
        cardAdapter = new CardAdapter(context, cardItemList, userId);
        recyclerViewToSetAdapter.setAdapter(cardAdapter);
        recyclerViewToSetAdapter.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    public CardAdapter getCardAdapter() {
        return cardAdapter;
    }
}
