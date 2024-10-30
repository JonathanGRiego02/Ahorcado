package dad.controllers;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

  private SecretWord secretWordController;

  private StringProperty palabra = new SimpleStringProperty();

  private StringProperty palabraAdivinar = new SimpleStringProperty();

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
    secretWordController.guessLetter(toUpperCase(palabraAdivinar.get().charAt(0)));
    System.out.println(secretWordController.getHiddenWord());
    secretWordController.updateHiddenWord();
    palabra.set(secretWordController.getHiddenWord());
    System.out.println(secretWordController.getHiddenWord());
    adivinarTextField.clear();
  }

  @FXML
  void onResolverAction(ActionEvent event) {

  }

  @FXML
  void onNewGameAction(ActionEvent event) {
    String randomWord = palabras.get((int) (Math.random() * palabras.size()));
    secretWordController = new SecretWord(randomWord);
    palabra.set(secretWordController.getHiddenWord());
    System.out.println(randomWord);
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
      ahorcadoImageView.setImage(null); // Clear the image if necessary
      numPuntosLabel.setText("0"); // Reset points if necessary
      // Add any other necessary reset logic here
    }


  }
