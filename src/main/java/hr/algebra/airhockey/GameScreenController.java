package hr.algebra.airhockey;

import hr.algebra.airhockey.Threads.ClientChatThread;
import hr.algebra.airhockey.hr.algebra.airhockey.utils.GameUtils;
import hr.algebra.airhockey.hr.algebra.airhockey.utils.XMLUtility;
import hr.algebra.airhockey.models.*;
import hr.algebra.airhockey.models.SerializableActor.SerializablePlayer;
import hr.algebra.airhockey.models.SerializableActor.SerializablePuck;
import hr.algebra.airhockey.rmiserver.ChatService;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.apache.commons.lang3.SerializationUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class GameScreenController{
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Rectangle redGoalRectangle;
    @FXML
    private Rectangle blueGoalRectangle;
    @FXML
    private Label redGoalsLabel;
    @FXML
    private Label blueGoalsLabel;
    @FXML
    private Label lblGameSeconds;
    @FXML
    private TextArea chatHistoryTextArea;
    @FXML
    private TextField chatMessageTextField;

    private int secondsLeft = GameUtils.GAME_DURATIONS_SECONDS;
    public static ArrayList<Move> redPlayerMoves = new ArrayList<Move>();
    public static ArrayList<Move> bluePlayerMoves = new ArrayList<Move>();
    private Player redPlayer;
    private Player bluePlayer;
    private Puck puck;
    private boolean winnerDefined = false;
    private ChatService stub = null;

    private Document xmlDocument;
    private List<Game> gameMoves = new ArrayList<>();

    //TIMERS
    private AnimationTimer collisionTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            checkPuckPlayerCollision(redPlayer.getCircle(), bluePlayer.getCircle(), puck);
        }
    };

    private AnimationTimer puckMovementTimer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if(puck.getLayoutY() <= 15 + puck.getRadius() || puck.getLayoutY() >= GameUtils.SCENE_HEIGHT - puck.getRadius()){
                puck.setyDirection((byte) (puck.getyDirection() * -1));
                puck.speedUp();
                if(redGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    if(bluePlayer.getBoostPressed()){
                        bluePlayer.scoredGoal(true);
                    }else{
                        bluePlayer.scoredGoal(false);
                    }
                    redPlayer.goalConceived();
                    manageMoves(bluePlayer, redPlayer);
                    blueGoalsLabel.setText(Integer.toString(bluePlayer.getGoals()));
                    puck.reset();
                    puckMovementTimer.stop();
                    puck.setFirstTouch(false);
                }else if(blueGoalRectangle.getBoundsInParent().intersects(puck.getBoundsInParent())){
                    if(redPlayer.getBoostPressed()){
                        redPlayer.scoredGoal(true);
                    }else{
                        redPlayer.scoredGoal(false);
                    }
                    bluePlayer.goalConceived();
                    manageMoves(redPlayer, bluePlayer);
                    redGoalsLabel.setText(Integer.toString(redPlayer.getGoals()));
                    puck.reset();
                    puckMovementTimer.stop();
                    puck.setFirstTouch(false);
                }
            }
            puck.setLayoutY(puck.getLayoutY() + (puck.getSpeed() * puck.getyDirection()));

            if(puck.getLayoutX() <= 0 + puck.getRadius() || puck.getLayoutX() >= GameUtils.SCENE_WIDTH - puck.getRadius()){
                //puck.slowDown();
                puck.setxDirection((byte) (puck.getxDirection() * -1));
            }
            puck.setLayoutX(puck.getLayoutX() + (puck.getSpeed() * puck.getxDirection()));
        }
    };




    //BEFORE GAMEPLAY METHODS
    public void initGame(final String redPlayerName, final String bluePlayerName) {
        redGoalsLabel.setText("0");
        blueGoalsLabel.setText("0");

        Puck puck = new Puck(15, Color.BLACK, GameUtils.SCENE_WIDTH / 2, GameUtils.SCENE_HEIGHT / 2);
        this.puck = puck;
        anchorPane.getChildren().add(puck);
        initializePlayer(redPlayerName, bluePlayerName);
        initializeXMLDocument();
        Registry registry = null;
        try {
            registry = LocateRegistry.getRegistry("localhost", 1099);
            //new Thread(() -> listenToUDPRequests()).start();
            new Thread(() -> writeXML()).start();
            //new Thread(() ->  refreshMsg());
            stub = (ChatService) registry.lookup(ChatService.REMOTE_OBJECT_NAME);
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.execute(new ClientChatThread(stub, chatHistoryTextArea));


        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void initializeXMLDocument() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            this.xmlDocument = documentBuilder.newDocument();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

    }

    private void writeXML() {
       while (!winnerDefined){
           try {
               gameMoves.add(getGame());
               Thread.sleep(100);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
    }
    private void listenToUDPRequests() {
        try (DatagramSocket socket = new DatagramSocket(0)) {
            System.err.println("Client socket started...");
            socket.setSoTimeout(10000);
            InetAddress host = InetAddress.getLocalHost();
            while (true) {
                try {
                    Thread.sleep(2000);
                    System.err.println("Client sending game object...");
                    Game saveGame = getGame();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    SerializationUtils.serialize(saveGame, baos);
                    baos.flush();
                    byte[] data = baos.toByteArray();

                    DatagramPacket request = new DatagramPacket(data, data.length, host, 5001);
                    DatagramPacket response = new DatagramPacket(data, data.length);
                    socket.send(request);
                    socket.receive(response);
                    Game game = Game.bytesToGame(response.getData());
                    System.err.println("Received a game object from server. :)");


                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void refreshMsg(){
        while (true){
            try{
                Thread.sleep(1500);
                chatHistoryTextArea.clear();
                StringBuilder chatHistoryBuilder = new StringBuilder();
                stub.getChatHistory().forEach(a -> chatHistoryBuilder.append(a + "\n"));
                chatHistoryTextArea.setText(chatHistoryBuilder.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();        }
        }
        }




        private void initializePlayer(final String redPlayerName, final String bluePlayerName){
        //CREATE RED PLAYER
        Player redPlayer = new Player(redPlayerName, MainApplication.getMainScene(), PlayerType.RED, GameUtils.wasd);
        anchorPane.getChildren().add(redPlayer.getCircle());
        redPlayer.sceneKeyboardMovementSetup();
        redPlayer.initPlayerTimer();
        this.redPlayer = redPlayer;

        //CREATE BLUE PLAYER
        Player bluePlayer = new Player(bluePlayerName, MainApplication.getMainScene(), PlayerType.BLUE, GameUtils.arrows);
        anchorPane.getChildren().add(bluePlayer.getCircle());
        bluePlayer.sceneKeyboardMovementSetup();
        bluePlayer.initPlayerTimer();
        this.bluePlayer = bluePlayer;

        collisionTimer.start();



    }


    //DURING GAMEPLAY METHODS
    private void checkPuckPlayerCollision(Circle redPlayerCircle, Circle bluePlayerCircle, Puck puck) {
        if(puck.getBoundsInParent().intersects(redPlayerCircle.getBoundsInParent())){
            //puck.slowDown();
            puckDefineDirection(redPlayer);
            if(!puck.getFirstTouch()){
                puckMovementTimer.start();
                startGameTimer();
                puck.setFirstTouch(true);
            }
        }else if(puck.getBoundsInParent().intersects(bluePlayerCircle.getBoundsInParent())){
            //puck.slowDown();
            puckDefineDirection(bluePlayer);
            if(!puck.getFirstTouch()){
                puckMovementTimer.start();
                startGameTimer();
                puck.setFirstTouch(true);
            }
        }
    }
    private void startGameTimer() {
        Timeline gameTime = new Timeline();
        gameTime.setCycleCount(Timeline.INDEFINITE);
        KeyFrame perSecondKeyFrame = new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                secondsLeft--;
                lblGameSeconds.setText(String.valueOf(secondsLeft));
                if(secondsLeft <= 0){
                    gameTime.stop();
                    collisionTimer.stop();
                    puckMovementTimer.stop();
                    loadLeaderboard();
                }
            }
        });
        gameTime.getKeyFrames().add(perSecondKeyFrame);
        gameTime.play();
    }
    private void puckDefineDirection(final Player player) {
        if(player.getBoostPressed()){
            puck.speedUp();
        }
        if(player.getUpPressed()){
            this.puck.setyDirection((byte) -1);
        }
        if(player.getDownPressed()){
            this.puck.setyDirection((byte) 1);
        }
        if(player.getLeftPressed()){
            this.puck.setxDirection((byte) -1);
        }
        if(player.getRightPressed()){
            this.puck.setxDirection((byte) 1);
        }
        this.puck.updatePosition();
    }
    private void manageMoves(final Player playerScored, final Player playerConceived) {
        int gameSecond = secondsLeft;
        if(playerScored.getType() == PlayerType.RED){
            GoalType goalType = playerScored.isLastGoalIsBoost() ? GoalType.BOOST : GoalType.NORMAL;
            redPlayerMoves.add(new Move(gameSecond, goalType));
            bluePlayerMoves.add(new Move(gameSecond, GoalType.CONCEIVED));
        }else{
            GoalType goalType = playerScored.isLastGoalIsBoost() ? GoalType.BOOST : GoalType.NORMAL;
            bluePlayerMoves.add(new Move(gameSecond, goalType));
            redPlayerMoves.add(new Move(gameSecond, GoalType.CONCEIVED));
        }
    }
    public void sendMessage(){
        try {
            stub.sendMessage(chatMessageTextField.getText(), redPlayer.getName());
            StringBuilder chatHistoryBuilder = new StringBuilder();
            stub.getChatHistory().forEach(a -> chatHistoryBuilder.append(a + "\n"));
            chatHistoryTextArea.setText(chatHistoryBuilder.toString());
            chatMessageTextField.clear();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    //AFTER GAMEPLAY METHODS

    private void defineWinnerAndLoser() {
        if(!winnerDefined){
            int redGoals = Integer.parseInt(redGoalsLabel.getText());
            int blueGoals = Integer.parseInt(blueGoalsLabel.getText());
            if(redGoals > blueGoals){
                redPlayer.playerWon();
                bluePlayer.playerLost();
            }else if(blueGoals > redGoals){
                bluePlayer.playerWon();
                redPlayer.playerLost();
            }else { //TODO: DRAW
                redPlayer.playerLost();
                bluePlayer.playerLost();
            }
            winnerDefined = true;
        }

    }
    private void loadLeaderboard() {
        saveXML();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("leaderboard.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 700);
        } catch (IOException e) {
            e.printStackTrace();
        }
        defineWinnerAndLoser();
        LeaderboardController leaderboardController = fxmlLoader.getController();
        leaderboardController.setPlayers(redPlayer, bluePlayer);        //TODO: set BLUE PLAYER
        leaderboardController.initStats();
        MainApplication.getStage().setTitle("AirHockey");
        MainApplication.getStage().setScene(scene);
        MainApplication.getStage().show();
        GameUtils.currentScene = MainApplication.getStage().getScene();

    }

    private void saveXML() {
        try {
            XMLUtility.createGameMovesElement(gameMoves, xmlDocument);

            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            Source xmlSource = new DOMSource(this.xmlDocument);
            Result xmlResult = new StreamResult(new File("gameplay.xml"));

            transformer.transform(xmlSource, xmlResult);
            System.err.println("XML CREATED :) ---------");
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }

    }


    public void replayGame(ActionEvent actionEvent){
        try{
            puckMovementTimer.stop();
            collisionTimer.stop();

            File gameplayFile = new File("gameplay.xml");
            InputStream gameplayStream = new FileInputStream(gameplayFile);

            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document xmlGameplayDocument = parser.parse(gameplayStream);

            String rootNodeName = xmlGameplayDocument.getDocumentElement().getNodeName();
            NodeList nodeList = xmlGameplayDocument.getElementsByTagName("GameMove");

            List<Game> gameList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                SerializablePuck puck;
                SerializablePlayer redPlayer;
                SerializablePlayer bluePlayer;
                Node gameMoveNode = nodeList.item(i);

                if(gameMoveNode.getNodeType() == Node.ELEMENT_NODE){
                    Element gameMoveElement = (Element) gameMoveNode;
                    puck = nodeToPuck(gameMoveElement.getElementsByTagName("Puck").item(0));
                    redPlayer = nodeToPlayer(gameMoveElement.getElementsByTagName("RedPlayer").item(0));
                    bluePlayer = nodeToPlayer(gameMoveElement.getElementsByTagName("BluePlayer").item(0));

                    Byte redPlayerScore = Byte.parseByte(gameMoveElement
                            .getElementsByTagName("RedPlayerScore")
                            .item(0)
                            .getTextContent());
                    Byte bluePlayerScore = Byte.parseByte(gameMoveElement
                            .getElementsByTagName("BluePlayerScore")
                            .item(0)
                            .getTextContent());

                    Short seconds = Short.parseShort(gameMoveElement
                            .getElementsByTagName("Seconds")
                            .item(0)
                            .getTextContent());

                    gameList.add(new Game(seconds, redPlayer, bluePlayer, puck, redPlayerScore, bluePlayerScore));
                }
            }

           new Thread(() -> {
               try {
                   setGameMoves(gameList);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }).start();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }


    }

    private synchronized void setGameMoves(List<Game> gameList) throws InterruptedException {
        for (Game game : gameList) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lblGameSeconds.setText(Short.toString(game.getSecondsLeft()));
                    redGoalsLabel.setText(Byte.toString(game.getRedPlayerScore()));
                    blueGoalsLabel.setText(Byte.toString(game.getBluePlayerScore()));
                }
            });

            puck.setLayoutX(game.getPuck().getxPosition());
            puck.setLayoutY(game.getPuck().getyPosition());
            redPlayer.getCircle().setLayoutX(game.getRedPlayer().getxPosition());
            redPlayer.getCircle().setLayoutY(game.getRedPlayer().getyPosition());
            bluePlayer.getCircle().setLayoutX(game.getBluePlayer().getxPosition());
            bluePlayer.getCircle().setLayoutY(game.getBluePlayer().getyPosition());
            Thread.sleep(100);
        }

    }

    private SerializablePuck nodeToPuck(Node actor) {
        if(actor.getNodeType() == Node.ELEMENT_NODE){
            Element actorElement = (Element) actor;
            double xPosition = Double.parseDouble(actorElement.getElementsByTagName("PositionX").item(0).getTextContent());
            double yPosition = Double.parseDouble(actorElement.getElementsByTagName("PositionY").item(0).getTextContent());
            byte xDirection = Byte.parseByte(actorElement.getElementsByTagName("DirectionX").item(0).getTextContent());
            byte yDirection = Byte.parseByte(actorElement.getElementsByTagName("DirectionY").item(0).getTextContent());
            return new SerializablePuck(xDirection, yDirection, xPosition, yPosition);
        }
        return null;
    }
    private SerializablePlayer nodeToPlayer(Node actor) {

        if(actor.getNodeType() == Node.ELEMENT_NODE){
            Element actorElement = (Element) actor;
            double xPosition = Double.parseDouble(actorElement.getElementsByTagName("PositionX").item(0).getTextContent());
            double yPosition = Double.parseDouble(actorElement.getElementsByTagName("PositionY").item(0).getTextContent());
            byte xDirection = Byte.parseByte(actorElement.getElementsByTagName("DirectionX").item(0).getTextContent());
            byte yDirection = Byte.parseByte(actorElement.getElementsByTagName("DirectionY").item(0).getTextContent());
            return new SerializablePlayer(actor.getNodeName(), (byte) 0,(byte)0,(byte)0,(byte)0,(byte)0, xDirection, yDirection, xPosition, yPosition);
        }
        return null;
    }

    public void saveGame(ActionEvent actionEvent) throws IOException {
        Game saveGame = getGame();

        try (ObjectOutputStream serializer = new ObjectOutputStream(
                new FileOutputStream("savedGame.ser"))) {
            serializer.writeObject(saveGame);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Gamed save successfully!");
            alert.setContentText("Your game has been saved successfully!");

            alert.showAndWait();
        }

        saveXML();
        winnerDefined = true;
    }

    private Game getGame() {
        SerializableActor redPlayerSerializable = new SerializablePlayer(redPlayer.getName(),
                redPlayer.getWins(), redPlayer.getLost(), redPlayer.getGoals(), redPlayer.getGoalsConceived(),redPlayer.getBoostGoals(),
                (byte) 0, (byte) 0, redPlayer.getCircle().getLayoutX(), redPlayer.getCircle().getLayoutY());
        SerializableActor bluePlayerSerializable = new SerializablePlayer( bluePlayer.getName(),
                  bluePlayer.getWins(), bluePlayer.getLost(), bluePlayer.getGoals(),  bluePlayer.getGoalsConceived(), bluePlayer.getBoostGoals(),
                (byte) 0, (byte) 0,  bluePlayer.getCircle().getLayoutX(),  bluePlayer.getCircle().getLayoutY());
        SerializableActor puckSerializable = new SerializablePuck((byte) puck.getxDirection(), (byte) puck.getyDirection(),
                puck.getLayoutX(), puck.getLayoutY());
        Game saveGame = new Game(
                (short) secondsLeft,
                (SerializablePlayer) redPlayerSerializable,
                (SerializablePlayer) bluePlayerSerializable,
                (SerializablePuck) puckSerializable,
                Byte.parseByte(redGoalsLabel.getText()), Byte.parseByte(blueGoalsLabel.getText()));
        return saveGame;
    }

    public void loadGame(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        try(ObjectInputStream deserializer = new ObjectInputStream(new FileInputStream("savedGame.ser"))){
                Game game = (Game) deserializer.readObject();
                redPlayer.deserializePlayer(game.getRedPlayer());
                bluePlayer.deserializePlayer(game.getBluePlayer());
                secondsLeft = game.getSecondsLeft();
                lblGameSeconds.setText(Integer.toString(secondsLeft));
                blueGoalsLabel.setText(Byte.toString(game.getBluePlayerScore()));
                redGoalsLabel.setText(Byte.toString(game.getRedPlayerScore()));
        }
    }

    public void generateDocumentation(ActionEvent actionEvent) {
        GameUtils.generateDocumentation();
    }
    

}



