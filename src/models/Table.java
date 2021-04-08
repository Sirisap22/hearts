package models;

import java.util.ArrayList;
import java.util.Arrays;

public class Table {
  private ArrayList<Integer> handStack = new ArrayList<Integer>();
  private Card[] cardSlots = new Card[4];

  // index of Hands mean player
  public void placeCardAt(Card card, int slot) {
    handStack.add(slot);
    cardSlots[slot] = card;
  }

  public Card[] popAllCards() {
    Card[] cards = cardSlots.clone();
    handStack.clear();
    Arrays.fill(cardSlots, null);
    return cards;
  }

  public int findWinner() {
    Suit selectedSuit = cardSlots[handStack.get(0)].getSuit();
    int winner = handStack.get(0);
    for (int hand = 0; hand < cardSlots.length; hand++) {
      Card winnerCard = cardSlots[winner];
      Card currentCard = cardSlots[hand];
      if (currentCard.getSuit() == selectedSuit && winnerCard.compareTo(currentCard) == -1) {
        winner = hand;
      }
    }

    return winner;
  }

}
