package models;
import java.util.Random;
public class reinforce {
    public static void main(String[] args) {
        int[][] bestGene = new int[21][15];
        Bot[] bot = new Bot[4];
        bot[0] = new Bot("A");
        bot[1] = new Bot("B");
        bot[2] = new Bot("C");
        bot[3] = new Bot("D");

        Table table = new Table();
        Deck deck = new Deck();
        while(!anyOneDone(bot)){
            System.out.println("Dealing...");
            deck.shuffle();
            for(int j = 0; j < 13; j++){
                bot[0].addCardInHand(deck.popCard());
                bot[1].addCardInHand(deck.popCard());
                bot[2].addCardInHand(deck.popCard());
                bot[3].addCardInHand(deck.popCard());
            }
            for(int i = 0; i < 4; i++){
                bot[i].sortCardsInHand();
            }
            System.out.println("-------------------------------------\n card list:");
            for(int i = 0; i < 4; i++){
                System.out.println("\n"+bot[i].getName());
                for(Card card : bot[i].getCardsInHand()){
                    System.out.println(card.toString());
                }
            }
            System.out.println("-------------------------------------");
            int First = whoFirst(bot);
            System.out.println(bot[First].getName() + " play first");
            System.out.println("Dealing done. starting round");
            for(int i = 0; i < 13; i++){
                for(int j = 0; j < 4; j++){
                    if(First >= 4){
                       First = 0;
                    }
                    Card card = bot[First].choosenCard(table);
                    System.out.print(table.getPlayedNumber() + ". " + bot[First].getName() + " played " + card.toString() + " Break : " + !bot[First].hasSuit(table.getFirstSuit()) + "\t");
                    table.placeCardAt(card, First);
                    bot[First].removeCardInHand(card);
                    for(int k = 0; k < 4; k++){
                        try{
                            System.out.print(table.getCardSlot()[k].toString() + "\t");                       
                        }
                        catch(Exception e){
                            System.out.print("-\t");   
                        }
                    }
                    System.out.print("\n");
                    First++;
                    
                }
                int winner = table.findWinner();
                First = winner;
                Suit firstCardSuit = table.getFirstSuit();
                int winnerScore = bot[winner].getPoints();
                for(Card card : table.popAllCards()){
                    bot[winner].addCardInPile(card);
                }
                System.out.println(bot[winner].getName() + " has to keep the card and got " + (bot[winner].getPoints() - winnerScore) + " point, first suit : " + firstCardSuit + ", Is broken : " + table.isBroken() + "\n");
            }
            System.out.println("End the round\n---------------------------------------------------------\nreport:\n");
            for(int j = 0; j < 4; j++){
                System.out.println(bot[j].getName() + " get " + bot[j].getPoints());
                bot[j].addOverAllScore(bot[j].getPoints());
                bot[j].refreshHand();
                System.out.println("\t\t\t\t\t\t" + bot[j].getName() + " get " + bot[j].getOverallScore());
            }
            deck.refresh();
            System.out.println("---------------------------------------------------------");
        }
    }
    public static int[][] randWeight(){
        Random rand = new Random();
        int[][] res = new int[15][21];
        for(int i = 0; i < 21; i++){
            for(int j = 0; j < 15; j++){
                res[j][i] = rand.nextInt(20);
            }
        }
        return res.clone();
    }
    public static int whoFirst(Bot[] bot){
        Card S2 = new Card(2, Suit.CLUBS);
        if(bot[0].has(S2)) return 0;
        if(bot[1].has(S2)) return 1;
        if(bot[2].has(S2)) return 2;
        if(bot[3].has(S2)) return 3;
        return -1;
    }
    public static boolean anyOneDone(Bot[] bot){
        for(Bot b : bot){
            if(b.getOverallScore() >= 10){
                return true;
            }
        }
        return false;
    }
}
