package dad.controllers;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SecretWord {

  private final ListProperty<String> guessedLetters = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final StringProperty word = new SimpleStringProperty();
  private final StringProperty hiddenWord = new SimpleStringProperty();
  private IntegerProperty vidas = new SimpleIntegerProperty(9);

  public void StartGame(String word) {
    this.word.set(word.toUpperCase());
    StringBuilder hidden = new StringBuilder();
    for (int i = 0; i < word.length(); i++) {
      hidden.append((word.charAt(i) == ' ') ? " " : "_");
    }
    this.hiddenWord.set(hidden.toString());
  }

  public SecretWord() {
    this.word.set("");
    this.hiddenWord.set("...");
  }

  public int guessLetter(String letter) {
    // ya se acertó esta letra
    if (guessedLetters.contains(letter)) {
      return 0;
    }
    int appeareances = 0;
    for (int i = 0; i < word.get().length(); i++) {
      if (word.get().charAt(i) == letter.charAt(0)) {
        appeareances += 1;
      }
    }
    if (appeareances == 0) {
      vidas.set(vidas.get() - 1);
    }
    guessedLetters.add(letter);
    updateHiddenWord();
    return appeareances;
  }

  protected void updateHiddenWord() {
    StringBuilder hidden = new StringBuilder();
    for (int i = 0; i < word.get().length(); i++) {
      char currentChar = word.get().charAt(i);
      if (currentChar == ' ') {
        hidden.append(" ");
      } else if (guessedLetters.contains(String.valueOf(currentChar))) {
        hidden.append(currentChar);
      } else {
        hidden.append("_");
      }
    }
    this.hiddenWord.set(hidden.toString());
  }

  // getters and setters

  public String getWord() {
    return word.get();
  }

  public StringProperty wordProperty() {
    return word;
  }

  public void setWord(String word) {
    this.word.set(word);
  }

  public String getHiddenWord() {
    return hiddenWord.get();
  }

  public StringProperty hiddenWordProperty() {
    return hiddenWord;
  }

  public void setHiddenWord(String hiddenWord) {
    this.hiddenWord.set(hiddenWord);
  }

  public ObservableList<String> getGuessedLetters() {
    return guessedLetters.get();
  }

  public ListProperty<String> guessedLettersProperty() {
    return guessedLetters;
  }

  public void setGuessedLetters(ObservableList<String> guessedLetters) {
    this.guessedLetters.set(guessedLetters);
  }

  public void addGuessedWord(String word) {
    guessedLetters.add(word);
    updateHiddenWord();
  }

  public int getVidas() {
    return vidas.get();
  }

  public IntegerProperty vidasProperty() {
    return vidas;
  }

  public void setVidas(int vidas) {
    this.vidas.set(vidas);
  }
}
