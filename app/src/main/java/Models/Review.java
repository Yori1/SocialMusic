package Models;

public class Review {
    private String songName;
    private String content;

    public Review(String songName, String content) {
        this.songName = songName;
        this.content = content;
    }

    public String getSongName() {
        return songName;
    }

    public String getContent() {
        return content;
    }
}
