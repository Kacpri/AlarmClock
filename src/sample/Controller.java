package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.awt.Point;
import java.time.LocalTime;
import java.util.Timer;
import java.util.Vector;

import static java.lang.Math.*;

public class Controller {
    private Vector<Double> mousePos;
    private Point center;
    private Timer clock;
    private Timer alarmHammer;
    private boolean setupMode;
    private boolean isRinging;
    private int hammerDirection;

    @FXML
    ImageView hour;

    @FXML
    ImageView minute;

    @FXML
    ImageView second;

    @FXML
    ImageView alarm;

    @FXML
    Button button;

    @FXML
    ImageView hammer;

    public void initialize() {
        alarmHammer = new Timer();
        alarmHammer.cancel();
        hammerDirection = 2;
        setupMode = false;
        center = new Point(480, 300);
        mousePos = new Vector<>();
        mousePos.add(0.0);
        mousePos.add(-50.0);
        setupMode = false;
        isRinging = false;
        button.setOnMouseClicked((e) -> {
            if (setupMode) {
                setupMode = false;
                startTimer();
            } else {
                setupMode = true;
                stopTimer();
            }
        });
        setCurrentTime();
        startTimer();
    }

    public void startTimer() {
        clock = new Timer();
        clock.schedule(new Tick(this), 1000, 100);
    }

    public void stopTimer() {
        clock.cancel();
    }

    public void startHammerTimer() {
        alarmHammer = new Timer();
        alarmHammer.schedule(new Ding(this), 0, 2);
    }

    public void stopHammerTimer() {
        alarmHammer.cancel();
        hammer.setRotate(0);
    }

    public void rotateHammer() {
        if(hammer.getRotate() > 10 || hammer.getRotate() < -10)
            hammerDirection *=-1;
        hammer.setRotate(hammer.getRotate()+hammerDirection);
    }

    public void nextSecond() {
        second.setRotate((second.getRotate() + 6) % 360);
        moveClock(0.1);
        Sounds.tick();
    }

    public void moveClock(double angle) {
        minute.setRotate((minute.getRotate() + angle) % 360);
        hour.setRotate((hour.getRotate() + angle / 12) % 360);
        if (abs(fixAngle(hour.getRotate()) - fixAngle(alarm.getRotate())) <= 0.25 && second.getRotate() < 6) {
            isRinging = true;
            Sounds.alarm();
            startHammerTimer();
        }
    }

    public double fixAngle(double angle)
    {
        if(angle < 0)
            return 360+angle;
        return angle;
    }

    private void moveAlarm(double angle)
    {
        alarm.setRotate((alarm.getRotate() + rint(angle)) % 360);
    }

    public void handleStopAlarm() {
        if (isRinging) {
            Sounds.stopAlarm();
            isRinging = false;
            stopHammerTimer();
        }
    }

    public void handleSetClock(MouseEvent mouseEvent) {
        if (setupMode && mouseEvent.isShiftDown()) {
            Vector<Double> currentPos = new Vector<>();
            currentPos.add(mouseEvent.getSceneX() - center.getX());
            currentPos.add(mouseEvent.getSceneY() - center.getY());
            moveClock(angleBetweenVectors(mousePos, currentPos));
        }
        if (setupMode && mouseEvent.isControlDown()) {
            Vector<Double> currentPos = new Vector<>();
            currentPos.add(mouseEvent.getSceneX() - center.getX());
            currentPos.add(mouseEvent.getSceneY() - center.getY());
            moveAlarm(angleBetweenVectors(mousePos, currentPos));
        }
        mousePos.set(0, mouseEvent.getSceneX() - center.getX());
        mousePos.set(1, mouseEvent.getSceneY() - center.getY());
    }

    public double angleBetweenVectors(Vector<Double> a, Vector<Double> b) {
        double divider = (sqrt(pow(a.get(0), 2) + pow(a.get(1), 2)) * sqrt(pow(b.get(0), 2) + pow(b.get(1), 2)));
        if (divider == 0.0)
            return 0.0;

        double cos = (a.get(0) * b.get(0) + a.get(1) * b.get(1)) / divider;
        double degrees = Math.toDegrees(Math.acos(cos));

        if ((b.get(0) >= 0 && b.get(1) > 0 && (-a.get(0) + a.get(1) > -b.get(0) + b.get(1))) || (b.get(0) > 0 && b.get(1) <= 0 && (a.get(0) + a.get(1) > b.get(0) + b.get(1))) || (b.get(0) <= 0 && b.get(1) < 0 && (a.get(0) + -a.get(1) > b.get(0) + -b.get(1))) || (b.get(0) < 0 && b.get(1) >= 0 && (-a.get(0) + -a.get(1) > -b.get(0) + -b.get(1))))
            degrees *= -1;
        //System.out.println(divider + " " +cos + " " + degrees);
        return degrees;

    }

    public void setCurrentTime()
    {
        LocalTime time = LocalTime.now();
        hour.setRotate(time.getHour()*30.0 + time.getMinute()*0.5 + time.getSecond()*(0.5/60));
        minute.setRotate(time.getMinute()*6.0 + time.getSecond()*0.1);
        second.setRotate(time.getSecond()*6.0);
    }
}
