package hr.algebra.airhockey.hr.algebra.airhockey.utils;

import hr.algebra.airhockey.models.Game;
import hr.algebra.airhockey.models.SerializableActor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

/*
* <GameMove>
    <Puck>
        <PositionX>200.1</PositionX>
        <PositionY>200.1</PositionY>
        <DirectionX>0</DirectionX>
        <DirectionY>1</DirectionY>
*   </Puck>
*   <RedPlayer>
        <PositionX>200.1</PositionX>
        <PositionY>200.1</PositionY>
        <DirectionX>0</DirectionX>
        <DirectionY>1</DirectionY>
*   </RedPlayer>
*   <BluePlayer>
        <PositionX>200.1</PositionX>
        <PositionY>200.1</PositionY>
        <DirectionX>0</DirectionX>
        <DirectionY>1</DirectionY>
*   </BluePlayer>
*   <RedPlayerScore>4</RedPlayerScore>
*   <BluePlayerScore>4</BluePlayerScore>
*   <Seconds>23</Seconds>
* </GameMove>
* */



public class XMLUtility {
    private XMLUtility(){}

    public static void createGameMovesElement(List<Game> gamemoves, Document xmlDocument) {
        Element rootArrayElement = xmlDocument.createElement("GameMoves");
        xmlDocument.appendChild(rootArrayElement);

        for (Game game: gamemoves) {
            Element rootElement = xmlDocument.createElement("GameMove");
            rootArrayElement.appendChild(rootElement);

            Element puckElement = createSerializableActorXMLElement("Puck", game.getPuck(), xmlDocument);
            rootElement.appendChild(puckElement);

            Element redPlayerElement = createSerializableActorXMLElement("RedPlayer", game.getRedPlayer(), xmlDocument);
            rootElement.appendChild(redPlayerElement);

            Element bluePlayerElement = createSerializableActorXMLElement("BluePlayer", game.getBluePlayer(), xmlDocument);
            rootElement.appendChild(bluePlayerElement);

            Element redPlayerScoreElement = xmlDocument.createElement("RedPlayerScore");
            Node redPlayerScoreNode = xmlDocument.createTextNode(Byte.toString(game.getRedPlayerScore()));
            redPlayerScoreElement.appendChild(redPlayerScoreNode);
            rootElement.appendChild(redPlayerScoreElement);

            Element bluePlayerScoreElement = xmlDocument.createElement("BluePlayerScore");
            Node bluePlayerScoreNode = xmlDocument.createTextNode(Byte.toString(game.getBluePlayerScore()));
            bluePlayerScoreElement.appendChild(bluePlayerScoreNode);
            rootElement.appendChild(bluePlayerScoreElement);

            Element secondsElement = xmlDocument.createElement("Seconds");
            Node secondsNode = xmlDocument.createTextNode(Short.toString(game.getSecondsLeft()));
            secondsElement.appendChild(secondsNode);
            rootElement.appendChild(secondsElement);
        }

    }

    private static Element createSerializableActorXMLElement(String elementName, SerializableActor actor, Document xmlDocument){
        Element actorElement = xmlDocument.createElement(elementName);

        Element actorPositionXElement = xmlDocument.createElement("PositionX");
        Node actorPositionXNode = xmlDocument.createTextNode(Double.toString(actor.getxPosition()));
        actorPositionXElement.appendChild(actorPositionXNode);

        Element actorPositionYElement = xmlDocument.createElement("PositionY");
        Node actorPositionYNode = xmlDocument.createTextNode(Double.toString(actor.getyPosition()));
        actorPositionYElement.appendChild(actorPositionYNode);

        Element actorDirectionXElement = xmlDocument.createElement("DirectionX");
        Node actorDirectionXNode = xmlDocument.createTextNode(Byte.toString(actor.getxDirection()));
        actorDirectionXElement.appendChild(actorDirectionXNode);

        Element actorDirectionYElement = xmlDocument.createElement("DirectionY");
        Node actorDirectionYNode = xmlDocument.createTextNode(Byte.toString(actor.getyDirection()));
        actorDirectionYElement.appendChild(actorDirectionYNode);

        actorElement.appendChild(actorPositionXElement);
        actorElement.appendChild(actorPositionYElement);
        actorElement.appendChild(actorDirectionXElement);
        actorElement.appendChild(actorDirectionYElement);

        return actorElement;
    }
}
