package com.aspiresys;
import java.sql.*;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
public class ATMFunctions implements ATMInterface
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    public double balance;
    
    // Mini_Statement Package
    Map<Double,String>viewMiniStatement= new HashMap<>();
    // Constructor to initialize the database connection
    public ATMFunctions()
    {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost/atm", "root", "ibrahim123");

            // Create a statement
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void displayMenu() {
        System.out.println("\t\t\t\t\t\t====================  ATM Menus  ====================\n");
        System.out.println("\t\t\t\t\t\t1. Check Balance");
        System.out.println("\t\t\t\t\t\t2. Withdraw Money");
        System.out.println("\t\t\t\t\t\t3. Deposit Money");
        System.out.println("\t\t\t\t\t\t4. Mini Statement");
        System.out.println("\t\t\t\t\t\t5. Exit");
        System.out.print("\t\t\t\t\t\tEnter your choice (1-5): ");
    }

    // Method to handle user input
    public void processUserChoice(int choice, int accountNumber) {
        switch (choice) {
            case 1:
                balance = checkBalance(accountNumber);
                System.out.println("\t\t\t\t\t\tCurrent Balance: " + balance);
                System.out.print("\n\n");
                break;
            case 2:
                System.out.print("\t\t\t\t\t\tEnter the amount to withdraw: ");
                double withdrawAmount = new Scanner(System.in).nextDouble();
                if(withdrawAmount%100==0)
                {
                withdraw(accountNumber, withdrawAmount);
                }
                else
                {
                	System.err.print("\t\t\t\t\t\t! Enter Amount Multiple Of Hundred !");	
                }
                System.out.print("\n\n");
                break;
            case 3:
                System.out.print("\t\t\t\t\t\tEnter the amount to deposit: ");
                double depositAmount = new Scanner(System.in).nextDouble();
                if(depositAmount%100==0)
                {
                	deposit(accountNumber, depositAmount);
                }
                else
                {
                	System.err.print("\t\t\t\t\t\t\t! Please Insert Valid Amount !"+depositAmount);	
                }
                System.out.print("\n\n");
                break;
            case 4:
                System.out.println("\t\t\t\t\t\tMini Statement:");
                viewMiniStatement();
                System.out.println("\t\t\t\t\t\t\tCustomer Account No: "+accountNumber);
                balance = checkBalance(accountNumber);
                System.out.println("\t\t\t\t\t\t\t!! Available Balance: "+balance+" !!");
                System.out.print("\n\n");
                break;
            case 5:
                System.out.println("\t\t\t\t\t\t\t!!! Thankyou For Visiting Ibriah !!!");
                System.out.print("\n\n");
                closeConnection();
                System.exit(0);
                break;
            default:
                System.err.println("\t\t\t\t\t\t! Invalid Choice. Enter A Number Between 1 And 5.");
        }
    }
    
    // Method to perform login
    public boolean login(int accountNumber, int pin) {
        try {
            String query = "SELECT * FROM accounts WHERE account_number = " + accountNumber + " AND pin = " + pin;
            resultSet = statement.executeQuery(query);

            return resultSet.next(); // If there is a matching record, login is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check balance
    public double checkBalance(int accountNumber) {
        try {
            String query = "SELECT balance FROM accounts WHERE account_number = " + accountNumber;
            resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                return resultSet.getDouble("balance");
            } else {
                System.out.println("\t\t\t\t\t\tAccount not found");
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to withdraw money
    public boolean withdraw(int accountNumber, double amount) {
        try {
            double currentBalance = checkBalance(accountNumber);

            if (currentBalance >= amount) {
                double newBalance = currentBalance - amount;
                String updateQuery = "UPDATE accounts SET balance = " + newBalance + " WHERE account_number = " + accountNumber;
                statement.executeUpdate(updateQuery);
                viewMiniStatement.put(newBalance,"\t\t\t\t\t\tAmount Withdrawn");
                System.out.println("\t\t\t\t\t\tUpdated balance: "+newBalance);
                System.out.println("\t\t\t\t\t\t\t\t!!! Withdraw successfully !!!");
                return true;
            } else {
                System.err.println("\t\t\t\t\t\t\t\t!!! Insufficient Balance !!! ");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to deposit money
    public boolean deposit(int accountNumber, double amount) {
        try {
            double currentBalance = checkBalance(accountNumber);
            double newBalance = currentBalance + amount;
            String updateQuery = "UPDATE accounts SET balance = " + newBalance + " WHERE account_number = " + accountNumber;
            statement.executeUpdate(updateQuery);
            viewMiniStatement.put(newBalance,"\t\t\t\t\t\tAmount deposited");
            System.out.println("\t\t\t\t\t\tUpdated balance: "+newBalance);
            System.out.println("\t\t\t\t\t\t\t\t!!! Deposit successfully !!!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get MINI statement
    	public void viewMiniStatement()
    	{
    		for(Map.Entry<Double,String> m:viewMiniStatement.entrySet())
    		{
    			System.out.println(m.getKey()+""+m.getValue());
    		}
    	}


    // Method to close the database connection
    public void closeConnection() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (SQLException exception) {
            System.out.println("Connection is not closed");
        }
    }
}
