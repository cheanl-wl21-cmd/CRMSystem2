/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;
import java.util.Scanner;
/**
 *
 * @author PC 11
 */
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
        for (int i = 0; i < 50; i++) System.out.println();
    }
}
