import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;
import java.awt.*;


public class CrosswordSolver extends JFrame {
    public static void main(String[] args) {
        HashMap <Integer,int[]> directionMap = new HashMap<Integer,int[]>();
        directionMap.put(0, new int[]{0,-1});
        directionMap.put(1, new int[]{1,-1});
        directionMap.put(2, new int[]{1,0});
        directionMap.put(3, new int[]{1,1});
        directionMap.put(4, new int[]{0,1});
        directionMap.put(5, new int[]{-1,1});
        directionMap.put(6, new int[]{-1,0});
        directionMap.put(7, new int[]{-1,-1});

        Scanner input = new Scanner(System.in);

        String[] wordList = makeWordList(input);
        System.out.println(Arrays.toString(wordList));

        Scanner crosswordFile = getInputFile(input);
        char[][] crosswordGrid = readCrossword(crosswordFile);

        char[][] solutionGrid = new char[crosswordGrid.length][crosswordGrid[0].length];
        
        for (String word : wordList) { //for each word
            wordLoop:
            for (int i = 0; i < crosswordGrid.length; i++) { //for each column
                for (int j = 0; j < crosswordGrid[0].length; j++) { //for each row
                    if(crosswordGrid[i][j] == word.charAt(0)) { // if the character at a given position match the first lette of the word
                        for (int angle = 0; angle < 8; angle++) { // for each angle
                            try {
                                for (int letter = 0; letter < word.length(); letter++) {
                                    if (crosswordGrid[i + (directionMap.get(angle)[1] * letter)][j + (directionMap.get(angle)[0] * letter)] != word.charAt(letter)){
                                        throw new Exception();
                                    }
                                }

                                for (int letter = 0; letter < word.length(); letter++){
                                    solutionGrid[i + (directionMap.get(angle)[1] * letter)][j + (directionMap.get(angle)[0] * letter)] = word.charAt(letter);
                                }
                                break wordLoop;
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }
        }

        //TODO debug output file
        for (int i = 0; i < solutionGrid.length; i++) {
            for (int j = 0; j < solutionGrid.length; j++) {
                if (solutionGrid[i][j] == '\u0000') {
                    System.out.print("·");
                }
                else{
                System.out.print(solutionGrid[i][j]);
                }
            }
            System.out.println("");
        }
        CrosswordSolver frame1 = new CrosswordSolver(solutionGrid);
    }

    public CrosswordSolver(char[][] solutionGrid) { 
        setTitle("GUIExample Frame");
        setSize(320, 240);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set layout   
        setLayout(new GridLayout(solutionGrid.length,solutionGrid[0].length));

        // add components
        JLabel[][] cellArray = new JLabel[solutionGrid.length][solutionGrid[0].length];
        for(JLabel[] row : cellArray) {
            for (JLabel column : row) {
                column = new JLabel();
            }
          }
          

        for (int i = 0; i < solutionGrid.length; i++) {
            for (int j = 0; j < solutionGrid[0].length; j++) {
                cellArray[1][1].setText(Character.toString(solutionGrid[i][j]));
                if (cellArray[i][j].getText().charAt(0) != '\u0000'){
                    cellArray[i][j].setBackground(Color.green);
                }
            }
        }
    }
    public static String[] makeWordList(Scanner input) {
        Scanner inputFile = getInputFile(input);
        //Makes sure the input file contains valid words and adds it to word list
        ArrayList<String> wordList = new ArrayList<String>();
        while (inputFile.hasNextLine()) {
            try {
                String nextWord = inputFile.nextLine();
                //Checks if word is between 4-8 letters long and checks if it is all lower case and alpabetic letters
                if (nextWord.length() < 4 || nextWord.length() > 8 || !nextWord.matches("[a-z]+")) {
                    throw new Exception();
                }

                wordList.add(nextWord);
                
                if (wordList.size() > 10) {
                    throw new Exception();
                }

            } catch (Exception e) {
                System.out.println("Invalid Input File Try Again");
                inputFile = getInputFile(input);
                wordList.clear();
            }
        }
            String[] returnList = new String[wordList.size()];
            return wordList.toArray(returnList);
       
    }
    
    public static Scanner getInputFile(Scanner input){
        while (true) {
            System.out.print("\nEnter The Input File Name: ");
        
            try {
                return new Scanner(new File(input.nextLine()));

            } catch (Exception e) {
                System.out.print("Invalid File Name Try Again");
                continue;
            }
        }
    }
    
    public static char[][] readCrossword(Scanner crosswordFile){
        //TODO fix this its so bad :(
        int row = 0;
        String line = crosswordFile.nextLine();
        int column = line.length();
        char[][] crosswordGrid = new char[20][column];

        for (int letter = 0; letter < line.length(); letter++) {
            crosswordGrid[row][letter] = line.charAt(letter);
        }
        row+=1;
        while (crosswordFile.hasNext()) {
            line = crosswordFile.nextLine();
            for (int letter = 0; letter < line.length(); letter++) {
                crosswordGrid[row][letter] = line.charAt(letter);

            }
            row++;
        }

        int rows;
        for (rows = 0; rows < crosswordGrid.length; rows++) {
            if (crosswordGrid[rows][0] == '\u0000') {
                break;
            }
        }
        char[][] returnGrid = new char[rows][column];
        for (int i = 0; i < returnGrid.length; i++) {
            for (int j = 0; j < returnGrid[0].length; j++) {
                returnGrid[i][j] = crosswordGrid[i][j];
            }
        }

        return returnGrid;
    }
}
