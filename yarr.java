import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class yarr {
    int sizeX;
    int sizeY; 
    public static void init(){
        yarr y = new yarr();
        
        ArrayList<String> wordList = new ArrayList<>();
        try {
            // Open the file
            Scanner fileScanner = new Scanner(new File("data.txt"));

            // Read each line and add it to the ArrayList
            while (fileScanner.hasNextLine()) {
                wordList.add(fileScanner.nextLine());
            }
            fileScanner.close();

            y.sizeX = Integer.parseInt(wordList.get(0));
            y.sizeY = Integer.parseInt(wordList.get(1));
            

            for(int i = 0; i<wordList.size(); i++){

            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }
    public static boolean isValidWord(String word){
        if ( word.length() >= 4 || word.length()<= 8){
            return true;
        }else{
            return false;
        }
    }
    public static boolean isValidPosition(int x, int y, int word){


    }

    public static void main(String[] args) {
        init();
        
    }
}