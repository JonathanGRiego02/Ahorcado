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
  private SecretWord secretWordController = new SecretWord();
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

  @FXML
  private Label vidasLabel;

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
    System.out.println(secretWordController.getHiddenWord());
    secretWordController.guessLetter(palabraAdivinar.get().toUpperCase());
    palabraAdivinar.set("");
  }


  @FXML
  void onResolverAction(ActionEvent event) {
    if (palabraAdivinar.get().isEmpty()) {
      WrongWord();
      return;
    }

    // Comprobamos si la palabra introducida es correcta
    String palabra_mayusculas = palabraAdivinar.get().toUpperCase();
    if (palabra_mayusculas.equals(secretWordController.getWord())) {
      secretWordController.setHiddenWord(palabra_mayusculas);
    } else {
      palabraAdivinar.set("");
    }
  }


  @FXML
  void onNewGameAction(ActionEvent event) {
    // Obtenemos la palabra del array de palabras
    String randomWord = palabras.get((int) (Math.random() * palabras.size()));
    secretWordController.StartGame(randomWord);
    // Ponemos la palabra en el controlador que gestiona la palabra secreta
    System.out.println(secretWordController.getWord());

    // Habilitamos los botones y textfield para jugar
    adivinarTextField.setDisable(false);
    letraButton.setDisable(false);
    resolverButton.setDisable(false);
  }

  // Listener de haber ganado la partida por si ganas letra a letra
  private void checkWordGuessed(String newValue) {
    if (newValue.equals(secretWordController.getWord())) {
      secretWordController.setHiddenWord("");
      ganarAlert();
    }
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
    palabraAdivinar.bindBidirectional(adivinarTextField.textProperty());
    // Binds
    palabraLabel.textProperty().bind(secretWordController.hiddenWordProperty());
    adivinadasListView.itemsProperty().bind(secretWordController.guessedLettersProperty());
    secretWordController.hiddenWordProperty().addListener((observable, oldValue, newValue) -> checkWordGuessed(newValue));
    vidasLabel.textProperty().bind(secretWordController.vidasProperty().asString());
  }


    // Limpiar para empezar nueva partida
  private void clearGameState() {
    palabraAdivinar.set("");
    secretWordController.setWord("");
    secretWordController.setHiddenWord("");
    ahorcadoImageView.setImage(null);
    numPuntosLabel.setText("0");
    secretWordController.getGuessedLetters().clear();
    adivinarTextField.setDisable(true);
    letraButton.setDisable(true);
    resolverButton.setDisable(true);
  }

  // Alertas
  private void ganarAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("¡Has ganado!");
    alert.setHeaderText(null);
    alert.setContentText("¡La palabra ha sido adivinada correctamente!");
    alert.showAndWait();
    clearGameState();
  }

  private void WrongLetter() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Input inválido");
    alert.setHeaderText(null);
    alert.setContentText("Introduce una letra");
    alert.showAndWait();
  }

  private void WrongWord() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Input inválido");
    alert.setHeaderText(null);
    alert.setContentText("Introduce una palabra");
    alert.showAndWait();
  }
}
