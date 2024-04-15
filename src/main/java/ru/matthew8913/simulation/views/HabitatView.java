package ru.matthew8913.simulation.views;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import ru.matthew8913.simulation.model.Habitat;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Класс представления среды, реализует нетривиальную логику графического интерфейса.
 */
public class HabitatView {
    private MenuItem tbMenuStartButton;
    private MenuItem tbMenuStopButton;
    private Button startButton;
    private Button stopButton;
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
     * Поток, занимающийся отрисовкой.
     */
    private Thread drawingThread;
    /**
     * Лейбл со временем.
     */
    private Label timeLabel;

    private boolean isDrawingPaused;
    private Timer drawingTimer;

    /**
     * Метод запуска потока отрисовки.
     */
    public void startDrawingThread() {
        isDrawingPaused = false;
        if (drawingThread == null || !drawingThread.isAlive()) {
            // Создаем новый поток только если он не существует или завершился
            drawingThread = new Thread(() -> {
                drawingTimer = new Timer();
                int fps = 30;
                long frameDelay = 1000 / fps;

                drawingTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if (!isDrawingPaused) {
                            Platform.runLater(() -> {
                                clearHabitatPane();
                                drawHabitat();
                                if(timeLabel.isVisible()){
                                    timeLabel.setText(habitat.getSimulationStopWatch().getFormattedTime());
                                }
                            });
                        }
                    }
                }, 0, frameDelay);
            });
            drawingThread.start();
        }
    }

    /**
     * Метод остановки потока отрисовки.
     */
    public void close() {
        if (drawingThread != null) {
            drawingThread.interrupt();
            drawingTimer.cancel();
        }
    }
    public void pauseDrawing() {
        isDrawingPaused=true;
    }

    /**
     * Метод продолжения отрисовки.
     */
    public void resumeDrawing() {
        isDrawingPaused=false;
    }

    /**
     * Метод, отрисовывающий среду на панели.
     */
    public void drawHabitat() {
        Drawer.drawHabitat(habitat, habitatPane);
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
                    this.pauseDrawing();
                    habitat.clear();
                    clearHabitatPane();
                    switchMainButtons();
                    System.out.println("OK button pressed");
                } else if (result.get() == ButtonType.CANCEL) {


                    habitat.resumeSimulation();
                    this.resumeDrawing();
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
