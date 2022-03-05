package com.mycompany.timetables;

import java.io.File;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

/**
 * JavaFX App
 *
 * @author chalu
 */
public class App extends Application {

    protected static Stage stage;
    protected static Scene scene;
    protected static String bundleLang = "cs"; // The default App language
    protected static File confFile;

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        App.scene = new Scene(loadFXML("homepage"), 900, 600);
        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getResource("/img/icon.png").toString()));
        stage.setTitle("TIMETABLES");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"), getBundle());
        return fxmlLoader.load();
    }

    /**
     * Gets a bundle based on the selected language
     *
     * @return ResourceBundle - The bundle
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("/langResources/Bundle_" + bundleLang);
    }

    /**
     * Shows a new Alert of type CONFIRMATION
     *
     * @return boolean - User reaction
     */
    public static boolean showConfirmationDialog() {
        ResourceBundle rb = getBundle();
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(rb.getString("app.dialog.confirmation.title"));
        alert.setHeaderText(rb.getString("app.dialog.confirmation.text"));
        //
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     * Shows a new Alert of type ERROR
     */
    public static void showErrorDialog() {
        ResourceBundle rb = getBundle();
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(rb.getString("app.dialog.error.title"));
        alert.setHeaderText(rb.getString("app.dialog.error.text"));
        alert.show();
    }

    /**
     * Shows a new Alert of type ERROR
     */
    public static void showInfoDialog() {
        ResourceBundle rb = getBundle();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(rb.getString("app.dialog.info.title"));
        alert.setHeaderText(rb.getString("app.dialog.info.text"));
        alert.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
