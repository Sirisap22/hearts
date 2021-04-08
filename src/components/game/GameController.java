package components.game;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import components.card.CardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import models.*;
import utils.ComponentLoader;

public class GameController implements Initializable {
  private Hearts hearts;
  private ComponentLoader<GameController, ImageView, CardController> cardComponentLoader = new ComponentLoader<>();

  @FXML
  private Button startBtn;

  @FXML
  private GridPane gridPane;

  @FXML
  private HBox zerothHand;
  @FXML
  private VBox firstHand;
  @FXML
  private HBox secondHand;
  @FXML
  private VBox thirdHand;

  HashMap<String, ImageView> cards = new HashMap<String, ImageView>();
  HashMap<String, CardController> cardControllers = new HashMap<String, CardController>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    gridPane.getStylesheets().add("components/game/Game.css");
  }

  @FXML
  private void onClickStart(ActionEvent event) throws Exception {
    Hand[] handsInit = { new Player("test1"), new Player("test2"), new Player("test3"), new Player("test4") };

    hearts = new Hearts(handsInit);

    hearts.resetGame();

    Hand[] hands = hearts.getHands();
    renderZerothHand(zerothHand, hands[0]);
    renderFirstHand(firstHand, hands[1]);
    renderSecondHand(secondHand, hands[2]);
    renderThirdHand(thirdHand, hands[3]);

  }

  private void renderZerothHand(HBox hbox, Hand hand) throws IOException {
    for (Card card : hand.getCardsInHand()) {
      Pair<ImageView, CardController> cardComponent = cardComponentLoader.loadComponent(this,
          "/components/card/Card.fxml");
      ImageView cardImage = cardComponent.getKey();
      CardController cardController = cardComponent.getValue();
      setUpCardComponent(cardImage, cardController, false, card);
      setUpCardView(cardImage, 0);
      hbox.getChildren().add(cardImage);
    }

  }

  private void renderFirstHand(VBox vbox, Hand hand) throws IOException {
    for (Card card : hand.getCardsInHand()) {
      Pair<ImageView, CardController> cardComponent = cardComponentLoader.loadComponent(this,
          "/components/card/Card.fxml");
      ImageView cardImage = cardComponent.getKey();
      CardController cardController = cardComponent.getValue();
      setUpCardComponent(cardImage, cardController, true, card);
      setUpCardView(cardImage, 90);
      vbox.getChildren().add(cardImage);
    }

  }

  private void renderSecondHand(HBox hbox, Hand hand) throws IOException {
    for (Card card : hand.getCardsInHand()) {
      Pair<ImageView, CardController> cardComponent = cardComponentLoader.loadComponent(this,
          "/components/card/Card.fxml");
      ImageView cardImage = cardComponent.getKey();
      CardController cardController = cardComponent.getValue();
      setUpCardComponent(cardImage, cardController, true, card);
      setUpCardView(cardImage, 180);
      hbox.getChildren().add(cardImage);
    }

  }

  private void renderThirdHand(VBox vbox, Hand hand) throws IOException {
    for (Card card : hand.getCardsInHand()) {
      Pair<ImageView, CardController> cardComponent = cardComponentLoader.loadComponent(this,
          "/components/card/Card.fxml");
      ImageView cardImage = cardComponent.getKey();
      CardController cardController = cardComponent.getValue();
      setUpCardComponent(cardImage, cardController, true, card);
      setUpCardView(cardImage, 270);
      vbox.getChildren().add(cardImage);
    }

  }

  private void setUpCardComponent(ImageView cardImage, CardController cardController, boolean isFacingDown, Card card) {
    cardController.setFacingDown(isFacingDown);
    cardController.setCard(card);
    cardController.updateCardImage();
    // cards.get("2_clubs");
    // cardControllers.get("2_clubs").setFacingDown(false);
    // cardControllers.get("2_clubs").updateCardImage();
    cards.put(card.toString(), cardImage);
    cardControllers.put(card.toString(), cardController);
  }

  private void setUpCardView(ImageView cardImage, double rotateDegrees) {
    cardImage.setRotate(rotateDegrees);
  }

}
