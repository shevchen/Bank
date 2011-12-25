package ru.ifmo.pp.bank.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ru.ifmo.pp.bank.Bank;
import ru.ifmo.pp.bank.Snapshot;

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
		new Bank(1).transfer(0, 1, 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException3() {
		new Bank(1).transfer(0, 0, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException4() {
		new Bank(1).transfer(0, 0, -10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferIllegalArgumentException5() {
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
		b.deposit(1, 10);
		s = b.snapshot();
		Assert.assertEquals(s.getAmount(0), 5);
		Assert.assertEquals(s.getAmount(1), 10);
	}

	@Test
	public void testSnapshot2() {
		final int ACC = 10;
		Bank b = new Bank(ACC);
		for (int i = 0; i < 100000; ++i) {
			for (int j = 0; j < ACC; ++j) {
				b.deposit(j, 1);
			}
			Snapshot s = b.snapshot();
			for (int j = 0; j < ACC; ++j) {
				Assert.assertEquals(s.getAmount(j), i + 1);
			}
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSnapshotIllegalArgumentException1() {
		new Bank(1).snapshot().getAmount(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSnapshotIllegalArgumentException2() {
		new Bank(1).snapshot().getAmount(1);
	}
}
