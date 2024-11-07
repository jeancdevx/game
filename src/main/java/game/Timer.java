package game;

import java.time.Duration;
import java.time.Instant;

public class Timer {
  private Instant startTime;
  private Duration duration;
  private boolean timeUp;

  public Timer(int durationInSeconds) {
    this.duration = Duration.ofSeconds(durationInSeconds);
    this.timeUp = false;
    start();
  }

  public void start() {
    this.startTime = Instant.now();
    this.timeUp = false;
  }

  public void update() {
    if (!timeUp) {
      Instant now = Instant.now();
      if (Duration.between(startTime, now).compareTo(duration) >= 0) {
        timeUp = true;
      }
    }
  }

  public boolean isTimeUp() {
    return timeUp;
  }

  public int getTime() {
    if (timeUp) {
      return 0;
    } else {
      Instant now = Instant.now();
      Duration elapsed = Duration.between(startTime, now);
      return (int) (duration.minus(elapsed).getSeconds());
    }
  }
}
