package com.mycompany.schedules;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

/**
 * @author chalu
 */
public class HomepageController implements Initializable {

    @FXML
    ComboBox<String> langChooser;

    /**
     * Switches to the GENERATE page
     *
     * @throws IOException
     */
    @FXML
    private void swithToGenerate() throws IOException {
        App.setRoot("generate");
    }

    /**
     * Displays a window for user to select a config file
     */
    @FXML
    private void importConfig() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(App.getBundle().getString("app.fileChooser.open"));
            fileChooser.getExtensionFilters()
                    .addAll(new FileChooser.ExtensionFilter("Conf Files", "*.conf", "*.dat", "*.ser", "*.bin"));
            App.confFile = fileChooser.showOpenDialog(App.stage);
            //
            App.setRoot("generate");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Switches the App language
     */
    @FXML
    private void switchLang() {
        try {
            App.bundleLang = langChooser.getSelectionModel().getSelectedItem().toString().toLowerCase();
            App.setRoot("homepage");
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Displays a window with help for user
     */
    @FXML
    private void showHelp() {
        App.showInfoDialog();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        langChooser.getItems().addAll("CS", "EN", "DE");
        langChooser.getSelectionModel().select(rb.getString("language").toUpperCase());
    }
}
