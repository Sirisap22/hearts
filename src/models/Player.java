package models;
import java.util.ArrayList;
import java.util.Collections;

public class Player extends Hand {
  public ArrayList<Integer> CardToGive = new ArrayList<>();
  public Sounds sound = new Sounds();
  public Player(String name) {
    super(name);
  }

  @Override
  public void makeAction() {

  }
  @Override
  public void giveCard(Hand hand) {
    Collections.sort(CardToGive, Collections.reverseOrder());
    for (int cardIndex : CardToGive) {
      Card chosenCard = super.cardsInHand.get(cardIndex);
      hand.addCardInHand(chosenCard);
      removeCardInHand(chosenCard);
    }
  }
  public int addPlayerChooseCardToGive(int cardIndex){
    if(CardToGive.contains(cardIndex)){
      CardToGive.remove(Integer.valueOf(cardIndex));
      sound.getSoundSwitchCardPlay();
      return -1;
    }
    else if(CardToGive.size() < 3){
        CardToGive.add(cardIndex);
        sound.getSoundSwitchCardPlay();
        return 1;
    }
    else{
      sound.getSoundWrongPlay();
      return 0;
    }
  }
  public boolean doneSelection(){
    return this.CardToGive.size() == 3;
  }
}
