package ru.matthew8913.simulation.model.helpers;

public interface ThreadController {
    void start();
    void resume();
    void pause();
    void close();
}
