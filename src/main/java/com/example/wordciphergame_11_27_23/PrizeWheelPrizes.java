package com.example.wordciphergame_11_27_23;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class PrizeWheelPrizes {
    public boolean isNotSelected(){return !this.selected;}
    public void setSelected(){this.selected = true;}

    public PrizeWheelPrizes(short xPos, short yPos, short width, short height){
        this.point = new Point2D(xPos, yPos);
        this.width = width;
        this.height = height;
        this.selected = false;
        this.prizeSelected = PossiblePrizes.randomlyGeneratePrize();
    }

    public boolean isMouseOver(short mouseX, short mouseY){return Math.abs(this.point.getX() - mouseX) < width/2.0 && Math.abs(this.point.getY() - mouseY) < height/2.0;}

    public void grantPrize(PlayerInformation playerInformation){
        this.prizeSelected.setNewInfamyBalance(playerInformation);
        this.prizeSelected.setNewMonetaryBalance(playerInformation);
    }

    public void drawPrize(Canvas canvasToDrawOn){
        GraphicsContext ctx = canvasToDrawOn.getGraphicsContext2D();
        ctx.setFill(Paint.valueOf("black"));
        ctx.fillRect(this.point.getX() - this.width/2.0, this.point.getY() - this.height/2.0, this.width, this.height);
//        ctx.drawImage(new Image(""), this.point.getX(), this.point.getY(), this.width, this.height);
    }

    public void drawInfo(Canvas canvasToDraw){
        canvasToDraw.getGraphicsContext2D().setTextAlign(TextAlignment.CENTER);
        canvasToDraw.getGraphicsContext2D().fillText(this.prizeSelected.getDetails(), this.point.getX(), this.point.getY(), this.width);
    }

    final private Point2D point;
    final private short width, height;
    private boolean selected;
    final private PossiblePrizes prizeSelected;
}
