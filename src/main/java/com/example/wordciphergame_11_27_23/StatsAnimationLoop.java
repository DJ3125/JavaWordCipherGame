package com.example.wordciphergame_11_27_23;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class StatsAnimationLoop extends Thread{
    @Override public void run(){
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();
        short fps = 25;
        this.drawStats();
        while (!this.isInterrupted()){
            while(!this.drawInfo.isEmpty() && !this.isInterrupted()){
                ctx.setFill(Paint.valueOf("black"));
                ctx.setGlobalAlpha(1);
                this.drawStats();
                short interval = 0;
                while(interval < this.drawInfo.size()){
                    if(this.drawInfo.get(interval).stillAlive(this.canvas)){
                        this.drawInfo.get(interval).drawInfo(this.canvas);
                        interval++;
                    }else{this.drawInfo.remove(interval);}
                }
                try{Thread.sleep(1000/fps);}
                catch (InterruptedException error){this.interrupt();}
            }
            while(this.drawInfo.isEmpty() && !this.isInterrupted()){Thread.onSpinWait();}
        }
    }

    private void drawStats(){
        GraphicsContext ctx = this.canvas.getGraphicsContext2D();
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        ctx.fillText("Money: " + this.playerInformation.getMoney(), this.canvas.getWidth()/4.0, this.canvas.getHeight()/2.0);
        ctx.fillText("Infamy: " + this.playerInformation.getInfamy(), 3 * this.canvas.getWidth()/4.0, this.canvas.getHeight()/2.0);
    }

    public ArrayList<StatsDrawInformation> getDrawInfo() {return this.drawInfo;}
    
    public StatsAnimationLoop(Canvas canvasToDrawOn, PlayerInformation playerInformation){
        this.canvas = canvasToDrawOn;
        this.drawInfo = new ArrayList<>();
        this.playerInformation = playerInformation;
    }

    private final ArrayList<StatsDrawInformation> drawInfo;
    private final Canvas canvas;
    private final PlayerInformation playerInformation;
}
