import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;
    Patient(Connection connection, Scanner scanner) {
        this.connection=connection;
        this.scanner=scanner;
    }
    //ADD PATIENT
     public void addPatient(){
        try{
            String query="INSERT INTO PATIENTS (FULL_NAME,AGE,GENDER) VALUES (?,?,?);";
            System.out.println("Enter Patient FULL_NAME:");
            String name=scanner.next();
            scanner.nextLine();
            System.out.println("Enter Patient AGE:");
            int  age= scanner.nextInt();
            scanner.nextLine();
            System.out.println("Enter Patient GENDER:");
            String gender= scanner.next();
            try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, gender);

                int rowsAffected= preparedStatement.executeUpdate();
                if(rowsAffected>0)
                    System.out.println("Patient Added Successfully!");
                else
                    System.out.println("Failed to Add Patient!");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //VIEW PATIENT
    public void viewPatient(){
        try{
            String query="SELECT * FROM PATIENTS;";

            try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    System.out.println();
                    System.out.println("PATIENT DETAILS:");
                    do{
                        System.out.println("=========================================================");
                        System.out.println("PATIENT_ID:" + resultSet.getInt("PAT_ID"));
                        System.out.println("PATIENT_NAME:" + resultSet.getString("FULL_NAME"));
                        System.out.println("PATIENT_AGE:" + resultSet.getInt("AGE"));
                        System.out.println("PATIENT_GENDER:" + resultSet.getString("GENDER"));
                    }while (resultSet.next());
                }
                else {
                    System.out.println("NO PATIENT YET!");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //CHECK PATIENT
    public boolean checkPatient(int id){
        try{
            String query="SELECT * FROM PATIENTS WHERE PAT_ID=?;";

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
