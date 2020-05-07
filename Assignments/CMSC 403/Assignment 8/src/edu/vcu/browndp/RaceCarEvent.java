package edu.vcu.browndp;

public class RaceCarEvent {
    private final RaceCar car;
    private final RaceCarPosition carPosition;

    public RaceCarEvent(RaceCar car, int carPosition) {
        this.car = car;
        this.carPosition = carPosition;
    }

    public RaceCar getCar() {
        return car;
    }

    public RaceCarPosition getCarPosition() {
        return carPosition;
    }
}
