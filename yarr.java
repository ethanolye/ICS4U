import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class yarr {
    int sizeX;
    int sizeY;

    public static void init() {
        yarr y = new yarr();

        try {
            // Open the file
            Scanner fileScanner = new Scanner(new File("input.txt"));

            // Read the first two lines as numbers
            if (!fileScanner.hasNextLine()) {
                System.out.println("Insufficient data in the file.");
                
            }
            String line1 = fileScanner.nextLine();

            if (!fileScanner.hasNextLine()) {
                System.out.println("Insufficient data in the file.");
                
            }
            String line2 = fileScanner.nextLine();

            try {
                y.sizeX = Integer.parseInt(line1);
                y.sizeY = Integer.parseInt(line2);
                System.out.println("X: " + y.sizeX + " Y: " + y.sizeY);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in the file.");
                return;
            }

            fileScanner.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }
    }

    public static boolean isValidWord(String word) {
        return word.length() >= 4 && word.length() <= 8;
    }

    public static boolean isValidPosition(int x, int y, int word) {
        // TODO: Implement this method
        return false;
    }

    public static boolean isValidSize(int x, int y) {
        return x > 10 && x < 20 && y > 10 && y < 20;
    }

    public static void main(String[] args) {
        init();
    }
}