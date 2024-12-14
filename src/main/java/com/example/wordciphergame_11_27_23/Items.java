package com.example.wordciphergame_11_27_23;

public class Items {
    public ItemReference getItemReference() {return this.itemReference;}
    public void setQuantity(short quantity) {this.quantity = quantity;}
    public short getQuantity(){return this.quantity;}
    public void increaseQuantity(short amountToIncrease){
        this.quantity += amountToIncrease;
        if(this.quantity < 0){this.quantity = 0;}
    }

    public Items(short initialQuantity, AllItemReferences itemReference){
        this.itemReference = itemReference.getItemReference();
        this.quantity = initialQuantity;
    }

    private short quantity;
    private final ItemReference itemReference;
}
