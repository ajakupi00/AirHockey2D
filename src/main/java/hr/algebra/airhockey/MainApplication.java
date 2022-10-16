package hr.algebra.airhockey;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    private static Stage mainStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("AirHockey");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return mainStage;
    }
    public static Scene getMainScene(){return mainScene;}
    public static void setMainScene(final Scene scene){
        mainScene = scene;
    }

    public static void main(String[] args) {
        launch();
    }
}