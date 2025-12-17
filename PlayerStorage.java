import java.io.*;
import java.util.*;

public class PlayerStorage {

    private static final String FILE_NAME = "players.dat";

    // Load all players
    @SuppressWarnings("unchecked")
    public static List<PlayerData> loadPlayers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<PlayerData>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // empty list if file not found
        }
    }

    // Save all players
    public static void savePlayers(List<PlayerData> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get player by username
    public static PlayerData getPlayer(String username) {
        for (PlayerData p : loadPlayers()) {
            if (p.getUsername().equals(username)) return p;
        }
        return null;
    }

    // Add or update player
    public static void addOrUpdatePlayer(PlayerData player) {
        List<PlayerData> players = loadPlayers();
        boolean found = false;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUsername().equals(player.getUsername())) {
                players.set(i, player); // update
                found = true;
                break;
            }
        }
        if (!found) players.add(player); // add new
        savePlayers(players);
    }

    // Get top N players for leaderboard
    public static List<PlayerData> getTopPlayers(int n) {
        List<PlayerData> players = loadPlayers();
        players.sort((a, b) -> b.getScore() - a.getScore());
        return players.size() > n ? players.subList(0, n) : players;
    }
}
