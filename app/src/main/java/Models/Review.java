package Models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Review {
    private String songName;
    private String content;
    @ServerTimestamp
    Date created;

    public Review () {}

    public Review(String songName, String content) {
        this.songName = songName;
        this.content = content;
    }

    public Review(String songName, String content, Date created) {
        this.songName = songName;
        this.content = content;
        this.created = created;
    }

    public String getSongName() {
        return songName;
    }

    public String getContent() {
        return content;
    }

    public Date getCreated() {
        return created;
    }
}
