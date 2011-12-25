package ru.ifmo.pp.bank;

/**
 * Event about bank account modification.
 */
public class ChangeEvent {

	/**
	 * Version of the snapshot.
	 */
	private long version;

	/**
	 * Account number.
	 */
	private int account;

	/**
	 * The difference between the old and new amounts of money.
	 */
	private long difference;

	/**
	 * Creates a new event.
	 * 
	 * @param version
	 *            current snapshot version
	 * @param account
	 *            account number
	 * @param difference
	 *            deposit change
	 */
	public ChangeEvent(long version, int account, long difference) {
		this.version = version;
		this.account = account;
		this.difference = difference;
	}

	/**
	 * Returns snapshot version.
	 * 
	 * @return snapshot version
	 */
	public long getVersion() {
		return version;
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
