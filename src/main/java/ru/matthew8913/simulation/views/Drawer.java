package ru.matthew8913.simulation.views;

import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.vehicles.Vehicle;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Класс-художник.
 */
public class Drawer {
    /**
     * Метод, отрисовывающий среду.
     * @param habitat Среда, требующая отрисовки.
     * @param habitatPane Панель, на которой нужно рисовать.
     */
    public static void drawHabitat(Habitat habitat, Pane habitatPane) {
        // Добавляем ImageView для каждой машины в списке
        for (Vehicle veh : habitat.getVehicleList()) {
            ImageView imageView = new ImageView(veh.getImage());
            // Устанавливаем позицию ImageView на основе координат машины
            imageView.setX(veh.getCoordinates().x());
            imageView.setY(veh.getCoordinates().y());
            // Добавляем ImageView в корневой узел сцены
            habitatPane.getChildren().add(imageView);
        }
    }

    /**
     * Метод очистки панели от объектов.
     * @param habitatPane Панель, требующая очистки.
     */
    public static void clearHabitat(Pane habitatPane){
        habitatPane.getChildren().clear();
    }
}