package com.example.wordciphergame_11_27_23;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GuessingWordGames {
    public File getThemeFile(){return this.themeFile;}
    public String getWordSelected() {return this.wordSelected;}
    public String getStringGuessed() {return this.stringGuessed;}
    public void addToStringGuessed(char newCharacter){if(this.stringGuessed.length() < this.wordSelected.replace(" ", "").length()){this.stringGuessed += Character.toString(newCharacter).toUpperCase();}}
    public void removeLastCharacterToStringGuessed(){if(!this.stringGuessed.isEmpty()){this.stringGuessed = this.stringGuessed.substring(0, this.stringGuessed.length() -1);}}
    public boolean[] getGivenLetters() {return this.givenLetters;}
    public PuzzleTimer getPuzzleTimer() {return this.puzzleTimer;}
    public void startTimer(){this.puzzleTimer.start();}

    public GuessingWordGames(File themeFile, int timeRemaining){
        this.themeFile = themeFile;
        this.wordSelected = this.selectRandomWordInTheme();
        System.out.println(this.wordSelected);
//        this.wordSelected = "cat";
        this.givenLetters = new boolean[this.wordSelected.length()];
        this.stringGuessed = "";
        this.puzzleTimer = new PuzzleTimer((byte) (this.wordSelected.length() * 2));
    }

    public String selectRandomWordInTheme(){
        String selectedString = null;
        int lengthOfFile = this.getAmountOfLinesInTxtFile();
        int selectedIndex = (int)((lengthOfFile - 1) * Math.random());
        assert lengthOfFile >= 0;
        try{
            Scanner scanner = new Scanner(this.themeFile);
            int count = 0;
            boolean selected = false;
            while(scanner.hasNextLine() && !selected){
                if(count==selectedIndex){
                    selectedString = scanner.nextLine().toLowerCase();
                    selected = true;
                }else{scanner.nextLine();}
                count++;
            }
            scanner.close();
        }catch (FileNotFoundException error){error.printStackTrace();}
        return selectedString;
    }

    private int getAmountOfLinesInTxtFile(){
        int count = -1;
        try{
            Scanner scanner = new Scanner(new File("src/main/resources/RefinedTopics/Animals"));
            count = 0;
            while(scanner.hasNextLine()){scanner.nextLine(); count++;}
            scanner.close();
        }catch (FileNotFoundException error){error.printStackTrace();}
        return count;
    }

    public boolean checkIfWordIsCorrect(){
        byte interval = 0;
        for(byte i = 0; i < this.wordSelected.length(); i++){
            if(!this.getGivenLetters()[i]){
                try{if(Character.toLowerCase(this.stringGuessed.charAt(interval)) != Character.toLowerCase(this.wordSelected.charAt(i))){return false;}}
                catch(StringIndexOutOfBoundsException error){return false;}
                interval++;
            }
        }
        return true;
    }

    public int[] getLengthOfStringsInWord(){
        ArrayList<String> stringsInWord = new ArrayList<>();
        byte counter = 0;
        while(counter < this.wordSelected.length()){
            String word = "";
            while(counter < this.wordSelected.length() && Character.isAlphabetic(this.wordSelected.charAt(counter))){
                word = word.concat(Character.toString(this.wordSelected.charAt(counter)));
                counter++;
            }
            counter++;
            stringsInWord.add(word);
        }
        int[] lengthOfStringsInWordArray = new int[stringsInWord.size()];
        for(byte i = 0; i< stringsInWord.size(); i++){lengthOfStringsInWordArray[i] = stringsInWord.get(i).length();}
        return lengthOfStringsInWordArray;
    }

    final private File themeFile;
    final private String wordSelected;
    private String stringGuessed;
    private final boolean[] givenLetters;
    private final PuzzleTimer puzzleTimer;
}
