package components.game;

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
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import models.*;
import utils.ComponentLoader;

public class GameController implements Initializable {
  private Hearts hearts;

  @FXML
  private HBox hbox;
  @FXML
  private Button startBtn;

  HashMap<String, ImageView> cards = new HashMap<String, ImageView>();
  HashMap<String, CardController> cardControllers = new HashMap<String, CardController>();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    hbox.getStylesheets().add("components/Game.css");
  }

  @FXML
  private void onClickStart(ActionEvent event) throws Exception {
    Hand[] hands = { new Player("test1"), new Player("test2"), new Player("test3"), new Player("test4") };

    hearts = new Hearts(hands);

    hearts.resetGame();

    Hand hand = hearts.getHands()[0];

    for (Card card : hand.getCardsInHand()) {
      Pair<ImageView, CardController> cardComponent = new ComponentLoader<GameController, ImageView, CardController>()
          .loadComponent(this, "/components/card/Card.fxml");
      ImageView cardImage = cardComponent.getKey();
      CardController cardController = cardComponent.getValue();
      cardController.setCard(card);
      cardController.updateCardImage();
      // Label label = new Label();
      // label.getStyleClass().add("card-label");
      // label.setText(card.toString());
      // label.setId(Integer.toString(card.hashCode()));
      // label.setWrapText(true);
      cards.put(card.toString(), cardImage);
      cardControllers.put(card.toString(), cardController);

      hbox.getChildren().add(cardImage);
    }
  }

}
