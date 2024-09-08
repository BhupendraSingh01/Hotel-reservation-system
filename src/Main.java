import org.w3c.dom.ls.LSOutput;

import java.sql.*;
import java.util.Scanner;

public class Main {

    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "Bhup#497";

    public static void main(String[] args) throws SQLException ,ClassNotFoundException{
       //driver loader
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        //connection
        try{
            Connection con = DriverManager.getConnection(url, username, password);
            while(true){
                Scanner sc = new Scanner(System.in);
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                System.out.println();
                System.out.println("1. New reservation");
                System.out.println("2. Check reservation");
                System.out.println("3. Get room number");
                System.out.println("4. Update reservation");
                System.out.println("5. Delete reservation");
                System.out.println("0. exit");
                System.out.println();
                System.out.println("Choose an option");
                int n = sc.nextInt();
                switch (n){
                    case 1: newRes(con, sc);
                            break;
                    case 2: viewRes(con);
                            break;
                    case 3: getRoom(con, sc);
                            break;
                            case 4: updateRes(con, sc);
                           break;
                    case 5: deleteRes(con, sc);
                            break;

                    case 0: exit();
                            sc.close();
                           return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
       }catch( InterruptedException e){
            throw new RuntimeException(e);
       }


    }
    private static void newRes(Connection con, Scanner sc){
        System.out.println("Enter guest name : ");
        String name = sc.next();
        System.out.println("Enter room no : ");
        int rn=sc.nextInt();
        System.out.println("Enter contact no. : ");
        String cn = sc.next();

        String query = "INSERT INTO reservations (name,room_num, contact_num ) VALUES('"+name+"',"+rn+",'"+ cn+"');";

        try{
            Statement stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate(query);

            if(rowsAffected > 0){
                System.out.println("Insertion successful "+ rowsAffected+ " rows affected");
            }else{
                System.out.println("Insertion failed");
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private static void viewRes(Connection con){
        String query = "SELECT * FROM reservations;";
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            System.out.println("Current reservations");
            System.out.println("+================+============+==========+=============+==================+");
            while(rs.next()){
                    int id = rs.getInt("res_id");
                    String nm = rs.getString("name");
                    int rn = rs.getInt("room_num");
                    String cn = rs.getString("contact_num");
                    String resDate = rs.getTimestamp("res_date").toString();

                     //System.out.println( "        "+id +"         " + nm +"   "+ rn +"    "+ cn +"        "+ resDate);
                System.out.println("Reservation Id      : " + id);
                System.out.println("Guest name          : "+ nm);
                System.out.println("Room number         : "+rn);
                System.out.println("Contact number      : "+cn);
                System.out.println("Date of reservation : "+resDate);
                System.out.println("---------------------------------------------------------------------");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void getRoom(Connection con, Scanner sc){
        System.out.println("Enter reservation id ");
        int id = sc.nextInt();
        String query = "select room_num from reservations"+" where res_id = " + id+";";

        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if(rs.next()){
                int rn = rs.getInt("room_num");
                System.out.println("Room number for id " + id + " is "+ rn );
            }else{
                System.out.println("Room number not found for id "+ id);
            }
        }catch (SQLException e ){
            e.printStackTrace();
        }
    }

    private static void updateRes(Connection con, Scanner sc ){
        System.out.println("Enter reservation ID : ");
        int id = sc.nextInt();

        if(!isResevation(con, id)){
            System.out.println("This ID is not registered in the system");
            return;
        }

        System.out.println("Enter new name : " );
        String nm = sc.next();
        sc.nextLine();
        System.out.println("Enter new room No. : ");
        int rn = sc.nextInt();
        System.out.println("Enter new contact No. : ");
        String cn = sc.next();
        String query= "UPDATE reservations SET name = '"+nm +"',"+"room_num = '"+ rn + "',"+"contact_num = '"+cn+ "' where res_id = " + id;

        try{
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(query);

            if(rs>0){
                System.out.println("Updation is successful");
            }else{
                System.out.println("Updation failed");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }



    }

    private static void deleteRes(Connection con, Scanner sc ){
        System.out.println("Enter reservation id :");
        int id = sc.nextInt();


        if(!isResevation(con, id )){
            System.out.println("Invalid reservation id!");
            return;
        }
try{
    String query="DELETE from reservations where res_id = "+id;

    Statement stmt = con.createStatement();
    int rs = stmt.executeUpdate(query);

    if(rs>0){
        System.out.println("successfully Deletion of reservation.");
    }else{
        System.out.println("Deletion failed.");
    }

}catch(SQLException e ){
    e.printStackTrace();
        }

    }
    public static boolean isResevation(Connection con, int id){
        try{
            String query = "select res_id from reservations where res_id = "+id;

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

           return rs.next();

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public static void exit() throws InterruptedException{
        System.out.print("Exiting system ");
        int i = 5;
        while(i>0){
            System.out.print(". ");
            Thread.sleep(500);
            i--;
        }
        System.out.println();
        System.out.println("Thanks for using Hotel reservation system.");
    }
}