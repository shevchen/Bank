package ru.ifmo.pp.bank;

/**
 * Event about bank account modification.
 */
public class ChangeEvent {
	/**
	 * Account number.
	 */
	private int account;

	/**
	 * The difference between the old and new amounts of money.
	 */
	private long difference;

	/**
	 * Creates new event.
	 */
	public ChangeEvent(int account, long difference) {
		this.account = account;
		this.difference = difference;
	}

	/**
	 * Returns the account number.
	 * 
	 * @return the account number.
	 */
	public int getAccount() {
		return account;
	}

	/**
	 * Returns the difference.
	 * 
	 * @return the difference.
	 */
	public long getDifference() {
		return difference;
	}
}
