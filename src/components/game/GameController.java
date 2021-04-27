package components.game;

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
import utils.ComponentLoader;

public class GameController implements Initializable {

    private double setX = 0;
    private double setY = 0;
    private Hearts hearts;
    // private ArrayList<ImageView> deck = new ArrayList<>(52);
    private ComponentLoader<GameController> loader = new ComponentLoader<>();
    private HashMap<String, Pair<ImageView, CardController>> cards = new HashMap<>();
    private ArrayList<ImageView> imgs = new ArrayList<ImageView>();
    private ArrayList<Pair<ImageView, CardController>> card = new ArrayList<Pair<ImageView, CardController>>();
    private  ArrayList<ImageView> cardOnTable= new ArrayList<ImageView>();
    // private ArrayList<ImageView> player1 =new ArrayList<>(13);
    // private ArrayList<ImageView> player2 =new ArrayList<>(13);
    // private ArrayList<ImageView> player3 =new ArrayList<>(13);
    // private ArrayList<ImageView> player4 =new ArrayList<>(13);
    private ArrayList<Pair<ImageView, CardController>> player1 = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Pair<ImageView, CardController>> player2 = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Pair<ImageView, CardController>> player3 = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Pair<ImageView, CardController>> player4 = new ArrayList<Pair<ImageView, CardController>>();

    // player choose cards
    private ArrayList<Pair<ImageView, CardController>> p1Choose = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Integer> p1ChooseIndex = new ArrayList<>();

    private ArrayList<Pair<ImageView, CardController>> p2Choose = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Integer> p2ChooseIndex = new ArrayList<>();

    private ArrayList<Pair<ImageView, CardController>> p3Choose = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Integer> p3ChooseIndex = new ArrayList<>();

    private ArrayList<Pair<ImageView, CardController>> p4Choose = new ArrayList<Pair<ImageView, CardController>>();
    private ArrayList<Integer> p4ChooseIndex = new ArrayList<>();

    private double windowWidth = 1400;
    private double windowHeight = 700;
    private double cardWidth = 50;
    private double cardHeight = 100;
    private double posCenterX = windowWidth / 2 - cardWidth;
    private double posCenterY = windowHeight / 2 - cardHeight;
    private double posXPlayer1 = 600;
    private double posyPlayer1 = 600;
    private double posXPlayer2 = 300;
    private double posyPlayer2 = 300;
    private double posXPlayer3 = 840;
    private double posyPlayer3 = 70;
    private double posXPlayer4 = 1100;
    private double posyPlayer4 = 420;

    @FXML
    private Pane root = new Pane();
    @FXML
    private Button btnDeal;
    @FXML
    private Button swap;
    @FXML
    private Button btnExit;

    @FXML
    private Label notification;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // String path ="C:\\Users\\LENOVO LEGION\\Downloads\\12313.mp3";
        // Media media = new Media(new File(path).toURI().toString());
        // MediaPlayer player = new MediaPlayer(media);
        // player.play();
        notification.setText("");
        Hand[] handsInit = { new Player("test1"), new Bot("bot1"), new Bot("bot2"), new Bot("bot3") };

        hearts = new Hearts(handsInit);

        hearts.resetGame();

        // let bots join the table
        Hand[] hands = hearts.getHands();
        for (int i = 0; i < hands.length; i++) {
            if (hands[i] instanceof Bot) {
                ((Bot) hands[i]).joinTable(hearts.getTable());
            }
        }

