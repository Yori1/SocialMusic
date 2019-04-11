package com.example.socialmusic;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Models.CardItem;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHolder> {
    Context mContext;
    List<CardItem> mData;
    String userId;

    public int maxSize;

    public CardAdapter(Context mContext, List<CardItem> mData, String userId) {
        this.mContext = mContext;
        this.mData = mData;
        this.userId = userId;
    }

    public void clearList()
    {
        mData.clear();
    }

    public void addToList(CardItem cardItem)
    {
        if(maxSize != mData.size())
        {
            mData.add(cardItem);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.card_item, viewGroup, false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        Picasso.get().load(mData.get(i).getAppUser().getImage()).into(myViewHolder.profilePhoto);
        myViewHolder.textViewSongName.setText(mData.get(i).getReview().getSongName());
        myViewHolder.textViewContent.setText(mData.get(i).getReview().getContent());

        myViewHolder.profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, OtherUserProfileActivity.class);
                intent.putExtra("ownerProfilePageId", mData.get(i).getUserFirebaseId());
                intent.putExtra("userId" ,userId);
                mContext.startActivity(intent);
            }
        });


        String songName = mData.get(i).getReview().getSongName();
        myViewHolder.buttonSpotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
                intent.setComponent(new ComponentName("com.spotify.music", "com.spotify.music.MainActivity"));
                intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS, MediaStore.Audio.Media.ENTRY_CONTENT_TYPE);
                intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST, songName);
                intent.putExtra(SearchManager.QUERY, songName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePhoto,background_img;
        TextView textViewSongName;
        TextView textViewContent;
        ImageButton buttonSpotify;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePhoto = itemView.findViewById(R.id.profile_img);
            background_img = itemView.findViewById(R.id.card_background);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
            textViewContent = itemView.findViewById(R.id.textViewDetails);
            buttonSpotify = itemView.findViewById(R.id.buttonSpotify);
        }
    }
}
