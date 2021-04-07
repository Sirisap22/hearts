package hearts;

import java.util.ArrayList;

public abstract class Hand {
  protected int points;
  protected String name;
  protected ArrayList<Card> cardsInHand;
  protected ArrayList<Card> cardsInPile;

  protected Hand(String name) {
    setPoints(0);
    setName(name);
    setCardsInHand(new ArrayList<Card>());
    setCardsInPile(new ArrayList<Card>());
  }

  // getters and setters

  public int getPoints() {
    return this.points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<Card> getCardsInHand() {
    return this.cardsInHand;
  }

  public void setCardsInHand(ArrayList<Card> cardsInHand) {
    this.cardsInHand = cardsInHand;
  }

  public ArrayList<Card> getCardsInPile() {
    return this.cardsInPile;
  }

  public void setCardsInPile(ArrayList<Card> cardsInPile) {
    this.cardsInPile = cardsInPile;
  }

  // hand action

  public void addCardInHand(Card card) {
    cardsInHand.add(card);
  }

  public void removeCardInHand(Card removeCard) {
    cardsInHand.removeIf(card -> card.equals(removeCard));
  }

  public void addCardInPile(Card card) {
    checkAndAddPoints(card);
    cardsInPile.add(card);
  }

  public void checkAndAddPoints(Card card) {
    if (isHeart(card)) {
      points += 1;
    } else if (isQueenOfSpade(card)) {
      points += 13;
    }
  }

  private boolean isHeart(Card card) {
    return card.getSuit() == Suit.HEARTS;
  }

  private boolean isQueenOfSpade(Card card) {
    return card.getSuit() == Suit.SPADES && card.getRank() == 12;
  }

  public void sortCardsInHand() {
    cardsInHand.sort((card1, card2) -> card1.compareTo(card2));
  }

  public abstract void makeAction();

  // utils for debug.
  public void printCardsInHand() {
    for (Card card : getCardsInHand()) {
      System.out.println(card.toString());
    }
  }

  public void printCardsInPile() {
    for (Card card : getCardsInPile()) {
      System.out.println(card.toString());
    }
  }

}
