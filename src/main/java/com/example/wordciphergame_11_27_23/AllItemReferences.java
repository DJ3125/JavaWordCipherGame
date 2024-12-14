package com.example.wordciphergame_11_27_23;

public enum AllItemReferences {
    MINOR_INCREASE_TIME_ITEM(new IncreaseTimeItem((short)(250), (byte)10)),
    MINOR_REVEAL_LETTER_HINT_ITEM(new RevealLetterHintItem((short) 400, (byte) 1)),
    EXTRA_TRY_FOR_FINAL_PUZZLE_ITEM(new ExtraTryForFinalPuzzleItem((short) 500, (byte) 1));


    AllItemReferences(ItemReference itemReference){this.itemReference = itemReference;}
    public ItemReference getItemReference() {return this.itemReference;}
    private final ItemReference itemReference;
}
