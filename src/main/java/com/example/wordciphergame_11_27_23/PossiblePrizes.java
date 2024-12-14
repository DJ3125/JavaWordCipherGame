package com.example.wordciphergame_11_27_23;

public enum PossiblePrizes {
    DYLANS_HACKING_BONANZA("Dylan's Hacking Bonanza", 3, true, 3, true, (short)1),
    LUCKY_DAY("Lucky Day", 100, false, 0, false, (short)20);


    PossiblePrizes(String title, double changeInMoney, boolean moneyMultiplier, double changeInInfamy, boolean infamyMultiplier, short weight){
        this.name = title;
        this.changeInInfamy = changeInInfamy;
        this.changeInMoney = changeInMoney;
        this.infamyMultiplier = infamyMultiplier;
        this.moneyMultiplier = moneyMultiplier;
        this.weight = weight;
    }

    public void setNewMonetaryBalance(PlayerInformation playerInformation){
        int monetaryValue = playerInformation.getMoney();
        if(this.moneyMultiplier){monetaryValue = (int)(((double)(monetaryValue)) * this.changeInMoney);}
        else{monetaryValue += (int)this.changeInMoney;}
        playerInformation.setMoney(monetaryValue);
    }

    public void setNewInfamyBalance(PlayerInformation playerInformation){
        int infamyValue = playerInformation.getInfamy();
        if(this.infamyMultiplier){infamyValue = (int)(((double)(infamyValue)) * this.changeInInfamy);}
        else{infamyValue += (int)this.changeInInfamy;}
        playerInformation.setInfamy(infamyValue);
    }

    public String getDetails(){
        String details = this.name + "\n";
        if((this.moneyMultiplier && this.changeInMoney == 1) || (!this.moneyMultiplier && this.changeInMoney == 0)){details += "Your Bank Account Balance Has Not Changed\n";}
        else if(this.moneyMultiplier){details += "Your Bank Account has "+ ((this.changeInMoney > 1)? "increased " : "decreased ") +"by " + Math.abs(((int)((this.changeInMoney - 1) * 100)))+"%\n";}
        else{details += "Your Bank Account has " + ((this.changeInMoney > 0) ? "increased" : "decreased") + " by $" + Math.abs(this.changeInMoney) + "\n";}
        if((this.infamyMultiplier && this.changeInInfamy == 1) || (!this.infamyMultiplier && this.changeInInfamy == 0)){details += "Your Infamy Has Not Changed\n";}
        else if(this.infamyMultiplier){details += "Your Infamy has "+ ((this.changeInInfamy > 1)? "increased " : "decreased ") +"by " + Math.abs(((int)((this.changeInInfamy - 1) * 100)))+"%\n";}
        else{details += "Your Infamy has " + ((this.changeInInfamy > 0) ? "increased" : "decreased") + " by $" + Math.abs(this.changeInInfamy) + "\n";}
        return details;
    }

    public static PossiblePrizes randomlyGeneratePrize(){
        int totalWeight = 0;
        for(PossiblePrizes i : PossiblePrizes.values()){totalWeight += i.weight;}
        int chance = 1 + (int)(totalWeight * Math.random());
        int interval = 0;
        while(interval < PossiblePrizes.values().length && chance > 0){
            chance -= PossiblePrizes.values()[interval].weight;
            interval++;
        }
        return PossiblePrizes.values()[--interval];
    }

    private final String name;
    private final double changeInMoney, changeInInfamy;
    private final boolean moneyMultiplier, infamyMultiplier;
    private final short weight;

}
