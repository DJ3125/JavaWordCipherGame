package com.example.wordciphergame_11_27_23;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ResultsSceneController {
    @FXML private Button proceedButton;
    @FXML private Canvas resultsCanvas;

    public void initializeResultsScene(PlayerInformation playerInformation, short changeInInfamy, short changeInMoney){
        this.playerInformation = playerInformation;
        this.proceedButton.setDisable(true);
        this.statsAnimationLoop = new StatsAnimationLoop(this.resultsCanvas, this.playerInformation);
        try{this.resultsCanvas.getGraphicsContext2D().setFont(Font.loadFont(new FileInputStream("src/main/resources/Font/Play-Bold.ttf"), 30));}catch(FileNotFoundException error){error.printStackTrace();}
        this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation(changeInMoney, this.resultsCanvas, 'M'));
        this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation(changeInInfamy, this.resultsCanvas, 'I'));
        try{Thread.sleep(1000);}catch(InterruptedException error){}
        this.statsAnimationLoop.start();
        this.proceedButton.setDisable(false);
    }

    @FXML public void handleProceed(){
        this.statsAnimationLoop.interrupt();
        HelloApplication.setCurrentScene(Scenes.PRIZE_WHEEL_SCENE);
        ((BaseCampSceneController)(Scenes.BASE_CAMP_SCENE.getFXMLLoader().getController())).initializeBaseCamp(this.playerInformation);
    }

    private StatsAnimationLoop statsAnimationLoop;
    private PlayerInformation playerInformation;
}
