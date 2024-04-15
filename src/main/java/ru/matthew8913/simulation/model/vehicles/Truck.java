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
        return;
    }
}
