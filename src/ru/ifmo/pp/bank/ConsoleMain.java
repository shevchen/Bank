package ru.ifmo.pp.bank;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Console utility to test {@link Bank} methods.
 */
public class ConsoleMain {
	/**
	 * A bank instance to test {@link Bank} methods
	 */
	private static Bank bank;

	/**
	 * A collection of snapshots made.
	 */
	private static HashMap<String, Snapshot> snapshots;

	/**
	 * Tries to deposit some amount of money to the specified account.
	 * 
	 * @param command
	 *            {"deposit", account, amount}
	 * @return false if the command has wrong format, true otherwise
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
			System.out.println("Invalid operation: "
					+ (message == null ? "undefined" : message));
		}
		return true;
	}

	/**
	 * Tries to withdraw some amount of money from the specified account.
	 * 
	 * @param command
	 *            {"withdraw", account, amount}
	 * @return false if the command has wrong format, true otherwise
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
			System.out.println("Invalid operation: "
					+ (message == null ? "undefined" : message));
		}
		return true;
	}

	/**
	 * Tries to transfer some amount of money from one specified account to
	 * another.
	 * 
	 * @param command
	 *            {"transfer", accountFrom, accountTo, amount}
	 * @return false if the command has wrong format, true otherwise
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
			System.out.println("Invalid operation: "
					+ (message == null ? "undefined" : message));
		}
		return true;
	}

	/**
	 * Tries to get the amount of money on some deposit.
	 * 
	 * @param command
	 *            {"amount", account}
	 * @return false if the command has wrong format, true otherwise
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
			System.out.println("Invalid operation: "
					+ (message == null ? "undefined" : message));
		}
		return true;
	}

	/**
	 * Tries to get the total amount of money in the bank.
	 * 
	 * @param command
	 *            {"total"}
	 * @return false if the command has wrong format, true otherwise
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

	/**
	 * Tries to do some snapshot-related operation.
	 * 
	 * @param command
	 *            {"snapshot", ...}
	 * @return false if the command has wrong format, true otherwise
	 */
	private static boolean trySnapshot(String[] command) {
		if (command.length == 0) {
			return false;
		}
		if (command[0].toLowerCase().compareTo("snapshot") != 0) {
			return false;
		}
		return trySnapshotMake(command) || trySnapshotGetAmount(command);
	}

	/**
	 * Tries to make a new snapshot.
	 * 
	 * @param command
	 *            {"snapshot", "make", id}
	 * @return false if the command has wrong format, true otherwise
	 */
	private static boolean trySnapshotMake(String[] command) {
		if (command.length != 3) {
			return false;
		}
		if (command[1].toLowerCase().compareTo("make") != 0) {
			return false;
		}
		snapshots.put(command[2], bank.snapshot());
		System.out.println("Snapshot " + command[2]
				+ " has been successfully created.");
		return true;
	}

	/**
	 * Tries to check some deposit from the specified snapshot.
	 * 
	 * @param command
	 *            {"snapshot", "amount", id, account}
	 * @return false if the command has wrong format, true otherwise
	 */
	private static boolean trySnapshotGetAmount(String[] command) {
		if (command.length != 4) {
			return false;
		}
		if (command[1].toLowerCase().compareTo("amount") != 0) {
			return false;
		}
		int account;
		try {
			account = Integer.parseInt(command[3]);
		} catch (Exception e) {
			return false;
		}
		if (!snapshots.containsKey(command[2])) {
			System.out.println("Snapshot " + command[2] + " does not exist.");
		} else {
			try {
				System.out.println("Total money on deposit " + account
						+ " of snapshot " + command[2] + ": "
						+ snapshots.get(command[2]).getAmount(account));
			} catch (Exception e) {
				String message = e.getMessage();
				System.out.println("Invalid operation: "
						+ (message == null ? "undefined" : message));
			}
		}
		return true;
	}

	/**
	 * Prints help.
	 * 
	 * @param command
	 *            {"help"}
	 * 
	 * @return false if the command has wrong format, true otherwise
	 */
	private static boolean tryHelp(String[] command) {
		if (command.length != 1
				|| command[0].toLowerCase().compareTo("help") != 0) {
			return false;
		}
		System.out.println("Commands available:");
		System.out.println("deposit <account> <amount>,");
		System.out.println("withdraw <account> <amount>,");
		System.out.println("transfer <accountFrom> <accountTo> <amount>,");
		System.out.println("amount <account>,");
		System.out.println("total,");
		System.out.println("snapshot make <id>,");
		System.out.println("snapshot amount <id> <account>,");
		System.out.println("help,");
		System.out.println("quit.");
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
		snapshots = new HashMap<String, Snapshot>();
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
			if (command.length == 1
					&& command[0].toLowerCase().compareTo("quit") == 0) {
				break;
			}
			if (tryDeposit(command) || tryWithdraw(command)
					|| tryTransfer(command) || tryGetAmount(command)
					|| tryGetTotalAmount(command) || trySnapshot(command)
					|| tryHelp(command)) {
				continue;
			}
			System.out.println("Invalid command");
		}
	}
}
