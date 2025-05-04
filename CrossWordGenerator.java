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

        HashMap <Integer,int[]> directionMap = new HashMap<Integer,int[]>();
        directionMap.put(0, new int[]{0,-1});
        directionMap.put(1, new int[]{1,-1});
        directionMap.put(2, new int[]{1,0});
        directionMap.put(3, new int[]{1,1});
        directionMap.put(4, new int[]{0,1});
        directionMap.put(5, new int[]{-1,1});
        directionMap.put(6, new int[]{-1,0});
        directionMap.put(7, new int[]{-1,-1});

        //Crossword grid setup
        generateCrossword(makeWordList(input), getGridDimensions(input), directionMap);

        PrintWriter outputFile = makeOutputFile(input);
        outputFile.println("test"); //debug
        outputFile.close();
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
    public static PrintWriter makeOutputFile(Scanner input){
        while (true) {
            System.out.print("\nEnter The Solution File Name: ");
            input.nextLine();
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
        return gridDimensions;
    }

    public static void generateCrossword(String[] wordList, int[] gridDimensions, HashMap<Integer,int[]> directionMap) {
        char[][] crosswordGrid = new char[gridDimensions[0]][gridDimensions[1]];

        for (int currentWord = 0; currentWord < wordList.length; currentWord++) {
            crosswordGrid = Arrays.copyOf(placeWord(crosswordGrid, wordList[currentWord], directionMap), crosswordGrid.length);
        }
        
        printCrossword(crosswordGrid); //debug


    }

    public static void printCrossword(char[][] crosswordGrid) {
        for (int i = 0; i < crosswordGrid.length; i++) {
            for (int j = 0; j < crosswordGrid[0].length; j++) {
                if (crosswordGrid[i][j] == '\u0000') {
                    System.out.print("\u001B[30m"  + "-"); // debug for empty spaces
                }
                System.out.print("\u001B[32m" + crosswordGrid[i][j] + " " + "\u001B[0m");
            }
            System.out.println("");
        }
    }

    public static char[][] placeWord(char[][] crosswordGrid, String word, HashMap<Integer,int[]> directionMap) {
        
        //Create a copy of the grid
        char[][] returnGrid = Arrays.copyOf(crosswordGrid, crosswordGrid.length);

        //Create the random number generator
        Random rng = new Random();

        //Grab the direction matrix
        int[] direction = directionMap.get(rng.nextInt(8));

        //Picks a random starting space
        //TODO make it so this wont place on an already placed letter
        int[] startPosition = {rng.nextInt(crosswordGrid.length),rng.nextInt(crosswordGrid[0].length)};
        
        //Trys to place the word
        try {
            for (int letter = 0; letter < word.length(); letter++) {
                //If the space is empty or is a maching letter the space is valid
                if (returnGrid[startPosition[0]][startPosition[1]] == '\u0000' || returnGrid[startPosition[0]][startPosition[1]] == word.charAt(letter)) {
                    returnGrid[startPosition[0]][startPosition[1]] = word.charAt(letter);
                    startPosition = new int[]{startPosition[0] + direction[0], startPosition[1] + direction[1]}; //Moves
                }
                //If it is not throw an error
                else {
                    throw new Exception();
                }
            }
            //Return the updated grid
            return returnGrid;

        //Exception if the word is not placed
        } catch (Exception e) {
            System.out.println(word + " | " + Arrays.toString(startPosition) + " | " + Arrays.toString(direction)); //TODO debug
            return placeWord(crosswordGrid, word, directionMap);
        }
    }
}