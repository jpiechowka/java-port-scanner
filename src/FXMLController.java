import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.Inet4Address;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class FXMLController implements Initializable
{
    //HOST DISCOVERY TAB
    @FXML private Button hostDiscoveryButton;
    @FXML private Slider hostDiscoveryThreadsSlider;
    @FXML private Slider hostDiscoveryTimeoutSlider;
    @FXML private Label hostDiscoveryThreadsSliderLabel;
    @FXML private Label hostDiscoveryTimeoutSliderLabel;
    @FXML private TableView<HostDiscoveryResult> hostDiscoveryResultsTable;
    @FXML private TableColumn<HostDiscoveryResult, String> hostDiscoveryResultIPTableColumn;
    @FXML private TableColumn<HostDiscoveryResult, String> hostDiscoveryResultHostnameTableColumn;

    //PORT SCAN TAB
    @FXML private Button portScanButton;
    @FXML private Slider portScanThreadsSlider;
    @FXML private Slider portScanTimeoutSlider;
    @FXML private Label portScanThreadsSliderLabel;
    @FXML private Label portScanTimeoutSliderLabel;
    @FXML private TextField portScanIPAddressTextField;
    @FXML private TextField portScanStartPortTextField;
    @FXML private TextField portScanEndPortTextField;
    @FXML private TableView<PortScanResult> portScanResultsTable;
    @FXML private TableColumn<PortScanResult, Integer> portScanResultPortNumberTableColumn;
    @FXML private TableColumn<PortScanResult, String> portScanResultDescriptionTableColumn;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources)
    {
        //HOST DISCOVERY TAB
        assert hostDiscoveryButton != null : "fx:id=\"hostDiscoveryButton\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryThreadsSlider != null : "fx:id=\"hostDiscoveryThreadsSlider\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryTimeoutSlider != null : "fx:id=\"hostDiscoveryTimeoutSlider\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryThreadsSliderLabel != null : "fx:id=\"hostDiscoveryThreadsSliderLabel\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryTimeoutSliderLabel != null : "fx:id=\"hostDiscoveryTimeoutSliderLabel\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryResultsTable != null : "fx:id=\"hostDiscoveryResultsTable\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryResultIPTableColumn != null : "fx:id=\"hostDiscoveryResultIPTableColumn\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert hostDiscoveryResultHostnameTableColumn != null : "fx:id=\"hostDiscoveryResultHostnameTableColumn\" was not injected: check your FXML file 'PortScanner.fxml'.";

        //PORT SCAN TAB
        assert portScanButton != null : "fx:id=\"portScanButton\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanThreadsSlider != null : "fx:id=\"portScanThreadsSlider\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanTimeoutSlider != null : "fx:id=\"portScanTimeoutSlider\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanThreadsSliderLabel != null : "fx:id=\"portScanThreadsSliderLabel\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanTimeoutSliderLabel != null : "fx:id=\"portScanTimeoutSliderLabel\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanIPAddressTextField != null : "fx:id=\"portScanIPAddressTextField\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanStartPortTextField != null : "fx:id=\"portScanStartPortTextField\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanEndPortTextField != null : "fx:id=\"portScanEndPortTextField\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanResultsTable != null : "fx:id=\"portScanResultsTable\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanResultPortNumberTableColumn != null : "fx:id=\"portScanResultPortNumberTableColumn\" was not injected: check your FXML file 'PortScanner.fxml'.";
        assert portScanResultDescriptionTableColumn != null : "fx:id=\"portScanResultDescriptionTableColumn\" was not injected: check your FXML file 'PortScanner.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected

        //TABLE ALIGNMENT
        hostDiscoveryResultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hostDiscoveryResultIPTableColumn.setStyle("-fx-alignment: CENTER;");
        hostDiscoveryResultHostnameTableColumn.setStyle("-fx-alignment: CENTER;");
        portScanResultsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        portScanResultPortNumberTableColumn.setStyle("-fx-alignment: CENTER;");
        portScanResultDescriptionTableColumn.setStyle("-fx-alignment: CENTER;");

        //HANDLE SLIDERS AND SLIDER LABELS
        hostDiscoveryThreadsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hostDiscoveryThreadsSliderLabel.textProperty().setValue(String.valueOf((int) hostDiscoveryThreadsSlider.getValue()));
        });

        hostDiscoveryTimeoutSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            hostDiscoveryTimeoutSliderLabel.textProperty().setValue(String.valueOf((int) hostDiscoveryTimeoutSlider.getValue()));
        });

        portScanThreadsSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            portScanThreadsSliderLabel.textProperty().setValue(String.valueOf((int) portScanThreadsSlider.getValue()));
        });

        portScanTimeoutSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            portScanTimeoutSliderLabel.textProperty().setValue(String.valueOf((int) portScanTimeoutSlider.getValue()));
        });


        //HANDLE BUTTON ACTIONS
        //HOST DISCOVERY BUTTON
        hostDiscoveryButton.setOnAction(event -> {
            Task<Void> hostDiscoveryTask = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    final int hostDiscoveryThreads = (int) hostDiscoveryThreadsSlider.getValue();
                    final int hostDiscoveryTimeout = (int) hostDiscoveryTimeoutSlider.getValue();

                    //Setup columns
                    hostDiscoveryResultIPTableColumn.setCellValueFactory(new PropertyValueFactory<HostDiscoveryResult, String>("IPAddress"));
                    hostDiscoveryResultHostnameTableColumn.setCellValueFactory(new PropertyValueFactory<HostDiscoveryResult, String>("Hostname"));
                    //Clear table every time host discovery is started
                    hostDiscoveryResultsTable.getItems().clear();

                    final ExecutorService exService = Executors.newFixedThreadPool(hostDiscoveryThreads);
                    final List<Future<HostDiscoveryResult>> futures = new ArrayList<>();

                    //Get subnet
                    String localNetwork = Inet4Address.getLocalHost().getHostAddress();
                    localNetwork = localNetwork.substring(0, localNetwork.lastIndexOf('.') + 1);

                    for (int i = 1; i < 255; i++)
                    {
                        StringBuilder currentIPAddress = new StringBuilder().append(localNetwork).append(i);
                        HostDiscovery dHost = new HostDiscovery(currentIPAddress.toString(), hostDiscoveryTimeout);
                        futures.add(dHost.multithreadedHostDicovery(exService));
                    }

                    try
                    {
                        exService.shutdown();
                        exService.awaitTermination(hostDiscoveryTimeout, TimeUnit.MILLISECONDS);
                    } catch (Exception e)
                    {
                        System.out.println(e);
                    }

                    for (final Future<HostDiscoveryResult> i : futures)
                    {
                        try
                        {
                            if (i.get().isOnline())
                            {
                                hostDiscoveryResultsTable.getItems().add(i.get());
                            }
                        } catch (Exception e)
                        {
                            System.out.println(e);
                        }
                    }
                    hostDiscoveryEnableControls();
                    return null;
                }
            };
            Thread hostDiscoveryBackgroundThread = new Thread(hostDiscoveryTask);
            hostDiscoveryDisableControls();
            hostDiscoveryBackgroundThread.setDaemon(true);
            hostDiscoveryBackgroundThread.start();
        });

        //PORT SCAN BUTTON
        portScanButton.setOnAction(event -> {
            Task<Void> portScanTask = new Task<Void>()
            {
                @Override
                protected Void call() throws Exception
                {
                    final int portScanThreads = (int) portScanThreadsSlider.getValue();
                    final int portScanTimeout = (int) portScanTimeoutSlider.getValue();
                    final Pattern ipValidPattern = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
                    final Pattern portNumberValidPattern = Pattern.compile("[0-9]*");

                    //Check if starting and ending port text fields are empty
                    boolean startPortTextFieldEmpty = false;
                    boolean endPortTextFieldEmpty = false;
                    if ((portScanStartPortTextField.getText() == null) || (portScanStartPortTextField.getText().trim().isEmpty())) { startPortTextFieldEmpty = true; }
                    if ((portScanEndPortTextField.getText() == null) || (portScanEndPortTextField.getText().trim().isEmpty())) { endPortTextFieldEmpty = true; }

                    //Check if entered target IP is valid
                    if(!ipValidPattern.matcher(portScanIPAddressTextField.getText()).matches())
                    {
                        portScanIPAddressTextField.setText("INVALID IP ADDRESS");
                        portScanEnableControls();
                        return null;
                    }

                    //Check if starting port is valid
                    if(!portNumberValidPattern.matcher(portScanStartPortTextField.getText()).matches() || startPortTextFieldEmpty)
                    {
                        portScanStartPortTextField.setText("INVALID PORT NUMBER");
                        portScanEnableControls();
                        return null;
                    }
                    else if ((Integer.parseInt(portScanStartPortTextField.getText()) < 1) || (Integer.parseInt(portScanStartPortTextField.getText()) > 65534))
                    {
                        portScanStartPortTextField.setText("INVALID PORT RANGE (1 - 65534)");
                        portScanEnableControls();
                        return null;
                    }

                    //Check if ending port is valid
                    if(!portNumberValidPattern.matcher(portScanEndPortTextField.getText()).matches() || endPortTextFieldEmpty)
                    {
                        portScanEndPortTextField.setText("INVALID PORT NUMBER");
                        portScanEnableControls();
                        return null;
                    }
                    else if ((Integer.parseInt(portScanEndPortTextField.getText()) < 2) || (Integer.parseInt(portScanEndPortTextField.getText()) > 65535))
                    {
                        portScanEndPortTextField.setText("INVALID PORT RANGE (2 - 65535)");
                        portScanEnableControls();
                        return null;
                    }

                    //Check if starting port is smaller than ending port
                    if ((Integer.parseInt(portScanStartPortTextField.getText()) >= Integer.parseInt(portScanEndPortTextField.getText())))
                    {
                        portScanStartPortTextField.setText("GREATER THAN ENDING PORT");
                        portScanEndPortTextField.setText("SMALLER THAN STARTING PORT");
                        portScanEnableControls();
                        return null;
                    }

                    final String targetIP = portScanIPAddressTextField.getText();
                    final int startingPort = Integer.parseInt(portScanStartPortTextField.getText());
                    final int endingPort = Integer.parseInt(portScanEndPortTextField.getText());

                    //Setup columns
                    portScanResultPortNumberTableColumn.setCellValueFactory(new PropertyValueFactory<PortScanResult, Integer>("port"));
                    portScanResultDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory<PortScanResult, String>("description"));
                    //Clear table every time scan is started
                    portScanResultsTable.getItems().clear();

                    final ExecutorService exService = Executors.newFixedThreadPool(portScanThreads);
                    final List<Future<PortScanResult>> futures = new ArrayList<>();

                    for (int i = startingPort; i <= endingPort; i++)
                    {
                        PortScanner pScan = new PortScanner(targetIP, i, portScanTimeout);
                        futures.add(pScan.multithreadedScan(exService));
                    }

                    try
                    {
                        exService.shutdown();
                        exService.awaitTermination(portScanTimeout, TimeUnit.MILLISECONDS);
                    } catch (Exception e)
                    {
                        System.out.println(e);
                    }

                    for (final Future<PortScanResult> i : futures)
                    {
                        try
                        {
                            if (i.get().getIsOpen())
                            {
                                portScanResultsTable.getItems().add(i.get());
                            }
                        } catch (Exception e)
                        {
                            System.out.println(e);
                        }
                    }
                    portScanEnableControls();
                    return null;
                }
            };
            Thread portScanBackgroundThread = new Thread(portScanTask);
            portScanDisableControls();
            portScanBackgroundThread.setDaemon(true);
            portScanBackgroundThread.start();
        });
    }

    //HELPER METHODS USED TO CHANGE CONTROLS STATE
    public void hostDiscoveryDisableControls()
    {
        hostDiscoveryButton.setDisable(true);
        hostDiscoveryButton.setText("WORKING");
        hostDiscoveryThreadsSlider.setDisable(true);
        hostDiscoveryTimeoutSlider.setDisable(true);
    }
    public void hostDiscoveryEnableControls()
    {
        Platform.runLater(() -> hostDiscoveryButton.setDisable(false));
        Platform.runLater(() -> hostDiscoveryButton.setText("Restart Host Discovery"));
        Platform.runLater(() -> hostDiscoveryThreadsSlider.setDisable(false));
        Platform.runLater(() -> hostDiscoveryTimeoutSlider.setDisable(false));
    }
    public void portScanDisableControls()
    {
        portScanButton.setDisable(true);
        portScanButton.setText("SCANNING");
        portScanThreadsSlider.setDisable(true);
        portScanTimeoutSlider.setDisable(true);
        portScanIPAddressTextField.setDisable(true);
        portScanStartPortTextField.setDisable(true);
        portScanEndPortTextField.setDisable(true);
    }
    public void portScanEnableControls()
    {
        Platform.runLater(() -> portScanButton.setDisable(false));
        Platform.runLater(() -> portScanButton.setText("Restart Port Scan"));
        Platform.runLater(() -> portScanThreadsSlider.setDisable(false));
        Platform.runLater(() -> portScanTimeoutSlider.setDisable(false));
        Platform.runLater(() -> portScanIPAddressTextField.setDisable(false));
        Platform.runLater(() -> portScanStartPortTextField.setDisable(false));
        Platform.runLater(() -> portScanEndPortTextField.setDisable(false));
    }
}

