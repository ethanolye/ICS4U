//Imports all necessary packages 
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

public class WordSearchGenerator {
    public static void main(String[] args) {
        //Scanner for user input
        Scanner input = new Scanner(System.in);

        //Used to generate random numbers
        Random rng = new Random();

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

        //A boolean that determines if the file should run
        boolean runFile = true;

        //Loops the file until the user ends it
        while (runFile) {
            //String array with a list of words
            String[] wordList = makeWordList(input);

            //Creates a grid with dimensions entered by the user
            int[] gridDimensions = getGridDimensions(input);

            //Creates the wordsearch
            char[][] wordSearch = generateWordSearch(new char[gridDimensions[0]][gridDimensions[1]], wordList, directionMap, rng, 0);
             
            //Writes the solutions to a file
            printWordSearch(wordSearch, makeOutputFile(input, "Solutions"));

            //Writes the puzzle to a file
            printWordSearch(fillGrid(wordSearch, rng), makeOutputFile(input, "Puzzle"));

            //Displays a message to the user
            System.out.println("Word Search Generated!");

            //Promps the user to run again
            while (true) {
                System.out.print("\nRun Program Again (y/n): ");
                String response = input.nextLine();
                if (response.matches("n")){
                    runFile = false;
                    break;
                }
                else if (response.matches("y")){break;}
                else{System.out.println("Invalid Input Must Be 'y' or 'n'");}
            }
        }
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
     * Make Output File
     * Converts a txt file into a array of strings
     * @param input a scanner reading the terminal
     * @param fileType a string used to display what type of file the user should enter
     * @returns PrintWriter returns a printwriter reading the output file
     */    
    public static PrintWriter makeOutputFile(Scanner input, String fileType){
        //Loops until a valid file name is entered
        while (true) {
            System.out.printf("\nEnter The %s File Name: ",fileType);
            try {
                return new PrintWriter(input.nextLine());

            } catch (Exception e) {
                System.out.print("Invalid File Name Try Again");
                continue;
            }
        }
    }

    /*
     * Get Grid Dimensions
     * promps the user for dimensions for the grid
     * @param input a scanner reading the terminal
     * @returns int[] and intager array with each index repersenting the x and y respectively
     */   
    public static int[] getGridDimensions(Scanner input){
        int[] gridDimensions = {0,0};
        String[] currentAxis = {"Row","Column"};

        //Loops twice once for rows and once for columns
        for (int i = 0; i < 2; i++) {
            while (true) {
                System.out.printf("Enter The number of %ss In The Word Search Grid: ", currentAxis[i]);
                try {
                    //promps the user for the current axis
                    int axis = input.nextInt();

                    //returns an error if the axis is larger than 20 or smaller than 10
                    if (axis < 10 || axis > 20) {
                        throw new Exception();
                    }

                    //updates the return array
                    gridDimensions[i] = axis;
                    break;

                //Displays an error if the input is invalid
                } catch (Exception e) {
                    System.out.printf("Invalid %s Number Try Again\n\n", currentAxis[i]);
                    input.nextLine();
                }
            }
        }

        //returns the array
        input.nextLine();
        return gridDimensions;
    }

    /*
     * Print Word Search
     * writes the inputed word search to a file
     * @param wordsearchgrid the word search grid to be writen
     * @param file the file to be writen to
     */  
    public static void printWordSearch(char[][] wordSearchGrid, PrintWriter file) {
        //loops over each index of the array
        for (int i = 0; i < wordSearchGrid.length; i++) {
            for (int j = 0; j < wordSearchGrid[0].length; j++) {
                //if the space is empty print a dot
                if (wordSearchGrid[i][j] == '\u0000') {
                    file.print("·");
                }
                else {
                    //if the space has a character write it
                    file.print(wordSearchGrid[i][j]);
                }
            }
            //move to the next line
            file.println("");
        }
        //closes the scanner
        file.close();
    }

    /*
     * Generate Word Search
     * fills the word search grid with the required words and empty spaces with random characters
     * @param wordSearchGrid the word search grid to be writen
     * @param wordList a list of words to place
     * @param directionMap the direction map
     * @param rng the random number generator
     * @param currentWord the current word being placed
     * @param rng the random number generator
     * @return char[][] returns the updated word search
     */  
    public static char[][] generateWordSearch(char[][] wordSearchGrid, String[] wordList, HashMap<Integer,int[]> directionMap, Random rng, int currentWord) {
        
        //Base case
        if (currentWord >= wordList.length) {
            return wordSearchGrid;
        }

        //Create a copy of the grid
        char[][] returnGrid = Arrays.stream(wordSearchGrid).map(char[]::clone).toArray(char[][]::new);

        //Choose a direction
        int[] direction = directionMap.get(rng.nextInt(8));

        //Picks a random starting space
        int[] startPosition = {rng.nextInt(wordSearchGrid.length), rng.nextInt(wordSearchGrid[0].length)};
        
        //Trys to place the word
        try {
            for (int letter = 0; letter < wordList[currentWord].length(); letter++) {
                //If the space is empty or is a maching letter the space is valid
                if (returnGrid[startPosition[0]][startPosition[1]] == '\u0000' || returnGrid[startPosition[0]][startPosition[1]] == wordList[currentWord].charAt(letter)) {
                    returnGrid[startPosition[0]][startPosition[1]] = wordList[currentWord].charAt(letter);
                    startPosition = new int[]{startPosition[0] + direction[0], startPosition[1] + direction[1]}; //Moves
                }
                //If it is not throw an error
                else {
                    throw new Exception();
                }
            }
            //Return the updated grid
            return generateWordSearch(returnGrid, wordList, directionMap, rng, currentWord+=1);

        //Exception if the word is not placed
        } catch (Exception e) {
            return generateWordSearch(wordSearchGrid, wordList, directionMap, rng, currentWord);
        }
    }

    /*
     * Fill Grid
     * fills the empty spaces in the word search with random letters
     * @param wordSearchGrid the grid to be filled
     * @param rng the random number generator
     * @return char[][] returns the updated word search
     */
    public static char[][] fillGrid(char[][] wordSearchGrid, Random rng) {
        //Creates a copy of the grid
        char[][] returnGrid = Arrays.stream(wordSearchGrid).map(char[]::clone).toArray(char[][]::new);

        //Loops over each element in the array
        for (int i = 0; i < returnGrid.length; i++) {
            for (int j = 0; j < returnGrid[0].length; j++) {
                //If the character is blank fill it with a random letter
                if (returnGrid[i][j] == '\u0000'){returnGrid[i][j] = (char)(rng.nextInt(26) + 'a');}
            }
        }
        //returns the final grid
        return returnGrid;
    }
}