        Deck temp = hearts.getDeck();
        // create all cards
        try {
            int t = 0;
            for (var card : temp) {
                // System.out.println("on Round " + t);
                Pair<ImageView, CardController> cardComponent = loader.loadComponent(this,
                        "/components/card/Card.fxml");
                // System.out.println("hello of on start1");
                CardController tempCon = cardComponent.getValue();
                tempCon.setCard(card);
                tempCon.setFacingDown(false);
                // System.out.println("hello of on start2");
                tempCon.updateCardImage();
                // System.out.println("hello of on start3");
                ImageView tempView = cardComponent.getKey();
                // System.out.println("hello of on start4");
                tempView.setX(posCenterX);
                // System.out.println("hello of on start5");
                tempView.setY(posCenterY);
                // System.out.println("hello of on start6");
                tempView.setFitHeight(cardHeight);
                // System.out.println("hello of on start7");
                tempView.setFitWidth(cardWidth);
                // System.out.println("hello of on start8");
                this.cards.put(card.toString(), cardComponent);
                // System.out.println("hello of on start9");
                this.card.add(this.cards.get(card.toString()));
                // System.out.println("hello of on start10");
                t += 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        // System.out.println(this.card[0].toString());

        swap.setVisible(false);

        for (var c : card) {
            imgs.add(c.getKey());
        }
        // var it = cards.entrySet().iterator();
        // while (it.hasNext()) {
        // var pair = it.next();
        // imgs.add(pair.getValue().getKey());
        // System.out.println(pair.getKey() + " = " + pair.getValue());
        // it.remove(); // avoids a ConcurrentModificationException

        root.getChildren().addAll(imgs);

    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        // Platform.exit();
        // System.exit(0);
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
    private void dealCard(ActionEvent event) {
        // test
        int j = 0;
        for (int i = 0; i < 13; i++) {
            // Player1
            // if(j%2!=0){
            // rg[j].setRotate(90);
            // }
            // PathTransition path1 = new PathTransition(Duration.millis(500),
            //         new Line(posCenterX, posCenterY, posXPlayer1, posyPlayer1), card.get(j).getKey());
            card.get(j).getKey().setX(posXPlayer1);
            card.get(j).getKey().setY(posyPlayer1);
            player1.add(card.get(j));
            j++;
            // path1.play();
            posXPlayer1 += 20;

            // player 2
            // if (j % 2 != 0) {
            //     card.get(j).getKey().setRotate(90);
            // }
            // PathTransition path2 = new PathTransition(Duration.millis(500),
            //         new Line(posCenterX, posCenterY, posXPlayer2, posyPlayer2), card.get(j).getKey());
            card.get(j).getKey().setX(posXPlayer2);
            card.get(j).getKey().setY(posyPlayer2);
            player2.add(card.get(j));
            j++;
            posyPlayer2 += 10;
            // path2.play();

            // player3
            // if(j%2!=0){
            // rg[j].setRotate(90);
            // }
            // PathTransition path3 = new PathTransition(Duration.millis(500),
            //         new Line(posCenterX, posCenterY, posXPlayer3, posyPlayer3), card.get(j).getKey());
            card.get(j).getKey().setX(posXPlayer3);
            card.get(j).getKey().setY(posyPlayer3);
            player3.add(card.get(j));
            j++;
            posXPlayer3 -= 20;
            // path3.play();

            // player4
            // if (j % 2 != 0) {
            //     card.get(j).getKey().setRotate(-90);
            // }
            // PathTransition path4 = new PathTransition(Duration.millis(500),
            //         new Line(posCenterX, posCenterY, posXPlayer4, posyPlayer4), card.get(j).getKey());
            card.get(j).getKey().setX(posXPlayer4);
            card.get(j).getKey().setY(posyPlayer4);
            player4.add(card.get(j));
            j++;
            posyPlayer4 -= 10;
            // path4.play();

        }
        hearts.dealCards();
        hearts.printHands();
        int first = hearts.whoFirst();
        Hand[] hands = hearts.getHands();

        if (hands[first] instanceof Bot) {
            int cc = ((Bot) hands[first]).choose();
            System.out.println("bot choose " + cc);
            System.out.println(hands[first].getCardsInHand().get(cc).toString());

        }

        // hearts.dealCard
        btnDeal.setVisible(false);
        sortCardsInHands();
        refreshCard();
        pickCard();
    }

    private void pickCard() {

        for (int i = 0; i < player1.size(); i++) {
            player1.get(i).getKey().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    ImageView temp = (ImageView) e.getSource();
                    // System.out.println(temp==player1.get(0).getKey());
                    if (temp.getY() == 590) {
                        // PathTransition slideDown = new PathTransition(Duration.millis(100),
                        //         new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() + 10), temp);
                        temp.setY(temp.getY() + 10);
                        // slideDown.play();
                        for (int i = 0; i < p1Choose.size(); i++) {
                            if (temp == p1Choose.get(i).getKey()) {
                                System.out.println(p1Choose);
                                p1Choose.remove(i);
                                p1ChooseIndex.remove(i);
                                System.out.println(p1ChooseIndex);
                            }
                        }

                    } else {
                        // PathTransition slideUp = new PathTransition(Duration.millis(100),
                                // new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() - 10), temp);
                        temp.setY(temp.getY() - 10);
                        // slideUp.play();
                        for (int i = 0; i < player1.size(); i++) {
                            if (temp == player1.get(i).getKey()) {
                                p1Choose.add(player1.get(i));
                                p1ChooseIndex.add(i);
                                System.out.println(p1Choose);
                                System.out.println(p1ChooseIndex);
                            }
                        }
                    }
                }
            });
        }
        // set choosen cards
        // p1Choose.add(player1.get(0));
        // p1Choose.add(player1.get(1));
        // p1Choose.add(player1.get(2));

        Bot bot1 = (Bot)hearts.getHands()[1];
        Bot bot2 = (Bot)hearts.getHands()[2];
        Bot bot3 = (Bot)hearts.getHands()[3];

        bot1.chooseCardToGive();
        bot2.chooseCardToGive();
        bot3.chooseCardToGive();
        
