package edu.vcu.browndp;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class RaceTrackView extends VBox {

    @FXML
    private ImageView car1;

    @FXML
    private ImageView car2;

    @FXML
    private ImageView car3;

    public RaceTrackView() {
        super();

        this.car1 = new RaceCarView("resources/sportive-car.png");
        this.car2 = new RaceCarView("resources/sportive-car.png");
        this.car3 = new RaceCarView("resources/sportive-car.png");

        this.getChildren().addAll(car1, car2, car3);
    }


    @Override
    public void positionChanged(RaceCarEvent event) {

        // update GUI
        Platform.runLater(() -> {

            event.getCar().getPosition();
        });
    }
}
