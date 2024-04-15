package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Point;
import javafx.scene.image.Image;

/**
 * Абстрактный класс транспортного средства.
 */
public abstract class Vehicle implements IBehaviour {
    protected int id;

    protected int lifeTime;

    /**
     * Изображение транспортного средства.
     */
    protected Image image;
    /**
     * Переменная координаты.
     */
    protected Point coordinates;
    protected Vehicle(Point coordinates, int lifeTime) {
        this.coordinates = coordinates;
        this.lifeTime=lifeTime;
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}