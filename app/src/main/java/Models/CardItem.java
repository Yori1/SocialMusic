package Models;

public class CardItem {
    private AppUser appUser;
    private Review review;

    public CardItem(AppUser appUser, Review review) {
        this.appUser = appUser;
        this.review = review;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Review getReview() {
        return review;
    }
}
