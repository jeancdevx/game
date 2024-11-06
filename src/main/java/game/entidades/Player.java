package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Player extends BaseEntity implements Movable {
  private int score;

  public Player() {
    super(Config.WIDTH / 2, 660, Config.ENTITY_SIZE, Config.ENTITY_SIZE, Color.RED, Config.PLAYER_HEALTH);
    score = 0;
  }

  @Override
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

  @Override
  public void draw(Raylib r) {
    r.shapes.DrawRectangle(10, 85, Config.HEALTH_WIDTH, Config.HEALTH_HEIGHT, Color.GREEN);
    r.text.DrawText("Health: " + health, 10, 10, 20, Color.BLACK);
    r.text.DrawText("Player", x, y - 20, 20, Color.BLACK);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }

  public void accelerate() {
    sx++;
    sy++;
  }

  public void decelerate() {
    sx--;
    sy--;
  }

  public Projectile shoot(int mouseX, int mouseY) {
    return new Projectile(x + width / 2, y, mouseX, mouseY);
  }

  public void increaseScore() {
    score += 75;
  }

  public int getScore() {
    return score;
  }
}
