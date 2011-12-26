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
	 * Returns snapshot version.
	 * 
	 * @return snapshot version
	 */
	public long getVersion() {
		return version;
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
