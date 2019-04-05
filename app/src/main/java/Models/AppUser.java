package Models;

import java.util.ArrayList;
import java.util.List;

public class AppUser {
    String displayName;
    String description;
    String image;
    List<String> usersFollowingIds;
    List<String> reviewIds;

    public AppUser() {}

    public AppUser(String displayName, String description, String image) {
        this.displayName = displayName;
        this.description = description;
        this.image = image;
    }

    public AppUser(String displayName, String description, String image, ArrayList<String> usersFollowingIds, ArrayList<String> reviewIds) {
        this.displayName = displayName;
        this.description = description;
        this.image = image;
        this.usersFollowingIds = usersFollowingIds;
        this.reviewIds = reviewIds;
    }

    public List<String> getUsersFollowingIds() {
        return usersFollowingIds;
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
