package components.game;

import javafx.event.EventType;

public class EndTurnEvent extends GameEvent {
  public static final EventType<GameEvent> END_TURN = new EventType<>(GAME_EVENT, "EndTurnEvent");


  public EndTurnEvent() {
    super(END_TURN);
  }

  @Override
  public void invokeHandler(GameEventHandler handler) {
    handler.onEndTurn();
  }
}
