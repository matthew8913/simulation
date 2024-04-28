package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.helpers.Point;
import javafx.scene.image.Image;

import java.io.Serializable;

/**
 * Абстрактный класс транспортного средства.
 */
public abstract class Vehicle implements IBehaviour, Serializable {
    protected int id;
    protected Point endPoint;
    protected Point moveVector;
    protected int lifeTime;

    /**
     * Изображение транспортного средства.
     */
    protected transient Image image;
    /**
     * Переменная координаты.
     */
    protected Point coordinates;
    protected Vehicle(Point coordinates, int lifeTime) {
        this.coordinates = coordinates;
        this.lifeTime=lifeTime;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(Point endPoint) {
        this.endPoint = endPoint;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public Point getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(Point moveVector) {
        this.moveVector = moveVector;
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