package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class EnemyProjectile {
  private int x;
  private int y;
  private double directionX;
  private double directionY;
  private int speed;
  private int width;
  private int height;
  private boolean active;

  public EnemyProjectile(int startX, int startY, int targetX, int targetY, int speed) {
    this.x = startX;
    this.y = startY;
    this.speed = speed;
    this.width = 10;
    this.height = 10;
    this.active = true;

    int deltaX = targetX - startX;
    int deltaY = targetY - startY;
    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    this.directionX = deltaX / distance;
    this.directionY = deltaY / distance;
  }

  public void move() {
    x += (int) (speed * directionX);
    y += (int) (speed * directionY);

    if (x < 0 || x > Config.WIDTH || y < 0 || y > Config.HEIGHT) {
      active = false;
    }
  }

  public void draw(Raylib r) {
    if (active) {
      r.shapes.DrawRectangle(x, y, width, height, Color.RED);
    }
  }

  public boolean collidesWith(Player player) {
    return active && x < player.getX() + player.getWidth() && x + width > player.getX() &&
        y < player.getY() + player.getHeight() && y + height > player.getY();
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
