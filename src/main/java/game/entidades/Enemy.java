package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Enemy extends BaseEntity implements Movable {
  public Enemy(int startX, int startY) {
    super((startX / Config.ENTITY_SIZE) * Config.ENTITY_SIZE, (startY / Config.ENTITY_SIZE) * Config.ENTITY_SIZE,
        Config.ENTITY_SIZE, Config.ENTITY_SIZE, Color.BEIGE, Config.ENEMY_HEALTH);
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
    r.shapes.DrawRectangle(x, y - 10, width, 5, Color.RED);
    r.shapes.DrawRectangle(x, y - 10, (int) (width * (health / 3.0)), 5, Color.GREEN);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }
}
