package components.game;

import javafx.event.EventHandler;
import javafx.scene.control.Label;

public abstract class GameEventHandler implements EventHandler<GameEvent> {

  public abstract void onEndTurn(int whoseTurn);
  public abstract void onStartTurn();

  @Override
  public void handle(GameEvent event) {
    event.invokeHandler(this);
  }

}

