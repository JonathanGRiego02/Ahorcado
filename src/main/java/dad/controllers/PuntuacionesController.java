package dad.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dad.adapters.IntegerPropertyAdapter;
import dad.adapters.StringPropertyAdapter;
import dad.models.Usuario;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PuntuacionesController implements Initializable {

    // Model
    private final ListProperty<Usuario> usuarios = new SimpleListProperty<>(FXCollections.observableArrayList());

    private final ListProperty<Usuario> filteredUsuarios = new SimpleListProperty<>(FXCollections.observableArrayList());


    // View
    @FXML
    private Button buscarButton;

    @FXML
    private Button restartButton;

    @FXML
    private TableColumn<Usuario, String> nombreUsuario;

    @FXML
    private TableColumn<Usuario, Number> puntuacionUsuario;

    @FXML
    private GridPane root;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TableView<Usuario> usuariosTableView;

    public GridPane getRoot() {
        return root;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CargarUsuarios();

        // Lista de usuarios filtrada
        usuariosTableView.itemsProperty().bind(filteredUsuarios);

        // Initially, show all users
        filteredUsuarios.set(usuarios);

        // Asociar la lista de usuarios a la TableView
        nombreUsuario.setCellValueFactory(c -> c.getValue().nombreProperty());
        puntuacionUsuario.setCellValueFactory(c -> c.getValue().puntosProperty());
        usuariosTableView.itemsProperty().bind(usuarios);

        // Ordenar la tabla por puntos en orden descendente
        puntuacionUsuario.setSortType(TableColumn.SortType.DESCENDING);
        usuariosTableView.getSortOrder().add(puntuacionUsuario); // Añadir la columna a la lista de orden
        usuariosTableView.sort(); // Aplicar el orden
    }

    private void CargarUsuarios() {
        // Crear un Gson con los adaptadores personalizados
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                .registerTypeAdapter(IntegerProperty.class, new IntegerPropertyAdapter())
                .create();

        // Leer el archivo JSON
        try (FileReader reader = new FileReader("JsonFiles/usuarios.json")) {
            // Cargar la lista de usuarios desde el archivo JSON
            Usuario[] usuariosArray = gson.fromJson(reader, Usuario[].class);

            // Convertir el array a una lista observable
            usuarios.setAll(usuariosArray);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void GuardarUsuarios() {
        // Crear un Gson con los adaptadores personalizados para el objeto Usuario
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(StringProperty.class, new StringPropertyAdapter())
                .registerTypeAdapter(IntegerProperty.class, new IntegerPropertyAdapter())
                .setPrettyPrinting()  // Para que el JSON sea más legible
                .create();

        // Convertir la lista de usuarios a JSON
        String json = gson.toJson(usuarios.get());

        // Escribir el JSON en el archivo usuarios.json
        try (FileWriter writer = new FileWriter("JsonFiles/usuarios.json")) {
            writer.write(json);
            writer.flush(); // Asegurarse de que todos los datos se escriben en el archivo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBuscarAction() {
        String searchText = usuarioTextField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            filteredUsuarios.set(usuarios);
        } else {
            List<Usuario> filteredList = usuarios.stream()
                    .filter(usuario -> usuario.getNombre().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            filteredUsuarios.set(FXCollections.observableArrayList(filteredList));
        }
    }

    @FXML
    void onRestablecerAction(ActionEvent event) {
        filteredUsuarios.set(usuarios);
    }


    public PuntuacionesController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PuntuacionesView.fxml"));
            loader.setController(this);
            loader.load();
            root.getStylesheets().add(getClass().getResource("/StyleSheets/puntuaciones.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
