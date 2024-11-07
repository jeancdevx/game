package game.entidades;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Player extends BaseEntity {
  private int score;
  private int level;
  private int ammo;
  private int maxAmmo;
  private boolean shieldActive;
  private Instant shieldStartTime;
  private Duration shieldDuration;

  public Player() {
    super(
        Config.WIDTH / 2,
        660,
        Config.ENTITY_SIZE,
        Config.ENTITY_SIZE,
        Color.RED,
        Config.PLAYER_HEALTH);
    score = 0;
    level = 0;
    maxAmmo = 20;
    ammo = maxAmmo;
    shieldActive = false;
    shieldDuration = Duration.ofSeconds(15);
  }

  public void move(int direction, int screenWidth, int screenHeight, List<Enemy> enemies) {
    boolean collision = false;
    int newX = x;
    int newY = y;

    switch (direction) {
      case 0: // Right
        if (x < screenWidth - width)
          x = x + dx * sx;
        break;
      case 1: // Left
        if (x > 0)
          x = x - dx * sx;
        break;
      case 2: // Down
        if (y < screenHeight - height)
          y = y + dy * sx;
        break;
      case 3: // Up
        if (y > 0)
          y = y - dy * sy;
        break;
    }

    for (Enemy enemy : enemies) {
      if ((x == enemy.getX() && y == enemy.getY()) || (x == enemy.getX() && y == enemy.getY())
          || (x == enemy.getX() && y == enemy.getY()) || (x == enemy.getX() && y == enemy.getY())) {
        collision = true;
        break;
      }
    }

    if (collision) {
      x = newX;
      y = newY;
    }
  }

  @Override
  public void draw(Raylib r) {
    r.text.DrawText("Health: " + health, 10, 10, 20, Color.BLACK);
    r.text.DrawText("Player", x, y - 20, 20, Color.BLACK);
    r.shapes.DrawRectangle(x, y, width, height, color);

    if (shieldActive) {
      r.shapes.DrawRectangle(x, y, width, height, Color.MAGENTA);
    }
  }

  public void activateShield() {
    shieldActive = true;
    shieldStartTime = Instant.now();
    health++;
  }

  public void updateShield() {
    if (shieldActive && Duration.between(shieldStartTime, Instant.now()).compareTo(shieldDuration) >= 0) {
      shieldActive = false;

      if (health > 1) {
        health--;
      }
    }
  }

  public boolean isShieldActive() {
    return shieldActive;
  }

  public void reload() {
    this.ammo = maxAmmo;
  }

  public Projectile shoot(int mouseX, int mouseY) {
    if (ammo > 0) {
      ammo--;
      return new Projectile(x + width / 2, y, mouseX, mouseY);
    } else {
      return new Projectile(0, 0, 0, 0);
    }
  }

  public void increaseScore() {
    score += 75;
  }

  public void increaseLevel() {
    if (score == 0) {
    } else if (score == 600) {
      level = 1;
      maxAmmo = 22;
    } else if (score == 1200) {
      level = 2;
      maxAmmo = 24;
    } else if (score == 2400) {
      level = 3;
      maxAmmo = 26;
    } else if (score == 4800) {
      level = 4;
      maxAmmo = 28;
    } else if (score == 7200) {
      level = 5;
      maxAmmo = 30;
    }
  }

  public int getScore() {
    return score;
  }

  public int getLevel() {
    return level;
  }
}
