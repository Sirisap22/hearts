package models;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class Sounds {
    private MediaPlayer soundShuffleCard;
    private MediaPlayer soundMenu;
    private MediaPlayer soundGiveCard;
    private MediaPlayer soundGameOver;
    private MediaPlayer soundSmashHeart;
    private MediaPlayer soundSmashMamSpades;
    private MediaPlayer soundSwitchCard;
    private MediaPlayer soundMenuClick;
    private MediaPlayer soundWrong;
    private MediaPlayer soundLose;
    public Sounds () {
    }
	// public void getMusicMenuPlay() {
  //       URL resource = getClass().getResource("/public/sound/Music_new_menu_02.mp3");
  //       this.soundMenu =new MediaPlayer(new Media(resource.toString()));
  //       this.soundMenu.setOnEndOfMedia(new Runnable() {
  //       public void run() {
  //       soundMenu.seek(Duration.ZERO);
  //       }
  //       });
  //       this.soundMenu.play();
            
  //   }
  //   public void getMusicMenuStop() {
  //       this.soundMenu.stop();
  //   }
  //   public void getSoundShuffleCardPlay(){
  //       URL resource = getClass().getResource("/public/sound/Shuffle_card.mp4");
  //       this.soundShuffleCard =new MediaPlayer(new Media(resource.toString()));
  //       this.soundShuffleCard.play();
  //   }
  //   public void getSoundShuffleCardStop(){
        
  //       this.soundShuffleCard.stop();
  //   }
  //   public void getSoundGiveCardPlay(){
  //       URL resource = getClass().getResource("/public/sound/Give_cards.mp4");
  //       this.soundGiveCard =new MediaPlayer(new Media(resource.toString()));
  //       this.soundGiveCard.play();
  //   }
  //   public void getSoundGiveCardStop(){
        
  //       this.soundGiveCard.stop();
  //   }
  //   public void getSoundGameOverPlay(){
  //       URL resource = getClass().getResource("/public/sound/game_Over.mp4");
  //       this.soundGameOver =new MediaPlayer(new Media(resource.toString()));
  //       this.soundGameOver.play();
  //   }
  //   public void getSoundGameOverStop(){
        
  //       this.soundGameOver.stop();
  //   }
  //   public void getSoundSmashHeartPlay(){
  //       URL resource = getClass().getResource("/public/sound/hearts.mp4");
  //       this.soundSmashHeart =new MediaPlayer(new Media(resource.toString()));
  //       this.soundSmashHeart.play();
  //   }
  //   public void getSoundSmashHeartStop(){
        
  //       this.soundSmashHeart.stop();
  //   }
  //   public void getSoundSmashMamSpadesPlay(){
  //       URL resource = getClass().getResource("/public/sound/mam_Spades.mp4");
  //       this.soundSmashMamSpades =new MediaPlayer(new Media(resource.toString()));
  //       this.soundSmashMamSpades.play();
  //   }
  //   public void getSoundSmashMamSpadesStop(){
        
  //       this.soundSmashMamSpades.stop();
  //   }
  //   public void getSoundSwitchCardPlay(){
  //       URL resource = getClass().getResource("/public/sound/Switch-card.mp4");
  //       this.soundSwitchCard =new MediaPlayer(new Media(resource.toString()));
  //       this.soundSwitchCard.play();
  //   }
  //   public void getSoundSwitchCardStop(){
        
  //       this.soundSwitchCard.stop();
  //   }
  //   public void getSoundMenuClickPlay(){
  //       URL resource = getClass().getResource("/public/sound/menu_Click.mp4");
  //       this.soundMenuClick =new MediaPlayer(new Media(resource.toString()));
  //       this.soundMenuClick.play();
  //   }
  //   public void getSoundMenuClickStop(){
        
  //       this.soundMenuClick.stop();
  //   }
  //   public void getSoundWrongPlay(){
  //       URL resource = getClass().getResource("/public/sound/wrong.mp4");
  //       this.soundWrong =new MediaPlayer(new Media(resource.toString()));
  //       this.soundWrong.play();
  //   }
  //   public void getSoundWrongStop(){
        
  //       this.soundWrong.stop();
  //   }
  //   public void getSoundLosePlay(){
  //       URL resource = getClass().getResource("/public/sound/lose.mp4");
  //       // this.soundLoseTurn =new MediaPlayer(new Media(resource.toString()));
  //       // this.soundLoseTurn.play();
  //   }
  //   public void getSoundLoseStop(){
        
  //       // this.soundLoseTurn.stop();
  //   }
}
