package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Point;

/**
 * Класс легкового автомобиля.
 */
public class Car extends Vehicle {
    public Car(Point coordinates,int lifeTime) {
        super(coordinates, lifeTime);
    }
    @Override
    public void move() {
        return;
    }

}
