package dad.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PalabrasController implements Initializable {

    // Model
    private final ListProperty<String> palabras = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final String FILE_PATH = "JsonFiles/palabras.json";

    // View
    @FXML
    private ListView<String> palabrasListView;

    @FXML
    private GridPane root;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;


    public ListView<?> getPalabrasListView() {
        return palabrasListView;
    }

    public void setPalabrasListView(ListView<String> palabrasListView) {
        this.palabrasListView = palabrasListView;
    }

    public GridPane getRoot() {
        return root;
    }

    public void setRoot(GridPane root) {
        this.root = root;
    }


    public PalabrasController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PalabrasView.fxml"));
            loader.setController(this);
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargamos las palabras en el Json
        try {
            GetPalabras();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Bindings
        palabrasListView.itemsProperty().bindBidirectional(palabras);
    }



    private void GetPalabras() throws IOException {
        Gson gson = new Gson();

        Path jsonFilePath = Paths.get(FILE_PATH);

        // Comprueba si el archivo existe
        if (!Files.exists(jsonFilePath)) {
            throw new FileNotFoundException("The file " + FILE_PATH + " was not found");
        }

        String jsonContent = Files.readString(jsonFilePath);
        List<String> palabrasList = gson.fromJson(jsonContent, new TypeToken<List<String>>() {}.getType());

        palabras.addAll(palabrasList);
    }

    // Vulnerabilidad en AhorcadoApp al tener que hacerlo public
    public void guardarPalabras() {
        // Crear una instancia de Gson con formato bonito
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Convertir las palabras a JSON
        String json = gson.toJson(palabras.get());

        // Guardar el JSON en el archivo
        try {
            Files.writeString(Paths.get(FILE_PATH), json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // Botones
    @FXML
    public void onAddAction() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("");
        dialog.setHeaderText("La palabra será añadida al juego");
        dialog.setContentText("Nueva palabra:");

        Optional<String> result = dialog.showAndWait();

        // Comprobamos si el resultado es presente
        if (result.isPresent()) {
            String palabra = result.get();
            if (palabra != null && !palabra.trim().isEmpty()) {
                palabras.add(palabra); // Añadir la palabra solo si no es vacía
            } else {
                mostrarAdvertencia("Por favor, introduce una palabra válida.");
            }
        }
    }

    // Método para mostrar un cuadro de diálogo de advertencia
    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML void onDeleteAction() {
        String palabra = palabrasListView.getSelectionModel().getSelectedItem();
        palabras.remove(palabra);
    }

}
