package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;
import java.util.List;
import java.util.Random;

public class Boss extends AbstractEnemy {
  private Random random;
  private long lastSpawnTime;
  private static final int SPAWN_COOLDOWN = 5000;
  private static final int BOSS_SIZE = Config.ENTITY_SIZE * 2;
  private static final int BOSS_HEALTH = 20;
  private static final int MOVE_DISTANCE = Config.ENTITY_SIZE * 2;

  public Boss(int startX, int startY) {
    super(startX, startY, BOSS_SIZE, BOSS_SIZE, Color.PURPLE, BOSS_HEALTH);
    random = new Random();
    lastSpawnTime = System.currentTimeMillis();
  }

  @Override
  public void move(int direction, int screenWidth, int screenHeight, Player player, List<AbstractEnemy> enemies) {
    int newX = x;
    int newY = y;

    switch (direction) {
      case 0: // Right
        newX = x + MOVE_DISTANCE;
        break;
      case 1: // Left
        newX = x - MOVE_DISTANCE;
        break;
      case 2: // Down
        newY = y + MOVE_DISTANCE;
        break;
      case 3: // Up
        newY = y - MOVE_DISTANCE;
        break;
    }

    // Ensure movement stays on grid
    newX = (newX / Config.ENTITY_SIZE) * Config.ENTITY_SIZE;
    newY = (newY / Config.ENTITY_SIZE) * Config.ENTITY_SIZE;

    // Check collision with screen bounds, player and other enemies
    if (canMove(newX, newY, screenWidth, screenHeight, enemies) &&
        !collidesWithPlayer(newX, newY, player)) {
      x = newX;
      y = newY;
    }
  }

  private boolean collidesWithPlayer(int newX, int newY, Player player) {
    return newX < player.getX() + player.getWidth() &&
        newX + width > player.getX() &&
        newY < player.getY() + player.getHeight() &&
        newY + height > player.getY();
  }

  @Override
  protected boolean canMove(int newX, int newY, int screenWidth, int screenHeight,
      List<? extends BaseEntity> entities) {
    // Check screen bounds - account for boss size
    if (newX < 0 || newX + width > screenWidth || newY < 0 || newY + height > screenHeight) {
      return false;
    }

    // Check collision with other entities
    for (BaseEntity entity : entities) {
      if (entity != this &&
          newX < entity.getX() + entity.getWidth() &&
          newX + width > entity.getX() &&
          newY < entity.getY() + entity.getHeight() &&
          newY + height > entity.getY()) {
        return false;
      }
    }

    return true;
  }

  @Override
  public void draw(Raylib r) {
    // Health bar
    r.shapes.DrawRectangle(x, y - 20, width, 10, Color.RED);
    r.shapes.DrawRectangle(x, y - 20, (int) (width * (health / (float) BOSS_HEALTH)), 10, Color.GREEN);
    // Boss body
    r.shapes.DrawRectangle(x, y, width, height, color);
    r.text.DrawText("BOSS", x + 10, y - 40, 20, Color.RED);
  }

  public boolean canSpawnEnemy() {
    return System.currentTimeMillis() - lastSpawnTime >= SPAWN_COOLDOWN;
  }

  public Enemy spawnEnemy() {
    lastSpawnTime = System.currentTimeMillis();
    int spawnX = x + random.nextInt(200) - 100;
    int spawnY = y + random.nextInt(200) - 100;

    // Keep spawn position within screen bounds
    spawnX = Math.max(0, Math.min(spawnX, Config.WIDTH - Config.ENTITY_SIZE));
    spawnY = Math.max(0, Math.min(spawnY, Config.HEIGHT - Config.ENTITY_SIZE));

    return new Enemy(spawnX, spawnY);
  }

  public EnemyProjectile shoot(Player player) {
    if (random.nextInt(10000) < 5) {
      return new EnemyProjectile(x + width / 2, y + height / 2, player.getX(), player.getY(), 8);
    }
    return null;
  }
}
