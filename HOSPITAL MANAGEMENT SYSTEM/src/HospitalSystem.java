import java.sql.*;
import java.util.Scanner;

public class HospitalSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital";
    private static final String username = "root";
    private static final String password = "Diyajain@27";

    private static void exit() throws InterruptedException {
        System.out.println("THANK YOU FOR USING HOSPITAL MANAGEMENT SYSTEM!");
        System.out.println("TAKE CARE!");
        System.out.print("EXISTING SYSTEM");
        int i = 5;
        while (i != 0) {
            Thread.sleep(500);
            System.out.print(".");
            i--;
        }

        System.out.println();

    }

    private static void printMenu() {
        System.out.println();
        System.out.println("==========*****HOSPITAL MANAGEMENT SYSTEM*****==========");
        System.out.println("1. Add Patient");
        System.out.println("2. View Patient");
        System.out.println("3. View Doctor");
        System.out.println("4. Book Appointment");
        System.out.println("5. Exit");
        System.out.println("Enter your choice:");
    }


    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("CLASSES LOADED UNSUCCESSFULLY!");
            System.out.println(e.getMessage());
        }
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner scanner = new Scanner(System.in);

            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true) {
                printMenu();
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        patient.addPatient();
                        break;
                    case 2:
                        patient.viewPatient();
                        break;
                    case 3:
                        doctor.viewDoctor();
                        break;
                    case 4:
                        bookAppointment(doctor,patient,connection,scanner);
                        break;
                    case 5:
                        exit();
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice please try again");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void bookAppointment(Doctor doctor,Patient patient,Connection connection,Scanner scanner) {
        System.out.println("Enter Patient Id:");
        int pId=scanner.nextInt();
        System.out.println("Enter Doctor Id:");
        int dId=scanner.nextInt();
        System.out.println("Enter Appointment Date(YYYY-MM-DD):");
        String date= scanner.next();
        scanner.nextLine();
        //check kr yeh dono available hai
        if(doctor.checkDoctor(dId)&& patient.checkPatient(pId)){
            //check kr kya doctor us date ko available hai
            if(checkDoctorAvailaibility(dId,date,connection,scanner)){
                try{
                    PreparedStatement preparedStatement= connection.prepareStatement("INSERT INTO APPOINTMENTS (PATIENT_ID,DOCTOR_ID,Appointment_DATE) VALUES (?,?,?);");
                    preparedStatement.setInt(1,pId);
                    preparedStatement.setInt(2,dId);
                    preparedStatement.setString(3,date);
                    int rowsAffected=preparedStatement.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Appointment Booked!");
                    }else{
                        System.out.println("Failed to Booked Appointment!");
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor is not available on this date!");
            }
        }else{
            System.out.println("Either Doctor or Patient Doesn't Exist!");
        }
    }

    private static boolean checkDoctorAvailaibility(int dId, String date,Connection connection,Scanner scanner) {
        try{
            String query="Select Count(*) from Appointments where doctor_id=? and appointment_date=?";
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setInt(1,dId);
            preparedStatement.setString(2,date);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);//it gives no of rows
                if(count==0){   //doctor availaible h
                    return true;
                }else{
                   return false;
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}


