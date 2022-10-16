package hr.algebra.airhockey.models;

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
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    public static Scene currentScene;
    private Player redPlayer;
    @FXML
    private TableView tableLeaderboard;
    @FXML
    private Button btnNewGame;


    public void setPlayers(final Player redPlayer) {
        this.redPlayer = redPlayer;
        this.redPlayer.setScore(calculatePlayerStats(redPlayer));
    }
    public void initStats(){
        final ObservableList<Player> data = FXCollections.observableArrayList(
                redPlayer, redPlayer
        );
        tableLeaderboard.setItems(data);
    }

    private int calculatePlayerStats(final Player player) {
        int n = player.getScore() + (5 - player.getGoalsConceived()) * 5 * (1 + player.getGoals()); //TODO: replace getGoals() with spaceBarGoals!!
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
                new PropertyValueFactory<RedPlayer,String>("name")
        );
        TableColumn scoreColumn = new TableColumn("Score");
        scoreColumn.setPrefWidth(200);
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<Player,Integer>("score")
        );
        TableColumn spaceColumn = new TableColumn("boostGoals");
        spaceColumn.setPrefWidth(200);
        spaceColumn.setCellValueFactory(
                new PropertyValueFactory<Player,Integer>("boostGoals")
        );


        tableLeaderboard.getColumns().addAll(nameColumn, scoreColumn, spaceColumn);



    }

    public void showMovesScene(final Player player) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("moves.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MovesController movesController = fxmlLoader.getController();
        movesController.setPlayer(player);
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
