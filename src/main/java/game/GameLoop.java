package game;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import game.entidades.Enemy;
import game.entidades.Player;
import game.entidades.Projectile;
import game.textures.Grass;
import game.textures.Sky;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoop {
  private Raylib r;
  private Player player;
  private List<Enemy> enemies;
  private List<Projectile> projectiles;
  private Random random;

  public GameLoop() {
    r = new Raylib();
    player = new Player();
    random = new Random(); // Initialize random here
    enemies = spawnEnemies(player.getX(), 5);
    projectiles = new ArrayList<>();
  }

  public void run() {
    r.core.InitWindow(Config.WIDTH, Config.HEIGHT, "Core Game");

    while (!r.core.WindowShouldClose()) {
      update();
      draw();
    }
  }

  private void update() {
    if (r.core.IsKeyPressed(Keyboard.KEY_D)) {
      player.move(0, Config.WIDTH, Config.HEIGHT);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_A)) {
      player.move(1, Config.WIDTH, Config.HEIGHT);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_S)) {
      player.move(2, Config.WIDTH, Config.HEIGHT);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_W)) {
      player.move(3, Config.WIDTH, Config.HEIGHT);
    } else if (r.core.IsMouseButtonPressed(0)) {
      int mouseX = r.core.GetMouseX();
      int mouseY = r.core.GetMouseY();
      projectiles.add(player.shoot(mouseX, mouseY));
    }

    for (Projectile projectile : projectiles) {
      projectile.move();
    }

    for (Enemy enemy : enemies) {
      enemy.move(random.nextInt(0, 200), Config.WIDTH, Config.HEIGHT);
      for (Projectile projectile : projectiles) {
        if (projectile.collidesWith(enemy)) {
          enemy.damage();
          projectile.setActive(false);
        }
      }
    }

    enemies.removeIf(enemy -> enemy.getHealth() <= 0);
    projectiles.removeIf(projectile -> !projectile.isActive());

    if (enemies.isEmpty()) {
      enemies = spawnEnemies(player.getX(), 5);
    }
  }

  private void draw() {
    r.core.BeginDrawing();
    r.core.ClearBackground(Color.SKYBLUE);

    new Sky().generate(r);
    new Grass().generate(r);

    player.draw(r);

    for (Enemy enemy : enemies) {
      enemy.draw(r);
    }

    for (Projectile projectile : projectiles) {
      projectile.draw(r);
    }

    r.core.EndDrawing();
  }

  private List<Enemy> spawnEnemies(int playerX, int numEnemies) {
    List<Enemy> enemies = new ArrayList<>();
    for (int i = 0; i < numEnemies; i++) {
      int randomY = (random.nextInt(Config.HEIGHT / Config.ENTITY_SIZE) * Config.ENTITY_SIZE);
      enemies.add(new Enemy(playerX, randomY));
    }
    return enemies;
  }
}
