/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.credit;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author LENOVO LEGION
 */
public class CreditController implements Initializable {

    @FXML
    private ImageView heart;
    @FXML
    private ImageView apple;
    @FXML
    private ImageView people;
    @FXML
    private ImageView stop;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        PathTransition path = new PathTransition(Duration.millis(2000), new Line(100, 450, 100, -150), heart);
        path.setAutoReverse(true);
        path.setCycleCount(Timeline.INDEFINITE);
        path.play();
        PathTransition path1 = new PathTransition(Duration.millis(2000), new Line(110, -150, 110, 450), apple);
        path1.setAutoReverse(true);
        path1.setCycleCount(Timeline.INDEFINITE);
        path1.play();
        PathTransition path2 = new PathTransition(Duration.millis(2000), new Line(90, 450, 90, -150), people);
        path2.setAutoReverse(true);
        path2.setCycleCount(Timeline.INDEFINITE);
        path2.play();
        PathTransition path3 = new PathTransition(Duration.millis(2000), new Line(80, -150, 80, 450), stop);
        path3.setAutoReverse(true);
        path3.setCycleCount(Timeline.INDEFINITE);
        path3.play();
    }

}
