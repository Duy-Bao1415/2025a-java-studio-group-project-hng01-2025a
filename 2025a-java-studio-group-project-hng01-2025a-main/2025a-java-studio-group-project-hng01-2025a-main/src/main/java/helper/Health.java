package helper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class Health {
    
 private static final String DATABASE = "jdbc:sqlite:database/database.db"; //sửa cái này trước khi chạy (sửa thành đường dẫn của file database)
   private static final String CSV_FILE = "database/lga_long_term_health_conditions_by_indigenous_status_by_sex_2021.csv";


   static void process(){
      // The following arrays define the order of columns in the INPUT CSV.
      // The order of each array MUST match the order of the CSV.
      // These are specific to the given file and should be changed for each file.
      // This is a *simple* way to help you get up and running quickly wihout being confusing
      String category[] = {
         "arthritis",
         "asthma",
         "cancer",
         "dementia",
         "diabetes",
         "heartdisease",
         "kidneydisease",
         "lungcondition",
         "mentalhealth",
         "stroke",
         "other"
      };
      String status[] = {
         "indig",
         "non_indig",
         "indig_ns"
      };
      String sex[] = {
         "f",
         "m"
      };
      // The following arrays define the age ranges for each category
      // JDBC Database Object
      Connection connection = null;

      // We need some error handling.
      try {
         // Open A CSV File to process, one line at a time
         // CHANGE THIS to process a different file
         Scanner lineScanner = new Scanner(new File(CSV_FILE)); // scanner đọc từ tệp CSV_FILE

         // Read the first line of "headings"
         String header = lineScanner.nextLine();
         System.out.println("Heading row" + header + "\n");

         // Connect to JDBC database
         connection = DriverManager.getConnection(DATABASE);

         // Read each line of the CSV
         int row = 1;
         while (lineScanner.hasNext()) {
            // Always get scan by line
            String line = lineScanner.nextLine();
            
            // Create a new scanner for this line to delimit by commas (,)
            Scanner rowScanner = new Scanner(line);
            rowScanner.useDelimiter(",");

            // These indicies track which column we are up to
            int indexStatus = 0;
            int indexSex = 0;
            int indexCategory = 0;

            // Save the lga_code as we need it for the foreign key
            String lgaCode = rowScanner.next();

            // Skip lga_name
            String lgaName = rowScanner.next();

            // Fix the year
            int year = 2021; // Chinh năm tuy theo tệp CSV

            // Go through the data for the row
            // If we run out of categories, stop for safety (so the code doesn't crash)
            while (rowScanner.hasNext() && indexCategory < category.length) {
               String count = rowScanner.next();

               // Prepare a new SQL Query & Set a timeout
               Statement statement = connection.createStatement();

               // Create Insert Statement
               String query = "INSERT into Health (lga_code, lga_year, indigenous_status, sex, Category, count) VALUES ("
                              + lgaCode + ","
                              + year + ","
                              + "'" + status[indexStatus] + "',"
                              + "'" + sex[indexSex] + "',"
                              + "'" + category[indexCategory] + "',"
                              + count
                              + ")";

               // Execute the INSERT
               System.out.println("Executing: " + query);
               statement.execute(query);

               // Update indices - go to next sex
               indexSex++;
               if (indexSex >= sex.length) {
                  // Go to next status
                  indexSex = 0;
                  indexStatus++;

                  if (indexStatus >= status.length) {
                     // Go to next Category
                     indexStatus = 0;
                     indexCategory++;
                  }
               }
               row++;
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }      
   }
}
