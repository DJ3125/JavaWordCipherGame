package com.example.wordciphergame_11_27_23;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

public class ShopSceneController {
    @FXML private Button goBackToBaseCampButton;
    @FXML private ScrollPane itemOptionsScrollBox;
    @FXML private VBox itemsVBox;
    @FXML private Canvas statsCanvas;

    public void initializeShopScene(PlayerInformation playerInformation){
        this.playerInformation = playerInformation;
        this.itemsVBox.setSpacing(10);
        this.itemsVBox.setPrefWidth(this.itemOptionsScrollBox.getPrefWidth() - 10);
        this.statsAnimationLoop = new StatsAnimationLoop(this.statsCanvas, this.playerInformation);
        this.statsAnimationLoop.start();
        this.updateShopScene();
    }

    private void updateShopScene(){
        this.itemsVBox.getChildren().clear();
        byte interval = 0;
        AllItemReferences[] allItemReferences = AllItemReferences.values();
        while(interval < allItemReferences.length){
            byte counter = 0;
            HBox row = new HBox();
            row.setSpacing(10);
            row.setAlignment(Pos.CENTER);
            while(counter < this.amountOfItemsPerRow && interval < allItemReferences.length){
                row.getChildren().add(this.createVBoxForItem(allItemReferences[interval]));
                counter++;
                interval++;
            }
            this.itemsVBox.getChildren().add(row);
        }
    }

    private VBox createVBoxForItem(AllItemReferences itemReferences){
        VBox itemVBox = new VBox();
        Label title = new Label(itemReferences.getItemReference().getName());
        title.setWrapText(true);
        itemVBox.setPrefHeight(200);
        title.setTextAlignment(TextAlignment.CENTER);
        itemVBox.setPrefWidth(this.itemsVBox.getPrefWidth()/this.amountOfItemsPerRow);
        itemVBox.getChildren().add(title);
        itemVBox.setPadding(new Insets(5));
        itemVBox.setAlignment(Pos.CENTER);
        itemVBox.setBorder(Border.stroke(Paint.valueOf("black")));
        itemVBox.setSpacing(10);
        itemVBox.setMaxWidth(this.itemOptionsScrollBox.getPrefWidth()/3);
        Label info = new Label(itemReferences.getItemReference().getInfo());
        info.setWrapText(true);
        info.setTextAlignment(TextAlignment.CENTER);
        itemVBox.getChildren().add(info);
        itemVBox.getChildren().add(new Label("Costs $" + itemReferences.getItemReference().getCost()));
        Label quantity = new Label("You Have " + this.playerInformation.getAmountOfItem(itemReferences));
        quantity.setWrapText(true);
        itemVBox.getChildren().add(quantity);
        Button buyButton = new Button("Buy Item");
        itemVBox.getChildren().add(buyButton);
        buyButton.setDisable(!this.playerInformation.checkIfCanBuyItem(itemReferences));
        buyButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
            this.playerInformation.buyItem(itemReferences, (short)1);
            this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation((short)(-itemReferences.getItemReference().getCost()), this.statsCanvas, 'M'));
            this.updateShopScene();
        });
        return itemVBox;
    }


    @FXML public void handleGoBackToBaseCamp(){
        this.statsAnimationLoop.interrupt();
        HelloApplication.setCurrentScene(Scenes.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(Scenes.BASE_CAMP_SCENE.getFXMLLoader().getController())).initializeBaseCamp(this.playerInformation);
    }

    private PlayerInformation playerInformation;
    private final byte amountOfItemsPerRow = 3;
    private StatsAnimationLoop statsAnimationLoop;
}
