package sample;

import java.util.TimerTask;

public class Ding extends TimerTask {
    Controller controller;

    public Ding(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.rotateHammer();
    }
}
