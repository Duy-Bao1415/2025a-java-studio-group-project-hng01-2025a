package app.dataLogic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

// Make sure JDBCmain is imported if it's in another package
// import app.dataLogic.JDBCmain;

public class DatabaseInitializer {
    
    /**
     * Initialize the database with tables and sample data
     */
    public static void initializeDatabase() {
        Connection conn = null;
        try {
            // Get database connection
            conn = JDBCmain.getConnection();
            if (conn == null) {
                System.err.println("Failed to connect to database");
                return;
            }

            // Try loading the SQL script resource
            java.io.InputStream sqlStream = DatabaseInitializer.class.getResourceAsStream("/database/init.sql");
            if (sqlStream == null) {
                // Try without leading slash (relative path)
                sqlStream = DatabaseInitializer.class.getResourceAsStream("database/init.sql");
                if (sqlStream == null) {
                    System.err.println("Could not find /database/init.sql or database/init.sql resource");
                    // Print classloader resource paths for debugging
                    System.err.println("Current classloader: " + DatabaseInitializer.class.getClassLoader());
                    return;
                }
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(sqlStream))) {
                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                        continue;
                    }
                    sql.append(line).append(" ");
                    if (line.trim().endsWith(";")) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sql.toString());
                        } catch (Exception ex) {
                            System.err.println("SQL execution error: " + ex.getMessage());
                        }
                        sql.setLength(0);
                    }
                }
            }

            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ensure connection is closed
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    System.err.println("Error closing database connection: " + ex.getMessage());
                }
            }
        }
    }
}