package hearts;

import java.util.Objects;

public class Card implements Comparable<Card> {
  private Suit suit;
  private int rank;

  private static final int MIN_RANK = 1;
  private static final int MAX_RANK = 13;

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
    return String.format("%s[rank=%d, suit=%s]", getClass().getSimpleName(), getRank(), getSuit().toString());
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

  @Override
  public int compareTo(Card card) {
    int thisCardRank = getRank() == 1 ? 14 : getRank();
    int thatCardRank = card.getRank() == 1 ? 14 : getRank();
    if (thisCardRank < thatCardRank)
      return -1;
    else if (thisCardRank > thatCardRank)
      return 1;
    else
      return 0;
  }

  public static void main(String[] args) {
    Card card;

    card = new Card(10, Suit.DIAMONDS);
    System.out.println("And a ten of diamonds:");
    System.out.println("Suit = " + card.getSuit().toString().toLowerCase());
    System.out.println("Rank = " + card.getRank());
    System.out.println("Hash = " + card.hashCode());
    System.out.println();

    card = new Card(7, Suit.CLUBS);
    System.out.println("And a 7 of clubs:");
    System.out.println("Suit = " + card.getSuit().toString().toLowerCase());
    System.out.println("Rank = " + card.getRank());
    System.out.println("Hash = " + card.hashCode());
  }

}
