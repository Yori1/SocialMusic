package Models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socialmusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHolder> {

    Context mContext;
    List<CardItem> mData;

    public CardAdapter(Context mContext, List<CardItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    public void clearList()
    {
        mData.clear();
    }

    public void addToList(CardItem cardItem)
    {
        mData.add(cardItem);
        notifyDataSetChanged();
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
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePhoto,background_img;
        TextView textViewSongName;
        TextView textViewContent;
        Button buttonFollow;
        Button buttonSpotify;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePhoto = itemView.findViewById(R.id.profile_img);
            background_img = itemView.findViewById(R.id.card_background);
            textViewSongName = itemView.findViewById(R.id.textViewSongName);
            textViewContent = itemView.findViewById(R.id.textViewDetails);
            buttonFollow = itemView.findViewById(R.id.buttonFollow);
            buttonSpotify = itemView.findViewById(R.id.buttonSpotify);
        }
    }
}
