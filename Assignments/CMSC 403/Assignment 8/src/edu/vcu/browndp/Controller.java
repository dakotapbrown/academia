package edu.vcu.browndp;

import java.util.List;

public class Controller {
    private RaceTrackModel model;
    private RaceTrackView view;

    private RaceCarView carView_1;
    private RaceCarView carView_2;
    private RaceCarView carView_3;

    private List<RaceCar> cars;


    public Controller(RaceTrackModel model, RaceTrackView view) {
        this.model = model;
        this.view = view;
    }


    public void startButton() {
        // start/resume all threads
        model.fireStartAction();
    }

    public void pauseButton() {
        // interrupt/pause all threads
        model.firePauseAction();
    }

    public void resetButton() {
        // stop all cars to beginning/end each thread
        model.fireStopAction();
    }
}
