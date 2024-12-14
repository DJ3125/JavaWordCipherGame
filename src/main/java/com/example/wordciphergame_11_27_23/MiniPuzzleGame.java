package com.example.wordciphergame_11_27_23;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class MiniPuzzleGame extends GuessingWordGames{

    public String[] getEncodedWordsAssociatedWithCharacter(int characterIndex){return this.listOfWordsForEachCharacter[characterIndex];}
    private String encodeWord(char characterSelected, String wordSelected){
        wordSelected = wordSelected.replace(characterSelected, '_');
        byte amountOfExtraLettersToRemove = (byte)((Math.log(wordSelected.length())/Math.log(4)) -1);
        while(amountOfExtraLettersToRemove > 0){
            byte indexToRemove = (byte)(Math.random() * wordSelected.length());
            if(wordSelected.charAt(indexToRemove) != '_'){
                wordSelected = wordSelected.substring(0, indexToRemove) + "_" + wordSelected.substring(indexToRemove + 1);
                amountOfExtraLettersToRemove--;
            }
        }
        return wordSelected;
    }

    public MiniPuzzleGame(File themeFile){
        super(themeFile, 5);
        this.listOfWordsForEachCharacter = this.createListOfCharactersAndWords();
        for(byte i = 0; i < super.getWordSelected().length(); i++){super.getGivenLetters()[i] = !Character.isAlphabetic(super.getWordSelected().charAt(i));}
    }
    
    private String[] getWordsForCharacter(char characterSelected, byte amountOfWordsDesired){
        ArrayList<String> stringsThatHaveDesiredCharacter = new ArrayList<>();
        for(File i : new File("src/main/resources/RefinedTopics").listFiles()){
            try{
                Scanner scanner = new Scanner(i);
                while(scanner.hasNextLine()){
                    String stringToExamine = scanner.nextLine();
                    if(stringToExamine.indexOf(characterSelected) != -1){stringsThatHaveDesiredCharacter.add(stringToExamine);}
                }
                scanner.close();
            }catch (FileNotFoundException error){error.printStackTrace();}
        }
        String[] selectedWords = new String[amountOfWordsDesired];
        while(amountOfWordsDesired > 0 && !stringsThatHaveDesiredCharacter.isEmpty()){
            selectedWords[amountOfWordsDesired - 1] = stringsThatHaveDesiredCharacter.remove((int)(Math.random() * stringsThatHaveDesiredCharacter.size()));
            amountOfWordsDesired--;
        }
        return selectedWords;
    }

    private String[][] createListOfCharactersAndWords(){
        byte amountOfHints = 3;
        String[][] arrayListOfCharacterIndexAndEncodedWords = new String[super.getWordSelected().length()][amountOfHints];
        byte indexOfRun = 0;
        for(char i : super.getWordSelected().toCharArray()){
            if(Character.isAlphabetic(i)){
                String[] encodedWords = new String[amountOfHints];
                byte counter = 0;
                for(String j : this.getWordsForCharacter(i, amountOfHints)){
                    encodedWords[counter] = this.encodeWord(i, j);
                    counter++;
                }
                arrayListOfCharacterIndexAndEncodedWords[indexOfRun] = encodedWords;
            }
            indexOfRun++;
        }
        return arrayListOfCharacterIndexAndEncodedWords;
    }
    
    final private String[][] listOfWordsForEachCharacter;
}
