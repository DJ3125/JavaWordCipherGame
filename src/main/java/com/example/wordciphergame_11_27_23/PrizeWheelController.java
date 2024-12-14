package com.example.wordciphergame_11_27_23;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PrizeWheelController {
    @FXML private Canvas prizeWheelCanvas, statsCanvas;
    @FXML private Button skipButton;
    @FXML private Label hacksRemainingLabel;

    public void initializeScene(String[] puzzleAnswersArray, PlayerInformation playerInformation, short changeInMoney, short changeInInfamy){
        this.gameInfo = new PrizeWheelGameInfo(playerInformation, puzzleAnswersArray);
        this.changeInMoney = changeInMoney;
        this.changeInInfamy = changeInInfamy;
        short amountOfRows = 3;
        this.hitboxes = new PrizeWheelPrizes[puzzleAnswersArray.length * amountOfRows];
        short padding = 10;
        short hitboxHeight = (short)((this.prizeWheelCanvas.getHeight() - (amountOfRows + 1) * padding)/amountOfRows);
        short hitboxWidth = (short)((this.prizeWheelCanvas.getWidth() - (puzzleAnswersArray.length + 1) * padding)/puzzleAnswersArray.length);
        byte counter = 0;
        for(byte i = 0; i < puzzleAnswersArray.length; i++){
            for(byte j = 0; j < amountOfRows; j++){
                this.hitboxes[counter] = new PrizeWheelPrizes((short)(padding +hitboxWidth/2 + (hitboxWidth + padding) * i), (short)(padding + hitboxHeight/2 + (hitboxHeight + padding) * j), hitboxWidth, hitboxHeight);
                counter++;
            }
        }
        this.statsAnimationLoop = new StatsAnimationLoop(this.statsCanvas, this.gameInfo.getPlayerInformation());
        this.statsAnimationLoop.start();
        this.updatePrizeWheelScene();
    }

    @FXML public void handlePrizeClicked(MouseEvent mouseEvent){
        byte interval = 0;
        while(interval < this.hitboxes.length && !(this.hitboxes[interval].isNotSelected() && this.hitboxes[interval].isMouseOver((short)mouseEvent.getX(), (short)mouseEvent.getY()))){interval++;}
        if(interval < this.hitboxes.length){
            int moneyBefore = this.gameInfo.getPlayerInformation().getMoney();
            int infamyBefore = this.gameInfo.getPlayerInformation().getInfamy();
            if(this.gameInfo.handlePrizeClicked(this.hitboxes, interval)){this.handleKillPrizeScene();}
            moneyBefore -= this.gameInfo.getPlayerInformation().getMoney();
            infamyBefore -= this.gameInfo.getPlayerInformation().getInfamy();
            this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation((short)(-moneyBefore), this.statsCanvas, 'M'));
            this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation((short)(-infamyBefore), this.statsCanvas, 'I'));
            this.changeInInfamy -= (short)infamyBefore;
            this.changeInMoney -= (short)moneyBefore;
            this.updatePrizeWheelScene();
        }
    }

    private void updatePrizeWheelScene(){
        this.prizeWheelCanvas.getGraphicsContext2D().clearRect(0, 0, this.prizeWheelCanvas.getWidth(), this.prizeWheelCanvas.getHeight());
        for(PrizeWheelPrizes i : this.hitboxes){
            if(i.isNotSelected()){i.drawPrize(this.prizeWheelCanvas);}
            else{i.drawInfo(this.prizeWheelCanvas);}
        }
        this.hacksRemainingLabel.setText("Hacks Remaining: " + this.gameInfo.getPicksRemaining());
    }

    @FXML public void handleKillPrizeScene(){
        this.statsAnimationLoop.interrupt();
        HelloApplication.setCurrentScene(Scenes.RESULTS_SCENE);
        ((ResultsSceneController)(Scenes.RESULTS_SCENE.getFXMLLoader().getController())).initializeResultsScene(this.gameInfo.getPlayerInformation(), this.changeInInfamy, this.changeInMoney);
    }

    private PrizeWheelGameInfo gameInfo;
    private PrizeWheelPrizes[] hitboxes;
    private StatsAnimationLoop statsAnimationLoop;
    private short changeInMoney, changeInInfamy;
}
