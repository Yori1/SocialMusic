package Models;

import android.net.Uri;

public class AppUser {
    String displayName;
    String description;
    String image;

    public AppUser() {}

    public AppUser(String displayName, String description, String image) {
        this.displayName = displayName;
        this.description = description;
        this.image = image;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
