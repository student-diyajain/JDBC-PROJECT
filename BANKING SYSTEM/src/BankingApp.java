import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    private static final String url="jdbc:mysql://127.0.0.1:3306/banking_system";
    private static final String username="root";
    private static final String password="Diyajain@27";

    private static void exit () throws InterruptedException{
        System.out.println("THANK YOU FOR USING BANKING SYSTEM!");
        System.out.print("EXISTING SYSTEM");
        int i=5;
        while(i!=0){
            Thread.sleep(500);
            System.out.print(".");
            i--;
        }

        System.out.println();

    }
    private static void printMenu() {
        System.out.println();
        System.out.println("==========*****BANKING SYSTEM*****==========");
        System.out.println("1.Register");
        System.out.println("2.Login");
        System.out.println("3.Exit");
        System.out.println("Enter your choice:");
    }
    private static void printAccountManagerMenu() {
        System.out.println();
        System.out.println("=====================================================");
        System.out.println("1.Debit Money");
        System.out.println("2.Credit Money");
        System.out.println("3.Transfer Money");
        System.out.println("4.Check Balance");
        System.out.println("5.Log Out");
        System.out.println("=====================================================");
        System.out.println("Enter your choice:");
    }

    public static void main(String[] args)throws ClassNotFoundException,SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("CLASSES LOADED UNSUCCESSFULLY!");
            System.out.println(e.getMessage());
        }
        try{
            Connection connection= DriverManager.getConnection(url,username,password);
            Scanner scanner=new Scanner(System.in);

            //for register,login
            user user=new user(connection,scanner);
            //for account open
            Accounts accounts=new Accounts(connection,scanner);
            //for managing account-
            AccountManager accountManager=new AccountManager(connection,scanner);

            String email;
            Long account_number;

            while(true){
                printMenu();
                int choice=scanner.nextInt();
                switch(choice){
                    case 1: user.register();
                            break;
                    case 2:email= user.login();
                            if(email!=null){    //now we have email......
                                System.out.println("User Login In!");
                                //if account exist nhi krta toh create krenge
                                if(!accounts.account_exist(email)) {
                                    System.out.println("============================================");
                                    System.out.println("1. Open a Bank Account");
                                    System.out.println("2. Exit");
                                    System.out.print("Enter your choice:");
                                    System.out.println("\n============================================");
                                    int decision = scanner.nextInt();
                                    if (decision == 1) {
                                        account_number=accounts.open_account(email);
                                        System.out.println("Account Created Successfully!");
                                        System.out.println("Your Account Number is :"+account_number);
                                    } else if (decision == 2) {
                                        exit();
                                        scanner.close();
                                        return;
                                    } else {
                                        System.out.println("Invalid Choice!");
                                        return;
                                    }
                                }
                                account_number=accounts.getAccountNumber(email);
                                //after getting account number-
                                while(true) {
                                    printAccountManagerMenu();
                                    int decision2= scanner.nextInt();
                                    switch(decision2){
                                            case 1:accountManager.debit_money(account_number);
                                            break;
                                            case 2:accountManager.credit_money(account_number);
                                            break;
                                            case 3:accountManager.transfer_money(account_number);
                                            break;
                                            case 4:accountManager.check_balance(account_number);
                                            break;
                                            case 5:exit();
                                                  scanner.close();
                                                   return;
                                        default:
                                            System.out.println("Invalid choice!");


                                    }
                                }
                            }else{
                                System.out.println("Incorrect Email or Password!");
                            }
                            break;
                    case 3: exit();
                           scanner.close();
                           return;
                    default:
                        System.out.println("Invalid choice please try again");
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
          catch (InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

}