package models;

import java.util.ArrayList;

public class Hearts {
  private Table table;
  private Deck deck;
  private Hand[] hands;
  private int[] scores = new int[] {0, 0, 0, 0};
  private int bigRound;
  private int smallRound;
  private int turn;
  private int whoseTurn;

  public Hearts(Hand[] hands) {
    setHands(hands);
    setTable(new Table());
  }

  // getters and setters.

  public int[] getScores() {
    return scores;
  }

  public void setScores(int[] scores) {
    this.scores = scores;
  }

  public int getWhoseTurn() {
    return whoseTurn;
  }

  public void setWhoseTurn(int whoseTurn) {
    this.whoseTurn = whoseTurn;
  }

  public Table getTable() {
    return this.table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public Hand[] getHands() {
    return this.hands;
  }

  public void setHands(Hand[] hands) {
    this.hands = hands;
  }

  public int getBigRound() {
    return this.bigRound;
  }

  public void setBigRound(int bigRound) {
    this.bigRound = bigRound;
  }

  public int getSmallRound() {
    return this.smallRound;
  }

  public void setSmallRound(int smallRound) {
    this.smallRound = smallRound;
  }

  public int getTurn() {
    return this.turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public Deck getDeck() {
    return this.deck;
  }

  public void setDeck(Deck deck) {
    this.deck = deck;
  }

  // Game methods
  public void resetGame() {
    setBigRound(1);
    setSmallRound(1);

    resetPoints();

    setDeck(new Deck());
    table.reset();
    for (Hand hand: hands) {
      hand.refreshHand();
    }
    deck.shuffle();

  }

  private void resetPoints() {
    for (Hand hand : hands) {
      hand.setPoints(0);
    }
  }

  public void dealCards() {

    int numberOfHand = hands.length;
    int handth = 0;
    for (Card card : deck) {
      if (handth == numberOfHand)
        handth = 0;

      hands[handth].addCardInHand(card);

      deck.popCard();
      handth += 1;

    }

  }

  public void placeCardToTable() {
    Hand hand = hands[turn];
    ArrayList<Card> cardsInHand = hand.getCardsInHand();
    int chosenCard = hand.getChosenPlaceCard();
    Card card = cardsInHand.get(chosenCard);

    table.placeCardAt(card, turn);
  }

  public int whoFirst() {
    Card S2 = new Card(2, Suit.CLUBS);
    for (int i = 0; i < 4; i++) {
      if (hands[i].has(S2))
        return i;
    }
    // never reach
    return -1;
  }


  public Hand checkEndGameConditionAndFindWinner() {
    Hand winner = null;
    int winnerScore = Integer.MAX_VALUE;
    boolean isMoreOrEqualTo100 = false;
    for (int i = 0; i < scores.length; i++) {
      int score = scores[i];
      if (score < winnerScore) {
        winner = hands[i];
        winnerScore = score;
      }
      if (score >= 100)
        isMoreOrEqualTo100 = true;
    }

    // check if winner is duplicate
    int count = 0;
    for (int score : scores) {
      if (score == winnerScore)
        count++;
    }

    boolean isMoreThanOneWinner = count > 1;

    if (isMoreOrEqualTo100 && !isMoreThanOneWinner)
      return winner;

    return null;
  }

  public void sortCardsInHands() {
    for (Hand hand : getHands()) {
      hand.sortCardsInHand();
    }
  }


  
}
