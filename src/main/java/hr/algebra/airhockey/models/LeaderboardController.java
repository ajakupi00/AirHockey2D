package hr.algebra.airhockey.models;

import hr.algebra.airhockey.GameScreenController;
import hr.algebra.airhockey.MainApplication;
import hr.algebra.airhockey.MovesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    private Player redPlayer;
    private Player bluePlayer;

    @FXML
    private TableView tableLeaderboard;
    @FXML
    private Button btnNewGame;


    public void setPlayers(final Player redPlayer, final Player bluePlayer) {
        //TODO: load all player that ever played
        this.redPlayer = redPlayer;
        this.bluePlayer = bluePlayer;
        this.redPlayer.setScore(calculatePlayerStats(redPlayer));
        this.bluePlayer.setScore(calculatePlayerStats(bluePlayer));
    }
    public void initStats(){
        final ObservableList<Player> data = FXCollections.observableArrayList(
                redPlayer, bluePlayer
        );
        tableLeaderboard.setItems(data);
    }

    private int calculatePlayerStats(final Player player) {
        int n = player.getScore() + (Math.abs(player.getGoals() - player.getGoalsConceived()) + player.getBoostGoals() * 2);
        return n;
    }



    public void startNewGame(MouseEvent mouseEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("login.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableLeaderboard.setEditable(false);
        TableColumn nameColumn = new TableColumn("Player name");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Player,String>("name")
        );
        TableColumn typeColumn = new TableColumn("Color");
        typeColumn.setPrefWidth(150);
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<Player,String>("type")
        );
        TableColumn scoreColumn = new TableColumn("Score");
        scoreColumn.setPrefWidth(200);
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<Player,Integer>("score")
        );
        TableColumn spaceColumn = new TableColumn("boostGoals");
        spaceColumn.setPrefWidth(150);
        spaceColumn.setCellValueFactory(
                new PropertyValueFactory<Player,Integer>("boostGoals")
        );


        tableLeaderboard.getColumns().addAll(nameColumn, typeColumn,scoreColumn, spaceColumn);



    }

    public void showMovesScene(final Player player) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("moves.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), MovesController.SCENE_WIDTH, MovesController.SCENE_HEIGHT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MovesController movesController = fxmlLoader.getController();
        ArrayList<Move> playerMoves;
        if(player.getType() == PlayerType.RED)
            playerMoves = GameScreenController.redPlayerMoves;
        else
            playerMoves = GameScreenController.bluePlayerMoves;

        movesController.setPlayer(player, playerMoves);
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
    }

    public void rowMouseClick(MouseEvent mouseEvent) {
        if(tableLeaderboard.getSelectionModel().getSelectedItem() != null && mouseEvent.getClickCount() == 2){
           showMovesScene((Player) tableLeaderboard.getSelectionModel().getSelectedItem());
        }
    }
}
