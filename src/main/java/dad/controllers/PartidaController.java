package dad.controllers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Character.toUpperCase;

public class PartidaController implements Initializable {

  // Model
  private final ListProperty<String> palabras = new SimpleListProperty<>();

  public ListProperty<String> palabrasProperty() {
    return palabras;
  }

  private final ListProperty<String> guessedLetters = new SimpleListProperty<>(FXCollections.observableArrayList());

  private SecretWord secretWordController;

  private final StringProperty palabra = new SimpleStringProperty();

  private final StringProperty palabraAdivinar = new SimpleStringProperty();

  // View

  @FXML
  private TextField adivinarTextField;

  @FXML
  private ImageView ahorcadoImageView;

  @FXML
  private ListView<String> adivinadasListView;

  @FXML
  private Label numPuntosLabel;

  @FXML
  private Label palabraLabel;

  @FXML
  private Button resolverButton;

  @FXML
  private Button letraButton;

  @FXML
  private Button partidaButton;

  @FXML
  private AnchorPane root;

  public AnchorPane getRoot() {
    return root;
  }

  @FXML
  void onLetraAction(ActionEvent event) {
    // Comprobamos que la letra introducida sea válida
    if (palabraAdivinar.get().length() != 1) {
      WrongLetter();
      return;
    }
    // Ponemos la letra y actualizamos la palabra
    secretWordController.guessLetter(toUpperCase(palabraAdivinar.get().charAt(0)));
    System.out.println(secretWordController.getHiddenWord());
    secretWordController.updateHiddenWord();

    // Ponemos la palabra actualizada en el Label
    palabra.set(secretWordController.getHiddenWord());
    System.out.println(secretWordController.getHiddenWord());
    adivinarTextField.clear();
    guessedLetters.add(palabraAdivinar.get());
  }

  private void WrongLetter() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Input inválido");
    alert.setHeaderText(null);
    alert.setContentText("Introduce una letra");
    alert.showAndWait();
  }

  @FXML
  void onResolverAction(ActionEvent event) {

  }

  @FXML
  void onNewGameAction(ActionEvent event) {
    // Obtenemos la palabra del array de palabras
    String randomWord = palabras.get((int) (Math.random() * palabras.size()));
    secretWordController = new SecretWord(randomWord);

    // Ponemos la palabra en el controlador que gestiona la palabra secreta
    palabra.set(secretWordController.getHiddenWord());
    System.out.println(randomWord);

    // Habilitamos los botones y textfield para jugar
    adivinarTextField.setDisable(false);
    letraButton.setDisable(false);
    resolverButton.setDisable(false);
  }

  public PartidaController() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaView.fxml"));
      loader.setController(this);
      loader.load();
      root.getStylesheets().add(getClass().getResource("/StyleSheets/partida.css").toExternalForm());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    // binds
    palabra.bindBidirectional(palabraLabel.textProperty());
    palabraAdivinar.bind(adivinarTextField.textProperty());
    adivinadasListView.itemsProperty().bind(guessedLetters);

    // listeners
    palabra.addListener((observable, oldValue, newValue) -> checkWordGuessed(newValue));
  }

  // Listener de haber ganado la partida
  private void checkWordGuessed(String newValue) {
    if (newValue.equals(secretWordController.getWord())) {
      System.out.println("¡La palabra ha sido adivinada correctamente!");
      clearGameState();
    }
  }

    // Limpiar para empezar nueva partida
    private void clearGameState() {
      palabra.set("");
      palabraAdivinar.set("");
      adivinarTextField.clear();
      secretWordController = null;
      ahorcadoImageView.setImage(null);
      numPuntosLabel.setText("0");
    }


  }
