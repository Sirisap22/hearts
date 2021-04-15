package models;

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

  public static int compare(Suit s1, Suit s2) {
    if (s1 == s2)
      return 0;

    switch (s1) {
    case CLUBS:
      return compareClubsWith(s2);
    case DIAMONDS:
      return compareDiamondsWith(s2);
    case HEARTS:
      return compareHeartsWith(s2);
    case SPADES:
      return compareSpadesWith(s2);
    default:
      return 0;
    }
  }

  private static int compareHeartsWith(Suit s) {
    return -1;
  }

  private static int compareSpadesWith(Suit s) {
    switch (s) {
    case HEARTS:
      return 1;
    default:
      return -1;
    }
  }

  private static int compareDiamondsWith(Suit s) {
    switch (s) {
    case HEARTS:
      return 1;
    case SPADES:
      return 1;
    default:
      return -1;
    }
  }

  private static int compareClubsWith(Suit s) {
    return 1;
  }

}