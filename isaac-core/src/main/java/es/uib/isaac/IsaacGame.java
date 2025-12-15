package es.uib.isaac;

import es.uib.isaac.controller.ControllerButton;
import es.uib.isaac.controller.ControllerEvent;
import es.uib.isaac.controller.ControllerHandler;
import es.uib.isaac.entity.BasePlayer;
import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Player;
import es.uib.isaac.ui.LiveRender;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import processing.core.PApplet;

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private boolean moving = false;
    private LiveRender liveRender;
    private Controller selectedController;
    private ControllerHandler controllerHandler;

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);
        for (Controller controller : ControllerEnvironment.getDefaultEnvironment().getControllers()) {
            // Find first controller
            if (controller.getType() != Controller.Type.GAMEPAD) continue;

            System.out.println("Selecting controller " + controller.getName());
            selectedController = controller;
            controllerHandler = new ControllerHandler(controller, this::onKeyPressed, this::onKeyReleased);
            break;
        }

        if (selectedController == null) {
            System.out.println("No controller available");
        }
    }

    @Override
    public void setup() {
        isaac = new BasePlayer(100, 100);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);
        liveRender = new LiveRender(isaac.getLiveContainers());
        liveRender.initialize();
    }

    @Override
    public void draw() {

        /*
        if (key == 'w') isaac.setDirection(Direction.NORTH);
        else if (key == 's') isaac.setDirection(Direction.SOUTH);
        else if (key == 'a') isaac.setDirection(Direction.WEST);
        else if (key == 'd') isaac.setDirection(Direction.EAST);
         */

        background(200);
        liveRender.render();
        isaac.display();

        if (moving) isaac.update();
    }

    private void onKeyPressed(ControllerEvent button) {
        if (button.button() == ControllerButton.CROSS) {
            moving = true;
            if (button.value() == 0.25f) isaac.setDirection(Direction.NORTH);
            else if (button.value() == 0.5f) isaac.setDirection(Direction.EAST);
            else if (button.value() == 0.75f) isaac.setDirection(Direction.SOUTH);
            else if (button.value() == 1.0f) isaac.setDirection(Direction.WEST);
        }

        System.out.println("Pressed button " + button.button());
    }

    private void onKeyReleased(ControllerEvent button) {
        moving = false;
        System.out.println("Released button " + button.button());
    }

    public void keyReleased() {
        moving = false;
    }

    public void keyPressed() {
        moving = true;
    }
}
