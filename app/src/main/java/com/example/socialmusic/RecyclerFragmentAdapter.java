package com.example.socialmusic;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import Models.CardItem;

public class RecyclerFragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "All", "Following" };
    private Context context;
    private List<CardItem> cardItemsRecent;
    private List<CardItem> cardItemsFollowing;

    private RecyclerViewFragment fragmentRecent;
    private RecyclerViewFragment fragmentFollowing;

    public RecyclerFragmentAdapter(FragmentManager fm, Context context, List<CardItem> cardItemsRecent, List<CardItem> cardItemsFollowing) {
        super(fm);
        this.context = context;
        this.cardItemsRecent = cardItemsRecent;
        this.cardItemsFollowing = cardItemsFollowing;

        this.fragmentRecent = RecyclerViewFragment.newInstance(cardItemsRecent, R.layout.fragement_recyclerview_home, R.id.recyclerView);
        this.fragmentFollowing = RecyclerViewFragment.newInstance(cardItemsFollowing, R.layout.fragment_recyclerview_following, R.id.reyclerViewFollowing);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragmentToReturn;
        switch (position)
        {
            case 0:
                fragmentToReturn = fragmentRecent;
                break;

            case 1:
                fragmentToReturn = fragmentFollowing;
                break;

            default:
                fragmentToReturn = null;
        }
        return fragmentToReturn;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public RecyclerViewFragment getFragmentRecent() {
        return fragmentRecent;
    }

    public RecyclerViewFragment getFragmentFollowing() {
        return fragmentFollowing;
    }
}
