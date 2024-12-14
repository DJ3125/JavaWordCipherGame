package com.example.wordciphergame_11_27_23;

public class PrizeWheelGameInfo {

    public PlayerInformation getPlayerInformation() {return this.playerInformation;}

    public byte getPicksRemaining() {return this.picksRemaining;}

    public PrizeWheelGameInfo(PlayerInformation playerInformation, String[] puzzlesAnsweredArray){
        byte puzzlesCorrect = 0;
        for(String i : puzzlesAnsweredArray){if(i != null){puzzlesCorrect++;}}
        this.picksRemaining = puzzlesCorrect;
        this.playerInformation = playerInformation;
    }

    public boolean handlePrizeClicked(PrizeWheelPrizes[] prizeWheelPrizes, byte index){
        prizeWheelPrizes[index].grantPrize(this.playerInformation);
        prizeWheelPrizes[index].setSelected();
        this.picksRemaining--;
        return picksRemaining <= 0;
    }

    private byte picksRemaining;
    private final PlayerInformation playerInformation;
}
