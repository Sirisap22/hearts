package components.game;

import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import components.card.CardController;
import java.io.File;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.Pair;
import models.Bot;
import models.Card;
import models.Deck;
import models.Hand;
import models.Hearts;
import models.Player;
import models.Suit;
import models.Table;
import utils.ComponentLoader;

public class GameController implements Initializable {

    double setX = 0;
    double setY = 0;

    final double WINDOW_WIDTH = 1400;
    final double WINDOW_HEIGHT = 700;
    final double CARD_WIDTH = 50;
    final double CARD_HEIGHT = 100;
    final double X_CENTER = WINDOW_WIDTH/2 - CARD_WIDTH;
    final double Y_CENTER = WINDOW_HEIGHT/2 - CARD_HEIGHT;
    final double X_PLAYER = 600;
    final double Y_PLAYER = 600;
    final double X_FIRST_BOT = 300;
    final double Y_FIRST_BOT = 300;
    final double X_SECOND_BOT = 840;
    final double Y_SECOND_BOT = 70;
    final double X_THRID_BOT = 1100;
    final double Y_THRID_BOT = 420;

    Hearts hearts;
    State currentState;
    ComponentLoader<GameController> loader = new ComponentLoader<>();
    HashMap<String, Pair<ImageView, CardController>> cards = new HashMap<>();

    @FXML
    private Pane root = new Pane();
    @FXML
    private Button deal;
    @FXML
    private Button swap;
    @FXML
    private Button exit;
    @FXML
    private Label notification;

    @Override
    public void initialize(URL url, ResourceBundle rb)  {
        initCurrentState();
        initDeckView();
        initHearts();
        initBots();
        renderAllCards();
        swap.setVisible(false);

    }

    private void initCurrentState() {
        currentState = State.INIT;
    }

    private void initDeckView() {
        try {
            _initDeckView();
        } catch(Exception e) {
            System.out.println(e);
        }
    }

    private void _initDeckView() throws IOException {
        Deck deck = new Deck();
            for (var card : deck) {
                Pair<ImageView, CardController> cardComponent = loader.loadComponent(this,
                        "/components/card/Card.fxml");
                CardController tempCon = cardComponent.getValue();
                tempCon.setCard(card);
                tempCon.setFacingDown(false);
                tempCon.updateCardImage();
                ImageView tempView = cardComponent.getKey();
                tempView.setX(X_CENTER);
                tempView.setY(Y_CENTER);
                tempView.setFitHeight(CARD_HEIGHT);
                tempView.setFitWidth(CARD_WIDTH);

                this.cards.put(card.toString(), cardComponent);
            }
    }

    private void initHearts() {
        Hand[] allHand = {new Player("Player"), new Bot("Bot1"), new Bot("Bot2"), new Bot("Bot3")};
        hearts = new Hearts(allHand);
        hearts.resetGame();
    }

    private void initBots() {
        Hand[] hands = hearts.getHands();
        for (int i = 0; i < hands.length; i++) {
            if (hands[i] instanceof Bot) {
                ((Bot) hands[i]).joinTable(hearts.getTable());
            }
        }
    }

    private void renderAllCards() {
        var it = cards.entrySet().iterator();
        while (it.hasNext()) {
            var pair = it.next();
            // System.out.println(pair.getKey() + " = " + pair.getValue());
            var cardView = pair.getValue().getKey();
            root.getChildren().add(cardView);
            // it.remove(); // avoids a ConcurrentModificationException 
        }
    }

    private void addCardViewToParent(ImageView cardView) {
        root.getChildren().add(cardView);
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/components/main/Main.fxml"));
        Scene gameScene = new Scene(root);
        gameScene.setFill(Color.TRANSPARENT);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();   
        window.setScene(gameScene);
        root.setOnMousePressed(e -> {
            setX = e.getSceneX();
            setY = e.getSceneY();
        });
        root.setOnMouseDragged(e -> {
            window.setX(e.getScreenX() - setX);
            window.setY(e.getScreenY() - setY);
        });
    }

    @FXML
    private void dealCards(ActionEvent event) {
        hearts.dealCards(); 
        sortCardsInHand();

        deal.setVisible(false);
        changeState(State.GIVE);
    }

    private void refreshCardPosition() {
        Hand[] hands = hearts.getHands();
        for (int handIndex = 0; handIndex< hands.length; handIndex++) {
            var cardsInHand = hands[handIndex].getCardsInHand();
            double offset = 0;
            double offSetChangeRate = getOffSetChangeRate(handIndex);
            for (var card: cardsInHand) {
               var cardView = cards.get(card.toString()).getKey();
               removeCardViewFromParent(cardView);
               setCardViewPositionByHandIndex(handIndex, cardView, offset);
               addCardViewToParent(cardView);
               offset += offSetChangeRate;
            }
        }
    }

    private void removeCardViewFromParent(ImageView cardView) {
        root.getChildren().remove(cardView);
    }

    private void sortCardsInHand() {
        hearts.sortCardsInHands();
        refreshCardPosition();
    }

    private void setCardViewPositionByHandIndex(int handIndex, ImageView cardView, double offset) {
        switch(handIndex) {
            case 0:
                cardView.setX(X_PLAYER + offset);
                cardView.setY(Y_PLAYER);
                cardView.setRotate(0);
                break;
            case 1:
                cardView.setX(X_FIRST_BOT);
                cardView.setY(Y_FIRST_BOT + offset);
                cardView.setRotate(90);
                break;
            case 2:
                cardView.setX(X_SECOND_BOT + offset);
                cardView.setY(Y_SECOND_BOT);
                cardView.setRotate(180);
                break;
            case 3:
                cardView.setX(X_THRID_BOT);
                cardView.setY(Y_THRID_BOT + offset);
                cardView.setRotate(270);
                break;
        }
    }

    private double getOffSetChangeRate(int handIndex) {
        switch(handIndex){
            case 0:
                return 20;
            case 1:
                return 10;
            case 2:
                return -20;
            case 3:
                return -10;
            default:
                return 0;
        }
    }

    private void changeState(State state) {
        currentState = state;
        switch (currentState) {
            case INIT:
                break;
            case GIVE:
                giveState();
                break;
            case PLAY:
                playState();
                break;
            case END:
                break;
        }
    }

    private void giveState() {
        swap.setVisible(true);
        botsChooseCardsToGive();
        playerChooseCardsToGive();
    }

    private void playState() {
        int whoFirst = hearts.whoFirst();
        hearts.setWhoseTurn(whoFirst);
        updateWhoseTurnNotification();
        playerChooseCardsToPlace();
        
        playCards();
        
    }

    private void playCards() {
          switch (hearts.getWhoseTurn()) {
              case 0:
                break;
              case 1:
                firstBotChooseCardsToPlace();
                break;
              case 2:
                secondBotChooseCardsToPlace();
                break;
              case 3:
                thirdBotChooseCardsToPlace();
                break;
          }
    }

