package game.textures;

import com.raylib.java.Raylib;

public abstract class Texture {
  protected int[][] arr;
  protected int nr;
  protected int nc;
  protected int width;
  protected int height;

  public Texture(int nr, int nc, int width, int height) {
    this.nr = nr;
    this.nc = nc;
    this.width = width;
    this.height = height;
    this.arr = new int[nr][nc];
  }

  protected void fill() {
    for (int i = 0; i < nr; i++) {
      for (int j = 0; j < nc; j++) {
        if ((i + j) % 2 == 0) {
          arr[i][j] = 1;
        } else {
          arr[i][j] = 0;
        }
      }
    }
  }

  public abstract void generate(Raylib r);
}
