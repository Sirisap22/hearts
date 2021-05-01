package components.game;

import javafx.event.EventType;
import javafx.scene.control.Label;

public class StartTurnEvent extends GameEvent {
  public static final EventType<GameEvent> START_TURN = new EventType<>(GAME_EVENT, "StartTurnEvent");


  public StartTurnEvent() {
    super(START_TURN);
  }

  @Override
  public void invokeHandler(GameEventHandler handler) {
    handler.onStartTurn();
  }
}
