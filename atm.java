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
    private static long amount;

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
                    
                    // Get account id
                    id = result.getInt("id");
                    System.out.println("Logged In");
                } 
                
                // Prompt Account Options
                while (true) {
                	System.out.print("Select 1 to see amount, 2 to withdraw, 3 to deposit, 4 to quit: ");
                	option = input.nextInt();
                    if (option == 4) break;
                    
                	// Amount Query
	                sql = "SELECT * FROM data WHERE id = \'" + id + "\'";
	                result = statement.executeQuery(sql);
	                
	                // Get Amount
	                if (result.next()) {
	                	amount = result.getLong("amount");
	                }
	                
	                if (option == 1) {
	                	System.out.println(amount);
	                } else if (option == 2) {
	                	// Check amount >= 50000
	                	if (amount < 50000) {
	                		System.out.println("Amount < 50000");
	                		continue;
	                	}
	                	
	                	// Prompt Withdraw Amount
	                	System.out.print("Withdraw Amount (Multiples of 10000): ");
	                	long withdraw = input.nextLong();
	                	while (withdraw < 0 || withdraw > amount || withdraw % 10000 != 0) {
	                		System.out.print("Invalid, Withdraw Amount (Multiples of 10000): ");
		                	withdraw = input.nextLong();
	                	}
	                	
	                	// Calculate Bills To Use
	                	long[] a = new long[7];
	                	int[] billValue = new int[]{0, 500000, 200000, 100000, 50000, 20000, 10000};
	                	
	                	long currentWithdrawAmount = withdraw;
	                	for (int i = 1; i <= 6; i++) {
	                		a[i] = currentWithdrawAmount / billValue[i];
	                		currentWithdrawAmount %= billValue[i];
	                	}
	                	
	                	// Update DB
	                	amount -= withdraw;
	                	sql = "UPDATE data SET amount = " + amount + " WHERE id = \'" + id + "\'";
	                	statement.executeUpdate(sql);
	            
	                	// Print Result
	                	System.out.println("Withdrawed " + withdraw + ", New Amount: " + amount);
	                	for (int i = 1; i <= 6; i++) {
	                		System.out.println(billValue[i] + ": " + a[i]);
	                	}
	                	
	                	
	                } else if (option == 3) {
	                	// Prompt Deposit Amount
	                	System.out.print("Deposite Amount: ");
	                	long deposit = input.nextLong();
	                	while (deposit < 0) {
	                		System.out.print("Invalid, Deposite Amount: ");
		                	deposit = input.nextLong();
	                	}
	                	amount += deposit;
	                	
	                	// Update DB
	                	sql = "UPDATE data SET amount = " + amount + " WHERE id = \'" + id + "\'";
	                	statement.executeUpdate(sql);
	                	
	                	// Print Result
	                	System.out.println("Deposited " + amount + ", New Amount: " + amount);
	                }
                }
                
            }
            input.close();
            
        } catch (SQLException e) {
            System.out.println("Connection Error");
            e.printStackTrace();
        }

        
    }
}
