package dad.controllers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
  private final SecretWord secretWord = new SecretWord();
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
    if (palabraAdivinar.get().length() != 1) {
      InvalidLetter();
      return;
    }
    System.out.println(secretWord.getHiddenWord());
    secretWord.guessLetter(palabraAdivinar.get().toUpperCase());
    palabraAdivinar.set("");
  }

  @FXML
  void onResolverAction(ActionEvent event) {
    if (palabraAdivinar.get().isEmpty()) {
      WrongWord();
      return;
    }

    String palabra_mayusculas = palabraAdivinar.get().toUpperCase();
    if (palabra_mayusculas.equals(secretWord.getWord())) {
      secretWord.setHiddenWord(palabra_mayusculas);
    } else {
      palabraAdivinar.set("");
      secretWord.setVidas(secretWord.getVidas() - 1);
    }
  }

  @FXML
  void onNewGameAction(ActionEvent event) {
    // Metemos la palabra aleatoria de la pool del array
    String randomWord = palabras.get((int) (Math.random() * palabras.size()));
    secretWord.StartGame(randomWord);
    System.out.println(secretWord.getWord());
    // Colocamos las vidas
    vidasLabel.setVisible(true);
    vidasLabel.setText("❤❤❤❤❤❤❤❤❤");
    //Activamos para probar palabras y letras
    secretWord.getGuessedLetters().clear();
    adivinarTextField.setDisable(false);
    adivinarTextField.setVisible(true);
    letraButton.setDisable(false);
    resolverButton.setDisable(false);
    palabraLabel.setVisible(true);
  }

  private void checkWordGuessed(String newValue) {
    if (newValue.equals(secretWord.getWord())) {
      secretWord.setHiddenWord("");
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
    // Bindeos
    palabraAdivinar.bindBidirectional(adivinarTextField.textProperty());
    palabraLabel.textProperty().bind(secretWord.hiddenWordProperty());
    adivinadasListView.itemsProperty().bind(secretWord.guessedLettersProperty());
    // Listener
    secretWord.hiddenWordProperty().addListener((observable, oldValue, newValue) -> checkWordGuessed(newValue));
    secretWord.vidasProperty().addListener((observable, oldValue, newValue) -> updateVidasLabel(newValue.intValue()));
    vidasLabel.setVisible(false);
  }

  private void updateVidasLabel(int vidas) {
    String hearts = "❤".repeat(Math.max(0, vidas));
    if (secretWord.getVidas() == 0) {
      PerderPartida();
    }
    vidasLabel.setText(hearts);
  }

  private void PerderPartida() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("¡Has perdido!");
    alert.setHeaderText(null);
    alert.setContentText("¡Has perdido todas tus vidas! \nLa palabra era: " + secretWord.getWord());
    alert.showAndWait();
    clearGameState();
  }

  private void clearGameState() {
    palabraAdivinar.set("");
    secretWord.setWord(".");
    secretWord.setHiddenWord("...");
    ahorcadoImageView.setImage(null);
    numPuntosLabel.setText("0");
    adivinarTextField.setVisible(false);
    palabraLabel.setVisible(false);
    letraButton.setDisable(true);
    resolverButton.setDisable(true);
    secretWord.setVidas(9);
  }

  private void ganarAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("¡Has ganado!");
    alert.setHeaderText(null);
    alert.setContentText("¡La palabra ha sido adivinada correctamente!");
    alert.showAndWait();
    clearGameState();
  }

  private void InvalidLetter() {
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