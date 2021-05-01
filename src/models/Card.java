package models;

import java.util.Objects;

public class Card implements Comparable<Card> {
  private Suit suit;
  private int rank;

  // 14 is Ace, 13 is King, 12 is Queen, 11 is Jack.
  private static final int MIN_RANK = 2;
  private static final int MAX_RANK = 14;

  public Card(int rank, Suit suit) {
    setSuit(suit);
    setRank(rank);
  }

  public Suit getSuit() {
    return this.suit;
  }

  public void setSuit(Suit suit) {
    this.suit = suit;
  }

  public int getRank() {
    return this.rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  @Override
  public String toString() {
    return String.format("%s_%s", rankToString(getRank()), getSuit().toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Card))
      return false;
    if (obj == this)
      return true;

    Card cardToCompare = (Card) obj;

    return cardToCompare.getRank() == getRank() && cardToCompare.getSuit() == getSuit();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getRank(), getSuit());
  }

  public static int getMinRank() {
    return MIN_RANK;
  }

  public static int getMaxRank() {
    return MAX_RANK;
  }

  public static Suit[] getSuits() {
    return Suit.values();
  }

  public static String rankToString(int rank) {
    if (rank <= 10)
      return Integer.toString(rank);

    switch (rank) {
    case 11:
      return "J";
    case 12:
      return "Q";
    case 13:
      return "K";
    case 14:
      return "A";
    default:
      return null;
    }
  }

  @Override
  public int compareTo(Card card) {
    Suit thisCardSuit = getSuit();
    Suit thatCardSuit = card.getSuit();
    if (thisCardSuit == thatCardSuit) {
      int thisCardRank = getRank();
      int thatCardRank = card.getRank();
      if (thisCardRank < thatCardRank)
        return -1;
      else if (thisCardRank > thatCardRank)
        return 1;
      else
        return 0;
    }

    return Suit.compare(thisCardSuit, thatCardSuit);
  }


}
