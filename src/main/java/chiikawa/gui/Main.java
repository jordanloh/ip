package chiikawa.gui;

import java.io.IOException;

import chiikawa.Chiikawa;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class for setting up Application.
 */
public class Main extends Application {

    private Chiikawa chiikawa = new Chiikawa();
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setTitle("Chiikawa Chatbot");
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setChiikawa(chiikawa); // inject the Duke instance
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

