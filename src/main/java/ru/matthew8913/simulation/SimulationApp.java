package ru.matthew8913.simulation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ru.matthew8913.simulation.controllers.Controller;
import ru.matthew8913.simulation.model.ai.CarAi;
import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.ai.TruckAi;
import ru.matthew8913.simulation.views.HabitatView;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class SimulationApp extends Application {
    private HabitatView habitatView;
    private Habitat habitat;
    private CarAi carAi;
    private TruckAi truckAi;
    private Controller controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SimulationApp.class.getResource("views/habitat-view.fxml"));
        stage.setTitle("Simulation");
        Image icon = new Image(Objects.requireNonNull(SimulationApp.class.getResourceAsStream("icons/main-icon.png")));
        stage.getIcons().add(icon);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        controller = fxmlLoader.getController();
        habitatView = controller.getHabitatView();
        habitat = controller.getHabitat();
        carAi = controller.getCarAi();
        truckAi = controller.getTruckAi();
        controller.setStage(stage);
    }
    @Override
    public void stop(){
        controller.close();
        habitat.close();
        habitatView.close();
        carAi.close();
        truckAi.close();
    }
    public static void main(String[] args) {
        launch();


    }
}