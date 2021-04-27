package components.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Sounds;

public class MainController implements Initializable {

    private Pane rulesScene = new Pane();
    private Pane creditScene = new Pane();
    private Pane game = new Pane();
    private double setX = 0;
    private double setY = 0;
    private Sounds sound = new Sounds();

    @FXML
    private Button Home;
    @FXML
    private Pane scenePane;
    @FXML
    private Button rules;
    @FXML
    private Button credit;
    @FXML
    private Button another;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button quit;
    @FXML
    private Button playBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // sound.getMusicMenuPlay();
        rules.setOnAction(e -> {
            // sound.getSoundMenuClickPlay();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/rules/Rules.fxml"));
            try {
                rulesScene = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            borderPane.setRight(rulesScene);
        });
        credit.setOnAction(e -> {
            // sound.getSoundMenuClickPlay();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/components/credit/Credit.fxml"));

            try {
                creditScene = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            borderPane.setRight(creditScene);
        });
        Home.setOnAction(e -> {
            // sound.getSoundMenuClickPlay();
            borderPane.setRight(scenePane);
        });
        quit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    private void playGame(ActionEvent event) throws IOException {
        // sound.getMusicMenuStop();
        Parent root = FXMLLoader.load(getClass().getResource("/components/game/Game.fxml"));
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
    private void HomePress(MouseEvent event) {
        Home.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void HomeExit(MouseEvent event) {
        Home.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void HomeEnter(MouseEvent event) {
        Home.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void HomeRelease(MouseEvent event) {
        Home.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void ruleRelese(MouseEvent event) {
        rules.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void ruleExit(MouseEvent event) {
        rules.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void ruleEnter(MouseEvent event) {
        rules.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void rulePress(MouseEvent event) {
        rules.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void creditRelese(MouseEvent event) {
        credit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void creditExit(MouseEvent event) {
        credit.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void creditEnter(MouseEvent event) {
        credit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void creditPress(MouseEvent event) {
        credit.setStyle("-fx-background-color:transparent");
    }

    
    @FXML
    private void quitRelese(MouseEvent event) {
        quit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void quitExit(MouseEvent event) {
        quit.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void quitEnter(MouseEvent event) {
        quit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void quitPress(MouseEvent event) {
        quit.setStyle("-fx-background-color:transparent");
    }

}
