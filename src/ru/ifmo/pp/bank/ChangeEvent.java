package ru.ifmo.pp.bank;

/**
 * Event of bank account modification.
 */
public class ChangeEvent {

	/**
	 * Version of the bank at the moment this change happened.
	 */
	private long version;

	/**
	 * The difference between the old and new amounts of money.
	 */
	private long difference;

	/**
	 * Creates a new event.
	 * 
	 * @param version
	 *            current snapshot version
	 * @param difference
	 *            deposit change
	 */
	public ChangeEvent(long version, long difference) {
		this.version = version;
		this.difference = difference;
	}

	/**
	 * Returns a version of the bank at the moment this change happened.
	 * 
	 * @return bank version
	 */
	public long getVersion() {
		return version;
	}

	/**
	 * Returns the deposit amount difference.
	 * 
	 * @return the difference
	 */
	public long getDifference() {
		return difference;
	}
}
