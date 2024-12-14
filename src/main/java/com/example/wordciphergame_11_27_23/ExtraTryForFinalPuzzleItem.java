package com.example.wordciphergame_11_27_23;

public class ExtraTryForFinalPuzzleItem extends ItemReference{
    public ExtraTryForFinalPuzzleItem(short cost, byte amountOfExtraTries){
        super(cost, "Extra Try For Final Puzzle");
        this.amountOfExtraTries = amountOfExtraTries;
    }

    @Override public boolean canBeUsed(OverallGameInfo gameInfo, PlayerInformation playerInformation) {return gameInfo.getCurrentPuzzle() instanceof FinalPuzzle finalPuzzle && gameInfo.getCurrentPuzzle().getPuzzleTimer().isAlive() && finalPuzzle.getMaximumStrikes() - finalPuzzle.getStrikes() > 0;}
    @Override public String getInfo() {return "Increases The Amount Of Tries You Get For The Final Puzzle";}
    @Override public void performAction(OverallGameInfo gameInfo, PlayerInformation playerInformation) {
        ((FinalPuzzle)(gameInfo.getCurrentPuzzle())).changeStrikes((byte)(-this.amountOfExtraTries));
        System.out.println(((FinalPuzzle)(gameInfo.getCurrentPuzzle())).getStrikes());
    }

    private final byte amountOfExtraTries;
}
