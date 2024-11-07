package game;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import game.entidades.AbstractEnemy;
import game.entidades.Enemy;
import game.entidades.EnemyProjectile;
import game.entidades.Heart;
import game.entidades.Player;
import game.entidades.Projectile;
import game.entidades.Shield;
import game.textures.Grass;
import game.textures.Sky;
import game.entidades.Boss;

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
  private List<Heart> hearts;
  private Boss boss;
  private boolean bossActive;

  private Timer tiempo;
  private int wave;
  private boolean waveDisplay;
  private Instant waveStartTime;
  private boolean gameOver;
  private Instant gameOverStartTime;

  public GameLoop() {
    r = new Raylib();
    player = new Player();
    random = new Random();
    enemies = new ArrayList<>();
    projectiles = new ArrayList<>();
    enemyProjectiles = new ArrayList<>();
    shields = new ArrayList<>();
    hearts = new ArrayList<>();
    tiempo = new Timer(3);
    wave = 1;
    waveDisplay = true;
    waveStartTime = Instant.now();
    gameOver = false;
    boss = null;
    bossActive = false;
  }

  public void run() {
    r.core.InitWindow(Config.WIDTH, Config.HEIGHT, "Core Game");

    while (!r.core.WindowShouldClose()) {
      update();
      draw();
    }
  }

  private void update() {
    if (gameOver) {
      if (Duration.between(gameOverStartTime, Instant.now()).getSeconds() >= 3) {
        resetGame();
      }
      return;
    }

    // Timer hit zero - spawn boss if not already active
    if (tiempo.getTime() <= 0 && !bossActive) {
      spawnBoss();
    }

    // Check for player death
    if (player.getHealth() <= 0 && !gameOver) {
      gameOver = true;
      gameOverStartTime = Instant.now();
      return;
    }

    // Timer update
    tiempo.update();

    // Player movement
    if (r.core.IsKeyPressed(Keyboard.KEY_D)) {
      player.move(0, Config.WIDTH, Config.HEIGHT, enemies, boss);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_A)) {
      player.move(1, Config.WIDTH, Config.HEIGHT, enemies, boss);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_S)) {
      player.move(2, Config.WIDTH, Config.HEIGHT, enemies, boss);
    } else if (r.core.IsKeyPressed(Keyboard.KEY_W)) {
      player.move(3, Config.WIDTH, Config.HEIGHT, enemies, boss);
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

            // Random chance to drop heart (10%) or shield (20%)
            int drop = random.nextInt(100);
            if (drop < 10) {
              hearts.add(new Heart(enemy.getX(), enemy.getY()));
            } else if (drop < 30) { // 20% chance for shield
              shields.add(new Shield(enemy.getX(), enemy.getY()));
            }
          }
        }
      }
    }

    // Add boss update logic after enemy updates
    if (bossActive && boss != null) {
      // Boss movement
      boss.move(random.nextInt(500), Config.WIDTH, Config.HEIGHT, player, enemies);

      // Boss shooting
      EnemyProjectile bossProjectile = boss.shoot(player);
      if (bossProjectile != null) {
        enemyProjectiles.add(bossProjectile);
      }

      // Boss spawning minions
      if (boss.canSpawnEnemy() && enemies.size() < 5) {
        enemies.add(boss.spawnEnemy());
      }

      // Check projectile collisions with boss
      for (Projectile projectile : projectiles) {
        if (projectile.collidesWith(boss)) {
          boss.damage();
          projectile.setActive(false);

          if (boss.getHealth() <= 0) {
            bossDefeated();
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
        player.damage(); // This now handles shield logic internally
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

    // Heart pickup
    for (Heart heart : hearts) {
      if (heart.collidesWith(player)) {
        player.collectHeart();
        heart.setActive(false);
      }
    }

    // Remove inactive shields
    shields.removeIf(shield -> !shield.isActive());

    // Remove inactive hearts
    hearts.removeIf(heart -> !heart.isActive());

    // Update player shield status
    player.updateShield();
  }

  private void spawnBoss() {
    enemies.clear(); // Clear existing enemies
    boss = new Boss(Config.WIDTH / 2 - 40, 100);
    bossActive = true;
  }

  private void bossDefeated() {
    bossActive = false;
    boss = null;
    player.increaseScore(); // Add extra score for defeating boss
    tiempo = new Timer(60); // Reset timer
    wave++; // Increment wave
    waveDisplay = true;
    waveStartTime = Instant.now();
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

    for (Heart heart : hearts) {
      heart.draw(r);
    }

    // Draw boss if active
    if (bossActive && boss != null) {
      boss.draw(r);
    }

    r.text.DrawText("Tiempo restante: " + tiempo.getTime() + " seg", 1175, 15, 20, Color.WHITE);

    if (waveDisplay) {
      r.text.DrawText("Wave " + wave, Config.WIDTH / 2 - 50, Config.HEIGHT / 2, 40, Color.RED);
    }

    if (gameOver) {
      r.text.DrawText("GAME OVER", Config.WIDTH / 2 - 100, Config.HEIGHT / 2, 40, Color.RED);
      r.text.DrawText("Final Score: " + player.getScore(), Config.WIDTH / 2 - 100, Config.HEIGHT / 2 + 50, 30,
          Color.RED);
    }

    r.core.EndDrawing();
  }

  private void resetGame() {
    player = new Player();
    enemies.clear();
    projectiles.clear();
    enemyProjectiles.clear();
    shields.clear();
    hearts.clear();
    tiempo = new Timer(60);
    wave = 1;
    waveDisplay = true;
    waveStartTime = Instant.now();
    gameOver = false;
    boss = null;
    bossActive = false;
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
