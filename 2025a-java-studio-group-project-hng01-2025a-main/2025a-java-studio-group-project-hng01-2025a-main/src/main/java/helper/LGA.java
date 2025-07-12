package helper;

import java.io.File;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class LGA {
    
    // Database and file paths
   private static final String DATABASE = "jdbc:sqlite:database/database.db";
   private static final String CSV_FILE = "database/lgas_2016.csv";
   private static final String CSV_FILE1 = "database/lgas_2021.csv";
  
   /**
    * Process 2016 LGA data from CSV to database
    * This method:
    * 1. Validates file existence
    * 2. Reads CSV data
    * 3. Inserts data into database with default values for missing fields
    * 4. Handles errors and provides detailed statistics
    */
   static void process2016() {
       Connection connection = null;
       PreparedStatement pstmt = null;

       try {
           // Step 1: Verify database file exists
           File dbFile = new File("database/database.db");
           if (!dbFile.exists()) {
               System.out.println("Error: Database file does not exist at: " + DATABASE);
               return;
           }

           // Step 2: Verify CSV file exists
           File csvFile = new File(CSV_FILE);
           if (!csvFile.exists()) {
               System.out.println("Error: CSV file does not exist at: " + CSV_FILE);
               return;
           }

           // Step 3: Initialize CSV reading
           Scanner lineScanner = new Scanner(csvFile);
           String header = lineScanner.nextLine();
           System.out.println("Processing 2016 CSV file with header: " + header);

           // Step 4: Establish database connection
           connection = DriverManager.getConnection(DATABASE);
           
           // Step 5: Prepare SQL statement for insertion
           String sql = "INSERT OR IGNORE INTO LGA (code, year, name, area, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
           pstmt = connection.prepareStatement(sql);

           // Step 6: Initialize counters for statistics
           int lineCount = 0;
           int successCount = 0;
           int skipCount = 0;

           // Step 7: Process each line in the CSV
           while (lineScanner.hasNext()) {
               String line = lineScanner.nextLine().trim();
               if (line.isEmpty()) {
                   continue;
               }

               try {
                   // Step 7.1: Parse CSV line
                   String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                   
                   // Step 7.2: Validate minimum required fields (code and name)
                   if (tokens.length < 2 || 
                       tokens[0].trim().isEmpty() || // code
                       tokens[1].trim().isEmpty()) { // name
                       System.out.println("Skipping line due to missing required code or name: " + line);
                       skipCount++;
                       continue;
                   }

                   // Step 7.3: Process values with defaults for missing fields
                   String lgaCode = tokens[0].trim();
                   String lgaName = tokens[1].trim();
                   // Use default values if fields are missing
                   String areaSqKm = tokens.length > 2 && !tokens[2].trim().isEmpty() ? tokens[2].trim() : "0";
                   String latitude = tokens.length > 3 && !tokens[3].trim().isEmpty() ? tokens[3].trim() : "0";
                   String longitude = tokens.length > 4 && !tokens[4].trim().isEmpty() ? tokens[4].trim() : "0";

                   // Step 7.4: Set parameters for database insertion
                   pstmt.setString(1, lgaCode);
                   pstmt.setInt(2, 2016);
                   pstmt.setString(3, lgaName);
                   pstmt.setDouble(4, Double.parseDouble(areaSqKm));
                   pstmt.setDouble(5, Double.parseDouble(latitude));
                   pstmt.setDouble(6, Double.parseDouble(longitude));

                   // Step 7.5: Execute insertion
                   pstmt.executeUpdate();
                   successCount++;
               } catch (NumberFormatException e) {
                   System.out.println("Warning: Invalid number format in line: " + line);
                   skipCount++;
               } catch (SQLException e) {
                   System.out.println("Error executing SQL for line: " + line);
                   System.out.println("Error message: " + e.getMessage());
                   skipCount++;
               }
               lineCount++;
           }

           // Step 8: Print processing statistics
           System.out.println("\n2016 Data Processing complete:");
           System.out.println("Total lines processed: " + lineCount);
           System.out.println("Successfully inserted: " + successCount);
           System.out.println("Skipped lines: " + skipCount);

       } catch (Exception e) {
           System.out.println("Error: " + e.getMessage());
           e.printStackTrace();
       } finally {
           // Step 9: Clean up resources
           try {
               if (pstmt != null) pstmt.close();
               if (connection != null) connection.close();
           } catch (SQLException e) {
               System.out.println("Error closing resources: " + e.getMessage());
           }
       }
   }

   /**
    * Process 2021 LGA data from CSV to database
    * Similar to process2016() but handles 2021 data
    */
   static void process2021() {
       Connection connection = null;
       PreparedStatement pstmt = null;

       try {
           // Step 1: Verify database file exists
           File dbFile = new File("database/database.db");
           if (!dbFile.exists()) {
               System.out.println("Error: Database file does not exist at: " + DATABASE);
               return;
           }

           // Step 2: Verify CSV file exists
           File csvFile = new File(CSV_FILE1);  // Fixed: Using correct 2021 file
           if (!csvFile.exists()) {
               System.out.println("Error: CSV file does not exist at: " + CSV_FILE1);
               return;
           }

           // Step 3: Initialize CSV reading
           Scanner lineScanner = new Scanner(csvFile);
           String header = lineScanner.nextLine();
           System.out.println("Processing 2021 CSV file with header: " + header);

           // Step 4: Establish database connection
           connection = DriverManager.getConnection(DATABASE);
           
           // Step 5: Prepare SQL statement for insertion
           String sql = "INSERT OR IGNORE INTO LGA (code, year, name, area, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
           pstmt = connection.prepareStatement(sql);

           // Step 6: Initialize counters for statistics
           int lineCount = 0;
           int successCount = 0;
           int skipCount = 0;

           // Step 7: Process each line in the CSV
           while (lineScanner.hasNext()) {
               String line = lineScanner.nextLine().trim();
               if (line.isEmpty()) {
                   continue;
               }

               try {
                   // Step 7.1: Parse CSV line
                   String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                   
                   // Step 7.2: Validate minimum required fields (code and name)
                   if (tokens.length < 2 || 
                       tokens[0].trim().isEmpty() || // code
                       tokens[1].trim().isEmpty()) { // name
                       System.out.println("Skipping line due to missing required code or name: " + line);
                       skipCount++;
                       continue;
                   }

                   // Step 7.3: Process values with defaults for missing fields
                   String lgaCode = tokens[0].trim();
                   String lgaName = tokens[1].trim();
                   // Use default values if fields are missing
                 String areaSqKm = tokens.length > 2 && !tokens[2].trim().isEmpty() ? tokens[2].trim() : "0";
                   String latitude = tokens.length > 3 && !tokens[3].trim().isEmpty() ? tokens[3].trim() : "0";
                   String longitude = tokens.length > 4 && !tokens[4].trim().isEmpty() ? tokens[4].trim() : "0";

                   // Step 7.4: Set parameters for database insertion
                   pstmt.setString(1, lgaCode);
                   pstmt.setInt(2, 2021);  // Fixed: Using correct year
                   pstmt.setString(3, lgaName);
                   pstmt.setDouble(4, Double.parseDouble(areaSqKm));
                   pstmt.setDouble(5, Double.parseDouble(latitude));
                   pstmt.setDouble(6, Double.parseDouble(longitude));

                   // Step 7.5: Execute insertion
                   pstmt.executeUpdate();
                   successCount++;
               } catch (NumberFormatException e) {
                   System.out.println("Warning: Invalid number format in line: " + line);
                   skipCount++;
               } catch (SQLException e) {
                   System.out.println("Error executing SQL for line: " + line);
                   System.out.println("Error message: " + e.getMessage());
                   skipCount++;
               }
               lineCount++;
           }

           // Step 8: Print processing statistics
           System.out.println("\n2021 Data Processing complete:");
           System.out.println("Total lines processed: " + lineCount);
           System.out.println("Successfully inserted: " + successCount);
           System.out.println("Skipped lines: " + skipCount);

       } catch (Exception e) {
           System.out.println("Error: " + e.getMessage());
           e.printStackTrace();
       } finally {
           // Step 9: Clean up resources
           try {
               if (pstmt != null) pstmt.close();
               if (connection != null) connection.close();
           } catch (SQLException e) {
               System.out.println("Error closing resources: " + e.getMessage());
           }
       }
      }
    }