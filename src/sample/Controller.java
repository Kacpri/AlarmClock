package sample;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.Point;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.time.LocalTime;
import java.util.Timer;
import java.util.Vector;

import static java.lang.Math.*;

public class Controller {
    private Vector<Double> mousePosition;
    private Point center;
    private Timer clock;
    private Timer alarmHammer;
    private boolean isRinging;
    private short hammerDirection;

    @FXML
    ImageView hourHand;

    @FXML
    ImageView minuteHand;

    @FXML
    ImageView secondHand;

    @FXML
    ImageView alarmHand;

    @FXML
    ImageView hammer;

    public void initialize() {
        alarmHammer = new Timer();
        alarmHammer.cancel();
        hammerDirection = 2;
        center = new Point(1120 / 2, 700 / 2);
        mousePosition = new Vector<>();
        mousePosition.add(0.0);
        mousePosition.add(-50.0);
        isRinging = false;
        setCurrentTime();
        startTimer(3000);
        alarmHand.setRotate(0.0);
    }

    private void startTimer(int delay) {
        clock = new Timer();
        clock.schedule(new Tick(this), delay, 1000);
    }

    void stopTimer() {
        clock.cancel();
    }

    private void startHammerTimer() {
        alarmHammer = new Timer();
        alarmHammer.schedule(new Ding(this), 0, 5);
    }

    void stopHammerTimer() {
        alarmHammer.cancel();
        hammer.setRotate(0);
    }

    void rotateHammer() {
        if (hammer.getRotate() > 10 || hammer.getRotate() < -10)
            hammerDirection *= -1;
        hammer.setRotate(hammer.getRotate() + hammerDirection);
    }

    void nextSecond() {
        secondHand.setRotate((secondHand.getRotate() + 6) % 360);
        moveClock(0.1);
        Sounds.tick();
    }

    private void moveSecond(double angle) {
        secondHand.setRotate((secondHand.getRotate() + angle) % 360);
    }

    private void moveClock(double angle) {
        minuteHand.setRotate((minuteHand.getRotate() + angle) % 360);
        hourHand.setRotate((hourHand.getRotate() + angle / 12) % 360);
        if (abs(hourHand.getRotate() % 360 - alarmHand.getRotate() % 360) <= 0.25 && (int) secondHand.getRotate() == 0 && !isRinging) {
            isRinging = true;
            Sounds.alarm();
            startHammerTimer();
        }
    }

    private void moveAlarm(double angle) {
        alarmHand.setRotate((alarmHand.getRotate() + rint(angle)) % 360);
    }

    public void handleStopAlarm() {
        if (isRinging) {
            Sounds.stopAlarm();
            stopHammerTimer();
            isRinging = false;
        }
    }

    public void handleSetMinute(MouseEvent mouseEvent) {
        setClock(mouseEvent, Hand.MINUTEHAND);
    }

    public void handleSetHour(MouseEvent mouseEvent) {
        setClock(mouseEvent, Hand.HOURHAND);
    }

    public void handleSetAlarm(MouseEvent mouseEvent) {
        setClock(mouseEvent, Hand.ALARMHAND);
    }

    public void handleSetSecond(MouseEvent mouseEvent) {
        setClock(mouseEvent, Hand.SECONDHAND);
    }

    public void handleSetMousePosition(MouseEvent mouseEvent) {
        stopTimer();
        mousePosition.set(0, mouseEvent.getSceneX() - center.getX());
        mousePosition.set(1, mouseEvent.getSceneY() - center.getY());
    }
    public void handleFixSecondHand(MouseEvent mouseEvent){
        secondHand.setRotate((int)secondHand.getRotate() / 6 * 6);
        startTimer(1000);
    }

    public void handleRestart(MouseEvent mouseEvent){
        startTimer(1000);
    }

    private void setClock(MouseEvent mouseEvent, Hand hand) {

        Vector<Double> currentPos = new Vector<>();
        currentPos.add(mouseEvent.getSceneX() - center.getX());
        currentPos.add(mouseEvent.getSceneY() - center.getY());
        double angle = angleBetweenVectors(mousePosition, currentPos);
        switch (hand) {
            case MINUTEHAND:
                moveClock(angle);
                break;
            case HOURHAND:
                moveClock(angle * 12);
                break;
            case ALARMHAND:
                moveAlarm(angle);
                break;
            case SECONDHAND:
                moveSecond(angle);
                break;
        }
        mousePosition.set(0, mouseEvent.getSceneX() - center.getX());
        mousePosition.set(1, mouseEvent.getSceneY() - center.getY());

    }

    private double angleBetweenVectors(Vector<Double> a, Vector<Double> b) {
        double divider = (sqrt(pow(a.get(0), 2) + pow(a.get(1), 2)) * sqrt(pow(b.get(0), 2) + pow(b.get(1), 2)));
        if (divider == 0.0)
            return 0.0;

        double cos = (a.get(0) * b.get(0) + a.get(1) * b.get(1)) / divider;
        double degrees = Math.toDegrees(Math.acos(cos));

        if ((b.get(0) >= 0 && b.get(1) > 0 && (-a.get(0) + a.get(1) > -b.get(0) + b.get(1))) || (b.get(0) > 0 && b.get(1) <= 0 && (a.get(0) + a.get(1) > b.get(0) + b.get(1))) || (b.get(0) <= 0 && b.get(1) < 0 && (a.get(0) + -a.get(1) > b.get(0) + -b.get(1))) || (b.get(0) < 0 && b.get(1) >= 0 && (-a.get(0) + -a.get(1) > -b.get(0) + -b.get(1))))
            degrees *= -1;
        return degrees;
    }

    private void setCurrentTime() {
        LocalTime time = LocalTime.now();
        hourHand.setRotate(time.getHour() * 30.0 + time.getMinute() * 0.5 + time.getSecond() * (0.5 / 60));
        minuteHand.setRotate(time.getMinute() * 6.0 + time.getSecond() * 0.1);
        secondHand.setRotate(time.getSecond() * 6.0);
    }

    private enum Hand {
        MINUTEHAND,
        HOURHAND,
        ALARMHAND,
        SECONDHAND
    }
}
