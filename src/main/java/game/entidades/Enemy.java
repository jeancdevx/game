package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Enemy {
  private int x;
  private int y;
  private int dx;
  private int dy;
  private int sx;
  private int sy;
  private int width;
  private int height;
  private Color color;
  private int health;

  public Enemy(int startX, int startY) {
    this.x = (startX / Config.ENTITY_SIZE) * Config.ENTITY_SIZE;
    this.y = (startY / Config.ENTITY_SIZE) * Config.ENTITY_SIZE;

    // movement
    dx = Config.ENTITY_SIZE;
    dy = Config.ENTITY_SIZE;

    // scale
    sx = 1;
    sy = 1;

    // sizes
    width = Config.ENTITY_SIZE;
    height = Config.ENTITY_SIZE;

    // color
    color = Color.BEIGE;

    health = Config.ENEMY_HEALTH;
  }

  public void move(int direction, int screenWidth, int screenHeight) {
    switch (direction) {
      case 0:
        if (x < screenWidth - width)
          x = x + dx * sx;
        break;

      case 1:
        if (x > 0)
          x = x - dx * sx;
        break;

      case 2:
        if (y < screenHeight - height)
          y = y + dy * sx;
        break;

      case 3:
        if (y > 0)
          y = y - dy * sy;
        break;
    }
  }

  public void draw(Raylib r) {
    // draw health bar
    r.shapes.DrawRectangle(x, y - 10, width, 5, Color.RED);
    r.shapes.DrawRectangle(x, y - 10, (int) (width * (health / 3.0)), 5, Color.GREEN);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }

  public void damage() {
    health--;
    if (health <= 0) {
      // Handle enemy death (e.g., remove from game)

    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public int getHealth() {
    return health;
  }
}
