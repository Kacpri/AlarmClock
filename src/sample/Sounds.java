package sample;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

class Sounds {
    private static MediaPlayer tick, alarm, alarmEnd;

    static {
        tick = new MediaPlayer(new Media(new File("src\\sample\\sounds\\clock.wav").toURI().toString()));
        alarm = new MediaPlayer(new Media(new File("src\\sample\\sounds\\alarmSound.wav").toURI().toString()));
        alarmEnd = new MediaPlayer(new Media(new File("src\\sample\\sounds\\alarmEnd.wav").toURI().toString()));
        tick.setVolume(0.3);
        alarm.setOnEndOfMedia(new Runnable() {
            public void run() {
                alarm.seek(Duration.ZERO);
            }
        });
    }
    static void tick () {
        tick.stop();
        tick.play();
    }
    static void alarm(){
        alarm.stop();
        alarm.play();
    }
    static void stopAlarm(){
        alarmEnd.stop();
        alarm.stop();
        alarmEnd.play();
    }
}
