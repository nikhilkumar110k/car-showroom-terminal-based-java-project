import validation.Validation;
import carshowroomclient.CarShowroomClient;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class CarShowroom extends CarShowroomClient
 {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/CarShowroomManagement";
    static final String USER = "root";
    static final String PASS = "Nikhil@123k";

    static Scanner scanner = new Scanner(System.in);

    private static final String SHOWROOM_FILE = "new_showrooms.txt";
    Validation validation = new Validation();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        Statement stmt = null;

        Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            
            while (true) {
                System.out.println("1. Manager");
                System.out.println("2. Client");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");
                String choice = scanner.next();

                switch (choice) {
                    case "1":
                        managerMenu(stmt);
                        break;
                    case "2":
                        CarShowroomClient.clientMenu(stmt);
                        break;
                    case "3":
                        System.out.println("Exiting program.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            
            }
        }

    public static void managerMenu(Statement stmt) throws SQLException {
        Validation validation= new Validation();
        System.out.print("Enter manager password: ");
        String password = validation.validateUsername();

        if (password.equals("hello")) {
            while (true) {
                System.out.println("Manager Menu:");
                System.out.println("1. Add new showroom");
                System.out.println("2. View all showrooms");
                System.out.println("3. Add new car to showroom");
                System.out.println("4. Remove a car from showroom");
                System.out.println("5. View Profit of Showroom");
                System.out.println("6. Back to main menu");
                System.out.print("Choose an option: ");
                String choice = scanner.next();

                switch (choice) {
                    case "1":
                        addShowroom(stmt);
                        break;
                    case "2":
                        viewAllShowrooms(stmt);
                        break;
                    case "3":
                        addCarToShowroom(stmt);
                        break;
                    case "4":
                        removecarfromshowroom(stmt);
                        break;
                    case "5":
                        viewShowroomProfit(stmt);
                        break;
                    case "6":
                         return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } else {
            System.out.println("Invalid password. Access denied.");
        }
    }

    private static void addShowroom(Statement stmt) throws SQLException {
        Validation validation = new Validation();
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
        System.out.print("Enter showroom location(city): ");
        String showroomLocation = validation.validateLocation();

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + showroomName + " (" +
                "car_id INT AUTO_INCREMENT PRIMARY KEY," +
                "car_name VARCHAR(50) NOT NULL," +
                "model_no VARCHAR(20) NOT NULL," +
                "price DECIMAL(10,2) NOT NULL)";
        stmt.executeUpdate(createTableSQL);

        String insertShowroomSQL = "INSERT INTO allshowrooms (showroom_name, showroom_location) VALUES " +
                "('" + showroomName + "', '" + showroomLocation + "')";
        stmt.executeUpdate(insertShowroomSQL);

        String insertProfitSQL = "INSERT INTO allshowroomprofit (showroom_name, showroom_location, profit) VALUES " +
                "('" + showroomName + "', '" + showroomLocation + "', 0)";
        stmt.executeUpdate(insertProfitSQL);

        writeShowroomToFile("NEW SHOWROOM NAME : "+ showroomName );
        writeShowroomToFile("SHOWROOM LOCATION : "+showroomLocation );
         writeShowroomToFile("-------------------------------------------");

        System.out.println("Showroom added successfully.");
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

    private static void addCarToShowroom(Statement stmt) throws SQLException 
    {
        Validation validation = new Validation();
        while(true)
        {
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
        if (checkShowroomExists(stmt, showroomName)) 
        {
            System.out.print("Enter car brand name: ");
            String carName = validation.validateUsername();
            System.out.print("Enter model number: ");
            String modelNo = scanner.next();
            System.out.print("Enter price: ");
            String price = validation.validatePassword();
            if(price.length()<10){

            String sql = "INSERT INTO " + showroomName + " (car_name, model_no, price) VALUES " +
                    "('" + carName + "', '" + modelNo + "', " + price + ")";
            stmt.executeUpdate(sql);
            System.out.println("Car added to showroom successfully.");
        }
        else
        {
            System.out.println("Valid price for car has exceded the limit.");
        }
        }
        else
        {
            System.out.println("Showroom does not exist.");
        }
        break;
    }
    }

    private static boolean checkShowroomExists(Statement stmt, String showroomName) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM allshowrooms WHERE showroom_name = '" + showroomName + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt("count") > 0;
    }

    

    private static void viewShowroomProfit(Statement stmt) throws SQLException {
        Validation validation = new Validation();
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
    
        if (checkShowroomExists(stmt, showroomName)) {
            // Retrieve profit from the allshowroomprofit table
            String sql = "SELECT profit FROM allshowroomprofit WHERE showroom_name = '" + showroomName + "'";
            ResultSet rs = stmt.executeQuery(sql);
    
            if (rs.next()) {
                double profit = rs.getDouble("profit");
                System.out.println("Profit for Showroom '" + showroomName + "': $" + profit);
            } else {
                System.out.println("Profit information not available for Showroom '" + showroomName + "'.");
            }
        } else {
            System.out.println("Showroom does not exist.");
        }
    }
    

    


    private static void removecarfromshowroom(Statement stmt) throws SQLException {
        Validation validation = new Validation();
        System.out.print("Enter showroom name: ");
        String showroomName = validation.validateUsername();
    
        if (checkShowroomExists(stmt, showroomName)) {
            System.out.print("Enter car id to remove: ");
            int carId = scanner.nextInt();
    
            // Retrieve car name before deletion
            String carName = getCarNameById(stmt, showroomName, carId);
    
            String sql = "DELETE FROM " + showroomName + " WHERE car_id = " + carId;
            int rowsAffected = stmt.executeUpdate(sql);
    
            if (rowsAffected > 0) {
                System.out.println("Car '" + carName + "' removed from showroom successfully.");
            } else {
                System.out.println("Car with ID " + carId + " not found in showroom.");
            }
        } else {
            System.out.println("Showroom does not exist.");
        }
    }


    private static String getCarNameById(Statement stmt, String showroomName, int carId) throws SQLException {
        String sql = "SELECT car_name FROM " + showroomName + " WHERE car_id = " + carId;
        ResultSet resultSet = stmt.executeQuery(sql);
    
        if (resultSet.next()) {
            return resultSet.getString("car_name");
        } else {
            return null;
        }
    }
    

   // // // // // // // // // file handling
   private static void writeShowroomToFile(String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SHOWROOM_FILE, true))) {
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}



// private static void readShowroomFromFile() {
//         try (BufferedReader reader = new BufferedReader(new FileReader(SHOWROOM_FILE))) {
//             String line;
//             while ((line = reader.readLine()) != null) {
//                 String[] parts = line.split(",");
//                 System.out.println("Showroom Name: " + parts[0]);
//                 System.out.println("Showroom Location: " + parts[1]);
//                 System.out.println("------------------------");
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
