import java.io.Serializable;

public class PlayerData implements Serializable {
    private String username;
    private int score;

    public PlayerData(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() { return username; }
    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }
}
