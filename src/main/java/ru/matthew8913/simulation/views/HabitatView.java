package ru.matthew8913.simulation.views;

import ru.matthew8913.simulation.model.Habitat;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс представления среды, реализует нетривиальную логику графического интерфейса.
 */
public class HabitatView {
    /**
     * Объект среды.
     */
    private Habitat habitat;
    /**
     * Pane для отрисовки среды.
     */
    private Pane habitatPane;
    /**
     * Поток, занимающийся отрисовкой.
     */
    private Thread drawingThread;
    /**
     * Лейбл со временем.
     */
    private Label timeLabel;

    /**
     * Метод запуска потока отрисовки.
     */
    public void startDrawingThread() {
        if (drawingThread == null || !drawingThread.isAlive()) {
            // Создаем новый поток только если он не существует или завершился
            drawingThread = new Thread(() -> {
                Timer timer = new Timer();
                int fps = 30;
                long frameDelay = 1000 / fps;

                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> {
                            drawHabitat();
                            if(timeLabel.isVisible()){
                                timeLabel.setText(habitat.getSimulationStopWatch().getFormattedTime());
                            }
                        });
                    }
                }, 0, frameDelay);
            });

            drawingThread.start();
        }
    }

    /**
     * Метод остановки потока отрисовки.
     */
    public void stopDrawingThread() {
        if (drawingThread != null && drawingThread.isAlive()) {
            drawingThread.interrupt();
        }
    }

    /**
     * Метод, отрисовывающий среду на панели.
     */
    public void drawHabitat() {
        Drawer.drawHabitat(habitat, habitatPane);
    }

    /**
     * Метод переключения видимости лейбла с секундомером.
     */
    public void switchTimeLabelVisible() {
        timeLabel.setVisible(!timeLabel.isVisible());
    }

    /**
     * Метод вывода статистики по среде.
     */
    public void printStatistics() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Habitat Statistics");
        alert.setHeaderText(null);
        Label label = new Label(habitat.getStatistics().generateStatistics());
        label.setFont(Font.font("Comic Sans MS", 12));
        alert.setContentText(null);
        alert.getDialogPane().setContent(label);
        alert.show();
    }

    /**
     * Метод очистки панели от объектов.
     */
    public void clear(){
        Drawer.clearHabitat(habitatPane);
    }



    public void setTimeLabel(Label timeLabel) {
        this.timeLabel = timeLabel;
    }

    public void setHabitat(Habitat habitat) {
        this.habitat = habitat;
    }

    public void setHabitatPane(Pane habitatPane) {
        this.habitatPane = habitatPane;
    }

}
