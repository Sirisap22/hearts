/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.game;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import components.card.CardController;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.animation.PathTransition;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import javafx.util.Pair;
import models.Deck;
import utils.ComponentLoader;

public class GameController implements Initializable {
    // private ArrayList<ImageView> deck = new ArrayList<>(52);
    private ComponentLoader<GameController> loader = new ComponentLoader<>();
    private HashMap<String, Pair<ImageView, CardController>> cards = new HashMap<>();
    private ImageView[] card = new ImageView[52];
    // private ArrayList<ImageView> player1 =new ArrayList<>(13);
    // private ArrayList<ImageView> player2 =new ArrayList<>(13);
    // private ArrayList<ImageView> player3 =new ArrayList<>(13);
    // private ArrayList<ImageView> player4 =new ArrayList<>(13);
    private ImageView[] player1 = new ImageView[13];
    private ImageView[] player2 = new ImageView[13];
    private ImageView[] player3 = new ImageView[13];
    private ImageView[] player4 = new ImageView[13];

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
    private double posXPlayer3 = 600;
    private double posyPlayer3 = 70;
    private double posXPlayer4 = 1100;
    private double posyPlayer4 = 300;

    @FXML
    private Pane root = new Pane();
    @FXML
    private Button btnDeal;
    @FXML
    private Button swap;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Deck temp = new Deck();
        // temp.refresh();

        // // create all cards
        // try {
        // int t = 0;
        // for (var card : temp) {
        // Pair<ImageView, CardController> cardComponent = loader.loadComponent(this,
        // "components/card/Card.fxml");
        // CardController tempCon = cardComponent.getValue();
        // tempCon.setCard(card);
        // tempCon.updateCardImage();
        // ImageView tempView = cardComponent.getKey();
        // tempView.setX(posCenterX);
        // tempView.setY(posCenterY);
        // tempView.setFitHeight(cardHeight);
        // tempView.setFitWidth(cardWidth);
        // this.cards.put(card.toString(), cardComponent);
        // this.card[t] = cardComponent.getKey();
        // t++;
        // }
        // } catch (Exception e) {

        // }

        // swap.setVisible(false);
        // // for (int i = 0; i < card.length; i++) {
        // // card[i] = new ImageView(posCenterX, posCenterY, cardWidth, cardHeight);
        // // card[i].setFill(Color.BISQUE);
        // // card[i].setStroke(Color.BLACK);

        // // }

        // // for(int i=0;i<deck.size();i++){
        // // ImageView reg=deck.get(i);
        // // reg=new ImageView(500, 200, 50, 100);
        // // reg.setWidth(50);
        // // reg.setHeight(100);
        // // reg.setFill(Color.BLUE);
        // // }
        // // root.getChildren().addAll(deck);

