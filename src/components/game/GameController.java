package components.game;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import components.card.CardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.util.Pair;
import models.Bot;
import models.Card;
import models.Deck;
import models.Hand;
import models.Hearts;
import models.Player;
import models.Sounds;
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
    
    private Label host = new Label();
    private Label player = new Label();
    private Label bot1 = new Label();
    private Label bot2 = new Label();
    private Label bot3 = new Label();

    private boolean isPlayerClicked = false;

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

    // install 

    @FXML
    private Button showScores;

    @FXML
    private Button closeScores;

    @FXML
    private Pane scoresBoard;
    @FXML
    private Text playerScore;
    @FXML
    private Text bot1Score;
    @FXML
    private Text bot2Score;
    @FXML
    private Text bot3Score;

    
    @FXML
    private Pane winnerBoard;
    @FXML
    private Text winnerText;
    @FXML
    private Button backToMenu;

    private Hand winner;

    private Sounds sounds = new Sounds();

    @Override
    public void initialize(URL url, ResourceBundle rb)  {
    }

    public void initGame(String playerName) {
        initCurrentState();
        initDeckView();
        initNotification();
        initHearts(playerName);
        initHandLabel();
        initGameEventHandler();
        initBots();
        initWinnerBoard();
        initScoresBoard();
        renderAllCards();
        sounds.getMusicGameBGPlay();
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
                tempCon.setFacingDown(true);
                tempCon.updateCardImage();
                ImageView tempView = cardComponent.getKey();
                tempView.setX(X_CENTER);
                tempView.setY(Y_CENTER);
                tempView.setFitHeight(CARD_HEIGHT);
                tempView.setFitWidth(CARD_WIDTH);

                this.cards.put(card.toString(), cardComponent);
            }
    }

    private void initNotification() {
        this.notification.setText("");
    }

    private void initHearts(String playerName) {
        Hand[] allHand = {new Player(playerName), new Bot("Billy"), new Bot("Jane"), new Bot("Mike")};
        hearts = new Hearts(allHand);
        hearts.resetGame();
    }

    private void initHandLabel() {
        Hand[] allHand = hearts.getHands();
        player.setText(allHand[0].getName());
        bot1.setText(allHand[1].getName());
        bot2.setText(allHand[2].getName());
        bot3.setText(allHand[3].getName());
    }

    private void initGameEventHandler() {
        host.addEventHandler(GameEvent.GAME_EVENT, new GameEventHandler() {
            @Override
            public void onEndTurn() {
                boolean endBig = checkBigEndRound();
                if (endBig) {
                    hearts.setBigRound(hearts.getBigRound() + 1);
                    changeState(State.END);
                    return;
                }
                if (!endBig && checkSmallEndRound()) {
                    hearts.setSmallRound(hearts.getSmallRound() + 1);
                    clearTableView(); 
                    _findWinner();
                    // refreshCardPosition();
                    // updateChooseToPlaceEvent();
                    return;
                }
                switch(hearts.getWhoseTurn()) {
                    case 3:
                        isPlayerClicked = false;
                        player.fireEvent(new StartTurnEvent());
                        break;
                    case 0:
                        bot1.fireEvent(new StartTurnEvent());
                        break;
                    case 1:
                        bot2.fireEvent(new StartTurnEvent());
                        break;
                    case 2:
                        bot3.fireEvent(new StartTurnEvent());
                        break;
                }
            }

            @Override
            public void onStartTurn() {}
        });

        player.addEventHandler(GameEvent.GAME_EVENT, new GameEventHandler(){
            @Override
            public void onEndTurn() {
                
            }
            @Override
            public void onStartTurn() {
                 refreshCardPosition();
                updateChooseToPlaceEvent();
                hearts.setWhoseTurn(0);
                updateWhoseTurnNotification();
            }
        });
        addStartTurnEventHandler(bot1, 1);
        addStartTurnEventHandler(bot2, 2);
        addStartTurnEventHandler(bot3, 3);
    }

    private void addStartTurnEventHandler(Label node, int handIndex) {
        node.addEventHandler(GameEvent.GAME_EVENT, new GameEventHandler() {
            @Override
            public void onEndTurn() {
            }

            @Override
            public void onStartTurn() {
                refreshCardPosition();
                hearts.setWhoseTurn(handIndex);
                updateWhoseTurnNotification();
                botPlaceCard(handIndex);
            }
        });

    }

    private void placeAnimation(ImageView cardView, double xOffset, double yOffset) {
        PathTransition dropCard = new PathTransition(Duration.millis(500),
                        new Line(cardView.getX(), cardView.getY(), X_CENTER + xOffset, Y_CENTER + yOffset), cardView);
                cardView.setX(X_CENTER + xOffset);
                cardView.setY(Y_CENTER + yOffset);
                dropCard.play();
    }

    private void initBots() {
        Hand[] hands = hearts.getHands();
        for (int i = 0; i < hands.length; i++) {
            if (hands[i] instanceof Bot) {
                ((Bot) hands[i]).joinTable(hearts.getTable());
            }
        }
    }

    private void initWinnerBoard() {
        this.winnerBoard.setVisible(false);
    }

    private void initScoresBoard() {
        this.scoresBoard.setVisible(false);
        updateScoresBoard();
    }

    private void updateScoresBoard() {
        Hand[] hands = hearts.getHands();
        int[] scores = hearts.getScores();
        this.playerScore.setText(hands[0].getName() + "'s score is " + scores[0]);
        this.bot1Score.setText(hands[1].getName()+ "'s score is " + scores[1]);
        this.bot2Score.setText(hands[2].getName() + "'s score is " + scores[2]);
        this.bot3Score.setText(hands[3].getName() + "'s score is " + scores[3]);
    }

    @FXML
    private void openScoresBoard() {
        sounds.getSoundGameClickPlay();
        this.showScores.setVisible(false);
        updateScoresBoard();
        this.scoresBoard.toFront();
        this.closeScores.toFront();
        this.scoresBoard.setVisible(true);
    }

    @FXML
    private void closeScoresBoard() {
        sounds.getSoundGameClickPlay();
        this.showScores.setVisible(true);
        this.scoresBoard.setVisible(false);
    }

    private void renderAllCards() {
        var it = cards.entrySet().iterator();
        while (it.hasNext()) {
            var pair = it.next();
            var cardView = pair.getValue().getKey();
            root.getChildren().add(cardView);
        }
    }

    private void addCardViewToParent(ImageView cardView) {
        root.getChildren().add(cardView);
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        sounds.getSoundGameClickPlay();
        sounds.getMusicGameBGStop();
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
        sounds.getSoundShuffleCardPlay();
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
            if (hands[handIndex] instanceof Player) {
                for (var card: cardsInHand) {
                    var cardCon = cards.get(card.toString()).getValue();
                    var cardView = cards.get(card.toString()).getKey();
                    cardCon.faceCardUp();
                    removeCardViewFromParent(cardView);
                    setCardViewPositionByHandIndex(handIndex, cardView, offset);
                    addCardViewToParent(cardView);
                    offset += offSetChangeRate;
                }

                continue;
            } 


            for (var card: cardsInHand) {
                var cardCon = cards.get(card.toString()).getValue();
               var cardView = cards.get(card.toString()).getKey();
               cardCon.faceCardDown();
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
                initState();
                break;
            case GIVE:
                giveState();
                break;
            case PLAY:
                playState();
                break;
            case END:
                endState();
                break;
            case FINAL:
                finalState();
                break;
        }
    }

    private void initState() {
        hearts.dealCards(); 
        sortCardsInHand();
        isPlayerClicked = false;

        changeState(State.GIVE);
    }

    private void endState(){
        for (int i = 0; i < 4; i++) {
            hearts.getScores()[i] += hearts.getHands()[i].getPoints();
        }
        Hand winnerLocal = hearts.checkEndGameConditionAndFindWinner();
        if(winnerLocal != null){
            winner = winnerLocal;
            changeState(State.FINAL);
        }
        hearts.resetGame();
        resetView();
        changeState(State.INIT);
    }

    private void finalState(){
        this.notification.setVisible(false);
      
        var it = cards.entrySet().iterator();
        while (it.hasNext()) {
            var pair = it.next();
            var cardView = pair.getValue().getKey();
            removeCardViewFromParent(cardView);
            it.remove(); // avoids a ConcurrentModificationException
        }
        this.winnerBoard.setVisible(true);
        this.winnerBoard.toFront();
        this.winnerText.setText(winner.getName() + " is a winner !!!");
        if (winner instanceof Player) {
            sounds.getSoundWinPlay();
        } else {
            sounds.getSoundLosePlay();
        }

        this.exit.setVisible(false);
        
    }

    private void giveState() {
        // give player give card
        Player player = (Player)hearts.getHands()[0];
        player.CardToGive.clear();
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
                player.fireEvent(new StartTurnEvent());
                break;
              case 1:
                bot1.fireEvent(new StartTurnEvent());
                break;
              case 2:
                bot2.fireEvent(new StartTurnEvent());
                break;
              case 3:
                bot3.fireEvent(new StartTurnEvent());
                break;
          }
    }

    private void resetView() {

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
// TODOS BUGG Q_spades compare maybe card:
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

    private void playerPlaceCard(ImageView cardView, int cardIndex) {

        Player player = (Player)hearts.getHands()[0];
        Card card = player.getCardsInHand().get(cardIndex);
        placeAnimation(cardView, 50, 160);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            hearts.getTable().placeCardAt(card, 0);
            player.removeCardInHand(card);
            cardView.setX(X_CENTER + 50);
            cardView.setY(Y_CENTER + 160);
            hearts.getTable().Update();
            isPlayerClicked = false;
            host.fireEvent(new EndTurnEvent());
        }
        );
        pause.play();
    }

    
    private void botPlaceCard(int handIndex)  {
        Bot bot = (Bot)hearts.getHands()[handIndex];
        bot.chooseCardToPlace();
        int cardIndex = bot.getChosenPlaceCard();
        Card card = bot.getCardsInHand().get(cardIndex);

        var cardCon = cards.get(card.toString()).getValue();
        ImageView cardView = cards.get(card.toString()).getKey();

        double[] offsets = getBotOffset(handIndex);
        placeAnimation( cardView, offsets[0], offsets[1] );
        
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            hearts.getTable().placeCardAt(card, handIndex);
            bot.removeCardInHand(card);
            hearts.getTable().Update();
            botPlaceCardView( cardView, handIndex);
            cardCon.faceCardUp();
            PauseTransition pause2 = new PauseTransition(Duration.seconds(0.5));
            pause2.setOnFinished(event2 -> {
                host.fireEvent(new EndTurnEvent()); 
            });
            pause2.play();
        }
        );
        pause.play();

    }

    private double[] getBotOffset(int handIndex) {
        switch(handIndex) {
            case 1:
                return new double[] {0, 100};
            case 2:
                return new double[] {50, 40};
            case 3:
                return new double [] {100, 100};
        }

        return new double[] {0, 0};
    }


    private void botPlaceCardView(ImageView cardView, int handIndex) {
        double[] offsets = getBotOffset(handIndex);
        cardView.setX(X_CENTER + offsets[0]);
        cardView.setY(Y_CENTER + offsets[1]);
    }



    private boolean checkBigEndRound() {
        boolean isEnd = false;
        if(hearts.getSmallRound() > 13){
            isEnd = true;
        }
        return isEnd;
    }

    private boolean checkSmallEndRound() {
        return hearts.getTable().getPlayedNumber() >= 4;
    }

    private void clearTableView() {
        Card[] cardsOnTable = hearts.getTable().getCardSlot();
        for (Card card: cardsOnTable) {
            ImageView cardView = cards.get(card.toString()).getKey();
            removeCardViewFromParent(cardView);
        }
    }

    private void _findWinner(){
        if(hearts.getTable().getPlayedNumber() < 4){
            return;
        }
        int winner = hearts.getTable().findWinner();
        
        if (winner - 1 < 0) {
            hearts.setWhoseTurn(3);
        } else {
            hearts.setWhoseTurn(winner-1);
        }
        for(Card card : hearts.getTable().popAllCards()){
            hearts.getHands()[winner].addCardInPile(card);
        }
        host.fireEvent(new EndTurnEvent());
    }

    private void addChooseToPlaceEvent(ImageView cardView, int cardIndex) {
        cardView.setOnMouseClicked(event -> onMouseClickedToPlace(event, cardIndex));
    }

    private void onMouseClickedToPlace(MouseEvent event, int cardIndex) {
        if (isPlayerClicked) {
            return;
        }
        Hand player = hearts.getHands()[0];
        Card card = player.getCardsInHand().get(cardIndex);
        if (isFirstOneInTurn() && 
            isHeart(card) && 
            !isHeartBroken()) {
            sounds.getSoundWrongPlay();
            return;
        }
        if(!isTurn(0) || !canPlay(player, card)) {
            sounds.getSoundWrongPlay();
            return;
        }
        if(hasTwoClubs(player) && !isTwoClubs(card)){
            sounds.getSoundWrongPlay();
            return;
        }
        isPlayerClicked = true;
        var cardView = (ImageView)event.getSource();
        playerPlaceCard(cardView, cardIndex);

    }

    private boolean isHeartBroken() {
        return hearts.getTable().isBroken();
    }

    private boolean isHeart(Card card) {
        return card.getSuit() == Suit.HEARTS;
    }

    private boolean isFirstOneInTurn() {
        return hearts.getTable().gethandStack().size() == 0;
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

    private void offsetCardView(ImageView cardView, double offset) {
        double y = cardView.getY();
        cardView.setY(y + offset);
    }

    @FXML
    private void swapCards() {
        Player player = (Player)hearts.getHands()[0];
        if (player.CardToGive.size() < 3) {
            return;
        }
        sounds.getSoundSwitchCardPlay();

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
        // refreshCardPosition();
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


    @FXML
    private void scoreExit(MouseEvent event) {
        showScores.setStyle("-fx-background-color:#8497B5");
    }

    @FXML
    private void scoreEnterd(MouseEvent event) {
        showScores.setStyle("-fx-background-color:#9575CC");
    }




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
