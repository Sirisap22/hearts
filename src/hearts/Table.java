package hearts;

import java.util.Arrays;
import java.util.Stack;

public class Table {
  private Stack<Card> cardsStack = new Stack<Card>();
  private Card[] cardSlots = new Card[4];

  // index of Hands mean player
  public void placeCardAt(Card card, int slot) {
    cardsStack.push(card);
    cardSlots[slot] = card;
  }

  public Card[] popAllCards() {
    Card[] cards = (Card[]) cardsStack.toArray();
    cardsStack.clear();
    Arrays.fill(cardSlots, null);
    return cards;
  }
}
