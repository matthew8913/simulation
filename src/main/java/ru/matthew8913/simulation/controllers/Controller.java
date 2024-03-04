package ru.matthew8913.simulation.controllers;

import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.views.HabitatView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class Controller{
    private Habitat habitat;
    private HabitatView habitatView;

    @FXML
    private Pane habitatPane;

    @FXML
    private Label timeLabel;

    @FXML
    public void initialize(){
        habitatView = new HabitatView();
        habitat = new Habitat();
        Platform.runLater(() -> {
            habitatPane.requestFocus();
            System.out.println(habitatPane.isFocused());
        });
        habitatView.setHabitatPane(habitatPane);
        habitatView.setHabitat(habitat);
        habitatView.setTimeLabel(timeLabel);
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case B -> {
                if(!habitat.isRunning()){
                    habitatView.clear();
                    habitat.runSimulation();
                    habitatView.startDrawingThread();
                }
            }
            case E ->{
                if(habitat.isRunning()){
                    habitat.stopSimulation();
                    habitatView.stopDrawingThread();
                    habitatView.printStatistics();
                    habitat.clear();
                }
            }
            case T -> habitatView.switchTimeLabelVisible();
            default -> {
                System.out.println("Что-то не то ввелось :(");
            }
        }
    }


}