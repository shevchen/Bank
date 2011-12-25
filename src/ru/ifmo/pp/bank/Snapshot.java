package ru.ifmo.pp.bank;

import java.util.List;

/**
 * Snapshot data structure.
 */
public class Snapshot {

	/**
	 * Amounts of money for every account.
	 */
	private long[] accounts;

	/**
	 * New events of account changes.
	 */
	private List<ChangeEvent> events;

	/**
	 * Returns amount in account as of snapshot.
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
}
