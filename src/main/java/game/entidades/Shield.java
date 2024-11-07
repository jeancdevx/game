package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class Shield {
  private int x;
  private int y;
  private int width;
  private int height;
  private boolean active;

  public Shield(int x, int y) {
    this.x = x;
    this.y = y;
    this.width = 20;
    this.height = 20;
    this.active = true;
  }

  public void draw(Raylib r) {
    if (active) {
      r.shapes.DrawRectangle(x, y, width, height, Color.BLUE);
    }
  }

  public boolean collidesWith(Player player) {
    return active && x < player.getX() + player.getWidth() && x + width > player.getX() &&
        y < player.getY() + player.getHeight() && y + height > player.getY();
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isActive() {
    return active;
  }
}
