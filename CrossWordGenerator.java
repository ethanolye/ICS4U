import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class CrossWordGenerator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        //Crossword grid setup
        generateCrossword(makeWordList(input),getGridDimensions(input));

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

    public static void generateCrossword(String[] wordList, int[] gridDimensions) {
        Random rng = new Random();
        char[][] crosswordGrid = new char[gridDimensions[1]][gridDimensions[0]];
        for (int currentWord = 0; currentWord < wordList.length; currentWord++) {
            //placeWord(wordList[currentWord], {rng.nextInt(crosswordGrid[0].length - 1), rng.nextInt(crosswordGrid.length - 1)});
            crosswordGrid[rng.nextInt(crosswordGrid[0].length - 1)][rng.nextInt(crosswordGrid.length - 1)] = wordList[currentWord].charAt(0);
            int direction = rng.nextInt(8);
        }
        printCrossword(crosswordGrid); //debug


    }

    public static void printCrossword(char[][] crosswordGrid) {
        for (int i = 0; i < crosswordGrid.length; i++) {
            for (int j = 0; j < crosswordGrid.length; j++) {
                if (crosswordGrid[i][j] == '\u0000') {
                    System.out.print("-"); // debug for empty spaces
                }
                System.out.print(crosswordGrid[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static void placeWord(String word, int[] position) {
        
    }
}