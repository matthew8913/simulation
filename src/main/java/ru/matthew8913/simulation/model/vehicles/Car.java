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
        if (moveVector != null) {
            if (!Point.near(endPoint,coordinates)) {
                coordinates = new Point(coordinates.x()+ moveVector.x(), coordinates.y()+ moveVector.y());
            //    System.out.println("Координаты: x=" + coordinates.x() +", y="+coordinates.y());
            //    System.out.println("Вектор: x=" + moveVector.x() +", y="+moveVector.y());
            }

        }
    }

}
