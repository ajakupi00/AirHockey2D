package hr.algebra.airhockey;

import hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils;
import hr.algebra.airhockey.models.GoalType;
import hr.algebra.airhockey.models.Move;
import hr.algebra.airhockey.models.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MovesController implements Initializable {
    public static int SCENE_WIDTH = 725;
    public static int SCENE_HEIGHT = 504;
    /*
    *   This should load the moves & player stats
    *   A Move is when on what second was a goal scored
    */

    @FXML
    private Label labelPlayerName;
    @FXML
    private TableView tblGoals;
    @FXML
    private Label lblWins;
    @FXML
    private Label lblLost;
    @FXML
    private Label lblScored;
    @FXML
    private Label lblConceived;
    @FXML
    private Label lblBoost;
    @FXML
    private Label lblPlayerColor;

    private Player player;
    private ObservableList<Move> playerMoves;


    public void setPlayer(final Player player, final ArrayList<Move> lastGamePlayerMoves){
        this.player = player;
        this.playerMoves =  FXCollections.observableArrayList(lastGamePlayerMoves);
        tblGoals.setItems(playerMoves);
        manageLabels();
        /*
        * BROJ      PLAYER1     PLAYER2
        *  5          YES         NO
        * */


    }

    private void manageLabels() {
        labelPlayerName.setText(player.getName());
        lblWins.setText(Integer.toString(player.getWins()));
        lblLost.setText(Integer.toString(player.getLost()));
        lblScored.setText(Integer.toString(player.getGoals()));
        lblConceived.setText(Integer.toString(player.getGoalsConceived()));
        lblBoost.setText(Integer.toString(player.getBoostGoals()));
        lblPlayerColor.setText(player.getType().toString());
    }

    public void returnClick(ActionEvent actionEvent) {
        Scene scene = null;
        scene =  GameUtils.currentScene;
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblGoals.setEditable(false);
        TableColumn ColumnGameSeconds = new TableColumn("Goal in second");
        ColumnGameSeconds.setPrefWidth(150);
        ColumnGameSeconds.setCellValueFactory(
                new PropertyValueFactory<Move,Integer>("gameSecond")
        );

        TableColumn ColumnGoalType = new TableColumn("Type of Goal");
        ColumnGoalType.setPrefWidth(220);
        ColumnGoalType.setCellValueFactory(
                new PropertyValueFactory<Move, GoalType>("goalType")
        );
        tblGoals.getColumns().addAll(ColumnGameSeconds, ColumnGoalType);


    }
}
