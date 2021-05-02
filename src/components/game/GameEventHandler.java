package components.game;

import javafx.event.EventHandler;

public abstract class GameEventHandler implements EventHandler<GameEvent> {

  public abstract void onEndTurn();
  public abstract void onStartTurn();

  @Override
  public void handle(GameEvent event) {
    event.invokeHandler(this);
  }

}

