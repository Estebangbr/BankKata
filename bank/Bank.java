package bank;


import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Bank {

    /*
        Strings de connection à la base postgres
     */
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";



    private static final String TABLE_NAME = "accounts";

    private Connection c;

    public Bank() {
        initDb();

        // TODO
            createTableAccount();

    }

    private void initDb() {
        try {
            Class.forName(JDBC_DRIVER);
            c = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            System.out.println("Opened database successfully");

            // TODO Init DB

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }


    public void closeDb() {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Could not close the database : " + e);
        }
    }




    void createTableAccount() {
        try (Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE " + TABLE_NAME + "(\n" +
                    "name VARCHAR(120) NOT NULL,\n" +
                    "balance INT NOT NULL,\n" +
                    "threshold INT NOT NULL,\n" +
                    "locked BOOLEAN NOT NULL)");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    void dropAllTables() {
        try (Statement s = c.createStatement()) {
            s.executeUpdate(
                    "DROP table "+TABLE_NAME);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }


    private Account verifAccountExist(String name) {
        String query = "SELECT * " +
                "FROM " + TABLE_NAME + " WHERE name = '"+name+"'";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();
            if (r.next()) {
                return new Account(
                        r.getString(1), r.getInt(2),
                        r.getInt(3), r.getBoolean(4)
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public void createNewAccount(String name, int balance, int threshold) {
        // TODO

            if (threshold <= 0) {
                try (Statement s = c.createStatement()) {
                    s.executeUpdate("INSERT INTO " + TABLE_NAME + " " +
                            "(name, balance, threshold, locked) " +
                            "VALUES " +
                            "('" + name + "','" + balance + "','" + threshold + "',false)");
                } catch (Exception e) {
                    System.out.println(e.toString());
                }
            } else {
                System.out.println("The threshold is incorrect");
            }
        }

    public String printAllAccounts() {
        // TODO
        String query = "SELECT name,balance,threshold,locked " +
                "FROM " + TABLE_NAME;
        String result = "";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();

            // while there is a next row
            while (r.next()){
                result += (new Account(
                        r.getString(1), r.getInt(2),
                        r.getInt(3), r.getBoolean(4)
                )).toString();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;

    }

    public void changeBalanceByName(String name, int balanceModifier) { //Update de la bdd en changeant le solde du compte
        // TODO
        Account ac = verifAccountExist(name);
            if (ac.getLocked() == false) {
                int nouvelleBalance = ac.getBalance() + balanceModifier;
                if (nouvelleBalance >= ac.getThreshold()) {
                    ac.setBalance(nouvelleBalance);
                    try (Statement s = c.createStatement()) {
                        s.executeUpdate("UPDATE " + TABLE_NAME + " SET " +
                                " balance = '" + ac.getBalance() + "' WHERE name = '" + name + "'");
                    } catch (Exception e) {
                        System.out.println(e.toString());
                    }
                } else {
                    System.out.println("Balance < threshold");
                }
            } else {
                System.out.println("Le compte est bloqué");
            }
    }

    public void blockAccount(String name) {
        // TODO
        try
        {
            String query = "UPDATE accounts SET locked = \"1\" WHERE name = ?";
            PreparedStatement state = c.prepareStatement(query);
            state.setString(1,name);
            state.executeUpdate();

            state.close();

        }
        catch (Exception e)
        {
            System.out.println("Could not create a new account");
            e.printStackTrace();//affiche l'exception et l'état de la pile d'exécution au moment de son appel
        }
    }

    // For testing purpose
    String getTableDump() {
        String query = "select * from " + TABLE_NAME;
        String res = "";

        try (PreparedStatement s = c.prepareStatement(query)) {
            ResultSet r = s.executeQuery();

            // Getting nb colmun from meta data
            int nbColumns = r.getMetaData().getColumnCount();

            // while there is a next row
            while (r.next()){
                String[] currentRow = new String[nbColumns];

                // For each column in the row
                for (int i = 1 ; i <= nbColumns ; i++) {
                    currentRow[i - 1] = r.getString(i);
                }
                res += Arrays.toString(currentRow);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
