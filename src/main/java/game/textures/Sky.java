package game.textures;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class Sky {
  private int[][] arr;
  private int nr;
  private int nc;
  private int width;
  private int height;

  public Sky() {
    nr = 4;
    nc = 24;
    arr = new int[nr][nc];
    width = 60;
    height = 60;
  }

  public void fill(Raylib r) {
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

  public void generateSky(Raylib r) {
    fill(r);

    for (int i = 0; i < nr; i++) {
      for (int j = 0; j < nc; j++) {
        if (arr[i][j] == 1) {
          r.shapes.DrawRectangle(j * width, 0 + i * height, width, height, Color.SKYBLUE);
        } else {
          r.shapes.DrawRectangle(j * width, 0 + i * height, width, height, Color.BLUE);
        }
      }
    }
  }
}
