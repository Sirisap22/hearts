package models;

import java.util.ArrayList;
import java.util.Arrays;

public class Table {
  private ArrayList<Integer> handStack = new ArrayList<Integer>();
  private Card[] cardSlots = new Card[4];

  // index of Hands mean player
  public void placeCardAt(Card card, int slot) {
    handStack.add(slot);
    cardSlots[slot] = card;
  }

  public Card[] popAllCards() {
    Card[] cards = cardSlots.clone();
    handStack.clear();
    Arrays.fill(cardSlots, null);
    return cards;
  }

  public int findWinner() {
    Suit selectedSuit = cardSlots[handStack.get(0)].getSuit();
    int winner = handStack.get(0);
    for (int hand = 0; hand < cardSlots.length; hand++) {
      Card winnerCard = cardSlots[winner];
      Card currentCard = cardSlots[hand];
      if (currentCard.getSuit() == selectedSuit && winnerCard.compareTo(currentCard) == -1) {
        winner = hand;
      }
    }

    return winner;
  }

  // By BillyBacker
  // Use to check if there are a card with the interested suit. For bot.
  public boolean suitIsOnTable(String suit){
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      if(cardSlots[handStack.get(hand)].getSuit().toString().equals(suit)){
        return true;
      }
    }
    return false;
  }
  public Card[] getCardSlot(){
    return cardSlots;
  }
  public boolean cardIsOnTable(Card card){
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      if(cardSlots[handStack.get(hand)].equals(card)){
        return true;
      }
    }
    return false;
  }

  public int getMaxRank(){
    int max = -1;
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      if(cardSlots[handStack.get(hand)].getRank() > max && cardSlots[handStack.get(hand)].getSuit().equals(cardSlots[handStack.get(0)].getSuit())){
        max = cardSlots[handStack.get(hand)].getRank();
      }
    }
    return max;
  }

  public int getminRank(){
    int min = 15;
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      if(cardSlots[handStack.get(hand)].getRank() < min && cardSlots[handStack.get(hand)].getSuit().equals(cardSlots[handStack.get(0)].getSuit())){
        min = cardSlots[handStack.get(hand)].getRank();
      }
    }
    return min;
  }

  public int getPlayedNumber(){
    int number = 0;
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      number += 1;
    }
    return number;
  }

  public boolean playable(Card card){
    if(getPlayedNumber() == 0){
      return true;
    }
    return card.getSuit().equals(cardSlots[handStack.get(0)].getSuit());
  }

  public Suit getFirstSuit(){
    if(getPlayedNumber() == 0){
      return null;
    }
    return this.cardSlots[handStack.get(0)].getSuit();
  }

  public boolean isBroken(){
    boolean hasHeart = false;
    for(int hand = 0; hand < cardSlots.length; hand++) {
      try{ // check if card is available
        Card check = cardSlots[handStack.get(hand)];
      }
      catch(IndexOutOfBoundsException e){ // if not, it's mean there is not other card to think of. So just end the loop.
        break;
      }
      if(cardSlots[handStack.get(hand)].getSuit().equals(Suit.HEARTS)){
        hasHeart = true;
      }
    }
    if(getPlayedNumber() != 0){
      return !cardSlots[handStack.get(0)].getSuit().equals(Suit.HEARTS) && hasHeart;
    }
    return false;
  }

  public int getHeartNumber(){
    int output = 0;
    for(int i = 0; i < getPlayedNumber(); i++){
      if(cardSlots[handStack.get(i)].getSuit() == Suit.HEARTS){
        output++;
      }
    }
    return output;
  }
}
