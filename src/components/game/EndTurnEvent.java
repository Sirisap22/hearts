package components.game;

import javafx.event.EventType;

public class EndTurnEvent extends GameEvent {
  public static final EventType<GameEvent> END_TURN = new EventType<>(GAME_EVENT, "EndTurnEvent");

  private final int whoseTurn;

  public EndTurnEvent(int whoseTurn) {
    super(END_TURN);
    this.whoseTurn = whoseTurn;
  }

  @Override
  public void invokeHandler(GameEventHandler handler) {
    handler.onEndTurn(whoseTurn);
  }
}
