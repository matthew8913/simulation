package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.util.List;

/**
 * Класс статистики симуляции.
 */
public class Statistics {

    /**
     * Продолжительность симуляции.
     */
    private String duration;

    /**
     * Список транспортных средств.
     */
    private List<Vehicle> vehicleList;

    /**
     * Конструктор.
     */
    Statistics(){
        duration = "";
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }


    /**
     * Метод генерации статистики.
     * @return Статистика в виде строки.
     */
    public String generateStatistics(){
        StringBuilder sb = new StringBuilder();
        int truckCount = 0;
        int carCount = 0;
        for (Vehicle v:vehicleList) {
            if(v.getClass() == Car.class){
                carCount++;
            }
            if(v.getClass() == Truck.class){
                truckCount++;
            }
        }
        sb.append("Duration: ").append(duration).append("\n");
        sb.append("Car count: ").append(carCount).append("\n");
        sb.append("Truck count: ").append(truckCount);
        return sb.toString();
    }

}
