package ru.matthew8913.simulation.views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ru.matthew8913.simulation.model.Habitat;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import ru.matthew8913.simulation.model.ThreadController;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Класс представления среды, реализует нетривиальную логику графического интерфейса.
 */
public class HabitatView implements ThreadController {

    private MenuItem tbMenuStartButton;
    private MenuItem tbMenuStopButton;
    private Button startButton;
    private Button stopButton;
    private final Object lock = new Object();
    /**
     * Объект среды.
     */
    private Habitat habitat;

    public MenuItem getTbMenuStartButton() {
        return tbMenuStartButton;
    }

    public void setTbMenuStartButton(MenuItem tbMenuStartButton) {
        this.tbMenuStartButton = tbMenuStartButton;
    }

    public MenuItem getTbMenuStopButton() {
        return tbMenuStopButton;
    }

    public void setTbMenuStopButton(MenuItem tbMenuStopButton) {
        this.tbMenuStopButton = tbMenuStopButton;
    }

    public void setStartButton(Button startButton) {
        this.startButton = startButton;
    }
    public void setStopButton(Button stopButton) {
        this.stopButton = stopButton;
    }


    /**
     * Pane для отрисовки среды.
     */
    private Pane habitatPane;

    /**
     * Лейбл со временем.
     */
    private Label timeLabel;

    private boolean isDrawingPaused;
    private ScheduledExecutorService executorService;

    private Runnable createDrawingTask() {
        return () -> {
                if (isDrawingPaused) {
                    return;
                }
                Platform.runLater(() -> {
                    clearHabitatPane();
                    drawHabitat();
                    if(timeLabel.isVisible()){
                        timeLabel.setText(habitat.getSimulationStopWatch().getFormattedTime());
                    }
                });
        };
    }

    @Override
    public void start() {
            isDrawingPaused = false;
            if (executorService == null || executorService.isShutdown()) {
                executorService = Executors.newSingleThreadScheduledExecutor();
                long frameDelay = 1000 / 60;
                executorService.scheduleAtFixedRate(createDrawingTask(), 0, frameDelay, TimeUnit.MILLISECONDS);
            }
    }

    @Override
    public void resume() {
            if (executorService != null) {
                executorService.shutdownNow();
            }
            start();
    }

    @Override
    public void pause() {
            if(executorService!=null){
                executorService.shutdown();
            }
    }

    public void close() {
            if (executorService != null) {
                executorService.shutdownNow();
                executorService = null;
            }
    }



    /**
     * Метод, отрисовывающий среду на панели.
     */
    public void drawHabitat() {
        Drawer.drawHabitat(habitatPane);
    }

    /**
     * Метод очистки главной панели от картинок.
     */
    public void clearHabitatPane(){
        habitatPane.getChildren().removeIf(node->node instanceof ImageView);
    }

    /**
     * Метод переключения видимости лейбла с секундомером.
     */
    public void switchTimeLabelVisible() {
        timeLabel.setVisible(!timeLabel.isVisible());
        System.out.println(timeLabel.isVisible());
    }

    /**
     * Метод вывода статистики по среде.
     */
    public void showStatistics() {
        if(habitat.isStatsIsAvailable()){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Habitat Statistics");
            alert.setHeaderText(null);
            Label label = new Label(habitat.getStatistics().generateStatistics());
            label.setFont(Font.font("Comic Sans MS", 12));
            alert.setContentText(null);
            alert.getDialogPane().setContent(label);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    timeLabel.setText("");
                    this.pause();
                    habitat.clear();
                    clearHabitatPane();
                    switchMainButtons();
                    System.out.println("OK button pressed");
                } else if (result.get() == ButtonType.CANCEL) {
                    habitat.resume();
                    this.resume();
                }
            }
        }
    }

    /**
     * Метод переключения компонент, отвечающих за старт/стоп.
     */
    public void switchMainButtons(){
        if(startButton.isDisabled()){
            tbMenuStopButton.setDisable(true);
            tbMenuStartButton.setDisable(false);
            stopButton.setDisable(true);
            startButton.setDisable(false);

        }else{
            tbMenuStopButton.setDisable(false);
            tbMenuStartButton.setDisable(true);
            stopButton.setDisable(false);
            startButton.setDisable(true);
        }
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
