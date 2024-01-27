package com.aspiresys;

import java.util.Scanner;

public class AppWorking {

		//Encapsulation implemented
		private int accountNumber;
		private int pin;
		public void openApp()
		{
			ATMFunctions atm = new ATMFunctions();
	        Scanner scanner = new Scanner(System.in);
	        try
	        {
	        	System.out.print("\t\t\t\t\t\t! Welcome to Ibriah ATM Machine !!\n");
	            System.out.print("\t\t\t\t\t\tEnter your Account number: ");
	            accountNumber=scanner.nextInt();
	            System.out.print("\t\t\t\t\t\tEnter your PIN number: ");
	            pin=scanner.nextInt();
	        }
	        catch(Exception e)
	        {
	        	System.err.print("\t\t\t\t\t\t\t !! Enter Only Number !!\n");
	        }
        if (atm.login(accountNumber, pin)) 
        {
            int choice;
            do {
                // Display the menu
                atm.displayMenu();

                // Get user choice
                choice = scanner.nextInt();

                // Process user choice
                atm.processUserChoice(choice, accountNumber);

            } 
        while (choice != 5);
        } else {
            System.err.println("\t\t\t\t\t\t!! Login Failed Enter Correct Format  !!");
            System.out.println("\n");
            openApp();
        }
        

        // Close the database connection
        atm.closeConnection();
        scanner.close();
	}
}
