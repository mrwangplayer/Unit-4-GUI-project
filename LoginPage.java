import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LoginPage extends JFrame {

    public LoginPage() {
        setTitle("Login - Party Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Top: leaderboard
        JTextArea leaderboardArea = new JTextArea();
        leaderboardArea.setEditable(false);
        updateLeaderboard(leaderboardArea);

        panel.add(new JScrollPane(leaderboardArea), BorderLayout.CENTER);

        // Bottom: login
        JPanel loginPanel = new JPanel();
        JTextField usernameField = new JTextField(15);
        JButton loginBtn = new JButton("Login");
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(loginBtn);
        panel.add(loginPanel, BorderLayout.SOUTH);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a username!");
                return;
            }

            PlayerData player = PlayerStorage.getPlayer(username);
            if (player == null) player = new PlayerData(username, 0); // new player

            // Save player in a global place or pass to song select screen
            PlayerStorage.addOrUpdatePlayer(player);

            dispose();
            new SongSelectScreen(player);
        });

        setVisible(true);
    }

    private void updateLeaderboard(JTextArea leaderboardArea) {
        List<PlayerData> topPlayers = PlayerStorage.getTopPlayers(10);
        StringBuilder sb = new StringBuilder("Leaderboard:\n\n");
        int rank = 1;
        for (PlayerData p : topPlayers) {
            sb.append(rank++).append(". ").append(p.getUsername())
              .append(" - ").append(p.getScore()).append("\n");
        }
        leaderboardArea.setText(sb.toString());
    }
}
