package ru.matthew8913.simulation.model;

import ru.matthew8913.simulation.model.vehicles.Car;
import ru.matthew8913.simulation.model.vehicles.CarFactory;
import ru.matthew8913.simulation.model.vehicles.Truck;
import ru.matthew8913.simulation.model.vehicles.Vehicle;

import java.io.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс среды.
 */
public class Habitat implements ThreadController, Serializable {
    /**
     * Статистика по среде.
     */
    private transient Statistics statistics;
    /**
     * Флаг активности среды.
     */
    private transient boolean isRunning;
    /**
     * Флаг для статистики.
     */
    private transient boolean statsIsAvailable;

    /**
     * Секундомер для симуляции.
     */
    private transient SimulationStopWatch simulationStopWatch;
    /**
     * Фабрика автомобилей.
     */
    private transient final CarFactory factory;

    /**
     * Множество идентификаторов.
     */
    private TreeSet<Integer> identifiers;
    /**
     * Мапа времен рождения по id.
     */
    private HashMap<Integer,Integer> birthTimes;
    private transient int timeAtStart;
    private int timeForSerialize;
    private transient ScheduledExecutorService executorService;
    /**
     * Ширина поля.
     */
    public static final int WIDTH = 640;

    /**
     * Высота поля.
     */
    public static final int HEIGHT = 400;

    public final transient Object lock = new Object();

    public Object getLock() {
        return lock;
    }

    public SimulationStopWatch getSimulationStopWatch() {
        return simulationStopWatch;
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
        timeAtStart = 0;
        identifiers = new TreeSet<>();
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
                int sec = simulationStopWatch.getSeconds();
                System.out.println(sec);
                if(sec == 10){
                    System.out.println();
                }
                printCollectionsSize();
                update(sec+timeAtStart);
                System.out.println("Машинки генерятся!");
        };
    }

    public CarFactory getFactory() {
        return factory;
    }

    public void start() {
            if (executorService == null || executorService.isShutdown()){
                statistics = new Statistics();
                simulationStopWatch = new SimulationStopWatch();
                simulationStopWatch.start();
                executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(createTask(), 0, 1, TimeUnit.SECONDS);
                isRunning = true;

            }


    }

    public void resume() {
            isRunning = true;
            if(simulationStopWatch.isSuspended()){
                simulationStopWatch.resume();
            }
            if (executorService == null || executorService.isShutdown()) {
                executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(createTask(), 0, 1, TimeUnit.SECONDS);
            }
    }

    @Override
    public void pause() {
            if(isRunning){
                isRunning = false;
                simulationStopWatch.suspend();
                statistics.setDuration(simulationStopWatch.getFormattedTime());
                statistics.setVehicleList(List.copyOf(VehicleList.getInstance().getVehicles()));
                if (executorService != null) {
                    executorService.shutdown();
                }
            }
    }

    public void close(){
            if(executorService!=null){
                executorService.shutdownNow();
                executorService = null;
                simulationStopWatch.stop();
            }
            clear();
    }

    /**
     * Метод обновления среды.
     * @param sec Время, прошедшее с начала симуляции.
     */
    public void update(int sec) {
        synchronized (VehicleList.getInstance().getVehicles()) {
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
        Iterator<Vehicle> iterator = VehicleList.getInstance().getVehicles().iterator();
        while (iterator.hasNext()) {
            Vehicle v = iterator.next();

            if (sec - birthTimes.get(v.getId()) >= v.getLifeTime()) {
                birthTimes.remove(v.getId());
                identifiers.remove(v.getId());
                VehicleList.remove(v.getId());
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
        VehicleList.addVehicle(v);
        identifiers.add(id);
        birthTimes.put(id,sec);
    }

    /**
     * Метод очистки. Используется по окончании работы симуляции.
     */
    public void clear() {
        simulationStopWatch.reset();
        executorService = null;
        if(VehicleList.getInstance()!=null){
            VehicleList.clear();
        };
        if(birthTimes!=null){
            birthTimes.clear();
        }
        if(identifiers!=null){
            identifiers.clear();

        }
        if(statistics!=null){
            statistics.setVehicleList(new ArrayList<>());
            statistics.setDuration("0:0");
        }

    }
    public void initializeVehicleCollections(){
        for(Vehicle v:VehicleList.getInstance().getVehicles()){
            identifiers.add(v.getId());
            birthTimes.put(v.getId(),0);
        }
    }
    public void printCollectionsSize(){
        if(identifiers!=null){
            System.out.print("i: "+identifiers.size());
        }
        if(birthTimes!=null){
            System.out.print("; b: "+birthTimes.size());
        }
        System.out.println("; v: " + VehicleList.getInstance().getVehicles().size());
    }
    public void setTime(){
        timeForSerialize = simulationStopWatch.getSeconds();
    }
    public void serialize() {
        try {
            FileOutputStream fileOut = new FileOutputStream("habitat.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            setTime();
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public void deserialize() {
        try {
            FileInputStream fileIn = new FileInputStream("habitat.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            Habitat habitat = (Habitat) in.readObject();
            this.identifiers = habitat.identifiers;
            this.birthTimes = habitat.birthTimes;
            this.timeAtStart = timeForSerialize;
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Habitat class not found");
            c.printStackTrace();
        }
    }

}
