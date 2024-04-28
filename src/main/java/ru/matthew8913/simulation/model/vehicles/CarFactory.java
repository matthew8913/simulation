package ru.matthew8913.simulation.model.vehicles;

import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.helpers.Point;
import javafx.scene.image.Image;
import ru.matthew8913.simulation.model.VehicleList;

import java.io.InputStream;
import java.util.Objects;
import java.util.SplittableRandom;

public class CarFactory {
    private int lifeTimeTruck;
    private int lifeTimeCar;
    private int pTruck;
    private  int pCar;
    private  int nTruck;
    private   int nCar;
    private static final String carImagePath = "/ru/matthew8913/simulation/assets/car.png";
    private static final String truckImagePath = "/ru/matthew8913/simulation/assets/truck.png";

    public void setpTruck(int pTruck) {
        this.pTruck = pTruck;
    }

    public void setpCar(int pCar) {
        this.pCar = pCar;
    }

    public void setnTruck(int nTruck) {
        this.nTruck = nTruck;
    }

    public void setnCar(int nCar) {
        this.nCar = nCar;
    }

    public int getLifeTimeTruck() {
        return lifeTimeTruck;
    }

    public void setLifeTimeTruck(int lifeTimeTruck) {
        this.lifeTimeTruck = lifeTimeTruck;
    }

    public int getLifeTimeCar() {
        return lifeTimeCar;
    }

    public void setLifeTimeCar(int lifeTimeCar) {
        this.lifeTimeCar = lifeTimeCar;
    }




    public Truck createTruck(int sec){
        SplittableRandom random = new SplittableRandom();
        if(sec%nTruck==0){
            if(random.nextInt(1,101)<=pTruck){
                Truck truck = new Truck(new Point(random.nextInt(1, Habitat.WIDTH),random.nextInt(1,Habitat.HEIGHT)),lifeTimeTruck);
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
                Car car = new Car(new Point(random.nextInt(1, Habitat.WIDTH),random.nextInt(1,Habitat.HEIGHT)),lifeTimeCar);
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
    public void setImages(){
        for(Vehicle v: VehicleList.getInstance().getVehicles()){
            if(v instanceof Car){
                v.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(carImagePath))));

            }else{
                v.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(truckImagePath))));
            }
        }
    }
}
