package ru.ifmo.pp.bank.test;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ru.ifmo.pp.bank.Bank;
import ru.ifmo.pp.bank.Snapshot;
import ru.ifmo.pp.bank.UpdateEvent;

@RunWith(JUnit4.class)
public class BankTest {
	@Test
	public void testBankConstructor1() {
		new Bank(10);
	}

	@Test
	public void testBankConstructor2() {
		new Bank(-2);
	}

	@Test
	public void testDeposit() {
		Bank b = new Bank(3);
		Assert.assertEquals(b.deposit(0, 100), 100);
		Assert.assertEquals(b.deposit(0, 150), 250);
		Assert.assertEquals(b.deposit(1, 1), 1);
		Assert.assertEquals(b.deposit(2, Bank.MAX_AMOUNT), Bank.MAX_AMOUNT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositIllegalArgumentException1() {
		new Bank(1).deposit(-5, 100);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositIllegalArgumentException2() {
		new Bank(1).deposit(1, 500);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositIllegalArgumentException3() {
		new Bank(1).deposit(0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositIllegalArgumentException4() {
		new Bank(1).deposit(0, -10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDepositIllegalArgumentException5() {
		new Bank(1).deposit(0, Bank.MAX_AMOUNT + 1);
	}

	@Test(expected = IllegalStateException.class)
	public void testDepositIllegalStateException() {
		Bank b = new Bank(1);
		b.deposit(0, Bank.MAX_AMOUNT);
		b.deposit(0, 1);
	}

	@Test
	public void testGetAmount() {
		Bank b = new Bank(2);
		Assert.assertEquals(b.getAmount(0), 0);
		b.deposit(0, 100);
		Assert.assertEquals(b.getAmount(0), 100);
		b.deposit(0, 5);
		Assert.assertEquals(b.getAmount(0), 105);
		Assert.assertEquals(b.getAmount(1), 0);
		b.deposit(1, Bank.MAX_AMOUNT);
		Assert.assertEquals(b.getAmount(1), Bank.MAX_AMOUNT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAmountIllegalArgumentException1() {
		new Bank(1).getAmount(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAmountIllegalArgumentException2() {
		new Bank(1).getAmount(-1);
	}

	@Test
	public void testGetTotalAmount() {
		final int ACC = 10;
		Bank b = new Bank(ACC);
		Assert.assertEquals(b.getTotalAmount(), 0);
		for (int i = 0; i < ACC; ++i) {
			b.deposit(i, 2 * (i + 1));
			Assert.assertEquals(b.getTotalAmount(), (i + 1) * (i + 2));
		}
	}

	@Test
	public void testWithdraw() {
		Bank b = new Bank(2);
		b.deposit(0, 100);
		Assert.assertEquals(b.withdraw(0, 40), 60);
		Assert.assertEquals(b.getAmount(1), 0);
		Assert.assertEquals(b.withdraw(0, 60), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawIllegalArgumentException1() {
		new Bank(1).withdraw(-1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawIllegalArgumentException2() {
		new Bank(1).withdraw(1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawIllegalArgumentException3() {
		new Bank(1).withdraw(0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawIllegalArgumentException4() {
		new Bank(1).withdraw(0, -10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWithdrawIllegalArgumentException5() {
		new Bank(1).withdraw(0, Bank.MAX_AMOUNT + 1);
	}

	@Test(expected = IllegalStateException.class)
	public void testWithdrawIllegalStateException() {
		Bank b = new Bank(1);
		b.deposit(0, 1);
		b.withdraw(0, 2);
	}

	@Test
	public void testTransfer() {
		Bank b = new Bank(3);
		b.deposit(0, 100);
		b.transfer(0, 1, 70);
		Assert.assertEquals(b.getAmount(0), 30);
		Assert.assertEquals(b.getAmount(1), 70);
		Assert.assertEquals(b.getAmount(2), 0);
		b.transfer(0, 2, 30);
		Assert.assertEquals(b.getAmount(0), 0);
		Assert.assertEquals(b.getAmount(1), 70);
		Assert.assertEquals(b.getAmount(2), 30);
		b.deposit(0, Bank.MAX_AMOUNT);
		b.transfer(0, 0, Bank.MAX_AMOUNT);
		Assert.assertEquals(b.getAmount(0), Bank.MAX_AMOUNT);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException1() {
		new Bank(1).transfer(-1, 0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException2() {
		new Bank(1).transfer(1, 0, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException3() {
		new Bank(1).transfer(0, -1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException4() {
		new Bank(1).transfer(0, 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException5() {
		new Bank(1).transfer(-1, 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException6() {
		new Bank(1).transfer(0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException7() {
		new Bank(1).transfer(0, 0, -10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException8() {
		new Bank(1).transfer(0, 0, Bank.MAX_AMOUNT + 1);
	}

	@Test(expected = IllegalStateException.class)
	public void testTransferIllegalStateException1() {
		Bank b = new Bank(2);
		b.deposit(0, 1);
		b.transfer(0, 1, 2);
	}

	@Test(expected = IllegalStateException.class)
	public void testTransferIllegalStateException2() {
		Bank b = new Bank(2);
		b.deposit(0, 1);
		b.deposit(1, Bank.MAX_AMOUNT);
		b.transfer(0, 1, 1);
	}

	@Test
	public void testSnapshot1() {
		Bank b = new Bank(2);
		Snapshot s = b.snapshot();
		Assert.assertEquals(s.getAmount(0), 0);
		Assert.assertEquals(s.getAmount(1), 0);
		b.deposit(0, 5);
		Assert.assertEquals(s.getAmount(0), 0);
		b.deposit(1, 10);
		s = b.snapshot();
		Assert.assertEquals(s.getAmount(0), 5);
		Assert.assertEquals(s.getAmount(1), 10);
	}

	@Test
	public void testSnapshot2() {
		final int ACC = 10;
		Bank b = new Bank(ACC);
		for (int i = 0; i < 1000; ++i) {
			for (int j = 0; j < ACC; ++j) {
				b.deposit(j, 1);
			}
			Snapshot s = b.snapshot();
			for (int j = 0; j < ACC; ++j) {
				Assert.assertEquals(s.getAmount(j), i + 1);
			}
		}
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testSnapshotUnsupportedOperationException() {
		new Bank(1).snapshot().addEvent(0, new UpdateEvent(0L, 1L));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSnapshotIllegalArgumentException1() {
		new Bank(1).snapshot().getAmount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSnapshotIllegalArgumentException2() {
		new Bank(1).snapshot().getAmount(1);
	}

	@Test
	public void testSyncOperations1() throws Throwable {
		final Bank b = new Bank(1);
		TestRunnable[] runnables = new TestRunnable[10];
		for (int i = 0; i < 10; ++i) {
			runnables[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					for (int i = 0; i < 1000; ++i) {
						b.deposit(0, 1);
					}
				}
			};
		}
		MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);
		runner.runTestRunnables();
		Assert.assertEquals(b.getAmount(0), 10000);
	}

	@Test
	public void testSyncOperations2() throws Throwable {
		final Bank b = new Bank(2);
		TestRunnable[] runnables = new TestRunnable[40];
		for (int i = 30; i < 40; ++i) {
			runnables[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					for (int i = 0; i < 2000; ++i) {
						b.deposit(0, 1);
					}
				}
			};
		}
		for (int i = 20; i < 30; ++i) {
			runnables[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					for (int i = 0; i < 500; ++i) {
						boolean succeeded = false;
						do {
							succeeded = true;
							try {
								b.transfer(0, 1, 1);
							} catch (IllegalStateException e) {
								succeeded = false;
							}
						} while (!succeeded);
					}
				}
			};
		}
		for (int i = 10; i < 20; ++i) {
			runnables[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					for (int i = 0; i < 500; ++i) {
						boolean succeeded = false;
						do {
							succeeded = true;
							try {
								b.withdraw(0, 1);
							} catch (IllegalStateException e) {
								succeeded = false;
							}
						} while (!succeeded);
					}
				}
			};
		}
		for (int i = 0; i < 10; ++i) {
			runnables[i] = new TestRunnable() {
				@Override
				public void runTest() throws Throwable {
					for (int i = 0; i < 490; ++i) {
						boolean succeeded = false;
						do {
							succeeded = true;
							try {
								b.withdraw(1, 1);
							} catch (IllegalStateException e) {
								succeeded = false;
							}
						} while (!succeeded);
					}
				}
			};
		}
		MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);
		runner.runTestRunnables();
		Assert.assertEquals(b.getAmount(0), 10000);
		Assert.assertEquals(b.getAmount(1), 100);
	}

	@Test
	public void testSyncOperations3() throws Throwable {
		final int ACC = 1000;
		final Bank b = new Bank(ACC);
		TestRunnable[] runnables = new TestRunnable[2];
		runnables[0] = new TestRunnable() {
			@Override
			public void runTest() throws Throwable {
				for (int i = 0; i < 100; ++i) {
					for (int j = 0; j < ACC; ++j) {
						b.deposit(j, 1);
					}
				}
			}
		};
		final Snapshot[] s = new Snapshot[50];
		runnables[1] = new TestRunnable() {
			@Override
			public void runTest() throws Throwable {
				for (int i = 0; i < s.length; ++i) {
					s[i] = b.snapshot();
				}
			}
		};
		MultiThreadedTestRunner runner = new MultiThreadedTestRunner(runnables);
		runner.runTestRunnables();
		for (int i = 0; i < s.length; ++i) {
			long min = Long.MAX_VALUE;
			long max = Long.MIN_VALUE;
			for (int j = 0; j < ACC; ++j) {
				long amount = s[i].getAmount(j);
				if (amount > max) {
					max = amount;
				}
				if (amount < min) {
					min = amount;
				}
			}
			Assert.assertTrue(max >= min && max <= min + 1);
		}
	}
}
