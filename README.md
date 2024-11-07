# Juego de Supervivencia Espacial

Un juego de disparos en 2D desarrollado en Java usando Raylib. Enfréntate a oleadas de enemigos, recoge potenciadores y enfréntate a desafiantes jefes mientras intentas sobrevivir el mayor tiempo posible.

## Características

- Sistema de generación de enemigos por oleadas
- Batallas contra jefes
- Sistema de potenciadores (escudos y salud)
- Seguimiento de puntuación
- Mecánicas de salud y escudo del jugador
- Progresión del juego basada en un temporizador

## Controles

- **Movimiento**: WASD teclas
  - W: Mover hacia arriba
  - A: Mover hacia la izquierda
  - S: Mover hacia abajo
  - D: Mover hacia la derecha
- **Disparo**: Clic izquierdo del ratón
- **Reload**: Tecla R

## Elementos del Juego

- **Enemigos**: Regular enemies spawn in waves. The number of enemies increases with each wave.
- **Jefe**: Appears when the timer reaches zero. Can spawn minions and shoot projectiles.
- **Potenciadores**:
  - Corazones: Restauran la salud del jugador (10% de probabilidad de aparecer al derrotar enemigos)
  - Escudos: Ofrecen protección temporal (20% de probabilidad de aparecer al derrotar enemigos)
- **Puntuación**: Se ganan puntos al derrotar enemigos y jefes

## Requirements

- Java Development Kit (JDK) 8 o superior
- Raylib para Java

## Building and Running

1. Clona el repositorio
2. Asegúrate de tener el JDK instalado
3. Compila el proyecto usando tu IDE de Java preferido o una herramienta de construcción
4. Ejecuta la clase Main

## Game Structure

- `Main.java`: Entry point of the game
- `GameLoop.java`: Main game logic and update loop
- `Timer.java`: Handles game timing mechanics
- Entities:
  - `Player.java`: Player character implementation
  - `Enemy.java`: Basic enemy implementation
  - `Boss.java`: Boss enemy implementation
  - `Projectile.java`: Projectile mechanics
- Textures:
  - `Sky.java`: Sky background
  - `Grass.java`: Ground textures

## License

This project is open source and available under the MIT License.