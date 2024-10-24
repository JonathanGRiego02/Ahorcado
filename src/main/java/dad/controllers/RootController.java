package dad.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    // Model
    private final ObjectProperty<Tab> selectedTab = new SimpleObjectProperty<>();
    private final PalabrasController palabrasController = new PalabrasController();

    public PalabrasController getPalabrasController() {
        return palabrasController;
    }

    // View

    @FXML
    private Tab palabrasTab;

    @FXML
    private Tab partidaTab;

    @FXML
    private Tab puntuacionTab;

    @FXML
    private TabPane root;

    public Tab getPalabrasTab() {
        return palabrasTab;
    }

    public void setPalabrasTab(Tab palabrasTab) {
        this.palabrasTab = palabrasTab;
    }

    public Tab getPartidaTab() {
        return partidaTab;
    }

    public void setPartidaTab(Tab partidaTab) {
        this.partidaTab = partidaTab;
    }

    public Tab getPuntuacionTab() {
        return puntuacionTab;
    }

    public void setPuntuacionTab(Tab puntuacionTab) {
        this.puntuacionTab = puntuacionTab;
    }

    public TabPane getRoot() {
        return root;
    }

    public void setRoot(TabPane root) {
        this.root = root;
    }

    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Binds
        selectedTab.bind(root.getSelectionModel().selectedItemProperty());

        // Listener para cambiar tabla
        selectedTab.addListener((obs, oldTab, newTab) -> {
            if (newTab == palabrasTab) {
                // Set the root of PalabrasController as the content of palabrasTab
                palabrasTab.setContent(palabrasController.getRoot());
            }
        });
    }

}


