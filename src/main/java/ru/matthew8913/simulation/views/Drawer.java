package ru.matthew8913.simulation.views;

import javafx.scene.Node;
import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.VehicleList;
import ru.matthew8913.simulation.model.vehicles.Vehicle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс-художник.
 */
public class Drawer {
    /**
     * Метод, отрисовывающий среду.
     * @param habitatPane Панель, на которой нужно рисовать.
     */
    public static void drawHabitat(Pane habitatPane) {
        List<Vehicle> vehList = VehicleList.getInstance().getVehicles();
        // Добавляем ImageView для каждой машины в списке
        synchronized (vehList){
            for (Vehicle veh : vehList) {
                ImageView imageView = new ImageView(veh.getImage());
                // Устанавливаем позицию ImageView на основе координат машины
                imageView.setX(veh.getCoordinates().x());
                imageView.setY(veh.getCoordinates().y());
                // Добавляем ImageView в корневой узел сцены
                habitatPane.getChildren().add(imageView);
            }
        }
    }

    /**
     * Метод очистки панели от объектов.
     * @param habitatPane Панель, требующая очистки.
     */
    public static void clearHabitat(Pane habitatPane) {
        List<Node> children = new ArrayList<>(habitatPane.getChildren());
        for (Node child : children) {
            if (child instanceof ImageView) {
                habitatPane.getChildren().remove(child);
            }
        }
    }

}