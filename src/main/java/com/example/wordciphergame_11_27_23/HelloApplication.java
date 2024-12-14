package com.example.wordciphergame_11_27_23;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class HelloApplication extends Application {
    @Override public void start(Stage stage){
        GAME_STAGE = stage;
        refineTopics();
        HelloApplication.setCurrentScene(Scenes.BASE_CAMP_SCENE);
        ((BaseCampSceneController)(Scenes.BASE_CAMP_SCENE.getFXMLLoader().getController())).initializeBaseCamp();
        GAME_STAGE.setTitle("Dylan's Word Guessing Bonanza!");
        GAME_STAGE.show();
    }

    private static void refineTopics(){
        try{
            for(File i : (new File("src/main/resources/RawTopics")).listFiles()){
                Scanner scanner = new Scanner(i);
                PrintWriter printWriter = new PrintWriter("src/main/resources/RefinedTopics/" + i.getName());
                while(scanner.hasNextLine()){printWriter.println(scanner.nextLine());}
                scanner.close();
                printWriter.close();
            }
        }catch(NullPointerException | FileNotFoundException error){error.printStackTrace();}
    }

    public static void setCurrentScene(Scenes sceneToInitialize){
        for(Scenes i : Scenes.values()){
            i.getFXMLLoader().setRoot(null);
            i.getFXMLLoader().setController(null);
        }
        try{GAME_STAGE.setScene(new Scene(sceneToInitialize.getFXMLLoader().load()));}
        catch (IOException error){error.printStackTrace();}
        System.gc();
    }

    public static void main(String[] args) {launch();}

    private static Stage GAME_STAGE;
}