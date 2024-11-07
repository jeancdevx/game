package game.entidades;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Player extends BaseEntity {
  private static final int MAX_HEALTH = 5;
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
        MAX_HEALTH); // Start with max health
    score = 0;
    level = 0;
    maxAmmo = 20;
    ammo = maxAmmo;
    shieldActive = false;
    shieldDuration = Duration.ofSeconds(15);
  }

  public void move(int direction, int screenWidth, int screenHeight, List<AbstractEnemy> enemies) {
    int newX = x;
    int newY = y;

    switch (direction) {
      case 0: // Right
        newX = x + dx * sx;
        break;
      case 1: // Left
        newX = x - dx * sx;
        break;
      case 2: // Down
        newY = y + dy * sx;
        break;
      case 3: // Up
        newY = y - dy * sy;
        break;
    }

    if (canMove(newX, newY, screenWidth, screenHeight, enemies)) {
      x = newX;
      y = newY;
    }
  }

  @Override
  public void draw(Raylib r) {
    r.text.DrawText("Health: " + health, 10, 10, 20, Color.BLACK);
    r.text.DrawText("Player", x, y - 20, 20, Color.BLACK);
    r.text.DrawText("Score: " + score, 10, 40, 20, Color.BLACK);
    r.text.DrawText("Level: " + level, 10, 70, 20, Color.BLACK);
    r.text.DrawText("Ammo: " + ammo, 10, 100, 20, Color.BLACK);
    r.shapes.DrawRectangle(x, y, width, height, color);

    if (shieldActive) {
      r.shapes.DrawRectangle(x, y, width, height, Color.MAGENTA);
    }
  }

  public void activateShield() {
    if (!shieldActive && health < MAX_HEALTH + 1) {
      shieldActive = true;
      shieldStartTime = Instant.now();
      health = Math.min(health + 1, MAX_HEALTH + 1);
    }
  }

  public void removeShield() {
    if (shieldActive) {
      shieldActive = false;
      health = Math.min(health - 1, MAX_HEALTH);
    }
  }

  @Override
  public void damage() {
    if (shieldActive) {
      removeShield();
    } else {
      health = Math.max(0, health - 1);
    }
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
      return new Projectile(-1, -1, -1, -1);
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

  public void collectHeart() {
    if (health < MAX_HEALTH) {
      health++;
    }
  }
}
