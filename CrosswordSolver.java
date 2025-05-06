//Imports all necessary packages 
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.Color;

public class CrosswordSolver extends JFrame {
    public static void main(String[] args) {
        //TODO let them run again
        //A Hashmap that converts a number (0-7) to a direction matrix
        //0 is north and it rotates clockwise
        HashMap <Integer,int[]> directionMap = new HashMap<Integer,int[]>();
        directionMap.put(0, new int[]{0,-1});
        directionMap.put(1, new int[]{1,-1});
        directionMap.put(2, new int[]{1,0});
        directionMap.put(3, new int[]{1,1});
        directionMap.put(4, new int[]{0,1});
        directionMap.put(5, new int[]{-1,1});
        directionMap.put(6, new int[]{-1,0});
        directionMap.put(7, new int[]{-1,-1});

        //Input used for reading the terminal
        Scanner input = new Scanner(System.in);

        //String array with a list of words
        String[] wordList = makeWordList(input);

        //Creates the crossword grid
        char[][] crosswordGrid = readCrossword(getInputFile(input));

        //Creates an array to store the solution
        char[][] solutionGrid = new char[crosswordGrid.length][crosswordGrid[0].length];

        // Start of the execution timer
        double startTime = System.nanoTime(); 
        
        for (String word : wordList) { //for each word
            wordLoop:
            for (int i = 0; i < crosswordGrid.length; i++) { //for each column
                for (int j = 0; j < crosswordGrid[0].length; j++) { //for each row
                    if (crosswordGrid[i][j] == word.charAt(0)) { // if the character at a given position match the first letter of the word
                        for (int angle = 0; angle < 8; angle++) { // for each angle
                            try {
                                for (int letter = 0; letter < word.length(); letter++){ //Moves forward and checks if the it matches the word
                                    //If it does not match throw an error
                                    if (crosswordGrid[i + (directionMap.get(angle)[1] * letter)][j + (directionMap.get(angle)[0] * letter)] != word.charAt(letter)){
                                        throw new Exception();
                                    }
                                }

                                //If the maches it is found and writen to the solution grid
                                for (int letter = 0; letter < word.length(); letter++){
                                    solutionGrid[i + (directionMap.get(angle)[1] * letter)][j + (directionMap.get(angle)[0] * letter)] = word.charAt(letter);
                                }
                                //Moves on to the next word
                                break wordLoop;
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }
        }

        // End of the execution timer
        double stopTime = System.nanoTime();

        //Displays the window
        new CrosswordSolver(solutionGrid, crosswordGrid,(stopTime-startTime) / 1000000);
    }

    //The visuals output of the file
    public CrosswordSolver(char[][] solutionGrid, char[][] crosswordGrid, double executionTime) { 
        //Sets the tiles to the run time of the file
        setTitle("Wordsearch Solver (Executed in " + Double.toString(executionTime) + "ms)");

        //Sets the size of the window
        setSize(400, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Creates the grid to display the wordsearch on
        setLayout(new GridLayout(solutionGrid.length,solutionGrid[0].length));

        //Fills the grid with the cell class
        for (int i = 0; i < solutionGrid.length; i++) {
            for (int j = 0; j < solutionGrid[0].length; j++) {
                add(new Cell(i, j, crosswordGrid, solutionGrid).element);
            }
        }
    }

    /*
     * Make Word List
     * Converts a txt file into a array of strings
     * @param input a scanner reading the terminal
     * @returns String[] a list of strings with each element being a diffrent word
     */
    public static String[] makeWordList(Scanner input) {
        //A scanner reading the input file
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

                //Checks if there are more than 10 words and throws an error if there is
                if (wordList.size() > 10) {
                    throw new Exception();
                }

            //Displays an error message if an exception is thrown
            } catch (Exception e) {
                System.out.println("Invalid Input File Try Again");
                inputFile = getInputFile(input);
                wordList.clear();
            }
        }
            // Returns the list of words
            String[] returnList = new String[wordList.size()];
            return wordList.toArray(returnList);
       
    }
    
    /*
     * Get Input Files
     * Returnes a scanner that reads the inputed file
     * @param input a scanner reading the terminal
     * @returns Scanner scanner that reads the inputed file
     */
    public static Scanner getInputFile(Scanner input){
        //Loops until a valid input is given
        while (true) {
            System.out.print("\nEnter The Input File Name: ");
        
            try {
                //returns a scanner reading the inputed line
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

//A cell class used to fill each square of the grid
class Cell {
    //The contents of each square of the grid
    JLabel element;

    public Cell(int row,int column, char[][] wordSearch, char[][] solutionGrid) {
        this.element = new JLabel(Character.toString(wordSearch[row][column]).toUpperCase(), SwingConstants.CENTER);
        if (solutionGrid[row][column] != '\u0000') {
            element.setBackground(new Color(205,231,202));
            element.setForeground(new Color(114,131,112));
            element.setText("<html><b>"+Character.toString(wordSearch[row][column]).toUpperCase()+"</b></html>"); //Bold
        } 
        else {
            element.setBackground(new Color(114,131,112));
            element.setForeground(new Color(205,231,202));
        }
        element.setBorder(BorderFactory.createLineBorder(new Color(75,81,74),2));
        element.setOpaque(true);

    }
}
