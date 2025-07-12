package app.dataLogic;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;


import helper.Education_completion;
import helper.Education_level;


public class Level_2 extends JDBCmain {
   public ArrayList<healthClass> getAllHealth() { //////////////////////////////////////////// hien thi raw health data
       ArrayList<healthClass> healths = new ArrayList<healthClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);


           // Prepare a new SQL Query & Set a timeout
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);


           /*List all health, raw data and percentage */
           String query =
                   "SELECT \r\n" +
                           "    lga.name, \r\n" +
                           "    health.lga_code, \r\n" +
                           "    CASE \r\n" +
                           "        WHEN health.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
                           "        WHEN health.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
                           "        WHEN health.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
                           "        ELSE 'Unknown' \r\n" +
                           "    END AS indigenous_status, \r\n" +
                           "    health.Category, \r\n" +
                           "    SUM(health.count) AS total_count, \r\n" +
                           "    ROUND( \r\n" +
                           "        SUM(health.count) * 100.0 / SUM(SUM(health.count)) OVER (PARTITION BY health.lga_code), \r\n" +
                           "        2 \r\n" +
                           "    ) AS percentage \r\n" +
                           "FROM \r\n" +
                           "    health \r\n" +
                           "JOIN \r\n" +
                           "    lga ON health.lga_code = lga.code \r\n" +
                           "GROUP BY \r\n" +
                           "    lga.name, health.lga_code, health.indigenous_status, health.Category \r\n" +
                           "ORDER BY \r\n" +
                           "    health.lga_code;";




           ResultSet results = statement.executeQuery(query);
           // Process all of the results


           while (results.next()) {
               String name = results.getString("name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("Category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");


               healthClass health = new healthClass(name, lga_code, indi_status, category, total_counts, percentage);
               healths.add(health);
           }
           // Close the connection
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return healths;
   }


   public ArrayList<healthClass> getallhealth_order(String order) {
       ArrayList<healthClass> healths = new ArrayList<healthClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);

           // Prepare a new SQL Query & Set a timeout
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);

           // Validate sort order parameter to prevent SQL injection
           String sortDirection = "ASC";  // Default
           if (order != null && order.equalsIgnoreCase("DESC")) {
               sortDirection = "DESC";
           }

           /*List all health, raw data and percentage with ranking */
           String query =
                   "WITH RankedData AS (\r\n" +
                   "    SELECT \r\n" +
                   "        lga.name, \r\n" +
                   "        health.lga_code, \r\n" +
                   "        CASE \r\n" +
                   "            WHEN health.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
                   "            WHEN health.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
                   "            WHEN health.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
                   "            ELSE 'Unknown' \r\n" +
                   "        END AS indigenous_status, \r\n" +
                   "        health.Category, \r\n" +
                   "        SUM(health.count) AS total_count, \r\n" +
                   "        ROUND( \r\n" +
                   "            SUM(health.count) * 100.0 / SUM(SUM(health.count)) OVER (PARTITION BY health.lga_code), \r\n" +
                   "            2 \r\n" +
                   "        ) AS percentage,\r\n" +
                   "        DENSE_RANK() OVER (ORDER BY SUM(health.count) " + sortDirection + ") as rank\r\n" +
                   "    FROM \r\n" +
                   "        health \r\n" +
                   "    JOIN \r\n" +
                   "        lga ON health.lga_code = lga.code \r\n" +
                   "    GROUP BY \r\n" +
                   "        lga.name, health.lga_code, health.indigenous_status, health.Category\r\n" +
                   ")\r\n" +
                   "SELECT * FROM RankedData\r\n" +
                   "ORDER BY total_count " + sortDirection + ", rank;";

           ResultSet results = statement.executeQuery(query);
           // Process all of the results
           while (results.next()) {
               String name = results.getString("name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("Category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");
               int rank = results.getInt("rank");

               healthClass health = new healthClass(name, lga_code, indi_status, category, total_counts, percentage, rank);
               healths.add(health);
           }
           // Close the connection
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return healths;
   }


   public ArrayList<ageClass> getPopulationAge() {
       ArrayList<ageClass> ages = new ArrayList<ageClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);


           String query = 
    "SELECT \r\n" +
    "    lga.name, \r\n" +
    "    p.lga_code, \r\n" +
    "    CASE \r\n" +
    "        WHEN p.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
    "        WHEN p.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
    "        WHEN p.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
    "        ELSE 'Unknown' \r\n" +
    "    END AS indigenous_status, \r\n" +
    "    CASE \r\n" +
    "        WHEN p.age_category = '_0_4' THEN '0–4 years' \r\n" +
    "        WHEN p.age_category = '_5_9' THEN '5–9 years' \r\n" +
    "        WHEN p.age_category = '_10_14' THEN '10–14 years' \r\n" +
    "        WHEN p.age_category = '_15_19' THEN '15–19 years' \r\n" +
    "        WHEN p.age_category = '_20_24' THEN '20–24 years' \r\n" +
    "        WHEN p.age_category = '_25_29' THEN '25–29 years' \r\n" +
    "        WHEN p.age_category = '_30_34' THEN '30–34 years' \r\n" +
    "        WHEN p.age_category = '_35_39' THEN '35–39 years' \r\n" +
    "        WHEN p.age_category = '_40_44' THEN '40–44 years' \r\n" +
    "        WHEN p.age_category = '_45_49' THEN '45–49 years' \r\n" +
    "        WHEN p.age_category = '_50_54' THEN '50–54 years' \r\n" +
    "        WHEN p.age_category = '_55_59' THEN '55–59 years' \r\n" +
    "        WHEN p.age_category = '_60_64' THEN '60–64 years' \r\n" +
    "        WHEN p.age_category = '_65_yrs_ov' THEN '65+ years' \r\n" +
    "        ELSE 'Unknown' \r\n" +
    "    END AS age_category, \r\n" +
    "    SUM(p.count) AS total_count, \r\n" +
    "    ROUND( \r\n" +
    "        SUM(p.count) * 100.0 / SUM(SUM(p.count)) OVER (PARTITION BY p.lga_code), \r\n" +
    "        2 \r\n" +
    "    ) AS percentage \r\n" +
    "FROM \r\n" +
    "    Population p \r\n" +
    "JOIN \r\n" +
    "    LGA lga ON p.lga_code = lga.code AND p.lga_year = lga.year \r\n" +
    "GROUP BY \r\n" +
    "    lga.name, p.lga_code, p.indigenous_status, p.age_category \r\n" +
    "ORDER BY \r\n" +
    "    p.lga_code, p.age_category, p.indigenous_status;\r\n";


           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String age_category = results.getString("age_category");
               int count = results.getInt("total_count");
               double percentage = results.getDouble("percentage");


               ageClass age = new ageClass(name, lga_code, indi_status, age_category, count, percentage);
               ages.add(age);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return ages;
   }


   // New method for sorted age data
   public ArrayList<ageClass> getPopulationAge_order(String order) {
       ArrayList<ageClass> ages = new ArrayList<ageClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);

           // Validate sort order parameter to prevent SQL injection
           String sortDirection = "ASC";  // Default
           if (order != null && order.equalsIgnoreCase("DESC")) {
               sortDirection = "DESC";
           }

           String query = 
                   "WITH RankedData AS (\r\n" +
                   "    SELECT \r\n" +
                   "        lga.name, \r\n" +
                   "        p.lga_code, \r\n" +
                   "        CASE \r\n" +
                   "            WHEN p.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
                   "            WHEN p.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
                   "            WHEN p.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
                   "            ELSE 'Unknown' \r\n" +
                   "        END AS indigenous_status, \r\n" +
                   "        CASE \r\n" +
                   "            WHEN p.age_category = '_0_4' THEN '0–4 years' \r\n" +
                   "            WHEN p.age_category = '_5_9' THEN '5–9 years' \r\n" +
                   "            WHEN p.age_category = '_10_14' THEN '10–14 years' \r\n" +
                   "            WHEN p.age_category = '_15_19' THEN '15–19 years' \r\n" +
                   "            WHEN p.age_category = '_20_24' THEN '20–24 years' \r\n" +
                   "            WHEN p.age_category = '_25_29' THEN '25–29 years' \r\n" +
                   "            WHEN p.age_category = '_30_34' THEN '30–34 years' \r\n" +
                   "            WHEN p.age_category = '_35_39' THEN '35–39 years' \r\n" +
                   "            WHEN p.age_category = '_40_44' THEN '40–44 years' \r\n" +
                   "            WHEN p.age_category = '_45_49' THEN '45–49 years' \r\n" +
                   "            WHEN p.age_category = '_50_54' THEN '50–54 years' \r\n" +
                   "            WHEN p.age_category = '_55_59' THEN '55–59 years' \r\n" +
                   "            WHEN p.age_category = '_60_64' THEN '60–64 years' \r\n" +
                   "            WHEN p.age_category = '_65_yrs_ov' THEN '65+ years' \r\n" +
                   "            ELSE 'Unknown' \r\n" +
                   "        END AS age_category, \r\n" +
                   "        SUM(p.count) AS total_count, \r\n" +
                   "        ROUND( \r\n" +
                   "            SUM(p.count) * 100.0 / SUM(SUM(p.count)) OVER (PARTITION BY p.lga_code), \r\n" +
                   "            2 \r\n" +
                   "        ) AS percentage,\r\n" +
                   "        DENSE_RANK() OVER (ORDER BY SUM(p.count) " + sortDirection + ") as rank\r\n" +
                   "    FROM \r\n" +
                   "        Population p \r\n" +
                   "    JOIN \r\n" +
                   "        LGA lga ON p.lga_code = lga.code AND p.lga_year = lga.year \r\n" +
                   "    GROUP BY \r\n" +
                   "        lga.name, p.lga_code, p.indigenous_status, p.age_category\r\n" +
                   ")\r\n" +
                   "SELECT * FROM RankedData\r\n" +
                   "ORDER BY total_count " + sortDirection + ", rank;";

           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String age_category = results.getString("age_category");
               int count = results.getInt("total_count");
               double percentage = results.getDouble("percentage");
               int rank = results.getInt("rank");

               ageClass age = new ageClass(name, lga_code, indi_status, age_category, count, percentage, rank);
               ages.add(age);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return ages;
   }


   ///////////////////////////////////////////////////////// cnay la hienn phan tram raw cua school completion
   public ArrayList<school_CompletionClass> getSchoolRaw() {
       ArrayList<school_CompletionClass> schoolCompletionClass = new ArrayList<school_CompletionClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);
           String query = String.format(
                   "SELECT lga.name AS lga_name, " +
                           "s.lga_code, " +
                           "CASE " +
                           "WHEN s.indigenous_status = 'indig' THEN 'Indigenous' " +
                           "WHEN s.indigenous_status = 'non_indig' THEN 'Non-indigenous' " +
                           "WHEN s.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' " +
                           "ELSE 'Unknown' " +
                           "END AS indigenous_status, " +
                           "CASE " +
                           "WHEN s.Category = 'did_not_go_to_school' THEN 'Did not attend School' " +
                           "WHEN s.Category = 'y8_below' THEN 'Year 8 or Below' " +
                           "WHEN s.Category = 'y9_equivalent' THEN 'Year 9 or Equivalent' " +
                           "WHEN s.Category = 'y10_equivalent' THEN 'Year 10 or Equivalent' " +
                           "WHEN s.Category = 'y11_equivalent' THEN 'Year 11 or Equivalent' " +
                           "WHEN s.Category = 'y12_equivalent' THEN 'Year 12 or Equivalent' " +
                           "ELSE 'Other' " +
                           "END AS category, " +
                           "SUM(s.count) AS total_count, " +
                           "ROUND(SUM(s.count) * 100.0 / SUM(SUM(s.count)) OVER (PARTITION BY s.lga_code), 2) AS percentage " +
                           "FROM School_completion s " +
                           "JOIN LGA lga ON s.lga_code = lga.code AND s.lga_year = lga.year " +
                           "GROUP BY lga.name, s.lga_code, s.indigenous_status, s.Category " +
                           "ORDER BY s.lga_code, indigenous_status, category;"
           );
           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("lga_name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");


               school_CompletionClass schoolCompletion = new school_CompletionClass(name, lga_code, indi_status, category, total_counts, percentage);
               schoolCompletionClass.add(schoolCompletion);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return schoolCompletionClass;
   }


   public ArrayList<school_CompletionClass> getSchoolRaw_order(String order) {
       ArrayList<school_CompletionClass> schoolCompletionClass = new ArrayList<school_CompletionClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);

           // Validate sort order parameter to prevent SQL injection
           String sortDirection = "ASC";  // Default
           if (order != null && order.equalsIgnoreCase("DESC")) {
               sortDirection = "DESC";
           }

           String query = String.format(
                   "WITH RankedData AS (\r\n" +
                   "    SELECT \r\n" +
                   "        lga.name AS lga_name, \r\n" +
                   "        s.lga_code, \r\n" +
                   "        CASE \r\n" +
                   "            WHEN s.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
                   "            WHEN s.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
                   "            WHEN s.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
                   "            ELSE 'Unknown' \r\n" +
                   "        END AS indigenous_status, \r\n" +
                   "        CASE \r\n" +
                   "            WHEN s.Category = 'did_not_go_to_school' THEN 'Did not attend School' \r\n" +
                   "            WHEN s.Category = 'y8_below' THEN 'Year 8 or Below' \r\n" +
                   "            WHEN s.Category = 'y9_equivalent' THEN 'Year 9 or Equivalent' \r\n" +
                   "            WHEN s.Category = 'y10_equivalent' THEN 'Year 10 or Equivalent' \r\n" +
                   "            WHEN s.Category = 'y11_equivalent' THEN 'Year 11 or Equivalent' \r\n" +
                   "            WHEN s.Category = 'y12_equivalent' THEN 'Year 12 or Equivalent' \r\n" +
                   "            ELSE 'Other' \r\n" +
                   "        END AS category, \r\n" +
                   "        SUM(s.count) AS total_count, \r\n" +
                   "        ROUND(SUM(s.count) * 100.0 / SUM(SUM(s.count)) OVER (PARTITION BY s.lga_code), 2) AS percentage,\r\n" +
                   "        DENSE_RANK() OVER (ORDER BY SUM(s.count) " + sortDirection + ") as rank\r\n" +
                   "    FROM \r\n" +
                   "        School_completion s \r\n" +
                   "    JOIN \r\n" +
                   "        LGA lga ON s.lga_code = lga.code AND s.lga_year = lga.year \r\n" +
                   "    GROUP BY \r\n" +
                   "        lga.name, s.lga_code, s.indigenous_status, s.Category\r\n" +
                   ")\r\n" +
                   "SELECT * FROM RankedData\r\n" +
                   "ORDER BY total_count " + sortDirection + ", rank;"
           );

           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("lga_name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");
               int rank = results.getInt("rank");

               school_CompletionClass schoolCompletion = new school_CompletionClass(name, lga_code, indi_status, category, total_counts, percentage, rank);
               schoolCompletionClass.add(schoolCompletion);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return schoolCompletionClass;
   }


   ///////////////////////////////////////////////////////// cnay show raw education level
   public ArrayList<Education_levelClass> getEducationLevelRaw() {
       ArrayList<Education_levelClass> educationLevels = new ArrayList<Education_levelClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);


           String query = "SELECT \r\n" + //
                   "    lga.name AS lga_name,\r\n" + //
                   "    e.lga_code,\r\n" + //
                   "    CASE \r\n" + //
                   "        WHEN e.indigenous_status = 'indig' THEN 'Indigenous' \r\n" + //
                   "        WHEN e.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" + //
                   "        WHEN e.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" + //
                   "        ELSE 'Unknown' \r\n" + //
                   "    END AS indigenous_status,\r\n" + //
                   "    CASE \r\n" + //
                   "        WHEN e.Category = 'pd_gd_gc' THEN 'Postgraduate Degree, Graduate Diploma and Graduate Certificate Level' \r\n" + //
                   "        WHEN e.Category = 'bd' THEN 'Bachelor Degree Level' \r\n" + //
                   "        WHEN e.Category = 'adip_dip' THEN 'Advanced Diploma and Diploma Level' \r\n" + //
                   "        WHEN e.Category = 'ct_iii_iv' THEN 'Certificate III & IV Level' \r\n" + //
                   "        WHEN e.Category = 'ct_i_ii' THEN 'Certificate I & II Level' \r\n" + //
                   "        ELSE 'Other' \r\n" + //
                   "    END AS category,\r\n" + //
                   "    SUM(e.count) AS total_count,\r\n" + //
                   "    ROUND(\r\n" + //
                   "        SUM(e.count) * 100.0 / SUM(SUM(e.count)) OVER (PARTITION BY e.lga_code),\r\n" + //
                   "        2\r\n" + //
                   "    ) AS percentage\r\n" + //
                   "FROM \r\n" + //
                   "    Education_level e\r\n" + //
                   "JOIN \r\n" + //
                   "    LGA lga ON e.lga_code = lga.code AND e.lga_year = lga.year\r\n" + //
                   "GROUP BY \r\n" + //
                   "    lga.name, e.lga_code, e.indigenous_status, e.Category\r\n" + //
                   "ORDER BY \r\n" + //
                   "    e.lga_code;";


           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("lga_name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");


               Education_levelClass educationLevel = new Education_levelClass(name, lga_code, indi_status, category, total_counts, percentage);
               educationLevels.add(educationLevel);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return educationLevels;
   }


   public ArrayList<Education_levelClass> getEducationLevelRaw_order(String order) {
       ArrayList<Education_levelClass> educationLevels = new ArrayList<Education_levelClass>();
       try {
           // Connect to JDBC data base
           Connection connection = DriverManager.getConnection(DATABASE);
           Statement statement = connection.createStatement();
           statement.setQueryTimeout(30);

           // Validate sort order parameter to prevent SQL injection
           String sortDirection = "ASC";  // Default
           if (order != null && order.equalsIgnoreCase("DESC")) {
               sortDirection = "DESC";
           }

           String query = "WITH RankedData AS (\r\n" +
                   "    SELECT \r\n" +
                   "        lga.name AS lga_name,\r\n" +
                   "        e.lga_code,\r\n" +
                   "        CASE \r\n" +
                   "            WHEN e.indigenous_status = 'indig' THEN 'Indigenous' \r\n" +
                   "            WHEN e.indigenous_status = 'non_indig' THEN 'Non-indigenous' \r\n" +
                   "            WHEN e.indigenous_status = 'indig_stat_notstated' THEN 'Not Stated' \r\n" +
                   "            ELSE 'Unknown' \r\n" +
                   "        END AS indigenous_status,\r\n" +
                   "        CASE \r\n" +
                   "            WHEN e.Category = 'pd_gd_gc' THEN 'Postgraduate Degree, Graduate Diploma and Graduate Certificate Level' \r\n" +
                   "            WHEN e.Category = 'bd' THEN 'Bachelor Degree Level' \r\n" +
                   "            WHEN e.Category = 'adip_dip' THEN 'Advanced Diploma and Diploma Level' \r\n" +
                   "            WHEN e.Category = 'ct_iii_iv' THEN 'Certificate III & IV Level' \r\n" +
                   "            WHEN e.Category = 'ct_i_ii' THEN 'Certificate I & II Level' \r\n" +
                   "            ELSE 'Other' \r\n" +
                   "        END AS category,\r\n" +
                   "        SUM(e.count) AS total_count,\r\n" +
                   "        ROUND(\r\n" +
                   "            SUM(e.count) * 100.0 / SUM(SUM(e.count)) OVER (PARTITION BY e.lga_code),\r\n" +
                   "            2\r\n" +
                   "        ) AS percentage,\r\n" +
                   "        DENSE_RANK() OVER (ORDER BY SUM(e.count) " + sortDirection + ") as rank\r\n" +
                   "    FROM \r\n" +
                   "        Education_level e\r\n" +
                   "    JOIN \r\n" +
                   "        LGA lga ON e.lga_code = lga.code AND e.lga_year = lga.year\r\n" +
                   "    GROUP BY \r\n" +
                   "        lga.name, e.lga_code, e.indigenous_status, e.Category\r\n" +
                   ")\r\n" +
                   "SELECT * FROM RankedData\r\n" +
                   "ORDER BY total_count " + sortDirection + ", rank;";

           ResultSet results = statement.executeQuery(query);
           while (results.next()) {
               String name = results.getString("lga_name");
               int lga_code = results.getInt("lga_code");
               String indi_status = results.getString("indigenous_status");
               String category = results.getString("category");
               int total_counts = results.getInt("total_count");
               double percentage = results.getDouble("percentage");
               int rank = results.getInt("rank");

               Education_levelClass educationLevel = new Education_levelClass(name, lga_code, indi_status, category, total_counts, percentage, rank);
               educationLevels.add(educationLevel);
           }
           statement.close();
       } catch (SQLException e) {
           // If there is an error, lets just print the error
           System.err.println(e.getMessage());
       }
       return educationLevels;
   }
}
   ///////////////////////////////////////////////////////// sorting  Education_level (orderBy)