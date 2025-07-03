import java.sql.*;
import java.util.Scanner;

public class AccountManager {


    Connection connection;
    Scanner scanner;
    public AccountManager(Connection connection, Scanner scanner) {
        this.connection=connection;
        this.scanner=scanner;
    }

    public void debit_money(long account_number) throws SQLException {
            System.out.println("Enter Amount:");
            double amount = scanner.nextDouble();
            System.out.println("Enter SecurityPin:");
            String pin = scanner.next();
            try{
                connection.setAutoCommit(false);
            PreparedStatement Statement = connection.prepareStatement("Select * from accounts where security_pin=? and account_number=?");
            Statement.setString(1,pin);
            Statement.setLong(2, account_number);
            ResultSet rs= Statement.executeQuery();
            if(rs.next()){
                double Current_Balance=rs.getDouble("balance");
                    if(Current_Balance>=amount){
                        String dedit="update accounts set balance=balance-? where security_pin=? and account_number=?";  //minus krna
                        PreparedStatement withdrawStatement = connection.prepareStatement(dedit);
                        withdrawStatement.setDouble(1, amount);
                        withdrawStatement.setString(2, pin);
                        withdrawStatement.setLong(3, account_number);
                    int depositAffected = withdrawStatement.executeUpdate();
                     if (depositAffected > 0){
                        connection.commit();
                        System.out.println(amount + " Debit Successful!");
                          connection.setAutoCommit(true);
                     } else {
                        connection.rollback();
                        System.out.println("Transaction Failed!");
                          connection.setAutoCommit(true);
                    }
                    }else{
                        System.out.println("Insufficient Amount!");
                    }
            }else{
                System.out.println("Wrong Security Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void credit_money(long account_number) throws SQLException {
        System.out.println("Enter Amount:");
        double amount = scanner.nextDouble();
        System.out.println("Enter SecurityPin:");
        String pin = scanner.next();
        try{
            connection.setAutoCommit(false);
            PreparedStatement Statement = connection.prepareStatement("Select * from accounts where security_pin=? and account_number=?");
            Statement.setString(1,pin);
            Statement.setLong(2, account_number);
            ResultSet rs= Statement.executeQuery();
            if(rs.next()){

                    String credit ="update accounts set balance=balance+? where security_pin=? and account_number=?";  //add krna
                    PreparedStatement depositStatement = connection.prepareStatement(credit);
                    depositStatement.setDouble(1, amount);
                    depositStatement.setString(2, pin);
                    depositStatement.setLong(3, account_number);
                    int depositAffected = depositStatement.executeUpdate();
                    if (depositAffected > 0){
                        connection.commit();
                        System.out.println(amount + " Credit Successful!");
                        connection.setAutoCommit(true);
                    } else {
                        connection.rollback();
                        System.out.println("Transaction Failed!");
                        connection.setAutoCommit(true);
                    }

            }else{
                System.out.println("Wrong Security Pin!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connection.setAutoCommit(true);
    }

    public void transfer_money(long account_number) throws SQLException {
        System.out.println("Enter Reciever Account No:");
        long recieverAccountNo= scanner.nextLong();
        System.out.println("Enter Amount:");
        double amount = scanner.nextDouble();
        System.out.println("Enter SecurityPin:");
        String pin = scanner.next();
        try{
            connection.setAutoCommit(false);
            if(recieverAccountNo!=0) {
                PreparedStatement Statement = connection.prepareStatement("Select * from accounts where security_pin=? and account_number=?");
                Statement.setString(1, pin);
                Statement.setLong(2, account_number);
                ResultSet rs = Statement.executeQuery();
                if (rs.next()) {
                    double Current_Balance = rs.getDouble("balance");
                    if (Current_Balance >= amount) {

                        String withdrawQuery = "update accounts set balance=balance-? where account_number=?";
                        String depositQuery = "update accounts set balance=balance+? where account_number=?";

                        PreparedStatement withdrawStatement = connection.prepareStatement(withdrawQuery);
                        withdrawStatement.setDouble(1, amount);
                        withdrawStatement.setLong(2, account_number);
                        int withdrawsAffected = withdrawStatement.executeUpdate();
                        PreparedStatement depositStatement = connection.prepareStatement(depositQuery);
                        depositStatement.setDouble(1, amount);
                        depositStatement.setLong(2, recieverAccountNo);
                        int depositAffected = depositStatement.executeUpdate();
                        if (withdrawsAffected > 0 && depositAffected > 0) {
                            connection.commit();
                            System.out.println("Transaction Successful!");
                            connection.setAutoCommit(true);
                        } else {
                            connection.rollback();
                            System.out.println("Transaction failed!");
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Amount!");
                    }
                } else {
                    System.out.println("Wrong Security Pin!");
                }
            }else{
                System.out.println("Invalid Account Number!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void check_balance(long account_number){
        String query="Select * from accounts where security_pin=? and account_number=?";
        try{
            PreparedStatement Statement = connection.prepareStatement(query);
            System.out.println("Enter SecurityPin:");
            Statement.setString(1, scanner.next());
            Statement.setLong(2, account_number);
            ResultSet rs= Statement.executeQuery();
            if(rs.next()){
                System.out.println("Balance:"+rs.getDouble("balance"));
            }else{
                System.out.println("Wrong Security Pin!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }







}
