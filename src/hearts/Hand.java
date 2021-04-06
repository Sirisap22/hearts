package hearts;

import java.util.ArrayList;

public abstract class Hand {
  protected String name;
  protected ArrayList<Card> cardsInHand;

  public void draw(Deck deck, int numberOfCards) {
    for (int i = 0; i < numberOfCards; i++) {
      cardsInHand.add(deck.popCard());
    }
  }

}
