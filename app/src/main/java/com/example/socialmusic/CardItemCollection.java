package com.example.socialmusic;

import java.util.List;

import Models.CardItem;

public class CardItemCollection {
    private List<CardItem> cardItems;

    public CardItemCollection(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    public List<CardItem> getCardItems() {
        return cardItems;
    }
}
