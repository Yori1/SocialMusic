package Models;

public class CardItem {
    private String userFirebaseId;
    private AppUser appUser;
    private Review review;

    public CardItem(String userFirebaseId, AppUser appUser, Review review) {
        this.userFirebaseId = userFirebaseId;
        this.appUser = appUser;
        this.review = review;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Review getReview() {
        return review;
    }

    public String getUserFirebaseId() {
        return userFirebaseId;
    }
}
