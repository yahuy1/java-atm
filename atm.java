import java.util.Scanner;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class atm {
    
    private static String username;
    private static String password;
    private static int id;

    public static void main(String[] args) {
        String db_url = "jdbc:mysql://localhost:3306/atm";
        String db_username = "root";
        String db_password = "matkhau123";

        try {
            Connection connection = DriverManager.getConnection(db_url, db_username, db_password);
            System.out.println("Connected");

            Scanner input = new Scanner(System.in);
            while (true) {
                // Prompt options
                System.out.print("Select 1 to log in, 2 to quit: ");
                int option = input.nextInt();
                if (option == 2) break;

                // Input
                System.out.print("Username: ");
                username = input.next();
                System.out.print("Password: ");
                password = input.next();

                // Account Query
                String sql = "SELECT * FROM accounts WHERE username = \'" + username + "\'";
                Statement statement = connection.createStatement();
                ResultSet result = statement.executeQuery(sql);
                if (result.next()) {
                    if (!result.getString("password").equals(password)) {
                        System.out.println("Wrong Username/Password");
                        continue;
                    }
                    id = result.getInt("id");
                    System.out.println("Logged In");
                    break;
                } 

                
            }

            
        } catch (SQLException e) {
            System.out.println("Connection Error");
            e.printStackTrace();
        }

        
    }
}
