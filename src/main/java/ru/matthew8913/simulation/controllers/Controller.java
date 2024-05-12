package ru.matthew8913.simulation.controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.matthew8913.simulation.model.ai.CarAi;
import ru.matthew8913.simulation.model.Habitat;
import ru.matthew8913.simulation.model.ai.TruckAi;
import ru.matthew8913.simulation.model.VehicleList;
import ru.matthew8913.simulation.model.vehicles.Vehicle;
import ru.matthew8913.simulation.views.HabitatView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Controller {
    Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Button saveButton;
    public Button loadButton;

    public void save() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Выберите папку для сохранения");
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory != null) {
            // Выбрана папка, можно выполнять сериализацию
            String selectedPath = selectedDirectory.getAbsolutePath();
            serialize(selectedPath);
        }
    }

    public void load() {
        // Открываем диалоговое окно для выбора файла habitat
        FileChooser habitatFileChooser = new FileChooser();
        habitatFileChooser.setTitle("Выберите файл habitat");
        File habitatFile = habitatFileChooser.showOpenDialog(stage);
        // Открываем диалоговое окно для выбора файла vehicleList
        FileChooser vehicleListFileChooser = new FileChooser();
        vehicleListFileChooser.setTitle("Выберите файл vehicleList");
        File vehicleListFile = vehicleListFileChooser.showOpenDialog(stage);
        if (vehicleListFile!=null) {
            try{
                deserialize(habitatFile,vehicleListFile);
                return;
            }catch(Exception e){
                System.out.println("Произошла ошибка десериализации");
            }
        }
        habitat.close();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Button consoleButton;

    public CarAi getCarAi() {
        return carAi;
    }

    public TruckAi getTruckAi() {
        return truckAi;
    }

    @FXML
    public Button carAiStartButton;
    @FXML
    public Button carAiStopButton;
    @FXML
    public Button truckAiStartButton;
    @FXML
    public Button truckAiStopButton;
    private Habitat habitat;
    private HabitatView habitatView;
    private CarAi carAi;
    private TruckAi truckAi;

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
    private Properties properties;

    private Console console;
    /**
     * Метод инициализации всех компонент
     */
    @FXML
    public void initialize() {
        try{
            properties = new Properties();
            properties.load(Controller.class.getResourceAsStream("/ru/matthew8913/simulation/configs/simulation.properties"));

        } catch (IOException e) {
            System.out.println("Проблемы со считыванием файла конфигурации!");
        }
        habitat = new Habitat();
        carAi = new CarAi();
        truckAi = new TruckAi();
        Platform.runLater(() -> habitatPane.requestFocus());
        initializeHabitatView();
        initializeMainButtons();
        initializeStatsControls();
        initializeTimeControls();
        initializePControls();
        initializeIntervalControls();
        initializeLifetimeControls();
        initializeAiButtons();

    }

    /**
     * Метод инициализации класса view среды. Передаёт в него компоненты с нетривиальной логикой.
     */
    public void initializeHabitatView() {
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
    public void initializeMainButtons() {
        startButton.setFocusTraversable(false);
        stopButton.setFocusTraversable(false);
        startButton.setDisable(false);
        stopButton.setDisable(true);
        tbEndButton.setDisable(true);
    }

    /**
     * Инициализация компонент управления статистическим окном.
     */
    public void initializeStatsControls() {
        boolean val = Boolean.parseBoolean(properties.getProperty("showStatistics","true"));
        showStatsCheckBox.setFocusTraversable(false);
        showStatsCheckBox.setSelected(val);
        tbShowStatsCheckBox.setSelected(val);
    }

    /**
     * Инициализация компонент управления отображением времени.
     */
    public void initializeTimeControls() {
        boolean val = Boolean.parseBoolean(properties.getProperty("showTime", "true"));
        ToggleGroup timeToggleGroup = new ToggleGroup();
        showTimeRadioButton.setToggleGroup(timeToggleGroup);
        hideTimeRadioButton.setToggleGroup(timeToggleGroup);
        showTimeRadioButton.setFocusTraversable(false);
        hideTimeRadioButton.setFocusTraversable(false);
        if(val){
            showTimeRadioButton.setSelected(true);
            tbShowTimeCheckBox.setSelected(true);

            habitatView.switchTimeLabelVisible();
        }else{
            hideTimeRadioButton.setSelected(true);
            tbShowTimeCheckBox.setSelected(false);
        }
    }

    /**
     * Инициализация choice boxes для вероятности.
     */
    public void initializePControls(){
        for(int i = 0;i<=100;i+=10){
            pCarChoiceBox.getItems().add(i+ "%");
            pTruckChoiceBox.getItems().add(i+ "%");
        }

        String pCar = properties.getProperty("pCar","50");
        String pTruck = properties.getProperty("pTruck","50");

        try {
            int valueCar = Integer.parseInt(pCar);
            if (valueCar >= 0 && valueCar <= 100) {
                pCar = Integer.toString(((valueCar + 5) / 10) * 10);
            } else {
                pCar = "50";
            }
        } catch (NumberFormatException e) {
            pCar = "50";
        }

        try {
            int valueTruck = Integer.parseInt(pTruck);
            if (valueTruck >= 0 && valueTruck <= 100) {
                pTruck = Integer.toString(((valueTruck + 5) / 10) * 10);
            } else {
                pTruck = "50";
            }
        } catch (NumberFormatException e) {
            pTruck = "50";
        }

        pCarChoiceBox.setValue(pCar+"%");
        pTruckChoiceBox.setValue(pTruck+"%");
    }

    /**
     * Инициализация филдов для установки интервалов рождения
     */
    public void initializeIntervalControls(){
        String carInterval = getValidInterval(properties.getProperty("carInterval", "1"));
        String truckInterval = getValidInterval(properties.getProperty("truckInterval", "1"));
        carIntervalTextField.setText(carInterval);
        truckIntervalTextField.setText(truckInterval);
        carIntervalTextField.setOnKeyPressed(this::handleKeyPressed);
        truckIntervalTextField.setOnKeyPressed(this::handleKeyPressed);
    }

    private String getValidInterval(String interval) {
        try {
            int value = Integer.parseInt(interval);
            if (value < 1) {
                return "1";
            } else {
                return Integer.toString(value);
            }
        } catch (NumberFormatException e) {
            return "1";
        }
    }
    /**
     * Инициализация компонент управления временем жизни объеков.
     */
    public void initializeLifetimeControls(){
        String carLifetime = getValidInterval(properties.getProperty("carLifetime", "1"));
        String truckLifetime = getValidInterval(properties.getProperty("truckLifetime", "1"));
        carLifeTimeTextField.setText(carLifetime);
        truckLifeTimeTextField.setText(truckLifetime);
        carLifeTimeTextField.setOnKeyPressed(this::handleKeyPressed);
        truckLifeTimeTextField.setOnKeyPressed(this::handleKeyPressed);
    }


    public Habitat getHabitat() {
        return habitat;
    }

    public HabitatView getHabitatView() {
        return habitatView;
    }

    public void setDefaultFactoryParameters() {
        carIntervalTextField.setText("1");
        truckIntervalTextField.setText("1");
        carLifeTimeTextField.setText("10");
        truckLifeTimeTextField.setText("10");
    }

    /**
     * Метод считывающий данные со всех компонент управления. Формирует фабрику для среды.
     *
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
            int pCar = Integer.parseInt(pCarChoiceBox.getValue().substring(0, pCarChoiceBox.getValue().length() - 1));
            int pTruck = Integer.parseInt(pTruckChoiceBox.getValue().substring(0, pTruckChoiceBox.getValue().length() - 1));
            habitat.setFactoryParameters(pCar, pTruck, carInterval, truckInterval, carLifeTime, truckLifeTime);
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
     *
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
    private void handleStartButton() {
        startSimulation();
    }

    /**
     * Обработчик кнопки Stop.
     */
    @FXML
    private void handleStopButton() {
        stopSimulation();
    }

    /**
     * Обработчик компонент, отвечающий за показ/скрытие статистики.
     *
     * @param event Событие.
     */
    @FXML
    private void handleShowStatsAction(ActionEvent event) {
        Object source = event.getSource();
        boolean isSelected;
        if (source == showStatsCheckBox) {
            isSelected = showStatsCheckBox.isSelected();
        } else {
            isSelected = tbShowStatsCheckBox.isSelected();
        }
        showStatsCheckBox.setSelected(isSelected);
        tbShowStatsCheckBox.setSelected(isSelected);
        habitat.setStatsIsAvailable(isSelected);
    }

    /**
     * Обработчик компонент, отвечающих за показ/скрытие лейбла времени.
     *
     * @param action Событие.
     */
    @FXML
    private void handleShowTimeAction(Event action) {
        habitatView.switchTimeLabelVisible();
        Object source = action.getSource();
        if (source == showTimeRadioButton) {
            tbShowTimeCheckBox.setSelected(showTimeRadioButton.isSelected());
        } else if (source == tbShowTimeCheckBox) {
            showTimeRadioButton.setSelected(tbShowTimeCheckBox.isSelected());
            hideTimeRadioButton.setSelected(!tbShowTimeCheckBox.isSelected());
        } else {
            tbShowTimeCheckBox.setSelected(timeLabel.isVisible());
            showTimeRadioButton.setSelected(timeLabel.isVisible());
            hideTimeRadioButton.setSelected(!timeLabel.isVisible());
        }

    }

    /**
     * Обработчик кнопки показа текущих объектов.
     */
    @FXML
    private void handleCurrentObjectsButton() {
        if (habitat.isRunning()) {
            habitat.pause();
            habitatView.pause();
            StringBuilder sb = new StringBuilder();
            List<Vehicle> vehicleList = VehicleList.getInstance().getVehicles();
            Set<Map.Entry<Integer, Integer>> entrySet = habitat.getBirthTimes().entrySet();
            for (Map.Entry<Integer, Integer> e : entrySet) {
                Vehicle veh = Objects.requireNonNull(vehicleList.stream()
                        .filter(v -> v.getId() == e.getKey())
                        .findFirst()
                        .orElse(null));
                sb.append(veh.getClass().getSimpleName())
                        .append("(")
                        .append(veh.getId())
                        .append("): ").append("born at ")
                        .append(e.getValue()).append(" sec ")
                        .append("and lives for ").append(e.getValue() + veh.getLifeTime())
                        .append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Vehicle Information");
            alert.setHeaderText("Vehicle Details:");
            alert.setContentText(sb.toString());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                        habitatView.resume();
                        habitat.resume();
                    });


        }

    }

    /**
     * Метод, запускающий симуляцию. Либо продолжающий, если она запущена.
     */
    void startSimulation() {
        if (!habitat.isRunning()) {
            if (setFactoryParameters()) {
                habitatView.clear();
                habitat.start();
                habitatView.start();
            }
            habitat.resume();
            habitatView.resume();
            habitatView.switchMainButtons();
        }
    }


    /**
     * Метод остановки симуляции либо ее прерывания.
     */
    void stopSimulation() {
        if (habitat.isRunning()) {
            habitat.pause();
            habitatView.pause();
            habitatView.showStatistics();
            if (!habitat.isStatsIsAvailable()) {
                habitatView.switchMainButtons();
                habitat.clear();
            }
        }

    }

    /**
     * Метод проверки интервалов рождения.
     *
     * @param carBirthInterval   Интервал рождения car.
     * @param truckBirthInterval Интервал рождения truck.
     * @throws IllegalArgumentException Если значения некорректны.
     */
    public void checkTimeIntervals(String carBirthInterval, String truckBirthInterval) throws IllegalArgumentException {
        try {
            int i1 = Integer.parseInt(carBirthInterval);
            int i2 = Integer.parseInt(truckBirthInterval);
            if (i1 <= 0 || i2 <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значения интервалов рождения должны быть натуральными числами!");
        }
    }

    /**
     * Метод проверки времен жизни объектов.
     *
     * @param carLifeTime   Время жизни car.
     * @param truckLifeTime Время жизни truck.
     * @throws IllegalArgumentException Если значения некорректны.
     */
    public void checkLifeTimes(String carLifeTime, String truckLifeTime) throws IllegalArgumentException {
        try {
            int i1 = Integer.parseInt(carLifeTime);
            int i2 = Integer.parseInt(truckLifeTime);
            if (i1 <= 0 || i2 <= 0) {
                throw new NumberFormatException();
            }


        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Значения времени жизни должны быть натуральными числами!");
        }
    }

    @FXML
    public void handleStartCarAiButton() {
        if (!carAi.isRunning()) {
            if (carAi.getExecutorService() == null) {
                carAi.start();
                switchCarAiButtons();
            } else {
                carAi.resume();
                switchCarAiButtons();
            }
        }
    }

    @FXML
    public void handleStopCarAiButton() {
        if (carAi.isRunning()) {
            switchCarAiButtons();
            carAi.pause();
        }
    }

    @FXML
    public void handleStartTruckAiButton() {
        if (!truckAi.isRunning()) {
            if (truckAi.getExecutorService() == null) {
                truckAi.start();
                switchTruckAiButtons();
            } else {
                truckAi.resume();
                switchTruckAiButtons();
            }
        }
    }

    @FXML
    public void handleStopTruckAiButton() {
        if (truckAi.isRunning()) {
            switchTruckAiButtons();
            truckAi.pause();
        }
    }

    public void switchCarAiButtons() {
        if (carAiStartButton.isDisabled()) {
            carAiStartButton.setDisable(false);
            carAiStopButton.setDisable(true);
        } else {
            carAiStopButton.setDisable(false);
            carAiStartButton.setDisable(true);
        }
    }

    public void switchTruckAiButtons() {
        if (truckAiStartButton.isDisabled()) {
            truckAiStartButton.setDisable(false);
            truckAiStopButton.setDisable(true);
        } else {
            truckAiStopButton.setDisable(false);
            truckAiStartButton.setDisable(true);
        }
    }

    public void initializeAiButtons() {
        truckAiStartButton.setDisable(false);
        truckAiStopButton.setDisable(true);
        carAiStartButton.setDisable(false);
        carAiStopButton.setDisable(true);
        truckAiStartButton.setFocusTraversable(false);
        carAiStartButton.setFocusTraversable(false);
        truckAiStopButton.setFocusTraversable(false);
        carAiStopButton.setFocusTraversable(false);
    }

    public void serialize(String path){
        VehicleList.serializeVehicles(path);
        habitat.serialize(path);
    }
    public void deserialize(File habitatFile, File vehicleFile){
        habitatView.pause();
        habitat.close();
        VehicleList.deserializeVehicles(vehicleFile);
        habitat.deserialize(habitatFile);
        habitat.getFactory().setImages();
        setFactoryParameters();
        habitat.start();
        habitatView.start();
        if(!startButton.isDisabled()){
            startButton.setDisable(true);
            stopButton.setDisable(false);
            tbStartButton.setDisable(true);
            tbEndButton.setDisable(false);
        }
    }
    public void openConsole(){
        console = new Console(carAi, truckAi);
        console.setController(this);
        console.start();
    }


    @FXML
    public void handleConsoleButton() {
        if(console==null){
            openConsole();
        }else{
            console.close();
            console = null;
        }
    }
    public void switchCarAiButton(){
        if(carAiStartButton.isDisabled()){
            carAiStartButton.setDisable(false);
            carAiStopButton.setDisable(true);
        }else{
            carAiStartButton.setDisable(true);
            carAiStopButton.setDisable(false);
        }
    }
    public void switchTruckAiButton(){
        if(truckAiStartButton.isDisabled()){
            truckAiStartButton.setDisable(false);
            truckAiStopButton.setDisable(true);
        }else{
            truckAiStartButton.setDisable(true);
            truckAiStopButton.setDisable(false);
        }
    }
    public void close() {
        properties.setProperty("showStatistics", String.valueOf(showStatsCheckBox.isSelected()));
        properties.setProperty("showTime", String.valueOf(showTimeRadioButton.isSelected()));
        properties.setProperty("pCar", pCarChoiceBox.getValue().substring(0, pCarChoiceBox.getValue().length() - 1));
        properties.setProperty("pTruck", pTruckChoiceBox.getValue().substring(0, pCarChoiceBox.getValue().length() - 1));
        properties.setProperty("carInterval", getValidInterval(carIntervalTextField.getText()));
        properties.setProperty("truckInterval", getValidInterval(truckIntervalTextField.getText()));
        properties.setProperty("carLifetime", getValidInterval(carLifeTimeTextField.getText()));
        properties.setProperty("truckLifetime", getValidInterval(truckLifeTimeTextField.getText()));

        try {
            // Запись изменений в файл
            FileOutputStream fileOut = new FileOutputStream("src/main/resources/ru/matthew8913/simulation/configs/simulation.properties");
            properties.store(fileOut, "Updated properties");
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
