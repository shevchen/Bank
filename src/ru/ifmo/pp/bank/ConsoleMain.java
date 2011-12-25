package ru.ifmo.pp.bank;

/**
 * Console driver class to test {@link Bank} methods.
 */
public class ConsoleMain {
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: ConsoleMain <n>");
			return;
		}
		int n = Integer.parseInt(args[0]);
		Bank bank = new Bank(n);
		// todo:
	}
}
