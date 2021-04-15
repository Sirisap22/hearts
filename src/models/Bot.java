package models;
import java.util.ArrayList;
import java.util.Map;

public class Bot extends Hand {
  protected Card SQ = new Card(12, Suit.SPADES);
  protected Card SK = new Card(13, Suit.SPADES);
  protected Card SA = new Card(14, Suit.SPADES);
  protected Card H2 = new Card(2, Suit.HEARTS);
  protected Card C2 = new Card(2, Suit.CLUBS);
  protected int heartLeft = 13;
  protected int heartInHand = 0;
  protected int overAllScore = 0;
  protected Table table;
  public Bot(String name) {
    super(name);
  }
  public void joinTable(Table table){
    this.table = table;
  }
  //for using in game
  private int choose(){
    int []cardScore = new int[super.getCardsInHand().size()];
    //if there is no card can play
    if(playableCard(table).size() == 0){
      //System.out.println(this.name + " Cant play");
      for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
        Card card = super.getCardsInHand().get(cardIndex);
        cardScore[cardIndex] = card.getRank();
        if(card.equals(this.SQ) && (table.cardIsOnTable(this.SK) || table.cardIsOnTable(this.SA))){
          cardScore[cardIndex] += 100;
        }
        if(!table.cardIsOnTable(this.SQ) && !table.suitIsOnTable("hearts")){
          cardScore[cardIndex] += card.getRank();
          if(card.getSuit().equals(Suit.HEARTS)){
            cardScore[cardIndex] += card.getRank();
          }
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
        //cardScore[cardIndex] = (int)(card.getRank() - card.getRank()*0.3);
        if(table.suitIsOnTable("hearts")){
          cardScore[cardIndex] += 14-card.getRank();
        }
        if(!table.SQIsOut() && !has(this.SQ)){
          if(card.equals(this.SK) || card.equals(this.SA)){
            cardScore[cardIndex] -= 10;
          }
          if(card.getRank() > table.getMaxRank()){
            cardScore[cardIndex] -= 3;
          }
        }
        if(card.equals(this.H2) && table.isBroken() && table.getPlayedNumber() == 0){
          cardScore[cardIndex] += 10;
        }
        if(table.getPlayedNumber() < 3){
          if(!table.SQIsOut() && !has(this.SQ)){
            cardScore[cardIndex] -= card.getRank()/2;
          }
          if(card.equals(this.SQ)){
            cardScore[cardIndex] -= 5;
          }
          if(card.getSuit().equals(Suit.HEARTS) || card.equals(this.SQ)){
            cardScore[cardIndex] -= card.getRank()*0.3;
          }
          else{
            cardScore[cardIndex] += 2;
          }
        }
        if(table.getPlayedNumber() == 3){
          if(card.equals(this.SQ) && (table.cardIsOnTable(this.SA) || table.cardIsOnTable(this.SK))){
            cardScore[cardIndex] += 100;
          }
          if(!table.suitIsOnTable("hearts") && !table.cardIsOnTable(this.SQ)){
            cardScore[cardIndex] += (int)(card.getRank() - card.getRank()*0.3);
          }
        }
        if(!table.playable(card) || (card.getSuit().equals(Suit.HEARTS) && !table.isBroken())){
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
    // spades queen critical event
    for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(table.cardIsOnTable(this.SQ) && table.getFirstSuit() == Suit.SPADES && (card.equals(this.SK) || card.equals(this.SA))){
        cardScore[cardIndex] = 0;
      }
      if(table.cardIsOnTable(this.SQ) && table.getFirstSuit() != Suit.SPADES && card.getRank() > table.getMaxRank()){
        cardScore[cardIndex] = 0;
      }
    }
    // first to play event
    for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      if(table.getPlayedNumber() == 0 && card.equals(this.C2)){
        cardScore[cardIndex] = 100;
      }
    }
    // Cut non-playable card
    for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      try{
        if(table.getPlayedNumber() != 0 && !card.equals(this.C2) && (!card.getSuit().equals(table.getFirstSuit()))){
          cardScore[cardIndex] = -100; 
        }
        else if((card.getSuit().equals(Suit.HEARTS) && !table.isBroken() && table.getPlayedNumber() == 0)){
          //System.out.println("+++++");
          cardScore[cardIndex] = -100; 
        }
        //System.out.println("cutting : " + card.toString() + ", " + table.isBroken() + ", " + table.getPlayedNumber() + ", " + cardScore[cardIndex]);
      }
      catch(IndexOutOfBoundsException e){
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
    // Update if SQ had played
    table.Update();
    //System.out.println(this.name + " : Score = " + maxIndex[1]);
    return maxIndex[0];
  }
  public void chooseCardToGive(){
     // choose which card to give
     int []res = new int[3];
     int []cardScore = new int[super.getCardsInHand().size()];
     Card []cardbuffer = new Card[3];
     for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
      Card card = super.getCardsInHand().get(cardIndex);
      cardScore[cardIndex] = card.getRank();
      if(card.equals(this.SK) || card.equals(this.SA)){
        cardScore[cardIndex] += 100;
      }
      else if(card.getSuit().equals(Suit.HEARTS)){
        cardScore[cardIndex] += card.getRank();
      }
      else if(card.equals(this.SQ)){
        cardScore[cardIndex] -= 10;
      }
    }
    System.out.println(this.name + " :");
    for(int i = 0; i < 3; i++){ // give 3 card
      int maxScore = 0;
      int pos = -1;
      for(int cardIndex = 0; cardIndex < super.getCardsInHand().size(); cardIndex++){
        if(cardScore[cardIndex] > maxScore){
          maxScore = cardScore[cardIndex];
          pos = cardIndex;
        }
      }
      res[i] = pos;
      cardbuffer[i] = super.getCardsInHand().get(pos);
      System.out.println(cardbuffer[i].toString());
      super.removeCardInHand(cardbuffer[i]);
    }
    for(int i = 0; i < 3; i++){
      this.addCardInHand(cardbuffer[i]);
    }
    this.sortCardsInHand();
    super.setChosenGiveCards(res);
    // for(int i = 0; i < 3; i++){
    //   System.out.println(super.getCardsInHand().get(super.getChosenGiveCards()[i]));
    // }
  }
  public void chooseCardToPlace(){
    this.chosenPlaceCard = choose();
  }
  //for debug
  public Card choosenCard(Table table){
    return getCardsInHand().get(choose());
  }
  public boolean hasSuit(Suit s){
    for(Card card : super.getCardsInHand()){
      if(card.getSuit().equals(s)){
        return true;
      }
    }
    return false;
  }
  public void refreshRound() {
    super.refreshHand();
    this.heartInHand = 0;
    this.heartLeft = 0;
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
    if(table.getPlayedNumber() == 0){
      for(int cardIndex = 0; cardIndex <  super.getCardsInHand().size(); cardIndex++){
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
  @Override
  public void makeAction() {

  }

}
