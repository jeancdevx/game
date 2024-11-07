package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Projectile {
  private int x;
  private int y;
  private double directionX;
  private double directionY;
  private int speed;
  private int width;
  private int height;
  private boolean active;

  public Projectile(int startX, int startY, int targetX, int targetY) {
    this.x = startX;
    this.y = startY;
    this.speed = Config.PROJECTILE_SPEED;
    this.width = 10;
    this.height = 10;
    this.active = true;

    double deltaX = targetX - startX;
    double deltaY = targetY - startY;
    double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    this.directionX = deltaX / distance;
    this.directionY = deltaY / distance;
  }

  public void move() {
    x += (int) (directionX * speed);
    y += (int) (directionY * speed);

    if (x < 0 || x > 1440 || y < 0 || y > 960) {
      active = false;
    }
  }

  public void draw(Raylib r) {
    if (active) {
      r.shapes.DrawRectangle(x, y, width, height, Color.BLACK);
    }
  }

  public boolean collidesWith(AbstractEnemy enemy) {
    if (enemy == null) {
      return false;
    }

    return active && x < enemy.getX() + enemy.getWidth() && x + width > enemy.getX() &&
        y < enemy.getY() + enemy.getHeight() && y + height > enemy.getY();
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
