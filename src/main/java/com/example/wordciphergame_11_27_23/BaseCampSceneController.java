package com.example.wordciphergame_11_27_23;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BaseCampSceneController {
    @FXML private Button goToTheShopButton, hackButton;

    @FXML public void handleStartHacking(){
        HelloApplication.setCurrentScene(Scenes.HACKING_SCENE);
        ((HackingSceneController)(Scenes.HACKING_SCENE.getFXMLLoader().getController())).initializeScene(this.playerInformation);
    }

    @FXML public void handleGoToShop(){
        HelloApplication.setCurrentScene(Scenes.SHOP_SCENE);
        ((ShopSceneController)(Scenes.SHOP_SCENE.getFXMLLoader().getController())).initializeShopScene(this.playerInformation);
    }

    public void initializeBaseCamp(PlayerInformation playerInformation){
        this.playerInformation = playerInformation;
        this.initializeButtons();
    }
    public void initializeBaseCamp(){
        this.playerInformation = new PlayerInformation(1000, 0, (short)3);
        this.initializeButtons();
    }

    private void initializeButtons(){
        try{
            this.goToTheShopButton.setFont(Font.loadFont(new FileInputStream("src/main/resources/Font/Play-Bold.ttf"), 20));
            this.hackButton.setFont(Font.loadFont(new FileInputStream("src/main/resources/Font/Play-Bold.ttf"), 20));
            this.goToTheShopButton.setBackground(Background.EMPTY);
            this.hackButton.setBackground(Background.EMPTY);
        }catch (FileNotFoundException error){error.printStackTrace();}
    }

    private PlayerInformation playerInformation;
}
