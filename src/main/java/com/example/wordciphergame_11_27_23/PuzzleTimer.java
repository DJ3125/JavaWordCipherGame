package com.example.wordciphergame_11_27_23;


public class PuzzleTimer extends Thread{
    synchronized public byte getCurrentTimeRemaining() {return this.currentTimeRemaining;}
    synchronized public void subtractTimeRemaining(byte amountToSubtract){
        this.amountOfTimeSubtracted += amountToSubtract;
        if(this.currentTimeRemaining - this.amountOfTimeSubtracted< 0){this.currentTimeRemaining = 0;}
    }

    synchronized public void addTimeRemaining(byte amountToAdd){if(this.isAlive()){this.amountOfTimeSubtracted -= amountToAdd;}}

    public PuzzleTimer(byte timeRemaining){this.currentTimeRemaining = this.initialTimeRemaining = timeRemaining;}

    @Override public void run() {
        try{
            long currentTimeInMilliseconds = System.currentTimeMillis();
            while(this.currentTimeRemaining > 0 && !this.isInterrupted()){
                Thread.sleep(1000);
                this.currentTimeRemaining = (byte)(this.initialTimeRemaining - ((System.currentTimeMillis() - currentTimeInMilliseconds)/1000) - this.amountOfTimeSubtracted);
                if(this.currentTimeRemaining < 0){this.currentTimeRemaining = 0;}
            }
        }catch(InterruptedException error){}
    }

    private final byte initialTimeRemaining;
    private byte currentTimeRemaining;
    private short amountOfTimeSubtracted;
}
