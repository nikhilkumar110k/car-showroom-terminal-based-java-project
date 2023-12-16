package carshowroomclient;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;


import validation.Validation;

public class CarShowroomClient {

    static Scanner scanner = new Scanner(System.in);
   // private static final String PROFIT_FILE = "customer_bill.txt";


    public static void clientMenu(Statement stmt) throws SQLException {
        System.out.println("Client Menu:");
        System.out.println("1. View all showrooms");
        System.out.println("2. View all cars in a showroom");
        System.out.println("3. Book a car");
        System.out.println("4. Back to main menu");
        System.out.print("Choose an option: ");
        String choice = scanner.next();
    while(true){
        switch (choice) {
            case "1":
                viewAllShowrooms(stmt);
                break;
            case "2":
                viewCarsInShowroom(stmt);
                break;
            case "3":
                bookCar(stmt);
                break;
            case "4":
               return;
            default:
                System.out.println("Invalid option. Please try again.");
        }
       break;
       }
    }

    private static void viewAllShowrooms(Statement stmt) throws SQLException {
        
        String sql = "SELECT * FROM allshowrooms";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("All Showrooms:");
        while (rs.next()) {
             System.out.println("------------------------");
            System.out.println("Showroom Name: " + rs.getString("showroom_name"));
            System.out.println("Showroom Location: " + rs.getString("showroom_location"));
            System.out.println("------------------------");
        }
    }

    private static void viewCarsInShowroom(Statement stmt) throws SQLException {
        Validation validation = new Validation();
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
        if (checkShowroomExists(stmt, showroomName)) {

        String sql = "SELECT * FROM " + showroomName;
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("Cars in Showroom '" + showroomName + "':");
        while (rs.next()) {
            System.out.println("Car ID: " + rs.getInt("car_id"));
            System.out.println("Car Name: " + rs.getString("car_name"));
            System.out.println("Model No: " + rs.getString("model_no"));
            System.out.println("Price: " + rs.getDouble("price"));
            System.out.println("------------------------");
        }
    }
    else{
        System.out.println("showroom doesn't exists");
    }
    }

    private static void bookCar(Statement stmt) throws SQLException {
        Validation validation = new Validation();
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
    
        if (checkShowroomExists(stmt, showroomName)) {
            System.out.print("Enter car ID to book: ");
            String carId = validation.validatePassword();

            System.out.print("Enter your name: ");
            String custname = validation.validateUsername();
    
            String getCarPriceSql = "SELECT * FROM " + showroomName + " WHERE car_id = " + carId;
            ResultSet priceResult = stmt.executeQuery(getCarPriceSql);
             
            if (priceResult.next()) {
                double carPrice = priceResult.getDouble("price");
                String carname = priceResult.getString("car_name");
                String modelnumber = priceResult.getString("model_no");
    
                // Update the profit in the allshowroomprofit table
                String updateProfitSql = "UPDATE allshowroomprofit SET profit = profit + " + carPrice +
                                         " WHERE showroom_name = '" + showroomName + "'";
                int rowsAffected = stmt.executeUpdate(updateProfitSql);
    
                if (rowsAffected > 0) {
                    // Remove the booked car from the showroom table
                    String removeCarSql = "DELETE FROM " + showroomName + " WHERE car_id = " + carId;
                    stmt.executeUpdate(removeCarSql);
    
                    System.out.println("Car booked successfully.");
                    
                    long timestamp = System.currentTimeMillis();
                    String filename = "booking_" + timestamp + ".txt";
                
                    // Write the booking details to the unique file
                    writeProfitToFile("CUSTOMER NAME: " + custname, filename);
                    writeProfitToFile("BOOKED CAR NAME AND MODEL NUMBER: " + carname + " " + modelnumber, filename);
                    writeProfitToFile("BOOKED CAR PRICE: " + carPrice, filename);
                    writeProfitToFile("CAR BOOKED FROM SHOW ROOM: " + showroomName, filename);
                } else {
                    System.out.println("Car booking failed.");
                }
            } else {
                System.out.println("Car ID not found in the showroom.");
            }
        } else {
            System.out.println("Showroom does not exist.");
        }
    }
    
    

    private static boolean checkShowroomExists(Statement stmt, String showroomName) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM allshowrooms WHERE showroom_name = '" + showroomName + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt("count") > 0;
    }

    private static void writeProfitToFile(String data, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
            writer.write("-------------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}


