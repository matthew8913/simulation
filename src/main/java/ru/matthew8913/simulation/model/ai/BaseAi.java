package ru.matthew8913.simulation.model.ai;

import ru.matthew8913.simulation.model.helpers.ThreadController;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BaseAi implements ThreadController {
    protected ScheduledExecutorService executorService;
    protected boolean isRunning;
    protected  boolean isPaused;
    protected final Object lock = new Object();

    public BaseAi() {
        this.isRunning = false;
        this.isPaused = false;
    }

    public abstract void move();

    public void start() {
        synchronized (lock) {
            if (executorService != null && !executorService.isShutdown()){
                executorService.shutdownNow();
            }
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(this::runTask, 0, 30, TimeUnit.MILLISECONDS);
            this.isRunning = true;
        }
    }

    public void resume() {
        synchronized (lock) {
            if(!isRunning){
                this.isRunning = true;
                executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(this::runTask, 0, 30, TimeUnit.MILLISECONDS);
            }
        }
    }

    public void pause() {
        synchronized (lock) {
            if(isRunning){
                this.isRunning = false;
                executorService.shutdownNow();
            }
        }
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public void close() {
        synchronized (lock) {
            this.isRunning = false;
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }
    public abstract void generateMoveVectors();

    public boolean isRunning() {
        return isRunning;
    }
    public boolean isPaused() {
        return isPaused;
    }

    private void runTask() {
        synchronized (lock) {
            if (isRunning) {
                generateMoveVectors();
                move();
            }
        }
    }
}
