package hearts;

public enum Suit {
  CLUBS, DIAMONDS, HEARTS, SPADES;

  public String toString() {
    switch (this) {
    case CLUBS:
      return "clubs";
    case DIAMONDS:
      return "diamonds";
    case HEARTS:
      return "hearts";
    case SPADES:
      return "spades";
    default:
      return null;
    }
  }
}
