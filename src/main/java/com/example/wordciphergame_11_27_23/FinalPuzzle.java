package com.example.wordciphergame_11_27_23;

import java.io.File;

public class FinalPuzzle extends GuessingWordGames{
    public FinalPuzzle(File theme, byte maximumStrikes){
        super(theme, 20);
        this.maximumStrikes = maximumStrikes;
        this.strikes = 0;
    }

    public byte getMaximumStrikes() {return this.maximumStrikes;}
    public byte getStrikes() {return this.strikes;}
    public void changeStrikes(byte changeInStrikes){this.strikes += changeInStrikes;}

    public boolean increaseStrikesAndCheckForEnd(){
        this.strikes++;
        return strikes >= this.maximumStrikes;
    }

    public void setGivenLettersBasedOnCorrectPuzzles(String[] puzzlesCorrect){for(byte i = 0; i < puzzlesCorrect.length; i++){super.getGivenLetters()[i] = puzzlesCorrect[i] != null;}}

    private byte strikes;
    private final byte maximumStrikes;
}
