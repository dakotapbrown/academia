package edu.vcu.browndp;

import javafx.scene.image.ImageView;

public class RaceCarView extends ImageView implements RaceCarEventListener {
    private RaceCar car;

    public RaceCarView(String url, RaceCar car) {
        super(url);

        this.car = car;
        car.setEventListener(this);
    }

    @Override
    public void positionChanged(RaceCarEvent event) {
        // update GUI
        Platform.runLater(() -> {
            this.setX(event.getCar().getPosition());
        });
    }
}
