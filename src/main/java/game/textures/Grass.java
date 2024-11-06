package game.textures;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class Grass extends Texture {
  public Grass() {
    super(12, 24, 60, 60);
  }

  @Override
  public void generate(Raylib r) {
    fill();

    for (int i = 0; i < nr; i++) {
      for (int j = 0; j < nc; j++) {
        if (arr[i][j] == 1) {
          r.shapes.DrawRectangle(j * width, 720 + i * height, width, height, Color.GREEN);
        } else {
          r.shapes.DrawRectangle(j * width, 720 + i * height, width, height, Color.DARKGREEN);
        }
      }
    }
  }
}