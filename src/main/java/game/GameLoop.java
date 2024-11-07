package game;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import game.entidades.AbstractEnemy;
import game.entidades.Enemy;
import game.entidades.EnemyProjectile;
import game.entidades.Player;
import game.entidades.Projectile;
import game.entidades.Shield;
import game.textures.Grass;
import game.textures.Sky;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.Duration;
import java.time.Instant;

public class GameLoop {
  private Raylib r;
  private Random random;

  private Player player;
  private List<AbstractEnemy> enemies;
  private List<Projectile> projectiles;
  private List<EnemyProjectile> enemyProjectiles;
  private List<Shield> shields;

  private Timer tiempo;
  private int wave;
  private boolean waveDisplay;
  private Instant waveStartTime;

  public GameLoop() {
    r = new Raylib();
    player = new Player();
    random = new Random();
    enemies = new ArrayList<>();
    projectiles = new ArrayList<>();
    enemyProjectiles = new ArrayList<>();
    shields = new ArrayList<>();
    tiempo = new Timer(60);
    wave = 1;
    waveDisplay = true;
    waveStartTime = Instant.now();
  }

  public void run() {
    r.core.InitWindow(Config.WIDTH, Config.HEIGHT, "Core Game");

    while (!r.core.WindowShouldClose()) {
      update();
      draw();
    }
  }

  private void update() {
    // Timer update
    tiempo.update();

    // Player movement
    if (r.core.IsKeyPressed(Keyboard.KEY_D)) {
      player.move(0, Config.WIDTH, Config.HEIGHT, enemies);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_A)) {
      player.move(1, Config.WIDTH, Config.HEIGHT, enemies);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_S)) {
      player.move(2, Config.WIDTH, Config.HEIGHT, enemies);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_W)) {
      player.move(3, Config.WIDTH, Config.HEIGHT, enemies);
    } else if (r.core.IsMouseButtonPressed(0)) {
      int mouseX = r.core.GetMouseX();
      int mouseY = r.core.GetMouseY();

      projectiles.add(player.shoot(mouseX, mouseY));
    }

    // Projectile reload
    if (r.core.IsKeyPressed(Keyboard.KEY_R))
      player.reload();

    // Enemy movement and shooting
    for (AbstractEnemy enemy : enemies) {
      // enemy movement randomization
      enemy.move(random.nextInt(400), Config.WIDTH, Config.HEIGHT, player, enemies);

      // enemy shooting
      EnemyProjectile enemyProjectile = ((Enemy) enemy).shoot(player, wave + 2); // Increase speed with wave
      if (enemyProjectile != null) {
        enemyProjectiles.add(enemyProjectile);
      }

      // enemy collision with projectiles
      for (Projectile projectile : projectiles) {
        if (projectile.collidesWith(enemy)) {
          enemy.damage();
          projectile.setActive(false);

          // puntaje de los enemigos al ser matados
          if (enemy.getHealth() <= 0) {
            player.increaseScore();
            player.increaseLevel();

            // Randomly drop a shield
            if (random.nextInt(100) < 20) { // 20% chance to drop a shield
              shields.add(new Shield(enemy.getX(), enemy.getY()));
            }
          }
        }
      }
    }

    // Projectile movement
    for (Projectile projectile : projectiles) {
      projectile.move();
    }

    // Enemy projectile movement
    for (EnemyProjectile enemyProjectile : enemyProjectiles) {
      enemyProjectile.move();
      if (enemyProjectile.collidesWith(player)) {
        player.damage();
        enemyProjectile.setActive(false);
      }
    }

    // Remove dead entities
    enemies.removeIf(enemy -> enemy.getHealth() <= 0);
    // Remove inactive projectiles
    projectiles.removeIf(projectile -> !projectile.isActive());
    enemyProjectiles.removeIf(enemyProjectile -> !enemyProjectile.isActive());

    // Wave logic
    if (waveDisplay) {
      if (Duration.between(waveStartTime, Instant.now()).getSeconds() >= 2) {
        waveDisplay = false;
        enemies = spawnEnemies(wave * 2 + 3); // Increase enemies with each wave
      }
    } else {
      // Check if all enemies are dead
      if (enemies.isEmpty()) {
        wave++;
        waveDisplay = true;
        waveStartTime = Instant.now();
      }
    }

    // Shield pickup
    for (Shield shield : shields) {
      if (shield.collidesWith(player)) {
        player.activateShield();
        shield.setActive(false);
      }
    }

    // Remove inactive shields
    shields.removeIf(shield -> !shield.isActive());

    // Update player shield status
    player.updateShield();
  }

  private void draw() {
    r.core.BeginDrawing();
    r.core.ClearBackground(Color.SKYBLUE);

    new Sky().generate(r);
    new Grass().generate(r);

    player.draw(r);

    for (AbstractEnemy enemy : enemies) {
      enemy.draw(r);
    }

    for (Projectile projectile : projectiles) {
      projectile.draw(r);
    }

    for (EnemyProjectile enemyProjectile : enemyProjectiles) {
      enemyProjectile.draw(r);
    }

    for (Shield shield : shields) {
      shield.draw(r);
    }

    r.text.DrawText("Tiempo restante: " + tiempo.getTime() + " seg", 1175, 15, 20, Color.WHITE);

    if (waveDisplay) {
      r.text.DrawText("Wave " + wave, Config.WIDTH / 2 - 50, Config.HEIGHT / 2, 40, Color.RED);
    }

    r.core.EndDrawing();
  }

  private List<AbstractEnemy> spawnEnemies(int numEnemies) {
    List<AbstractEnemy> enemies = new ArrayList<>();

    for (int i = 0; i < numEnemies; i++) {
      int randomX = (random.nextInt(Config.WIDTH / Config.ENTITY_SIZE) * Config.ENTITY_SIZE);
      int randomY = (random.nextInt(Config.HEIGHT / Config.ENTITY_SIZE) * Config.ENTITY_SIZE);

      enemies.add(new Enemy(randomX, randomY));
    }

    return enemies;
  }
}
