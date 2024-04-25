package ru.matthew8913.simulation;


import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import ru.matthew8913.simulation.controllers.Controller;
import ru.matthew8913.simulation.model.CarAi;
import ru.matthew8913.simulation.model.ThreadController;
import ru.matthew8913.simulation.model.TruckAi;

public class Console{
    CarAi carAi;
    TruckAi truckAi;
    private Controller controller;
    private final Stage consoleStage;
    private final TextArea textArea;

    public Console(CarAi carAi, TruckAi truckAi){
        this.carAi = carAi;
        this.truckAi = truckAi;
        textArea = new TextArea();

        consoleStage = new Stage();

        consoleStage.setTitle("Консоль");

        // Устанавливаем сцену для нового окна
        Scene scene = new Scene(textArea, 450, 450);
        consoleStage.setScene(scene);
        consoleStage.show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void close(){
        consoleStage.close();
    }
    public void start() {
        textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String[] lines = textArea.getText().split("\n");
                String command = lines[lines.length - 1].trim(); // Берем последнюю строку
                switch (command) {
                    case "stop-car":
                        if(carAi.isRunning()){
                            carAi.pause();
                            textArea.appendText("carAi успешно остановлен!!\n");
                            controller.switchCarAiButton();
                        }else{
                            textArea.appendText("carAi уже остановлен!\n");
                        }
                        break;
                    case "stop-truck":
                        if(truckAi.isRunning()){
                            truckAi.pause();
                            textArea.appendText("truckAi успешно остановлен!!\n");
                            controller.switchTruckAiButton();

                        }else{
                            textArea.appendText("truckAi уже остановлен!\n");
                        }
                        break;
                    case "start-car":
                        if(!carAi.isRunning()){
                            carAi.resume();
                            textArea.appendText("carAi успешно запущен!\n");
                            controller.switchCarAiButton();
                        }else{
                            textArea.appendText("carAi уже запущен!\n");
                        }
                        break;
                    case "start-truck":
                        if(!truckAi.isRunning()){
                            truckAi.resume();
                            textArea.appendText("truckAi успешно запущен!\n");
                            controller.switchTruckAiButton();
                        }else{
                            textArea.appendText("truckAi уже запущен!\n");
                        }
                        break;
                    case "exit":
                        close();
                        break;
                    default:
                        textArea.appendText("Неизвестная команда: " + command+"\n");
                        break;
                }
                textArea.appendText("\n"); // Добавляем новую строку для следующей команды
            }
        });
    }

}
