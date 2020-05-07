package edu.vcu.browndp;

import javafx.application.Platform;

import java.util.Random;

public class RaceCar implements Runnable {
    private int position;

    private static volatile boolean raceEnded = false;
    private static volatile boolean racePaused = false;
    private static volatile boolean raceStarted;


    public RaceCar() {
        this.position = RaceCarPosition.START;
    }


    public void start() {}
    // TODO

    public void resume() {
        synchronized (RaceCar.class) {
            racePaused = false;
            RaceCar.class.notifyAll();
        }
    }

    public void pause() {
        racePaused = true;
    }

    public void stop() {
        raceEnded = true;

        resume();
    }


    private void race() {
        while (!raceEnded) {
            synchronized (RaceCar.class) {
                if (raceEnded) {
                    // could've been updated while waiting for lock
                    break;
                }

                if (racePaused) {
                    try {
                        RaceCar.class.wait();
                    } catch (InterruptedException e) {
                        break;
                    }

                    if (raceEnded) {
                        // could've been updated before wait() returned
                        break;
                    }
                }


                int rand = (new Random()).nextInt(10);
                position += rand;

                if (this.isAtEnd()) {
                    // somehow let other cars know the race has ended,
                    // can use static boolean flag
                    raceEnded = true;
                    position = RaceCarPosition.END;

                    // TODO
                    // view.positionChanged(new RaceCarEvent(this, position));

                    Platform.runLater(() -> {
                        // Update GUI
                    });
                } else {
                    // TODO
                    // view.positionChanged(new RaceCarEvent(this, position));

                    Platform.runLater(() -> {
                        // Update GUI
                    });
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }

    private boolean isAtEnd() {
        return position >= 188;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (RaceCar.class) {
                if (raceStarted) {
                    race();
                } else {
                    try {
                        RaceCar.class.wait();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }
    }


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    class RaceCarPosition {
        public static final int START = 12;
        public static final int END = 188;
    }
}
