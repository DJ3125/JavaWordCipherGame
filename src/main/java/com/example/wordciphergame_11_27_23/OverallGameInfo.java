package com.example.wordciphergame_11_27_23;

import java.io.File;

public class OverallGameInfo{
    public GuessingWordGames getCurrentPuzzle() {
        if(this.onFinalGame){return this.finalPuzzle;}
        else{return this.currentMiniPuzzle;}
    }
    public String[] getPuzzleAnswers(){return this.puzzleAnswers;}
    public byte getPuzzlesAnswered() {return this.puzzlesAnswered;}

    public OverallGameInfo() {
        File[] refinedTopics = new File("src/main/resources/RefinedTopics").listFiles();
        this.themeFile = refinedTopics[(int) (refinedTopics.length * Math.random())];
        this.currentMiniPuzzle = new MiniPuzzleGame(this.themeFile);
        this.finalPuzzle = new FinalPuzzle(this.themeFile, (byte)3);
        this.puzzleAnswers = new String[this.finalPuzzle.getWordSelected().length()];
        this.puzzlesAnswered = 0;
        this.onFinalGame = false;
    }

    public boolean terminateAndPrepareForNextPuzzle(boolean answeredCorrectly, ThreadGroup threadGroupToTerminate){
        threadGroupToTerminate.interrupt();
        this.getCurrentPuzzle().getPuzzleTimer().interrupt();
        Thread.interrupted();
        while(threadGroupToTerminate.activeCount() > 1 || this.getCurrentPuzzle().getPuzzleTimer().isAlive()){Thread.onSpinWait();}
        System.out.println("running2");
        if(this.onFinalGame){return true;}
        else{
            this.puzzlesAnswered++;
            this.puzzleAnswers[this.puzzlesAnswered - 1] = answeredCorrectly ? this.currentMiniPuzzle.getWordSelected() : null;
            this.onFinalGame = this.puzzlesAnswered >= this.finalPuzzle.getWordSelected().length();
            this.currentMiniPuzzle = this.onFinalGame ? null : new MiniPuzzleGame(this.themeFile);
            if(this.currentMiniPuzzle == null){this.finalPuzzle.setGivenLettersBasedOnCorrectPuzzles(this.getPuzzleAnswers());}
            return false;
        }
    }

    final private File themeFile;
    private MiniPuzzleGame currentMiniPuzzle;
    private final FinalPuzzle finalPuzzle;
    private byte puzzlesAnswered;
    private boolean onFinalGame;
    private final String[] puzzleAnswers; //For each puzzle, it holds the answer for every puzzle correct. If a puzzle was not answered correctly, it is null
}
