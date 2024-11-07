package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;
import java.util.List;

public class Enemy extends AbstractEnemy {
  public Enemy(int startX, int startY) {
    super((startX / Config.ENTITY_SIZE) * Config.ENTITY_SIZE, (startY / Config.ENTITY_SIZE) * Config.ENTITY_SIZE,
        Config.ENTITY_SIZE, Config.ENTITY_SIZE, Color.BEIGE, Config.ENEMY_HEALTH);
  }

  @Override
  public void move(int direction, int screenWidth, int screenHeight, Player player, List<AbstractEnemy> enemies) {
    int newX = x;
    int newY = y;

    switch (direction) {
      case 0:
        newX = x + dx * sx;
        break;
      case 1:
        newX = x - dx * sx;
        break;
      case 2:
        newY = y + dy * sx;
        break;
      case 3:
        newY = y - dy * sy;
        break;
    }

    if (canMove(newX, newY, screenWidth, screenHeight, enemies) && !(player.getX() == newX && player.getY() == newY)) {
      x = newX;
      y = newY;
    }
  }

  @Override
  public void draw(Raylib r) {
    r.shapes.DrawRectangle(x, y - 10, width, 5, Color.RED);
    r.shapes.DrawRectangle(x, y - 10, (int) (width * (health / 3.0)), 5, Color.GREEN);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }
}
