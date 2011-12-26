package ru.ifmo.pp.bank;

import java.util.List;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Snapshot data structure.
 */
public class Snapshot {
	/**
	 * Snapshot version.
	 */
	private long version;

	/**
	 * Amounts of money for every account.
	 */
	private AtomicLongArray accounts;

	/**
	 * New events of deposit changes.
	 */
	private List<List<ChangeEvent>> events;

	/**
	 * Creates a new bank snapshot.
	 * 
	 * @param version
	 *            snapshot version
	 * @param accounts
	 *            bank accounts
	 * @param events
	 *            change events
	 */
	public Snapshot(long version, AtomicLongArray accounts,
			List<List<ChangeEvent>> events) {
		this.version = version;
		this.accounts = accounts;
		this.events = events;
	}

	/**
	 * Creates a new snapshot made from the old one.
	 * 
	 * @param oldSnapshot
	 *            old one
	 * @param actualVersion
	 *            actual bank version
	 */
	public Snapshot(Snapshot oldSnapshot, long actualVersion) {
		this.accounts = oldSnapshot.getAccounts();
		this.events = oldSnapshot.getEvents();
		this.version = actualVersion;
	}

	/**
	 * Retrieves all the deposits.
	 * 
	 * @return money on deposits at the last update
	 */
	public AtomicLongArray getAccounts() {
		return accounts;
	}

	/**
	 * Retrieves all changes.
	 * 
	 * @return all changes made since the last update
	 */
	public List<List<ChangeEvent>> getEvents() {
		return events;
	}

	/**
	 * Add the specified change event to the events list.
	 * 
	 * @param account
	 *            a changed deposit number
	 * @param e
	 *            change event
	 */
	public void addEvent(int account, ChangeEvent e) {
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
		if (n < 0 || n >= accounts.length()) {
			throw new IllegalArgumentException("Invalid index: " + n);
		}
		long result = accounts.get(n);
		for (int i = 0; i < events.get(n).size(); ++i) {
			ChangeEvent e = events.get(n).get(i);
			if (e.getVersion() > version) {
				break;
			}
			result += e.getDifference();
		}
		return result;
	}
}
