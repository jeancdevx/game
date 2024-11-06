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
  private Random random;
  
  private Player player;
  private List<Enemy> enemies;

  private List<Projectile> projectiles;
  
  private Timer tiempo;
  private int puntaje;
  
  public GameLoop() {
    r = new Raylib();
    player = new Player();
    random = new Random();
    enemies = spawnEnemies(player.getX(), 5);
    projectiles = new ArrayList<>();
    tiempo = new Timer(180);
    puntaje = 0;
  }

  public void run() {
    r.core.InitWindow(Config.WIDTH, Config.HEIGHT, "Core Game");

    while (!r.core.WindowShouldClose()) {
      update();
      draw();
      tiempo.update();
    }
  }

  private void update() {
     // Player movement
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

    // Projectile movement
    for (Projectile projectile : projectiles) {
      projectile.move();
    }

    // Enemy movement and collision
    for (Enemy enemy : enemies) {
      // enemy movement randomization
      enemy.move(random.nextInt(0, 300), Config.WIDTH, Config.HEIGHT);

      // enemy collision with projectiles
      for (Projectile projectile : projectiles) {
        if (projectile.collidesWith(enemy)) {
          enemy.damage();
          projectile.setActive(false);
          //puntaje de los enemigos al ser matados
        if(enemy.getHealth()<= 0){
           puntaje += 75;
       }
          
        }
      }
    }

    // Remove dead entities
    enemies.removeIf(enemy -> enemy.getHealth() <= 0);
    // Remove inactive projectiles
    projectiles.removeIf(projectile -> !projectile.isActive());

    // Check if all enemies are dead
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
    r.text.DrawText("Tiempo restante: " + tiempo.getTime() + " seg", 1175, 15, 20, Color.WHITE);
    // puntaje en pantalla
    r.text.DrawText("Puntaje: " + puntaje, 10, 125, 22, Color.GOLD);
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
