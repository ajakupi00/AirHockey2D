package hr.algebra.airhockey.hr.algebra.airhockey.utils;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameUtils {
    public static Scene currentScene;

    public static final int SCENE_WIDTH = 480;
    public static final int SCENE_HEIGHT = 700;
    public static final int GAME_DURATIONS_SECONDS = 30;

    public static final KeyCode[] wasd = {KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.SPACE};
    public static final KeyCode[] arrows = {KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.L};


    private GameUtils(){

    }
}
