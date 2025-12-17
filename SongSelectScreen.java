import java.awt.*;
import javax.swing.*;

public class SongSelectScreen extends JFrame {
    private PlayerData currentPlayer;

    public SongSelectScreen(PlayerData player) {
        this.currentPlayer = player;

        setTitle("Select Song");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1));
        JLabel label = new JLabel("Choose a Song + Difficulty", JLabel.CENTER);
        panel.add(label);

        // Song selection
        String[] songs = {"Song A", "Song B", "Song C"}; // replace with actual songs
        JComboBox<String> songBox = new JComboBox<>(songs);
        panel.add(songBox);

        // Difficulty selection
        Difficulty[] difficulties = Difficulty.values();
        JComboBox<Difficulty> diffBox = new JComboBox<>(difficulties);
        panel.add(diffBox);

        // Start button
        JButton startBtn = new JButton("Start");
        panel.add(startBtn);
        add(panel);

        startBtn.addActionListener(e -> {
            String song = (String) songBox.getSelectedItem();
            Difficulty diff = (Difficulty) diffBox.getSelectedItem();

            dispose(); // close selection screen
            new GameFrame(currentPlayer, song, diff); // start game
        });

        setVisible(true);
    }
}
