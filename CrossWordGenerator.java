import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

public class CrossWordGenerator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Random rng = new Random();
        HashMap <Integer,int[]> directionMap = new HashMap<Integer,int[]>();
        directionMap.put(0, new int[]{0,-1});
        directionMap.put(1, new int[]{1,-1});
        directionMap.put(2, new int[]{1,0});
        directionMap.put(3, new int[]{1,1});
        directionMap.put(4, new int[]{0,1});
        directionMap.put(5, new int[]{-1,1});
        directionMap.put(6, new int[]{-1,0});
        directionMap.put(7, new int[]{-1,-1});

        boolean runFile = true;

        while (runFile) {
            String[] wordList = makeWordList(input);
            int[] gridDimensions = getGridDimensions(input);

            //Crossword grid setup
            char[][] crossword = placeWord(new char[gridDimensions[0]][gridDimensions[1]], wordList, directionMap, rng, 0, 0);
        
            printCrossword(crossword, makeOutputFile(input, "Solutions"));
            printCrossword(fillGrid(crossword, rng), makeOutputFile(input, "Puzzle"));
            System.out.println("\nCrossword Generated!");

            //Promps the user to run again
            while (true) {
                System.out.print("\nRun Program Again (y/n): ");
                String response = input.nextLine();
                if (response.matches("n")){
                    runFile = false;
                    break;
                }
                else if (response.matches("y")){break;}
                else{System.out.println("Invalid Input Must Be 'y' or 'n'");
            }
            }
        }
    }

    //Returns a scanner that reads the inputed file
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

    //Makes the word list
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

    //Makes the output file
    public static PrintWriter makeOutputFile(Scanner input, String fileType){
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

    //Returns an array with the number of column and rows in the crossword grid
    public static int[] getGridDimensions(Scanner input){
        int[] gridDimensions = {0,0};
        String[] currentAxis = {"Row","Column"};

        //Loops twice once for rows and once for columns
        for (int i = 0; i < 2; i++) {
            while (true) {
                System.out.printf("Enter The number of %ss In The Crossword Grid: ", currentAxis[i]);
                try {
                    int axis = input.nextInt();
                    if (axis < 10 || axis > 20) {
                        throw new Exception();
                    }
                    gridDimensions[i] = axis;
                    break;
                } catch (Exception e) {
                    System.out.printf("Invalid %s Number Try Again\n\n", currentAxis[i]);
                    input.nextLine();
                }
            }
        }
        input.nextLine();
        return gridDimensions;
    }

    public static void printCrossword(char[][] crosswordGrid, PrintWriter file) {
        for (int i = 0; i < crosswordGrid.length; i++) {
            for (int j = 0; j < crosswordGrid[0].length; j++) {
                if (crosswordGrid[i][j] == '\u0000') {
                    file.print("·");
                }
                else {
                    file.print(crosswordGrid[i][j]);
                }
            }
            file.println("");
        }
        file.close();
    }

    public static char[][] placeWord(char[][] crosswordGrid, String[] wordList, HashMap<Integer,int[]> directionMap, Random rng, int currentWord,int attempt) {
        
        //Base case
        if (currentWord >= wordList.length) {
            return crosswordGrid;
        }

        //Create a copy of the grid
        char[][] returnGrid = Arrays.stream(crosswordGrid).map(char[]::clone).toArray(char[][]::new);

        //Grab the direction matrix
        int[] direction = directionMap.get(rng.nextInt(8));

        //Picks a random starting space
        int[] startPosition = {rng.nextInt(crosswordGrid.length), rng.nextInt(crosswordGrid[0].length)};
        
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
            return placeWord(returnGrid, wordList, directionMap, rng, currentWord+=1, 0);

        //Exception if the word is not placed
        } catch (Exception e) {
            System.out.println(wordList[currentWord] + " | " + Arrays.toString(startPosition) + " | " + Arrays.toString(direction)); //TODO debug
            return placeWord(crosswordGrid, wordList, directionMap, rng, currentWord, attempt+=1);
        }
    }

    public static char[][] fillGrid(char[][] crosswordGrid, Random rng) {
        char[][] returnGrid = Arrays.stream(crosswordGrid).map(char[]::clone).toArray(char[][]::new);
        for (int i = 0; i < returnGrid.length; i++) {
            for (int j = 0; j < returnGrid[0].length; j++) {
                if (returnGrid[i][j] == '\u0000'){returnGrid[i][j] = (char)(rng.nextInt(26) + 'a');}
            }
        }
        return returnGrid;
    }
}