package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class Player {
  private int x;
  private int y;
  private int dx;
  private int dy;
  private int sx;
  private int sy;
  private int width;
  private int height;
  private Color color;
  private int health;

  public Player() {
    this.x = Config.WIDTH / 2;
    this.y = 660;
    this.dx = Config.TILE_SIZE;
    this.dy = Config.TILE_SIZE;
    this.sx = 1;
    this.sy = 1;
    this.width = Config.TILE_SIZE;
    this.height = Config.TILE_SIZE;
    this.color = Color.RED;
    this.health = Config.PLAYER_HEALTH;
  }

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

  public void draw(Raylib r) {
    r.text.DrawText("Health: " + health, 10, 10, 20, Color.BLACK);
    r.text.DrawText("Player", x, y - 20, 20, Color.BLACK);
    r.shapes.DrawRectangle(x, y, width, height, color);
  }

  public void accelerate() {
    sx++;
    sy++;
  }

  public void decelerate() {
    sx--;
    sy--;
  }

  public void damage() {
    health--;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getDx() {
    return dx;
  }

  public void setDx(int dx) {
    this.dx = dx;
  }

  public int getDy() {
    return dy;
  }

  public void setDy(int dy) {
    this.dy = dy;
  }

  public int getSx() {
    return sx;
  }

  public void setSx(int sx) {
    this.sx = sx;
  }

  public int getSy() {
    return sy;
  }

  public void setSy(int sy) {
    this.sy = sy;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public Projectile shoot(int mouseX, int mouseY) {
    return new Projectile(x + width / 2, y, mouseX, mouseY);
  }
}
