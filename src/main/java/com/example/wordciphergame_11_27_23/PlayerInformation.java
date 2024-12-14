package com.example.wordciphergame_11_27_23;

import java.util.ArrayList;

public class PlayerInformation {
    public int getInfamy() {return this.infamy;}
    public ArrayList<Items> getItemsOwned() {return this.itemsOwned;}
    public void subtractInfamy(int changeInInfamy){
        this.infamy -= changeInInfamy;
        if(this.infamy < 0){this.infamy = 0;}
    }
    public void addInfamy(int changeInInfamy){
        this.infamy += changeInInfamy;
        if(this.infamy < 0){this.infamy = 0;}
    }
    public void setInfamy(int infamy) {
        this.infamy = infamy;
        if(this.infamy < 0){this.infamy = 0;}
    }
    public int getMoney() {return this.money;}
    public void addMoney(int changeInBalance){
        this.money += changeInBalance;
        if(this.money < 0){this.money = 0;}
    }

    public void setMoney(int money) {
        this.money = money;
        if(this.money < 0){this.money = 0;}
    }
    public short getLives() {return this.lives;}
    public void increaseLives(short lives){
        this.lives += lives;
        if(this.lives < 0){this.lives = 0;}
    }
    public void setLives(short newValue){
        this.lives = newValue;
        if(this.lives < 0){this.lives = 0;}
    }

    public PlayerInformation(int startingBalance, int startingInfamy, short startingLives){
        this.money = startingBalance;
        this.infamy = startingInfamy;
        this.lives = startingLives;
        this.itemsOwned = new ArrayList<>();
    }

    public boolean checkIfCanBuyItem(AllItemReferences itemReferences){return this.money >= itemReferences.getItemReference().getCost();}

    public void buyItem(AllItemReferences itemReferences, short amountToBuy){
        byte indexOfItem = this.getIndexOfItemReference(itemReferences);
        if(indexOfItem >= 0){this.itemsOwned.get(indexOfItem).increaseQuantity(amountToBuy);}
        else{this.itemsOwned.add(new Items(amountToBuy, itemReferences));}
        this.money -= amountToBuy * itemReferences.getItemReference().getCost();
    }

    public void useItem(ItemReference itemReferences, OverallGameInfo gameInfo){
        byte index = this.getIndexOfItemReference(itemReferences);
        if(index >= 0){
            itemReferences.performAction(gameInfo, this);
            this.itemsOwned.get(index).increaseQuantity((short) -1);
            if(this.itemsOwned.get(index).getQuantity() <= 0){this.itemsOwned.remove(index);}
        }
    }

    public short getAmountOfItem(AllItemReferences itemReferences){
        byte index = this.getIndexOfItemReference(itemReferences);
        if(index < 0){return 0;}
        else{return this.itemsOwned.get(index).getQuantity();}
    }

    private byte getIndexOfItemReference(AllItemReferences itemReferences){return this.getIndexOfItemReference(itemReferences.getItemReference());}

    private byte getIndexOfItemReference(ItemReference itemReferences){
        byte index = -1;
        byte interval = 0;
        while(interval < this.itemsOwned.size() && index == -1){
            if(itemReferences == this.itemsOwned.get(interval).getItemReference()){index = interval;}
            interval++;
        }
        return index;
    }

    private int money, infamy;
    private short lives;
    private final ArrayList<Items> itemsOwned;
}
