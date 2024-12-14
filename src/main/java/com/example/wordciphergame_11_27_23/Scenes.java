package com.example.wordciphergame_11_27_23;

import javafx.fxml.FXMLLoader;

public enum Scenes {
    HACKING_SCENE("hacking-scene.fxml"),
    SHOP_SCENE("shop-scene.fxml"),
    BASE_CAMP_SCENE("base-camp-scene.fxml"),
    RESULTS_SCENE("results-scene.fxml"),
    PRIZE_WHEEL_SCENE("prize-wheel.fxml");

    Scenes(String resource){this.fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(resource));}
    public FXMLLoader getFXMLLoader() {return this.fxmlLoader;}
    final private FXMLLoader fxmlLoader;
}
