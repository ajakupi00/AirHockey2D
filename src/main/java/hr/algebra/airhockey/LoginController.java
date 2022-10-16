package hr.algebra.airhockey;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField redPlayerTextField;
    @FXML
    private TextField bluePlayerTextField;
    @FXML
    private Button btnStartGame;

    private ArrayList<TextField> inputFields;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
      inputFields = new ArrayList<>();
      inputFields.add(redPlayerTextField);
      inputFields.add(bluePlayerTextField);
    }

    public void startGame(ActionEvent actionEvent) {
        Boolean gameReady = checkInputs();
        if(gameReady){
          FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gameScreen.fxml"));
          Parent root;
          try {
             root = fxmlLoader.load();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          GameScreenController controller = fxmlLoader.getController();
          Scene scene = null;
          scene = new Scene(root, 480, 700);
          MainApplication.setMainScene(scene);
          controller.initGame(redPlayerTextField.getText().trim().toString(), bluePlayerTextField.getText().trim().toString());
          MainApplication.getStage().setTitle("AirHockey");
          MainApplication.getStage().setScene(scene);
          MainApplication.getStage().show();
        }
    }

    private Boolean checkInputs() {
      for (TextField tf: inputFields) {
        if(tf.getText().isBlank()){
          tf.setPromptText("Enter players name");
          return false;
        }
      }
      return true;
    }


}