    private void updateWhoseTurnNotification() {
        notification.setText(String.format("%s's Turn.", hearts.getHands()[hearts.getWhoseTurn()].getName()));
    }

    private void botsChooseCardsToGive(){
        Hand[] player = hearts.getHands();
        for(int i = 0; i < 4; i++){
            if(player[i] instanceof Bot){
                Bot bot = (Bot)(player[i]);
                bot.chooseCardToGive();
            }
        }
    }
    private void playerChooseCardsToGive() {
        Hand player = hearts.getHands()[0];
        ArrayList<Card> cardsInPlayerHand = player.getCardsInHand();
        for (int cardIndex = 0; cardIndex < cardsInPlayerHand.size(); cardIndex++) {
            var cardView = cards.get(cardsInPlayerHand.get(cardIndex).toString()).getKey();
            addChooseToGiveEvent(cardView, cardIndex);
        }
    }

    private void addChooseToGiveEvent(ImageView cardView, int cardIndex) {
        cardView.setOnMouseClicked(event -> onMouseClickedToGive(event, cardIndex));
    }

    private void onMouseClickedToGive(MouseEvent event, int cardIndex) {
        Player player = (Player)hearts.getHands()[0];
        int response = player.addPlayerChooseCardToGive(cardIndex);

        var cardView = (ImageView)event.getSource();

        if (response == 1) {
            offsetCardView(cardView, -10);
        } else if (response == -1) {
            offsetCardView(cardView, 10);
        }

    }

    private void playerChooseCardsToPlace() {
        updateWhoseTurnNotification();
        updateChooseToPlaceEvent();
    }

    private void updateChooseToPlaceEvent() {
        Hand player = hearts.getHands()[0];
        ArrayList<Card> cardsInPlayerHand = player.getCardsInHand();
        for (int cardIndex = 0; cardIndex < cardsInPlayerHand.size(); cardIndex++) {
            var cardView = cards.get(cardsInPlayerHand.get(cardIndex).toString()).getKey();
            addChooseToPlaceEvent(cardView, cardIndex);
        }
    }
    private void firstBotChooseCardsToPlace(){
        System.out.println("bot1 turn");
        updateWhoseTurnNotification();
        botPlaceCard(1);
        var isEnd = checkEndRound();
        if (!isEnd)
            hearts.setWhoseTurn(2);
            secondBotChooseCardsToPlace();
    }
    private void secondBotChooseCardsToPlace(){
        System.out.println("bot2 turn");
        updateWhoseTurnNotification();
        botPlaceCard(2);
        var isEnd = checkEndRound();
        if (!isEnd)
            hearts.setWhoseTurn(3);
            thirdBotChooseCardsToPlace();

    }
    private void thirdBotChooseCardsToPlace(){
        System.out.println("bot3 turn");
        updateWhoseTurnNotification();
        botPlaceCard(3);
        var isEnd = checkEndRound();
        if (!isEnd) {
            hearts.setWhoseTurn(0);
            updateWhoseTurnNotification();
        }
    }

    private void botPlaceCard(int handIndex) {
        try {
            _botPlaceCard(handIndex);
        } catch(InterruptedException e) {
            System.out.println(e);
        }
    }
    
    private void _botPlaceCard(int handIndex) throws InterruptedException {
        Bot bot = (Bot)hearts.getHands()[handIndex];
        bot.chooseCardToPlace();
        int cardIndex = bot.getChosenPlaceCard();
        Card card = bot.getCardsInHand().get(cardIndex);

        System.out.println("BOT HAND_INDEX = " + handIndex);


        hearts.getTable().placeCardAt(card, handIndex);
        bot.removeCardInHand(card);
        hearts.getTable().Update();
        botPlaceCardView(cards.get(card.toString()).getKey(), handIndex);
        _tempDebug("BOT_PLACE_CARD_" + handIndex);
        pause(3);

    }

    private void botPlaceCardView(ImageView cardView, int handIndex) {
        switch(handIndex) {
            case 1:
                cardView.setX(X_CENTER);
                cardView.setY(Y_CENTER + 100);
                break;
            case 2:
                cardView.setX(X_CENTER + 50);
                cardView.setY(Y_CENTER + 40);
                break;
            case 3:
                cardView.setX(X_CENTER + 100);
                cardView.setY(Y_CENTER + 100);
                break;
        }
    }


