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
    private final PalabrasController palabrasController = new PalabrasController();
    private final PuntuacionesController puntuacionesController = new PuntuacionesController();
    private final PartidaController partidaController = new PartidaController();

    private final ObjectProperty<Tab> selectedTab = new SimpleObjectProperty<>();

    public PalabrasController getPalabrasController() {
        return palabrasController;
    }

    public PuntuacionesController getPuntuacionesController() {
        return puntuacionesController;
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


    public TabPane getRoot() {
        return root;
    }


    public RootController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootView.fxml"));
            loader.setController(this);
            loader.load();
            root.getStylesheets().add(getClass().getResource("/StyleSheets/root.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Binds
        selectedTab.bind(root.getSelectionModel().selectedItemProperty());
        partidaController.palabrasProperty().bind(palabrasController.palabrasProperty());
        partidaController.usuariosProperty().bind(puntuacionesController.usuariosProperty());

        // Tabs
        palabrasTab.setContent(palabrasController.getRoot());
        puntuacionTab.setContent(puntuacionesController.getRoot());
        partidaTab.setContent(partidaController.getRoot());


    }

}


