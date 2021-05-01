package models;

import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;

public class Deck implements Iterable<Card> {
  private final Card[] cards;
  private int top;

  public Deck() {
    cards = new Card[Card.getSuits().length * (Card.getMaxRank() - Card.getMinRank() + 1)];

    refresh();
  }

  public void refresh() {
    Suit[] suits = Card.getSuits();
    int minRank = Card.getMinRank();
    int maxRank = Card.getMaxRank();

    int i = 0;
    for (Suit suit : suits)
      for (int rank = minRank; rank <= maxRank; rank++)
        cards[i++] = new Card(rank, suit);
    top = cards.length - 1;
    assert cards != null;
  }

  public void shuffle() {
    Random rng = new Random();

    for (int i = cards.length - 1; i > 0; i--) {
      int randomPosition = rng.nextInt(i + 1);
      swapCardPosition(i, randomPosition);
    }
  }

  private void swapCardPosition(int firstPosition, int secondPosition) {
    Card temp = cards[secondPosition];
    cards[secondPosition] = cards[firstPosition];
    cards[firstPosition] = temp;
  }

  public boolean empty() {
    return top < 0;
  }

  public Card popCard() {
    if (empty())
      throw new IllegalStateException("Can't deal from an empty deck.");
    return cards[top--];
  }

  public Iterator<Card> iterator() {
    return new Iterator<Card>() {
      private int cursor = top;

      public boolean hasNext() {
        return cursor >= 0;
      }

      public Card next() {
        if (hasNext())
          return cards[cursor--];
        throw new NoSuchElementException();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };
  }
}
