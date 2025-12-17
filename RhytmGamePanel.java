
   import java.awt.Color;
   import java.awt.Graphics;
   import java.awt.event.ActionEvent;
   import java.awt.event.ActionListener;
   import java.io.File;
   import java.util.ArrayList;
   import java.util.Iterator;
   import java.util.Random;
   import javax.sound.sampled.AudioInputStream;
   import javax.sound.sampled.AudioSystem;
   import javax.sound.sampled.Clip;
   import javax.swing.AbstractAction;
   import javax.swing.ActionMap;
   import javax.swing.ImageIcon;
   import javax.swing.InputMap;
   import javax.swing.JFrame;
   import javax.swing.JOptionPane;
   import javax.swing.JPanel;
   import javax.swing.KeyStroke;
   import javax.swing.SwingUtilities;
   import javax.swing.Timer;
   import java.awt.Graphics2D;
   import java.awt.AlphaComposite;

   public class RhythmGamePanel extends JPanel implements ActionListener {
      private ArrayList<Arrow> arrows = new ArrayList<>();
      private Timer gameTimer;
      private Timer spawnTimer;
      private int score = 0;
      private int combo = 0;
      private Difficulty difficulty;
      private long startTime;
      private final long gameDurationMs = 30000; // 30 seconds in milliseconds
      private PlayerData currentPlayer;
      private String hitFeedback = "";   // empty at start
      private long feedbackTime = 0;     // when feedback was triggered
      private final int FEEDBACK_DURATION = 300; // milliseconds to show

      public RhythmGamePanel(PlayerData currentPlayer, Difficulty difficulty) {
      this.currentPlayer = currentPlayer;
      this.difficulty = difficulty;

      setBackground(Color.BLACK);
      setFocusable(true);
      setFocusTraversalKeysEnabled(false);

      setupKeyBindings();

      gameTimer = new Timer(16, this);
      spawnTimer = new Timer(difficulty.spawnIntervalMs, e -> spawnArrow());
      }

      private void spawnArrow() {
      Random rand = new Random();
      Arrow.Direction[] dirs = Arrow.Direction.values();
      Arrow.Direction dir = dirs[rand.nextInt(dirs.length)];

      int panelHeight = getHeight();
      if (panelHeight <= 0) {
         panelHeight = getParent() != null ? getParent().getHeight() : 700; 
      }
      int y = panelHeight + 20; // start just below the panel
      int panelWidth = getWidth() > 0 ? getWidth() : 400; // fallback if 0
      arrows.add(new Arrow(dir, y, difficulty.arrowSpeed, panelWidth));
   }  
   private void drawHitArrows(Graphics g) {
    int y = HIT_Y; // fixed hit position
    float opacity = 0.3f; 
    Graphics g2 = g.create();
    ((Graphics2D) g2).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

    int panelWidth = getWidth();
    int numArrows = Arrow.Direction.values().length;
    int spacing = panelWidth / (numArrows + 1);

    g2.drawImage(new ImageIcon("images/left.png").getImage(), spacing * 1 - 20, y - 20, 40, 40, null);
    g2.drawImage(new ImageIcon("images/down.png").getImage(), spacing * 2 - 20, y - 20, 40, 40, null);
    g2.drawImage(new ImageIcon("images/up.png").getImage(), spacing * 3 - 20, y - 20, 40, 40, null);
    g2.drawImage(new ImageIcon("images/right.png").getImage(), spacing * 4 - 20, y - 20, 40, 40, null);

    g2.dispose();
   }

      public void actionPerformed(ActionEvent var1) {
         Iterator var2 = this.arrows.iterator();

         while(var2.hasNext()) {
         long elapsed = System.currentTimeMillis() - startTime;
         if (elapsed >= gameDurationMs) {
               endGame();
               return; // stop updating arrows
            }
            Arrow var3 = (Arrow)var2.next();
            var3.update();
            if (var3.isOffscreen()) {
               this.combo = 0;
               var2.remove();
            }
         }

         this.repaint();
      }
   
      
      private void endGame() {
      gameTimer.stop();
      spawnTimer.stop();

      // Save the player score
      currentPlayer.setScore(score);
      PlayerStorage.addOrUpdatePlayer(currentPlayer);

      // Show final score
      JOptionPane.showMessageDialog(this,
               "Game Over!\nYour Score: " + score);

      // Go back to login or song select
      JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
      topFrame.dispose();
      new SongSelectScreen(currentPlayer);
   }

   @Override
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       drawHitArrows(g);

    // draw moving arrows
    for (Arrow a : arrows) {
        a.draw(g);
    }

    // draw score/combo
    g.setColor(Color.WHITE);
    g.drawString("Score: " + score, 20, 20);
    g.drawString("Combo: " + combo, 20, 40);

    // draw hit feedback only if recent
    if (!hitFeedback.isEmpty() && System.currentTimeMillis() - feedbackTime < FEEDBACK_DURATION) {
    g.setColor(Color.GREEN); 
    g.drawString(hitFeedback, getWidth() / 2 - 30, 80); // centered at top
   }
}

      private final int HIT_Y = 100; // same as stationary arrows

      private void checkHit(Arrow.Direction dir) {
         Arrow closest = null;
         int distance = Integer.MAX_VALUE;

         for (Arrow a : arrows) {
            if (a.getDirection() == dir) {
               int diff = Math.abs(a.getY() - HIT_Y);
               if (diff < distance) {
                  distance = diff;
                  closest = a;
               }
         }
      }

         if (closest != null && distance <= 30) { // hit window
            arrows.remove(closest);
            score += 100;
            combo++;
            hitFeedback = "Perfect";
            feedbackTime = System.currentTimeMillis();
         } else if (closest != null && distance <= 60) {
            arrows.remove(closest);
            score += 50;
            combo++;
            hitFeedback = "Good";
            feedbackTime = System.currentTimeMillis();
         } else {
            hitFeedback = "Miss!";
            feedbackTime = System.currentTimeMillis();
            combo = 0;
         }
      }

      private void setupKeyBindings() {
         InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
         ActionMap am = this.getActionMap();

         // Arrow key bindings
         im.put(KeyStroke.getKeyStroke("LEFT"), "left");
         im.put(KeyStroke.getKeyStroke("UP"), "up");
         im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
         im.put(KeyStroke.getKeyStroke("DOWN"), "down");

         am.put("left", new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  checkHit(Arrow.Direction.LEFT);
               }
         });
         am.put("up", new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  checkHit(Arrow.Direction.UP);
               }
         });
         am.put("right", new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  checkHit(Arrow.Direction.RIGHT);
               }
         });
         am.put("down", new AbstractAction() {
               @Override
               public void actionPerformed(ActionEvent e) {
                  checkHit(Arrow.Direction.DOWN);
               }
         });
      }
      
      public void startGame() {
      startTime = System.currentTimeMillis();
      spawnArrow(); // first arrow
      gameTimer.start();
      spawnTimer.start();
      requestFocusInWindow();
   }
}
