package dev.webfx.demo.ledclock;

import dev.webfx.extras.led.Led;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.stream.IntStream;

/**
 * @author Bruno Salmon
 */
final class LedMatrixPane extends Pane {

    private final Led[] leds = IntStream.range(0, 6 * 8 * 15).mapToObj(i -> LedMatrixPane.createLed()).toArray(Led[]::new);

    Paint hourGradient, hourOffGradient, minuteGradient, minuteOffGradient, secondGradient, secondOffGradient;
    int[][] hl, hr, mr, ml, sl, sr;
    double digitWidth, digitSpacer, dotSize, spacer;

    {
        getChildren().setAll(leds);
    }

    private static Led createLed() {
        Led led = Led.create(Color.RED);
        led.setShowBorder(false);
        led.setShowReflectionWhenNotHighlighted(true);
        led.setManaged(false);
        return led;
    }

/*
    @Override
    protected void layoutChildren() {
        double width = getWidth(), height = getHeight();
        canvas.setWidth(width);
        canvas.setHeight(height);
        layoutInArea(canvas, 0, 0, width, height, 0, HPos.LEFT, VPos.TOP);
    }
*/

    void drawTime() {
        // draw hours
        drawMatrix(0, 0, 0, hl, hourGradient, hourOffGradient);
        drawMatrix(1, digitWidth + digitSpacer, 0, hr, hourGradient, hourOffGradient);

        // draw colon

        // draw minutes
        drawMatrix(2, 2 * digitWidth + 3 * digitSpacer, 0, ml, minuteGradient, minuteOffGradient);
        drawMatrix(3, 3 * digitWidth + 4 * digitSpacer, 0, mr, minuteGradient, minuteOffGradient);

        // draw colon

        // draw seconds
        drawMatrix(4, 4 * digitWidth + 6 * digitSpacer, 0, sl, secondGradient, secondOffGradient);
        drawMatrix(5, 5 * digitWidth + 7 * digitSpacer, 0, sr, secondGradient, secondOffGradient);
    }

    private void drawMatrix(int matrixIndex, double X, double Y, int[][] MATRIX, Paint ON_PAINT, Paint OFF_PAINT) {
        double  x, y = Y;
        boolean fill;

        if (dotSize == 0 || ON_PAINT == null || OFF_PAINT == null)
            return;

        Color onColor = ((Color) ON_PAINT).brighter(), offColor = onColor.darker().darker().darker().darker().darker().darker();

        for (int row = 0 ; row < 15 ; row++) {
            x = X;
            for (int col = 0; col < 8; col++) {
                fill = MATRIX[row][col] == 1;
                Led led = leds[matrixIndex * 15 * 8 + row * 8 + col];
                led.resizeRelocate(x, y, dotSize, dotSize);
                Color ledColor = fill ? onColor : offColor;
                led.setColor(ledColor);
                led.setHighlighted(fill);
                x = X + ((col + 1) * (dotSize + spacer));
            }
            y = Y + ((row + 1) * (dotSize + spacer));
        }
    }

}
