package hr.algebra.airhockey.hr.algebra.airhockey.utils;

import hr.algebra.airhockey.jndi.helper.JndiHelper;
import hr.algebra.airhockey.jndi.helper.ServerConfigurationKey;
import hr.algebra.airhockey.models.Player;
import hr.algebra.airhockey.models.SerializableActor;
import hr.algebra.airhockey.models.SerializableActor.SerializablePlayer;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;

import javax.naming.NamingException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameUtils {
    private static final String CLASS_EXTENSION = ".class";

    public static Scene currentScene;

    public static final int SCENE_WIDTH = 480;
    public static final int SCENE_HEIGHT = 715;
    public static final int GAME_DURATIONS_SECONDS = 10;

    public static final KeyCode[] wasd = {KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.SPACE};
    public static final KeyCode[] arrows = {KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.L};

    public static String SERVER_HOST;
    public static int SERVER_PORT;

    private GameUtils() throws NamingException, IOException {
        SERVER_HOST = JndiHelper.getConfigurationParameter(
                ServerConfigurationKey.GAME_SERVER_IP);
        SERVER_PORT = Integer.parseInt(JndiHelper.getConfigurationParameter(
                ServerConfigurationKey.GAME_SERVER_PORT));
    }

    public static void generateDocumentation() {

        File documentationFile = new File("documentation.html");

        try {

            FileWriter writer = new FileWriter(documentationFile);

            writer.write("<!DOCTYPE html>");
            writer.write("<html>");
            writer.write("<head>");
            writer.write("<title>Project documentation</title>");
            writer.write("</head>");
            writer.write("<body>");
            writer.write("<h1>Project documentation</h1>");
            writer.write("<p>Class list:</p>");

            List<Path> paths = Files.walk(Paths.get("."))
                    .filter(path -> path.getFileName().toString().endsWith(CLASS_EXTENSION))
                    .collect(Collectors.toList());

            for (Path path : paths) {
                //System.out.println("Path: " + path);
                String[] tokens = path.toString().split(Pattern.quote(System.getProperty("file.separator")));

                Boolean startBuildingPath = false;

                StringBuilder sb = new StringBuilder();

                for (String token : tokens) {
                    if ("classes".equals(token)) {
                        startBuildingPath = true;
                        continue;
                    }

                    if (startBuildingPath) {

                        if (token.endsWith(CLASS_EXTENSION)) {
                            sb.append(token.substring(0, token.indexOf(".")));
                        } else {
                            sb.append(token);
                            sb.append(".");
                        }
                    } else {
                        continue;
                    }
                }

                if ("module-info".equals(sb.toString())) {
                    continue;
                }

                System.out.println("Fully qualified name: " + sb.toString());

                try {
                    Class<?> clazz = Class.forName(sb.toString());

                    writer.write("<h2>" + Modifier.toString(clazz.getModifiers()) + " " + clazz.getName() + "</h2>");

                    StringBuilder classFieldString = new StringBuilder();

                    for (Field classField : clazz.getDeclaredFields()) {
                        Annotation[] annotations = classField.getAnnotations();
                        if (annotations.length != 0) {
                            for (Annotation a : annotations) {
                                classFieldString.append(a.toString());
                                classFieldString.append("<br />");
                            }
                        }
                        classFieldString.append(Modifier.toString(classField.getModifiers()));
                        classFieldString.append(" ");
                        classFieldString.append(classField.getType().getSimpleName());
                        classFieldString.append(" ");
                        classFieldString.append(classField.getName());
                        classFieldString.append(" ");
                        classFieldString.append("<br /><br />");
                    }

                    writer.write("<h3>Fields</h3>");

                    writer.write("<h4>" + classFieldString + "</h4>");

                    Constructor[] constructors = clazz.getConstructors();

                    writer.write("<h3>Constructors:</h3>");

                    for (Constructor c : constructors) {

                        String constructorParams = generateDocumentation(c);

                        writer.write("<h4>Constructor:" + Modifier.toString(c.getModifiers()) + " " + c.getName()
                                + "(" + constructorParams + ")" + "</h4>");
                    }

                    Method[] methods = clazz.getMethods();

                    writer.write("<h3>Methods:</h3>");

                    for (Method m : methods) {

                        String methodsParams = generateDocumentation(m);

                        StringBuilder exceptionsBuilder = new StringBuilder();

                        for (int i = 0; i < m.getExceptionTypes().length; i++) {
                            if (exceptionsBuilder.isEmpty()) {
                                exceptionsBuilder.append(" throws ");
                            }

                            Class exceptionClass = m.getExceptionTypes()[i];
                            exceptionsBuilder.append(exceptionClass.getSimpleName());

                            if (i < m.getExceptionTypes().length - 1) {
                                exceptionsBuilder.append(", ");
                            }
                        }

                        writer.write("<h4>Method:" + Modifier.toString(m.getModifiers())
                                + " " + m.getReturnType().getSimpleName()
                                + " " + m.getName() + "(" + methodsParams + ")"
                                + " " + exceptionsBuilder.toString()
                                + "</h4>");
                    }

                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            writer.write("</body>");
            writer.write("</html>");
            writer.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Documentation");
            alert.setContentText("Documentation generated successfully!");


            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while generating documentation!");
            alert.setHeaderText("Cannot find the files");
            alert.setContentText("The class files cannot be accessed.");

            alert.showAndWait();
        }
    }

    private static <T extends Executable> String generateDocumentation(T executable) {
        Parameter[] params = executable.getParameters();

        StringBuilder methodsParams = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            String modifierString = Modifier.toString(params[i].getModifiers());

            if (!modifierString.isEmpty()) {
                methodsParams.append(modifierString);
                methodsParams.append(" ");
            }
            methodsParams.append(params[i].getType().getSimpleName());
            methodsParams.append(" ");
            methodsParams.append(params[i].getName());

            if (i < (params.length - 1)) {
                methodsParams.append(", ");
            }
        }

        return methodsParams.toString();
    }


}
