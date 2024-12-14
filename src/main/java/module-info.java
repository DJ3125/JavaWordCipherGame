module com.example.wordciphergame_11_27_23 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.wordciphergame_11_27_23 to javafx.fxml;
    exports com.example.wordciphergame_11_27_23;
}