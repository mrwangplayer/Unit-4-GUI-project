import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame {
    public GameFrame(PlayerData player, String song, Difficulty difficulty) {
        setTitle("Rhythm Game - Playing " + song + " [" + difficulty.name() + "]");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700);
        setLocationRelativeTo(null);

        RhythmGamePanel panel = new RhythmGamePanel(player, difficulty);
        add(panel);

        setVisible(true);
        SwingUtilities.invokeLater(() -> {
         panel.requestFocusInWindow(); // ensures the panel gets focus after GUI is drawn
         panel.startGame();
      });
   }
   
}
