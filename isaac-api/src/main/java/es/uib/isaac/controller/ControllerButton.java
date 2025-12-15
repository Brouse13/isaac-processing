package es.uib.isaac.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ControllerButton {
    RT("Z Axis"), CROSS("Hat Switch"),
    RB("Button 4"), LB("Button 5"),
    MOVE_Y("Y Axis"), MOVE_X("X Axis"),
    ORI_UP("Y Rotation"), ORI_LEFT("X Rotation"),
    X_BUTTON("Button 2"), Y_BUTTON("Button 3"),
    B_BUTTON("Button 1"), A_BUTTON("Button 0");

    private final String buttonName;
}
