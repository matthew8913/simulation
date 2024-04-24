package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Point;

/**
 * Класс грузовика.
 */
public class Truck extends Vehicle {
    public Truck(Point coordinates,int lifeTime) {
        super(coordinates, lifeTime);
    }
    @Override
    public void move() {
        if (moveVector != null) {
            if (!Point.near(endPoint,coordinates)) {
                coordinates = new Point(coordinates.x()+ moveVector.x(), coordinates.y()+ moveVector.y());
            }

        }
    }
}
