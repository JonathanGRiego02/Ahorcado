package dad.controllers;

import dad.models.SecretWord;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PartidaController implements Initializable {

  // Model
  private final ListProperty<String> palabras = new SimpleListProperty<>();

  public ListProperty<String> palabrasProperty() {
    return palabras;
  }

  private SecretWord secretWordController;

  private StringProperty palabra = new SimpleStringProperty();

  // View

  @FXML
  private TextField adivinarTextField;

  @FXML
  private ImageView ahorcadoImageView;

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
  private BorderPane root;

  public BorderPane getRoot() {
    return root;
  }

  @FXML
  void onLetraAction(ActionEvent event) {
    adivinarTextField.clear();
    
  }

  @FXML
  void onResolverAction(ActionEvent event) {

  }

  @FXML
  void onNewGameAction(ActionEvent event) {
    String randomWord = palabras.get((int) (Math.random() * palabras.size()));
    secretWordController = new SecretWord(randomWord);
    palabraLabel.setText(secretWordController.getHiddenWord());
    System.out.println(randomWord);
  }

  public PartidaController() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PartidaView.fxml"));
      loader.setController(this);
      loader.load();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    palabra.bind(adivinarTextField.textProperty());
  }



}
