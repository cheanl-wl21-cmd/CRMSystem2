package services;

import java.util.Scanner;

public class Utility {
    private static Scanner scanner = new Scanner(System.in);
    
    public static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e){
            return -1;
        }
    }
    
    public static void pauseAndClear(){
        System.out.println("\n+------------------------------------------+");
        System.out.println("|      Press ENTER to continue...          |");
        System.out.println("+------------------------------------------+");
        scanner.nextLine();
        
        // "Clears" the screen by printing 50 empty lines
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static String padRight(String s, int n) {
        if (s.length() >= n) {
            return s.substring(0, n - 3) + "...";
        }
        return String.format("%-" + n + "s", s);
    }
}