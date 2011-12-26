package ru.ifmo.pp.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Bank data structure.
 */
public class Bank {
	/**
	 * Maximum money amount per account.
	 */
	public static final long MAX_AMOUNT = 1000000000000000L;

	/**
	 * Number of accounts.
	 */
	private final int n;

	/**
	 * Current bank version.
	 */
	private volatile long actualVersion;

	/**
	 * Current money amounts for all accounts.
	 */
	private volatile AtomicLongArray money;

	/**
	 * Total money amount.
	 */
	private volatile long totalAmount;

	/**
	 * Local snapshot.
	 */
	private volatile Snapshot localSnapshot;

	/**
	 * Creates new bank instance.
	 * 
	 * @param n
	 *            the number of accounts (numbered from 0 to n-1).
	 */
	public Bank(int n) {
		if (n < 0) {
			n = 0;
		}
		this.n = n;
		actualVersion = 0;
		money = new AtomicLongArray(n);
		totalAmount = 0;
		localSnapshot = new Snapshot(actualVersion, new AtomicLongArray(n),
				createList());
	}

	/**
	 * Creates the new list containing n empty sublists.
	 * 
	 * @return the new list
	 */
	private List<List<UpdateEvent>> createList() {
		List<List<UpdateEvent>> list = new ArrayList<List<UpdateEvent>>(n);
		for (int i = 0; i < n; ++i) {
			list.add(new ArrayList<UpdateEvent>());
		}
		return list;
	}

	/**
	 * Returns current amount in account.
	 * 
	 * @param i
	 *            account index.
	 * @return amount in account.
	 * @throws IllegalArgumentException
	 *             when i is invalid index.
	 */
	public long getAmount(int i) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException("Invalid index: " + i);
		}
		return money.get(i);
	}

	/**
	 * Returns total amount deposited in bank.
	 */
	public long getTotalAmount() {
		return totalAmount;
	}

	/**
	 * Returns snapshot of all accounts in the bank.
	 * 
	 * @return snapshot of the current bank state
	 */
	public Snapshot snapshot() {
		return new Snapshot(localSnapshot, actualVersion);
	}

	/**
	 * Checks and, if necessary, updates the current snapshot.
	 */
	private void checkForUpdate() {
		if (actualVersion % n == 0) {
			AtomicLongArray newArray = new AtomicLongArray(n);
			for (int i = 0; i < n; ++i) {
				newArray.set(i, money.get(i));
			}
			localSnapshot = new Snapshot(actualVersion, newArray, createList());
		}
	}

	/**
	 * Deposits the specified amount of money to account.
	 * 
	 * @param i
	 *            account index.
	 * @param amount
	 *            amount to deposit.
	 * @return resulting amount on account.
	 * @throws IllegalArgumentException
	 *             when amount <= 0 or amount > {@link #MAX_AMOUNT}, or i is
	 *             invalid index.
	 * @throws IllegalStateException
	 *             when deposit will overflow account above {@link #MAX_AMOUNT}.
	 */
	public synchronized long deposit(int i, long amount) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException("Invalid index: " + i);
		}
		if (amount <= 0 || amount > MAX_AMOUNT) {
			throw new IllegalArgumentException("Invalid amount: " + amount);
		}
		long newValue = money.get(i) + amount;
		if (newValue > MAX_AMOUNT) {
			throw new IllegalStateException(
					"Illegal operation: money amount can't overflow "
							+ MAX_AMOUNT);
		}
		money.set(i, newValue);
		totalAmount += amount;
		long v = actualVersion + 1;
		localSnapshot.addEvent(i, new UpdateEvent(v, amount));
		actualVersion = v;
		checkForUpdate();
		return newValue;
	}

	/**
	 * Withdraws specified amount from account.
	 * 
	 * @param i
	 *            account index
	 * @param amount
	 *            amount to withdraw
	 * @return resulting amount on account.
	 * @throws IllegalArgumentException
	 *             when amount <= 0 or amount > {@link #MAX_AMOUNT}, or i is
	 *             invalid index.
	 * @throws IllegalStateException
	 *             when account does not have enough to withdraw.
	 */
	public synchronized long withdraw(int i, long amount) {
		if (i < 0 || i >= n) {
			throw new IllegalArgumentException("Invalid index: " + i);
		}
		if (amount <= 0 || amount > MAX_AMOUNT) {
			throw new IllegalArgumentException("Invalid amount: " + amount);
		}
		long newValue = money.get(i) - amount;
		if (newValue < 0) {
			throw new IllegalStateException(
					"Illegal operation: not enough money to withdraw " + amount);
		}
		money.set(i, newValue);
		totalAmount -= amount;
		long v = actualVersion + 1;
		localSnapshot.addEvent(i, new UpdateEvent(v, -amount));
		actualVersion = v;
		checkForUpdate();
		return newValue;
	}

	/**
	 * Transfers specified amount from one account to the other account.
	 * 
	 * @param fromIndex
	 *            account index to withdraw from.
	 * @param toIndex
	 *            account index to deposit to.
	 * @param amount
	 *            amount to transfer.
	 * @throws IllegalArgumentException
	 *             when amount <= 0 or amount > {@link #MAX_AMOUNT}, or account
	 *             indices are invalid.
	 * @throws IllegalStateException
	 *             when there is not enough funds in source account or too much
	 *             in target one.
	 */
	public synchronized void transfer(int fromIndex, int toIndex, long amount) {
		if (fromIndex < 0 || fromIndex >= n) {
			throw new IllegalArgumentException("Invalid index: " + fromIndex);
		}
		if (toIndex < 0 || toIndex >= n) {
			throw new IllegalArgumentException("Invalid index: " + toIndex);
		}
		if (amount <= 0 || amount > MAX_AMOUNT) {
			throw new IllegalArgumentException("Invalid amount: " + amount);
		}
		if (fromIndex == toIndex) {
			return;
		}
		long newFromValue = money.get(fromIndex) - amount;
		if (newFromValue < 0) {
			throw new IllegalStateException(
					"Illegal operation: not enough money to withdraw " + amount
							+ " from account " + fromIndex);
		}
		long newToValue = money.get(toIndex) + amount;
		if (newToValue > MAX_AMOUNT) {
			throw new IllegalStateException(
					"Illegal operation: money amount can't overflow "
							+ MAX_AMOUNT);
		}
		money.set(fromIndex, newFromValue);
		money.set(toIndex, newToValue);
		long v = actualVersion + 1;
		localSnapshot.addEvent(fromIndex, new UpdateEvent(v, -amount));
		localSnapshot.addEvent(toIndex, new UpdateEvent(v, amount));
		actualVersion = v;
		checkForUpdate();
	}
}
