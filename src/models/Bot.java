package models;

public class Bot extends Hand {

  public Bot(String name) {
    super(name);
  }
  public int choose(Table table){
    // choose which card to play
    return 0;
  }
  @Override
  public void giveCard(Hand hand){
     // choose which card to give
  }

  @Override
  public void makeAction() {

  }

}
