package game;

public class Timer {
  private int contador;
  private int Frames;
  private boolean Timeup;

  public Timer(int duracion) {
    Frames = duracion * 60;
    contador = 0;
    Timeup = false;
  }

  public void Iniciar() {
    contador = 0;
    Timeup = false;
  }

  public void update() {
    if (!Timeup) {
      contador++;

      if (contador == Frames) {
        Timeup = true;
      }
    }
  }

  public boolean Timeup() {
    return Timeup;
  }

  public int getTime() {
    return (Frames - contador) / 60;
  }
}
