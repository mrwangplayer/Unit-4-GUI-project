
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

public class Arrow {
   private int x;
   private int y;
   private Direction direction;
   private int speed;
   private Image image;

   public Arrow(Direction dir, int y, int speed, int panelWidth) {
    this.direction = dir;
    this.speed = speed;
    this.y = y;

    int numArrows = Direction.values().length;
    int spacing = panelWidth / (numArrows + 1); // divide width evenly
    switch(dir) {
    case LEFT:  x = spacing * 1; break;
    case DOWN:  x = spacing * 2; break;
    case UP:    x = spacing * 3; break;
    case RIGHT: x = spacing * 4; break;
   }

    // assign image
    
    switch (dir) {
        case LEFT: image = new ImageIcon("images/left.png").getImage(); break;
        case DOWN: image = new ImageIcon("images/down.png").getImage(); break;
        case UP: image = new ImageIcon("images/up.png").getImage(); break;
        case RIGHT: image = new ImageIcon("images/right.png").getImage(); break;
    }
}

   public void update() {
      this.y -= this.speed;
   }

   public void draw(Graphics g) {
    g.drawImage(image, x - 20, y - 20, 40, 40, null);
}

   public boolean isOffscreen() {
      return this.y < -20;
   }

   public int getY() {
      return this.y;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public static enum Direction {
      LEFT,
      DOWN,
      UP,
      RIGHT;

      private Direction() {
      }
   }
}
