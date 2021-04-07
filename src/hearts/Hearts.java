package hearts;

import java.util.ArrayList;

public class Hearts {
  private Table table;
  private Deck deck;
  private Hand[] hands;
  private int bigRound;
  private int smallRound;
  private int turn;
  private int whoseTurn;

  public Hearts(Hand[] hands) {
    setHands(hands);
  }

  // getters and setters.

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
  public void startGame() {
    // TODO create new deck/ shuffle deck/ deal cards/
    setBigRound(1);
    setSmallRound(1);
    setTurn(1);

    setDeck(new Deck());
    deck.shuffle();
    dealCards();

    sortCardsInHands();

    // startGiveCardsPhase();
    // startBigRound();

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

  public void nextTurn() {
    turn += 1;
    if (turn > 4) {
      turn = 1;
    }
  }

  public void endTurn() {
    if (turn == 4)
      return;
    hands[turn].setChosenPlaceCard(-1);
  }

  public void nextSmallRound() {
    if (smallRound == 13)
      return;
    smallRound += 1;
  }

  public void endSmallRound() {
    // find who win
    // get card from table to that hand
    // set whose turn to the one who win
  }

  public void nextBigRound() {
    // check if there is hand whose points is greater than 100 and have only one
    // winner
    // return

    // shuffle deck
    // deal cards
    // giveCard
    bigRound += 1;

  }

  public void endBigRound() {
    // check if there is hand whose points is greater than 100 and have only one
    // winner
    // return

    // reset deck
    // reset hand
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

    hearts.startGame();

    hearts.printHands();

    hearts.sortCardsInHands();

    hearts.printHands();

  }
}
