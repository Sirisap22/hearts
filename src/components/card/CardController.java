package components.card;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Card;

public class CardController implements Initializable {
  private boolean facingDown = false;
  private Card card;
  private Image frontCard;
  private Image backCard;

  @FXML
  private ImageView cardImage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    
  }

  public boolean isFacingDown() {
    return this.facingDown;
  }

  public void setFacingDown(boolean facingDown) {
    this.facingDown = facingDown;
  }

  public Card getCard() {
    return this.card;
  }

  public void setCard(Card card) {
    this.card = card;
    this.backCard = new Image("public/cards/back_card.png");
    String path = String.format("public/cards/%s_%s.png", Card.rankToString(card.getRank()), card.getSuit().toString());
    this.frontCard = new Image(path);
  }

  public void updateCardImage() {

    if (isFacingDown()) {
      cardImage.setImage(backCard);
      return;
    }

    cardImage.setImage(frontCard);
  }

  public void faceCardDown() {
    this.facingDown = true;
    updateCardImage();
  }

  public  void faceCardUp() {
    this.facingDown = false;
    updateCardImage();
  }

  public void filpCard() {
    setFacingDown(!facingDown);
    setCard(card);
  }

}
