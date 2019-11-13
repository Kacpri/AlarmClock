package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.awt.Point;
import java.util.Timer;
import java.util.Vector;

import static java.lang.Math.*;

public class Controller {
    private Vector<Double> mousePos;
    private Point center;
    private Timer clock;
    private boolean setupMode;
    private boolean isRinging;

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

    public void initialize() {
        setupMode = false;
        center = new Point(150, 100);
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
    }

    public void startTimer() {
        clock = new Timer();
        clock.schedule(new Tick(this), 0, 1000);
    }

    public void stopTimer() {
        clock.cancel();
    }

    public void nextSecond() {
        second.setRotate((second.getRotate() + 6) % 360);
        moveClock(0.1);
        Sounds.tick();
    }

    public void moveClock(double angle) {
        minute.setRotate((minute.getRotate() + angle) % 360);
        hour.setRotate((hour.getRotate() + angle / 60) % 360);
        if (abs(hour.getRotate() - alarm.getRotate()) < 0.001) {
            isRinging = true;
            Sounds.alarm();
        }
    }

    private void moveAlarm(double angle) {
        alarm.setRotate((alarm.getRotate() + angle) % 360);
    }

    public void handleStopAlarm() {
        if (isRinging) {
            Sounds.stopAlarm();
            isRinging = false;
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
}
