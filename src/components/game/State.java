package components.game;

public enum State {
   // states
    // inital 
    // give cards
    // play
    // end
    INIT, GIVE, PLAY, END;
    public String to_string(){
        switch(this){
            case INIT:
                return "InitalState";
            case GIVE:
                return "GiveCardsState";
            case PLAY:
                return "PlayState";
            case END:
                return "endState";
            default:
                return "NEVER_REACH";
        }
    }
}