        p2Choose.add(player2.get(bot1.getChosenGiveCards()[0]));
        p2ChooseIndex.add(bot1.getChosenGiveCards()[0]);
        p2Choose.add(player2.get(bot1.getChosenGiveCards()[1]));
        p2ChooseIndex.add(bot1.getChosenGiveCards()[1]);
        p2Choose.add(player2.get(bot1.getChosenGiveCards()[2]));
        p2ChooseIndex.add(bot1.getChosenGiveCards()[2]);

        p3Choose.add(player3.get(bot2.getChosenGiveCards()[0]));
        p3ChooseIndex.add(bot2.getChosenGiveCards()[0]);
        p3Choose.add(player3.get(bot2.getChosenGiveCards()[1]));
        p3ChooseIndex.add(bot2.getChosenGiveCards()[1]);
        p3Choose.add(player3.get(bot2.getChosenGiveCards()[2]));
        p3ChooseIndex.add(bot2.getChosenGiveCards()[2]);

        p4Choose.add(player4.get(bot3.getChosenGiveCards()[0]));
        p4ChooseIndex.add(bot3.getChosenGiveCards()[0]);
        p4Choose.add(player4.get(bot3.getChosenGiveCards()[1]));
        p4ChooseIndex.add(bot3.getChosenGiveCards()[1]);
        p4Choose.add(player4.get(bot3.getChosenGiveCards()[2]));
        p4ChooseIndex.add(bot3.getChosenGiveCards()[2]);

        swap.setVisible(true);
    }

    @FXML
    public void leftSwap() {
        p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
        p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
        p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);

        // place of cards
        double P1Card1X = p1Choose.get(0).getKey().getX();
        double P1Card1Y = p1Choose.get(0).getKey().getY();
        double P1Card2X = p1Choose.get(1).getKey().getX();
        double P1Card2Y = p1Choose.get(1).getKey().getY();
        double P1Card3X = p1Choose.get(2).getKey().getX();
        double P1Card3Y = p1Choose.get(2).getKey().getY();

        double P2Card1X = p2Choose.get(0).getKey().getX();
        double P2Card1Y = p2Choose.get(0).getKey().getY();
        double P2Card2X = p2Choose.get(1).getKey().getX();
        double P2Card2Y = p2Choose.get(1).getKey().getY();
        double P2Card3X = p2Choose.get(2).getKey().getX();
        double P2Card3Y = p2Choose.get(2).getKey().getY();

        double P3Card1X = p3Choose.get(0).getKey().getX();
        double P3Card1Y = p3Choose.get(0).getKey().getY();
        double P3Card2X = p3Choose.get(1).getKey().getX();
        double P3Card2Y = p3Choose.get(1).getKey().getY();
        double P3Card3X = p3Choose.get(2).getKey().getX();
        double P3Card3Y = p3Choose.get(2).getKey().getY();

        double P4Card1X = p4Choose.get(0).getKey().getX();
        double P4Card1Y = p4Choose.get(0).getKey().getY();
        double P4Card2X = p4Choose.get(1).getKey().getX();
        double P4Card2Y = p4Choose.get(1).getKey().getY();
        double P4Card3X = p4Choose.get(2).getKey().getX();
        double P4Card3Y = p4Choose.get(2).getKey().getY();

        // Transition
        // PathTransition path1card1 = new PathTransition(Duration.millis(500),
                // new Line(P1Card1X, P1Card1Y, P2Card1X, P2Card1Y), p1Choose.get(0).getKey());
        p1Choose.get(0).getKey().setX(P2Card1X);
        p1Choose.get(0).getKey().setY(P2Card1Y);
        p1Choose.get(0).getKey().setRotate(90);
        // PathTransition path1card2 = new PathTransition(Duration.millis(500),
                // new Line(P1Card2X, P1Card2Y, P2Card2X, P2Card2Y), p1Choose.get(1).getKey());
        p1Choose.get(1).getKey().setX(P2Card2X);
        p1Choose.get(1).getKey().setY(P2Card2Y);
        p1Choose.get(1).getKey().setRotate(90);
        // PathTransition path1card3 = new PathTransition(Duration.millis(500),
                // new Line(P1Card3X, P1Card3Y, P2Card3X, P2Card3Y), p1Choose.get(2).getKey());
        p1Choose.get(2).getKey().setX(P2Card3X);
        p1Choose.get(2).getKey().setY(P2Card3Y);
        p1Choose.get(2).getKey().setRotate(90);

        // PathTransition path2card1 = new PathTransition(Duration.millis(500),
                // new Line(P2Card1X, P2Card1Y, P3Card1X, P3Card1Y), p2Choose.get(0).getKey());
        p2Choose.get(0).getKey().setX(P3Card1X);
        p2Choose.get(0).getKey().setY(P3Card1Y);
        p2Choose.get(0).getKey().setRotate(0);
        // PathTransition path2card2 = new PathTransition(Duration.millis(500),
                // new Line(P2Card2X, P2Card2Y, P3Card2X, P3Card2Y), p2Choose.get(1).getKey());
        p2Choose.get(1).getKey().setX(P3Card2X);
        p2Choose.get(1).getKey().setY(P3Card2Y);
        p2Choose.get(1).getKey().setRotate(0);
        // PathTransition path2card3 = new PathTransition(Duration.millis(500),
                // new Line(P2Card3X, P2Card3Y, P3Card3X, P3Card3Y), p2Choose.get(2).getKey());
        p2Choose.get(2).getKey().setX(P3Card3X);
        p2Choose.get(2).getKey().setY(P3Card3Y);
        p2Choose.get(2).getKey().setRotate(0);

        // PathTransition path3card1 = new PathTransition(Duration.millis(500),
                // new Line(P3Card1X, P3Card1Y, P4Card1X, P4Card1Y), p3Choose.get(0).getKey());
        p3Choose.get(0).getKey().setX(P4Card1X);
        p3Choose.get(0).getKey().setY(P4Card1Y);
        p3Choose.get(0).getKey().setRotate(-90);
        // PathTransition path3card2 = new PathTransition(Duration.millis(500),
                // new Line(P3Card2X, P3Card2Y, P4Card2X, P4Card2Y), p3Choose.get(1).getKey());
        p3Choose.get(1).getKey().setX(P4Card2X);
        p3Choose.get(1).getKey().setY(P4Card2Y);
        p3Choose.get(1).getKey().setRotate(-90);
        // PathTransition path3card3 = new PathTransition(Duration.millis(500),
        //         new Line(P3Card3X, P3Card3Y, P4Card3X, P4Card3Y), p3Choose.get(2).getKey());
        p3Choose.get(2).getKey().setX(P4Card3X);
        p3Choose.get(2).getKey().setY(P4Card3Y);
        p3Choose.get(2).getKey().setRotate(-90);

        // PathTransition path4card1 = new PathTransition(Duration.millis(500),
        //         new Line(P4Card1X, P4Card1Y, P1Card1X, P1Card1Y), p4Choose.get(0).getKey());
        p4Choose.get(0).getKey().setX(P1Card1X);
        p4Choose.get(0).getKey().setY(P1Card1Y);
        p4Choose.get(0).getKey().setRotate(0);
        // PathTransition path4card2 = new PathTransition(Duration.millis(500),
        //         new Line(P4Card2X, P4Card2Y, P1Card2X, P1Card2Y), p4Choose.get(1).getKey());
        p4Choose.get(1).getKey().setX(P1Card2X);
        p4Choose.get(1).getKey().setY(P1Card2Y);
        p4Choose.get(1).getKey().setRotate(0);
        // PathTransition path4card3 = new PathTransition(Duration.millis(500),
        //         new Line(P4Card3X, P4Card3Y, P1Card3X, P1Card3Y), p4Choose.get(2).getKey());
        p4Choose.get(2).getKey().setX(P1Card3X);
        p4Choose.get(2).getKey().setY(P1Card3Y);
        p4Choose.get(2).getKey().setRotate(0);
