package Models;

public class CardItem {

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public int getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(int profilePhoto) {
        this.profilePhoto = profilePhoto;
    }



    int background;
    String profileName;
    int profilePhoto;

    public CardItem() {
    }

    public CardItem(int background, String profileName, int profilePhoto) {
        this.background = background;
        this.profileName = profileName;
        this.profilePhoto = profilePhoto;
    }

    public CardItem(String profileName, int profilePhoto) {
        this.profileName = profileName;
        this.profilePhoto = profilePhoto;
    }
}
