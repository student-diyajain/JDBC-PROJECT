import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    private Connection connection;

     Doctor(Connection connection) {
        this.connection=connection;
    }

    //VIEW DOCTOR
    public void viewDoctor(){
        try{
            String query="SELECT * FROM DOCTORS;";

            try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    System.out.println();
                    System.out.println("DOCTOR DETAILS:");
                    do{
                        System.out.println("=========================================================");
                        System.out.println("DOCTOR_ID:" + resultSet.getInt("DOC_ID"));
                        System.out.println("DOCTOR_NAME:" + resultSet.getString("FULL_NAME"));
                        System.out.println("SPECILIZATION:" + resultSet.getString("SPECILIZATION"));
                    }while (resultSet.next());
                }
                else {
                    System.out.println("NO DOCTOR YET!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //CHECK DOCTOR
    public boolean checkDoctor(int id){
        try{
            String query="SELECT * FROM DOCTORS WHERE DOC_ID=?;";

            try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                preparedStatement.setInt(1,id);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    return true;
                }
                else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }




}