// change card
        player1.set(p1ChooseIndex.get(0), p4Choose.get(0));
        player1.set(p1ChooseIndex.get(1), p4Choose.get(1));
        player1.set(p1ChooseIndex.get(2), p4Choose.get(2));
        player2.set(p2ChooseIndex.get(0), p1Choose.get(0));
        player2.set(p2ChooseIndex.get(1), p1Choose.get(1));
        player2.set(p2ChooseIndex.get(2), p1Choose.get(2));
        player3.set(p3ChooseIndex.get(0), p2Choose.get(0));
        player3.set(p3ChooseIndex.get(1), p2Choose.get(1));
        player3.set(p3ChooseIndex.get(2), p2Choose.get(2));
        player4.set(p4ChooseIndex.get(0), p3Choose.get(0));
        player4.set(p4ChooseIndex.get(1), p3Choose.get(1));
        player4.set(p4ChooseIndex.get(2), p3Choose.get(2));

        // animation
        // path1card1.play();
        // path1card2.play();
        // path1card3.play();
        // path2card1.play();
        // path2card2.play();
        // path2card3.play();
        // path3card1.play();
        // path3card2.play();
        // path3card3.play();
        // path4card1.play();
        // path4card2.play();
        // path4card3.play();

        // remove choose card
        // p1Choose.removeAll();
        // p1ChooseIndex.removeAll();
        // p2Choose.removeAll();
        // p2ChooseIndex.removeAll();
        // p3Choose.removeAll();
        // p3ChooseIndex.removeAll();
        // p4Choose.removeAll();
        // p4ChooseIndex.removeAll();
        swap.setVisible(false);

        // refreshCard();
        sortCardsInHands();

        // swap in hearts
        var hands = hearts.getHands();
        hands[0].setChosenGiveCards(new int[]{p1ChooseIndex.get(0).intValue(), p1ChooseIndex.get(1).intValue(), p1ChooseIndex.get(2).intValue()});
        hands[0].giveCard(hands[1]);
        hands[1].giveCard(hands[2]);
        hands[2].giveCard(hands[3]);
        hands[3].giveCard(hands[0]);
        refreshCard();
        begin();
    }

    public void rightSwap(){
        p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
        p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
        p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);
        // place of cards
        double P1Card1X = p1Choose.get(0).getKey().getX();
        double P1Card1Y = p1Choose.get(0).getKey().getY();
        double P1Card2X = p1Choose.get(1).getKey().getX();
        double P1Card2Y = p1Choose.get(1).getKey().getY();
        double P1Card3X = p1Choose.get(2).getKey().getX();
        double P1Card3Y = p1Choose.get(2).getKey().getY();

        double P2Card1X = p2Choose.get(0).getKey().getX();
        double P2Card1Y = p2Choose.get(0).getKey().getY();
        double P2Card2X = p2Choose.get(1).getKey().getX();
        double P2Card2Y = p2Choose.get(1).getKey().getY();
        double P2Card3X = p2Choose.get(2).getKey().getX();
        double P2Card3Y = p2Choose.get(2).getKey().getY();

        double P3Card1X = p3Choose.get(0).getKey().getX();
        double P3Card1Y = p3Choose.get(0).getKey().getY();
        double P3Card2X = p3Choose.get(1).getKey().getX();
        double P3Card2Y = p3Choose.get(1).getKey().getY();
        double P3Card3X = p3Choose.get(2).getKey().getX();
        double P3Card3Y = p3Choose.get(2).getKey().getY();

        double P4Card1X = p4Choose.get(0).getKey().getX();
        double P4Card1Y = p4Choose.get(0).getKey().getY();
        double P4Card2X = p4Choose.get(1).getKey().getX();
        double P4Card2Y = p4Choose.get(1).getKey().getY();
        double P4Card3X = p4Choose.get(2).getKey().getX();
        double P4Card3Y = p4Choose.get(2).getKey().getY();

        p1Choose.get(0).getKey().setX(P4Card1X);
        p1Choose.get(0).getKey().setY(P4Card1Y);
        p1Choose.get(0).getKey().setRotate(-90);
        p1Choose.get(1).getKey().setX(P4Card2X);
        p1Choose.get(1).getKey().setY(P4Card2Y);
        p1Choose.get(1).getKey().setRotate(-90);
        p1Choose.get(2).getKey().setX(P4Card3X);
        p1Choose.get(2).getKey().setY(P4Card3Y);
        p1Choose.get(2).getKey().setRotate(-90);
        
        p4Choose.get(0).getKey().setX(P3Card1X);
        p4Choose.get(0).getKey().setY(P3Card1Y);
        p4Choose.get(0).getKey().setRotate(0);
        p4Choose.get(1).getKey().setX(P3Card2X);
        p4Choose.get(1).getKey().setY(P3Card2Y);
        p4Choose.get(1).getKey().setRotate(0);
        p4Choose.get(2).getKey().setX(P3Card3X);
        p4Choose.get(2).getKey().setY(P3Card3Y);
        p4Choose.get(2).getKey().setRotate(0);

        p3Choose.get(0).getKey().setX(P2Card1X);
        p3Choose.get(0).getKey().setY(P2Card1Y);
        p3Choose.get(0).getKey().setRotate(90);
        p3Choose.get(1).getKey().setX(P2Card2X);
        p3Choose.get(1).getKey().setY(P2Card2Y);
        p3Choose.get(1).getKey().setRotate(90);
        p3Choose.get(2).getKey().setX(P2Card3X);
        p3Choose.get(2).getKey().setY(P2Card3Y);
        p3Choose.get(2).getKey().setRotate(90);

        p2Choose.get(0).getKey().setX(P1Card1X);
        p2Choose.get(0).getKey().setY(P1Card1Y);
        p2Choose.get(0).getKey().setRotate(0);
        p2Choose.get(0).getKey().setX(P1Card2X);
        p2Choose.get(0).getKey().setY(P1Card2Y);
        p2Choose.get(0).getKey().setRotate(0);
        p2Choose.get(0).getKey().setX(P1Card3X);
        p2Choose.get(0).getKey().setY(P1Card3Y);
        p2Choose.get(0).getKey().setRotate(0);

        //change cards
        player1.set(p1ChooseIndex.get(0), p2Choose.get(0));
        player1.set(p1ChooseIndex.get(1), p2Choose.get(1));
        player1.set(p1ChooseIndex.get(2), p2Choose.get(2));
        player2.set(p2ChooseIndex.get(0), p3Choose.get(0));
        player2.set(p2ChooseIndex.get(1), p3Choose.get(1));
        player2.set(p2ChooseIndex.get(2), p3Choose.get(2));
        player3.set(p3ChooseIndex.get(0), p4Choose.get(0));
        player3.set(p3ChooseIndex.get(1), p4Choose.get(1));
        player3.set(p3ChooseIndex.get(2), p4Choose.get(2));
        player4.set(p4ChooseIndex.get(0), p1Choose.get(0));
        player4.set(p4ChooseIndex.get(1), p1Choose.get(1));
        player4.set(p4ChooseIndex.get(2), p1Choose.get(2));
        
        //remove choose card
        // p1Choose.removeAll();
        // p1ChooseIndex.removeAll();
        // p2Choose.removeAll();
        // p2ChooseIndex.removeAll();
        // p3Choose.removeAll();
        // p3ChooseIndex.removeAll();
        // p4Choose.removeAll();
        // p4ChooseIndex.removeAll();
        sortCardsInHands();
        refreshCard();
        swap.setVisible(false);
    }

    public void oppositeSwap(){
        p1Choose.get(0).getKey().setY(p1Choose.get(0).getKey().getY() + 10);
        p1Choose.get(1).getKey().setY(p1Choose.get(1).getKey().getY() + 10);
        p1Choose.get(2).getKey().setY(p1Choose.get(2).getKey().getY() + 10);
        // place of cards
        double P1Card1X = p1Choose.get(0).getKey().getX();
        double P1Card1Y = p1Choose.get(0).getKey().getY();
        double P1Card2X = p1Choose.get(1).getKey().getX();
        double P1Card2Y = p1Choose.get(1).getKey().getY();
        double P1Card3X = p1Choose.get(2).getKey().getX();
        double P1Card3Y = p1Choose.get(2).getKey().getY();

        double P2Card1X = p2Choose.get(0).getKey().getX();
        double P2Card1Y = p2Choose.get(0).getKey().getY();
        double P2Card2X = p2Choose.get(1).getKey().getX();
        double P2Card2Y = p2Choose.get(1).getKey().getY();
        double P2Card3X = p2Choose.get(2).getKey().getX();
        double P2Card3Y = p2Choose.get(2).getKey().getY();

        double P3Card1X = p3Choose.get(0).getKey().getX();
        double P3Card1Y = p3Choose.get(0).getKey().getY();
        double P3Card2X = p3Choose.get(1).getKey().getX();
        double P3Card2Y = p3Choose.get(1).getKey().getY();
        double P3Card3X = p3Choose.get(2).getKey().getX();
        double P3Card3Y = p3Choose.get(2).getKey().getY();

        double P4Card1X = p4Choose.get(0).getKey().getX();
        double P4Card1Y = p4Choose.get(0).getKey().getY();
        double P4Card2X = p4Choose.get(1).getKey().getX();
        double P4Card2Y = p4Choose.get(1).getKey().getY();
        double P4Card3X = p4Choose.get(2).getKey().getX();
        double P4Card3Y = p4Choose.get(2).getKey().getY();

        p1Choose.get(0).getKey().setX(P3Card1X);
        p1Choose.get(0).getKey().setY(P3Card1Y);
        p1Choose.get(0).getKey().setRotate(0);
        p1Choose.get(1).getKey().setX(P3Card2X);
        p1Choose.get(1).getKey().setY(P3Card2Y);
        p1Choose.get(1).getKey().setRotate(0);
        p1Choose.get(2).getKey().setX(P3Card3X);
        p1Choose.get(2).getKey().setY(P3Card3Y);
        p1Choose.get(2).getKey().setRotate(0);
        
        p3Choose.get(0).getKey().setX(P1Card1X);
        p3Choose.get(0).getKey().setY(P1Card1Y);
        p3Choose.get(0).getKey().setRotate(0);
        p3Choose.get(1).getKey().setX(P1Card2X);
        p3Choose.get(1).getKey().setY(P1Card2Y);
        p3Choose.get(1).getKey().setRotate(0);
        p3Choose.get(2).getKey().setX(P1Card3X);
        p3Choose.get(2).getKey().setY(P1Card3Y);
        p3Choose.get(2).getKey().setRotate(0);

        p4Choose.get(0).getKey().setX(P2Card1X);
        p4Choose.get(0).getKey().setY(P2Card1Y);
        p4Choose.get(0).getKey().setRotate(90);
        p4Choose.get(1).getKey().setX(P2Card2X);
        p4Choose.get(1).getKey().setY(P2Card2Y);
        p4Choose.get(1).getKey().setRotate(90);
        p4Choose.get(2).getKey().setX(P2Card3X);
        p4Choose.get(2).getKey().setY(P2Card3Y);
        p4Choose.get(2).getKey().setRotate(90);

        p2Choose.get(0).getKey().setX(P4Card1X);
        p2Choose.get(0).getKey().setY(P4Card1Y);
        p2Choose.get(0).getKey().setRotate(-90);
        p2Choose.get(0).getKey().setX(P4Card2X);
        p2Choose.get(0).getKey().setY(P4Card2Y);
        p2Choose.get(0).getKey().setRotate(-90);
        p2Choose.get(0).getKey().setX(P4Card3X);
        p2Choose.get(0).getKey().setY(P4Card3Y);
        p2Choose.get(0).getKey().setRotate(-90);

        //change cards
        player1.set(p1ChooseIndex.get(0), p3Choose.get(0));
        player1.set(p1ChooseIndex.get(1), p3Choose.get(1));
        player1.set(p1ChooseIndex.get(2), p3Choose.get(2));
        player2.set(p2ChooseIndex.get(0), p4Choose.get(0));
        player2.set(p2ChooseIndex.get(1), p4Choose.get(1));
        player2.set(p2ChooseIndex.get(2), p4Choose.get(2));
        player3.set(p3ChooseIndex.get(0), p1Choose.get(0));
        player3.set(p3ChooseIndex.get(1), p1Choose.get(1));
        player3.set(p3ChooseIndex.get(2), p1Choose.get(2));
        player4.set(p4ChooseIndex.get(0), p2Choose.get(0));
        player4.set(p4ChooseIndex.get(1), p2Choose.get(1));
        player4.set(p4ChooseIndex.get(2), p2Choose.get(2));
        
        //remove choose card
        // p1Choose.removeAll();
        // p1ChooseIndex.removeAll();
        // p2Choose.removeAll();
        // p2ChooseIndex.removeAll();
        // p3Choose.removeAll();
        // p3ChooseIndex.removeAll();
        // p4Choose.removeAll();
        // p4ChooseIndex.removeAll();
        sortCardsInHands();
        refreshCard();
        swap.setVisible(false);
    }

    public void nextAction() {
        if (hearts.getTurn() == 4) {
            //If small round is end. Find winner and add score.
            int winner = hearts.getTable().findWinner();
            hearts.setWhoseTurn(winner);
            for(Card card : hearts.getTable().popAllCards()){
                hearts.getHands()[winner].addCardInPile(card);
            }
            hearts.setTurn(0);
            root.getChildren().removeAll(cardOnTable);
            //get imageView
            // for(ImageView temp: cardOnTable){
            //     root.getChildren().remove(temp);
            // }
            
        }
        else{
            hearts.setWhoseTurn(hearts.getWhoseTurn() + 1);
                if(hearts.getWhoseTurn() >= 4){
                    hearts.setWhoseTurn(0);
                }
        }
        switch (hearts.getWhoseTurn()) {
            case 0:
                Player player = (Player)hearts.getHands()[0];
                player.setChosenPlaceCard(-1);
                while(player.getChosenPlaceCard() == -1) {System.out.println("Loopy\n");};
                hearts.getTable().placeCardAt(player.getCardsInHand().get(player.getChosenPlaceCard()), 0);
                System.out.println(player.getChosenPlaceCard());
                cardOnTable.add(player1.get(player.getChosenPlaceCard()).getKey());
                break;
            case 1:
                Bot bot1 = (Bot)hearts.getHands()[1];
                if(hearts.getHands()[1] instanceof Bot){
                    bot1.chooseCardToPlace();
                }

                hearts.getTable().placeCardAt(bot1.getCardsInHand().get(bot1.getChosenPlaceCard()), 1);
                bot1.removeCardInHand(bot1.getCardsInHand().get(bot1.getChosenPlaceCard()));
                player2.get(bot1.getChosenPlaceCard()).getKey().setX(posCenterX);
                player2.get(bot1.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

                cardOnTable.add(player2.get(bot1.getChosenPlaceCard()).getKey());

                //delay 2

                break;
            case 2:
                Bot bot2 = (Bot)hearts.getHands()[2];
                if(hearts.getHands()[2] instanceof Bot){
                    bot2.chooseCardToPlace();
                }

                hearts.getTable().placeCardAt(bot2.getCardsInHand().get(bot2.getChosenPlaceCard()), 2);
                bot2.removeCardInHand(bot2.getCardsInHand().get(bot2.getChosenPlaceCard()));
                player3.get(bot2.getChosenPlaceCard()).getKey().setX(posCenterX);
                player3.get(bot2.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

                cardOnTable.add(player3.get(bot2.getChosenPlaceCard()).getKey());

                //delay 2
                break;
            case 3:
                Bot bot3 = (Bot)hearts.getHands()[3];
                if(hearts.getHands()[3] instanceof Bot){
                    bot3.chooseCardToPlace();
                }

                hearts.getTable().placeCardAt(bot3.getCardsInHand().get(bot3.getChosenPlaceCard()), 3);
                bot3.removeCardInHand(bot3.getCardsInHand().get(bot3.getChosenPlaceCard()));
                player3.get(bot3.getChosenPlaceCard()).getKey().setX(posCenterX);
                player3.get(bot3.getChosenPlaceCard()).getKey().setY(posCenterY + 100);

                cardOnTable.add(player4.get(bot3.getChosenPlaceCard()).getKey());
                
                //delay 2
                break;
        }
        updateNotificationWhoseTurn();
    }
    //begin small rouund
    public void begin() {
        for (int i = 0; i < player1.size(); i++) {
            player1.get(i).getKey().setOnMouseClicked(e -> {
                if (!(this.hearts.getWhoseTurn() == 0)) return; 
                ImageView temp = (ImageView) e.getSource();
                    PathTransition dropCard = new PathTransition(Duration.millis(500),
                            new Line(temp.getX(), temp.getY(), posCenterX + 50, posCenterY + 160), temp);
                    temp.setX(posCenterX + 50);
                    temp.setY(posCenterY + 160);
                    dropCard.play();
                    
                    int chIdx = -1;
                    for (int j = 0; j < player1.size(); j++) {
                        if (temp == player1.get(j).getKey()) {
                            System.out.println("clicked + " + j);
                            chIdx = j;
                        }
                    }
                    // break while loop condition
                    hearts.getHands()[0].setChosenPlaceCard(chIdx);
                    // hearts.getHands()[0].setChosenPlaceCard(j);
                this.updateNotificationWhoseTurn();
            });
        }

        hearts.setWhoseTurn(hearts.whoFirst());
        updateNotificationWhoseTurn();

        for(int i = 0; i < 13; i++)
            for (int k = 0; i < 4; k++){
                nextAction();
            } 
                
        

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
    }

    private void updateNotificationWhoseTurn() {
        notification.setText(String.format("Small Round %d\n%s's turn", this.hearts.getSmallRound(),this.hearts.getHands()[this.hearts.getWhoseTurn()].getName()));
    }

    public void refreshCard() {
        root.getChildren().removeAll(imgs);
        posXPlayer1 = 600;
        posyPlayer1 = 600;
        posXPlayer2 = 300;
        posyPlayer2 = 300;
        posXPlayer3 = 840;
        posyPlayer3 = 70;
        posXPlayer4 = 1100;
        posyPlayer4 = 420;

        for (int i = 0; i < player1.size(); i++) {
            System.out.println(player1.get(i).getValue().getCard().toString());
            player1.get(i).getKey().setX(posXPlayer1);
            player1.get(i).getKey().setY(posyPlayer1);
            posXPlayer1 += 20;
            System.out.println(String.format("P1 POSX : %f",player1.get(i).getKey().getX()));
            System.out.println(String.format("P1 POSY : %f",player1.get(i).getKey().getY()));
            root.getChildren().add(player1.get(i).getKey());
        }
        for (int i = 0; i < player2.size(); i++) {
            player2.get(i).getKey().setX(posXPlayer2);
            player2.get(i).getKey().setY(posyPlayer2);
            player2.get(i).getKey().setRotate(90);
            posyPlayer2 += 10;
            root.getChildren().add(player2.get(i).getKey());
        }
        for (int i = 0; i < player3.size(); i++) {
            player3.get(i).getKey().setX(posXPlayer3);
            player3.get(i).getKey().setY(posyPlayer3);
            posXPlayer3 -= 20;
            root.getChildren().add(player3.get(i).getKey());
        }
        for (int i = 0; i < player4.size(); i++) {
            player4.get(i).getKey().setX(posXPlayer4);
            player4.get(i).getKey().setY(posyPlayer4);
            player4.get(i).getKey().setRotate(-90);
            posyPlayer4 -= 10;
            root.getChildren().add(player4.get(i).getKey());
        }
    }

    public void placeCardToTable(int whoseTurn, int indexOfCard) {

    }

    private void sortCardsInHands() {
        hearts.sortCardsInHands();
        var players = new ArrayList<ArrayList<Pair<ImageView, CardController>>>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        int i = 0;
        for (Hand hand : hearts.getHands()) {
            for (Card card : hand.getCardsInHand()) {
                players.get(i).sort((cardC1, cardC2) -> {
                    var card1 = cardC1.getValue().getCard();
                    var card2 = cardC2.getValue().getCard();
                    return card1.compareTo(card2);
                });
            }
            i++;
        }
    }

    @FXML
    private void exitRelease(MouseEvent event) {
        btnExit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void exitOut(MouseEvent event) {
        btnExit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void exitEnter(MouseEvent event) {
        btnExit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void exitPressed(MouseEvent event) {
        btnExit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void dealRelease(MouseEvent event) {
        btnDeal.setStyle("-fx-background-color:#01B075;");
    }

    @FXML
    private void dealExit(MouseEvent event) {
        btnDeal.setStyle("-fx-background-color:linear-gradient(to bottom,#2D9D3B, #68C974, #CDF4D2);");
    }

    @FXML
    private void dealEnter(MouseEvent event) {
        btnDeal.setStyle("-fx-background-color:#01B075;");
    }

    @FXML
    private void dealPressed(MouseEvent event) {

        btnDeal.setStyle("-fx-background-color:linear-gradient(to bottom,#2D9D3B, #68C974, #CDF4D2);");
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
