package components;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import hearts.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class GameController implements Initializable {
  private Hearts hearts;

  @FXML
  private HBox hbox;
  @FXML
  private Button startBtn;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // TODO Auto-generated method stub
    hbox.getStylesheets().add("components/Game.css");
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/Card.fxml"));

    try {
      ImageView card = loader.load();
      hbox.getChildren().add(card);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      System.out.println("ERRORRRRR");
    }
  }

  @FXML
  private void onClickStart(ActionEvent event) {
    Hand[] hands = { new Player("test1"), new Player("test2"), new Player("test3"), new Player("test4") };

    hearts = new Hearts(hands);

    hearts.startGame();

    Hand hand = hearts.getHands()[0];

    for (Card card : hand.getCardsInHand()) {
      Label label = new Label();
      label.getStyleClass().add("card-label");
      label.setText(card.toString());
      label.setId(Integer.toString(card.hashCode()));
      label.setWrapText(true);

      hbox.getChildren().add(label);
    }
  }

}
