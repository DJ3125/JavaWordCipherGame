package com.example.wordciphergame_11_27_23;

abstract public class ItemReference {
    public short getCost() {return this.cost;}
    public String getName() {return this.name;}

    public ItemReference(short cost, String name){
        this.cost = cost;
        this.name = name;
    }

    abstract public void performAction(OverallGameInfo gameInfo, PlayerInformation playerInformation);
    abstract public String getInfo();
    abstract public boolean canBeUsed(OverallGameInfo gameInfo, PlayerInformation playerInformation);

    private final short cost;
    private final String name;
}
