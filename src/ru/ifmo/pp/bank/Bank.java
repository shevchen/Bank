package ru.ifmo.pp.bank;

/**
 * Bank data structure.
 */
public class Bank {
	public static final long MAX_AMOUNT = 1000000000000000L;

	/**
	 * Creates new bank instance.
	 * 
	 * @param n
	 *            the number of accounts (numbered from 0 to n-1).
	 */
	public Bank(int n) { /* todo */
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
	public long getAmount(int i) { /* todo */
		return 0;
	}

	/**
	 * Returns total amount deposited in bank.
	 */
	public long getTotalAmount() { /* todo */
		return 0;
	}

	/**
	 * Returns snapshot of all accounts in bank.
	 * 
	 * @return snapshot of amounts in all accounts.
	 */
	public Snapshot snapshot() { /* todo */
		return null;
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
	public long deposit(int i, long amount) { /* todo */
		return 0;
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
	public long withdraw(int i, long amount) { /* todo */
		return 0;
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
	public void transfer(int fromIndex, int toIndex, long amount) { /* todo */
	}
}
