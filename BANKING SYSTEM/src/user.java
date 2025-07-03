import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class user {
    Connection connection;
    Scanner scanner;
    //constructor call-
    public user(Connection connection, Scanner scanner) throws SQLException {
        this.connection=connection;
        this.scanner=scanner;
    }
    //register-
    public void register() {
        try{
            String query="insert into user (full_name,email,password) values (?,?,?)";
            System.out.print("Full_Name:");
            String name=scanner.next();
            scanner.nextLine();
            System.out.print("Email:");
            String email=scanner.next();
            scanner.nextLine();
            System.out.print("Password:");
            String password=scanner.next();
            scanner.nextLine();
            boolean check=user_exist(email);
            if(check){
                System.out.println("User Already Exists for this email address!");
                return;
            }
            try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
                preparedStatement.setString(1,name);
                preparedStatement.setString(2,email);
                preparedStatement.setString(3,password);
                int rowsAffected= preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    System.out.println("REGISTRATION SUCCESSFUL!");
                }else{
                    System.out.println("REGISTRATION FAILED!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //check user exist-
    private boolean user_exist(String email)  {
        String query="SELECT * FROM USER WHERE EMAIL=?";
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
    //login-
    public String login(){          //email return krega use throughout use krenge
        try{
            String query="Select * From User Where Email=? and Password=?";
            System.out.print("Email:");
            String email=scanner.next();
            scanner.nextLine();
            System.out.print("Password:");
            String password=scanner.next();
            scanner.nextLine();
            try(PreparedStatement preparedStatement= connection.prepareStatement(query)){
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password);
                ResultSet rs= preparedStatement.executeQuery();
                if(rs.next()){
                    return email;
                }else{
                    return null;
                }
            }
             } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       return null;
    }

}

