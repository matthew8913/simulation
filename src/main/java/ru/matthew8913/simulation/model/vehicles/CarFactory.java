package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.Point;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.SplittableRandom;

public class CarFactory {
    private static final double pTruck = 30;
    private static final double pCar = 60;
    private static final int nTruck = 2;
    private static final int nCar = 1;
    private static final String carImagePath = "/ru/matthew8913/simulation/assets/car.png";
    private static final String truckImagePath = "/ru/matthew8913/simulation/assets/truck.png";


    public Truck createTruck(int sec){
        SplittableRandom random = new SplittableRandom();
        if(sec%nTruck==0){
            if(random.nextInt(1,101)<=pTruck){
                Truck truck = new Truck(new Point(random.nextInt(1, Habitat.WIDTH),random.nextInt(1,Habitat.HEIGHT)));
                InputStream imageStream = getClass().getResourceAsStream(truckImagePath);
                if (imageStream != null) {
                    truck.setImage(new Image(imageStream));
                } else {
                    System.err.println("Image not found: ");
                }
                return truck;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }

    public Car createCar(int sec){
        SplittableRandom random = new SplittableRandom();
        if(sec%nCar==0){
            if(random.nextInt(1,101)<=pCar){
                Car car = new Car(new Point(random.nextInt(1, Habitat.WIDTH),random.nextInt(1,Habitat.HEIGHT)));
                InputStream imageStream = getClass().getResourceAsStream(carImagePath);
                if (imageStream != null) {
                    car.setImage(new Image(imageStream));
                } else {
                    System.err.println("Image not found: ");
                }
                return car;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
}
