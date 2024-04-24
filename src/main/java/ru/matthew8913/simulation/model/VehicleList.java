package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleList {
    private static VehicleList instance = null;
    private final ArrayList<Vehicle> vehicles;

    private VehicleList() {
        this.vehicles = new ArrayList<>();
    }

    public static VehicleList getInstance() {
        if (instance == null) {
            instance = new VehicleList();
        }
        return instance;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }
    public void clear(){
        vehicles.clear();
    }
    public void remove(int id){
        vehicles.removeIf(vehicle -> vehicle.getId()==id);
    }
}
