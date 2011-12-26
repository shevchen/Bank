package ru.ifmo.pp.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Snapshot data structure.
 */
public class Snapshot {
	/**
	 * Snapshot version.
	 */
	private volatile AtomicLong version;

	/**
	 * Amounts of money for every account.
	 */
	private long[] accounts;

	/**
	 * New events of account changes.
	 */
	private List<ChangeEvent> events;

	/**
	 * Creates a new bank snapshot from an existing one.
	 * 
	 * @param s
	 *            an old snapshot
	 */
	public Snapshot(Snapshot s, long version) {
		this.version = new AtomicLong(version);
		this.accounts = s.getAccounts();
		this.events = s.getEvents();
	}

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
	public Snapshot(AtomicLongArray accounts, long version) {
		this.version = new AtomicLong(version);
		this.accounts = new long[accounts.length()];
		for (int i = 0; i < this.accounts.length; ++i) {
			this.accounts[i] = accounts.get(i);
		}
		this.events = new ArrayList<ChangeEvent>();
	}

	/**
	 * Returns current version.
	 * 
	 * @return current version
	 */
	public AtomicLong getVersion() {
		return version;
	}

	/**
	 * Returns bank accounts.
	 * 
	 * @return bank accounts
	 */
	public long[] getAccounts() {
		return accounts;
	}

	/**
	 * Returns new events.
	 * 
	 * @return new change events
	 */
	public List<ChangeEvent> getEvents() {
		return events;
	}

	/**
	 * Adds the new event to the end of the list.
	 * 
	 * @param e
	 *            the new event
	 */
	public void addEvent(ChangeEvent e) {
		events.add(e);
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
		if (n < 0 || n >= accounts.length) {
			throw new IllegalArgumentException("Invalid index: " + n);
		}
		long result = accounts[n];
		for (int i = 0; i < events.size(); ++i) {
			ChangeEvent e = events.get(i);
			if (e.getVersion() > version.get()) {
				break;
			}
			if (e.getAccount() == n) {
				result += e.getDifference();
			}
		}
		return result;
	}
}
