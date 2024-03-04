package ru.matthew8913.simulation.model;

import org.apache.commons.lang3.time.StopWatch;

/**
 * Класс секундомера, используемого в симуляции.
 */
public class SimulationStopWatch extends StopWatch {

    /**
     * Метод, возвращающий текущее значение времени секундомера.
     * @return Текущее время в формате mm:ss.
     */
    public String getFormattedTime() {
        long elapsedTimeMillis = getTime();
        long elapsedMinutes = elapsedTimeMillis / 60000; // 60000 миллисекунд в минуте
        long elapsedSeconds = (elapsedTimeMillis / 1000) % 60; // остаток от деления на 60 даст секунды

        return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
    }

    /**
     * Метод, возвращающий секунды.
     * @return Количество секунд типа int.
     */
    public int getSeconds(){
        long elapsedTimeMillis = getTime();
        long elapsedSeconds = (elapsedTimeMillis / 1000) % 60;
        return (int) elapsedSeconds;
    }
}
