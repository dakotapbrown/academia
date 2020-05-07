package edu.vcu.browndp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RaceTrack extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        List<RaceCar> cars = new ArrayList<>();
        cars.add(new RaceCar());
        cars.add(new RaceCar());
        cars.add(new RaceCar());


        RaceTrackModel raceTrackModel = new RaceTrackModel(cars);
        RaceTrackView raceTrackView = new RaceTrackView();

        Controller mainController = new Controller(raceTrackModel, raceTrackView);


        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        primaryStage.setTitle("Richmond Raceway");
        primaryStage.setScene(new Scene(root, 500, 200));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
