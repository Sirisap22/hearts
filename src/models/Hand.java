package models;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Hand {
  protected int chosenPlaceCard = -1;
  protected int[] chosenGiveCards = { -1, -1, -1 };
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

  public int getChosenPlaceCard() {
    return this.chosenPlaceCard;
  }

  public void setChosenPlaceCard(int chosenPlaceCard) {
    this.chosenPlaceCard = chosenPlaceCard;
  }

  public int[] getChosenGiveCards() {
    return this.chosenGiveCards;
  }

  public void setChosenGiveCards(int[] chosenGiveCards) {
    this.chosenGiveCards = chosenGiveCards;
  }

  // hand action

  public void refreshHand() {
    setChosenPlaceCard(-1);
    setChosenGiveCards(new int[] { -1, -1, -1 });
    getCardsInHand().clear();
    getCardsInPile().clear();

    setPoints(0);
  }

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

  public void giveCard(Hand hand) {
    Arrays.sort(chosenGiveCards);
    for (int i = 2; i >= 0; i--) {
      int cardIndex = i;
      Card chosenCard = cardsInHand.get(cardIndex);
      hand.addCardInHand(chosenCard);
      removeCardInHand(chosenCard);
    }

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

  public boolean has(Card cardin) {
    for (int cardIndex = 0; cardIndex < cardsInHand.size(); cardIndex++) {
      Card card = cardsInHand.get(cardIndex);
      if (cardin.equals(card)) {
        return true;
      }
    }
    return false;
  }

  public boolean hasSuit(Card cardin) {
    for (int cardIndex = 0; cardIndex < cardsInHand.size(); cardIndex++) {
      Card card = cardsInHand.get(cardIndex);
      if (cardin.equals(card)) {
        return true;
      }
    }
    return false;
  }

  protected ArrayList<Integer> playableCard(Table table) {
    ArrayList<Integer> output = new ArrayList<>();
    for (int cardIndex = 0; cardIndex < getCardsInHand().size(); cardIndex++) {
      Card card = getCardsInHand().get(cardIndex);
      if (table.playable(card)) {
        output.add(cardIndex);
      }
    }
    if (table.getPlayedNumber() == 0) {
      for (int cardIndex = 0; cardIndex < getCardsInHand().size(); cardIndex++) {
        output.add(cardIndex);
      }
    }
    return output;
  }

}
