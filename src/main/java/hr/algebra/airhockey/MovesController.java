package hr.algebra.airhockey;

import hr.algebra.airhockey.models.LeaderboardController;
import hr.algebra.airhockey.models.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class MovesController {

    @FXML
    private Label labelPlayerName;
    @FXML
    private Label lblGoals;
    @FXML
    private Label lblGoalsConceived;
    @FXML
    private Label lblWins;
    @FXML
    private Label lblLosses;
    @FXML
    private Label lblBoostGoals;
    @FXML
    private Label lblTotalScore;

    private Player player;

    public void setPlayer(final Player player){
        this.player = player;
        labelPlayerName.setText(player.getName());
        lblGoals.setText(String.valueOf(player.getGoals()));
        lblGoalsConceived.setText(String.valueOf(player.getGoalsConceived()));
        lblBoostGoals.setText(String.valueOf(player.getBoostGoals()));
        lblTotalScore.setText(String.valueOf(player.getScore()));
    }

    public void returnClick(ActionEvent actionEvent) {
        Scene scene = null;
        scene = LeaderboardController.currentScene;
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
    }
}
