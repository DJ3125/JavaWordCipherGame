package com.example.wordciphergame_11_27_23;

import java.util.ArrayList;

public class RevealLetterHintItem extends ItemReference{
    public RevealLetterHintItem(short cost, byte howManyLettersToReveal){
        super(cost, "Reveal Letter Item");
        this.amountOfLettersToReveal = howManyLettersToReveal;
    }

    @Override public boolean canBeUsed(OverallGameInfo gameInfo, PlayerInformation playerInformation) {return !this.openIndexes(gameInfo).isEmpty();}
    @Override public String getInfo() {return "Reveals " + this.amountOfLettersToReveal + " letters in the current puzzle";}
    @Override public void performAction(OverallGameInfo gameInfo, PlayerInformation playerInformation) {
        ArrayList<Integer> openIndexes = this.openIndexes(gameInfo);
        byte lettersToRevealRemaining = this.amountOfLettersToReveal;
        while(lettersToRevealRemaining > 0 && !openIndexes.isEmpty()){
            gameInfo.getCurrentPuzzle().getGivenLetters()[openIndexes.remove((int)(Math.random() * openIndexes.size()))] = true;
            lettersToRevealRemaining--;
        }
    }

    private ArrayList<Integer> openIndexes(OverallGameInfo gameInfo){
        ArrayList<Integer> indexesOpen = new ArrayList<>();
        for(byte i = 0; i < gameInfo.getCurrentPuzzle().getGivenLetters().length; i++){if(!gameInfo.getCurrentPuzzle().getGivenLetters()[i]){indexesOpen.add((int)i);}}
        return indexesOpen;
    }

    private final byte amountOfLettersToReveal;
}
