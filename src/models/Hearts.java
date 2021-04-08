package models;

import java.util.ArrayList;

public class Hearts {
  private Table table;
  private Deck deck;
  private Hand[] hands;
  private int[] scores;
  private int bigRound;
  private int smallRound;
  private int turn;
  private int whoseTurn;

  public Hearts(Hand[] hands) {
    setHands(hands);
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
    // TODO create new deck/ shuffle deck/ deal cards/
    setBigRound(1);
    setSmallRound(1);
    setTurn(1);

    resetPoints();

    setDeck(new Deck());
    deck.shuffle();
    dealCards();

    sortCardsInHands();

    // startGiveCardsPhase();
    // startBigRound();

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

  public void endTurn() {

    if (turn == 4)
      endSmallRound();
    hands[whoseTurn].setChosenPlaceCard(-1);
  }

  public void nextTurn() {
    turn += 1;
    // check turn
    whoseTurn += 1;
    if (whoseTurn >= hands.length)
      whoseTurn = 0;
  }

  public void endSmallRound() {
    setTurn(1);
    if (smallRound == 13) {
      endBigRound();
    }

  }

  public void nextSmallRound() {
    // find who win on table
    // and set that person to whose turn
    smallRound += 1;

    int tableWinner = findWhoWinOnTable();
    whoseTurn = tableWinner;
  }

  private int findWhoWinOnTable() {
    return table.findWinner();
  }

  public Hand endBigRound() {
    setSmallRound(1);
    Hand winner = checkEndGameConditionAndFindWinner();

    // add points to keep track scores for each hand
    addScores();

    deck.refresh();
    refreshAllHands();

    return winner;
  }

  private void addScores() {
    for (int i = 0; i < hands.length; i++) {
      scores[i] += hands[i].getPoints();
    }
  }

  // TODO find 2 of clubs function
  // find who have 2 of clubs
  // set whoseTurn = that person

  public void nextBigRound() {
    bigRound += 1;

    deck.shuffle();
    dealCards();
    sortCardsInHands();
  }

  private Hand checkEndGameConditionAndFindWinner() {
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

  private void refreshAllHands() {
    for (Hand hand : hands) {
      hand.refreshHand();
    }
  }

  public void sortCardsInHands() {
    for (Hand hand : getHands()) {
      hand.sortCardsInHand();
    }
  }

  // utils for debug.

  public void printHands() {
    for (Hand hand : getHands()) {
      System.out.println("Hand name: " + hand.getName());
      hand.printCardsInHand();
    }
  }

  public static void main(String[] args) {
    Hand[] hands = { new Player("test1"), new Player("test2"), new Player("test3"), new Player("test4") };
    Hearts hearts = new Hearts(hands);

    hearts.resetGame();

    hearts.printHands();

    hearts.sortCardsInHands();

    hearts.printHands();

  }
}
