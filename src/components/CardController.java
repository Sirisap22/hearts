package components;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardController implements Initializable {

  @FXML
  private ImageView cardImage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    cardImage.setImage(new Image("public/test_card.png"));
  }

}
