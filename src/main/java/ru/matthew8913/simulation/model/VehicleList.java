package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public static void addVehicle(Vehicle vehicle) {
        instance.vehicles.add(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return new ArrayList<>(vehicles);
    }
    public static void clear(){
        instance.vehicles.clear();
    }
    public static void remove(int id){
        instance.vehicles.removeIf(vehicle -> vehicle.getId()==id);
    }
    public static void serializeVehicles() {
        synchronized (instance.vehicles){
            try {
                FileOutputStream fileOut = new FileOutputStream("vehicles.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(instance.vehicles);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }

    }

    public static void deserializeVehicles() {
            getInstance();
            try {
                FileInputStream fileIn = new FileInputStream("vehicles.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                instance.vehicles.clear();
                instance.vehicles.addAll((ArrayList<Vehicle>) in.readObject());
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException c) {
                System.out.println("Vehicle class not found");
                c.printStackTrace();
            }
        }

}
