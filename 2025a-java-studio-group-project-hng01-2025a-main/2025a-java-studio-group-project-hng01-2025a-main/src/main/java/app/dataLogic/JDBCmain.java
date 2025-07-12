package app.dataLogic;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCmain {
    public static final String DATABASE = "jdbc:sqlite:database/database.db";
    public static final String DATABASE_Member = "jdbc:sqlite:database/Member.db";

    /**
     * This creates a JDBC Object so we can keep talking to the database
     */
    public JDBCmain() {
        System.out.println("Created JDBC Connection Object");
    }

    public static Connection getConnection() {
        try {
            // Adjust the path as needed for your environment
            String url = "jdbc:sqlite:database/database.db";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
