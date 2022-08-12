package dev.webfx.demo.ledclock;

import eu.hansolo.medusa.Clock;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * @author Bruno Salmon
 */
public final class LedClockApplication extends Application {

    private final BorderPane borderPane = new BorderPane();
    private Clock clock;

    @Override
    public void start(Stage stage) {
        borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        stage.setTitle("Led clock");
        stage.setScene(new Scene(borderPane, 600, 600));
        stage.show();

        createMorphingClock();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                clock.setTimeMs(System.currentTimeMillis());
            }
        }.start();
    }

    private void createClock() {
        clock = new Clock();
        clock.setSecondsVisible(true);
        clock.setSecondColor(Color.RED);
        BorderPane.setMargin(clock, new Insets(10));
        borderPane.setCenter(clock);
    }

    private void createMorphingClock() {
        createClock();
        clock.setMinuteColor(Color.BLUE);
        clock.setHourColor(Color.GREEN);
        Skin<Clock> skin = new MorphingClockSkin(clock);
        clock.setSkin(skin);
    }
}
