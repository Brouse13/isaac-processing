package es.uib.isaac.controller;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class ControllerHandler {

    public static final int POLL_RATE = 50;
    public static final float DEADZONE = 0.05f;

    private final Controller controller;
    private final ScheduledExecutorService threadPool;
    private final Map<ControllerButton, Float> events = new HashMap<>();
    private final Set<String> JOYSTICKS = Set.of("X Axis", "Y Axis", "Z Axis", "X Rotation", "Y Rotation");
    private final Consumer<ControllerEvent> buttonPressedEventHandler, buttonReleaseEventHandler;

    public ControllerHandler(Controller controller, Consumer<ControllerEvent> buttonPressedEventHandler,
                             Consumer<ControllerEvent> buttonReleaseEventHandler) {
        this.controller = controller;
        this.buttonPressedEventHandler = buttonPressedEventHandler;
        this.buttonReleaseEventHandler = buttonReleaseEventHandler;

        // Initialize the thread
        threadPool = Executors.newSingleThreadScheduledExecutor();
        threadPool.scheduleAtFixedRate(this::run, 100, POLL_RATE, TimeUnit.MILLISECONDS);

        // Put all default values to 0
        for (ControllerButton type : ControllerButton.values()) events.put(type, 0f);
    }

    void stop() {
        threadPool.shutdown();
    }

    public void run() {
        if (controller == null) return;

        controller.poll();

        Event event = new Event();
        EventQueue queue = controller.getEventQueue();

        while (queue.getNextEvent(event)) {
            Component comp = event.getComponent();
            ControllerButton button = getButton(comp.getName());

            if (button == null) continue;

            float value = event.getValue();
            boolean isJoystick = JOYSTICKS.contains(button.getButtonName());
            float absVal = Math.abs(value);

            float lastValue = events.get(button);

            if (isJoystick) {
                if (absVal < DEADZONE) {
                    // Ensure we send a release event when joystick returns to center
                    if (lastValue != 0f) {
                        events.put(button, 0f);
                        buttonReleaseEventHandler.accept(new ControllerEvent(button, 0f));
                    }
                    continue;
                }
            }

            // Press
            if (lastValue == 0f && value != 0f) {
                buttonPressedEventHandler.accept(new ControllerEvent(button, value));
            }

            // Release
            if (lastValue != 0f && value == 0f) {
                buttonReleaseEventHandler.accept(new ControllerEvent(button, value));
            }

            events.put(button, value);
        }
    }

    private ControllerButton getButton(String name) {
        for (ControllerButton type : ControllerButton.values()) if (type.getButtonName().equals(name)) return type;
        return null;
    }
}
