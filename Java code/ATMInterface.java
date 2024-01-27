package com.aspiresys;
public interface ATMInterface
{
	public boolean login(int accountNumber, int pin);
	public double checkBalance(int accountNumber);
	public boolean withdraw(int accountNumber, double amount);
	public boolean deposit(int accountNumber, double amount);
	public void viewMiniStatement();
}