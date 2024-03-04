package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Point;
import javafx.scene.image.Image;

/**
 * Абстрактный класс транспортного средства.
 */
abstract public class Vehicle implements IBehaviour {
    /**
     * Изображение транспортного средства.
     */
    protected Image image;
    /**
     * Переменная координаты.
     */
    protected Point coordinates;
    public Point getCoordinates() {
        return coordinates;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    protected Vehicle(Point coordinates) {
        this.coordinates = coordinates;
    }

}