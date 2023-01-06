package hr.algebra.airhockey;

import hr.algebra.airhockey.Threads.ClientThread;
import hr.algebra.airhockey.message.LoginMessage;
import hr.algebra.airhockey.models.GameStateDto;
import hr.algebra.airhockey.server.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController implements Initializable {
    private String playerId;
    private String username;

    @FXML
    private RadioButton redRadioButton;
    @FXML
    private RadioButton blueRadioButton;
    @FXML
    private TextField playerTextField;
    @FXML
    private Button btnStartGame;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup radioButtonGroup = new ToggleGroup();
        redRadioButton.setToggleGroup(radioButtonGroup);
        blueRadioButton.setToggleGroup(radioButtonGroup);
    }

    public void startGame(ActionEvent actionEvent) throws IOException {
        Boolean gameReady = checkInputs();
        if(gameReady){

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(new ClientThread(new GameStateDto()));
            Server.configureProperties();

            try (Socket clientSocket = new Socket(Server.HOST, Server.PORT)){
                System.err.println("Client is connecting to " + clientSocket.getInetAddress() +
                        ":" +clientSocket.getPort());

                ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                oos.writeObject(new LoginMessage(playerTextField.getText(), playerTextField.getText()));
                System.out.println("Client sent message back to the server!");

            } catch (IOException e) {
                e.printStackTrace();
            }

            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("gameScreen.fxml"));
          Parent root = fxmlLoader.load();
          GameScreenController controller = fxmlLoader.getController();
          GameScreenController.redPlayerMoves.clear();
          GameScreenController.bluePlayerMoves.clear();
          Scene scene = null;
          scene = new Scene(root, 748, 731);
          MainApplication.setMainScene(scene);
          controller.initGame(playerTextField.getText().trim().toString(), "todo");
          MainApplication.getStage().setTitle("AirHockey");
          MainApplication.getStage().setScene(scene);
          MainApplication.getStage().show();

        }
    }

    private Boolean checkInputs() {
      boolean ok = true;
      if(playerTextField.getText().isEmpty()) {
          ok = false;
          playerTextField.setPromptText("Enter players name!");
      }
      if(!redRadioButton.isSelected() && !blueRadioButton.isSelected())
          ok = false;
      return ok;
    }


    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}