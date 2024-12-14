package com.example.wordciphergame_11_27_23;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class StatsDrawInformation {

    public StatsDrawInformation(short changeInStat, Canvas canvasToDrawOn, char statChanged){
        this.yLoc = (short)(canvasToDrawOn.getHeight()/2);
        this.changeInStat = changeInStat;
        this.statChanged = statChanged;
    }

    public boolean stillAlive(Canvas canvasToDrawOn){return yLoc < canvasToDrawOn.getHeight();}

    public void drawInfo(Canvas canvasToDrawOn){
        canvasToDrawOn.getGraphicsContext2D().setFill(Paint.valueOf((this.changeInStat > 0) ? "green" : "red"));
        canvasToDrawOn.getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
        canvasToDrawOn.getGraphicsContext2D().setGlobalAlpha(1 - ((yLoc - (canvasToDrawOn.getHeight()/2.0)) / (canvasToDrawOn.getHeight()/2.0)));
        canvasToDrawOn.getGraphicsContext2D().fillText("" + this.changeInStat, ((this.statChanged == 'M') ? 1 : 3) * canvasToDrawOn.getWidth()/4.0, yLoc);
        yLoc++;
    }

    private short yLoc;
    private final short changeInStat;
    private final char statChanged;
}
