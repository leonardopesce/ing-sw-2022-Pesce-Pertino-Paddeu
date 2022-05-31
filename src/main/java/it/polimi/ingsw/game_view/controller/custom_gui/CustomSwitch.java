package it.polimi.ingsw.game_view.controller.custom_gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * A custom Switch button for graphic porpoise
 */
public class CustomSwitch extends StackPane {
    private final Rectangle back = new Rectangle(30, 10, Color.RED);
    private final Button button = new Button();
    private final String buttonStyleOff = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: WHITE;";
    private final String buttonStyleOn = "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 0.2, 0.0, 0.0, 2); -fx-background-color: #9a365b;";
    private boolean state;

    /**
     * Init for the custom switch button
     */
    private void init() {
        state = false;
        getChildren().addAll(back, button);
        setMinSize(30, 15);
        back.maxWidth(30);
        back.minWidth(30);
        back.maxHeight(10);
        back.minHeight(10);
        back.setArcHeight(back.getHeight());
        back.setArcWidth(back.getHeight());
        back.setFill(Color.valueOf("#ced5da"));
        double r = 2.0;
        button.setShape(new Circle(r));
        setAlignment(button, Pos.CENTER_LEFT);
        button.setMaxSize(15, 15);
        button.setMinSize(15, 15);
        button.setStyle(buttonStyleOff);
    }

    /**
     * Constructor for the switch button
     */
    public CustomSwitch() {
        init();
        button.setFocusTraversable(true);
        button.setMouseTransparent(true);
    }

    /**
     * Function to change the state of the switch
     */
    public void changeState() {
        if (state) {
            button.setStyle(buttonStyleOff);
            back.setFill(Color.valueOf("#ced5da"));
            setAlignment(button, Pos.CENTER_LEFT);
            state = false;
        } else {
            button.setStyle(buttonStyleOn);
            back.setFill(Color.valueOf("#ca688c"));
            setAlignment(button, Pos.CENTER_RIGHT);
            state = true;
        }
    }

    /**
     * Function to get the current state of the switch
     * @return a boolean base on the switch state
     */
    public boolean getState() {
        return state;
    }
}