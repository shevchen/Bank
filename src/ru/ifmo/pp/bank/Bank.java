package ru.ifmo.pp.bank;

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
	 * The number of changes between two consecutive snapshot refreshes.
	 */
	private final int UPDATE_PERIOD = 100;

	/**
	 * Number of accounts.
	 */
	private final int n;

	/**
	 * Current money amounts for all accounts.
	 */
	private volatile AtomicLongArray money;

	/**
	 * Total money amount.
	 */
	private volatile long totalAmount;

	/**
	 * Current snapshot.
	 */
	private volatile Snapshot curSnapshot;

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
		money = new AtomicLongArray(n);
		totalAmount = 0;
		curSnapshot = new Snapshot(money);
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
	 * Returns snapshot of all accounts in bank. Creates a new Snapshot object
	 * if and only if there were some deposit changes; never changes any data
	 * itself.
	 * 
	 * @return snapshot of amounts in all accounts.
	 */
	public Snapshot snapshot() {
		Snapshot s = curSnapshot;
		return new Snapshot(s, s.getVersion().get());
	}

	/**
	 * Checks and, if necessary, updates the current snapshot.
	 */
	private void checkForUpdate() {
		long version = curSnapshot.getVersion().get();
		if (version % UPDATE_PERIOD == 0) {
			Snapshot s = new Snapshot(curSnapshot, version);
			s.applyChanges();
			curSnapshot = s;
		}
	}

	/**
	 * Deposits specified amount to account.
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
		totalAmount += newValue;
		ChangeEvent event = new ChangeEvent(curSnapshot.getVersion()
				.incrementAndGet(), i, amount);
		curSnapshot.addEvent(event);
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
		ChangeEvent event = new ChangeEvent(curSnapshot.getVersion()
				.incrementAndGet(), i, -amount);
		curSnapshot.addEvent(event);
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
		ChangeEvent event1 = new ChangeEvent(curSnapshot.getVersion()
				.incrementAndGet(), fromIndex, -amount);
		ChangeEvent event2 = new ChangeEvent(curSnapshot.getVersion().get(),
				toIndex, amount);
		curSnapshot.addEvent(event1);
		curSnapshot.addEvent(event2);
		checkForUpdate();
	}
}
