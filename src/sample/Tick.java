package sample;

import java.util.TimerTask;

public class Tick extends TimerTask {
    Controller controller;

    public Tick(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.nextSecond();
    }
}
