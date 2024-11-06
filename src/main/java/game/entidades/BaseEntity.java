package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public abstract class BaseEntity {
  protected int x;
  protected int y;
  protected int dx;
  protected int dy;
  protected int sx;
  protected int sy;
  protected int width;
  protected int height;
  protected Color color;
  protected int health;

  public BaseEntity(int startX, int startY, int width, int height, Color color, int health) {
    this.x = startX;
    this.y = startY;
    this.dx = width;
    this.dy = height;
    this.sx = 1;
    this.sy = 1;
    this.width = width;
    this.height = height;
    this.color = color;
    this.health = health;
  }

  public abstract void draw(Raylib r);

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
}
