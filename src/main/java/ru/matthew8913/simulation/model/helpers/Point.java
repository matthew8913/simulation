package ru.matthew8913.simulation.model.helpers;

import java.io.Serializable;

/**
 * Рекорд для координат.
 * @param x Абсцисса.
 * @param y Ордината.
 */
public record Point(double x, double y) implements Serializable {
    public static boolean near(Point point1, Point point2){
        double dx = point1.x() - point2.x();
        double dy = point1.y() - point2.y();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < 25;
    }

}