        // root.getChildren().addAll(Arrays.asList(card));

    }

    @FXML
    private void onStart() {
        Deck temp = new Deck();
        temp.refresh();
        System.out.println("hello of on start");

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
                this.card[t] = this.cards.get(card.toString()).getKey();
                // System.out.println("hello of on start10");
                t++;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        // System.out.println(this.card[0].toString());

        swap.setVisible(false);

        root.getChildren().addAll(Arrays.asList(card));
    }

    @FXML
    private void exit(ActionEvent event) throws IOException {
        Platform.exit();
        System.exit(0);
        // Parent root =
        // FXMLLoader.load(getClass().getResource("/components/main/Main.fxml"));
        // Scene gameScene = new Scene(root);
        // gameScene.setFill(Color.TRANSPARENT);
        // Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // window.setScene(gameScene);
    }

    @FXML
    private void dealCard(ActionEvent event) {
        int j = 0;
        int y = 0;
        for (int i = 0; i < 13; i++) {
            // if(j%2!=0){
            // rg[j].setRotate(90);
            // }
            PathTransition path1 = new PathTransition(Duration.millis(500),
                    new Line(posCenterX, posCenterY, posXPlayer1, posyPlayer1), card[j]);
            card[j].setX(posXPlayer1);
            card[j].setY(posyPlayer1);
            player1[y] = card[j];
            j++;
            path1.play();
            posXPlayer1 += 20;
            if (j % 2 != 0) {
                card[j].setRotate(90);
            }
            PathTransition path2 = new PathTransition(Duration.millis(500),
                    new Line(posCenterX, posCenterY, posXPlayer2, posyPlayer2), card[j]);
            card[j].setX(posXPlayer2);
            card[j].setY(posyPlayer2);
            player2[y] = card[j];
            j++;
            posyPlayer2 += 10;
            path2.play();
            // if(j%2!=0){
            // rg[j].setRotate(90);
            // }
            PathTransition path3 = new PathTransition(Duration.millis(500),
                    new Line(posCenterX, posCenterY, posXPlayer3, posyPlayer3), card[j]);
            card[j].setX(posXPlayer3);
            card[j].setY(posyPlayer3);
            player3[y] = card[j];
            j++;
            posXPlayer3 += 20;
            path3.play();
            if (j % 2 != 0) {
                card[j].setRotate(90);
            }
            PathTransition path4 = new PathTransition(Duration.millis(500),
                    new Line(posCenterX, posCenterY, posXPlayer4, posyPlayer4), card[j]);
            card[j].setX(posXPlayer4);
            card[j].setY(posyPlayer4);
            player4[y++] = card[j];
            j++;
            posyPlayer4 += 10;
            path4.play();

        }
        btnDeal.setVisible(false);
        swapCard();
    }

    private void swapCard() {

        ImageView[] exchangeCard = new ImageView[3];
        for (int i = 0; i < player1.length; i++) {
            player1[i].setOnMouseClicked(e -> {
                ImageView temp = (ImageView) e.getSource();
                // temp.setFill(Color.BLACK);
                if (temp.getY() == 590) {
                    PathTransition slideDown = new PathTransition(Duration.millis(100),
                            new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() + 10), temp);
                    temp.setY(temp.getY() + 10);
                    slideDown.play();
                } else {
                    PathTransition slideUp = new PathTransition(Duration.millis(100),
                            new Line(temp.getX(), temp.getY(), temp.getX(), temp.getY() - 10), temp);
                    temp.setY(temp.getY() - 10);
                    slideUp.play();
                }
                // System.out.println(e.getSource());
            });
        }
        swap.setVisible(true);
        swap.setOnAction(e -> {
            ImageView temp;
            double placeX1 = player1[0].getX();
            double placeY1 = player1[0].getY();
            double placeX2 = player2[0].getX();
            double placeY2 = player2[0].getY();

            PathTransition swapCard1 = new PathTransition(Duration.millis(500),
                    new Line(placeX1, placeY1, placeX2, placeY2), player1[0]);
            player1[0].setX(placeX2);
            player1[0].setY(placeY2);
            player1[0].setRotate(90);
            // player1[0].setFill(Color.RED);
            swapCard1.play();

            PathTransition swapCard2 = new PathTransition(Duration.millis(500),
                    new Line(placeX2, placeY2, placeX1, placeY1), player2[0]);
            player2[0].setX(placeX1);
            player2[0].setY(placeY1);
            player2[0].setRotate(0);
            // player2[0].setFill(Color.BLUE);
            swapCard2.play();

            // switch card in player hand
            temp = player1[0];
            player1[0] = player2[0];
            player2[0] = temp;
            swap.setVisible(false);
            begin();
        });

    }

    private void begin() {
        for (int i = 0; i < player1.length; i++) {
            player1[i].setOnMouseClicked(e -> {
                ImageView temp = (ImageView) e.getSource();
                PathTransition dropCard = new PathTransition(Duration.millis(500),
                        new Line(temp.getX(), temp.getY(), posCenterX + 80, posCenterY + 150), temp);
                dropCard.play();
            });
        }
    }
}
