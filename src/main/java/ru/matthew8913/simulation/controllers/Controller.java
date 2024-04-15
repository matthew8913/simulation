package ru.matthew8913.simulation.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.vehicles.Vehicle;
import ru.matthew8913.simulation.views.HabitatView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Controller{
    private Habitat habitat;
    private HabitatView habitatView;


    @FXML
    private Label timeLabel;
    @FXML
    private Pane habitatPane;


    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private CheckBox showStatsCheckBox;
    @FXML
    private RadioButton showTimeRadioButton;
    @FXML
    private RadioButton hideTimeRadioButton;
    @FXML
    private ChoiceBox<String> pTruckChoiceBox;
    @FXML
    private ChoiceBox<String> pCarChoiceBox;
    @FXML
    private TextField carIntervalTextField;
    @FXML
    private TextField truckIntervalTextField;
    @FXML
    private TextField carLifeTimeTextField;
    @FXML
    private TextField truckLifeTimeTextField;
    @FXML
    public Button currentObjectsButton;

    @FXML
    public MenuItem tbStartButton;
    @FXML
    public MenuItem tbEndButton;
    @FXML
    public CheckMenuItem tbShowStatsCheckBox;
    @FXML
    public CheckMenuItem tbShowTimeCheckBox;


    /**
     * Метод инициализации всех компонент
     */
    @FXML
    public void initialize(){
        habitat = new Habitat();
        Platform.runLater(() -> habitatPane.requestFocus());
        initializeHabitatView();
        initializeMainButtons();
        initializeStatsControls();
        initializeTimeControls();
        initializePControls();
        initializeIntervalControls();
        initializeLifetimeControls();
    }

    /**
     * Метод инициализации класса view среды. Передаёт в него компоненты с нетривиальной логикой.
     */
    public void initializeHabitatView(){
        habitatView = new HabitatView();
        habitatView.setTbMenuStartButton(tbStartButton);
        habitatView.setTbMenuStopButton(tbEndButton);
        habitatView.setHabitatPane(habitatPane);
        habitatView.setHabitat(habitat);
        habitatView.setTimeLabel(timeLabel);
        habitatView.setStartButton(startButton);
        habitatView.setStopButton(stopButton);
    }

    /**
     * Инициализация кнопок старта и остановки.
     */
    public void initializeMainButtons(){
        startButton.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        tbEndButton.setDisable(true);
    }

    /**
     * Инициализация компонент управления статистическим окном.
     */
    public void initializeStatsControls(){
        showStatsCheckBox.setFocusTraversable(false);
        showStatsCheckBox.setSelected(true);
        tbShowStatsCheckBox.setSelected(true);
    }

    /**
     * Инициализация компонент управления отображением времени.
     */
    public void initializeTimeControls(){
        ToggleGroup timeToggleGroup = new ToggleGroup();
        showTimeRadioButton.setToggleGroup(timeToggleGroup);
        hideTimeRadioButton.setToggleGroup(timeToggleGroup);
        showTimeRadioButton.setFocusTraversable(false);
        hideTimeRadioButton.setFocusTraversable(false);
        hideTimeRadioButton.setSelected(true);
    }

    /**
     * Инициализация choice boxes для вероятности.
     */
    public void initializePControls(){
        for(int i = 0;i<=100;i+=10){
            pCarChoiceBox.getItems().add(i+ "%");
            pTruckChoiceBox.getItems().add(i+ "%");
        }
        pCarChoiceBox.setValue("50%");
        pTruckChoiceBox.setValue("50%");
    }

    /**
     * Инициализация филдов для установки интервалов рождения
     */
    public void initializeIntervalControls(){
        carIntervalTextField.setText("1");
        truckIntervalTextField.setText("1");
        carIntervalTextField.setOnKeyPressed(this::handleKeyPressed);
        truckIntervalTextField.setOnKeyPressed(this::handleKeyPressed);
    }

    /**
     * Инициализация компонент управления временем жизни объеков.
     */
    public void initializeLifetimeControls(){
        carLifeTimeTextField.setText("10");
        truckLifeTimeTextField.setText("10");
        carLifeTimeTextField.setOnKeyPressed(this::handleKeyPressed);
        truckLifeTimeTextField.setOnKeyPressed(this::handleKeyPressed);
    }


    public Habitat getHabitat() {
        return habitat;
    }

    public HabitatView getHabitatView() {
        return habitatView;
    }

    public void setDefaultFactoryParameters(){
        carIntervalTextField.setText("1");
        truckIntervalTextField.setText("1");
        carLifeTimeTextField.setText("10");
        truckLifeTimeTextField.setText("10");
    }

    /**
     * Метод считывающий данные со всех компонент управления. Формирует фабрику для среды.
     * @return Отработало корректно/некорректно.
     */
    public boolean setFactoryParameters() {
        try {
            String carIntervalText = carIntervalTextField.getText();
            String truckIntervalText = truckIntervalTextField.getText();
            checkTimeIntervals(carIntervalText, truckIntervalText);
            String carLifeTimeText = carLifeTimeTextField.getText();
            String truckLifeTimeText = truckLifeTimeTextField.getText();
            checkLifeTimes(carLifeTimeText, truckLifeTimeText);
            int carInterval = Integer.parseInt(carIntervalText);
            int truckInterval = Integer.parseInt(truckIntervalText);
            int carLifeTime = Integer.parseInt(carLifeTimeText);
            int truckLifeTime = Integer.parseInt(truckLifeTimeText);
            int pCar = Integer.parseInt(pCarChoiceBox.getValue().substring(0, pCarChoiceBox.getValue().length()-1));
            int pTruck = Integer.parseInt(pTruckChoiceBox.getValue().substring(0, pTruckChoiceBox.getValue().length()-1));
            habitat.setFactoryParameters(pCar,pTruck,carInterval,truckInterval,carLifeTime,truckLifeTime);
            return true;
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверные параметры");
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
            setDefaultFactoryParameters();
            return false;
        }
    }

    /**
     * Обработчик нажатий по клавиатуре.
     * @param event Событие.
     */
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case ENTER -> habitatPane.requestFocus();
            case B -> startSimulation();
            case E -> stopSimulation();
            case T -> handleShowTimeAction(new ActionEvent());
            default -> System.out.println("Что-то не то ввелось :(");
        }
    }

    /**
     * Обработка кнопки Start.
     */
    @FXML
    private void handleStartButton(){
        startSimulation();
    }

    /**
     * Обработчик кнопки Stop.
     */
    @FXML
    private void handleStopButton(){
        stopSimulation();
    }

    /**
     * Обработчик компонент, отвечающий за показ/скрытие статистики.
     * @param event Событие.
     */
    @FXML
    private void handleShowStatsAction(ActionEvent event){
        Object source = event.getSource();
        boolean isSelected;
        if(source==showStatsCheckBox){
            isSelected = showStatsCheckBox.isSelected();
        }else{
            isSelected = tbShowStatsCheckBox.isSelected();
        }
        showStatsCheckBox.setSelected(isSelected);
        tbShowStatsCheckBox.setSelected(isSelected);
        habitat.setStatsIsAvailable(isSelected);
    }

    /**
     * Обработчик компонент, отвечающих за показ/скрытие лейбла времени.
     * @param action Событие.
     */
    @FXML
    private void handleShowTimeAction(Event action){
        habitatView.switchTimeLabelVisible();
        Object source = action.getSource();
        if(source == showTimeRadioButton){
            tbShowTimeCheckBox.setSelected(showTimeRadioButton.isSelected());
        } else if (source == tbShowTimeCheckBox) {
            showTimeRadioButton.setSelected(tbShowTimeCheckBox.isSelected());
            hideTimeRadioButton.setSelected(!tbShowTimeCheckBox.isSelected());
        } else{
            tbShowTimeCheckBox.setSelected(timeLabel.isVisible());
            showTimeRadioButton.setSelected(timeLabel.isVisible());
            hideTimeRadioButton.setSelected(!timeLabel.isVisible());
        }

    }

    /**
     * Обработчик кнопки показа текущих объектов.
     */
    @FXML
    private void handleCurrentObjectsButton(){
        if(habitat.isRunning()){
            habitat.pauseSimulation();
            habitatView.pauseDrawing();
            StringBuilder sb = new StringBuilder();
            List<Vehicle> vehicleList = habitat.getVehicleList();
            Set<Map.Entry<Integer,Integer>> entrySet = habitat.getBirthTimes().entrySet();
            for (Map.Entry<Integer,Integer> e : entrySet) {
                Vehicle veh = Objects.requireNonNull(vehicleList.stream()
                        .filter(v -> v.getId() == e.getKey())
                        .findFirst()
                        .orElse(null));
                sb.append(veh.getClass().getSimpleName())
                        .append("(")
                        .append(veh.getId())
                        .append("): ").append("born at ")
                        .append(e.getValue()).append(" sec ")
                        .append("and lives for ").append(veh.getLifeTime())
                        .append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vehicle Information");
            alert.setHeaderText("Vehicle Details:");
            alert.setContentText(sb.toString());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        habitatView.resumeDrawing();
                        habitat.resumeSimulation();
                    });


        }

    }

    /**
     * Метод, запускающий симуляцию. Либо продолжающий, если она запущена.
     */
    void startSimulation(){
        if(!habitat.isRunning()){
            if(habitat.getSimulationTimer()==null){
                if(setFactoryParameters()){
                    habitatView.clear();
                    habitat.startSimulation();
                    habitatView.startDrawingThread();
                    habitatView.switchMainButtons();
                }
            }else{
                habitat.resumeSimulation();
                habitatView.resumeDrawing();
                habitatView.switchMainButtons();
            }
        }
    }

    /**
     * Метод остановки симуляции либо ее прерывания.
     */
    void stopSimulation(){
        if(habitat.isRunning()){
            habitat.pauseSimulation();
            habitatView.pauseDrawing();
            habitatView.showStatistics();
        }
        if(!habitat.isStatsIsAvailable()){
            habitatView.switchMainButtons();
            habitat.clear();
        }

    }

    /**
     * Метод проверки интервалов рождения.
     * @param carBirthInterval Интервал рождения car.
     * @param truckBirthInterval Интервал рождения truck.
     * @throws IllegalArgumentException Если значения некорректны.
     */
    public void checkTimeIntervals(String carBirthInterval, String truckBirthInterval) throws IllegalArgumentException {
        try {
            int i1 = Integer.parseInt(carBirthInterval);
            int i2 = Integer.parseInt(truckBirthInterval);
            if(i1<=0||i2<=0){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значения интервалов рождения должны быть натуральными числами!");
        }
    }

    /**
     * Метод проверки времен жизни объектов.
     * @param carLifeTime Время жизни car.
     * @param truckLifeTime Время жизни truck.
     * @throws IllegalArgumentException Если значения некорректны.
     */
    public void checkLifeTimes(String carLifeTime, String truckLifeTime) throws IllegalArgumentException {
        try {
            int i1 = Integer.parseInt(carLifeTime);
            int i2 = Integer.parseInt(truckLifeTime);
            if(i1<=0||i2<=0){
                throw new NumberFormatException();
            }


        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значения времени жизни должны быть натуральными числами!");
        }
    }
}