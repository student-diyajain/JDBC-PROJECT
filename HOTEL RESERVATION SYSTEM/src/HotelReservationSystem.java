import java.sql.*;   //import all the packages
import java.util.Scanner;   // import scanner class


public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/HOTEL_DB"; //not changeable
    private static final String username = "root"; //not changeable
    private static final String password = "Diyajain@27"; //not changeable

    public static void printMenu(){
        System.out.println("====================================HOTEL RESERVATION SYSTEM===========================================================");
        System.out.println("1.newReservation");
        System.out.println("2.checkReservation");
        System.out.println("3.getRoomNumber");
        System.out.println("4.updateReservation");
        System.out.println("5.deleteReservation");
        System.out.println("6.Exit");
        System.out.println("========================================================================================================================");
        System.out.println("Choose an option:");
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //load all drivers

        } catch (ClassNotFoundException e) {
            System.out.println("Drivers loaded unsuccessfully.");
            System.out.println(e.getMessage());
        }
        try {
           Connection connection = DriverManager.getConnection(url, username, password);  //establish connection
            while (true) {
                Scanner scanner = new Scanner(System.in);
                printMenu();
                int choose = scanner.nextInt();
                switch (choose) {
                    case 1:
                        newReservation(scanner, connection);
                        break;
                    case 2:
                        checkReservation( connection);
                        break;
                    case 3:
                        getRoomNumber(scanner, connection);
                        break;
                    case 4:
                        updateReservation(scanner, connection);
                        break;
                    case 5:
                        deleteReservation(scanner, connection);
                        break;
                    case 6:
                        exit();
                        scanner.close();;
                        return;
                    default:
                        System.out.println("Invalid Choice try again");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
         //NEW RESERVATION
         private static void newReservation(Scanner scanner,Connection connection){
            try{
                String query="INSERT INTO RESERVATIONS (ROOM_NUMBER,GUEST_NAME,CONTACT_NUMBER) VALUES (?,?,?);";
                System.out.println("Enter ROOM_NUMBER:");
                int ROOM_NUMBER=scanner.nextInt();
                System.out.println("Enter GUEST_NAME:");
                String GUEST_NAME= scanner.next();
                scanner.nextLine();
                System.out.println("Enter CONTACT_NUMBER:");
                String CONTACT_NUMBER= scanner.next();
                try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                    preparedStatement.setInt(1, ROOM_NUMBER);
                    preparedStatement.setString(2, GUEST_NAME);
                    preparedStatement.setString(3, CONTACT_NUMBER);

                    int rowsAffected= preparedStatement.executeUpdate();
                    if(rowsAffected>0)
                        System.out.println("Reservation Successful!!!");
                    else
                        System.out.println("Reservation Failed");

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            }

            //CHECK RESERVATION
         private static void checkReservation(Connection connection){
             try{
                 String query="SELECT * FROM RESERVATIONS;";

                 try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                     ResultSet resultSet = preparedStatement.executeQuery();
                     if(resultSet.next()) {
                         do{
                             System.out.println();
                             System.out.println("DETAILS OF NEW GUEST:");
                             System.out.println("RESERVATION_ID:" + resultSet.getInt("RESERVATION_ID"));
                             System.out.println("GUEST_NAME:" + resultSet.getString("GUEST_NAME"));
                             System.out.println("ROOM_NUMBER:" + resultSet.getInt("ROOM_NUMBER"));
                             System.out.println("CONTACT_NUMBER:" + resultSet.getString("CONTACT_NUMBER"));
                             System.out.println("RESERVATION_DATE:" + resultSet.getTimestamp("RESERVATION_DATE"));
                         }while (resultSet.next());
                     }
                     else {
                         System.out.println("NO BOOKING YET!!!");
                     }
                 }
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
         }

         //GET ROOMNUMBER
         private  static void getRoomNumber(Scanner scanner,Connection connection){
             try{
                 String query="SELECT ROOM_NUMBER FROM RESERVATIONS WHERE RESERVATION_ID=? AND GUEST_NAME=?;";
                 System.out.println("Enter RESERVATION_ID");
                 int RESERVATION_ID=scanner.nextInt();
                 System.out.println("Enter GUEST_NAME");
                 String GUEST_NAME= scanner.next();
                 scanner.nextLine();
                 try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                     preparedStatement.setInt(1, RESERVATION_ID);
                     preparedStatement.setString(2, GUEST_NAME);
                     ResultSet resultSet = preparedStatement.executeQuery();
                     if (resultSet.next()) {
                         System.out.println("RoomNumber for Given ReservationID " +RESERVATION_ID+"AND GuestName "+ GUEST_NAME+" is:"+resultSet.getInt("ROOM_NUMBER"));
                     }else{
                         System.out.println("Reservation is not found in given reservationId and Name");
                     }
                 }
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
    }

         //UPDATE RESERVATION
         public static void updateReservation(Scanner scanner,Connection connection) {
             try {
                 System.out.println("Enter RESERVATION_ID");
                 int RESERVATION_ID = scanner.nextInt();
                 if (!reservationExist(connection, RESERVATION_ID)) {
                     System.out.println("Reservation not found for given ID");
                     return;
                 }
                 String query = "UPDATE RESERVATIONS SET GUEST_NAME=?, ROOM_NUMBER=?, CONTACT_NUMBER=? WHERE RESERVATION_ID="+RESERVATION_ID;
                 System.out.println("Enter ROOM_NUMBER");
                 int ROOM_NUMBER = scanner.nextInt();
                 System.out.println("Enter GUEST_NAME");
                 String GUEST_NAME = scanner.next();
                 scanner.nextLine();
                 System.out.println("Enter CONTACT_NUMBER");
                 String CONTACT_NUMBER = scanner.next();

                 try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {
                     preparedStatement.setInt(1, ROOM_NUMBER);
                     preparedStatement.setString(2, GUEST_NAME);
                     preparedStatement.setString(3, CONTACT_NUMBER);

                     int rowsAffected= preparedStatement.executeUpdate();
                     if(rowsAffected>0)
                         System.out.println("Reservation Successful");
                     else
                         System.out.println("Reservation Failed");

                 }
             } catch (SQLException e) {
                 System.out.println(e.getMessage());
             }
         }

    private static boolean reservationExist(Connection connection, int reservationId) {
        try {
            String query = "SELECT RESERVATION_ID FROM RESERVATIONS WHERE RESERVATION_ID=" + reservationId;
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery(query);
                return resultSet.next();

            }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return false;

            }
    }

//DELETE RESERVATION
         private static void deleteReservation(Scanner scanner,Connection connection){
          try{
              System.out.println("Enter RESERVATION_ID");
              int RESERVATION_ID = scanner.nextInt();
              if (!reservationExist(connection, RESERVATION_ID)) {
                  System.out.println("Reservation not found for given ID");
                  return;
              }
              String query = "DELETE FROM RESERVATIONS WHERE RESERVATION_ID="+RESERVATION_ID;

              try(PreparedStatement preparedStatement= connection.prepareStatement(query)) {

                  int rowsAffected= preparedStatement.executeUpdate();
                  if(rowsAffected>0) {
                      System.out.println("Check-out Successfully!!!");
                      System.out.println("Thank you for staying with us!!");
                      System.out.println("We hope to see you again soon!");
                  }
                  else
                      System.out.println("Check-out Failed!!!");

              }

          } catch (SQLException e) {
              System.out.println(e.getMessage());;
          }
    }
        private static void exit() throws InterruptedException {
            System.out.print("Exiting System");
            int i=8;
            while(i!=0){
                Thread.sleep(500);
                System.out.print(".");
                i--;
            }
            System.out.println();
            System.out.println("Thankyou For Using Hotel Reservation System!!!");

        }




}