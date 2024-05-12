module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;


    opens ru.matthew8913.simulation to javafx.fxml;
    exports ru.matthew8913.simulation;
    exports ru.matthew8913.simulation.model;
    opens ru.matthew8913.simulation.model to javafx.fxml;
    exports ru.matthew8913.simulation.views;
    opens ru.matthew8913.simulation.views to javafx.fxml;
    exports ru.matthew8913.simulation.controllers;
    opens ru.matthew8913.simulation.controllers to javafx.fxml;
    exports ru.matthew8913.simulation.model.vehicles;
    opens ru.matthew8913.simulation.model.vehicles to javafx.fxml;
    exports ru.matthew8913.simulation.model.ai;
    opens ru.matthew8913.simulation.model.ai to javafx.fxml;
    exports ru.matthew8913.simulation.model.helpers;
    opens ru.matthew8913.simulation.model.helpers to javafx.fxml;
}