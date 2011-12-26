package ru.ifmo.pp.bank;

import java.util.List;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Snapshot data structure.
 */
public class Snapshot {

	private final boolean isMutable;
	/**
	 * Snapshot version.
	 */
	private long version;

	/**
	 * Amounts of money for every account.
	 */
	private AtomicLongArray money;

	/**
	 * New events of deposit changes.
	 */
	private List<List<UpdateEvent>> events;

	/**
	 * Creates a new bank snapshot.
	 * 
	 * @param version
	 *            snapshot version
	 * @param money
	 *            bank accounts
	 * @param events
	 *            change events
	 */
	public Snapshot(long version, AtomicLongArray money,
			List<List<UpdateEvent>> events, boolean isMutable) {
		this.version = version;
		this.money = money;
		this.events = events;
		this.isMutable = isMutable;
	}

	/**
	 * Creates a new snapshot made from the old one.
	 * 
	 * @param oldSnapshot
	 *            old one
	 * @param actualVersion
	 *            actual bank version
	 */
	public Snapshot(Snapshot oldSnapshot, long actualVersion, boolean isMutable) {
		this.money = oldSnapshot.money;
		this.events = oldSnapshot.events;
		this.version = actualVersion;
		this.isMutable = isMutable;
	}

	/**
	 * Add the specified change event to the events list.
	 * 
	 * @param account
	 *            a changed deposit number
	 * @param e
	 *            change event
	 */
	public void addEvent(int account, UpdateEvent e) {
		if (!isMutable) {
			throw new UnsupportedOperationException();
		}
		events.get(account).add(e);
	}

	/**
	 * Returns amount in account as of snapshot.
	 * 
	 * @param n
	 *            account index.
	 * @return amount in account.
	 * @throws IllegalArgumentException
	 *             when n is invalid index.
	 */
	public long getAmount(int n) {
		if (n < 0 || n >= money.length()) {
			throw new IllegalArgumentException("Invalid index: " + n);
		}
		long result = money.get(n);
		for (int i = 0; i < events.get(n).size(); ++i) {
			UpdateEvent e = events.get(n).get(i);
			if (e.getVersion() > version) {
				break;
			}
			result += e.getDifference();
		}
		return result;
	}
}
