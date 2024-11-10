package dad.controllers;

import dad.models.Usuario;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Character.toUpperCase;

public class PartidaController implements Initializable {

  // Model
  private final ListProperty<String> palabras = new SimpleListProperty<>();
  private final ListProperty<Usuario> usuarios = new SimpleListProperty<>(FXCollections.observableArrayList());

  public ListProperty<String> palabrasProperty() {
    return palabras;
  }

  public ListProperty<Usuario> usuariosProperty() {
    return usuarios;
  }

  private final SecretWord secretWord = new SecretWord();
  private final StringProperty palabraAdivinar = new SimpleStringProperty();
  private Image ahorcadoImage = new Image(getClass().getResource("/hangman/1.png").toExternalForm());
  private int imageCounter = 1;

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
      // Calcular las letras sin adivinar
      int letrasSinAdivinar = (int) secretWord.getHiddenWord().chars().filter(ch -> ch == '_').count();

      // Sumar puntos por cada letra sin adivinar
      secretWord.setPuntos(secretWord.getPuntos() + (letrasSinAdivinar * 10));

      // Actualizar la palabra oculta a la palabra completa
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
    SetVisual();
  }

  private void SetVisual() {
    ahorcadoImage = new Image(getClass().getResource("/hangman/1.png").toExternalForm());
    ahorcadoImageView.setImage(ahorcadoImage);
    // Colocamos las vidas
    vidasLabel.setVisible(true);
    vidasLabel.setText("❤❤❤❤❤❤❤❤❤");
    //Activamos para probar palabras y letras
    secretWord.getGuessedLetters().clear();
    adivinarTextField.setDisable(false);
    letraButton.setDisable(false);
    resolverButton.setDisable(false);
    palabraLabel.setVisible(true);
    adivinadasListView.setVisible(true);
  }

  private void checkWordGuessed(String newValue) {
    if (newValue.equals(secretWord.getWord())) {
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
    ahorcadoImageView.setImage(ahorcadoImage);
    // Bindeos
    palabraAdivinar.bindBidirectional(adivinarTextField.textProperty());
    palabraLabel.textProperty().bind(secretWord.hiddenWordProperty());
    adivinadasListView.itemsProperty().bind(secretWord.guessedLettersProperty());
    numPuntosLabel.textProperty().bind(secretWord.puntosProperty().asString());

    // Listener
    secretWord.hiddenWordProperty().addListener((observable, oldValue, newValue) -> checkWordGuessed(newValue));
    secretWord.vidasProperty().addListener((observable, oldValue, newValue) -> updateVidasLabel(newValue.intValue()));
    vidasLabel.setVisible(false);
  }

  private void updateVidasLabel(int vidas) {
    imageCounter++;
    String hearts = "❤".repeat(Math.max(0, vidas));
    ahorcadoImage = new Image(getClass().getResource("/hangman/" + imageCounter + ".png").toExternalForm());
    ahorcadoImageView.setImage(ahorcadoImage);
    if (secretWord.getVidas() == 0) {
      PerderPartida();
    }
    vidasLabel.setText(hearts);
  }

  private void PerderPartida() {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("¡Has perdido!");
    alert.setHeaderText(null);
    alert.setContentText("¡Has perdido todas tus vidas! \nLa palabra era: " + secretWord.getWord() + "\n" +
            "Puntuación: " + secretWord.getPuntos() + " puntos.");
    alert.showAndWait();
    SaveScore();
  }



  private void ganarAlert() {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("¡Has ganado!");
    alert.setHeaderText(null);
    secretWord.setPuntos(secretWord.getPuntos() + 50);
    alert.setContentText("¡La palabra ha sido adivinada correctamente! \n" +
            "Puntuación: " + secretWord.getPuntos() + " puntos.");
    alert.showAndWait();
    SaveScore();
  }

  private void SaveScore() {
    // Create a custom dialog
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Guardar puntuación");
    dialog.setHeaderText("Introduce tu nombre:");

    // Set the button types
    ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

    // Create the username field
    TextField nameField = new TextField();
    nameField.setPromptText("Nombre");

    // Create a layout for the dialog content
    VBox content = new VBox();
    content.setSpacing(10);
    content.getChildren().add(nameField);

    // Set the content of the dialog
    dialog.getDialogPane().setContent(content);

    // Convert the result to a username when the OK button is clicked
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        return nameField.getText();
      }
      return null;
    });

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(name -> {
      if (!name.trim().isEmpty()) {
        usuarios.add(new Usuario(name, secretWord.getPuntos()));
      } else {
        // Show a warning if the name is empty
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nombre inválido");
        alert.setHeaderText(null);
        alert.setContentText("El nombre no puede estar vacío.");
        alert.showAndWait();
      }
    });
    clearGameState();
  }




  private void clearGameState() {
    palabraAdivinar.set("");
    secretWord.setWord(".");
    secretWord.setHiddenWord("...");
    ahorcadoImageView.setImage(null);
    secretWord.setPuntos(0);
    palabraLabel.setVisible(false);
    adivinarTextField.setDisable(true);
    letraButton.setDisable(true);
    resolverButton.setDisable(true);
    imageCounter = 0;
    secretWord.setVidas(8);
    adivinadasListView.setVisible(false);
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