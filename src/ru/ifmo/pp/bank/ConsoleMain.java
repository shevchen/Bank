package ru.ifmo.pp.bank;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console driver class to test {@link Bank} methods.
 */
public class ConsoleMain {
	/**
	 * Temp bank to test {@link Bank} methods
	 */
	private static Bank bank;

	/**
	 * Try to invoke {@link Bank#deposit(int, long) deposit} method with
	 * {@link ConsoleMain#bank bank}
	 * 
	 * @param command
	 *            {"deposit", account, amount}
	 * @return true if operation is successful, false else
	 */
	private static boolean tryDeposit(String[] command) {
		if (command.length != 3) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("deposit") != 0) {
			return false;
		}
		int account;
		long amount;
		try {
			account = Integer.parseInt(command[1]);
			amount = Long.parseLong(command[2]);
		} catch (Exception e) {
			return false;
		}
		try {
			System.out.println("Successful deposit of size " + amount
					+ " to account " + account + ", total money: "
					+ bank.deposit(account, amount));
		} catch (Exception e) {
			String message = e.getMessage();
			System.out
					.println("Invalid operation: " + message == null ? "undefined"
							: message);
		}
		return true;
	}

	/**
	 * Try to invoke {@link Bank#withdraw(int, long)) withdraw} method with
	 * {@link ConsoleMain#bank bank}
	 * 
	 * @param command
	 *            {"withdraw", account, amount}
	 * @return true if operation is successful, false else
	 */
	private static boolean tryWithdraw(String[] command) {
		if (command.length != 3) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("withdraw") != 0) {
			return false;
		}
		int account;
		long amount;
		try {
			account = Integer.parseInt(command[1]);
			amount = Long.parseLong(command[2]);
		} catch (Exception e) {
			return false;
		}
		try {
			System.out.println("Successful withdraw of size " + amount
					+ " from account " + account + ", money left: "
					+ bank.withdraw(account, amount));
		} catch (Exception e) {
			String message = e.getMessage();
			System.out
					.println("Invalid operation: " + message == null ? "undefined"
							: message);
		}
		return true;
	}

	/**
	 * Try to invoke {@link Bank#transfer(int, int, long) transfer} method with
	 * {@link ConsoleMain#bank bank}
	 * 
	 * @param command
	 *            {"transfer", accountFrom, accountTo, amount}
	 * @return true if operation is successful, false else
	 */
	private static boolean tryTransfer(String[] command) {
		if (command.length != 4) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("transfer") != 0) {
			return false;
		}
		int from, to;
		long amount;
		try {
			from = Integer.parseInt(command[1]);
			to = Integer.parseInt(command[2]);
			amount = Long.parseLong(command[3]);
		} catch (Exception e) {
			return false;
		}
		try {
			bank.transfer(from, to, amount);
			System.out.println("Successful transfer of size " + amount
					+ " from account " + from + " to account " + to);
		} catch (Exception e) {
			String message = e.getMessage();
			System.out
					.println("Invalid operation: " + message == null ? "undefined"
							: message);
		}
		return true;
	}

	/**
	 * Try to invoke {@link Bank#getAmount(int) getAmount} method with
	 * {@link ConsoleMain#bank bank}
	 * 
	 * @param command
	 *            {"amount", account}
	 * @return true if operation is successful, false else
	 */
	private static boolean tryGetAmount(String[] command) {
		if (command.length != 2) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("amount") != 0) {
			return false;
		}
		int account;
		try {
			account = Integer.parseInt(command[1]);
		} catch (Exception e) {
			return false;
		}
		try {
			System.out.println("Money on deposit " + account + ": "
					+ bank.getAmount(account));
		} catch (Exception e) {
			String message = e.getMessage();
			System.out
					.println("Invalid operation: " + message == null ? "undefined"
							: message);
		}
		return true;
	}

	/**
	 * Try to invoke {@link Bank#getTotalAmount() getTotalAmount} method with
	 * {@link ConsoleMain#bank bank}
	 * 
	 * @param command
	 *            {"total"}
	 * @return true if operation is successful, false else
	 */
	private static boolean tryGetTotalAmount(String[] command) {
		if (command.length != 1) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("total") != 0) {
			return false;
		}
		System.out.println("Total money on deposits: " + bank.getTotalAmount());
		return true;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: ConsoleMain <n>");
			return;
		}
		int n;
		try {
			n = Integer.parseInt(args[0]);
		} catch (Exception e) {
			System.out.println("Usage: ConsoleMain <n>");
			return;
		}
		bank = new Bank(n);
		Scanner sc = new Scanner(System.in);
		while (true) {
			String s;
			try {
				s = sc.nextLine();
			} catch (NoSuchElementException e) {
				break;
			}
			if (s == null) {
				break;
			}
			String[] command = s.split(" ");
			if (tryDeposit(command) || tryWithdraw(command)
					|| tryTransfer(command) || tryGetAmount(command)
					|| tryGetTotalAmount(command)) {
				continue;
			}
			System.out.println("Invalid command");
		}
	}
}
