package game.entidades;

import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import game.Config;

public class OleadasT {
    private boolean on;
    private long InicioTime;
    private final int duracionTime = 1000;
    private int numeroWave;
    
    public OleadasT(){
        on = false;
        InicioTime = 0;
        numeroWave = 1;
    }
    public void activar(){
        on = true;
        InicioTime = System.currentTimeMillis();
    }
     public void siguienteOleada() {
        numeroWave++;
        activar(); // Reinicia el contador de tiempo
    }
     
    public void draw(Raylib r) {
        if (on) {
            long Lapso = System.currentTimeMillis() - InicioTime;
            if (Lapso < duracionTime) {
                String message = "Oleada " + numeroWave;
                int x = Config.WIDTH / 2 - 100;
                int y = Config.HEIGHT / 2;
                r.text.DrawText(message, x, y, 40, Color.GOLD);
            } else {
                on = false;  // Desactiva el mensaje después de 1 segundo
            }
        }
    }
    public boolean isActive() {
        return on;
    }
    public int getNumeroWave(){
        return numeroWave;
    }
}
