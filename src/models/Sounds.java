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
    private MediaPlayer soundLoseTurn;
    private MediaPlayer soundGameBG;
    public Sounds () {
    }
	 public void getMusicMenuPlay() {
         URL resource = getClass().getResource("/public/sounds/Music_new_menu_01.mp3");
         this.soundMenu =new MediaPlayer(new Media(resource.toString()));
         this.soundMenu.setOnEndOfMedia(new Runnable() {
  
    public void run() {
         soundMenu.seek(Duration.ZERO);
         }
         });
         this.soundMenu.setVolume(0.05);
         this.soundMenu.play();
            
     }
     public void getMusicMenuStop() {
         this.soundMenu.stop();
     }
     public void getSoundShuffleCardPlay(){
         URL resource = getClass().getResource("/public/sounds/Shuffle_card.mp3");
         this.soundShuffleCard =new MediaPlayer(new Media(resource.toString()));
         this.soundShuffleCard.setVolume(0.05);
         this.soundShuffleCard.play();
     }
     public void getSoundShuffleCardStop(){
        
         this.soundShuffleCard.stop();
     }
     public void getSoundGiveCardPlay(){
         URL resource = getClass().getResource("/public/sounds/Give_cards.mp3");
         this.soundGiveCard =new MediaPlayer(new Media(resource.toString()));
         this.soundGiveCard.setVolume(0.05);
         this.soundGiveCard.play();
     }
     public void getSoundGiveCardStop(){
        
         this.soundGiveCard.stop();
     }
     public void getSoundGameOverPlay(){
         URL resource = getClass().getResource("/public/sounds/game_Over.mp3");
         this.soundGameOver =new MediaPlayer(new Media(resource.toString()));
         this.soundGameOver.setVolume(0.05);
         this.soundGameOver.play();
     }
     public void getSoundGameOverStop(){
        
         this.soundGameOver.stop();
     }
     public void getSoundSmashHeartPlay(){
         URL resource = getClass().getResource("/public/sounds/hearts.mp3");
         this.soundSmashHeart =new MediaPlayer(new Media(resource.toString()));
         this.soundSmashHeart.setVolume(0.05);
         this.soundSmashHeart.play();
    }
     public void getSoundSmashHeartStop(){
        
         this.soundSmashHeart.stop();
     }
     public void getSoundSmashMamSpadesPlay(){
         URL resource = getClass().getResource("/public/sounds/mam_Spades.mp3");
         this.soundSmashMamSpades =new MediaPlayer(new Media(resource.toString()));
         this.soundSmashMamSpades.setVolume(0.05);
         this.soundSmashMamSpades.play();
     }
     public void getSoundSmashMamSpadesStop(){
        
         this.soundSmashMamSpades.stop();
     }
     public void getSoundSwitchCardPlay(){
         URL resource = getClass().getResource("/public/sounds/Switch-card.mp3");
         this.soundSwitchCard =new MediaPlayer(new Media(resource.toString()));
         this.soundSwitchCard.setVolume(0.05);
         this.soundSwitchCard.play();
     }
     public void getSoundSwitchCardStop(){
        
         this.soundSwitchCard.stop();
     }
     public void getSoundMenuClickPlay(){
         URL resource = getClass().getResource("/public/sounds/menu_Click.mp3");
         this.soundMenuClick =new MediaPlayer(new Media(resource.toString()));
         this.soundMenuClick.setVolume(0.05);
         this.soundMenuClick.play();
     }
     public void getSoundMenuClickStop(){
        
         this.soundMenuClick.stop();
     }
     public void getSoundWrongPlay(){
         URL resource = getClass().getResource("/public/sounds/wrong.mp3");
         this.soundWrong =new MediaPlayer(new Media(resource.toString()));
         this.soundWrong.setVolume(0.05);
         this.soundWrong.play();
     }
     public void getSoundWrongStop(){
        
         this.soundWrong.stop();
     }
     public void getSoundLosePlay(){
         URL resource = getClass().getResource("/public/sounds/lose.mp3");
         this.soundLoseTurn =new MediaPlayer(new Media(resource.toString()));
         this.soundLoseTurn.setVolume(0.05);
         this.soundLoseTurn.play();
     }
     public void getSoundLoseStop(){
        
          this.soundLoseTurn.stop();
     }
     public void getMusicGameBGPlay(){
      URL resource = getClass().getResource("/public/sounds/GameBG.mp3");
      this.soundGameBG =new MediaPlayer(new Media(resource.toString()));
      this.soundGameBG.setOnEndOfMedia(new Runnable() {

         public void run() {
      soundGameBG.seek(Duration.ZERO);
      }
      });
      this.soundGameBG.setVolume(0.05);
      this.soundGameBG.play();

  }
  public void getMusicGameBGStop(){

       this.soundGameBG.stop();
  }
}