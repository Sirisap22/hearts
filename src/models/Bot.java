package models;
import java.util.ArrayList;
import java.util.Map;

public class Bot extends Hand {
  protected boolean CQIsOut = false;
  protected boolean heartIsBroken = false;
  protected Card CQ = new Card(12, Suit.CLUBS);
  protected Card CK = new Card(13, Suit.CLUBS);
  protected Card CA = new Card(14, Suit.CLUBS);
  protected Card H2 = new Card(2, Suit.HEARTS);
  protected Card S2 = new Card(2, Suit.SPADES);
  protected int heartLeft = 13;
  protected int heartInHand = 0;
  protected int overAllScore = 0;
  public Bot(String name) {
    super(name);
  }
  public void addOverAllScore(int amount){
    this.overAllScore += amount;
  }
  public int getOverallScore(){
    return this.overAllScore;
  }
  protected ArrayList<Integer> playableCard(Table table){
    ArrayList<Integer> output = new ArrayList<>();
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(table.playable(card)){
        output.add(cardIndex);
      }
    }
    return output;
  }
  protected boolean has(Card cardin){
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(cardin.equals(card)){
        return true;
      }
    }
    return false;
  }
  public int choose(Table table){
    // choose which card to play
    int []cardScore = new int[super.getCardsInHand().size()];
    //if there is no card can play
    if(playableCard(table).size() == 0){
      //System.out.println(this.name + " Cant play");
      for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
        Card card = super.getCardsInHand().get(cardIndex);
        cardScore[cardIndex] = card.getRank();
        if(card.equals(this.CQ) && (table.cardIsOnTable(this.CK) || table.cardIsOnTable(this.CA))){
          cardScore[cardIndex] += 100;
        }
        if(!table.cardIsOnTable(this.CQ) && !table.suitIsOnTable("hearts")){
          cardScore[cardIndex] += card.getRank();
        }
      }
    }
    else{
      if(super.getCardsInHand().size() == 13){
        for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
          Card card = super.getCardsInHand().get(cardIndex);
          if(card.getSuit().equals(Suit.HEARTS)){
            this.heartLeft--;
          }
        }
      }
      for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
        Card card = super.getCardsInHand().get(cardIndex);
        cardScore[cardIndex] = (int)(card.getRank() - card.getRank()*0.3);
        if(table.suitIsOnTable("hearts")){
          cardScore[cardIndex] += 14-card.getRank();
        }
        if(!CQIsOut && !has(this.CQ)){
          if(card.equals(this.CK) || card.equals(this.CA)){
            cardScore[cardIndex] -= 10;
          }
          if(card.getRank() > table.getMaxRank()){
            cardScore[cardIndex] -= 3;
          }
        }
        if(card.equals(this.H2) && this.heartIsBroken && table.getPlayedNumber() == 0){
          cardScore[cardIndex] += 10;
        }
        if(table.getPlayedNumber() < 3){
          if(!CQIsOut && !has(this.CQ)){
            cardScore[cardIndex] -= card.getRank()/2;
          }
          if(card.equals(this.CQ)){
            cardScore[cardIndex] -= 5;
          }
          if(card.getSuit().equals(Suit.HEARTS) || card.equals(this.CQ)){
            cardScore[cardIndex] -= card.getRank()*0.3;
          }
          else{
            cardScore[cardIndex] += 2;
          }
        }
        if(table.getPlayedNumber() == 3){
          if(card.equals(this.CQ) && (table.cardIsOnTable(this.CA) || table.cardIsOnTable(this.CK))){
            cardScore[cardIndex] += 100;
          }
          if(!table.suitIsOnTable("hearts") && !table.cardIsOnTable(this.CQ)){
            cardScore[cardIndex] += (int)(card.getRank() - card.getRank()*0.3);
          }
        }
        if(!table.playable(card) || (card.getSuit().equals(Suit.HEARTS) && !heartIsBroken)){
          cardScore[cardIndex] = 0;
        }
      }
      //heart number related event
      for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
        Card card = super.getCardsInHand().get(cardIndex);
        if(card.getSuit() == Suit.HEARTS){
          if(this.heartInHand > 5){
            cardScore[cardIndex] -= card.getRank()*0.5;
          }
        }
        cardScore[cardIndex] += 14-card.getRank(); 
      }
    }
    // Clubs queen critical event
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(table.cardIsOnTable(this.CQ) && table.getFirstSuit() == Suit.CLUBS && (card.equals(this.CK) || card.equals(this.CA))){
        cardScore[cardIndex] = 0;
      }
      if(table.cardIsOnTable(this.CQ) && table.getFirstSuit() != Suit.CLUBS && card.getRank() > table.getMaxRank()){
        cardScore[cardIndex] = 0;
      }
    }
    // first to play event
    for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(table.getPlayedNumber() == 0 && card.equals(this.S2)){
        cardScore[cardIndex] = 100;
      }
    }
    // Update if CQ had played
    if(table.cardIsOnTable(this.CQ)){
      this.CQIsOut = true;
    }
    if(table.isBroken()){
      this.heartIsBroken = true;
    }
    // Cut non-playable card
    for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      try{
        if(!card.equals(this.S2) && ((!card.getSuit().equals(table.getFirstSuit())) || (card.getSuit().equals(Suit.HEARTS) && !this.heartIsBroken && table.getPlayedNumber() == 0))){
          cardScore[cardIndex] = -100; 
        }
      }
      catch(Exception e){
        break;
      }
    }
    int[] maxIndex = new int[2];
    maxIndex[0] = 0;
    maxIndex[1] = -10;
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      if(cardScore[cardIndex] > maxIndex[1]){
        maxIndex[0] = cardIndex;
        maxIndex[1] = cardScore[cardIndex];
      }
    }
    //report for debug
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      //System.out.println(card.toString() + " " +cardScore[cardIndex]);
    }
    this.heartLeft -= table.getHeartNumber();
    if(super.getCardsInHand().get(maxIndex[0]).getSuit() == Suit.HEARTS){
      this.heartLeft--;
      this.heartInHand--;
    }
    return maxIndex[0];
  }
  //for debug
  public Card choosenCard(Table table){
    return getCardsInHand().get(choose(table));
  }
  public void setCQIsOut(boolean cQIsOut) {
      CQIsOut = cQIsOut;
  }
  public void setHeartIsBroken(boolean heartIsBroken) {
      this.heartIsBroken = heartIsBroken;
  }
  public boolean hasSuit(Suit s){
    for(Card card : super.getCardsInHand()){
      if(card.getSuit().equals(s)){
        return true;
      }
    }
    return false;
  }
  @Override
  public void giveCard(Hand hand){
     // choose which card to give
  }

  @Override
  public void makeAction() {

  }

}
