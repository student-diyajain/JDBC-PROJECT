import java.sql.*;
import java.util.Scanner;

public class Accounts {

    Connection connection;
    Scanner scanner;
    public Accounts(Connection connection, Scanner scanner) {
        this.connection=connection;
        this.scanner=scanner;
    }

    //jab account exist nhi krega tab hi open krenge
    //open account
    public long open_account(String email){
        //account exist nhi krta
        if(!account_exist(email)) {
            try {
                String query = "insert into accounts (account_number,full_name,balance,email,security_pin) values (?,?,?,?,?)";
                System.out.print("Full_Name:");
                String name = scanner.next();
                scanner.nextLine();
                System.out.print("Initial Amount:");
                double balance = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Security Pin:");
                String pin = scanner.next();
                scanner.nextLine();
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    Long account_number = generateAccountNumber();
                    preparedStatement.setLong(1, account_number);
                    preparedStatement.setString(2, name);
                    preparedStatement.setDouble(3, balance);
                    preparedStatement.setString(4, email);
                    preparedStatement.setString(5, pin);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        return account_number;
                    }else{
                        throw new RuntimeException("Account Creation Failed!");
                    }
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        //account already exist krta hai
        throw new RuntimeException("Account Already Exist!");
    }

    //get account number
    public long getAccountNumber(String email){
        String query="Select * From accounts Where Email=? ";
            try{
                try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
                        preparedStatement.setString(1,email);
                        ResultSet rs= preparedStatement.executeQuery();
                        if(rs.next()){
                            return rs.getLong("account_number");
                        }
                    }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            throw new RuntimeException("Account Number Doesn't Exist!");
    }

    //generate account number
    public long generateAccountNumber(){
        String query="SELECT ACCOUNT_NUMBER FROM ACCOUNTS order by ACCOUNT_NUMBER DESC LIMIT 1;"; //max number milega
      try{
          Statement stmt= connection.createStatement();
          ResultSet rs=stmt.executeQuery(query);
          if(rs.next()){
              Long last_account_number=rs.getLong("account_number");
             return last_account_number+1;  //jo bhi number usme ek no add krke generate krenge
          }else{
            return 10000100;  //first entry
          }
      }catch(SQLException e){
          e.printStackTrace();
      }
      return 10000100;  //first entry
    }

    //account exist
    public boolean account_exist(String email)  {
        String query="SELECT * FROM ACCOUNTS WHERE EMAIL=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
