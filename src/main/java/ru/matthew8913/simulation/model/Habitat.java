package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.CarFactory;
import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс среды.
 */
public class Habitat implements ThreadController{
    /**
     * Статистика по среде.
     */
    private Statistics statistics;
    /**
     * Флаг активности среды.
     */
    private boolean isRunning;
    /**
     * Флаг для статистики.
     */
    private boolean statsIsAvailable;

    /**
     * Секундомер для симуляции.
     */
    private SimulationStopWatch simulationStopWatch;
    /**
     * Фабрика автомобилей.
     */
    private final CarFactory factory;
    /**
     * Список автомобилей.
     */
    private final VehicleList vehicleList;
    /**
     * Множество идентификаторов.
     */
    private final TreeSet<Integer> identifiers;
    /**
     * Мапа времен рождения по id.
     */
    private final HashMap<Integer,Integer> birthTimes;
    private ScheduledExecutorService executorService;
    /**
     * Ширина поля.
     */
    public static final int WIDTH = 640;

    /**
     * Высота поля.
     */
    public static final int HEIGHT = 400;

    private final Object lock = new Object();
    public SimulationStopWatch getSimulationStopWatch() {
        return simulationStopWatch;
    }
    public VehicleList getVehicleList() {
        return vehicleList;
    }
    public Statistics getStatistics() {
        return statistics;
    }
    public boolean isStatsIsAvailable() {
        return statsIsAvailable;
    }
    public boolean isRunning() {
        return isRunning;
    }
    public Map<Integer, Integer> getBirthTimes() {
        return birthTimes;
    }

    public void setStatsIsAvailable(boolean statsIsAvailable) {
        this.statsIsAvailable = statsIsAvailable;
    }

    /**
     * Конструктор среды.
     */
    public Habitat() {
        //Пока что используем потокобезопасный массив.
        identifiers = new TreeSet<>();
        vehicleList = VehicleList.getInstance();
        birthTimes = new HashMap<>();
        factory = new CarFactory();
        isRunning = false;
        statsIsAvailable = true;
        simulationStopWatch = new SimulationStopWatch();
    }

    /**
     * Метод установки параметров фабрики.
     * @param pCar Вероятность рождения car.
     * @param pTruck Вероятность рождения truck.
     * @param nCar Интервал рождения car.
     * @param nTruck Интервал рождения car.
     * @param lifeTimeCar Время жизни car.
     * @param lifeTimeTruck Время жизни truck.
     */
    public void setFactoryParameters(int pCar, int pTruck, int nCar, int nTruck,
    int lifeTimeCar, int lifeTimeTruck) {
        factory.setnCar(nCar);
        factory.setnTruck(nTruck);
        factory.setpCar(pCar);
        factory.setpTruck(pTruck);
        factory.setLifeTimeCar(lifeTimeCar);
        factory.setLifeTimeTruck(lifeTimeTruck);
    }

    private Runnable createTask() {
        return () -> {
            synchronized (lock) {
                while (!isRunning) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                }
                int sec = simulationStopWatch.getSeconds();
                update(sec);
            }
        };
    }

    public void start() {
        System.out.println("Запустился старт ёпт");
        synchronized (lock) {
            if (executorService != null && !executorService.isShutdown()){
                executorService.shutdownNow();
            }
            statistics = new Statistics();
            simulationStopWatch = new SimulationStopWatch();
            simulationStopWatch.start();
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(createTask(), 0, 1, TimeUnit.SECONDS);
            isRunning = true;
            lock.notifyAll();
        }
    }

    public void resume() {
        synchronized (lock) {
            isRunning = true;
            if(simulationStopWatch.isSuspended()){
                simulationStopWatch.resume();
            }
            if (executorService == null || executorService.isShutdown()) {
                executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(createTask(), 0, 1, TimeUnit.SECONDS);
            }
            lock.notifyAll();
        }
    }

    @Override
    public void pause() {
        synchronized (lock) {
            if(isRunning){
                isRunning = false;
                simulationStopWatch.suspend();
                statistics.setDuration(simulationStopWatch.getFormattedTime());
                statistics.setVehicleList(List.copyOf(vehicleList.getVehicles()));
                if (executorService != null) {
                    executorService.shutdownNow();
                }
            }
        }
    }

    public void close(){
        synchronized (lock) {
            if(executorService!=null){
                executorService.shutdownNow();
                executorService = null;
                simulationStopWatch.reset();
            }
        }
    }

    /**
     * Метод обновления среды.
     * @param sec Время, прошедшее с начала симуляции.
     */
    public void update(int sec) {
        synchronized (vehicleList) {
            Truck newTruck = factory.createTruck(sec);
            Car newCar = factory.createCar(sec);
            if (newTruck != null) {
                addVehiclesToHabitat(newTruck,sec);
            }
            if (newCar != null) {
                addVehiclesToHabitat(newCar,sec);
            }
            deleteDeadCars(sec);
        }
    }

    /**
     * Метод удаления мертвых машин.
     * @param sec Время с начала симуляции.
     */
    public void deleteDeadCars(int sec) {
        Iterator<Vehicle> iterator = vehicleList.getVehicles().iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();

            if (sec - birthTimes.get(v.getId()) >= v.getLifeTime()) {
                birthTimes.remove(v.getId());
                identifiers.remove(v.getId());
                vehicleList.remove(v.getId());
            }
        }
    }

    /**
     * Метод добавления машин в среду. Обрабатывает все коллекции.
     * @param v Машина.
     * @param sec Время её появления.
     */
    public void addVehiclesToHabitat(Vehicle v, int sec){
        int id;
        do {
            id = new Random().nextInt(10000) + 1;
        } while (identifiers.contains(id));
        v.setId(id);
        vehicleList.addVehicle(v);
        identifiers.add(id);
        birthTimes.put(id,sec);
    }

    /**
     * Метод очистки. Используется по окончании работы симуляции.
     */
    public void clear() {
        simulationStopWatch.reset();
        executorService = null;
        vehicleList.clear();
        birthTimes.clear();
        identifiers.clear();
        statistics.setVehicleList(new ArrayList<>());
        statistics.setDuration("0:0");
    }


}
