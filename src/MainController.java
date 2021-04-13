
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

public class MainController implements Initializable {

    private Pane rulesScene = new Pane();
    private Pane creditScene = new Pane();
    private Pane anotherScene = new Pane();
    private Pane game = new Pane();
    private double setX = 0;
    private double setY = 0;

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
        rules.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/Rules.fxml"));
            try {
                rulesScene = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            borderPane.setRight(rulesScene);
        });
        credit.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/Credit.fxml"));

            try {
                creditScene = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            borderPane.setRight(creditScene);
        });
        another.setOnAction(e -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scene/Another.fxml"));

            try {
                anotherScene = loader.load();
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            borderPane.setRight(anotherScene);
        });
        Home.setOnAction(e -> {
            borderPane.setRight(scenePane);
        });
        quit.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });
    }

    @FXML
    private void playGame(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GameTest/Game.fxml"));
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
        Home.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void HomeExit(MouseEvent event) {
        Home.setStyle("-fx-background-color:#C92A42");
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
        rules.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void ruleEnter(MouseEvent event) {
        rules.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void rulePress(MouseEvent event) {
        rules.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void creditRelese(MouseEvent event) {
        credit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void creditExit(MouseEvent event) {
        credit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void creditEnter(MouseEvent event) {
        credit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void creditPress(MouseEvent event) {
        credit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void anotherRelese(MouseEvent event) {
        another.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void anotherExit(MouseEvent event) {
        another.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void anotherEnter(MouseEvent event) {
        another.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void anotherPress(MouseEvent event) {
        another.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void quitRelese(MouseEvent event) {
        quit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void quitExit(MouseEvent event) {
        quit.setStyle("-fx-background-color:#C92A42");
    }

    @FXML
    private void quitEnter(MouseEvent event) {
        quit.setStyle("-fx-background-color:#F66E63");
    }

    @FXML
    private void quitPress(MouseEvent event) {
        quit.setStyle("-fx-background-color:#C92A42");
    }

}
