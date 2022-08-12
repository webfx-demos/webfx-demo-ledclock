package dev.webfx.demo.ledclock;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public class CanvasMatrixPane extends Pane {

    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();

    Paint hourGradient, hourOffGradient, minuteGradient, minuteOffGradient, secondGradient, secondOffGradient;
    int[][] hl, hr, mr, ml, sl, sr;
    double digitWidth, digitSpacer, dotSize, spacer;

    {
        getChildren().setAll(canvas);
        Platform.runLater(() -> {
            ctx.setLineWidth(1);
            ctx.setStroke(null);
        });
    }

    @Override
    protected void layoutChildren() {
        double width = getWidth(), height = getHeight();
        canvas.setWidth(width);
        canvas.setHeight(height);
        layoutInArea(canvas, 0, 0, width, height, 0, HPos.LEFT, VPos.TOP);
    }

    void drawTime() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // draw hours
        drawMatrix(0, 0, hl, hourGradient, hourOffGradient);
        drawMatrix(digitWidth + digitSpacer, 0, hr, hourGradient, hourOffGradient);

        // draw colon

        // draw minutes
        drawMatrix(2 * digitWidth + 3 * digitSpacer, 0, ml, minuteGradient, minuteOffGradient);
        drawMatrix(3 * digitWidth + 4 * digitSpacer, 0, mr, minuteGradient, minuteOffGradient);

        // draw colon

        // draw seconds
        drawMatrix(4 * digitWidth + 6 * digitSpacer, 0, sl, secondGradient, secondOffGradient);
        drawMatrix(5 * digitWidth + 7 * digitSpacer, 0, sr, secondGradient, secondOffGradient);
    }

    private void drawMatrix(double X, double Y, int[][] MATRIX, Paint ON_PAINT, Paint OFF_PAINT) {
        double  x;
        double  y = Y;
        boolean fill;

        for (int row = 0 ; row < 15 ; row++) {
            x = X;
            for (int col = 0; col < 8; col++) {
                fill = MATRIX[row][col] == 1;
                ctx.setFill(fill ? ON_PAINT : OFF_PAINT);
                ctx.fillOval(x, y, dotSize, dotSize);
                x = X + ((col + 1) * (dotSize + spacer));
            }
            y = Y + ((row + 1) * (dotSize + spacer));
        }
    }

}

