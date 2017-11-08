package org.mbari.javafx;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;

import java.util.ResourceBundle;

/**
 * @author Brian Schlining
 * @since 2017-11-07T16:13:00
 */
public class JFXUtilities {

    private JFXUtilities() {
        // No instantiation allowed
    }


    public static <T> T loadFxmlController(Class<T> clazz, String fxmlPath) {
        FXMLLoader loader = new FXMLLoader(clazz.getResource(fxmlPath));
        try {
            loader.load();
            return loader.getController();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load " + fxmlPath, e);
        }
    }

    /**
     * Load a controller for an FXML document.
     *
     * @param clazz The controller class
     * @param fxmlPath The path to use to look up the fxml file for the controller
     * @param i18nBundle Internationalization bundle
     * @param <T> The type of the controller class
     * @return A controller loaded from the FXML file. The i18n bundle will be injected into it.
     */
    public static <T> T loadFxmlController(Class<T> clazz, String fxmlPath, ResourceBundle i18nBundle) {
        FXMLLoader loader = new FXMLLoader(clazz.getResource(fxmlPath), i18nBundle);
        try {
            loader.load();
            return loader.getController();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to load " + fxmlPath, e);
        }
    }

    public static void runOnFXThread(Runnable r) {
        if (Platform.isFxApplicationThread()) {
            r.run();
        }
        else {
            Platform.runLater(r);
        }
    }
}
