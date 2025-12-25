package es.uib.isaac;

import es.uib.isaac.entity.BasePlayer;
import es.uib.isaac.entity.Direction;
import es.uib.isaac.entity.Player;
import es.uib.isaac.map.RoomLayout;
import es.uib.isaac.map.RoomShape;
import es.uib.isaac.render.GameRoomRender;
import es.uib.isaac.ui.ListRenderer;
import es.uib.isaac.render.StaticRender;
import processing.core.PApplet;
import processing.core.PImage;

public class IsaacGame extends PApplet {
    public static IsaacGame INSTANCE;
    private Player isaac;
    private boolean moving = false;
    private boolean dirty = true;
    private final ListRenderer listRenderer = new ListRenderer();
    RoomLayout layout = new RoomLayout((byte) 0, RoomShape.SQUARE, 28, 16, null, null);
    GameRoomRender gameRoomRender;
    PImage img;
    float lastTime = 0;
    private boolean wPressed = false;
    private boolean aPressed = false;
    private boolean sPressed = false;
    private boolean dPressed = false;

    @Override
    public void settings() {
        INSTANCE = this;
        size(Constants.WIDTH, Constants.HEIGHT);
    }

    public void pre() {
        if (width != Constants.WIDTH || height != Constants.HEIGHT) {
            Constants.WIDTH = width;
            Constants.HEIGHT = height;
        }
    }

    @Override
    public void setup() {
        surface.setResizable(true);
        registerMethod("pre", this);

        isaac = new BasePlayer(0, 0);
        isaac.initialize();
        isaac.setDirection(Direction.SOUTH);
        img = loadImage("assets/players/isaac.png");

        //listRenderer.addRender("isaac-lives", new LiveRender(isaac.getLiveContainers()));

        gameRoomRender = new GameRoomRender(layout, "assets/rooms/basement-lroom.png");
        gameRoomRender.getEntities().add(isaac);
        gameRoomRender.follow(isaac);
        listRenderer.addRender("map-render", gameRoomRender);

        listRenderer.getRenders().forEach(StaticRender::initialize);
    }

    @Override
    public void draw() {
        float currentTime = millis() / 1000.0f; // time in seconds
        float deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        updateGame(deltaTime);

        if (!dirty) return;

        renderGame();
        dirty = false;
    }

    void renderGame() {
        background(0);
        gameRoomRender.render();
    }

    void updateGame(float deltaTime) {
        if (moving) {
            isaac.update(deltaTime);
            dirty = true;
        }
        gameRoomRender.update();
    }


    public void keyPressed() {
        switch (key) {
            case 'w' -> wPressed = true;
            case 's' -> sPressed = true;
            case 'a' -> aPressed = true;
            case 'd' -> dPressed = true;
        }

        updateDirection();
    }

    public void keyReleased() {
        switch (key) {
            case 'w' -> wPressed = false;
            case 's' -> sPressed = false;
            case 'a' -> aPressed = false;
            case 'd' -> dPressed = false;
        }

        updateDirection();
    }

    private void updateDirection() {
        if (wPressed && aPressed) {
            isaac.setDirection(Direction.NORTH_WEST);
        } else if (wPressed && dPressed) {
            isaac.setDirection(Direction.NORTH_EAST);
        } else if (sPressed && aPressed) {
            isaac.setDirection(Direction.SOUTH_WEST);
        } else if (sPressed && dPressed) {
            isaac.setDirection(Direction.SOUTH_EAST);
        } else if (wPressed) {
            isaac.setDirection(Direction.NORTH);
        } else if (sPressed) {
            isaac.setDirection(Direction.SOUTH);
        } else if (aPressed) {
            isaac.setDirection(Direction.WEST);
        } else if (dPressed) {
            isaac.setDirection(Direction.EAST);
        } else {
            moving = false;
            return; // no keys pressed
        }
        moving = true;
        dirty = true;
    }

}
