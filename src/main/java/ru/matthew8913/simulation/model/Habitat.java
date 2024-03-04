package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.CarFactory;
import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Класс среды.
 */
public class Habitat {
    /**
     * Статистика по среде.
     */
    private Statistics statistics;
    /**
     * Флаг активности среды.
     */
    private boolean isRunning;
    /**
     * Таймер для планирования периодических задач.
     */
    private Timer simulationTimer;
    /**
     * Секундомер для симуляции.
     */
    private final SimulationStopWatch simulationStopWatch;
    /**
     * Фабрика автомобилей.
     */
    private final CarFactory factory;
    /**
     * Список автомобилей.
     */
    private final List<Vehicle> vehicleList;
    /**
     * Ширина поля.
     */
    public static final int WIDTH = 640;
    /**
     * Высота поля.
     */
    public static final int HEIGHT = 400;

    /**
     * Конструктор среды.
     */
    public Habitat(){
        //Пока что используем потокобезопасный массив.
        vehicleList = new CopyOnWriteArrayList<>();
        factory = new CarFactory();
        isRunning = false;
        simulationStopWatch=new SimulationStopWatch();
    }

    /**
     * Метод запуска симуляции.
     */
    public void runSimulation() {
        statistics = new Statistics();
        isRunning = true;
        simulationTimer = new Timer();
        simulationStopWatch.start();
        simulationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                int sec = simulationStopWatch.getSeconds();
                update(sec);
            }
        }, 0, 1000);

    }

    /**
     * Метод остановки симуляции.
     */
    public void stopSimulation(){
        if(isRunning){
            isRunning=false;
            simulationStopWatch.stop();
            statistics.setDuration(simulationStopWatch.getFormattedTime());
            simulationTimer.cancel();
            statistics.setVehicleList(List.copyOf(vehicleList));
            simulationStopWatch.reset();
        }
    }

    /**
     * Метод обновления среды.
     * @param sec Время, прошедшее с начала симуляции.
     */
    public void update(int sec){
        Truck newTruck = factory.createTruck(sec);
        Car newCar = factory.createCar(sec);

        if (newTruck != null) {
            vehicleList.add(newTruck);
        }

        if (newCar != null) {
            vehicleList.add(newCar);
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void clear(){
        vehicleList.clear();
        statistics = null;
    }

    public SimulationStopWatch getSimulationStopWatch() {
        return simulationStopWatch;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }
}
