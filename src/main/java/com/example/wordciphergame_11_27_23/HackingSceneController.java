package com.example.wordciphergame_11_27_23;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class HackingSceneController{
    @FXML private Canvas hackingCanvas, puzzlesRightCanvas, statsCanvas, timeRemainingCanvas;
    @FXML private HBox parentNode;
    @FXML private ScrollPane itemsToUseScrollPane;
    @FXML private VBox itemsToUseVBox;
    @FXML private Button submitButton;
    private GraphicsContext hackingCanvasCTX, puzzlesRightCanvasCTX, statsCanvasCTX, timeRemainingCanvasCTX;
    private short letterWidth, letterHeight, letterGap, letterGapHeight;


    private void changePlayerInfoValues(int changeInMoney, int changeInfamy){
        this.playerInformation.addMoney(changeInMoney);
        this.playerInformation.addInfamy(changeInfamy);
        this.changeInMoney += changeInMoney;
        this.changeInInfamy += changeInfamy;
        this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation((short)changeInMoney, this.statsCanvas, 'M'));
        this.statsAnimationLoop.getDrawInfo().add(new StatsDrawInformation((short)changeInfamy, this.statsCanvas, 'I'));
    }


    public void initializeScene(PlayerInformation playerInformation){
        this.hackingCanvasCTX = this.hackingCanvas.getGraphicsContext2D();
        this.puzzlesRightCanvasCTX = this.puzzlesRightCanvas.getGraphicsContext2D();
        this.statsCanvasCTX = this.statsCanvas.getGraphicsContext2D();
        this.timeRemainingCanvasCTX = this.timeRemainingCanvas.getGraphicsContext2D();
        this.parentNode.addEventFilter(KeyEvent.KEY_PRESSED, this::handleLetterTyped);//needs this so you can backspace. Must be a filter, not a handler
        this.parentNode.requestFocus(); //Needs this otherwise typing won't work
        this.hackingCanvasCTX.setTextAlign(TextAlignment.CENTER);
        this.letterWidth = 20;
        this.letterHeight = (short)(this.hackingCanvasCTX.getFont().getSize() + 50);
        this.letterGap = 10;
        this.letterGapHeight = 100;
        this.overallGameInfo = new OverallGameInfo();
        this.timerGroup = new ThreadGroup("CheckAnswerThreads");
        this.itemsToUseVBox.setSpacing(10);
        this.playerInformation = playerInformation;
        this.changeInMoney = this.changeInInfamy = 0;
        this.statsAnimationLoop = new StatsAnimationLoop(this.statsCanvas, this.playerInformation);
        this.statsAnimationLoop.start();
        this.initializePuzzle();
    }

    private void drawLetters(){this.drawLetters(this.overallGameInfo.getCurrentPuzzle(), this.letterHitBoxes);}
    
    private void drawLetters(GuessingWordGames gameSelected, Point2D[] hitboxesSelected){
        this.setCanvasFont('B', this.hackingCanvasCTX);
        this.hackingCanvasCTX.clearRect(0, 0, this.hackingCanvas.getWidth(), this.hackingCanvas.getHeight());
        byte wordGuessedCounter = 0;
        for(byte i = 0; i < hitboxesSelected.length; i++){
            if(Character.isAlphabetic(gameSelected.getWordSelected().charAt(i))){
                this.hackingCanvasCTX.fillText("_", hitboxesSelected[i].getX() - (short) 0, hitboxesSelected[i].getY()-this.hackingCanvasCTX.getFont().getSize()/2);
                if(!gameSelected.getGivenLetters()[i]){
                    if(wordGuessedCounter < gameSelected.getStringGuessed().length()){this.hackingCanvasCTX.fillText(Character.toString(gameSelected.getStringGuessed().charAt(wordGuessedCounter)), hitboxesSelected[i].getX(), hitboxesSelected[i].getY()-this.hackingCanvasCTX.getFont().getSize()/2);}
                    wordGuessedCounter++;
                }else{this.hackingCanvasCTX.fillText(Character.toString(gameSelected.getWordSelected().charAt(i)).toUpperCase(), hitboxesSelected[i].getX() - (short) 0, hitboxesSelected[i].getY()-this.hackingCanvasCTX.getFont().getSize()/2);}
            }
        }
    }

    synchronized private void drawLetters(int index){
        if(this.overallGameInfo.getCurrentPuzzle() instanceof MiniPuzzleGame miniPuzzleGame && Character.isAlphabetic(this.overallGameInfo.getCurrentPuzzle().getWordSelected().charAt(index))){
            this.drawLetters();
            this.hackingCanvasCTX.save();
            this.hackingCanvasCTX.translate(this.letterHitBoxes[index].getX(), this.letterHitBoxes[index].getY() + 20);
            this.hackingCanvasCTX.rotate(-90);
            this.hackingCanvasCTX.fillText("->", 0, this.hackingCanvasCTX.getFont().getSize()/2);
            this.hackingCanvasCTX.restore();
            String[] listOfWords = miniPuzzleGame.getEncodedWordsAssociatedWithCharacter(index);
            this.setCanvasFont('N', this.hackingCanvasCTX);
            for(byte i = 0; i < listOfWords.length; i++){this.hackingCanvasCTX.fillText(listOfWords[i], this.letterHitBoxes[index].getX(), this.letterHitBoxes[index].getY() + this.hackingCanvasCTX.getFont().getSize() * 5 + i * this.hackingCanvasCTX.getFont().getSize());}
        }
    }

    private void createHitBoxes(){
        this.letterHeight = (short)this.hackingCanvasCTX.getFont().getSize();
        byte interval = 0;
        int[] lengthOfWords = this.overallGameInfo.getCurrentPuzzle().getLengthOfStringsInWord();
        short initialLocationY = (short)(this.hackingCanvas.getHeight()/2 - (this.letterHeight + this.letterGapHeight) * (lengthOfWords.length -1)/2);
        for(byte j = 0; j< lengthOfWords.length; j++){
            short initialLocation = (short)(this.hackingCanvas.getWidth()/2 - ((this.letterWidth + this.letterGap) * (lengthOfWords[j] - 1)/2));
            for(byte i = 0; i< lengthOfWords[j]; i++){
                this.letterHitBoxes[interval] = new Point2D(initialLocation + i*(this.letterWidth + this.letterGap), initialLocationY + j *(this.letterHeight + this.letterGapHeight));
                interval++;
            }
            while(interval < this.overallGameInfo.getCurrentPuzzle().getWordSelected().length() && !Character.isAlphabetic(this.overallGameInfo.getCurrentPuzzle().getWordSelected().charAt(interval))){interval++;}
        }
    }

    private void initializePuzzle(){
        HackingSceneController controller = this;
        this.letterHitBoxes = new Point2D[this.overallGameInfo.getCurrentPuzzle().getWordSelected().length()];
        this.updatePuzzlesCorrectCanvas();
        this.createHitBoxes();
        this.drawLetters();
        PuzzleTimer myTimer = this.overallGameInfo.getCurrentPuzzle().getPuzzleTimer();
        this.overallGameInfo.getCurrentPuzzle().startTimer();
        Platform.runLater(new Thread(controller::updateItemsToUse));
        this.updateTimeRemainingCanvas();
        new Thread(this.timerGroup, "TimerCheck"){
            @Override public void run() {
                while(myTimer.isAlive() && !this.isInterrupted()){
                    byte currentTime = myTimer.getCurrentTimeRemaining();
                    while(currentTime == myTimer.getCurrentTimeRemaining() && myTimer.isAlive() && !this.isInterrupted()){Thread.onSpinWait();}
                    if(myTimer.isAlive() && !this.isInterrupted()){
                        if(controller.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle){synchronized (controller){controller.updateTimeRemainingCanvas();}}
                        else{controller.updateTimeRemainingCanvas();}
                    }
                }
                if(myTimer.getCurrentTimeRemaining() <= 0 && !this.isInterrupted()){
                    synchronized(controller){
                        if(myTimer.getCurrentTimeRemaining() <= 0 && !this.isInterrupted()){
                            if(!controller.overallGameInfo.terminateAndPrepareForNextPuzzle(false, controller.timerGroup)){
                                controller.changePlayerInfoValues(-30, -30);
                                controller.initializePuzzle();
                            }else{
                                controller.changePlayerInfoValues(-70, -70);
                                controller.returnToBaseCamp();
                            }
                        }
                    }
                }
            }
        }.start();
        if(this.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle finalPuzzle){
            new Thread(this.timerGroup, "StrikeChecker"){
                @Override public void run(){
                    while(!this.isInterrupted() && finalPuzzle.getStrikes() < finalPuzzle.getMaximumStrikes()){
                        byte currentStrikes = finalPuzzle.getStrikes();
                        while(currentStrikes == finalPuzzle.getStrikes() && !this.isInterrupted()){Thread.onSpinWait();}
                        if(!this.isInterrupted() && finalPuzzle.getStrikes() >= finalPuzzle.getMaximumStrikes()){
                            if(controller.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle){synchronized (controller){controller.updateTimeRemainingCanvas();}}
                            else{controller.updateTimeRemainingCanvas();}
                        }
                    }
                    if(!this.isInterrupted() && finalPuzzle.getStrikes() >= finalPuzzle.getMaximumStrikes()){
                        synchronized(controller){
                            if(!this.isInterrupted() && finalPuzzle.getStrikes() >= finalPuzzle.getMaximumStrikes()){
                                if(!controller.overallGameInfo.terminateAndPrepareForNextPuzzle(false, controller.timerGroup)){
                                    controller.changePlayerInfoValues(-30, -30);
                                    controller.initializePuzzle();
                                }else{
                                    controller.changePlayerInfoValues(-70, -70);
                                    controller.returnToBaseCamp();
                                }
                            }
                        }
                    }
                }
            }.start();
        }
    }

    @FXML public void handleMouseMove(MouseEvent mouseEvent){
        if(!(this.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle)){
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();
            boolean actionPerformed = false;
            byte counter = 0;
            while(counter < this.letterHitBoxes.length && !actionPerformed){
                if(this.letterHitBoxes[counter] != null && Math.abs(mouseY - this.letterHitBoxes[counter].getY()) < this.letterHeight/2.0 && Math.abs(mouseX - this.letterHitBoxes[counter].getX()) < this.letterWidth/2.0){
                    actionPerformed = true;
                    this.drawLetters(counter);
                }
                counter++;
            }
        }
    }

    @FXML public void handleCheckIfWordIsCorrect(){
        HackingSceneController controller = this;
        PuzzleTimer myTimer = controller.overallGameInfo.getCurrentPuzzle().getPuzzleTimer();
        new Thread(this.timerGroup, "UserCheck"){
            @Override public void run() {
                synchronized(controller){
                    if(!this.isInterrupted()){
                        if(controller.overallGameInfo.getCurrentPuzzle().checkIfWordIsCorrect()){
                            if(!controller.overallGameInfo.terminateAndPrepareForNextPuzzle(true, controller.timerGroup)){
                                controller.changePlayerInfoValues(20, 20);
                                controller.initializePuzzle();
                            }else{
                                Platform.runLater(new Thread(() ->{
                                    HelloApplication.setCurrentScene(Scenes.PRIZE_WHEEL_SCENE);
                                    ((PrizeWheelController)(Scenes.PRIZE_WHEEL_SCENE.getFXMLLoader().getController())).initializeScene(controller.overallGameInfo.getPuzzleAnswers(), controller.playerInformation, controller.changeInMoney, controller.changeInInfamy);
                                }));
                            }
                        }else {
                            myTimer.subtractTimeRemaining((byte)5);
                            if(controller.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle finalPuzzle && finalPuzzle.increaseStrikesAndCheckForEnd()){myTimer.subtractTimeRemaining(myTimer.getCurrentTimeRemaining());}
                        }
                    }
                }
            }
        }.start();
    }

    public void handleLetterTyped(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.BACK_SPACE){this.overallGameInfo.getCurrentPuzzle().removeLastCharacterToStringGuessed();}
        else if(keyEvent.getCode().isLetterKey()){this.overallGameInfo.getCurrentPuzzle().addToStringGuessed(keyEvent.getText().charAt(0));}
        this.drawLetters();
    }

    private void setCanvasFont(char font, GraphicsContext graphicsContext){
        try{
            if(font == 'B'){graphicsContext.setFont(Font.loadFont(new FileInputStream("src/main/resources/Font/Play-Bold.ttf"), 20));}
            else if(font == 'N'){graphicsContext.setFont(Font.loadFont(new FileInputStream("src/main/resources/Font/Play-Regular.ttf"), 10));}
        }catch(FileNotFoundException error){error.printStackTrace();}
    }

    private void updatePuzzlesCorrectCanvas(){
        this.puzzlesRightCanvasCTX.clearRect(0, 0, this.puzzlesRightCanvas.getWidth(), this.puzzlesRightCanvas.getHeight());
        this.setCanvasFont('N', this.puzzlesRightCanvasCTX);
        short lineSpacing = 10;
        short wordSize = (short)this.puzzlesRightCanvasCTX.getFont().getSize();
        short startingPoint = (short)(this.puzzlesRightCanvas.getHeight()/2 - (((wordSize + lineSpacing) * (this.overallGameInfo.getPuzzlesAnswered() - 1)/2)));
        for(byte i = 0; i < this.overallGameInfo.getPuzzlesAnswered(); i++){this.puzzlesRightCanvasCTX.fillText(this.overallGameInfo.getPuzzleAnswers()[i] != null ? "Correct:" + this.overallGameInfo.getPuzzleAnswers()[i] : "Incorrect", this.puzzlesRightCanvas.getWidth()/2, startingPoint + i * (lineSpacing + wordSize));}
    }

    private void updateItemsToUse(){
        HackingSceneController controller = this;
        this.itemsToUseVBox.getChildren().clear();
        for(Items i : this.playerInformation.getItemsOwned()){
            if(i.getItemReference().canBeUsed(this.overallGameInfo, this.playerInformation)){
                VBox item = new VBox();
                item.setBorder(Border.stroke(Paint.valueOf("black")));
                item.setAlignment(Pos.CENTER);
                Label description = new Label(i.getItemReference().getName() + "\n" + i.getItemReference().getInfo() + "\nYou Have " + i.getQuantity());
                description.setTextAlignment(TextAlignment.CENTER);
                description.setWrapText(true);
                item.getChildren().add(description);
                Button useButton = new Button("Use This Item");
                useButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (mouseEvent)->{
                    this.playerInformation.useItem(i.getItemReference(), this.overallGameInfo);
                    this.updateItemsToUse();
                    controller.drawLetters();
                });
                item.getChildren().add(useButton);
                this.itemsToUseVBox.getChildren().add(item);
            }
        }
    }

    private void returnToBaseCamp(){
        Platform.runLater(new Thread(() -> {
            this.statsAnimationLoop.interrupt();
            HelloApplication.setCurrentScene(Scenes.RESULTS_SCENE);
            ((ResultsSceneController)(Scenes.RESULTS_SCENE.getFXMLLoader().getController())).initializeResultsScene(this.playerInformation, this.changeInInfamy, this.changeInMoney);
        }));
    }


    private void updateTimeRemainingCanvas(){
        this.timeRemainingCanvasCTX.clearRect(0, 0, this.timeRemainingCanvas.getWidth(), this.timeRemainingCanvas.getHeight());
        this.timeRemainingCanvasCTX.fillText("Time Left:" + this.overallGameInfo.getCurrentPuzzle().getPuzzleTimer().getCurrentTimeRemaining(), this.timeRemainingCanvas.getWidth()/2.0, 20);
        if(this.overallGameInfo.getCurrentPuzzle() instanceof FinalPuzzle finalPuzzle){this.timeRemainingCanvasCTX.fillText("Strikes Left:" + (finalPuzzle.getMaximumStrikes() - finalPuzzle.getStrikes()), this.timeRemainingCanvas.getWidth()/2.0, 40);}
    }

    private OverallGameInfo overallGameInfo;
    private Point2D[] letterHitBoxes;
    private ThreadGroup timerGroup;
    private PlayerInformation playerInformation;
    private StatsAnimationLoop statsAnimationLoop;
    private short changeInMoney, changeInInfamy;
}