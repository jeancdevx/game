package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import java.util.List;

public abstract class AbstractEnemy extends BaseEntity {
  public AbstractEnemy(int startX, int startY, int width, int height, Color color, int health) {
    super(startX, startY, width, height, color, health);
  }

  public abstract void move(int direction, int screenWidth, int screenHeight, Player player,
      List<AbstractEnemy> enemies);

  @Override
  public void draw(Raylib r) {
    r.shapes.DrawRectangle(x, y - 10, width, 5, Color.RED);
    r.shapes.DrawRectangle(x, y - 10, (int) (width * (health / 3.0)), 5, Color.GREEN);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }
}
