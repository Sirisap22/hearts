package models;

public class Game {
    public static void main(String[] args) {
        Hand[] player = new Hand[4];
        Table table = new Table();
        Deck deck = new Deck();
        int bigRound = 1;

        //Set player here
        player[0] = new Bot("A");
        player[1] = new Bot("B");
        player[2] = new Bot("C");
        player[3] = new Bot("D");
        
        //let bots join the table
        for(int i = 0; i < player.length; i++){
            if(player[i] instanceof Bot){
                ((Bot)player[i]).joinTable(table);
            }
        }
        //start game
        while(!anyOneDone(player)){
            deck.shuffle();
            //dealing card
            for(int j = 0; j < 13; j++){
                for(int i = 0; i < 4; i++){
                    player[i].addCardInHand(deck.popCard());
                }
            }
            for(int i = 0; i < 4; i++){
                player[i].sortCardsInHand();
            }
            //decide to give the card (left >> right >> opposite >> no giving >> left)
            if(bigRound % 4 != 0){
                for(int i = 0; i < 4; i++){
                    if(player[i] instanceof Bot){
                        ((Bot)player[i]).chooseCardToGive();
                    }
                    else{
                        //If not a bot. Choose card to give here.
                    }
                }
                if(bigRound % 4 == 1){
                    player[0].giveCard(player[1]);
                    player[1].giveCard(player[2]);
                    player[2].giveCard(player[3]);
                    player[3].giveCard(player[0]);
                }
                if(bigRound % 4 == 2){
                    player[0].giveCard(player[3]);
                    player[3].giveCard(player[2]);
                    player[2].giveCard(player[1]);
                    player[1].giveCard(player[0]);
                }
                if(bigRound % 4 == 3){
                    player[0].giveCard(player[2]);
                    player[1].giveCard(player[3]);
                    player[2].giveCard(player[0]);
                    player[3].giveCard(player[1]);
                }
                for(int i = 0; i < 3; i++){
                    player[i].sortCardsInHand();
                }
            }
            //Find who's first
            int First = whoFirst(player);
            //start 13 small round
            for(int i = 0; i < 13; i++){
                //everyone play a card
                for(int j = 0; j < 4; j++){
                    if(First >= 4){
                       First = 0;
                    }
                    Card card = new Card(3, Suit.SPADES);
                    if(player[First] instanceof Bot){
                        card = ((Bot)player[First]).choosenCard();
                    }
                    else{
                        //If not a bot. Choose card to play here.
                        // card = <card that player choose to play>
                    }
                    table.placeCardAt(card, First);
                    player[First].removeCardInHand(card);
                    First++;
                    
                }
                //update status
                table.Update();
                //find the one who's have to get all the cards and get
                int winner = table.findWinner();
                First = winner;
                for(Card card : table.popAllCards()){
                    player[winner].addCardInPile(card);
                }
            }
            //end of small round and processing point
            for(int j = 0; j < 4; j++){
                player[j].addOverAllScore(player[j].getPoints());
                player[j].refreshHand();
            }
            //refresh deck and table
            deck.refresh();
            table.reset();
            bigRound++;
        }
    }
    public static int whoFirst(Hand[] player){
        Card S2 = new Card(2, Suit.CLUBS);
        if(player[0].has(S2)) return 0;
        if(player[1].has(S2)) return 1;
        if(player[2].has(S2)) return 2;
        if(player[3].has(S2)) return 3;
        return -1;
    }
    public static boolean anyOneDone(Hand[] bot){
        for(Hand b : bot){
            if(b.getOverallScore() >= 100){
                return true;
            }
        }
        return false;
    }
}
