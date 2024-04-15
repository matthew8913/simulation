package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.CarFactory;
import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

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
     * Флаг для статистики.
     */
    private boolean statsIsAvailable;
    /**
     * Таймер для планирования периодических задач.
     */
    private Timer simulationTimer;
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
    private final List<Vehicle> vehicleList;
    /**
     * Множество идентификаторов.
     */
    private final Set<Integer> identifiers;
    /**
     * Мапа времен рождения по id.
     */
    private final Map<Integer,Integer> birthTimes;

    /**
     * Ширина поля.
     */
    public static final int WIDTH = 640;

    /**
     * Высота поля.
     */
    public static final int HEIGHT = 400;
    public SimulationStopWatch getSimulationStopWatch() {
        return simulationStopWatch;
    }
    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }
    public Timer getSimulationTimer() {
        return simulationTimer;
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
        vehicleList = Collections.synchronizedList(new ArrayList<>());
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

    /**
     * Метод запуска симуляции.
     */
    public void startSimulation() {
        statistics = new Statistics();
        isRunning = true;
        simulationTimer = new Timer();
        simulationStopWatch = new SimulationStopWatch();
        simulationStopWatch.start();
        simulationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isRunning) {
                    int sec = simulationStopWatch.getSeconds();
                    update(sec);
                }
            }
        }, 0, 1000);
    }

    /**
     * Метод приостановки симуляции.
     */
    public void pauseSimulation() {
        if(isRunning){
            isRunning = false;
            simulationStopWatch.suspend();
            statistics.setDuration(simulationStopWatch.getFormattedTime());
            statistics.setVehicleList(List.copyOf(vehicleList));
        }
    }

    /**
     * Метод продолжения симуляции.
     */
    public void resumeSimulation() {
        isRunning = true;
        simulationStopWatch.resume();
    }

    /**
     * Метод использующийся при закрытие среды.
     */
    public void close(){
        if(simulationTimer!=null){
            simulationTimer.cancel();
            simulationStopWatch.reset();
        }
    }

    /**
     * Метод обновления среды.
     * @param sec Время, прошедшее с начала симуляции.
     */
    public void update(int sec) {
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

    /**
     * Метод удаления мертвых машин.
     * @param sec Время с начала симуляции.
     */
    public void deleteDeadCars(int sec) {
        Iterator<Vehicle> iterator = vehicleList.iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();
            if (sec - birthTimes.get(v.getId()) >= v.getLifeTime()) {
                iterator.remove();
                birthTimes.remove(v.getId());
                identifiers.remove(v.getId());
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
        vehicleList.add(v);
        birthTimes.put(id,sec);
    }

    /**
     * Метод очистки. Используется по окончании работы симуляции.
     */
    public void clear() {
        simulationStopWatch.reset();
        simulationTimer.cancel();
        simulationTimer=null; 
        vehicleList.clear();
        birthTimes.clear();
        identifiers.clear();
        statistics.setVehicleList(new ArrayList<>());
        statistics.setDuration("0:0");
    }


}
