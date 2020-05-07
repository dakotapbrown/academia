package edu.vcu.browndp;

import java.util.List;

public class RaceTrackModel {
    private List<RaceCar> cars;
    private RaceState raceState;

    private Thread[] threads;

    private RaceCarEventListener listener;


    public RaceTrackModel(List<RaceCar> cars) {
        this.cars = cars;
        raceState = RaceState.RESET;
        threads = new Thread[] {
                new Thread(),
                new Thread(),
                new Thread()
        };

        for (Thread thread : threads) {
            thread.start();
        }
    }


    public void fireStartAction() {
        if (raceState == RaceState.RESET) {
            // cars haven't started
            for (RaceCar car : cars) {
                car.start();
            }

            raceState = RaceState.RACING;
        } else if (raceState == RaceState.PAUSED) {
            // cars were paused
            for (RaceCar car : cars) {
                car.resume();
            }

            raceState = RaceState.RACING;
        } else if (raceState == RaceState.FINISHED) {
            for (Thread thread : threads) {
                thread.run();
            }

            for (RaceCar car : cars) {
                car.start();
            }

            raceState = RaceState.RACING;
        }
    }

    public void firePauseAction() {
        if (raceState == RaceState.RACING) {
            // threads have been started, need to pause()
            for (RaceCar car : cars) {
                car.pause();
            }

            raceState = RaceState.PAUSED;
        }
    }

    public void fireStopAction() {
        if (raceState != RaceState.RESET) {
            for (RaceCar car : cars) {
                car.stop();
            }

            raceState = RaceState.FINISHED;
        }
    }


    private enum RaceState {
        RACING,
        PAUSED,
        RESET,
        FINISHED
    }
}
