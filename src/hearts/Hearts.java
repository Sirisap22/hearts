package hearts;

public class Hearts {
  private Table table;
  private Deck deck;
  private Hand[] hands;
  private int bigRound;
  private int smallRound;
  private String turn;

  public Hearts(Hand[] hands) {
    setHands(hands);
  }

  // getters and setters.

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

  public String getTurn() {
    return this.turn;
  }

  public void setTurn(String turn) {
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
    setDeck(new Deck());
    deck.shuffle();
    dealCards();

    sortCardsInHands();

    // startGiveCardsPhase();
    // startBigRound();

  }

  public void dealCards() {
    Deck deck = getDeck();
    Hand[] hands = getHands();

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
