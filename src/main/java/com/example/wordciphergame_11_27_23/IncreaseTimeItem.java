package com.example.wordciphergame_11_27_23;

public class IncreaseTimeItem extends ItemReference{
    public IncreaseTimeItem(short cost, byte timeIncrease){
        super(cost, "Increase Time Item");
        this.timeIncrease = timeIncrease;
    }

    @Override public void performAction(OverallGameInfo gameInfo, PlayerInformation playerInformation) {gameInfo.getCurrentPuzzle().getPuzzleTimer().addTimeRemaining(this.timeIncrease);}
    @Override public boolean canBeUsed(OverallGameInfo gameInfo, PlayerInformation playerInformation) {return gameInfo != null && gameInfo.getCurrentPuzzle().getPuzzleTimer().isAlive();}
    @Override public String getInfo() {return "Increases The Amount Of Time To Complete A Puzzle By " + this.timeIncrease + " seconds";}

    private final byte timeIncrease;
}
