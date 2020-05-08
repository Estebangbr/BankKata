package bank;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import static org.junit.Assert.*;

public class BankTest {

    private static Bank b;

    @BeforeClass
    public static void init() {
        b = new Bank();
        b.dropAllTables();
    }

    @Before
    public void initDB() {
        b = new Bank();
    }

    @After
    public void dropTables() {
        b.dropAllTables();
    }

    @Test
    public void testCreateAccount() {

        b.createNewAccount("TESTY", 100, -100);
        b.createNewAccount("TESTYTEST", 200, -300);

        // Check Account created
        assertEquals(
                "TESTY | 100 | -100 | false\n" +
                        "TESTYTEST | 200 | -300 | false\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 100, -100, 0]" +
                        "[TESTYTEST, 200, -300, 0]",
                b.getTableDump());
    }

    @Test
    public void illegalCreateAccount() {

        b.createNewAccount("TESTY", 100, 100);

        // Check Account created
        assertEquals(
                "",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "",
                b.getTableDump());
    }

    @Test
    public void testChangeBalanceDeposit() {
        b.createNewAccount("TESTY", 100, -100);

        b.changeBalanceByName("TESTY", 100);

        // Check new balance
        assertEquals(
                "TESTY | 200 | -100 | false\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 200, -100, 0]",
                b.getTableDump());
    }

    @Test
    public void testChangeBalanceWithdraw() {
        b.createNewAccount("TESTY", 100, -100);

        b.changeBalanceByName("TESTY", -10);

        // Check new balance
        assertEquals(
                "TESTY | 90 | -100 | false\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 90, -100, 0]",
                b.getTableDump());
    }

    @Test
    public void testBlockAccount() {
        b.createNewAccount("TESTY", 100, -100);

        b.blockAccount("TESTY");

        // Check new balance
        assertEquals(
                "TESTY | 100 | -100 | true\n",
                b.printAllAccounts());

        // Check in DB
        assertEquals(
                "[TESTY, 100, -100, 1]",
                b.getTableDump());
    }

    @Test
    public void testChangeBalanceBlocked() {
        b.createNewAccount("TESTY", 100, -100);

        b.blockAccount("TESTY");

        b.changeBalanceByName("TESTY", 100);

        // Check new balance
        assertEquals(
                "TESTY | 100 | -100 | true\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 100, -100, 1]",
                b.getTableDump());
    }

    @Test
    public void testChangeBalanceThresholdExceeded() {
        b.createNewAccount("TESTY", 100, -100);

        b.changeBalanceByName("TESTY", -1000);

        // Check new balance
        assertEquals(
                "TESTY | 100 | -100 | false\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 100, -100, 0]",
                b.getTableDump());
    }

    @Test
    public void bigFunctionalTest() {
        b.createNewAccount("TESTY", 100, -100);
        b.createNewAccount("TESTO", 300, -200);
        b.createNewAccount("TESTU", 150, 15);

        b.changeBalanceByName("TESTY", -10);
        b.blockAccount("TESTY");
        b.changeBalanceByName("TESTY", -100);
        b.changeBalanceByName("TESTO", 20);

        // Check new balance
        assertEquals(
                "TESTY | 90 | -100 | true\n" +
                        "TESTO | 320 | -200 | false\n",
                b.printAllAccounts());


        // Check in DB
        assertEquals(
                "[TESTY, 90, -100, 1]" +
                        "[TESTO, 320, -200, 0]",
                b.getTableDump());
    }
}
