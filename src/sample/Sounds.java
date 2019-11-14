package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sounds {
    static Media tick, alarmSound, alarmEnd;
    static MediaPlayer tickPlayer, alarmPlayer, alarmEndPlayer;

    static {
        tick = new Media(new File("src\\sample\\sounds\\clock.wav").toURI().toString());
        alarmSound = new Media(new File("src\\sample\\sounds\\alarmSound.wav").toURI().toString());
        alarmEnd = new Media(new File("src\\sample\\sounds\\alarmEnd.wav").toURI().toString());
        tickPlayer = new MediaPlayer(tick);
        tickPlayer.setVolume(0.3);
        alarmPlayer = new MediaPlayer((alarmSound));
        alarmEndPlayer = new MediaPlayer((alarmEnd));
        alarmPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                alarmPlayer.seek(Duration.ZERO);
            }
        });
    }
    public static void tick () {
        tickPlayer.stop();
        tickPlayer.play();
    }
    public static void alarm(){
        alarmPlayer.stop();
        alarmPlayer.play();
    }
    public static void stopAlarm(){
        alarmEndPlayer.stop();
        alarmPlayer.stop();
        alarmEndPlayer.play();
    }
}
