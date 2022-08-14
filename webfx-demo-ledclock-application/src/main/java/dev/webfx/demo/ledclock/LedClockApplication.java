package dev.webfx.demo.ledclock;

import dev.webfx.extras.led.Led;
import dev.webfx.platform.uischeduler.UiScheduler;
import eu.hansolo.medusa.Clock;
import eu.hansolo.medusa.skins.MorphingClockSkin;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

        updateClockTime();
        UiScheduler.schedulePeriodic(1000, this::updateClockTime);
    }

    private void updateClockTime() {
        clock.setTimeMs(System.currentTimeMillis());
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
        Skin skin = new MorphingClockSkin(clock) {
            private final Led[] leds = new Led[2 * 3]; // ON / OFF * HH MM SS
            private int matrixIndex = -1;

            protected void drawMatrix(double X, double Y, int[][] MATRIX, Paint ON_PAINT, Paint OFF_PAINT) {
                double x, y = Y;
                boolean fill;

                if (++matrixIndex >= 6)
                    matrixIndex = 0;

                if (dotSize == 0 || ON_PAINT == null)
                    return;

                int ledIndex = matrixIndex / 2 * 2;
                Led onLed = leds[ledIndex], offLed = leds[ledIndex + 1];

                if (onLed == null) {
                    Color onColor = (Color) ON_PAINT, offColor = onColor;
                    onColor = onColor.brighter().brighter().brighter().brighter();
                    offColor = offColor.darker().darker().darker().darker().darker().darker();
                    onLed = leds[ledIndex] = createLed(onColor, true);
                    offLed = leds[ledIndex + 1] = createLed(offColor, false);
                }

                for (int row = 0; row < 15; row++) {
                    x = X;
                    for (int col = 0; col < 8; col++) {
                        fill = MATRIX[row][col] == 1;
                        Led led = fill ? onLed : offLed;
                        led.drawInCanvas(ctx, x, y, dotSize);
                        x = X + ((col + 1) * (dotSize + spacer));
                    }
                    y = Y + ((row + 1) * (dotSize + spacer));
                }
            }

        };
        clock.setSkin(skin);
    }

    private static Led createLed(Color ledColor, boolean highlighted) {
        Led led = Led.create(ledColor);
        led.setHighlighted(highlighted);
        led.setShowBorder(false);
        led.setShowReflectionWhenNotHighlighted(false);
        return led;
    }

}