    private boolean checkEndRound() {
        try {
            return _checkEndRound();
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        return false;
    }


    private boolean _checkEndRound() throws InterruptedException {
        boolean isEnd = false;
        System.out.println("");
        if(hearts.getSmallRound() >= 13){
            isEnd = true;
            changeState(State.END);
            System.out.println("End round 1");
        }
        else if(hearts.getTable().getPlayedNumber() >= 4){
            System.out.println("End small");
            pause(5);
            isEnd = true;
            hearts.setSmallRound(hearts.getSmallRound() + 1);
            clearTableView(); 
            _findWinner();
            refreshCardPosition();
            updateChooseToPlaceEvent();
        }

        return isEnd;
    }

    private void clearTableView() {
        Card[] cardsOnTable = hearts.getTable().getCardSlot();
        System.out.println("FROM clear = " + cardsOnTable);
        for (Card card: cardsOnTable) {
            System.out.println(card);
            ImageView cardView = cards.get(card.toString()).getKey();
            removeCardViewFromParent(cardView);
        }
    }
    private void _tempDebug(String name) {
        Card[] cardsOnTable = hearts.getTable().getCardSlot();
        System.out.println("FROM " + name +" clear = " + cardsOnTable);
        for (Card card: cardsOnTable) {
            System.out.println(card);
        }
    }

    private void _findWinner(){
        System.out.println("CURRENT = " + hearts.getTable().getPlayedNumber() );
        if(hearts.getTable().getPlayedNumber() < 4){
            return;
        }
        int winner = hearts.getTable().findWinner();
        hearts.setWhoseTurn(winner);
            for(Card card : hearts.getTable().popAllCards()){
                hearts.getHands()[winner].addCardInPile(card);
            }
        switch(winner){
            case 0:
                break;
            case 1:
                firstBotChooseCardsToPlace();
                break;
            case 2:
                secondBotChooseCardsToPlace();
                break;
            case 3:
                thirdBotChooseCardsToPlace();
                break;
            default:
                System.out.println("findWinner failed");
        }
    }

    private void addChooseToPlaceEvent(ImageView cardView, int cardIndex) {
        cardView.setOnMouseClicked(event -> onMouseClickedToPlace(event, cardIndex));
    }

    private void onMouseClickedToPlace(MouseEvent event, int cardIndex) {
        Hand player = hearts.getHands()[0];
        Card card = player.getCardsInHand().get(cardIndex);
        System.out.println("!isTurn() = " + !isTurn(0));
        System.out.println("!canPlay = " + !canPlay(player, card));
        System.out.println("hasTwoClubs = " + hasTwoClubs(player));
        System.out.println("!isTwoClubs = " + !isTwoClubs(card));
        if(!isTurn(0) || !canPlay(player, card))
            return;
        if(hasTwoClubs(player) && !isTwoClubs(card)){
            return;
        }
        var cardView = (ImageView)event.getSource();
        playerPlaceCard(cardView, cardIndex);

    }

    private boolean canPlay(Hand hand,Card card) {
        return hearts.getTable().playable(card) || !hasRightSuit(hand);
    }

    private boolean isTurn(int whoseTurn) {
        return hearts.getWhoseTurn() == whoseTurn;
    }

    private boolean hasTwoClubs(Hand hand){
        return hand.has(new Card(2, Suit.CLUBS));
    }

    private boolean isTwoClubs(Card card) {
        return card.compareTo(new Card(2, Suit.CLUBS)) == 0;
    }

    private boolean hasRightSuit(Hand hand) {
        for(Card card : hand.getCardsInHand()){
            if(hearts.getTable().getFirstSuit().equals(card.getSuit())){
                return true;
            }
        }
        return false;
    }
    private void playerPlaceCard(ImageView cardView, int cardIndex) {
        updateWhoseTurnNotification();

        Player player = (Player)hearts.getHands()[0];
        Card card = player.getCardsInHand().get(cardIndex);
        hearts.getTable().placeCardAt(card, 0);
        player.removeCardInHand(card);
        cardView.setX(X_CENTER + 50);
        cardView.setY(Y_CENTER + 160);

        hearts.getTable().Update();
        var isEnd = checkEndRound();
        if (!isEnd)
            hearts.setWhoseTurn(1);
            firstBotChooseCardsToPlace();
    }

    private void offsetCardView(ImageView cardView, double offset) {
        double y = cardView.getY();
        cardView.setY(y + offset);
    }

    @FXML
    private void swapCards() {
        if(hearts.getBigRound() % 4 != 0){
            for(int i = 0; i < 4; i++){
                Hand p = hearts.getHands()[i];
                if(p instanceof Bot){
                    ((Bot)p).chooseCardToGive();
                }
            }
            if(hearts.getBigRound() % 4 == 1){
                leftSwap();                
            }
            if(hearts.getBigRound() % 4 == 2){
                rightSwap(); 
            }
            if(hearts.getBigRound() % 4 == 3){
                oppositSwap(); 
            }
        }

        swap.setVisible(false);

        sortCardsInHand();
        changeState(State.PLAY);

    }

    private void leftSwap() {
        Hand[] player = hearts.getHands();
        player[0].giveCard(player[1]);
        player[1].giveCard(player[2]);
        player[2].giveCard(player[3]);
        player[3].giveCard(player[0]);
    }

    private void rightSwap() {
        Hand[] player = hearts.getHands();
        player[0].giveCard(player[3]);
        player[3].giveCard(player[2]);
        player[2].giveCard(player[1]);
        player[1].giveCard(player[0]);
    }

    private void oppositSwap() {
        Hand[] player = hearts.getHands();
        player[0].giveCard(player[2]);
        player[1].giveCard(player[3]);
        player[2].giveCard(player[0]);
        player[3].giveCard(player[1]);
    }

    private void pause(int sec) {
        PauseTransition pause = new PauseTransition(Duration.seconds(sec));
        pause.play();
    }





//     @FXML
//     private void dealCard(ActionEvent event) {
//         // test
//         int j = 0;
//         for (int i = 0; i < 13; i++) {
//             // Player1
//             // if(j%2!=0){
//             // rg[j].setRotate(90);
//             // }
//             // PathTransition path1 = new PathTransition(Duration.millis(500),
//             //         new Line(posCenterX, posCenterY, posXPlayer1, posyPlayer1), card.get(j).getKey());
//             card.get(j).getKey().setX(posXPlayer1);
//             card.get(j).getKey().setY(posyPlayer1);
//             player1.add(card.get(j));
//             j++;
//             // path1.play();
//             posXPlayer1 += 20;

//             // player 2
//             // if (j % 2 != 0) {
//             //     card.get(j).getKey().setRotate(90);
//             // }
//             // PathTransition path2 = new PathTransition(Duration.millis(500),
//             //         new Line(posCenterX, posCenterY, posXPlayer2, posyPlayer2), card.get(j).getKey());
//             card.get(j).getKey().setX(posXPlayer2);
//             card.get(j).getKey().setY(posyPlayer2);
//             player2.add(card.get(j));
//             j++;
//             posyPlayer2 += 10;
//             // path2.play();

//             // player3
//             // if(j%2!=0){
//             // rg[j].setRotate(90);
//             // }
//             // PathTransition path3 = new PathTransition(Duration.millis(500),
//             //         new Line(posCenterX, posCenterY, posXPlayer3, posyPlayer3), card.get(j).getKey());
//             card.get(j).getKey().setX(posXPlayer3);
//             card.get(j).getKey().setY(posyPlayer3);
//             player3.add(card.get(j));
//             j++;
//             posXPlayer3 -= 20;
//             // path3.play();

//             // player4
//             // if (j % 2 != 0) {
//             //     card.get(j).getKey().setRotate(-90);
//             // }
//             // PathTransition path4 = new PathTransition(Duration.millis(500),
//             //         new Line(posCenterX, posCenterY, posXPlayer4, posyPlayer4), card.get(j).getKey());
//             card.get(j).getKey().setX(posXPlayer4);
//             card.get(j).getKey().setY(posyPlayer4);
//             player4.add(card.get(j));
//             j++;
//             posyPlayer4 -= 10;
//             // path4.play();

//         }
//         hearts.dealCards();
//         hearts.printHands();
//         int first = hearts.whoFirst();
//         Hand[] hands = hearts.getHands();

//         if (hands[first] instanceof Bot) {
//             int cc = ((Bot) hands[first]).choose();
//             System.out.println("bot choose " + cc);
//             System.out.println(hands[first].getCardsInHand().get(cc).toString());

//         }

//         // hearts.dealCard
//         btnDeal.setVisible(false);
//         sortCardsInHands();
//         refreshCard();
//         pickCard();
//     }

//     private void pickCard() {

//         for (int i = 0; i < player1.size(); i++) {
//             player1.get(i).getKey().setOnMouseClicked(new EventHandler<MouseEvent>() {
//                 @Override
//                 public void handle(MouseEvent e) {
//                     ImageView temp = (ImageView) e.getSource();
//                     // System.out.println(temp==player1.get(0).getKey());
//                     if (temp.getY() == 590) {
//                         // PathTransition slideDown = new PathTransition(Duration.millis(100),
//                         //         new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() + 10), temp);
//                         temp.setY(temp.getY() + 10);
//                         // slideDown.play();
//                         for (int i = 0; i < p1Choose.size(); i++) {
//                             if (temp == p1Choose.get(i).getKey()) {
//                                 System.out.println(p1Choose);
//                                 p1Choose.remove(i);
//                                 p1ChooseIndex.remove(i);
//                                 System.out.println(p1ChooseIndex);
//                             }
//                         }

//                     } else {
//                         // PathTransition slideUp = new PathTransition(Duration.millis(100),
//                                 // new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() - 10), temp);
//                         temp.setY(temp.getY() - 10);
//                         // slideUp.play();
//                         for (int i = 0; i < player1.size(); i++) {
//                             if (temp == player1.get(i).getKey()) {
//                                 p1Choose.add(player1.get(i));
//                                 p1ChooseIndex.add(i);
//                                 System.out.println(p1Choose);
//                                 System.out.println(p1ChooseIndex);
//                             }
//                         }
//                     }
//                 }
//             });
//         }
//         // set choosen cards
//         // p1Choose.add(player1.get(0));
//         // p1Choose.add(player1.get(1));
//         // p1Choose.add(player1.get(2));

//         Bot bot1 = (Bot)hearts.getHands()[1];
//         Bot bot2 = (Bot)hearts.getHands()[2];
//         Bot bot3 = (Bot)hearts.getHands()[3];

//         bot1.chooseCardToGive();
//         bot2.chooseCardToGive();
//         bot3.chooseCardToGive();
        
//         p2Choose.add(player2.get(bot1.getChosenGiveCards()[0]));
//         p2ChooseIndex.add(bot1.getChosenGiveCards()[0]);
//         p2Choose.add(player2.get(bot1.getChosenGiveCards()[1]));
//         p2ChooseIndex.add(bot1.getChosenGiveCards()[1]);
//         p2Choose.add(player2.get(bot1.getChosenGiveCards()[2]));
//         p2ChooseIndex.add(bot1.getChosenGiveCards()[2]);

//         p3Choose.add(player3.get(bot2.getChosenGiveCards()[0]));
//         p3ChooseIndex.add(bot2.getChosenGiveCards()[0]);
//         p3Choose.add(player3.get(bot2.getChosenGiveCards()[1]));
//         p3ChooseIndex.add(bot2.getChosenGiveCards()[1]);
//         p3Choose.add(player3.get(bot2.getChosenGiveCards()[2]));
//         p3ChooseIndex.add(bot2.getChosenGiveCards()[2]);

//         p4Choose.add(player4.get(bot3.getChosenGiveCards()[0]));
//         p4ChooseIndex.add(bot3.getChosenGiveCards()[0]);
//         p4Choose.add(player4.get(bot3.getChosenGiveCards()[1]));
//         p4ChooseIndex.add(bot3.getChosenGiveCards()[1]);
//         p4Choose.add(player4.get(bot3.getChosenGiveCards()[2]));
//         p4ChooseIndex.add(bot3.getChosenGiveCards()[2]);

//         swap.setVisible(true);
//     }

//     @FXML
//     public void leftSwap() {
//         p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
//         p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
//         p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);

//         // place of cards
//         double P1Card1X = p1Choose.get(0).getKey().getX();
//         double P1Card1Y = p1Choose.get(0).getKey().getY();
//         double P1Card2X = p1Choose.get(1).getKey().getX();
//         double P1Card2Y = p1Choose.get(1).getKey().getY();
//         double P1Card3X = p1Choose.get(2).getKey().getX();
//         double P1Card3Y = p1Choose.get(2).getKey().getY();

//         double P2Card1X = p2Choose.get(0).getKey().getX();
//         double P2Card1Y = p2Choose.get(0).getKey().getY();
//         double P2Card2X = p2Choose.get(1).getKey().getX();
//         double P2Card2Y = p2Choose.get(1).getKey().getY();
//         double P2Card3X = p2Choose.get(2).getKey().getX();
//         double P2Card3Y = p2Choose.get(2).getKey().getY();

//         double P3Card1X = p3Choose.get(0).getKey().getX();
//         double P3Card1Y = p3Choose.get(0).getKey().getY();
//         double P3Card2X = p3Choose.get(1).getKey().getX();
//         double P3Card2Y = p3Choose.get(1).getKey().getY();
//         double P3Card3X = p3Choose.get(2).getKey().getX();
//         double P3Card3Y = p3Choose.get(2).getKey().getY();

//         double P4Card1X = p4Choose.get(0).getKey().getX();
//         double P4Card1Y = p4Choose.get(0).getKey().getY();
//         double P4Card2X = p4Choose.get(1).getKey().getX();
//         double P4Card2Y = p4Choose.get(1).getKey().getY();
//         double P4Card3X = p4Choose.get(2).getKey().getX();
//         double P4Card3Y = p4Choose.get(2).getKey().getY();

//         // Transition
//         // PathTransition path1card1 = new PathTransition(Duration.millis(500),
//                 // new Line(P1Card1X, P1Card1Y, P2Card1X, P2Card1Y), p1Choose.get(0).getKey());
//         p1Choose.get(0).getKey().setX(P2Card1X);
//         p1Choose.get(0).getKey().setY(P2Card1Y);
//         p1Choose.get(0).getKey().setRotate(90);
//         // PathTransition path1card2 = new PathTransition(Duration.millis(500),
//                 // new Line(P1Card2X, P1Card2Y, P2Card2X, P2Card2Y), p1Choose.get(1).getKey());
//         p1Choose.get(1).getKey().setX(P2Card2X);
//         p1Choose.get(1).getKey().setY(P2Card2Y);
//         p1Choose.get(1).getKey().setRotate(90);
//         // PathTransition path1card3 = new PathTransition(Duration.millis(500),
//                 // new Line(P1Card3X, P1Card3Y, P2Card3X, P2Card3Y), p1Choose.get(2).getKey());
//         p1Choose.get(2).getKey().setX(P2Card3X);
//         p1Choose.get(2).getKey().setY(P2Card3Y);
//         p1Choose.get(2).getKey().setRotate(90);

//         // PathTransition path2card1 = new PathTransition(Duration.millis(500),
//                 // new Line(P2Card1X, P2Card1Y, P3Card1X, P3Card1Y), p2Choose.get(0).getKey());
//         p2Choose.get(0).getKey().setX(P3Card1X);
//         p2Choose.get(0).getKey().setY(P3Card1Y);
//         p2Choose.get(0).getKey().setRotate(0);
//         // PathTransition path2card2 = new PathTransition(Duration.millis(500),
//                 // new Line(P2Card2X, P2Card2Y, P3Card2X, P3Card2Y), p2Choose.get(1).getKey());
//         p2Choose.get(1).getKey().setX(P3Card2X);
//         p2Choose.get(1).getKey().setY(P3Card2Y);
//         p2Choose.get(1).getKey().setRotate(0);
//         // PathTransition path2card3 = new PathTransition(Duration.millis(500),
//                 // new Line(P2Card3X, P2Card3Y, P3Card3X, P3Card3Y), p2Choose.get(2).getKey());
//         p2Choose.get(2).getKey().setX(P3Card3X);
//         p2Choose.get(2).getKey().setY(P3Card3Y);
//         p2Choose.get(2).getKey().setRotate(0);

//         // PathTransition path3card1 = new PathTransition(Duration.millis(500),
//                 // new Line(P3Card1X, P3Card1Y, P4Card1X, P4Card1Y), p3Choose.get(0).getKey());
//         p3Choose.get(0).getKey().setX(P4Card1X);
//         p3Choose.get(0).getKey().setY(P4Card1Y);
//         p3Choose.get(0).getKey().setRotate(-90);
//         // PathTransition path3card2 = new PathTransition(Duration.millis(500),
//                 // new Line(P3Card2X, P3Card2Y, P4Card2X, P4Card2Y), p3Choose.get(1).getKey());
//         p3Choose.get(1).getKey().setX(P4Card2X);
//         p3Choose.get(1).getKey().setY(P4Card2Y);
//         p3Choose.get(1).getKey().setRotate(-90);
//         // PathTransition path3card3 = new PathTransition(Duration.millis(500),
//         //         new Line(P3Card3X, P3Card3Y, P4Card3X, P4Card3Y), p3Choose.get(2).getKey());
//         p3Choose.get(2).getKey().setX(P4Card3X);
//         p3Choose.get(2).getKey().setY(P4Card3Y);
//         p3Choose.get(2).getKey().setRotate(-90);

//         // PathTransition path4card1 = new PathTransition(Duration.millis(500),
//         //         new Line(P4Card1X, P4Card1Y, P1Card1X, P1Card1Y), p4Choose.get(0).getKey());
//         p4Choose.get(0).getKey().setX(P1Card1X);
//         p4Choose.get(0).getKey().setY(P1Card1Y);
//         p4Choose.get(0).getKey().setRotate(0);
//         // PathTransition path4card2 = new PathTransition(Duration.millis(500),
//         //         new Line(P4Card2X, P4Card2Y, P1Card2X, P1Card2Y), p4Choose.get(1).getKey());
//         p4Choose.get(1).getKey().setX(P1Card2X);
//         p4Choose.get(1).getKey().setY(P1Card2Y);
//         p4Choose.get(1).getKey().setRotate(0);
//         // PathTransition path4card3 = new PathTransition(Duration.millis(500),
//         //         new Line(P4Card3X, P4Card3Y, P1Card3X, P1Card3Y), p4Choose.get(2).getKey());
//         p4Choose.get(2).getKey().setX(P1Card3X);
//         p4Choose.get(2).getKey().setY(P1Card3Y);
//         p4Choose.get(2).getKey().setRotate(0);
// // change card
//         player1.set(p1ChooseIndex.get(0), p4Choose.get(0));
//         player1.set(p1ChooseIndex.get(1), p4Choose.get(1));
//         player1.set(p1ChooseIndex.get(2), p4Choose.get(2));
//         player2.set(p2ChooseIndex.get(0), p1Choose.get(0));
//         player2.set(p2ChooseIndex.get(1), p1Choose.get(1));
//         player2.set(p2ChooseIndex.get(2), p1Choose.get(2));
//         player3.set(p3ChooseIndex.get(0), p2Choose.get(0));
//         player3.set(p3ChooseIndex.get(1), p2Choose.get(1));
//         player3.set(p3ChooseIndex.get(2), p2Choose.get(2));
//         player4.set(p4ChooseIndex.get(0), p3Choose.get(0));
//         player4.set(p4ChooseIndex.get(1), p3Choose.get(1));
//         player4.set(p4ChooseIndex.get(2), p3Choose.get(2));

//         // animation
//         // path1card1.play();
//         // path1card2.play();
//         // path1card3.play();
//         // path2card1.play();
//         // path2card2.play();
//         // path2card3.play();
//         // path3card1.play();
//         // path3card2.play();
//         // path3card3.play();
//         // path4card1.play();
//         // path4card2.play();
//         // path4card3.play();

//         // remove choose card
//         // p1Choose.removeAll();
//         // p1ChooseIndex.removeAll();
//         // p2Choose.removeAll();
//         // p2ChooseIndex.removeAll();
//         // p3Choose.removeAll();
//         // p3ChooseIndex.removeAll();
//         // p4Choose.removeAll();
//         // p4ChooseIndex.removeAll();
//         swap.setVisible(false);

//         // refreshCard();
//         sortCardsInHands();

//         // swap in hearts
//         var hands = hearts.getHands();
//         hands[0].setChosenGiveCards(new int[]{p1ChooseIndex.get(0).intValue(), p1ChooseIndex.get(1).intValue(), p1ChooseIndex.get(2).intValue()});
//         hands[0].giveCard(hands[1]);
//         hands[1].giveCard(hands[2]);
//         hands[2].giveCard(hands[3]);
//         hands[3].giveCard(hands[0]);
//         refreshCard();
//         begin();
//     }

//     public void rightSwap(){
//         p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
//         p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
//         p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);
//         // place of cards
//         double P1Card1X = p1Choose.get(0).getKey().getX();
//         double P1Card1Y = p1Choose.get(0).getKey().getY();
//         double P1Card2X = p1Choose.get(1).getKey().getX();
//         double P1Card2Y = p1Choose.get(1).getKey().getY();
//         double P1Card3X = p1Choose.get(2).getKey().getX();
//         double P1Card3Y = p1Choose.get(2).getKey().getY();

//         double P2Card1X = p2Choose.get(0).getKey().getX();
//         double P2Card1Y = p2Choose.get(0).getKey().getY();
//         double P2Card2X = p2Choose.get(1).getKey().getX();
//         double P2Card2Y = p2Choose.get(1).getKey().getY();
//         double P2Card3X = p2Choose.get(2).getKey().getX();
//         double P2Card3Y = p2Choose.get(2).getKey().getY();

//         double P3Card1X = p3Choose.get(0).getKey().getX();
//         double P3Card1Y = p3Choose.get(0).getKey().getY();
//         double P3Card2X = p3Choose.get(1).getKey().getX();
//         double P3Card2Y = p3Choose.get(1).getKey().getY();
//         double P3Card3X = p3Choose.get(2).getKey().getX();
//         double P3Card3Y = p3Choose.get(2).getKey().getY();

//         double P4Card1X = p4Choose.get(0).getKey().getX();
//         double P4Card1Y = p4Choose.get(0).getKey().getY();
//         double P4Card2X = p4Choose.get(1).getKey().getX();
//         double P4Card2Y = p4Choose.get(1).getKey().getY();
//         double P4Card3X = p4Choose.get(2).getKey().getX();
//         double P4Card3Y = p4Choose.get(2).getKey().getY();

//         p1Choose.get(0).getKey().setX(P4Card1X);
//         p1Choose.get(0).getKey().setY(P4Card1Y);
//         p1Choose.get(0).getKey().setRotate(-90);
//         p1Choose.get(1).getKey().setX(P4Card2X);
//         p1Choose.get(1).getKey().setY(P4Card2Y);
//         p1Choose.get(1).getKey().setRotate(-90);
//         p1Choose.get(2).getKey().setX(P4Card3X);
//         p1Choose.get(2).getKey().setY(P4Card3Y);
//         p1Choose.get(2).getKey().setRotate(-90);
        
//         p4Choose.get(0).getKey().setX(P3Card1X);
//         p4Choose.get(0).getKey().setY(P3Card1Y);
//         p4Choose.get(0).getKey().setRotate(0);
//         p4Choose.get(1).getKey().setX(P3Card2X);
//         p4Choose.get(1).getKey().setY(P3Card2Y);
//         p4Choose.get(1).getKey().setRotate(0);
//         p4Choose.get(2).getKey().setX(P3Card3X);
//         p4Choose.get(2).getKey().setY(P3Card3Y);
//         p4Choose.get(2).getKey().setRotate(0);

//         p3Choose.get(0).getKey().setX(P2Card1X);
//         p3Choose.get(0).getKey().setY(P2Card1Y);
//         p3Choose.get(0).getKey().setRotate(90);
//         p3Choose.get(1).getKey().setX(P2Card2X);
//         p3Choose.get(1).getKey().setY(P2Card2Y);
//         p3Choose.get(1).getKey().setRotate(90);
//         p3Choose.get(2).getKey().setX(P2Card3X);
//         p3Choose.get(2).getKey().setY(P2Card3Y);
//         p3Choose.get(2).getKey().setRotate(90);

//         p2Choose.get(0).getKey().setX(P1Card1X);
//         p2Choose.get(0).getKey().setY(P1Card1Y);
//         p2Choose.get(0).getKey().setRotate(0);
//         p2Choose.get(0).getKey().setX(P1Card2X);
//         p2Choose.get(0).getKey().setY(P1Card2Y);
//         p2Choose.get(0).getKey().setRotate(0);
//         p2Choose.get(0).getKey().setX(P1Card3X);
//         p2Choose.get(0).getKey().setY(P1Card3Y);
//         p2Choose.get(0).getKey().setRotate(0);

//         //change cards
//         player1.set(p1ChooseIndex.get(0), p2Choose.get(0));
//         player1.set(p1ChooseIndex.get(1), p2Choose.get(1));
//         player1.set(p1ChooseIndex.get(2), p2Choose.get(2));
//         player2.set(p2ChooseIndex.get(0), p3Choose.get(0));
//         player2.set(p2ChooseIndex.get(1), p3Choose.get(1));
//         player2.set(p2ChooseIndex.get(2), p3Choose.get(2));
//         player3.set(p3ChooseIndex.get(0), p4Choose.get(0));
//         player3.set(p3ChooseIndex.get(1), p4Choose.get(1));
//         player3.set(p3ChooseIndex.get(2), p4Choose.get(2));
//         player4.set(p4ChooseIndex.get(0), p1Choose.get(0));
//         player4.set(p4ChooseIndex.get(1), p1Choose.get(1));
//         player4.set(p4ChooseIndex.get(2), p1Choose.get(2));
        
//         //remove choose card
//         // p1Choose.removeAll();
//         // p1ChooseIndex.removeAll();
//         // p2Choose.removeAll();
//         // p2ChooseIndex.removeAll();
//         // p3Choose.removeAll();
//         // p3ChooseIndex.removeAll();
//         // p4Choose.removeAll();
//         // p4ChooseIndex.removeAll();
//         sortCardsInHands();
//         refreshCard();
//         swap.setVisible(false);
//     }

//     public void oppositeSwap(){
//         p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
//         p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
//         p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);
//         // place of cards
//         double P1Card1X = p1Choose.get(0).getKey().getX();
//         double P1Card1Y = p1Choose.get(0).getKey().getY();
//         double P1Card2X = p1Choose.get(1).getKey().getX();
//         double P1Card2Y = p1Choose.get(1).getKey().getY();
//         double P1Card3X = p1Choose.get(2).getKey().getX();
//         double P1Card3Y = p1Choose.get(2).getKey().getY();

//         double P2Card1X = p2Choose.get(0).getKey().getX();
//         double P2Card1Y = p2Choose.get(0).getKey().getY();
//         double P2Card2X = p2Choose.get(1).getKey().getX();
//         double P2Card2Y = p2Choose.get(1).getKey().getY();
//         double P2Card3X = p2Choose.get(2).getKey().getX();
//         double P2Card3Y = p2Choose.get(2).getKey().getY();

//         double P3Card1X = p3Choose.get(0).getKey().getX();
//         double P3Card1Y = p3Choose.get(0).getKey().getY();
//         double P3Card2X = p3Choose.get(1).getKey().getX();
//         double P3Card2Y = p3Choose.get(1).getKey().getY();
//         double P3Card3X = p3Choose.get(2).getKey().getX();
//         double P3Card3Y = p3Choose.get(2).getKey().getY();

//         double P4Card1X = p4Choose.get(0).getKey().getX();
//         double P4Card1Y = p4Choose.get(0).getKey().getY();
//         double P4Card2X = p4Choose.get(1).getKey().getX();
//         double P4Card2Y = p4Choose.get(1).getKey().getY();
//         double P4Card3X = p4Choose.get(2).getKey().getX();
//         double P4Card3Y = p4Choose.get(2).getKey().getY();

//         p1Choose.get(0).getKey().setX(P3Card1X);
//         p1Choose.get(0).getKey().setY(P3Card1Y);
//         p1Choose.get(0).getKey().setRotate(0);
//         p1Choose.get(1).getKey().setX(P3Card2X);
//         p1Choose.get(1).getKey().setY(P3Card2Y);
//         p1Choose.get(1).getKey().setRotate(0);
//         p1Choose.get(2).getKey().setX(P3Card3X);
//         p1Choose.get(2).getKey().setY(P3Card3Y);
//         p1Choose.get(2).getKey().setRotate(0);
        
//         p3Choose.get(0).getKey().setX(P1Card1X);
//         p3Choose.get(0).getKey().setY(P1Card1Y);
//         p3Choose.get(0).getKey().setRotate(0);
//         p3Choose.get(1).getKey().setX(P1Card2X);
//         p3Choose.get(1).getKey().setY(P1Card2Y);
//         p3Choose.get(1).getKey().setRotate(0);
//         p3Choose.get(2).getKey().setX(P1Card3X);
//         p3Choose.get(2).getKey().setY(P1Card3Y);
//         p3Choose.get(2).getKey().setRotate(0);

//         p4Choose.get(0).getKey().setX(P2Card1X);
//         p4Choose.get(0).getKey().setY(P2Card1Y);
//         p4Choose.get(0).getKey().setRotate(90);
//         p4Choose.get(1).getKey().setX(P2Card2X);
//         p4Choose.get(1).getKey().setY(P2Card2Y);
//         p4Choose.get(1).getKey().setRotate(90);
//         p4Choose.get(2).getKey().setX(P2Card3X);
//         p4Choose.get(2).getKey().setY(P2Card3Y);
//         p4Choose.get(2).getKey().setRotate(90);

//         p2Choose.get(0).getKey().setX(P4Card1X);
//         p2Choose.get(0).getKey().setY(P4Card1Y);
//         p2Choose.get(0).getKey().setRotate(-90);
//         p2Choose.get(0).getKey().setX(P4Card2X);
//         p2Choose.get(0).getKey().setY(P4Card2Y);
//         p2Choose.get(0).getKey().setRotate(-90);
//         p2Choose.get(0).getKey().setX(P4Card3X);
//         p2Choose.get(0).getKey().setY(P4Card3Y);
//         p2Choose.get(0).getKey().setRotate(-90);

//         //change cards
//         player1.set(p1ChooseIndex.get(0), p3Choose.get(0));
//         player1.set(p1ChooseIndex.get(1), p3Choose.get(1));
//         player1.set(p1ChooseIndex.get(2), p3Choose.get(2));
//         player2.set(p2ChooseIndex.get(0), p4Choose.get(0));
//         player2.set(p2ChooseIndex.get(1), p4Choose.get(1));
//         player2.set(p2ChooseIndex.get(2), p4Choose.get(2));
//         player3.set(p3ChooseIndex.get(0), p1Choose.get(0));
//         player3.set(p3ChooseIndex.get(1), p1Choose.get(1));
//         player3.set(p3ChooseIndex.get(2), p1Choose.get(2));
//         player4.set(p4ChooseIndex.get(0), p2Choose.get(0));
//         player4.set(p4ChooseIndex.get(1), p2Choose.get(1));
//         player4.set(p4ChooseIndex.get(2), p2Choose.get(2));
        
//         //remove choose card
//         // p1Choose.removeAll();
//         // p1ChooseIndex.removeAll();
//         // p2Choose.removeAll();
//         // p2ChooseIndex.removeAll();
//         // p3Choose.removeAll();
//         // p3ChooseIndex.removeAll();
//         // p4Choose.removeAll();
//         // p4ChooseIndex.removeAll();
//         sortCardsInHands();
//         refreshCard();
//         swap.setVisible(false);
//     }

//     public void nextAction() {
//         if (hearts.getTurn() == 4) {
//             //If small round is end. Find winner and add score.
//             int winner = hearts.getTable().findWinner();
//             hearts.setWhoseTurn(winner);
//             for(Card card : hearts.getTable().popAllCards()){
//                 hearts.getHands()[winner].addCardInPile(card);
//             }
//             hearts.setTurn(0);
//             root.getChildren().removeAll(cardOnTable);
//             //get imageView
//             // for(ImageView temp: cardOnTable){
//             //     root.getChildren().remove(temp);
//             // }
            
//         }
//         else{
//             hearts.setWhoseTurn(hearts.getWhoseTurn() + 1);
//                 if(hearts.getWhoseTurn() >= 4){
//                     hearts.setWhoseTurn(0);
//                 }
//         }
//         switch (hearts.getWhoseTurn()) {
//             case 0:
//                 Player player = (Player)hearts.getHands()[0];
//                 player.setChosenPlaceCard(-1);
//                 while(player.getChosenPlaceCard() == -1) {System.out.println("Loopy\n");};
//                 hearts.getTable().placeCardAt(player.getCardsInHand().get(player.getChosenPlaceCard()), 0);
//                 System.out.println(player.getChosenPlaceCard());
//                 cardOnTable.add(player1.get(player.getChosenPlaceCard()).getKey());
//                 break;
//             case 1:
//                 Bot bot1 = (Bot)hearts.getHands()[1];
//                 if(hearts.getHands()[1] instanceof Bot){
//                     bot1.chooseCardToPlace();
//                 }

//                 hearts.getTable().placeCardAt(bot1.getCardsInHand().get(bot1.getChosenPlaceCard()), 1);
//                 bot1.removeCardInHand(bot1.getCardsInHand().get(bot1.getChosenPlaceCard()));
//                 player2.get(bot1.getChosenPlaceCard()).getKey().setX(posCenterX);
//                 player2.get(bot1.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

//                 cardOnTable.add(player2.get(bot1.getChosenPlaceCard()).getKey());

//                 //delay 2

//                 break;
//             case 2:
//                 Bot bot2 = (Bot)hearts.getHands()[2];
//                 if(hearts.getHands()[2] instanceof Bot){
//                     bot2.chooseCardToPlace();
//                 }

//                 hearts.getTable().placeCardAt(bot2.getCardsInHand().get(bot2.getChosenPlaceCard()), 2);
//                 bot2.removeCardInHand(bot2.getCardsInHand().get(bot2.getChosenPlaceCard()));
//                 player3.get(bot2.getChosenPlaceCard()).getKey().setX(posCenterX);
//                 player3.get(bot2.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

//                 cardOnTable.add(player3.get(bot2.getChosenPlaceCard()).getKey());

//                 //delay 2
//                 break;
//             case 3:
//                 Bot bot3 = (Bot)hearts.getHands()[3];
//                 if(hearts.getHands()[3] instanceof Bot){
//                     bot3.chooseCardToPlace();
//                 }

//                 hearts.getTable().placeCardAt(bot3.getCardsInHand().get(bot3.getChosenPlaceCard()), 3);
//                 bot3.removeCardInHand(bot3.getCardsInHand().get(bot3.getChosenPlaceCard()));
//                 player3.get(bot3.getChosenPlaceCard()).getKey().setX(posCenterX);
//                 player3.get(bot3.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

//                 cardOnTable.add(player4.get(bot3.getChosenPlaceCard()).getKey());
                
//                 //delay 2
//                 break;
//         }
//         updateNotificationWhoseTurn();
//     }
//     //begin small rouund
//     public void begin() {
//         for (int i = 0; i < player1.size(); i++) {
//             player1.get(i).getKey().setOnMouseClicked(e -> {
//                 if (!(this.hearts.getWhoseTurn() == 0)) return; 
//                 ImageView temp = (ImageView) e.getSource();
//                     PathTransition dropCard = new PathTransition(Duration.millis(500),
//                             new Line(temp.getX(), temp.getY(), posCenterX + 50, posCenterY + 160), temp);
//                     temp.setX(posCenterX + 50);
//                     temp.setY(posCenterY + 160);
//                     dropCard.play();
                    
//                     int chIdx = -1;
//                     for (int j = 0; j < player1.size(); j++) {
//                         if (temp == player1.get(j).getKey()) {
//                             System.out.println("clicked + " + j);
//                             chIdx = j;
//                         }
//                     }
//                     // break while loop condition
//                     hearts.getHands()[0].setChosenPlaceCard(chIdx);
//                     // hearts.getHands()[0].setChosenPlaceCard(j);
//                 this.updateNotificationWhoseTurn();
//             });
//         }
//         bot1play()

//         hearts.setWhoseTurn(hearts.whoFirst());
//         updateNotificationWhoseTurn();

//         for(int i = 0; i < 13; i++)
//             for (int k = 0; i < 4; k++){
//                 nextAction();
//             } 
                
        

        // for (int i = 0; i < player2.size(); i++) {
        //     player2.get(i).getKey().setOnMouseClicked(e -> {
        //         if (!(this.hearts.getWhoseTurn() == 1)) return; 
        //         ImageView temp = (ImageView) e.getSource();
        //         PathTransition dropCard = new PathTransition(Duration.millis(500),
        //                 new Line(temp.getX(), temp.getY(), posCenterX, posCenterY + 100), temp);
        //         temp.setX(posCenterX);
        //         temp.setY(posCenterY + 100);
        //         temp.setRotate(0);
        //         dropCard.play();

        //         this.hearts.endTurn();
        //         this.hearts.nextTurn();
        //         this.updateNotificationWhoseTurn();
        //     });
        // }
        // for (int i = 0; i < player3.size(); i++) {
        //     player3.get(i).getKey().setOnMouseClicked(e -> {
        //         if (!(this.hearts.getWhoseTurn() == 2)) return; 
        //         ImageView temp = (ImageView) e.getSource();
        //         PathTransition dropCard = new PathTransition(Duration.millis(500),
        //                 new Line(temp.getX(), temp.getY(), posCenterX + 50, posCenterY + 40), temp);
        //         temp.setX(posCenterX + 50);
        //         temp.setY(posCenterY + 40);
        //         dropCard.play();

        //         this.hearts.endTurn();
        //         this.hearts.nextTurn();
        //         this.updateNotificationWhoseTurn();
        //     });
        // }
        // for (int i = 0; i < player4.size(); i++) {
        //     player4.get(i).getKey().setOnMouseClicked(e -> {
        //         if (!(this.hearts.getWhoseTurn() == 3)) return; 
        //         ImageView temp = (ImageView) e.getSource();
        //         PathTransition dropCard = new PathTransition(Duration.millis(500),
        //                 new Line(temp.getX(), temp.getY(), posCenterX + 100, posCenterY + 100), temp);
        //         temp.setX(posCenterX + 100);
        //         temp.setY(posCenterY + 100);
        //         temp.setRotate(0);
        //         dropCard.play();

        //         this.hearts.endTurn();
        //         this.hearts.nextTurn();
        //         this.updateNotificationWhoseTurn();
        //     });
        // }

        // this.updateNotificationWhoseTurn();
    // }

    // private void updateNotificationWhoseTurn() {
    //     notification.setText(String.format("Small Round %d\n%s's turn", this.hearts.getSmallRound(),this.hearts.getHands()[this.hearts.getWhoseTurn()].getName()));
    // }

    // public void refreshCard() {
    //     root.getChildren().removeAll(imgs);
    //     posXPlayer1 = 600;
    //     posyPlayer1 = 600;
    //     posXPlayer2 = 300;
    //     posyPlayer2 = 300;
    //     posXPlayer3 = 840;
    //     posyPlayer3 = 70;
    //     posXPlayer4 = 1100;
    //     posyPlayer4 = 420;

    //     for (int i = 0; i < player1.size(); i++) {
    //         System.out.println(player1.get(i).getValue().getCard().toString());
    //         player1.get(i).getKey().setX(posXPlayer1);
    //         player1.get(i).getKey().setY(posyPlayer1);
    //         posXPlayer1 += 20;
    //         System.out.println(String.format("P1 POSX : %f",player1.get(i).getKey().getX()));
    //         System.out.println(String.format("P1 POSY : %f",player1.get(i).getKey().getY()));
    //         root.getChildren().add(player1.get(i).getKey());
    //     }
    //     for (int i = 0; i < player2.size(); i++) {
    //         player2.get(i).getKey().setX(posXPlayer2);
    //         player2.get(i).getKey().setY(posyPlayer2);
    //         player2.get(i).getKey().setRotate(90);
    //         posyPlayer2 += 10;
    //         root.getChildren().add(player2.get(i).getKey());
    //     }
    //     for (int i = 0; i < player3.size(); i++) {
    //         player3.get(i).getKey().setX(posXPlayer3);
    //         player3.get(i).getKey().setY(posyPlayer3);
    //         posXPlayer3 -= 20;
    //         root.getChildren().add(player3.get(i).getKey());
    //     }
    //     for (int i = 0; i < player4.size(); i++) {
    //         player4.get(i).getKey().setX(posXPlayer4);
    //         player4.get(i).getKey().setY(posyPlayer4);
    //         player4.get(i).getKey().setRotate(-90);
    //         posyPlayer4 -= 10;
    //         root.getChildren().add(player4.get(i).getKey());
    //     }
    // }

    // public void placeCardToTable(int whoseTurn, int indexOfCard) {

    // }

    // private void sortCardsInHands() {
    //     hearts.sortCardsInHands();
    //     var players = new ArrayList<ArrayList<Pair<ImageView, CardController>>>();
    //     players.add(player1);
    //     players.add(player2);
    //     players.add(player3);
    //     players.add(player4);
    //     int i = 0;
    //     for (Hand hand : hearts.getHands()) {
    //         for (Card card : hand.getCardsInHand()) {
    //             players.get(i).sort((cardC1, cardC2) -> {
    //                 var card1 = cardC1.getValue().getCard();
    //                 var card2 = cardC2.getValue().getCard();
    //                 return card1.compareTo(card2);
    //             });
    //         }
    //         i++;
    //     }
    // }

    @FXML
    private void exitRelease(MouseEvent event) {
        exit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void exitOut(MouseEvent event) {
        exit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void exitEnter(MouseEvent event) {
        exit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void exitPressed(MouseEvent event) {
        exit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void dealRelease(MouseEvent event) {
        deal.setStyle("-fx-background-color:#01B075;");
    }

    @FXML
    private void dealExit(MouseEvent event) {
        deal.setStyle("-fx-background-color:linear-gradient(to bottom,#2D9D3B, #68C974, #CDF4D2);");
    }

    @FXML
    private void dealEnter(MouseEvent event) {
        deal.setStyle("-fx-background-color:#01B075;");
    }

    @FXML
    private void dealPressed(MouseEvent event) {

        deal.setStyle("-fx-background-color:linear-gradient(to bottom,#2D9D3B, #68C974, #CDF4D2);");
    }

    @FXML
    private void swapExit(MouseEvent event) {
        swap.setPrefWidth(70);
        swap.setPrefHeight(70);
    }

    @FXML
    private void swapEnter(MouseEvent event) {
        swap.setPrefWidth(75);
        swap.setPrefHeight(75);
    }

}
