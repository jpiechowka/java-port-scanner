import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application
{
    public static void main (String []args)
    { Application.launch(Main.class, (java.lang.String[])null); }

    @Override
    public void start(Stage primaryStage)
    {
        try
        {
            TabPane page = FXMLLoader.load(Main.class.getResource("PortScanner.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Java Port Scanner");
            primaryStage.setResizable(false);
            primaryStage.show();
        }catch (Exception e)
        {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
